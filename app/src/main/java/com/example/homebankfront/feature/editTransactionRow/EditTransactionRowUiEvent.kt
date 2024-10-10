package com.example.homebankfront.feature.editTransactionRow

sealed interface EditTransactionRowUiEvent {
    data class onNameChange(val name: String) : EditTransactionRowUiEvent

    data class onAmountChange(val amount: String) : EditTransactionRowUiEvent

    data class onPaymentDateChange(val paymentDate: Long?) : EditTransactionRowUiEvent

    data class onTypeOfTransactionChangeUi(val typeOfTransactionCode: String) : EditTransactionRowUiEvent

    data class onDescriptionChange(val description: String) : EditTransactionRowUiEvent

    data object Save : EditTransactionRowUiEvent
}