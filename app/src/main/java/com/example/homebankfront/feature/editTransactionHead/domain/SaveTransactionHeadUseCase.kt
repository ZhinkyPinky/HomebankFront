package com.example.homebankfront.feature.editTransactionHead.domain

import com.example.homebankfront.dataAccess.bodies.TransactionHead
import com.example.homebankfront.dataAccess.repositories.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class SaveTransactionHeadUseCase
@Inject constructor(
    private val repository : Repository
){
    suspend operator fun invoke(transactionHead : TransactionHead)  = repository.saveTransactionHead(transactionHead)
}