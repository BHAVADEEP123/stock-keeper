package com.stockkeeper.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.stockkeeper.app.data.db.entity.SectionEntity
import com.stockkeeper.app.data.db.entity.StockEntity
import com.stockkeeper.app.ui.MainViewModel
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectionsScreen(
    viewModel: MainViewModel,
    onSectionClick: (Int) -> Unit,
    onAddSection: (String) -> Unit
) {
    val sectionsFlow: Flow<List<SectionEntity>> = viewModel.sections
    val sectionsState = sectionsFlow.collectAsState(initial = emptyList())
    val showDialog = remember { mutableStateOf(false) }
    val newSectionName = remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Sections") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog.value = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add section")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (sectionsState.value.isEmpty()) {
                Text(text = "No sections yet. Tap + to add one.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(sectionsState.value) { section ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSectionClick(section.id) }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = section.name)
                            }
                        }
                    }
                }
            }
        }

        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = { Text(text = "New section") },
                text = {
                    OutlinedTextField(
                        value = newSectionName.value,
                        onValueChange = { newSectionName.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Section name") }
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        val name = newSectionName.value.text.trim()
                        if (name.isNotEmpty()) {
                            onAddSection(name)
                            newSectionName.value = TextFieldValue("")
                            showDialog.value = false
                        }
                    }) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog.value = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectionDetailScreen(
    sectionId: Int,
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onEditStock: (Int) -> Unit,
    onAddStock: () -> Unit
) {
    val stocksFlow: Flow<List<StockEntity>> = viewModel.getStocksForSection(sectionId)
    val stocksState = stocksFlow.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Stocks") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddStock) {
                Icon(Icons.Default.Add, contentDescription = "Add stock")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (stocksState.value.isEmpty()) {
                Text(text = "No stocks in this section. Tap + to add one.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(stocksState.value) { stock ->
                        StockItemRow(
                            stock = stock,
                            onClick = { onEditStock(stock.id) },
                            onDelete = { viewModel.deleteStock(stock) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StockItemRow(
    stock: StockEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stock.displayName.ifBlank { stock.symbol }
                )
                Spacer(modifier = Modifier.padding(2.dp))
                Text(
                    text = "${stock.exchange} • Last: ${stock.latestPrice}"
                )
                if (stock.alertPrice != null && stock.alertDirection != null) {
                    Text(
                        text = "Alert ${stock.alertDirection} ${stock.alertPrice}"
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockEditorScreen(
    sectionId: Int,
    stockId: Int,
    viewModel: MainViewModel,
    onDone: () -> Unit
) {
    val symbolState = remember { mutableStateOf(TextFieldValue("")) }
    val displayNameState = remember { mutableStateOf(TextFieldValue("")) }
    val exchangeState: MutableState<String> = remember { mutableStateOf("NSE") }
    val alertPriceState = remember { mutableStateOf(TextFieldValue("")) }
    val alertDirectionState: MutableState<String?> = remember { mutableStateOf(null) }

    LaunchedEffect(stockId) {
        if (stockId != 0) {
            val existing = viewModel.getStockById(stockId)
            if (existing != null) {
                symbolState.value = TextFieldValue(existing.symbol)
                displayNameState.value = TextFieldValue(existing.displayName)
                exchangeState.value = existing.exchange
                alertPriceState.value = TextFieldValue(existing.alertPrice?.toString() ?: "")
                alertDirectionState.value = existing.alertDirection
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (stockId == 0) "Add stock" else "Edit stock"
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = symbolState.value,
                onValueChange = { symbolState.value = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Symbol") }
            )

            OutlinedTextField(
                value = displayNameState.value,
                onValueChange = { displayNameState.value = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Display name (optional)") }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ExchangeChip(
                    label = "NSE",
                    selected = exchangeState.value == "NSE",
                    onClick = { exchangeState.value = "NSE" }
                )
                ExchangeChip(
                    label = "BSE",
                    selected = exchangeState.value == "BSE",
                    onClick = { exchangeState.value = "BSE" }
                )
            }

            OutlinedTextField(
                value = alertPriceState.value,
                onValueChange = { alertPriceState.value = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Alert price (optional)") },
                keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DirectionChip(
                    label = "Above",
                    selected = alertDirectionState.value == "ABOVE",
                    onClick = { alertDirectionState.value = "ABOVE" }
                )
                DirectionChip(
                    label = "Below",
                    selected = alertDirectionState.value == "BELOW",
                    onClick = { alertDirectionState.value = "BELOW" }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val symbol = symbolState.value.text.trim()
                    if (symbol.isEmpty()) return@Button

                    val alertPrice = alertPriceState.value.text.toDoubleOrNull()
                    val stock = StockEntity(
                        id = stockId,
                        sectionId = sectionId,
                        symbol = symbol,
                        exchange = exchangeState.value,
                        displayName = displayNameState.value.text.trim(),
                        alertPrice = alertPrice,
                        alertDirection = alertPrice?.let { alertDirectionState.value },
                        isActive = true
                    )
                    viewModel.insertOrUpdateStock(stock)
                    onDone()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Save")
            }
        }
    }
}

@Composable
private fun ExchangeChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier,
        enabled = !selected
    ) {
        Text(text = label)
    }
}

@Composable
private fun DirectionChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = true
    ) {
        Text(text = label)
    }
}

