package com.example.homebankfront.feature.editTransactionHead

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.dataAccess.bodies.Customer
import com.example.homebankfront.dataAccess.bodies.TransactionHead
import com.example.homebankfront.feature.editTransactionHead.domain.GetCustomerAndTransactionHeadUseCase
import com.example.homebankfront.feature.editTransactionHead.domain.SaveTransactionHeadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EditTransactionHeadViewModel @Inject constructor(
    savedStateHandle : SavedStateHandle,
    private val getCustomerAndTransactionHeadUseCase : GetCustomerAndTransactionHeadUseCase,
    private val saveTransactionHeadUseCase : SaveTransactionHeadUseCase
) : ViewModel() {
    private val customerId : Long = checkNotNull(savedStateHandle["customerId"])
    private val transactionHeadId : Long = checkNotNull(savedStateHandle["transactionHeadId"])

    private val _editTransactionHeadUiState : MutableStateFlow<EditTransactionHeadUiState> = MutableStateFlow(EditTransactionHeadUiState.Loading)
    val editTransactionHeadUiState = _editTransactionHeadUiState.asStateFlow()



    fun onEvent(event : EditTransactionHeadEvent) {
        when (event) {
            is EditTransactionHeadEvent.UpdateTransactionHead -> updateTransactionHead(event.transactionHead)
            is EditTransactionHeadEvent.SaveTransactionHead -> viewModelScope.launch {
                saveTransactionHeadUseCase(event.transactionHead) }
        }
    }

    fun getCustomerAndTransactionHead() {
        _editTransactionHeadUiState.update { EditTransactionHeadUiState.Loading }

        viewModelScope.launch {
            getCustomerAndTransactionHeadUseCase(
                customerId,
                transactionHeadId
            ).collect { customerAndTransactionHead ->
                _editTransactionHeadUiState.update {
                    EditTransactionHeadUiState.Ready(
                        customer = customerAndTransactionHead.customer,
                        transactionHead = customerAndTransactionHead.transactionHead
                    )
                }
            }
        }
    }

    private fun updateTransactionHead(transactionHead : TransactionHead) {
        _editTransactionHeadUiState.update { currentState ->
            if (currentState is EditTransactionHeadUiState.Ready) {
                currentState.copy(transactionHead = transactionHead)
            } else {
                currentState
            }
        }
    }

    private fun saveTransactionHead() {}
}

sealed interface EditTransactionHeadUiState {
    data object Loading : EditTransactionHeadUiState
    data class Ready(
        val customer : Customer = Customer(),
        val transactionHead : TransactionHead = TransactionHead()
    ) : EditTransactionHeadUiState

    data object Saved : EditTransactionHeadUiState
}

sealed interface EditTransactionHeadEvent {
    data class UpdateTransactionHead(
        val transactionHead : TransactionHead
    ) : EditTransactionHeadEvent

    data class SaveTransactionHead(
        val transactionHead : TransactionHead
    ) : EditTransactionHeadEvent
}
