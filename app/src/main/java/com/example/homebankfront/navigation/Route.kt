package com.example.homebankfront.navigation

import NewPasswordInput
import com.example.homebankfront.feature.accountrecovery.emailinput.AccountRecoveryEmailInput
import com.example.homebankfront.feature.accountrecovery.recoverypasswordinput.RecoveryPasswordInput
import com.example.homebankfront.feature.authentication.navigation.Authentication
import com.example.homebankfront.feature.customerList.navigation.CustomerList
import com.example.homebankfront.feature.editTransactionHead.navigation.EditTransactionHead
import com.example.homebankfront.feature.editTransactionRow.navigation.EditTransactionRow
import com.example.homebankfront.feature.registration.navigation.Registration
import com.example.homebankfront.feature.transactionHeadAndRows.navigation.TransactionHeadAndRows
import com.example.homebankfront.feature.transactionHeadsList.navigation.TransactionHeadsList

interface Route {
    val enableNavDrawer: Boolean
    val name: String

    companion object {
        fun fromString(route: String?): Route? = when (route) {
            Authentication.name -> Authentication
            CustomerList.name -> CustomerList
            EditTransactionHead(0).name -> EditTransactionHead(0)
            EditTransactionRow(0, 0).name -> EditTransactionRow(0, 0)
            Registration.name -> Registration
            TransactionHeadAndRows(0, 0).name -> TransactionHeadAndRows(0, 0)
            TransactionHeadsList(0).name -> TransactionHeadsList(0)
            AccountRecoveryEmailInput(null).name -> AccountRecoveryEmailInput(null)
            RecoveryPasswordInput("").name -> RecoveryPasswordInput("")
            NewPasswordInput("").name -> NewPasswordInput("")
            else -> null
        }
    }
}
