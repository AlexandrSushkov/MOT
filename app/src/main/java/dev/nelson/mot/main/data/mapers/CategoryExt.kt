package dev.nelson.mot.main.data.mapers

import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.room.model.category.CategoryEntity

fun CategoryEntity.toCategory(): Category = Category(this.name, this.id)

fun Category.toCategoryEntity(): CategoryEntity = CategoryEntity(this.name, this.id)

fun Category.copyWith(name:String): Category = Category(name, this.id)
