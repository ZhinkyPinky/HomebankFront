package com.example.homebankfront.feature.editTransactionHead

sealed interface EditTransactionHeadUiEvent {
    data class onTransactionNameChangeUi(val transactionName: String) : EditTransactionHeadUiEvent

    data class onDescriptionChange(val description: String) : EditTransactionHeadUiEvent

    data class onStartDateChange(val startDate: Long) : EditTransactionHeadUiEvent

    data class onPrelEndDateChange(val prelEndDate: Long) : EditTransactionHeadUiEvent

    data class onEndDateChange(val endDate: Long) : EditTransactionHeadUiEvent

    data class onLenderChange(
        val lenderId: String,
        val lender: String
    ) : EditTransactionHeadUiEvent

    data class onBorrowerChange(
        val borrowerId: String,
        val borrower: String
    ) : EditTransactionHeadUiEvent

    data object Save : EditTransactionHeadUiEvent
}