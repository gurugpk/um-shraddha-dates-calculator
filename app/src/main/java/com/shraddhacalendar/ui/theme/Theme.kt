package com.shraddhacalendar.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = PrimarySaffron,
    onPrimary = SurfaceCard,
    primaryContainer = SurfaceCardVariant,
    onPrimaryContainer = TextPrimary,
    secondary = SecondaryGold,
    onSecondary = SurfaceCard,
    background = BackgroundWarm,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceCardVariant,
    onSurfaceVariant = TextSecondary,
    outline = DividerColor
)

@Composable
fun ShraddhaCalendarTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
