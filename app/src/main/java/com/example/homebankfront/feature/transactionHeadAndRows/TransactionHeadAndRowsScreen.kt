package com.example.homebankfront.feature.transactionHeadAndRows

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.homebankfront.R
import com.example.homebankfront.data.bodies.Customer
import com.example.homebankfront.data.bodies.TransactionHead
import com.example.homebankfront.data.bodies.TransactionRow
import com.example.homebankfront.ui.components.ConfirmationDialog
import com.example.homebankfront.ui.components.MoreDropDownMenu
import com.example.homebankfront.ui.components.TextWithLabel
import com.example.homebankfront.ui.theme.HomeBankFrontTheme
import com.example.homebankfront.ui.theme.ThemePreviews
import java.time.LocalDate

@Composable
internal fun TransactionHeadAndRowsRoute(
    viewModel: TransactionHeadAndRowsViewModel = hiltViewModel(),
    onEditTransactionHeadClick: (Long, Long) -> Unit,
    onEditTransactionRowClick: (Long, Long) -> Unit,
    onBackClick: () -> Unit
) {
    val transactionHeadAndRowsUiState: TransactionHeadAndRowsState by viewModel.transactionHeadAndRowsState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getCustomerTransactionHeadAndRows()
    }

    TransactionHeadAndRowsScreen(
        transactionHeadAndRowsState = transactionHeadAndRowsUiState,
        onEditTransactionClick = onEditTransactionHeadClick,
        onEditTransactionRowClick = onEditTransactionRowClick,
        onBackClick = onBackClick,
        onEvent = viewModel::onEvent
    )
}


@Composable
fun TransactionHeadAndRowsScreen(
    transactionHeadAndRowsState: TransactionHeadAndRowsState,
    onEditTransactionClick: (Long, Long) -> Unit,
    onEditTransactionRowClick: (Long, Long) -> Unit,
    onBackClick: () -> Unit,
    onEvent: (TransactionHeadAndRowsUiEvent) -> Unit
) {
    when (transactionHeadAndRowsState) {
        is TransactionHeadAndRowsState.Loading -> {}
        is TransactionHeadAndRowsState.Ready -> {
            TransactionHeadAndRowsScreen(
                customer = transactionHeadAndRowsState.customer,
                transactionHead = transactionHeadAndRowsState.transactionHead,
                transactionRows = transactionHeadAndRowsState.transactionRows,
                onEditTransactionHeadClick = onEditTransactionClick,
                onEditTransactionRowClick = onEditTransactionRowClick,
                onBackClick = onBackClick,
                onEvent = onEvent,
            )
        }

        is TransactionHeadAndRowsState.Deleted -> {
            onBackClick()
        }

        is TransactionHeadAndRowsState.Error -> {
            //TODO()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHeadAndRowsScreen(
    customer: Customer,
    transactionHead: TransactionHead,
    transactionRows: List<TransactionRow>,
    onEditTransactionHeadClick: (Long, Long) -> Unit,
    onEditTransactionRowClick: (Long, Long) -> Unit,
    onBackClick: () -> Unit,
    onEvent: (TransactionHeadAndRowsUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = customer.name) },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                },
                modifier = Modifier.padding(bottom = 1.dp)
            )
        },
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(1.dp),
            modifier = Modifier.padding(paddingValues)
        ) {
            TransactionHeadInfo(
                customer = customer,
                transactionHead = transactionHead,
                onEditTransactionHeadClick = onEditTransactionHeadClick,
                onEvent = onEvent
            )

            TransactionRowList(
                transactionHead = transactionHead,
                transactionRows = transactionRows,
                onEditTransactionRowClick = onEditTransactionRowClick,
                onEvent = onEvent
            )
        }
    }
}

