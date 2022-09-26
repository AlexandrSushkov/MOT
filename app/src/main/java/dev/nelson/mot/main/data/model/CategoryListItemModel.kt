package dev.nelson.mot.main.data.model

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
    class CategoryItemModel(val category: Category, override val key: String) : CategoryListItemModel()

    /**
     * Letter - model that show letter to divide categories
     */
    class Letter(val letter: String, override val key: String) : CategoryListItemModel()

    /**
     * Header - first item in the list to show some data.
     */
//    class Header(override val key: String) : CategoryListItemModel()

    /**
     * Footer - last item in the list to show data or set empty space
     */
    class Footer(override val key: String) : CategoryListItemModel()

    /**
     * Empty - model to add empty spaces to the list
     */
//    class Empty(override val key: String) : CategoryListItemModel()
}