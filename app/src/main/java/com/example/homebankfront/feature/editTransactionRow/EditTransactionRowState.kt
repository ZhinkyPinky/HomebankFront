package com.example.homebankfront.feature.editTransactionRow

import com.example.homebankfront.data.bodies.TransactionRow
import com.example.homebankfront.feature.editTransactionRow.EditTransactionRowField.AmountField
import com.example.homebankfront.feature.editTransactionRow.EditTransactionRowField.DescriptionField
import com.example.homebankfront.feature.editTransactionRow.EditTransactionRowField.NameField
import com.example.homebankfront.feature.editTransactionRow.EditTransactionRowField.PaymentDateField
import com.example.homebankfront.feature.editTransactionRow.EditTransactionRowField.TypeOfTransactionField
import com.example.homebankfront.feature.editTransactionRow.EditTransactionRowState.Ready
import com.example.homebankfront.feature.editTransactionRow.EditTransactionRowError.NameFieldError
import com.example.homebankfront.feature.utility.ResultGeneric
import com.example.homebankfront.feature.utility.ResultGeneric.*
import java.time.LocalDate
import java.time.LocalDateTime

sealed interface EditTransactionRowState {
    data object Loading : EditTransactionRowState
    data class Ready(
        val transactionHeadId: Long,
        val transactionRowId: Long,
        val transactionRowNo: Int,
        val transactionName: String?,
        val rowVersion: LocalDateTime?,
        val nameField: NameField,
        val amountField: AmountField,
        val paymentDateField: PaymentDateField,
        val typeOfTransactionField: TypeOfTransactionField,
        val descriptionField: DescriptionField
    ) : EditTransactionRowState {
        fun validate(): ResultGeneric<Unit, Ready> {
            val nameFieldError = nameField.validate()

            val errors = listOf(nameFieldError)

            val newState = copy(
                nameField = nameField.copy(error = nameFieldError)
            )

            return if (errors.any { it != null }) Failure(newState) else Success(Unit)
        }
    }

    data object Saved : EditTransactionRowState
}

fun Ready.toTransactionRow() = TransactionRow(
    id = transactionRowId,
    transactionHeadId = transactionHeadId,
    transactionRowNo = transactionRowNo,
    typeOfTransactionCode = typeOfTransactionField.typeOfTransactionCode,
    name = nameField.name,
    description = descriptionField.description,
    paymentDate = paymentDateField.paymentDate,
    amount = amountField.amount,
    transactionName = transactionName,
    typeOfTransaction = typeOfTransactionField.typeOfTransaction,
    rowVersion = rowVersion
)

fun TransactionRow.toReady(transactionHeadId: Long, transactionRowId: Long): Ready = Ready(
    transactionHeadId = transactionHeadId,
    transactionRowId = transactionRowId,
    transactionRowNo = transactionRowNo,
    transactionName = transactionName,
    rowVersion = rowVersion,
    nameField = NameField(name = name),
    amountField = AmountField(amount = amount),
    paymentDateField = PaymentDateField(paymentDate = paymentDate),
    typeOfTransactionField = TypeOfTransactionField(
        typeOfTransactionCode = typeOfTransactionCode,
        typeOfTransaction = typeOfTransaction
    ),
    descriptionField = DescriptionField(description = description)
)

sealed interface EditTransactionRowField {
    data class NameField(
        val name: String,
        val error: NameFieldError? = null
    ) : EditTransactionRowField {
        fun validate() = if (name.isBlank()) NameFieldError.MissingNameError else null
    }

    data class AmountField(val amount: Int) : EditTransactionRowField
    data class PaymentDateField(val paymentDate: LocalDate) : EditTransactionRowField
    data class TypeOfTransactionField(
        val typeOfTransactionCode: TransactionRow.Type,
        val typeOfTransaction: String?
    ) : EditTransactionRowField

    data class DescriptionField(val description: String?) : EditTransactionRowField
}

