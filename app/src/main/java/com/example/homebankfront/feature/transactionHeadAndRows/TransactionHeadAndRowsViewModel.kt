package com.example.homebankfront.feature.transactionHeadAndRows

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.feature.transactionHeadAndRows.domain.DeleteTransactionHeadUseCase
import com.example.homebankfront.feature.transactionHeadAndRows.domain.DeleteTransactionRowUseCase
import com.example.homebankfront.feature.transactionHeadAndRows.domain.GetCustomerTransactionHeadAndRowsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionHeadAndRowsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCustomerTransactionHeadAndRowsUseCase: GetCustomerTransactionHeadAndRowsUseCase,
    private val deleteTransactionHeadUseCase: DeleteTransactionHeadUseCase,
    private val deleteTransactionRowUseCase: DeleteTransactionRowUseCase
) : ViewModel() {
    private val customerId: Long = checkNotNull(savedStateHandle["customerId"])
    private val transactionHeadId: Long = checkNotNull(savedStateHandle["transactionHeadId"])

    private val _transactionHeadAndRowsState: MutableStateFlow<TransactionHeadAndRowsState> =
        MutableStateFlow(TransactionHeadAndRowsState.Loading)
    val transactionHeadAndRowsState = _transactionHeadAndRowsState.asStateFlow()

    fun onEvent(event: TransactionHeadAndRowsUiEvent) {
        when (event) {
            is TransactionHeadAndRowsUiEvent.DeleteTransactionHead -> viewModelScope.launch {
                deleteTransactionHeadUseCase(event.transactionHead)
            }.invokeOnCompletion {
                _transactionHeadAndRowsState.update {
                    TransactionHeadAndRowsState.Deleted
                }
            }

            is TransactionHeadAndRowsUiEvent.DeleteRow -> viewModelScope.launch {
                deleteTransactionRowUseCase(
                    event.transactionRow
                )
            }.invokeOnCompletion {
                getCustomerTransactionHeadAndRows()
            }
        }
    }

    fun getCustomerTransactionHeadAndRows() {
        _transactionHeadAndRowsState.update { TransactionHeadAndRowsState.Loading }

        viewModelScope.launch {
            getCustomerTransactionHeadAndRowsUseCase(
                customerId,
                transactionHeadId
            ).let { customerTransactionHeadAndRows ->
                try {
                    _transactionHeadAndRowsState.update {
                        TransactionHeadAndRowsState.Ready(
                            customer = customerTransactionHeadAndRows.customer,
                            transactionHead = customerTransactionHeadAndRows.transactionHead,
                            transactionRows = customerTransactionHeadAndRows.transactionRows
                        )
                    }
                } catch (e: Exception) {
                    error(e.message)
                }
            }
        }
    }

    private fun error(message: String?) {
        _transactionHeadAndRowsState.update {
            TransactionHeadAndRowsState.Error(message = message ?: "Unknown error")
        }
    }
}