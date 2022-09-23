package dev.nelson.mot.main.data.model

/**
 * Category list item model this models is used in presentation layer to show list of categories.
 */
sealed class CategoryListItemModel {
    abstract val key: Int
    /**
     * Category item model - main model to show category
     */
    class CategoryItemModel(val category: Category, override val key: Int) : CategoryListItemModel()

    /**
     * Letter - model that show letter to divide categories
     */
    class Letter(val letter: String, override val key: Int) : CategoryListItemModel()

    /**
     * Header - first item in the list to show some data.
     */
    class Header(override val key: Int) : CategoryListItemModel()

    /**
     * Footer - last item in the list to show data or set empty space
     */
    class Footer(override val key: Int) : CategoryListItemModel()

    /**
     * Empty - model to add empty spaces to the list
     */
    class Empty(override val key: Int) : CategoryListItemModel()
}