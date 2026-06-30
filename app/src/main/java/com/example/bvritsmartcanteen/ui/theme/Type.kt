package com.example.bvritsmartcanteen.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.sp
import com.example.bvritsmartcanteen.R
import androidx.compose.ui.graphics.Color

// 🔥 FONT FAMILY
val AppFontFamily = FontFamily(
    Font(R.font.montserrat_regular, FontWeight.Normal),
    Font(R.font.montserrat_medium, FontWeight.Medium),
    Font(R.font.montserrat_semibold, FontWeight.SemiBold),
    Font(R.font.montserrat_bold, FontWeight.Bold)
)

// 🔥 TYPOGRAPHY SYSTEM
val AppTypography = Typography(

    titleLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        color = Color.Black   // 🔥 ADD
    ),

    titleMedium = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        color = Color.Black   // 🔥 ADD
    ),

    bodyLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = Color.Black   // 🔥 ADD
    ),

    bodyMedium = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = Color.Gray   // 🔥 THIS FIXES GREY TEXT ISSUE
    ),

    labelLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        color = Color.Black   // 🔥 ADD
    )
)