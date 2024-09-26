package com.example.homebankfront.dataAccess.bodies

data class CustomerTransactionHeadAndRows(
    val customer : Customer = Customer(),
    val transactionHead : TransactionHead = TransactionHead(),
    val transactionRows : List<TransactionRow> = emptyList()
)
