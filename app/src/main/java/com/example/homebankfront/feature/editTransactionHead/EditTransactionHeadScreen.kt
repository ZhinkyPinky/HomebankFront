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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.homebankfront.LocalSnackHostState
import com.example.homebankfront.R
import com.example.homebankfront.data.bodies.Customer
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadField.BorrowerField
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadField.DescriptionField
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadField.EndDateField
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadField.LenderField
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadField.PrelEndDateField
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadField.StartDateField
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadField.TransactionNameField
import com.example.homebankfront.feature.utility.Either
import com.example.homebankfront.feature.utility.getStringResourceFromContext
import com.example.homebankfront.ui.components.DatePicker
import com.example.homebankfront.ui.components.LoadingOverlay
import com.example.homebankfront.ui.components.TextField
import com.example.homebankfront.ui.components.TextFieldWithDropdownMenu

@Composable
fun EditTransactionHeadScreen(
    viewModel: EditTransactionHeadViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state: EditTransactionHeadState by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = LocalSnackHostState.current

    LaunchedEffect(Unit) {
        viewModel.errorFlow.collect { error ->
            val errorMessage = when (error) {
                is Either.Left -> error.value.getStringResourceFromContext(context)
                is Either.Right -> error.value.getStringResourceFromContext(context)
            }

            snackbarHostState.showSnackbar(errorMessage)
        }
    }

    EditTransactionHeadScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick
    )
}

@Composable
fun EditTransactionHeadScreen(
    state: EditTransactionHeadState,
    snackbarHostState: SnackbarHostState,
    onEvent: (EditTransactionHeadUiEvent) -> Unit,
    onBackClick: () -> Unit
) {
    when (state) {
        is EditTransactionHeadState.Loading -> LoadingOverlay()
        is EditTransactionHeadState.Ready -> {
            EditTransactionHeadScreen(
                transactionHeadId = state.id,
                transactionNameField = state.transactionNameField,
                lenderField = state.lenderField,
                borrowerField = state.borrowerField,
                startDateField = state.startDateField,
                prelEndDateField = state.prelEndDateField,
                endDateField = state.endDateField,
                descriptionField = state.descriptionField,
                customers = state.customers,
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
    transactionHeadId: Long,
    transactionNameField: TransactionNameField,
    lenderField: LenderField,
    borrowerField: BorrowerField,
    startDateField: StartDateField,
    prelEndDateField: PrelEndDateField,
    endDateField: EndDateField,
    descriptionField: DescriptionField,
    customers: List<Customer>,
    snackbarHostState: SnackbarHostState,
    onEvent: (EditTransactionHeadUiEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(descriptionField.description) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (transactionHeadId) {
                            -1L -> stringResource(R.string.add)
                            else -> stringResource(R.string.edit)
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onEvent(EditTransactionHeadUiEvent.Save) }) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(R.string.save)
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
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .verticalScroll(scrollState)
        ) {
            TextField(label = stringResource(R.string.title),
                      text = transactionNameField.transactionName,
                      supportingText = transactionNameField.error?.toStringResource(),
                      isError = transactionNameField.error != null,
                      onValueChange = { updateTransactionName(onEvent, it) })

            TextFieldWithDropdownMenu(label = stringResource(R.string.lender),
                                      text = lenderField.lender,
                                      supportingText = lenderField.error?.toStringResource(),
                                      isError = lenderField.error != null,
                                      selectedKey = lenderField.lenderId.toString(),
                                      menuOptions = customers.associateBy(
                                          { it.id },
                                          { it.name }),
                                      onClick = { lenderId, lender ->
                                          updateLender(
                                              onEvent,
                                              lenderField.copy(
                                                  lenderId = lenderId,
                                                  lender = lender,
                                                  error = null
                                              )
                                          )
                                      }
            )

            TextFieldWithDropdownMenu(label = stringResource(R.string.borrower),
                                      text = borrowerField.borrower,
                                      supportingText = borrowerField.error?.toStringResource(),
                                      isError = borrowerField.error != null,
                                      selectedKey = borrowerField.borrowerId.toString(),
                                      menuOptions = customers.associateBy(
                                          { it.id },
                                          { it.name }),
                                      onClick = { borrowerId, borrower ->
                                          updateBorrower(
                                              onEvent,
                                              borrowerField.copy(
                                                  borrowerId = borrowerId,
                                                  borrower = borrower,
                                                  error = null
                                              )
                                          )
                                      }
            )

            DatePicker(
                label = stringResource(R.string.start_date),
                supportingText = startDateField.error?.toStringResource(),
                isError = startDateField.error != null,
                date = startDateField.startDate,
                onDateSelected = { it?.let { updateStartDate(onEvent, it) } },
            )

            DatePicker(
                label = stringResource(R.string.prel_end_date),
                date = prelEndDateField.prelEndDate,
                onDateSelected = { it?.let { updatePrelEndDate(onEvent, it) } },
            )

            DatePicker(
                label = stringResource(R.string.end_date),
                date = endDateField.endDate,
                onDateSelected = { updateEndDate(onEvent, it) }
            )

            TextField(
                label = stringResource(R.string.description),
                text = descriptionField.description ?: "",
                maxLines = 10,
                onValueChange = { updateDescription(onEvent, it) },
            )
        }
    }
}



