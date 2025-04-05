package com.ryinex.kotlin.js.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import preloader.sample.generated.resources.Res
import preloader.sample.generated.resources.noto

internal val Noto: FontFamily @Composable get() = FontFamily(Font(Res.font.noto, FontWeight.Normal))

@Composable
internal fun extendedTypography(): Typography {
    val default = Typography()
    return Typography(
        displayLarge = default.displayLarge.copy(fontFamily = Noto),
        displayMedium = default.displayMedium.copy(fontFamily = Noto),
        displaySmall = default.displaySmall.copy(fontFamily = Noto),
        headlineLarge = default.headlineLarge.copy(fontFamily = Noto),
        headlineMedium = default.headlineMedium.copy(fontFamily = Noto),
        headlineSmall = default.headlineSmall.copy(fontFamily = Noto),
        titleLarge = default.titleLarge.copy(fontFamily = Noto),
        titleMedium = default.titleMedium.copy(fontFamily = Noto),
        titleSmall = default.titleSmall.copy(fontFamily = Noto),
        bodyLarge = default.bodyLarge.copy(fontFamily = Noto),
        bodyMedium = default.bodyMedium.copy(fontFamily = Noto),
        bodySmall = default.bodySmall.copy(fontFamily = Noto),
        labelLarge = default.labelLarge.copy(fontFamily = Noto),
        labelMedium = default.labelMedium.copy(fontFamily = Noto),
        labelSmall = default.labelSmall.copy(fontFamily = Noto)
    )
}