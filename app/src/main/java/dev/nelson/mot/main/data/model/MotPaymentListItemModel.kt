package dev.nelson.mot.main.data.model

import dev.nelson.mot.main.util.UUIDUtils

/**
 * Payment list item model. This models is used in presentation layer to show list of [Payment].
 */
sealed class MotPaymentListItemModel {

    /**
     * Key is used for [androidx.compose.foundation.lazy.LazyColumn] item's key parameter
     */
    abstract val key: String
    abstract val isShow: Boolean

    /**
     * Payment item model - main model to show category
     */
    data class Item(
        val payment: Payment,
        val showCategory: Boolean,
        override val key: String = UUIDUtils.randomKey,
        override val isShow: Boolean = true,
    ) : MotPaymentListItemModel()

    /**
     * Letter - model that show letter to divide categories
     */
    data class Header(
        val date: String,
        override val key: String,
        override val isShow: Boolean = true
    ) : MotPaymentListItemModel()

    /**
     * Footer - last item in the list to show data or set empty space
     */
    data class Footer(
        override val key: String = UUIDUtils.randomKey,
        override val isShow: Boolean = true
    ) : MotPaymentListItemModel()
}
