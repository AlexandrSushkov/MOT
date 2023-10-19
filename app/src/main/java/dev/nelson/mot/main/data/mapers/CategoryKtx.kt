package dev.nelson.mot.main.data.mapers

import dev.nelson.mot.db.model.category.CategoryEntity
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.util.StringUtils

fun CategoryEntity.toCategory(): Category = Category(
    name = this.name,
    isFavorite = isFavorite == 1,
    id = this.id
)

fun List<CategoryEntity>.toCategoryList(): List<Category> = this.map { it.toCategory() }

fun List<Category>.toCategoryEntityList(): List<CategoryEntity> = this.map { it.toCategoryEntity() }

fun Category.toCategoryEntity(): CategoryEntity = CategoryEntity(this.name, if (isFavorite) 1 else 0, this.id)

@Deprecated("replace with default copy() method")
fun Category.copyWith(name: String): Category = Category(name, isFavorite, this.id)

fun Category.empty(): Category = Category(StringUtils.EMPTY)
