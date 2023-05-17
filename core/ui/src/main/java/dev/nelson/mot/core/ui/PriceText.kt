package dev.nelson.mot.core.ui

import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.utils.formatPrice
import dev.utils.preview.MotPreview
import java.util.Locale

@Composable
fun PriceText(
    locale: Locale,
    isShowCents: Boolean,
    priceInCents: Int,
    isShowCurrencySymbol: Boolean
) {
    val formattedCost = formatPrice(locale, priceInCents, isShowCents, isShowCurrencySymbol)
    Text(
        text = formattedCost,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.error
    )
}

@MotPreview
@Composable
fun TextPricePreview() {
    MotMaterialTheme {
        ListItem(
            headlineContent = {
                PriceText(
                    Locale.getDefault(),
                    isShowCents = true,
                    isShowCurrencySymbol = true,
                    priceInCents = 999999
                )
            },
        )
    }
}
