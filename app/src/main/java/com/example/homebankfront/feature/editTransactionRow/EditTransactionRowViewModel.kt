package com.example.homebankfront.feature.editTransactionRow

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.data.repositories.TransactionRowRepository
import com.example.homebankfront.feature.editTransactionRow.EditTransactionRowField.AmountField
import com.example.homebankfront.feature.editTransactionRow.EditTransactionRowField.DescriptionField
import com.example.homebankfront.feature.editTransactionRow.EditTransactionRowField.NameField
import com.example.homebankfront.feature.editTransactionRow.EditTransactionRowField.PaymentDateField
import com.example.homebankfront.feature.editTransactionRow.EditTransactionRowField.TypeOfTransactionField
import com.example.homebankfront.feature.editTransactionRow.EditTransactionRowState.Loading
import com.example.homebankfront.feature.editTransactionRow.EditTransactionRowState.Input
import com.example.homebankfront.feature.editTransactionRow.EditTransactionRowState.Saved
import com.example.homebankfront.feature.editTransactionRow.domain.GetTransactionRowUseCase
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.Either.Left
import com.example.homebankfront.feature.utility.Either.Right
import com.example.homebankfront.feature.utility.Error
import com.example.homebankfront.feature.utility.EventEmitter
import com.example.homebankfront.feature.utility.NetworkError
import com.example.homebankfront.feature.utility.ResultGeneric.Failure
import com.example.homebankfront.feature.utility.ResultGeneric.Success
import com.example.homebankfront.feature.utility.logError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTransactionRowViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val networkErrorEmitter: EventEmitter<NetworkError>,
    private val getTransactionRow: GetTransactionRowUseCase,
    private val transactionRowRepository: TransactionRowRepository
) : ViewModel() {
    private val transactionHeadId: Long = checkNotNull(savedStateHandle["transactionHeadId"])
    private val transactionRowId: Long = checkNotNull(savedStateHandle["transactionRowId"])

    private val _state: MutableStateFlow<EditTransactionRowState> =
        MutableStateFlow(Loading)
    val state = _state.asStateFlow()

    private val _errorFlow = MutableSharedFlow<Either<EditTransactionRowError, Error>>(
        extraBufferCapacity = 10
    )
    val errorFlow = _errorFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            try {
                when (val result = getTransactionRow(transactionHeadId, transactionRowId)) {
                    is Failure -> TODO()
                    is Success -> _state.update {
                        val transactionRow = result.data

                        transactionRow.toReady(
                            transactionHeadId = transactionHeadId,
                            transactionRowId = transactionRowId
                        )
                    }
                }
            } catch (e: Exception) {
                e.message?.let { logError(it) }
            }
        }

        observeNetworkErrors()
    }

    private fun observeNetworkErrors() = networkErrorEmitter.event.map {
        Right(it)
    }.buffer(10).onEach {
        _errorFlow.emit(it)
    }.catch { e ->
        e.message?.let { logError(it) }
    }.launchIn(viewModelScope)

    fun onEvent(event: EditTransactionRowUiEvent) {
        when (event) {
            is EditTransactionRowUiEvent.UpdateField -> updateField(event.field)
            is EditTransactionRowUiEvent.Save -> saveTransactionRow()
        }
    }

    private fun updateField(field: EditTransactionRowField) = _state.value.let { currentState ->
        if (currentState is Input) {
            _state.update {
                when (field) {
                    is AmountField -> currentState.copy(amountField = field)
                    is DescriptionField -> currentState.copy(descriptionField = field)
                    is NameField -> currentState.copy(nameField = field)
                    is PaymentDateField -> currentState.copy(paymentDateField = field)
                    is TypeOfTransactionField -> currentState.copy(typeOfTransactionField = field)
                }
            }
        }
    }

    private fun saveTransactionRow() = _state.value.let { currentState ->
        if (currentState is Input) {
            when (val validationResult = currentState.validate()) {
                is Failure -> _state.update { validationResult.error }
                is Success -> viewModelScope.launch {
                    val transactionRow = currentState.toTransactionRow()

                    when (val result =
                        transactionRowRepository.saveTransactionRow(transactionRow)) {
                        is Failure -> handleError(result.error)
                        is Success -> _state.update { Saved }
                    }
                }
            }
        }
    }

    private suspend fun handleError(error: Either<EditTransactionRowError, Error>) = when (error) {
        is Left -> TODO()
        is Right -> _errorFlow.emit(error)
    }
}
