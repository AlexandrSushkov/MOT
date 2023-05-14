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
    isShowCents: Boolean
): String {
    val formatter = NumberFormat.getCurrencyInstance(locale)
    return if (isShowCents) {
        formatter.format(price.toDouble() / 100)
    } else {
        formatter.maximumFractionDigits = 0
        formatter.format(price / 100)
    }
}