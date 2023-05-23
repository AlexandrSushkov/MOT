package dev.nelson.mot.core.ui

import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.nelson.mot.core.ui.view_state.PriceViewState
import dev.utils.formatPrice
import dev.utils.preview.MotPreview

/**
 * @param price price in cents
 * @param priceViewState view state for price
 */
@Composable
fun PriceText(
    price: Int,
    priceViewState: PriceViewState = PriceViewState()
) {
    val formattedPrice = formatPrice(price, priceViewState)

    Text(
        text = formattedPrice,
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
                    price = 999999,
                    priceViewState = PriceViewState()
                )
            },
        )
    }
}
