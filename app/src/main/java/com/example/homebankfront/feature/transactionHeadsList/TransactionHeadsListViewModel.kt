package com.example.homebankfront.feature.transactionHeadsList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.feature.transactionHeadsList.domain.GetCustomerAndTransactionHeadsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionHeadsListViewModel @Inject constructor(
    savedStateHandle : SavedStateHandle,
    private val getCustomerAndTransactionHeadsUseCase : GetCustomerAndTransactionHeadsUseCase
) : ViewModel() {
    private val customerId : Long = checkNotNull(savedStateHandle["customerId"])

    private val _transactionHeadsListState : MutableStateFlow<TransactionHeadsListState> = MutableStateFlow(TransactionHeadsListState.Loading)
    val transactionHeadsListState = _transactionHeadsListState.asStateFlow()

    fun getCustomerAndTransactionHeads() {
        _transactionHeadsListState.update { TransactionHeadsListState.Loading }

        viewModelScope.launch {
            getCustomerAndTransactionHeadsUseCase(customerId).let { customerAndTransactionHeads ->
                _transactionHeadsListState.update { TransactionHeadsListState.Ready(
                    customer = customerAndTransactionHeads.customer,
                    transactionHeads = customerAndTransactionHeads.transactionHeads
                ) }
            }
        }
    }
}

