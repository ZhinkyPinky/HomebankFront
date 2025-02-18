package com.example.homebankfront.feature.editTransactionHead

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.feature.editTransactionHead.domain.SaveTransactionHeadUseCase
import com.example.homebankfront.feature.editTransactionRow.domain.GetCustomersAndTransactionHeadUseCase
import com.example.homebankfront.feature.utility.Event
import com.example.homebankfront.feature.utility.Result
import com.example.homebankfront.feature.utility.ResultGeneric
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
    private val getData: GetCustomersAndTransactionHeadUseCase,
    private val save: SaveTransactionHeadUseCase
) : ViewModel() {
    private val customerId: Long = checkNotNull(savedStateHandle["customerId"])
    private val transactionHeadId: Long = checkNotNull(savedStateHandle["transactionHeadId"])

    private val _state: MutableStateFlow<EditTransactionHeadState> =
        MutableStateFlow(EditTransactionHeadState.Loading)
    val state = _state.asStateFlow()

    private val _snackbarFlow = MutableSharedFlow<Event<String>>()
    val snackbarFlow = _snackbarFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            try {
                when (val result = getData(transactionHeadId)) {
                    is ResultGeneric.Failure -> TODO()
                    is ResultGeneric.Success -> _state.update {
                        EditTransactionHeadState.Ready(
                            transactionHead = result.data.transactionHead,
                            customers = result.data.customers
                        )
                    }
                }
            } catch (e: Exception) {
                e.message?.let { showSnackbar(it) }
            }
        }
    }

    fun onEvent(event: EditTransactionHeadUiEvent): Any = when (event) {
        is EditTransactionHeadUiEvent.UpdateField -> update(event.field)
        is EditTransactionHeadUiEvent.Save -> save()
    }

    private fun update(field: EditTransactionHeadField) = _state.update { currentState ->
        if (currentState is EditTransactionHeadState.Ready) {
            var transactionHead = currentState.transactionHead

            transactionHead = when (field) {
                is EditTransactionHeadField.TransactionName -> transactionHead.copy(transactionName = field.transactionName)
                is EditTransactionHeadField.Lender -> transactionHead.copy(
                    lenderId = field.lenderId,
                    lender = field.lender
                )

                is EditTransactionHeadField.Borrower -> transactionHead.copy(
                    borrowerId = field.borrowerId,
                    borrower = field.borrower
                )

                is EditTransactionHeadField.StartDate -> transactionHead.copy(startDate = field.startDate)
                is EditTransactionHeadField.PrelEndDate -> transactionHead.copy(prelEndDate = field.prelEndDate)
                is EditTransactionHeadField.EndDate -> transactionHead.copy(endDate = field.endDate)
                is EditTransactionHeadField.Description -> transactionHead.copy(description = field.description)
            }

            currentState.copy(transactionHead = transactionHead)
        } else {
            currentState
        }
    }

    private fun save() = viewModelScope.launch {
        try {
            _state.value.let { currentState ->
                if (currentState is EditTransactionHeadState.Ready) {
                    val transactionHead = currentState.transactionHead

                    when (val result = save(transactionHead)) {
                        is Result.Failure -> showSnackbar(result.message)
                        is Result.Success -> _state.update { EditTransactionHeadState.Saved }
                    }
                }
            }
        } catch (e: Exception) {
            e.message?.let { message ->
                showSnackbar(message)
            }
        }
    }

    private fun showSnackbar(message: String) = viewModelScope.launch {
        _snackbarFlow.emit(Event(message))
    }
}



