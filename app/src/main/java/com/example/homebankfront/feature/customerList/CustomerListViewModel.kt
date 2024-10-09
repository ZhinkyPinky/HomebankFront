package com.example.homebankfront.feature.customerList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.feature.customerList.domain.GetCustomersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerListViewModel @Inject constructor(
    private val getCustomersUseCase: GetCustomersUseCase
) : ViewModel() {
    private val _customerListState: MutableStateFlow<CustomerListState> =
        MutableStateFlow(CustomerListState.Loading)
    val customerListState = _customerListState.asStateFlow()

    fun getCustomers() {
        _customerListState.update { CustomerListState.Loading }

        viewModelScope.launch {
            _customerListState.update {
                CustomerListState.Ready(
                    customers = getCustomersUseCase()
                )
            }
        }
    }
}
