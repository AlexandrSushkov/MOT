package dev.nelson.mot.main.data.model

/**
 * Category list item model this models is used in presentation layer to show list of categories.
 */
sealed class CategoryListItemModel {

    /**
     * Category item model - main model to show category
     */
    class CategoryItemModel(val category: Category) : CategoryListItemModel()

    /**
     * Letter - model that show letter to divide categories
     */
    class Letter(val letter: String) : CategoryListItemModel()

    /**
     * Header - first item in the list to show some data.
     */
    object Header : CategoryListItemModel()

    /**
     * Footer - last item in the list to show data or set empty space
     */
    object Footer : CategoryListItemModel()

    /**
     * Empty - model to add empty spaces to the list
     */
    object Empty : CategoryListItemModel()
}