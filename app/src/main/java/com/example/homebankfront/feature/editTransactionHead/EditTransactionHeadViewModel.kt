package com.example.homebankfront.feature.editTransactionHead

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.dataAccess.bodies.Customer
import com.example.homebankfront.dataAccess.bodies.TransactionHead
import com.example.homebankfront.feature.editTransactionHead.domain.SaveTransactionHeadUseCase
import com.example.homebankfront.feature.editTransactionRow.domain.GetCustomersAndTransactionHeadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EditTransactionHeadViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCustomersAndTransactionHeadUseCase: GetCustomersAndTransactionHeadUseCase,
    private val saveTransactionHeadUseCase: SaveTransactionHeadUseCase
) : ViewModel() {
    private val customerId: Long = checkNotNull(savedStateHandle["customerId"])
    private val transactionHeadId: Long = checkNotNull(savedStateHandle["transactionHeadId"])

    private val _editTransactionHeadState: MutableStateFlow<EditTransactionHeadState> =
        MutableStateFlow(EditTransactionHeadState.Loading)
    val editTransactionHeadState = _editTransactionHeadState.asStateFlow()

    init {
        getCustomerAndTransactionHead()
    }

    fun onEvent(event: EditTransactionHeadEvent) {
        when (event) {
            is EditTransactionHeadEvent.Update -> updateTransactionHead(event.transactionHead)
            is EditTransactionHeadEvent.Save -> viewModelScope.launch {
                saveTransactionHeadUseCase(event.transactionHead)
            }.invokeOnCompletion {
                _editTransactionHeadState.update {
                    EditTransactionHeadState.Saved
                }
            }
        }
    }

    private fun getCustomerAndTransactionHead() = viewModelScope.launch {
        getCustomersAndTransactionHeadUseCase(transactionHeadId).let { customersAndTransactionHead ->
            _editTransactionHeadState.update {
                EditTransactionHeadState.Ready(
                    transactionHead = customersAndTransactionHead.transactionHead,
                    customers = customersAndTransactionHead.customers
                )
            }
        }
    }

    private fun updateTransactionHead(transactionHead: TransactionHead) {
        _editTransactionHeadState.update { currentState ->
            if (currentState is EditTransactionHeadState.Ready) {
                currentState.copy(transactionHead = transactionHead)
            } else {
                currentState
            }
        }
    }
}

