package dev.nelson.mot.main.presentations.categories

import dev.nelson.mot.main.data.room.model.category.CategoryEntity

sealed class CategoryListItemModel {

    class CategoryItemModel(val category: CategoryEntity) : CategoryListItemModel()

    class Letter(val letter: String) : CategoryListItemModel()

    object Header : CategoryListItemModel()

    object Footer : CategoryListItemModel()

    object Empty : CategoryListItemModel()
}