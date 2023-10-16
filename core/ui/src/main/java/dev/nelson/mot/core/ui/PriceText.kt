package dev.nelson.mot.core.ui

import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import dev.nelson.mot.core.ui.view_state.PriceViewState
import dev.utils.formatPrice
import dev.utils.preview.MotPreview

/**
 * @param price price in cents
 * @param priceViewState view state for price
 */
@Composable
fun PriceText(
    modifier: Modifier = Modifier,
    price: Int,
    priceViewState: PriceViewState = PriceViewState(),
    style: TextStyle = MaterialTheme.typography.titleMedium,
) {
    val formattedPrice = formatPrice(price, priceViewState)

    Text(
        modifier = modifier,
        text = formattedPrice,
        style = style,
        color = MaterialTheme.colorScheme.error
    )
}

@MotPreview
@Composable
private fun TextPricePreview() {
    MotMaterialTheme {
        ListItem(
            headlineContent = {
                PriceText(
                    price = 999999,
                    priceViewState = PriceViewState(
//                        isShowCents = false,
//                        isShowCurrencySymbol = false,
//                        isShowDigits = false
                    )
                )
            }
        )
    }
}
