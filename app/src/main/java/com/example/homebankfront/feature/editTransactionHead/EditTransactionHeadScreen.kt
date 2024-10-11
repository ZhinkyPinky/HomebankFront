package com.example.homebankfront.feature.editTransactionHead

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.homebankfront.data.bodies.Customer
import com.example.homebankfront.data.bodies.TransactionHead
import com.example.homebankfront.ui.components.DatePicker
import com.example.homebankfront.ui.components.TextField
import com.example.homebankfront.ui.components.TextFieldWithDropdownMenu

@Composable
fun EditTransactionHeadRoute(
    viewModel: EditTransactionHeadViewModel = hiltViewModel(), onBackClick: () -> Unit
) {
    val editTransactionHeadState: EditTransactionHeadState by viewModel.state.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.snackbarFlow.collect { event ->
            event.consume()?.let { message ->
                snackbarHostState.showSnackbar(message = message)
            }
        }
    }

    EditTransactionHeadScreen(
        editTransactionHeadState = editTransactionHeadState,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick
    )
}

@Composable
fun EditTransactionHeadScreen(
    editTransactionHeadState: EditTransactionHeadState,
    snackbarHostState: SnackbarHostState,
    onEvent: (EditTransactionHeadUiEvent) -> Unit,
    onBackClick: () -> Unit
) {
    when (editTransactionHeadState) {
        is EditTransactionHeadState.Loading -> {}
        is EditTransactionHeadState.Ready -> {
            EditTransactionHeadScreen(
                transactionHead = editTransactionHeadState.transactionHead,
                customers = editTransactionHeadState.customers,
                snackbarHostState = snackbarHostState,
                onEvent = onEvent,
                onBackClick = onBackClick
            )
        }

        is EditTransactionHeadState.Saved -> {
            onBackClick()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionHeadScreen(
    transactionHead: TransactionHead,
    customers: List<Customer>,
    snackbarHostState: SnackbarHostState,
    onEvent: (EditTransactionHeadUiEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(transactionHead.description) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = if (transactionHead.id == -1L) "Lägg till" else "Redigera")
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackClick() },

                        ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onEvent(EditTransactionHeadUiEvent.Save) }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check, contentDescription = ""
                        )
                    }
                },
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .verticalScroll(scrollState)
        ) {
            TextField(label = "Titel",
                text = transactionHead.transactionName ?: "",
                onValueChange = { changeTransactionName(onEvent, it) })

            TextFieldWithDropdownMenu(label = "Långivare",
                text = transactionHead.lender ?: "",
                selectedKey = transactionHead.lenderId.toString(),
                menuOptions = customers.associateBy({ it.id.toString() }, { it.name }),
                onClick = { lenderId, lender -> changeLender(onEvent, lenderId, lender) }
            )

            TextFieldWithDropdownMenu(label = "Låntagare",
                text = transactionHead.borrower ?: "",
                selectedKey = transactionHead.borrowerId.toString(),
                menuOptions = customers.associateBy({ it.id.toString() }, { it.name }),
                onClick = { borrowerId, borrower -> changeBorrower(onEvent, borrowerId, borrower) }
            )

            DatePicker(
                label = "Startdatum",
                date = transactionHead.startDate,
                onDateSelected = { it?.let { changeStartDate(onEvent, it) } },
            )

            DatePicker(
                label = "Prel. Slutdatum",
                date = transactionHead.prelEndDate,
                onDateSelected = { it?.let { changePrelEndDate(onEvent, it) } },
            )

            DatePicker(
                label = "Slutdatum",
                date = transactionHead.endDate,
                onDateSelected = { changeEndDate(onEvent, it) }
            )

            TextField(
                label = "Beskrivning",
                text = transactionHead.description ?: "",
                maxLines = 10,
                onValueChange = { changeDescription(onEvent, it) },
            )
        }
    }
}



