package com.example.homebankfront.feature.transactionHeadsList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.feature.transactionHeadsList.domain.GetCustomerAndTransactionHeadsUseCase
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.Error
import com.example.homebankfront.feature.utility.EventEmitter
import com.example.homebankfront.feature.utility.NetworkError
import com.example.homebankfront.feature.utility.ResultGeneric
import com.example.homebankfront.feature.utility.ResultGeneric.*
import com.example.homebankfront.feature.utility.logError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionHeadsListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val networkErrorEmitter: EventEmitter<NetworkError>,
    private val getCustomerAndTransactionHeadsUseCase: GetCustomerAndTransactionHeadsUseCase
) : ViewModel() {
    private val customerId: Long = checkNotNull(savedStateHandle["customerId"])

    private val _transactionHeadsListState: MutableStateFlow<TransactionHeadsListState> =
        MutableStateFlow(TransactionHeadsListState.Loading)
    val transactionHeadsListState = _transactionHeadsListState.asStateFlow()

    private val _errorFlow = MutableSharedFlow<Either<Unit, Error>>(
        extraBufferCapacity = 10
    )
    val errorFlow = _errorFlow.asSharedFlow()

    init {
        observeNetworkErrors()
    }

    private fun observeNetworkErrors() = networkErrorEmitter.event.map {
        Either.Right(it)
    }.buffer(10).onEach {
        _errorFlow.emit(it)
    }.catch { e ->
        e.message?.let { logError(it) }
    }.launchIn(viewModelScope)

    fun getCustomerAndTransactionHeads() {
        _transactionHeadsListState.update { TransactionHeadsListState.Loading }

        viewModelScope.launch {
            getCustomerAndTransactionHeadsUseCase(customerId).let { result ->
                when (result) {
                    is Failure -> _errorFlow.emit(result.error)
                    is Success -> _transactionHeadsListState.update {
                        TransactionHeadsListState.Ready(
                            customer = result.data.customer,
                            transactionHeads = result.data.transactionHeads
                        )
                    }
                }
            }
        }
    }
}

