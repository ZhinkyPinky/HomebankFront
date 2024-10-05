package com.example.homebankfront.dataAccess.bodies

import java.time.LocalDate
import java.time.LocalDateTime

data class TransactionRow(
    val id: Long = -1L,
    val transactionHeadId: Long = -1L,
    val transactionRowNo: Int = -1,
    val typeOfTransactionCode: String? = null,
    val name: String = "",
    val description: String? = null,
    val paymentDate: LocalDate? = null,
    val amount: Int = 0,
    val transactionName: String? = null,
    val typeOfTransaction: String? = null,
    val rowVersion: LocalDateTime? = null,
) {
    enum class Type(val value: String) {
        LOAN("Lån"),
        PAYBACK("Återbetalning")
    }
}