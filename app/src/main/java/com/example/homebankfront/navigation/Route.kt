package com.example.homebankfront.navigation

import com.example.homebankfront.feature.authentication.navigation.Authentication
import com.example.homebankfront.feature.customerList.navigation.CustomerList
import com.example.homebankfront.feature.editTransactionHead.navigation.EditTransactionHead
import com.example.homebankfront.feature.editTransactionRow.navigation.EditTransactionRow
import com.example.homebankfront.feature.registration.navigation.Registration
import com.example.homebankfront.feature.transactionHeadAndRows.navigation.TransactionHeadAndRows
import com.example.homebankfront.feature.transactionHeadsList.navigation.TransactionHeadsList

interface Route {
    val enableNavDrawer: Boolean
    val route: String

    companion object {
        fun fromString(route: String?): Route? = when (route) {
            Authentication.route -> Authentication
            CustomerList.route -> CustomerList
            EditTransactionHead(0).route -> EditTransactionHead(0)
            EditTransactionRow(0, 0).route -> EditTransactionRow(0, 0)
            Registration.route -> Registration
            TransactionHeadAndRows(0,0).route -> TransactionHeadAndRows(0, 0)
            TransactionHeadsList(0).route -> TransactionHeadsList(0)
            else -> null
        }
    }
}
