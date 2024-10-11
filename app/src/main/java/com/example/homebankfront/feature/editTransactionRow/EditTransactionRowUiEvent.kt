package com.example.homebankfront.feature.editTransactionRow

import com.example.homebankfront.data.bodies.TransactionRow
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

sealed interface EditTransactionRowUiEvent {
    data class UpdateField(val field: EditTransactionRowField) : EditTransactionRowUiEvent
    data object Save : EditTransactionRowUiEvent
}

sealed interface EditTransactionRowField {
    data class Name(val name: String) : EditTransactionRowField
    data class Amount(val amount: Int) : EditTransactionRowField
    data class PaymentDate(val paymentDate: LocalDate) : EditTransactionRowField
    data class TypeOfTransaction(
        val typeOfTransactionCode: TransactionRow.Type,
        val typeOfTransaction: String
    ) : EditTransactionRowField

    data class Description(val description: String) : EditTransactionRowField
}

fun changeName(
    onEvent: (EditTransactionRowUiEvent) -> Unit,
    name: String
) = onEvent(
    EditTransactionRowUiEvent.UpdateField(
        EditTransactionRowField.Name(
            name = name
        )
    )
)

fun changeAmount(
    onEvent: (EditTransactionRowUiEvent) -> Unit,
    amount: String
) = onEvent(
    EditTransactionRowUiEvent.UpdateField(
        EditTransactionRowField.Amount(
            amount = amount.toIntOrNull() ?: 0
        )
    )
)

fun changePaymentDate(
    onEvent: (EditTransactionRowUiEvent) -> Unit,
    paymentDate: Long?
) = paymentDate?.let {
    onEvent(
        EditTransactionRowUiEvent.UpdateField(
            EditTransactionRowField.PaymentDate(
                paymentDate = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
            )
        )
    )
}

fun changeTypeOfTransaction(
    onEvent: (EditTransactionRowUiEvent) -> Unit,
    typeOfTransactionCode: String
) = TransactionRow.Type.valueOf(typeOfTransactionCode).let {
    onEvent(
        EditTransactionRowUiEvent.UpdateField(
            EditTransactionRowField.TypeOfTransaction(
                typeOfTransactionCode = it,
                typeOfTransaction = it.value
            )
        )
    )
}

fun changeDescription(
    onEvent: (EditTransactionRowUiEvent) -> Unit,
    description: String
) = onEvent(
    EditTransactionRowUiEvent.UpdateField(
        EditTransactionRowField.Description(
            description = description
        )
    )
)


