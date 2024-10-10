package com.example.homebankfront.feature.editTransactionRow

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.data.bodies.TransactionRow
import com.example.homebankfront.feature.utility.Event
import com.example.homebankfront.feature.editTransactionRow.domain.GetTransactionRowUseCase
import com.example.homebankfront.feature.editTransactionRow.domain.SaveTransactionRowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class EditTransactionRowViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTransactionRowUseCase: GetTransactionRowUseCase,
    private val saveTransactionRowUseCase: SaveTransactionRowUseCase,
) : ViewModel() {
    private val transactionHeadId: Long = checkNotNull(savedStateHandle["transactionHeadId"])
    private val transactionRowId: Long = checkNotNull(savedStateHandle["transactionRowId"])

    private val _editTransactionRowState: MutableStateFlow<EditTransactionRowState> =
        MutableStateFlow(EditTransactionRowState.Loading)
    val editTransactionRowUiState = _editTransactionRowState.asStateFlow()

    private val _snackbarState = MutableSharedFlow<Event<String>>()
    val snackbarState = _snackbarState.asSharedFlow()


    init {
        getTransactionRow()
    }

    fun onEvent(event: EditTransactionRowUiEvent) {
        when (event) {
            is EditTransactionRowUiEvent.onNameChange -> updateTransactionRow(name = event.name)

            is EditTransactionRowUiEvent.onAmountChange -> updateTransactionRow(amount = event.amount)

            is EditTransactionRowUiEvent.onPaymentDateChange -> updateTransactionRow(paymentDate = event.paymentDate)

            is EditTransactionRowUiEvent.onTypeOfTransactionChangeUi -> updateTransactionRow(
                typeOfTransactionCode = event.typeOfTransactionCode,
            )

            is EditTransactionRowUiEvent.onDescriptionChange -> updateTransactionRow(description = event.description)

            is EditTransactionRowUiEvent.Save -> saveTransactionRow()

        }
    }

    private fun getTransactionRow() = viewModelScope.launch {
        try {
            _editTransactionRowState.update {
                EditTransactionRowState.Ready(
                    getTransactionRowUseCase(transactionHeadId, transactionRowId)
                )
            }
        } catch (e: Exception) {
            e.message?.let { message ->
                showSnackbar(message = message)
            }
        }
    }

    private fun updateTransactionRow(
        name: String? = null,
        amount: String? = null,
        paymentDate: Long? = null,
        typeOfTransactionCode: String? = null,
        description: String? = null,
    ) = _editTransactionRowState.update { currentState ->
        if (currentState is EditTransactionRowState.Ready) {
            var transactionRow = currentState.transactionRow

            name?.let {
                transactionRow = transactionRow.copy(name = name)
            }

            amount?.let {
                if (amount.toIntOrNull() != null) {
                    transactionRow = transactionRow.copy(amount = amount.toInt())
                }
            }

            paymentDate?.let {
                transactionRow = transactionRow.copy(
                    paymentDate = Instant.ofEpochMilli(paymentDate).atZone(ZoneId.systemDefault())
                        .toLocalDate()
                )
            }

            typeOfTransactionCode?.let {
                val typeOfTransaction = TransactionRow.Type.valueOf(typeOfTransactionCode)

                transactionRow = transactionRow.copy(
                    typeOfTransactionCode = typeOfTransaction,
                    typeOfTransaction = typeOfTransaction.value
                )
            }

            description?.let {
                transactionRow = transactionRow.copy(description = description)
            }

            currentState.copy(transactionRow = transactionRow)
        } else {
            currentState
        }
    }

    private fun saveTransactionRow() = viewModelScope.launch {
        _editTransactionRowState.value.let { currentState ->
            try {
                if (currentState is EditTransactionRowState.Ready) {
                    val transactionRow = currentState.transactionRow

                    if (validateTransactionRow(transactionRow)) {
                        saveTransactionRowUseCase(transactionRow)
                        _editTransactionRowState.update { EditTransactionRowState.Saved }
                    }
                }
            } catch (e: Exception) {
                e.message?.let {
                    showSnackbar(message = it)
                }
            }
        }
    }


    private fun validateTransactionRow(transactionRow: TransactionRow): Boolean {
        return if (transactionRow.name.isBlank()) {
            showSnackbar("Titel saknas")
            false
        } else if (transactionRow.paymentDate == null) {
            showSnackbar("Datum saknas")
            false
        } else if (transactionRow.typeOfTransactionCode == null || transactionRow.typeOfTransaction == null) {
            showSnackbar("Typ saknas")
            false
        } else {
            true
        }
    }

    private fun showSnackbar(message: String) = viewModelScope.launch {
        _snackbarState.emit(Event(message))
    }
}
