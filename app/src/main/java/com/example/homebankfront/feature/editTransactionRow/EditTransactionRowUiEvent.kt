package com.example.homebankfront.feature.editTransactionRow

sealed interface EditTransactionRowUiEvent {
    data class UpdateField(val field: EditTransactionRowField) : EditTransactionRowUiEvent
    data object Save : EditTransactionRowUiEvent
}


