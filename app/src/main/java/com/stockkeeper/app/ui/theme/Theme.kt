package com.stockkeeper.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ── CRED-inspired dark palette ────────────────────────────────────────────────
object AppColors {
    val Background      = Color(0xFF0A0A0A)
    val Surface         = Color(0xFF111111)
    val SurfaceElevated = Color(0xFF1C1C1C)
    val Card            = Color(0xFF161616)
    val Gold            = Color(0xFFD4AF37)
    val GoldLight       = Color(0xFFE8C97A)
    val GoldDim         = Color(0xFF5C4A18)
    val TextPrimary     = Color(0xFFF5F5F5)
    val TextSecondary   = Color(0xFF888888)
    val TextTertiary    = Color(0xFF444444)
    val Divider         = Color(0xFF242424)
    val Green           = Color(0xFF00C48C)
    val Red             = Color(0xFFFF5252)
}

private val ColorScheme = darkColorScheme(
    primary            = AppColors.Gold,
    onPrimary          = Color(0xFF1A1A1A),
    primaryContainer   = AppColors.GoldDim,
    onPrimaryContainer = AppColors.GoldLight,
    secondary          = AppColors.GoldLight,
    onSecondary        = Color(0xFF1A1A1A),
    background         = AppColors.Background,
    onBackground       = AppColors.TextPrimary,
    surface            = AppColors.Surface,
    onSurface          = AppColors.TextPrimary,
    surfaceVariant     = AppColors.SurfaceElevated,
    onSurfaceVariant   = AppColors.TextSecondary,
    outline            = AppColors.Divider,
    error              = AppColors.Red,
    onError            = Color.White
)

@Composable
fun StockKeeperTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ColorScheme,
        content = content
    )
}
