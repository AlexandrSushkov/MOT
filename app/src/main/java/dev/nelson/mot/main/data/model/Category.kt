package dev.nelson.mot.main.data.model

import dev.nelson.mot.main.data.room.model.category.CategoryEntity

data class Category(val name: String, val id: Int? = null)

fun CategoryEntity.toCategory(): Category{
    return Category(this.name, this.id)
}