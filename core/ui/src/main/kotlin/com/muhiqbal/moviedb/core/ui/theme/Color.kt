package com.muhiqbal.moviedb.core.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private val CinemaRed700  = Color(0xFFC62828)
private val CinemaRed500  = Color(0xFFE53935)
private val CinemaRed300  = Color(0xFFEF9A9A)
private val CinemaRed100  = Color(0xFFFFEBEE)

private val DarkBg        = Color(0xFF0D1117)
private val DarkSurface   = Color(0xFF161B22)
private val DarkSurfaceVar = Color(0xFF21262D)

val RatingGold = Color(0xFFF5C518)
private val Amber50 = Color(0xFFFFF9C4)

val LightColorScheme = lightColorScheme(
    primary            = CinemaRed700,
    onPrimary          = Color.White,
    primaryContainer   = CinemaRed100,
    onPrimaryContainer = Color(0xFF410002),
    secondary          = Color(0xFF6B6B6B),
    onSecondary        = Color.White,
    secondaryContainer = Color(0xFFE8E8E8),
    onSecondaryContainer = Color(0xFF1A1A1A),
    tertiary           = RatingGold,
    onTertiary         = Color(0xFF3A2900),
    tertiaryContainer  = Amber50,
    onTertiaryContainer = Color(0xFF241A00),
    background         = Color(0xFFF6F6F6),
    onBackground       = Color(0xFF1A1A1A),
    surface            = Color.White,
    onSurface          = Color(0xFF1A1A1A),
    surfaceVariant     = Color(0xFFE0E0E0),
    onSurfaceVariant   = Color(0xFF555555),
    outline            = Color(0xFF8C8C8C),
    outlineVariant     = Color(0xFFD0D0D0),
    error              = Color(0xFFBA1A1A),
    onError            = Color.White,
    errorContainer     = Color(0xFFFFDAD6),
    onErrorContainer   = Color(0xFF410002),
    inverseSurface     = Color(0xFF2D2D2D),
    inverseOnSurface   = Color(0xFFF4F4F4),
    inversePrimary     = CinemaRed300,
    scrim              = Color.Black,
)

val DarkColorScheme = darkColorScheme(
    primary            = CinemaRed500,
    onPrimary          = Color.White,
    primaryContainer   = Color(0xFF7F1515),
    onPrimaryContainer = CinemaRed100,
    secondary          = Color(0xFFAAAAAA),
    onSecondary        = Color(0xFF1A1A1A),
    secondaryContainer = Color(0xFF2E2E2E),
    onSecondaryContainer = Color(0xFFDDDDDD),
    tertiary           = Color(0xFFFFD54F),
    onTertiary         = Color(0xFF3A2900),
    tertiaryContainer  = Color(0xFF524300),
    onTertiaryContainer = Amber50,
    background         = DarkBg,
    onBackground       = Color(0xFFEAEAEA),
    surface            = DarkSurface,
    onSurface          = Color(0xFFEAEAEA),
    surfaceVariant     = DarkSurfaceVar,
    onSurfaceVariant   = Color(0xFFBBBBBB),
    outline            = Color(0xFF666666),
    outlineVariant     = DarkSurfaceVar,
    error              = Color(0xFFFFB4AB),
    onError            = Color(0xFF690005),
    errorContainer     = Color(0xFF93000A),
    onErrorContainer   = Color(0xFFFFDAD6),
    inverseSurface     = Color(0xFFEAEAEA),
    inverseOnSurface   = Color(0xFF2D2D2D),
    inversePrimary     = CinemaRed700,
    scrim              = Color.Black,
)
