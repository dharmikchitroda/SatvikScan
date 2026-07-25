package com.example.satvikscan.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = SatvikGreen,    // main color SatvikGreen !
    secondary = SatvikMint,
    tertiary = SatvikBrown
)
private val DarkColorScheme = darkColorScheme(
    primary = SatvikGreen,    // Dark mode green SatvikGreen
    secondary = SatvikMint,
    tertiary = SatvikBrown
)



@Composable
fun SatvikScanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // set color according to the wallpaper this not for suitable this type app beacuse i want to fix my color primary app 
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        /*1 condition -> */
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }

        /* 2 condition -> */
        darkTheme -> DarkColorScheme

        /* 3 condition -> */
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}