@Composable
fun TransactionHeadInfo(
    customer: Customer,
    transactionHead: TransactionHead,
    onEditTransactionHeadClick: (Long, Long) -> Unit,
    onEvent: (TransactionHeadAndRowsUiEvent) -> Unit
) {
    TransactionHeadInfoTopBar(
        customer = customer,
        transactionHead = transactionHead,
        onEditTransactionHeadClick = onEditTransactionHeadClick,
        onEvent = onEvent
    )

    Surface {
        Column(modifier = Modifier.padding(12.dp)) {
            Row {
                Column(modifier = Modifier.weight(1f)) {
                    TextWithLabel(
                        label = stringResource(R.string.title),
                        text = transactionHead.transactionName ?: "",
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row {
                        TextWithLabel(
                            label = stringResource(R.string.lender),
                            text = transactionHead.lender ?: "",
                            modifier = Modifier.weight(1f)
                        )

                        TextWithLabel(
                            label = stringResource(R.string.borrower),
                            text = transactionHead.borrower ?: "",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    TextWithLabel(
                        label = stringResource(R.string.balance),
                        text = transactionHead.amount.toString(),
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    transactionHead.startDate?.let {
                        TextWithLabel(
                            label = stringResource(R.string.start_date),
                            text = it.toString(),
                            horizontalAlignment = Alignment.End,
                        )
                    }

                    transactionHead.prelEndDate?.let {
                        Spacer(modifier = Modifier.height(6.dp))
                        TextWithLabel(
                            label = stringResource(R.string.prel_end_date),
                            text = it.toString(),
                            horizontalAlignment = Alignment.End,
                        )
                    }

                    transactionHead.endDate?.let {
                        Spacer(modifier = Modifier.height(6.dp))
                        TextWithLabel(
                            label = stringResource(R.string.end_date),
                            text = it.toString(),
                            horizontalAlignment = Alignment.End
                        )
                    }
                }
            }

            transactionHead.description.takeIf { !it.isNullOrBlank() }?.let { description ->
                Spacer(modifier = Modifier.height(6.dp))
                TextWithLabel(
                    label = stringResource(R.string.description),
                    text = description,
                    textSoftWrap = true
                )
            }
        }
    }
}

@Composable
fun TransactionHeadInfoTopBar(
    customer: Customer,
    transactionHead: TransactionHead,
    onEditTransactionHeadClick: (Long, Long) -> Unit,
    onEvent: (TransactionHeadAndRowsUiEvent) -> Unit
) {
    val showDialog = rememberSaveable { mutableStateOf(false) }

    if (showDialog.value) {
        ConfirmationDialog(
            title = stringResource(R.string.remove),
            text = stringResource(R.string.remove_transaction_head_confirmation),
            confirmButtonText = stringResource(R.string.yes),
            onConfirm = { delete(onEvent, transactionHead) },
            onDismissRequest = { showDialog.value = false }
        )
    }

    Surface {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp)
        ) {
            Text(
                text = stringResource(R.string.transaction),
                style = MaterialTheme.typography.titleMedium
            )

            MoreDropDownMenu(
                map = mapOf(
                    stringResource(R.string.edit) to {
                        onEditTransactionHeadClick(
                            customer.id,
                            transactionHead.id
                        )
                    },
                    stringResource(R.string.remove) to { showDialog.value = true }
                )
            )
        }
    }
}

@Composable
fun TransactionRowList(
    transactionHead: TransactionHead,
    transactionRows: List<TransactionRow>,
    onEditTransactionRowClick: (Long, Long) -> Unit,
    onEvent: (TransactionHeadAndRowsUiEvent) -> Unit
) {
    TransactionRowListTopBar(
        transactionHead = transactionHead,
        onEditTransactionRowClick = onEditTransactionRowClick
    )

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        itemsIndexed(
            items = transactionRows,
            key = { _, transactionRow -> transactionRow.id }
        ) { _, transactionRow ->
            var expanded by rememberSaveable { mutableStateOf(false) }

            TransactionRowListItem(
                transactionHead = transactionHead,
                transactionRow = transactionRow,
                expanded = expanded,
                toggleExpanded = { expanded = !expanded },
                onEditTransactionRowClick = onEditTransactionRowClick,
                onEvent = onEvent
            )
        }
    }
}

@Composable
fun TransactionRowListTopBar(
    transactionHead: TransactionHead,
    onEditTransactionRowClick: (Long, Long) -> Unit
) {
    Surface(modifier = Modifier.wrapContentSize()) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp)
        ) {
            Text(
                text = stringResource(R.string.rows),
                style = MaterialTheme.typography.titleMedium
            )

            IconButton(
                onClick = { onEditTransactionRowClick(transactionHead.id, TransactionRow().id) }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "",
                )
            }
        }
    }
}


