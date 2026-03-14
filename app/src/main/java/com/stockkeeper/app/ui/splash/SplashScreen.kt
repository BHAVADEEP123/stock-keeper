package com.stockkeeper.app.ui.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import com.stockkeeper.app.ui.theme.AppColors
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onComplete: () -> Unit) {
    var open by remember { mutableStateOf(false) }

    // Shutter top panel slides up, bottom panel slides down
    val topFraction by animateFloatAsState(
        targetValue = if (open) -1f else 0f,
        animationSpec = tween(durationMillis = 650, easing = FastOutSlowInEasing),
        label = "topShutter"
    )
    val bottomFraction by animateFloatAsState(
        targetValue = if (open) 1f else 0f,
        animationSpec = tween(durationMillis = 650, easing = FastOutSlowInEasing),
        label = "bottomShutter"
    )
    val contentAlpha by animateFloatAsState(
        targetValue = if (open) 0f else 1f,
        animationSpec = tween(300),
        label = "contentAlpha"
    )

    LaunchedEffect(Unit) {
        delay(1800)   // hold splash
        open = true
        delay(750)    // wait for animation to finish
        onComplete()
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // ── Top shutter panel ──────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .align(Alignment.TopCenter)
                .offset(y = (topFraction * 500).dp)
                .background(AppColors.Background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 20.dp)
                    .alpha(contentAlpha),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = "STOCK",
                    color = AppColors.Gold,
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 10.sp
                )
            }
        }

        // ── Bottom shutter panel ───────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .align(Alignment.BottomCenter)
                .offset(y = (bottomFraction * 500).dp)
                .background(AppColors.Background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp)
                    .alpha(contentAlpha),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "KEEPER",
                    color = AppColors.TextPrimary,
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Thin,
                    letterSpacing = 10.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "NSE  ·  BSE",
                    color = AppColors.TextTertiary,
                    fontSize = 11.sp,
                    letterSpacing = 5.sp
                )
            }
        }
    }
}
