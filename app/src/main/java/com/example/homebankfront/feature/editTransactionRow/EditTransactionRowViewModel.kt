package com.example.homebankfront.feature.editTransactionRow

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.data.bodies.TransactionRow
import com.example.homebankfront.feature.editTransactionRow.domain.GetTransactionRowUseCase
import com.example.homebankfront.feature.editTransactionRow.domain.SaveTransactionRowUseCase
import com.example.homebankfront.feature.utility.Event
import com.example.homebankfront.feature.utility.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTransactionRowViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTransactionRow: GetTransactionRowUseCase,
    private val saveTransactionRowUseCase: SaveTransactionRowUseCase,
) : ViewModel() {
    private val transactionHeadId: Long = checkNotNull(savedStateHandle["transactionHeadId"])
    private val transactionRowId: Long = checkNotNull(savedStateHandle["transactionRowId"])

    private val _state: MutableStateFlow<EditTransactionRowState> =
        MutableStateFlow(EditTransactionRowState.Loading)
    val state = _state.asStateFlow()

    private val _snackbarFlow = MutableSharedFlow<Event<String>>()
    val snackbarFlow = _snackbarFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            try {
                _state.update {
                    EditTransactionRowState.Ready(
                        getTransactionRow(transactionHeadId, transactionRowId)
                    )
                }
            } catch (e: Exception) {
                e.message?.let { showSnackbar(message = it) }
            }
        }
    }

    fun onEvent(event: EditTransactionRowUiEvent) {
        when (event) {
            is EditTransactionRowUiEvent.UpdateField -> update(event.field)
            is EditTransactionRowUiEvent.Save -> saveTransactionRow()
        }
    }

    private fun update(field: EditTransactionRowField) = _state.update { currentState ->
        if (currentState is EditTransactionRowState.Ready) {
            var transactionRow = currentState.transactionRow

            transactionRow = when (field) {
                is EditTransactionRowField.Amount -> transactionRow.copy(amount = field.amount)
                is EditTransactionRowField.Description -> transactionRow.copy(description = field.description)
                is EditTransactionRowField.Name -> transactionRow.copy(name = field.name)
                is EditTransactionRowField.PaymentDate -> transactionRow.copy(paymentDate = field.paymentDate)
                is EditTransactionRowField.TypeOfTransaction -> transactionRow.copy(
                    typeOfTransactionCode = field.typeOfTransactionCode,
                    typeOfTransaction = field.typeOfTransaction
                )
            }

            currentState.copy(transactionRow = transactionRow)
        } else {
            currentState
        }
    }

    private fun saveTransactionRow() = viewModelScope.launch {
        try {
            _state.value.let { currentState ->
                if (currentState is EditTransactionRowState.Ready) {
                    val transactionRow = currentState.transactionRow

                    when (val result = saveTransactionRowUseCase(transactionRow)) {
                        is Result.Failure -> showSnackbar(result.message)
                        is Result.Success -> _state.update { EditTransactionRowState.Saved }
                    }
                }
            }
        } catch (e: Exception) {
            e.message?.let {
                showSnackbar(message = it)
            }
        }
    }

    private fun showSnackbar(message: String) = viewModelScope.launch {
        _snackbarFlow.emit(Event(message))
    }
}
