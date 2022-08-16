package dev.nelson.mot.main.data.mapers

import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.room.model.category.CategoryEntity

fun CategoryEntity.toCategory(): Category = Category(this.name, isFavorite == 1, this.id)

fun Category.toCategoryEntity(): CategoryEntity = CategoryEntity(this.name, if (isFavorite) 1 else 0, this.id)

fun Category.copyWith(name:String): Category = Category(name, isFavorite, this.id)
