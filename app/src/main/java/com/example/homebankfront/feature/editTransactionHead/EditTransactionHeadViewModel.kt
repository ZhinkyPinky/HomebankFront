package com.example.homebankfront.feature.editTransactionHead

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homebankfront.data.bodies.TransactionHead
import com.example.homebankfront.feature.editTransactionHead.domain.SaveTransactionHeadUseCase
import com.example.homebankfront.feature.editTransactionRow.domain.GetCustomersAndTransactionHeadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject


@HiltViewModel
class EditTransactionHeadViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCustomersAndTransactionHeadUseCase: GetCustomersAndTransactionHeadUseCase,
    private val saveTransactionHeadUseCase: SaveTransactionHeadUseCase
) : ViewModel() {
    private val customerId: Long = checkNotNull(savedStateHandle["customerId"])
    private val transactionHeadId: Long = checkNotNull(savedStateHandle["transactionHeadId"])

    private val _editTransactionHeadState: MutableStateFlow<EditTransactionHeadState> =
        MutableStateFlow(EditTransactionHeadState.Loading)
    val editTransactionHeadState = _editTransactionHeadState.asStateFlow()

    private val _snackbarState = MutableSharedFlow<String>()
    val snackbarState = _snackbarState.asSharedFlow()

    init {
        getCustomerAndTransactionHead()
    }

    fun onEvent(event: EditTransactionHeadUiEvent) {
        when (event) {
            is EditTransactionHeadUiEvent.onLenderChange -> updateTransactionHead(
                lenderId = event.lenderId,
                lender = event.lender
            )

            is EditTransactionHeadUiEvent.onBorrowerChange -> updateTransactionHead(
                borrowerId = event.borrowerId,
                borrower = event.borrower
            )

            is EditTransactionHeadUiEvent.onTransactionNameChangeUi -> updateTransactionHead(
                transactionName = event.transactionName
            )

            is EditTransactionHeadUiEvent.onDescriptionChange -> updateTransactionHead(description = event.description)

            is EditTransactionHeadUiEvent.onStartDateChange -> updateTransactionHead(endDate = event.startDate)

            is EditTransactionHeadUiEvent.onPrelEndDateChange -> updateTransactionHead(endDate = event.prelEndDate)

            is EditTransactionHeadUiEvent.onEndDateChange -> updateTransactionHead(endDate = event.endDate)

            is EditTransactionHeadUiEvent.Save -> saveTransactionHead()
        }
    }

    private fun getCustomerAndTransactionHead() = viewModelScope.launch {
        getCustomersAndTransactionHeadUseCase(transactionHeadId).let { customersAndTransactionHead ->
            _editTransactionHeadState.update {
                EditTransactionHeadState.Ready(
                    transactionHead = customersAndTransactionHead.transactionHead,
                    customers = customersAndTransactionHead.customers
                )
            }
        }
    }

    private fun updateTransactionHead(
        transactionName: String? = null,
        description: String? = null,
        startDate: Long? = null,
        prelEndDate: Long? = null,
        endDate: Long? = null,
        lenderId: String? = null,
        lender: String? = null,
        borrowerId: String? = null,
        borrower: String? = null
    ) = _editTransactionHeadState.update { currentState ->
        if (currentState is EditTransactionHeadState.Ready) {
            var transactionHead = currentState.transactionHead

            transactionName?.let {
                transactionHead = transactionHead.copy(transactionName = transactionName)
            }

            description?.let {
                transactionHead = transactionHead.copy(description = description)
            }

            startDate?.let {
                transactionHead = transactionHead.copy(
                    startDate = Instant.ofEpochMilli(startDate).atZone(ZoneId.systemDefault())
                        .toLocalDate()
                )
            }

            prelEndDate?.let {
                transactionHead = transactionHead.copy(
                    prelEndDate = Instant.ofEpochMilli(prelEndDate)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                )
            }

            endDate?.let {
                transactionHead = transactionHead.copy(
                    endDate = Instant.ofEpochMilli(endDate).atZone(ZoneId.systemDefault())
                        .toLocalDate()
                )
            }

            if (lenderId?.toLongOrNull() != null && lender != null) {
                transactionHead = transactionHead.copy(
                    lenderId = lenderId.toLong(),
                    lender = lender
                )
            }

            if (borrowerId?.toLongOrNull() != null && borrower != null) {
                transactionHead = transactionHead.copy(
                    borrowerId = borrowerId.toLong(),
                    borrower = borrower
                )
            }

            currentState.copy(transactionHead = transactionHead)
        } else {
            currentState
        }
    }

    private fun saveTransactionHead() = viewModelScope.launch {
        try {
            _editTransactionHeadState.value.let { currentState ->
                if (currentState is EditTransactionHeadState.Ready) {
                    val transactionHead = currentState.transactionHead

                    if (validateTransactionHead(transactionHead)) {
                        saveTransactionHeadUseCase(transactionHead)
                        _editTransactionHeadState.update { EditTransactionHeadState.Saved }
                    }
                }
            }
        } catch (e: Exception) {
            e.message?.let { message ->
                showSnackbar(message)
            }
        }
    }

    private fun validateTransactionHead(transactionHead: TransactionHead): Boolean {
        if (transactionHead.transactionName.isNullOrBlank()) {
            showSnackbar("Titel saknas")
            return false
        }

        if (transactionHead.startDate == null) {
            showSnackbar("Startdatum saknas")
            return false
        }

        return true
    }

    private fun showSnackbar(message: String) = viewModelScope.launch {
        _snackbarState.emit(message)
    }
}

