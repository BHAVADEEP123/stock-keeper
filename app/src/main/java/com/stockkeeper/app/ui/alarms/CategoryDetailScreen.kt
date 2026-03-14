package com.stockkeeper.app.ui.alarms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stockkeeper.app.data.db.entity.StockEntity
import com.stockkeeper.app.ui.theme.AppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailScreen(
    sectionId: Int,
    viewModel: AlarmsViewModel,
    onBack: () -> Unit,
    onStockClick: (Int) -> Unit
) {
    val sectionName by produceState(initialValue = "Category") {
        value = viewModel.getSectionById(sectionId)?.name ?: "Category"
    }
    val stocks by viewModel.getStocksForSection(sectionId).collectAsState(initial = emptyList())
    var showAddSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        containerColor = AppColors.Background,
        topBar = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AppColors.Background)
                        .padding(start = 8.dp, end = 24.dp, top = 20.dp, bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = AppColors.TextPrimary)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text(sectionName, color = AppColors.TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "${stocks.size} ${if (stocks.size == 1) "stock" else "stocks"}",
                            color = AppColors.TextSecondary, fontSize = 13.sp
                        )
                    }
                }
                Divider(color = AppColors.Divider, thickness = 0.5.dp)
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddSheet = true },
                containerColor = AppColors.Gold,
                contentColor = AppColors.Background,
                shape = RoundedCornerShape(50)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Text("Add Stock", fontWeight = FontWeight.SemiBold)
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        if (stocks.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("No stocks yet", color = AppColors.TextSecondary, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Tap + to add a stock", color = AppColors.TextTertiary, fontSize = 13.sp)
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(
                    start = 20.dp, end = 20.dp,
                    top = innerPadding.calculateTopPadding() + 8.dp,
                    bottom = 100.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(stocks, key = { it.id }) { stock ->
                    StockRowCard(
                        stock = stock,
                        onClick = { onStockClick(stock.id) },
                        onDelete = { viewModel.deleteStock(stock) }
                    )
                }
            }
        }
    }

    if (showAddSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAddSheet = false },
            sheetState = sheetState,
            containerColor = AppColors.SurfaceElevated,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .width(40.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(AppColors.Divider)
                )
            }
        ) {
            AddStockSheet(
                onDismiss = { showAddSheet = false },
                onSave = { stock ->
                    viewModel.addStock(stock.copy(sectionId = sectionId))
                    showAddSheet = false
                }
            )
        }
    }
}

