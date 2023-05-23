package dev.nelson.mot.core.ui.view_state

import java.util.Locale

data class PriceViewState(
    val locale: Locale = Locale.getDefault(),
    val isShowCents: Boolean = true,
    val isShowCurrencySymbol: Boolean = true,
    val isHideDigits: Boolean = false
)
