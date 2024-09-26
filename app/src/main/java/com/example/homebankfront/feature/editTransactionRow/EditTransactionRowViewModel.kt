package com.example.homebankfront.feature.editTransactionRow

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.dataAccess.bodies.TransactionRow
import com.example.homebankfront.feature.editTransactionRow.domain.GetTransactionRowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTransactionRowViewModel @Inject constructor(
    savedStateHandle : SavedStateHandle,
    private val getTransactionRowUseCase : GetTransactionRowUseCase,
    //private val saveTransactionRowUseCase : SaveTransactionRowUseCase
) : ViewModel() {
    private val transactionRowId : Long = checkNotNull(savedStateHandle["transactionRowId"])

    private val _editTransactionRowUiState : MutableStateFlow<EditTransactionRowUiState> = MutableStateFlow(EditTransactionRowUiState.Loading)
    val transactionRowUiState = _editTransactionRowUiState.asStateFlow()

    fun getTransactionRow() {
        _editTransactionRowUiState.update { EditTransactionRowUiState.Loading }

        viewModelScope.launch {
            getTransactionRowUseCase(transactionRowId).collect { transactionRow ->
                _editTransactionRowUiState.update {
                    EditTransactionRowUiState.Ready(
                        transactionRow = transactionRow
                    )
                }
            }
        }
    }

    fun onEvent() {

    }
}

sealed interface EditTransactionRowUiState {
    data object Loading : EditTransactionRowUiState
    data class Ready(
        val transactionRow : TransactionRow
    ) : EditTransactionRowUiState
}