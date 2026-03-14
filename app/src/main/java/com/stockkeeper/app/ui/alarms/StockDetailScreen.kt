package com.stockkeeper.app.ui.alarms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import com.stockkeeper.app.data.db.entity.PriceHistoryEntity
import com.stockkeeper.app.data.db.entity.StockEntity
import com.stockkeeper.app.ui.theme.AppColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun StockDetailScreen(
    stockId: Int,
    viewModel: AlarmsViewModel,
    onBack: () -> Unit
) {
    val stock by viewModel.getStockFlow(stockId).collectAsState(initial = null)
    val priceHistory by viewModel.getPriceHistory(stockId).collectAsState(initial = emptyList())

    stock?.let { s ->
        StockDetailContent(
            stock = s,
            priceHistory = priceHistory,
            onBack = onBack,
            onUpdateAlarm = { updated -> viewModel.updateStock(updated) }
        )
    } ?: Box(Modifier.fillMaxSize().background(AppColors.Background))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StockDetailContent(
    stock: StockEntity,
    priceHistory: List<PriceHistoryEntity>,
    onBack: () -> Unit,
    onUpdateAlarm: (StockEntity) -> Unit
) {
    val priceChange = stock.latestPrice - stock.previousPrice
    val pctChange   = if (stock.previousPrice > 0) (priceChange / stock.previousPrice) * 100 else 0.0
    val changeColor = when {
        priceChange > 0 -> AppColors.Green
        priceChange < 0 -> AppColors.Red
        else            -> AppColors.TextSecondary
    }

    var showAlarmSheet   by remember { mutableStateOf(false) }
    val alarmSheetState  = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
            .verticalScroll(rememberScrollState())
    ) {
        // ── Top bar ──────────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 16.dp, top = 20.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = AppColors.TextPrimary)
            }
            Spacer(modifier = Modifier.weight(1f))
            // Exchange pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(AppColors.SurfaceElevated)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(stock.exchange, color = AppColors.Gold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        // ── Name + price ─────────────────────────────────────────────────────
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = stock.displayName.ifBlank { stock.symbol },
                color = AppColors.TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stock.symbol.uppercase(),
                color = AppColors.TextSecondary,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            if (stock.latestPrice > 0) {
                Text(
                    text = "₹%.2f".format(stock.latestPrice),
                    color = AppColors.TextPrimary,
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Black
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "%+.2f".format(priceChange),
                        color = changeColor,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "(%+.2f%%)".format(pctChange),
                        color = changeColor,
                        fontSize = 15.sp
                    )
                    Text("today", color = AppColors.TextTertiary, fontSize = 12.sp)
                }
            } else {
                Text("Awaiting first price update…", color = AppColors.TextSecondary, fontSize = 15.sp)
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // ── Price chart ──────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(AppColors.Card)
        ) {
            if (priceHistory.size >= 2) {
                PriceLineChart(
                    prices = priceHistory.map { Pair(it.timestamp, it.price) },
                    lineColor = if (priceChange >= 0) AppColors.Green else AppColors.Red,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
                // Min/max labels
                val minPrice = priceHistory.minOf { it.price }
                val maxPrice = priceHistory.maxOf { it.price }
                Column(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text("H  ₹%.2f".format(maxPrice), color = AppColors.TextSecondary, fontSize = 10.sp)
                    Text("L  ₹%.2f".format(minPrice), color = AppColors.TextSecondary, fontSize = 10.sp)
                }
            } else {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Chart data loading…", color = AppColors.TextSecondary, fontSize = 14.sp)
                        Spacer(Modifier.height(4.dp))
                        Text("Prices update every 5 minutes", color = AppColors.TextTertiary, fontSize = 11.sp)
                    }
                }
            }
        }

        if (priceHistory.isNotEmpty()) {
            val lastTs = priceHistory.lastOrNull()?.timestamp ?: 0L
            Text(
                text = "Last updated: " + SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(lastTs)),
                color = AppColors.TextTertiary,
                fontSize = 11.sp,
                modifier = Modifier.padding(start = 20.dp, top = 6.dp)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))
        Divider(color = AppColors.Divider, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(modifier = Modifier.height(20.dp))

        // ── Alarm section ─────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Price Alarm", color = AppColors.TextPrimary, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                if (stock.alarmEnabled && stock.alarmPrice != null) {
                    val typeLabel = if (stock.alarmType == "BUY") "BUY below" else "SELL above"
                    Text(
                        "$typeLabel ₹%.2f".format(stock.alarmPrice),
                        color = AppColors.Gold,
                        fontSize = 13.sp
                    )
                } else {
                    Text("Not set", color = AppColors.TextSecondary, fontSize = 13.sp)
                }
            }
            IconButton(onClick = { showAlarmSheet = true }) {
                Icon(
                    imageVector = if (stock.alarmEnabled) Icons.Default.Notifications else Icons.Default.NotificationsOff,
                    contentDescription = "Edit alarm",
                    tint = if (stock.alarmEnabled) AppColors.Gold else AppColors.TextTertiary
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }

    if (showAlarmSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAlarmSheet = false },
            sheetState = alarmSheetState,
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
            AlarmEditSheet(
                stock = stock,
                onSave = { updated ->
                    onUpdateAlarm(updated)
                    showAlarmSheet = false
                },
                onDismiss = { showAlarmSheet = false }
            )
        }
    }
}

