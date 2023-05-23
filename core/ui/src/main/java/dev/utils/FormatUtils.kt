package dev.utils

import dev.nelson.mot.core.ui.view_state.PriceViewState
import java.text.NumberFormat

private const val EMPTY_STRING = ""
private const val HASHTAG = "#"
private const val DIGIT_PATTERN = "\\d"

/**
 * @param locale  the desired locale.
 * @param price price in cents.
 * @param isShowCents if true, it will show cents, otherwise not.
 */
fun formatPrice(price: Int, priceViewState: PriceViewState): String {
    val formatter = NumberFormat.getCurrencyInstance(priceViewState.locale)
    return if (priceViewState.isShowCents) {
        formatter.format(price.toDouble() / 100)
    } else {
        formatter.maximumFractionDigits = 0
        formatter.format(price / 100)
    }.hideDigitsIfNeeded(priceViewState.isHideDigits)
        .removeCurrencySymbolIfNeeded(
            priceViewState.isShowCurrencySymbol,
            formatter.currency?.symbol
        )
}

private fun String.hideDigitsIfNeeded(isHideDigits: Boolean): String {
    return if (isHideDigits) {
        this.replace(Regex(DIGIT_PATTERN), HASHTAG)
    } else {
        this
    }
}

private fun String.removeCurrencySymbolIfNeeded(
    isShowCurrencySymbol: Boolean,
    currencySymbol: String?
): String {
    return if (isShowCurrencySymbol) {
        this
    } else {
        currencySymbol
            ?.let { this.replace(currencySymbol, EMPTY_STRING) }
            ?: this
    }
}
