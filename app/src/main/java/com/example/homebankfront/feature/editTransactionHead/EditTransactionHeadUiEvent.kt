package com.example.homebankfront.feature.editTransactionHead

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

sealed interface EditTransactionHeadUiEvent {
    data class UpdateField(val field: EditTransactionHeadField) : EditTransactionHeadUiEvent
    data object Save : EditTransactionHeadUiEvent
}


fun updateTransactionName(
    onEvent: (EditTransactionHeadUiEvent) -> Unit,
    transactionName: String
) =
    onEvent(
    EditTransactionHeadUiEvent.UpdateField(
        EditTransactionHeadField.TransactionName(
            transactionName = transactionName
        )
    )
)

fun updateLender(
    onEvent: (EditTransactionHeadUiEvent) -> Unit,
    lenderId: String,
    lender: String
) = lenderId.toLongOrNull()?.let {
    onEvent(
        EditTransactionHeadUiEvent.UpdateField(
            EditTransactionHeadField.Lender(
                lenderId = it,
                lender = lender
            )
        )
    )
}

fun updateBorrower(
    onEvent: (EditTransactionHeadUiEvent) -> Unit,
    borrowerId: String,
    borrower: String
) = borrowerId.toLongOrNull()?.let {
    onEvent(
        EditTransactionHeadUiEvent.UpdateField(
            EditTransactionHeadField.Borrower(
                borrowerId = it,
                borrower = borrower
            )
        )
    )
}

fun updateStartDate(
    onEvent: (EditTransactionHeadUiEvent) -> Unit,
    startDate: Long?
) = startDate?.let {
    onEvent(
        EditTransactionHeadUiEvent.UpdateField(
            EditTransactionHeadField.StartDate(
                startDate = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
            )
        )
    )
}

fun updatePrelEndDate(
    onEvent: (EditTransactionHeadUiEvent) -> Unit,
    prelEndDate: Long?
) = prelEndDate?.let {
    onEvent(
        EditTransactionHeadUiEvent.UpdateField(
            EditTransactionHeadField.PrelEndDate(
                prelEndDate = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
            )
        )
    )
}

fun updateEndDate(
    onEvent: (EditTransactionHeadUiEvent) -> Unit,
    endDate: Long?
) = endDate?.let {
    onEvent(
        EditTransactionHeadUiEvent.UpdateField(
            EditTransactionHeadField.EndDate(
                endDate = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
            )
        )
    )
}

fun updateDescription(
    onEvent: (EditTransactionHeadUiEvent) -> Unit,
    description: String
) = onEvent(
    EditTransactionHeadUiEvent.UpdateField(
        EditTransactionHeadField.Description(description = description)
    )
)


