@file:OptIn(ExperimentalMaterialApi::class)

package dev.nelson.mot.core.ui

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.utils.formatPrice
import java.util.Locale

@Composable
fun PriceText(
    locale: Locale,
    isShowCents: Boolean,
    priceInCents: Int
) {
    val formattedCost = formatPrice(locale, priceInCents, isShowCents)
    Text(
        text = formattedCost,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.error
    )
}

@Preview(showBackground = true, group = "MotTextFieldDynamic")
@Composable
fun TextPricePreviewDarkOff() {
    MotMaterialTheme(darkTheme = true) {
        PriceTextPreviewData()
    }
}

@Preview(showBackground = true, group = "MotTextFieldDynamic")
@Composable
fun TextPricePreviewDynamicOn() {
    MotMaterialTheme(dynamicColor = true) {
        PriceTextPreviewData()
    }
}

@Preview(showBackground = true, group = "MotTextFieldDynamic")
@Composable
fun TextPricePreviewDynamicOff() {
    MotMaterialTheme(dynamicColor = true) {
        PriceTextPreviewData()
    }
}

@Composable
private fun PriceTextPreviewData() {
    ListItem(
        headlineContent = {
            PriceText(
//                Locale.getDefault(),
                Locale.Builder().setLanguage("ua").setRegion("UA").build(),
                isShowCents = true,
                priceInCents = 999999
            )
        },
    )
}
