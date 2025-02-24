package com.example.homebankfront.feature.editTransactionHead

import com.example.homebankfront.data.bodies.Customer
import com.example.homebankfront.data.bodies.TransactionHead
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadError.BorrowerFieldError
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadError.BorrowerFieldError.MissingBorrowerError
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadError.LenderFieldError
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadError.LenderFieldError.MissingLenderError
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadError.StartDateFieldError
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadError.TransactionNameFieldError
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadError.TransactionNameFieldError.MissingTransactionNameError
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadField.BorrowerField
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadField.DescriptionField
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadField.EndDateField
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadField.LenderField
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadField.PrelEndDateField
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadField.StartDateField
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadField.TransactionNameField
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadState.Ready
import com.example.homebankfront.feature.utility.ResultGeneric
import com.example.homebankfront.feature.utility.ResultGeneric.*
import java.time.LocalDate
import java.time.LocalDateTime

sealed interface EditTransactionHeadState {
    data object Loading : EditTransactionHeadState

    data class Ready(
        val id: Long,
        val transactionNameField: TransactionNameField,
        val descriptionField: DescriptionField,
        val startDateField: StartDateField,
        val prelEndDateField: PrelEndDateField,
        val endDateField: EndDateField,
        val lenderField: LenderField,
        val borrowerField: BorrowerField,
        val customers: List<Customer> = emptyList(),
        val rowVersion: LocalDateTime?
    ) : EditTransactionHeadState {
        fun validate(): ResultGeneric<Unit, Ready> {
            val transactionNameFieldError = transactionNameField.validate()
            val lenderFieldError = lenderField.validate()
            val borrowerFieldError = borrowerField.validate()

            val errors = listOf(transactionNameFieldError, lenderFieldError, borrowerFieldError)

            val newState = copy(
                transactionNameField = transactionNameField.copy(error = transactionNameFieldError),
                lenderField = lenderField.copy(error = lenderFieldError),
                borrowerField = borrowerField.copy(error = borrowerFieldError)
            )

            return if (errors.any { it != null }) Failure(newState) else Success(Unit)
        }
    }

    data object Saved : EditTransactionHeadState
}

fun TransactionHead.toReady(customers: List<Customer>) = Ready(
    id = id,
    transactionNameField = TransactionNameField(transactionName = transactionName),
    descriptionField = DescriptionField(description = description),
    startDateField = StartDateField(startDate = startDate),
    prelEndDateField = PrelEndDateField(prelEndDate = prelEndDate),
    endDateField = EndDateField(endDate = endDate),
    lenderField = LenderField(
        lenderId = lenderId,
        lender = lender
    ),
    borrowerField = BorrowerField(
        borrowerId = borrowerId,
        borrower = borrower
    ),
    rowVersion = rowVersion,
    customers = customers
)

fun Ready.toTransactionHead() = TransactionHead(
    id = id,
    lenderId = lenderField.lenderId,
    borrowerId = borrowerField.borrowerId,
    transactionName = transactionNameField.transactionName,
    description = descriptionField.description,
    startDate = startDateField.startDate,
    prelEndDate = prelEndDateField.prelEndDate,
    endDate = endDateField.endDate,
    borrower = borrowerField.borrower,
    lender = lenderField.lender,
    rowVersion = rowVersion
)


sealed interface EditTransactionHeadField {
    data class TransactionNameField(
        val transactionName: String,
        val error: TransactionNameFieldError? = null
    ) : EditTransactionHeadField {
        fun validate() = if (transactionName.isBlank()) MissingTransactionNameError else null
    }

    data class DescriptionField(val description: String?) : EditTransactionHeadField

    data class StartDateField(
        val startDate: LocalDate,
        val error: StartDateFieldError? = null
    ) : EditTransactionHeadField

    data class PrelEndDateField(val prelEndDate: LocalDate?) : EditTransactionHeadField

    data class EndDateField(val endDate: LocalDate?) : EditTransactionHeadField

    data class LenderField(
        val lenderId: Long,
        val lender: String,
        val error: LenderFieldError? = null
    ) : EditTransactionHeadField {
        fun validate() = if (lender.isBlank()) MissingLenderError else null
    }

    data class BorrowerField(
        val borrowerId: Long,
        val borrower: String,
        val error: BorrowerFieldError? = null
    ) : EditTransactionHeadField {
        fun validate() = if (borrower.isBlank()) MissingBorrowerError else null
    }
}

