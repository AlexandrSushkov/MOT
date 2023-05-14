package dev.nelson.mot.main.data.model

import java.util.Locale

data class PaymentListViewState(
    val isShowCents: Boolean = false,
    val selectedLocale: Locale = Locale.getDefault(),
    val paymentList: List<PaymentListItemModel> = emptyList(),
)