@Composable
private fun AlarmEditSheet(
    stock: StockEntity,
    onSave: (StockEntity) -> Unit,
    onDismiss: () -> Unit
) {
    var enabled       by remember { mutableStateOf(stock.alarmEnabled) }
    var alarmType     by remember { mutableStateOf(stock.alarmType ?: "BUY") }
    var alarmPriceStr by remember { mutableStateOf(stock.alarmPrice?.toString() ?: "") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .navigationBarsPadding()
            .imePadding()
    ) {
        Text(
            "Edit Alarm",
            color = AppColors.TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Enable alarm", color = AppColors.TextPrimary, fontSize = 16.sp)
            Switch(
                checked = enabled,
                onCheckedChange = { enabled = it },
                colors = SwitchDefaults.colors(checkedThumbColor = AppColors.Background, checkedTrackColor = AppColors.Gold)
            )
        }

        if (enabled) {
            Spacer(modifier = Modifier.height(20.dp))
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

            OutlinedTextField(
                value = alarmPriceStr,
                onValueChange = { alarmPriceStr = it },
                label = { Text("Target price (₹)", color = AppColors.TextSecondary) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                colors = credTextFieldColors()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val price = if (enabled) alarmPriceStr.toDoubleOrNull() else null
                onSave(
                    stock.copy(
                        alarmEnabled = enabled && price != null,
                        alarmType    = if (enabled) alarmType else null,
                        alarmPrice   = price
                    )
                )
            },
            enabled = !enabled || alarmPriceStr.toDoubleOrNull() != null,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColors.Gold,
                contentColor   = AppColors.Background
            )
        ) {
            Text("Save", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun PriceLineChart(
    prices: List<Pair<Long, Double>>,
    lineColor: Color,
    modifier: Modifier = Modifier
) {
    if (prices.size < 2) return

    Canvas(modifier = modifier) {
        val minY    = prices.minOf { it.second }.coerceAtLeast(0.0)
        val maxY    = prices.maxOf { it.second }
        val rangeY  = (maxY - minY).let { if (it == 0.0) 1.0 else it }
        val w       = size.width
        val h       = size.height

        fun xOf(i: Int)  = (i.toFloat() / (prices.size - 1)) * w
        fun yOf(v: Double) = h - ((v - minY) / rangeY * h).toFloat()

        val points = prices.indices.map { i -> Offset(xOf(i), yOf(prices[i].second)) }

        // Gradient fill under the line
        val fillPath = Path().apply {
            moveTo(points.first().x, h)
            points.forEach { lineTo(it.x, it.y) }
            lineTo(points.last().x, h)
            close()
        }
        drawPath(
            fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(lineColor.copy(alpha = 0.25f), Color.Transparent),
                startY = 0f,
                endY   = h
            )
        )

        // Draw line segments
        val linePath = Path().apply {
            moveTo(points.first().x, points.first().y)
            points.drop(1).forEach { lineTo(it.x, it.y) }
        }
        drawPath(linePath, color = lineColor, style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round))

        // Terminal dot
        points.last().let { drawCircle(lineColor, radius = 4.dp.toPx(), center = it) }
    }
}
