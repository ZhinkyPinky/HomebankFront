package com.example.homebankfront.data.remote.services

object ApiPaths {
    const val AUTH = "/auth"
    const val LOGIN = "$AUTH/login"
    const val LOGOUT = "$AUTH/logout"
    const val REGISTER = "$AUTH/register"
    const val REFRESH = "$AUTH/refresh"

    const val SAVE = "/save"
    const val DELETE = "/delete"

    const val TRANSACTION_ROWS = "/transactionRows"
    const val TRANSACTION_ROW = "$TRANSACTION_ROWS/{transactionRowId}"
    const val SAVE_TRANSACTION_ROW = TRANSACTION_ROWS + SAVE
    const val DELETE_TRANSACTION_ROW = TRANSACTION_ROWS + DELETE

    const val TRANSACTION_HEADS = "/transactionHeads"
    const val SAVE_TRANSACTION_HEAD = TRANSACTION_HEADS + SAVE
    const val DELETE_TRANSACTION_HEAD = TRANSACTION_HEADS + DELETE
    const val TRANSACTION_HEAD = "$TRANSACTION_HEADS/{transactionHeadId}"
    const val TRANSACTION_HEAD_AND_ROWS = TRANSACTION_HEAD + TRANSACTION_ROWS
    const val TRANSACTION_HEAD_AND_ROW = TRANSACTION_HEAD + TRANSACTION_ROW

    const val CUSTOMERS = "/customers"
    const val CUSTOMER = "$CUSTOMERS/{customerId}"
    const val CUSTOMER_AND_TRANSACTION_HEADS = CUSTOMER + TRANSACTION_HEADS
    const val CUSTOMER_AND_TRANSACTION_HEAD = CUSTOMER + TRANSACTION_HEAD
    const val CUSTOMERS_AND_TRANSACTION_HEAD = CUSTOMERS + TRANSACTION_HEAD
    const val CUSTOMER_TRANSACTION_HEAD_AND_ROWS =
        CUSTOMER_AND_TRANSACTION_HEAD + TRANSACTION_ROWS
    const val CUSTOMER_WITH_TRANSACTION_HEAD_AND_ROW =
        CUSTOMER_TRANSACTION_HEAD_AND_ROWS + TRANSACTION_ROW
}