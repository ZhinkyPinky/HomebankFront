package com.example.homebankfront.feature.customerTransactionHeadAndRows

import android.util.Printer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.dataAccess.bodies.Customer
import com.example.homebankfront.dataAccess.bodies.TransactionHead
import com.example.homebankfront.dataAccess.bodies.TransactionRow
import com.example.homebankfront.feature.customerTransactionHeadAndRows.domain.DeleteTransactionRowUseCase
import com.example.homebankfront.feature.customerTransactionHeadAndRows.domain.GetCustomerTransactionHeadAndRowsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerTransactionHeadAndRowsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCustomerTransactionHeadAndRowsUseCase: GetCustomerTransactionHeadAndRowsUseCase,
    private val deleteTransactionRowUseCase: DeleteTransactionRowUseCase
) : ViewModel() {
    private val customerId: Long = checkNotNull(savedStateHandle["customerId"])
    private val transactionHeadId: Long = checkNotNull(savedStateHandle["transactionHeadId"])

    private val _customerTransactionHeadAndRowsUiState: MutableStateFlow<CustomerTransactionHeadAndRowsUiState> =
        MutableStateFlow(CustomerTransactionHeadAndRowsUiState.Loading)
    val customerTransactionHeadAndRowsUiState = _customerTransactionHeadAndRowsUiState.asStateFlow()

    fun onEvent(event: TransactionHeadAndRowsEvent) {
        when (event) {
            is TransactionHeadAndRowsEvent.DeleteRow -> viewModelScope.launch {
                deleteTransactionRowUseCase(
                    event.transactionRow
                )
            }
        }
    }

    fun getCustomerTransactionHeadAndRows() {
        _customerTransactionHeadAndRowsUiState.update { CustomerTransactionHeadAndRowsUiState.Loading }

        viewModelScope.launch {
            getCustomerTransactionHeadAndRowsUseCase(
                customerId,
                transactionHeadId
            ).collect { customerTransactionHeadAndRows ->
                _customerTransactionHeadAndRowsUiState.update {
                    CustomerTransactionHeadAndRowsUiState.Ready(
                        customer = customerTransactionHeadAndRows.customer,
                        transactionHead = customerTransactionHeadAndRows.transactionHead,
                        transactionRows = customerTransactionHeadAndRows.transactionRows
                    )
                }
            }
        }
    }
}

sealed interface CustomerTransactionHeadAndRowsUiState {
    data object Loading : CustomerTransactionHeadAndRowsUiState
    data class Ready(
        val customer: Customer,
        val transactionHead: TransactionHead,
        val transactionRows: List<TransactionRow>
    ) : CustomerTransactionHeadAndRowsUiState
}

sealed interface TransactionHeadAndRowsEvent {
    data class DeleteRow(
        val transactionRow: TransactionRow
    ) : TransactionHeadAndRowsEvent
}