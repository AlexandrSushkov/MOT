package dev.nelson.mot.main.data.model

/**
 * Payment list item model. This models is used in presentation layer to show list of [Payment].
 */
sealed class PaymentListItemModel {

    /**
     * Key is used for [androidx.compose.foundation.lazy.LazyColumn] item's key parameter
     */
    abstract val key: String

    /**
     * Payment item model - main model to show category
     */
    class PaymentItemModel(val payment: Payment, override val key: String) : PaymentListItemModel()

    /**
     * Letter - model that show letter to divide categories
     */
    class Header(val date: String, override val key: String) : PaymentListItemModel()

    /**
     * Footer - last item in the list to show data or set empty space
     */
    class Footer(override val key: String) : PaymentListItemModel()
}
