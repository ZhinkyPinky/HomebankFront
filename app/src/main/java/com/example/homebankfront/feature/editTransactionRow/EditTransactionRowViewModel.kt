package com.example.homebankfront.feature.editTransactionRow

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.dataAccess.bodies.TransactionRow
import com.example.homebankfront.feature.editTransactionRow.domain.GetTransactionRowUseCase
import com.example.homebankfront.feature.editTransactionRow.domain.SaveTransactionRowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTransactionRowViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTransactionRowUseCase: GetTransactionRowUseCase,
    private val saveTransactionRowUseCase: SaveTransactionRowUseCase,
) : ViewModel() {
    private val transactionHeadId: Long = checkNotNull(savedStateHandle["transactionHeadId"])
    private val transactionRowId: Long = checkNotNull(savedStateHandle["transactionRowId"])

    private val _editTransactionRowUiState: MutableStateFlow<EditTransactionRowUiState> =
        MutableStateFlow(EditTransactionRowUiState.Loading)
    val editTransactionRowUiState = _editTransactionRowUiState.asStateFlow()

    fun onEvent(event: EditTransactionRowEvent) {
        when (event) {
            is EditTransactionRowEvent.Update -> updateTransactionRow(event.transactionRow)
            is EditTransactionRowEvent.Save -> saveTransactionRow()
        }
    }

    fun getCustomersAndTransactionRow() {
        _editTransactionRowUiState.update { EditTransactionRowUiState.Loading }

        if (transactionRowId != TransactionRow().id) {
            viewModelScope.launch {
                getTransactionRowUseCase(transactionRowId).collect { transactionRow ->
                    _editTransactionRowUiState.update {
                        EditTransactionRowUiState.Ready(
                            transactionRow = transactionRow
                        )
                    }
                }
            }
        } else {
            _editTransactionRowUiState.update {
                EditTransactionRowUiState.Ready(
                    transactionRow = TransactionRow(
                        transactionHeadId = transactionHeadId,
                        typeOfTransactionCode = TransactionRow.Type.LOAN.name
                    )
                )
            }
        }
    }

    private fun updateTransactionRow(transactionRow: TransactionRow) {
        _editTransactionRowUiState.update { currentState ->
            if (currentState is EditTransactionRowUiState.Ready) {
                currentState.copy(transactionRow = transactionRow)
            } else {
                currentState
            }
        }
    }

    private fun saveTransactionRow() {
        viewModelScope.launch {
            _editTransactionRowUiState.value.let {
                if (it is EditTransactionRowUiState.Ready) {
                    saveTransactionRowUseCase(it.transactionRow)
                }
            }
        }.invokeOnCompletion {
            _editTransactionRowUiState.update {
                EditTransactionRowUiState.Saved
            }
        }
    }
}

sealed interface EditTransactionRowEvent {
    data class Update(val transactionRow: TransactionRow) : EditTransactionRowEvent
    data class Save(val transactionRow: TransactionRow) : EditTransactionRowEvent
}

sealed interface EditTransactionRowUiState {
    data object Loading : EditTransactionRowUiState
    data class Ready(val transactionRow: TransactionRow) : EditTransactionRowUiState
    data object Saved : EditTransactionRowUiState
}