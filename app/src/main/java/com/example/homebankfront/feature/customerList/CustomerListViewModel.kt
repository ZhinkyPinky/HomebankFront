package com.example.homebankfront.feature.customerList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.dataAccess.bodies.Customer
import com.example.homebankfront.feature.customerList.domain.GetCustomersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerListViewModel @Inject constructor(
    private val getCustomersUseCase : GetCustomersUseCase
) : ViewModel() {
    private val _customerListUiState : MutableStateFlow<CustomerListUiState> = MutableStateFlow(CustomerListUiState.Loading)
    val customerListUiState = _customerListUiState.asStateFlow()

    fun getCustomers() {
        _customerListUiState.update { CustomerListUiState.Loading }

        viewModelScope.launch {
            getCustomersUseCase().collect { customers ->
                _customerListUiState.update {
                    CustomerListUiState.Ready(
                        customers = customers
                    )
                }
            }
        }
    }

    fun onEvent() {
        TODO("Not yet implemented")
    }
}

sealed interface CustomerListUiState {
    data object Loading : CustomerListUiState
    data class Ready(
        val customers : List<Customer>
    ) : CustomerListUiState
}