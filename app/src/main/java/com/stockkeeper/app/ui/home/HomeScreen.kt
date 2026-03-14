package com.stockkeeper.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stockkeeper.app.ui.alarms.AlarmsScreen
import com.stockkeeper.app.ui.alarms.AlarmsViewModel
import com.stockkeeper.app.ui.portfolio.PortfolioScreen
import com.stockkeeper.app.ui.theme.AppColors

@Composable
fun HomeScreen(
    viewModel: AlarmsViewModel,
    onSectionClick: (Int) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(1) } // 0=Portfolio, 1=Alarms

    Scaffold(
        containerColor = AppColors.Background,
        bottomBar = {
            NavigationBar(
                containerColor = AppColors.Surface,
                contentColor = AppColors.TextSecondary,
                tonalElevation = 0.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick  = { selectedTab = 0 },
                    icon     = {
                        Icon(Icons.Default.PieChart, contentDescription = "Portfolio",
                            tint = if (selectedTab == 0) AppColors.Gold else AppColors.TextTertiary)
                    },
                    label    = {
                        Text(
                            "Portfolio",
                            color = if (selectedTab == 0) AppColors.Gold else AppColors.TextTertiary,
                            fontSize = 11.sp,
                            fontWeight = if (selectedTab == 0) FontWeight.SemiBold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor   = AppColors.Gold,
                        unselectedIconColor = AppColors.TextTertiary,
                        indicatorColor      = AppColors.Gold.copy(alpha = 0.12f)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick  = { selectedTab = 1 },
                    icon     = {
                        Icon(Icons.Default.Notifications, contentDescription = "Alarms",
                            tint = if (selectedTab == 1) AppColors.Gold else AppColors.TextTertiary)
                    },
                    label    = {
                        Text(
                            "Alarms",
                            color = if (selectedTab == 1) AppColors.Gold else AppColors.TextTertiary,
                            fontSize = 11.sp,
                            fontWeight = if (selectedTab == 1) FontWeight.SemiBold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor   = AppColors.Gold,
                        unselectedIconColor = AppColors.TextTertiary,
                        indicatorColor      = AppColors.Gold.copy(alpha = 0.12f)
                    )
                )
            }
        }
    ) { padding ->
        when (selectedTab) {
            0 -> PortfolioScreen(modifier = Modifier.padding(padding))
            1 -> AlarmsScreen(
                    viewModel      = viewModel,
                    modifier       = Modifier.padding(padding),
                    onSectionClick = onSectionClick
                )
        }
    }
}
