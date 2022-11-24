package dev.nelson.mot.main.util.compose

import android.content.Context
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.google.gson.Gson
import dev.nelson.mot.main.data.mapers.toCategoryEntity
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.CategoryListItemModel
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.model.PaymentListItemModel
import java.io.InputStream
import java.util.UUID

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

    val categoryPreview: Category
        get() {
            val randomPosition = (categoryNames.indices).random()
            return Category(categoryNames[randomPosition])
        }

    val categoryEntityPreview = Category(categoryNames.first()).toCategoryEntity()

    val paymentItemPreview
        get() = Payment(
            "payment name",
            100,
            category = categoryPreview,
            message = "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. "
        )

    val paymentItemModelPreview
        get() = PaymentListItemModel.PaymentItemModel(paymentItemPreview, generateKey())

    val paymentListPreview: List<Payment>
        get() = (1..30).map { Payment("payment $it", it * 10, id = it.toLong(), category = categoryPreview) }

    fun jsonString(context: Context, assetFile: String): String {
        val inputStream: InputStream = context.assets.open(assetFile)
        val size: Int = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        return String(buffer, Charsets.UTF_8)
    }

    fun getTestJson(context: Context) {
        val jsonString = jsonString(context, "test.json")
        val gson = Gson().fromJson(jsonString, Payment::class.java)
    }

    val paymentListItemsPreview: List<PaymentListItemModel>
        get() = paymentListPreview.map { PaymentListItemModel.PaymentItemModel(it, generateKey()) as PaymentListItemModel }
            .toMutableList()
            .apply {
                this.add(0, PaymentListItemModel.Header("start", generateKey()))
                val indexOfTheLastElement = this.indexOf(this.last())
                this.add(indexOfTheLastElement / 2, PaymentListItemModel.Header("end", generateKey()))
            }

    val categoriesListItemsPreview: List<CategoryListItemModel> = getCategoryList()

    val categoriesSelectListItemsPreview: List<Category> = categoryNames
        .sortedBy { it.first() }
        .mapIndexed { index, categoryName -> Category(categoryName, id = index) }

    private fun getCategoryList(): List<CategoryListItemModel> {
        val map = categoryNames
            .sortedBy { it.first() }
            .mapIndexed { index, categoryName -> Category(categoryName, id = index) }
            .groupBy { category: Category -> category.name.first() }
        return createCategoryListViewRepresentation(map)
    }

    private fun createCategoryListViewRepresentation(value: Map<Char, List<Category>>): List<CategoryListItemModel> {
        return mutableListOf<CategoryListItemModel>()
            .apply {
                //no category item
                val noCategory = Category("No category")
                add(CategoryListItemModel.CategoryItemModel(noCategory, generateKey()))
                //add categories items
                value.forEach { (letter, categoryList) ->
                    add(CategoryListItemModel.Letter(letter.toString(), generateKey()))
                    addAll(categoryList.map { category -> CategoryListItemModel.CategoryItemModel(category, generateKey()) })
                }
            }
    }

    val categoryItemPreview = CategoryListItemModel.CategoryItemModel(categoryPreview, generateKey())

    val letterPreview = CategoryListItemModel.Letter("A", generateKey())

    private fun generateKey() = UUID.randomUUID().toString()
}

class PaymentPreviewProvider : PreviewParameterProvider<Payment> {
    val category = Category("category")
    override val values = sequenceOf(Payment("payment name", 100, category = category))
}

class CategoryPreviewProvider : PreviewParameterProvider<Category> {
    override val values = sequenceOf(Category("category"))
}