@Composable
private fun StockRowCard(
    stock: StockEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val priceChange = stock.latestPrice - stock.previousPrice
    val pctChange = if (stock.previousPrice > 0) (priceChange / stock.previousPrice) * 100 else 0.0
    val changeColor = when {
        priceChange > 0 -> AppColors.Green
        priceChange < 0 -> AppColors.Red
        else            -> AppColors.TextSecondary
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AppColors.Card)
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Exchange pill
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(AppColors.SurfaceElevated)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(stock.exchange, color = AppColors.Gold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stock.displayName.ifBlank { stock.symbol },
                color = AppColors.TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = stock.symbol.uppercase(),
                color = AppColors.TextSecondary,
                fontSize = 12.sp
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            if (stock.latestPrice > 0) {
                Text(
                    text = "₹%.2f".format(stock.latestPrice),
                    color = AppColors.TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "%+.2f%%".format(pctChange),
                    color = changeColor,
                    fontSize = 12.sp
                )
            } else {
                Text("—", color = AppColors.TextTertiary, fontSize = 15.sp)
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        // Alarm badge
        Icon(
            imageVector = if (stock.alarmEnabled) Icons.Default.NotificationsActive
                          else Icons.Default.NotificationsOff,
            contentDescription = null,
            tint = if (stock.alarmEnabled) AppColors.Gold else AppColors.TextTertiary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
            Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = AppColors.TextTertiary, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun AddStockSheet(
    onDismiss: () -> Unit,
    onSave: (StockEntity) -> Unit
) {
    var symbol      by remember { mutableStateOf("") }
    var displayName by remember { mutableStateOf("") }
    var exchange    by remember { mutableStateOf("NSE") }

    // Step 2: alarm prompt
    var step            by remember { mutableStateOf(1) }
    var setAlarm        by remember { mutableStateOf(false) }
    var alarmType       by remember { mutableStateOf("BUY") }
    var alarmPriceText  by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .navigationBarsPadding()
            .imePadding()
    ) {
        if (step == 1) {
            Text(
                "Add Stock",
                color = AppColors.TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Symbol
            OutlinedTextField(
                value = symbol,
                onValueChange = { symbol = it.uppercase() },
                label = { Text("Symbol  (e.g. RELIANCE)", color = AppColors.TextSecondary) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
                modifier = Modifier.fillMaxWidth(),
                colors = credTextFieldColors()
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Display name
            OutlinedTextField(
                value = displayName,
                onValueChange = { displayName = it },
                label = { Text("Display name (optional)", color = AppColors.TextSecondary) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = credTextFieldColors()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Exchange toggle
            Text("Exchange", color = AppColors.TextSecondary, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                listOf("NSE", "BSE").forEach { ex ->
                    val selected = exchange == ex
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (selected) AppColors.Gold else AppColors.Surface)
                            .clickable { exchange = ex }
                            .padding(horizontal = 24.dp, vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            ex,
                            color = if (selected) AppColors.Background else AppColors.TextSecondary,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (symbol.isNotBlank()) step = 2
                },
                enabled = symbol.isNotBlank(),
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.Gold,
                    contentColor = AppColors.Background
                )
            ) {
                Text("Next", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
        } else {
            // ── Step 2: alarm prompt ─────────────────────────────────────────
            Text(
                "Set a Price Alarm?",
                color = AppColors.TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Text(
                "Get notified when ${symbol.ifBlank { "this stock" }} hits your target",
                color = AppColors.TextSecondary,
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Yes/No toggle
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                listOf("Yes" to true, "No" to false).forEach { (label, value) ->
                    val selected = setAlarm == value
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (selected) AppColors.Gold else AppColors.Surface)
                            .clickable { setAlarm = value }
                            .padding(horizontal = 28.dp, vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            label,
                            color = if (selected) AppColors.Background else AppColors.TextSecondary,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            if (setAlarm) {
                Spacer(modifier = Modifier.height(20.dp))

                // BUY / SELL
                Text("Alert me to", color = AppColors.TextSecondary, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    listOf("BUY" to AppColors.Green, "SELL" to AppColors.Red).forEach { (type, color) ->
                        val selected = alarmType == type
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (selected) color.copy(alpha = 0.2f) else AppColors.Surface)
                                .clickable { alarmType = type }
                                .padding(horizontal = 28.dp, vertical = 12.dp)
                        ) {
                            Text(
                                type,
                                color = if (selected) color else AppColors.TextSecondary,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                val alarmHint = if (alarmType == "BUY") "Notify me when price drops to ₹" else "Notify me when price rises to ₹"
                OutlinedTextField(
                    value = alarmPriceText,
                    onValueChange = { alarmPriceText = it },
                    label = { Text(alarmHint, color = AppColors.TextSecondary) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    colors = credTextFieldColors()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val alarmPrice = if (setAlarm) alarmPriceText.toDoubleOrNull() else null
                    onSave(
                        StockEntity(
                            sectionId = 0, // will be overridden in caller
                            symbol = symbol.trim(),
                            exchange = exchange,
                            displayName = displayName.trim(),
                            alarmEnabled = setAlarm && alarmPrice != null,
                            alarmType = if (setAlarm) alarmType else null,
                            alarmPrice = alarmPrice,
                            isActive = true
                        )
                    )
                },
                enabled = !setAlarm || alarmPriceText.toDoubleOrNull() != null,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.Gold,
                    contentColor = AppColors.Background
                )
            ) {
                Text("Save Stock", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun credTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor   = AppColors.Gold,
    unfocusedBorderColor = AppColors.Divider,
    focusedTextColor     = AppColors.TextPrimary,
    unfocusedTextColor   = AppColors.TextPrimary,
    cursorColor          = AppColors.Gold,
    focusedLabelColor    = AppColors.Gold,
    unfocusedLabelColor  = AppColors.TextSecondary
)
