package com.example.homebankfront.feature.editTransactionHead

import java.time.Instant
import java.time.ZoneId

sealed interface EditTransactionHeadUiEvent {
    data class UpdateField(val field: EditTransactionHeadField) : EditTransactionHeadUiEvent
    data object Save : EditTransactionHeadUiEvent
}


fun updateTransactionName(
    onEvent: (EditTransactionHeadUiEvent) -> Unit,
    transactionName: String
) = onEvent(
    EditTransactionHeadUiEvent.UpdateField(
        EditTransactionHeadField.TransactionNameField(
            transactionName = transactionName
        )
    )
)

fun updateLender(
    onEvent: (EditTransactionHeadUiEvent) -> Unit,
    lenderField: EditTransactionHeadField.LenderField
) = onEvent(EditTransactionHeadUiEvent.UpdateField(lenderField))

fun updateBorrower(
    onEvent: (EditTransactionHeadUiEvent) -> Unit,
    borrowerField: EditTransactionHeadField.BorrowerField
) = onEvent(EditTransactionHeadUiEvent.UpdateField(borrowerField))

fun updateStartDate(
    onEvent: (EditTransactionHeadUiEvent) -> Unit,
    startDate: Long?
) = startDate?.let {
    onEvent(
        EditTransactionHeadUiEvent.UpdateField(
            EditTransactionHeadField.StartDateField(
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
            EditTransactionHeadField.PrelEndDateField(
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
            EditTransactionHeadField.EndDateField(
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
        EditTransactionHeadField.DescriptionField(description = description)
    )
)