@Composable
fun TransactionRowListItem(
    transactionHead: TransactionHead,
    transactionRow: TransactionRow,
    expanded: Boolean = false,
    toggleExpanded: () -> Unit,
    onEditTransactionRowClick: (Long, Long) -> Unit,
    onEvent: (TransactionHeadAndRowsUiEvent) -> Unit
) {
    val showDialog = rememberSaveable { mutableStateOf(false) }

    if (showDialog.value) {
        ConfirmationDialog(
            title = stringResource(R.string.remove),
            text = stringResource(R.string.remove_transaction_row_confirmation),
            confirmButtonText = stringResource(R.string.yes),
            onConfirm = { delete(onEvent, transactionRow) },
            onDismissRequest = { showDialog.value = false }
        )
    }

    Surface(
        modifier = Modifier
            .wrapContentSize()
            .clickable { toggleExpanded() }
            .animateContentSize()
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "${transactionRow.transactionRowNo}",
                modifier = Modifier.align(Alignment.Top)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Row {
                    Column(modifier = Modifier.weight(2f)) {
                        TextWithLabel(
                            label = stringResource(R.string.title),
                            text = transactionRow.name,
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        TextWithLabel(
                            label = stringResource(R.string.type),
                            text = transactionRow.typeOfTransactionCode?.value ?: "",
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        TextWithLabel(
                            label = stringResource(R.string.date),
                            text = "${transactionRow.paymentDate}",
                            horizontalAlignment = Alignment.End,
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        TextWithLabel(
                            label = stringResource(R.string.amount),
                            text = "${transactionRow.amount}",
                            horizontalAlignment = Alignment.End,
                        )
                    }
                }

                if (expanded) {
                    transactionRow.description.takeIf { !it.isNullOrBlank() }?.let { description ->
                        Spacer(modifier = Modifier.height(6.dp))

                        TextWithLabel(
                            label = stringResource(R.string.description),
                            text = description,
                            textSoftWrap = true,
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(onClick = {
                            onEditTransactionRowClick(
                                transactionHead.id,
                                transactionRow.id
                            )
                        }) {
                            Text(text = stringResource(R.string.edit))
                        }

                        TextButton(onClick = {
                            showDialog.value = true
                        }) {
                            Text(text = stringResource(R.string.remove))
                        }
                    }
                }
            }
        }
    }
}

@ThemePreviews
@Composable
fun TransactionHeadInfoPreview() {
    val customer = Customer()
    val transactionHead = TransactionHead(
        transactionName = "Test",
        lender = "Test",
        borrower = "Test",
        startDate = LocalDate.now(),
        prelEndDate = LocalDate.now(),
        endDate = LocalDate.now(),
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
    )

    HomeBankFrontTheme {
        TransactionHeadInfo(
            customer = customer,
            transactionHead = transactionHead,
            onEditTransactionHeadClick = { _, _ -> },
            onEvent = {}
        )
    }
}

@ThemePreviews
@Composable
fun TransactionRowListItemPreview() {
    val transactionHead = TransactionHead()
    val transactionRow = TransactionRow(
        transactionRowNo = 1,
        name = "TestTestTestTestTestTestTest",
        typeOfTransactionCode = TransactionRow.Type.PAYBACK,
        amount = 555,
        paymentDate = LocalDate.now(),
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
    )

    var expanded by rememberSaveable { mutableStateOf(false) }

    HomeBankFrontTheme {
        TransactionRowListItem(
            transactionHead = transactionHead,
            transactionRow = transactionRow,
            expanded = expanded,
            toggleExpanded = { expanded = !expanded },
            onEditTransactionRowClick = { _, _ -> },
            onEvent = {}
        )
    }
}

@ThemePreviews
@Composable
fun TransactionRowListItemExpandedPreview() {
    val transactionHead = TransactionHead()
    val transactionRow = TransactionRow(
        transactionRowNo = 1,
        name = "TestTestTestTestTestTestTest",
        typeOfTransactionCode = TransactionRow.Type.PAYBACK,
        amount = 555,
        paymentDate = LocalDate.now(),
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
    )

    var expanded by rememberSaveable { mutableStateOf(true) }

    HomeBankFrontTheme {
        TransactionRowListItem(
            transactionHead = transactionHead,
            transactionRow = transactionRow,
            expanded = expanded,
            toggleExpanded = { expanded = !expanded },
            onEditTransactionRowClick = { _, _ -> },
            onEvent = {}
        )
    }
}
