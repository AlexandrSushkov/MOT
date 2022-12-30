package dev.nelson.mot.main.presentations.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val MotTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    /* Other default text styles to override*/
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
)

val MotTypographyLight = Typography(
    bodyLarge = MotTypography.bodyLarge.copy(color = MotColors.Purple40),
    titleLarge = MotTypography.titleLarge.copy(color = MotColors.Purple40),
    labelSmall = MotTypography.labelSmall.copy(color = MotColors.Purple40)
)

val MotTypographyDark = Typography(
    bodyLarge = MotTypography.bodyLarge.copy(color = MotColors.Pink40),
    titleLarge = MotTypography.titleLarge.copy(color = MotColors.Pink40),
    labelSmall = MotTypography.labelSmall.copy(color = MotColors.Pink40)
)