package dev.nelson.mot.main.util.compose

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.nelson.mot.main.data.mapers.toCategoryEntity
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.room.model.category.CategoryEntity
import dev.nelson.mot.main.presentations.categories.CategoryListItemModel

val categoryNames = listOf(
    "Support",
    "Legal",
    "Sales",
    "Human Resources",
    "Support",
    "Product Management",
    "Legal",
    "Legal",
    "Research and Development",
    "Engineering",
    "Product Management",
    "Engineering",
    "Support",
    "Engineering",
    "Training",
    "Support",
    "Marketing",
    "Business Development",
    "Human Resources",
    "Sales",
    "Human Resources",
    "Product Management",
    "Human Resources",
    "Services",
    "Legal",
    "Accounting",
    "Training",
    "Services",
    "Support",
    "Support",
)

object PreviewData {
    val categoryPreview = Category(categoryNames.first())
    val paymentItemPreview = Payment("payment name", 100, category = categoryPreview)
    val paymentListPreview: List<Payment> = (1..30).map { Payment("payment $it", it * 10, id = it.toLong(), category = categoryPreview) }

    val categoryListPreview: List<CategoryListItemModel> = getCategoryList()

    private fun getCategoryList(): List<CategoryListItemModel> {
        val map = categoryNames
            .sortedBy { it.first() }
            .mapIndexed { index, categoryName -> CategoryEntity(categoryName, id = index) }
            .groupBy { category: CategoryEntity -> category.name.first() }
        return createCategoryListViewRepresentation(map)
    }

    private fun createCategoryListViewRepresentation(value: Map<Char, List<CategoryEntity>>): List<CategoryListItemModel> {
        return mutableListOf<CategoryListItemModel>()
            .apply {
                //no category item
                val noCategory = CategoryEntity("No category")
                add(CategoryListItemModel.CategoryItemModel(noCategory))
                //add categories items
                value.forEach { (letter, categoryList) ->
                    add(CategoryListItemModel.Letter(letter.toString()))
                    addAll(categoryList.map { categoryEntity -> CategoryListItemModel.CategoryItemModel(categoryEntity) })
                }
            }
    }

    val categoryItemPreview = CategoryListItemModel.CategoryItemModel(categoryPreview.toCategoryEntity())
    val letterPreview = CategoryListItemModel.Letter("A")
}

class PaymentPreviewProvider : PreviewParameterProvider<Payment> {
    val category = Category("category")
    override val values = sequenceOf(Payment("payment name", 100, category = category))
}

class CategoryPreviewProvider : PreviewParameterProvider<Category> {
    override val values = sequenceOf(Category("category"))
}