package com.example.homebankfront.feature.editTransactionHead

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.data.repositories.TransactionHeadRepository
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadField.BorrowerField
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadField.DescriptionField
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadField.EndDateField
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadField.LenderField
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadField.PrelEndDateField
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadField.StartDateField
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadField.TransactionNameField
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadState.Loading
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadState.Input
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadState.Saved
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadUiEvent.Save
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadUiEvent.UpdateField
import com.example.homebankfront.feature.editTransactionRow.domain.GetCustomersAndTransactionHeadUseCase
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.Error
import com.example.homebankfront.feature.utility.ResultGeneric.Failure
import com.example.homebankfront.feature.utility.ResultGeneric.Success
import com.example.homebankfront.feature.utility.logError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EditTransactionHeadViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val transactionHeadRepository: TransactionHeadRepository,
    private val getCustomersAndTransactionHead: GetCustomersAndTransactionHeadUseCase,
) : ViewModel() {
    private val transactionHeadId: Long = checkNotNull(savedStateHandle["transactionHeadId"])

    private val _state: MutableStateFlow<EditTransactionHeadState> =
        MutableStateFlow(Loading)
    val state = _state.asStateFlow()

    private val _errorFlow = MutableSharedFlow<Either<EditTransactionHeadError, Error>>(
        extraBufferCapacity = 10
    )
    val errorFlow = _errorFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            try {
                when (val result = getCustomersAndTransactionHead(transactionHeadId)) {
                    is Failure -> TODO()
                    is Success -> _state.update {
                        val customers = result.data.customers
                        val transactionHead = result.data.transactionHead

                        transactionHead.toReady(customers = customers)
                    }
                }
            } catch (e: Exception) {
                e.message?.let { logError(it) }
            }
        }
    }

    fun onEvent(event: EditTransactionHeadUiEvent): Any = when (event) {
        is UpdateField -> updateField(event.field)
        is Save -> save()
    }

    private fun updateField(field: EditTransactionHeadField) = _state.value.let { currentState ->
        if (currentState is Input) {
            _state.update {
                when (field) {
                    is TransactionNameField -> currentState.copy(transactionNameField = field)
                    is BorrowerField -> currentState.copy(borrowerField = field)
                    is DescriptionField -> currentState.copy(descriptionField = field)
                    is EndDateField -> currentState.copy(endDateField = field)
                    is LenderField -> currentState.copy(lenderField = field)
                    is PrelEndDateField -> currentState.copy(prelEndDateField = field)
                    is StartDateField -> currentState.copy(startDateField = field)
                }
            }
        }
    }

    private fun save() = _state.value.let { currentState ->
        if (currentState is Input) {
            when (val validationResult = currentState.validate()) {
                is Failure -> _state.update { validationResult.error }
                is Success -> viewModelScope.launch {
                    val transactionHead = currentState.toTransactionHead()
                    when (val result = transactionHeadRepository.saveTransactionHead(transactionHead)) {
                        is Failure -> handleError(result.error)
                        is Success -> _state.update { Saved }
                    }
                }
            }
        }
    }

    private suspend fun handleError(error: Either<EditTransactionHeadError, Error>) = when (error) {
        is Either.Left -> TODO()
        is Either.Right -> _errorFlow.emit(error)
    }
}



