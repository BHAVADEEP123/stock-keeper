package com.stockkeeper.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.stockkeeper.app.data.api.RetrofitClient
import com.stockkeeper.app.data.db.StockKeeperDatabase
import com.stockkeeper.app.data.repository.StockRepository
import com.stockkeeper.app.ui.alarms.AlarmsViewModel
import com.stockkeeper.app.ui.alarms.CategoryDetailScreen
import com.stockkeeper.app.ui.alarms.StockDetailScreen
import com.stockkeeper.app.ui.home.HomeScreen
import com.stockkeeper.app.ui.splash.SplashScreen
import com.stockkeeper.app.ui.theme.AppColors
import com.stockkeeper.app.ui.theme.StockKeeperTheme

class MainActivity : ComponentActivity() {

    private val viewModel: AlarmsViewModel by viewModels {
        val db   = StockKeeperDatabase.getInstance(this)
        val repo = StockRepository(
            db.sectionDao(), db.stockDao(), db.priceHistoryDao(),
            RetrofitClient.getStockApiService()
        )
        AlarmsViewModel.Factory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StockKeeperTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = AppColors.Background) {
                    StockKeeperApp(viewModel)
                }
            }
        }
    }
}

@Composable
fun StockKeeperApp(viewModel: AlarmsViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {

        composable("splash") {
            SplashScreen(onComplete = {
                navController.navigate("home") {
                    popUpTo("splash") { inclusive = true }
                }
            })
        }

        composable("home") {
            HomeScreen(
                viewModel      = viewModel,
                onSectionClick = { sectionId -> navController.navigate("category/$sectionId") }
            )
        }

        composable(
            route = "category/{sectionId}",
            arguments = listOf(navArgument("sectionId") { type = NavType.IntType })
        ) { back ->
            val sectionId = back.arguments?.getInt("sectionId") ?: return@composable
            CategoryDetailScreen(
                sectionId    = sectionId,
                viewModel    = viewModel,
                onBack       = { navController.popBackStack() },
                onStockClick = { stockId -> navController.navigate("stock/$stockId") }
            )
        }

        composable(
            route = "stock/{stockId}",
            arguments = listOf(navArgument("stockId") { type = NavType.IntType })
        ) { back ->
            val stockId = back.arguments?.getInt("stockId") ?: return@composable
            StockDetailScreen(
                stockId   = stockId,
                viewModel = viewModel,
                onBack    = { navController.popBackStack() }
            )
        }
    }
}
