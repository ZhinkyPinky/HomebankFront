package com.example.homebankfront.feature.customerList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.data.repositories.CustomerRepository
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.Either.Right
import com.example.homebankfront.feature.utility.Error
import com.example.homebankfront.feature.utility.EventEmitter
import com.example.homebankfront.feature.utility.Logger
import com.example.homebankfront.feature.utility.NetworkError
import com.example.homebankfront.feature.utility.ResultGeneric.Failure
import com.example.homebankfront.feature.utility.ResultGeneric.Success
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
class CustomerListViewModel @Inject constructor(
    private val customerRepository: CustomerRepository,
    private val networkErrorEmitter: EventEmitter<NetworkError>
) : ViewModel() {
    private val _state: MutableStateFlow<CustomerListState> =
        MutableStateFlow(CustomerListState())
    val state = _state.asStateFlow()

    private val _errorFlow = MutableSharedFlow<Either<CustomerError, Error>>(
        extraBufferCapacity = 10
    )
    val errorFlow = _errorFlow.asSharedFlow()

    init {
        observeNetworkEvents()
    }

    private fun observeNetworkEvents() = networkErrorEmitter.event.map {
        Right(it)
    }.buffer(10).onEach {
        _errorFlow.emit(it)
    }.catch { e ->
        e.message?.let { Logger.e(message = it) }
    }.launchIn(viewModelScope)

    fun getCustomers() {
        _state.update { currentState -> currentState.copy(isLoading = true) }

        viewModelScope.launch {
            _state.update { currentState ->
                when (val result = customerRepository.getCustomers()) {
                    is Failure -> {
                        //TODO: Specific errors for customer list????
                        _errorFlow.emit(result.error)
                        currentState.copy(isLoading = false)
                    }

                    is Success -> currentState.copy(customers = result.data, isLoading = false)
                }
            }
        }
    }
}