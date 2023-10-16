package dev.nelson.mot.main.data.model

import dev.nelson.mot.main.util.UUIDUtils

// TODO: combine with PaymentListItemModel

/**
 * Category list item model this models is used in presentation layer to show list of categories.
 */
sealed class CategoryListItemModel {

    /**
     * Key is used for [androidx.compose.foundation.lazy.LazyColumn] item's key parameter
     */
    abstract val key: String

    /**
     * Category item model - main model to show category
     */
    class CategoryItemModel(val category: Category, override val key: String) :
        CategoryListItemModel()

    /**
     * Letter - model that show letter to divide categories
     */
    class Letter(val letter: String, override val key: String) : CategoryListItemModel()

    /**
     * Footer - last item in the list to show data or set empty space
     */
    class Footer(override val key: String) : CategoryListItemModel()
}

/**
 * Payment list item model. This models is used in presentation layer to show list of [Payment].
 */
sealed class MotListItemModel {

    abstract val key: String
    abstract val isShow: Boolean

    /**
     * Payment item model - main model to show category
     */
    data class Item(
        val category: Category,
        override val key: String = UUIDUtils.randomKey,
        override val isShow: Boolean = true
    ) : MotListItemModel()

    /**
     * Letter - model that show letter to divide categories
     */

    data class Header(
        val date: String,
        override val key: String = UUIDUtils.randomKey,
        override val isShow: Boolean = true
    ) : MotListItemModel()

    /**
     * Footer - last item in the list to show data or set empty space
     */
    data class Footer(
        override val key: String = UUIDUtils.randomKey,
        override val isShow: Boolean = true
    ) : MotListItemModel()
}
