package com.apexcoretechs.beneaththesurface.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.apexcoretechs.beneaththesurface.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val ApexFontFamily = FontFamily(
    Font(R.font.apexfont)
)

val AppTypography = Typography(
    displayLarge = TextStyle(fontFamily = ApexFontFamily, fontSize = 57.sp, color = DarkSlateBlue),
    displayMedium = TextStyle(fontFamily = ApexFontFamily, fontSize = 45.sp, color = DarkSlateBlue),
    displaySmall = TextStyle(fontFamily = ApexFontFamily, fontSize = 36.sp, color = DarkSlateBlue),
    headlineLarge = TextStyle(fontFamily = ApexFontFamily, fontSize = 32.sp, color = DarkSlateBlue),
    headlineMedium = TextStyle(fontFamily = ApexFontFamily, fontSize = 28.sp, color = DarkSlateBlue),
    headlineSmall = TextStyle(fontFamily = ApexFontFamily, fontSize = 24.sp, color = DarkSlateBlue),
    titleLarge = TextStyle(fontFamily = ApexFontFamily, fontSize = 22.sp, color = DarkSlateBlue),
    titleMedium = TextStyle(fontFamily = ApexFontFamily, fontSize = 16.sp, color = DarkSlateBlue),
    titleSmall = TextStyle(fontFamily = ApexFontFamily, fontSize = 14.sp, color = DarkSlateBlue),
    bodyLarge = TextStyle(fontFamily = ApexFontFamily, fontSize = 16.sp, color = DarkSlateBlue),
    bodyMedium = TextStyle(fontFamily = ApexFontFamily, fontSize = 14.sp, color = DarkSlateBlue),
    bodySmall = TextStyle(fontFamily = ApexFontFamily, fontSize = 12.sp, color = DarkSlateBlue),
    labelLarge = TextStyle(fontFamily = ApexFontFamily, fontSize = 14.sp, color = DarkSlateBlue),
    labelMedium = TextStyle(fontFamily = ApexFontFamily, fontSize = 12.sp, color = DarkSlateBlue),
    labelSmall = TextStyle(fontFamily = ApexFontFamily, fontSize = 11.sp, color = DarkSlateBlue),
)
