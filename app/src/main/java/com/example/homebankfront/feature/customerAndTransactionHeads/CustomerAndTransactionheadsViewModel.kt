package com.example.homebankfront.feature.customerAndTransactionHeads

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.dataAccess.bodies.Customer
import com.example.homebankfront.dataAccess.bodies.TransactionHead
import com.example.homebankfront.feature.customerAndTransactionHeads.domain.GetCustomerAndTransactionHeadsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CustomerAndTransactionHeadsViewModel @Inject constructor(
    savedStateHandle : SavedStateHandle,
    private val getCustomerAndTransactionHeadsUseCase : GetCustomerAndTransactionHeadsUseCase
) : ViewModel() {
    private val customerId : Long = checkNotNull(savedStateHandle["customerId"])

    private val _customerAndTransactionHeadsUiState : MutableStateFlow<CustomerAndTransactionHeadsUiState> = MutableStateFlow(CustomerAndTransactionHeadsUiState.Loading)
    val customerAndTransactionHeadsUiState = _customerAndTransactionHeadsUiState.asStateFlow()

    fun getCustomerAndTransactionHeads() {
        _customerAndTransactionHeadsUiState.update { CustomerAndTransactionHeadsUiState.Loading }

        viewModelScope.launch {
            getCustomerAndTransactionHeadsUseCase(customerId).let { customerAndTransactionHeads ->
                _customerAndTransactionHeadsUiState.update { CustomerAndTransactionHeadsUiState.Ready(
                    customer = customerAndTransactionHeads.customer,
                    transactionHeads = customerAndTransactionHeads.transactionHeads
                ) }
            }
        }
    }

    fun onEvent() {}

}

sealed interface CustomerAndTransactionHeadsUiState {
    data object Loading : CustomerAndTransactionHeadsUiState
    data class Ready(
        val customer : Customer,
        val transactionHeads : List<TransactionHead>
    ) : CustomerAndTransactionHeadsUiState
}

