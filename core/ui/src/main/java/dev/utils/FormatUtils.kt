package dev.utils

import java.text.NumberFormat
import java.util.Locale

/**
 * @param locale  the desired locale.
 * @param price price in cents.
 * @param isShowCents if true, it will show cents, otherwise not.
 */
fun formatPrice(
    locale: Locale,
    price: Int,
    isShowCents: Boolean,
    isShowCurrencySymbol: Boolean
): String {
    val formatter = NumberFormat.getCurrencyInstance(locale)

    val formattedPrice = if (isShowCents) {
        formatter.format(price.toDouble() / 100)
    } else {
        formatter.maximumFractionDigits = 0
        formatter.format(price / 100)
    }
    return if (isShowCurrencySymbol) {
        formattedPrice
    } else {
        formatter.currency?.let { formattedPrice.replace(it.symbol, "") } ?: formattedPrice
    }
}