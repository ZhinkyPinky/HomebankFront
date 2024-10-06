package com.example.homebankfront.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,

    secondary = secondaryDark,
    onSecondary = onSecondaryDark,

    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,


    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,

    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,

    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,


    surfaceDim = surfaceDimDark,

    surface = surfaceDark,
    onSurface = onSurfaceDark,

    surfaceBright = surfaceBrightDark,

    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,

    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    surfaceTint = surfaceTintDark,

    outline = outlineDark,
    outlineVariant = outlineVariantDark,


    error = errorDark,
    onError = onErrorDark,

    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,


    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,

    inversePrimary = inversePrimaryDark,


    scrim = scrimDark,


    background = backgroundDark,
    onBackground = onBackgroundDark,
)

private val LightColorScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,

    secondary = secondaryLight,
    onSecondary = onSecondaryLight,

    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,


    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,

    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,

    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,


    surfaceDim = surfaceDimLight,

    surface = surfaceLight,
    onSurface = onSurfaceLight,

    surfaceBright = surfaceBrightLight,

    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,

    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    surfaceTint = surfaceTintLight,

    outline = outlineLight,
    outlineVariant = outlineVariantLight,


    error = errorLight,
    onError = onErrorLight,

    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,


    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,

    inversePrimary = inversePrimaryLight,


    scrim = scrimLight,


    background = backgroundLight,
    onBackground = onBackgroundLight,
)


@Composable
fun HomeBankFrontTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }


    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}