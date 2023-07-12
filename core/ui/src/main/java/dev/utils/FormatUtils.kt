package dev.utils

import dev.nelson.mot.core.ui.view_state.PriceViewState
import java.text.NumberFormat

private const val EMPTY_STRING = ""
private const val HASHTAG = "#"
private const val DIGIT_PATTERN = "\\d"

/**
 * @param price price in cents.
 * @param priceViewState parameters to format price.
 */
fun formatPrice(price: Int, priceViewState: PriceViewState): String {
    val formatter = NumberFormat.getCurrencyInstance(priceViewState.locale).apply {

    }
    return price.toPrice(formatter, priceViewState.isShowCents)
        .hideDigitsIfNeeded(priceViewState.isShowDigits)
        .removeCurrencySymbolIfNeeded(
            priceViewState.isShowCurrencySymbol,
            formatter.currency?.symbol
        )
}

/**
 * format price in cents to a string.
 */
private fun Int.toPrice(priceFormatter: NumberFormat, isShowCents: Boolean): String {
    return if (isShowCents) {
        priceFormatter.format(this.toDouble() / 100)
    } else {
        priceFormatter.maximumFractionDigits = 0
        priceFormatter.format(this / 100)
    }
}

/**
 * replace all digits with hashtag in a string if [isShowDigits] is true.
 */
private fun String.hideDigitsIfNeeded(isShowDigits: Boolean): String {
    return if (isShowDigits) this else this.replace(Regex(DIGIT_PATTERN), HASHTAG)
}

/**
 * remove currency symbol in a strings if [isShowCurrencySymbol] is false.
 */
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
