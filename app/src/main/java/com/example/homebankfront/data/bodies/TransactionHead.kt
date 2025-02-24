package com.example.homebankfront.data.bodies

import java.time.LocalDate
import java.time.LocalDateTime

data class TransactionHead(
    val id: Long = -1L,
    val lenderId: Long = -1L,
    val borrowerId: Long = -1L,
    val transactionName: String = "",
    val description: String? = null,
    val startDate: LocalDate = LocalDate.now(),
    val prelEndDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val amount: Int = 0,
    val borrower: String = "",
    val lender: String = "",
    val rowVersion: LocalDateTime? = null,
)
