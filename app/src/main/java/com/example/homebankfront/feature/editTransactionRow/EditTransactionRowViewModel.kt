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

    private val _editTransactionRowState: MutableStateFlow<EditTransactionRowState> =
        MutableStateFlow(EditTransactionRowState.Loading)
    val editTransactionRowUiState = _editTransactionRowState.asStateFlow()

    init {
        getTransactionRow()
    }

    fun onEvent(event: EditTransactionRowEvent) {
        when (event) {
            is EditTransactionRowEvent.Update -> updateTransactionRow(event.transactionRow)
            is EditTransactionRowEvent.Save -> saveTransactionRow()
        }
    }

    private fun getTransactionRow() {
        viewModelScope.launch {
            try {
                _editTransactionRowState.update {
                    EditTransactionRowState.Ready(
                        getTransactionRowUseCase(transactionHeadId, transactionRowId)
                    )
                }
            } catch (e: Exception) {
                error(e.message)
            }
        }
    }

    private fun updateTransactionRow(transactionRow: TransactionRow) {
        _editTransactionRowState.update { currentState ->
            if (currentState is EditTransactionRowState.Ready) {
                currentState.copy(transactionRow)
            } else {
                currentState
            }
        }
    }

    private fun saveTransactionRow() {
        viewModelScope.launch {
            _editTransactionRowState.value.let { currentState ->
                if (currentState is EditTransactionRowState.Ready) {
                    try {
                        saveTransactionRowUseCase(currentState.transactionRow)
                        _editTransactionRowState.update { EditTransactionRowState.Saved }
                    } catch (e: Exception) {
                        error(e.message)
                    }
                }
            }
        }
    }

    private fun error(message: String?) {
        _editTransactionRowState.update {
            EditTransactionRowState.Error(
                message ?: "Unknown error"
            )
        }
    }
}
