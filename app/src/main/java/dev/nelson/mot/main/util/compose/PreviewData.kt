package dev.nelson.mot.main.util.compose

import android.content.Context
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.nelson.mot.core.ui.view_state.PriceViewState
import dev.nelson.mot.main.data.mapers.toCategoryEntity
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.CategoryListItemModel
import dev.nelson.mot.main.data.model.MotListItemModel
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.model.PaymentListItemModel
import dev.nelson.mot.main.domain.use_case.statistic.Month
import dev.nelson.mot.main.domain.use_case.statistic.StatisticByCategoryPerMonthModel
import dev.nelson.mot.main.domain.use_case.statistic.StatisticForMonthForCategoryModel
import dev.nelson.mot.main.presentations.screen.statistic.StatisticByCategoryModel
import dev.nelson.mot.main.presentations.screen.statistic.StatisticByMonthModel
import dev.nelson.mot.main.presentations.screen.statistic.StatisticByYearModel
import dev.nelson.mot.main.util.UUIDUtils
import dev.nelson.mot.main.util.constant.Constants
import dev.nelson.mot.main.util.extention.formatMillsToDateText
import java.io.InputStream
import java.util.UUID

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
        get() = PaymentListItemModel.PaymentItemModel(paymentItemPreview, true, generateKey())

    val paymentListPreview: List<Payment>
        get() = (1..30).map {
            Payment("payment $it", it * 10, id = it, category = categoryPreview)
        }

    val statisticByMonthModelPreviewData
        get(): StatisticByMonthModel {
            val month = generateMonth
            val year = generateYear
            val sumOfCategories = (1000000..5000000).random() // Example sum of categories

            // Generate a list of 5-10 items for categoriesModelList
            val categoriesModelList = generateCategoriesModelList
            val time = generateTime // Example sum of categories
            return StatisticByMonthModel(
                key = generateKey(),
                monthText = time.formatMillsToDateText(),
                month = month,
                year = year,
                sumOfCategories = sumOfCategories,
                isExpanded = false,
                categoriesModelList = categoriesModelList
            )
        }

    val statisticByMonthModelEmptyPreviewData
        get(): StatisticByMonthModel {
            val month = generateMonth
            val year = generateYear
            val sumOfCategories = (1000000..5000000).random() // Example sum of categories

            // Generate a list of 5-10 items for categoriesModelList
            val categoriesModelList = generateCategoriesModelList
            val time = generateTime // Example sum of categories
            return StatisticByMonthModel(
                key = generateKey(),
                monthText = time.formatMillsToDateText(),
                month = month,
                year = year,
                sumOfCategories = sumOfCategories,
                isExpanded = false,
                categoriesModelList = emptyList()
            )
        }

    val generateTime
        get() = (1672609228000..1704058828000).random()

    val statisticByYearModelPreviewData
        get(): StatisticByYearModel {
            val year = generateYear
            val sumOfCategories = generateSumForCategory

            // Generate a list of 5-10 items for categoriesModelList
            val categoriesModelList = generateCategoriesModelList

            return StatisticByYearModel(
                key = generateKey(),
                year,
                sumOfCategories,
                isExpanded = false,
                categoriesModelList
            )
        }

//    val generateStatisticByCategoryPerMonthModelListPreviewProvider
//        get() = object : PreviewParameterProvider<List<StatisticByCategoryPerMonthModel>> {
//            override val values: Sequence<List<StatisticByCategoryPerMonthModel>>
//                get() = sequenceOf(
//                    listOf(
//                        statisticByCategoryPerMonthModel,
//                        statisticByCategoryPerMonthModel,
//                        statisticByCategoryPerMonthModel
//                    )
//                )
//        }

    val generateStatisticByCategoryPerMonthModelListPreviewProvider
        get(): List<StatisticByCategoryPerMonthModel> {
            val itemCount = (6..10).random()
            return (1..itemCount).map {
                statisticByCategoryPerMonthModel
            }
        }

    val statisticByCategoryPerMonthModel
        get(): StatisticByCategoryPerMonthModel {
            val itemCount = (6..10).random()
            val monthToPaymentsForMonthPreviewData = (1..itemCount).associate {
                paymentItemPreview
                val month =
                    Month(
                        generateTime.formatMillsToDateText(Constants.YEAR_MONTH_DATE_PATTERN),
                        generateMonth,
                        generateYear
                    )
                month to generateStatisticForMonthForCategoryModel
            }
            return StatisticByCategoryPerMonthModel(
                key = generateKey,
                category = categoryPreview,
                totalPrice = generateSumForCategory,
                paymentToMonth = monthToPaymentsForMonthPreviewData
            )
        }

    val statisticByCategoryPerMonthModelEmpty
        get(): StatisticByCategoryPerMonthModel {
            return StatisticByCategoryPerMonthModel(
                key = generateKey,
                category = categoryPreview,
                totalPrice = generateSumForCategory,
                paymentToMonth = emptyMap()
            )
        }

    val generateStatisticForMonthForCategoryModel
        get(): StatisticForMonthForCategoryModel {
            return StatisticForMonthForCategoryModel(
                generateSumForCategory,
                paymentListPreview
            )
        }

    private val generateYear
        get() = (2018..2023).random()

    private val generateMonth
        get() = (1..12).random()

    private val generateKey
        get() = UUIDUtils.randomKey

    private val generateSumForCategory
        get() = (1000000..5000000).random()


    private val generateCategoriesModelList
        get(): List<StatisticByCategoryModel> {
            val numCategories =
                (5..10).random() // Generate a random number of categories between 5 and 10
            val categoriesModelList = mutableListOf<StatisticByCategoryModel>()

            for (i in 1..numCategories) {
                val value = (10000..50000).random() // Generate a random value for the category

                val categoryModel = StatisticByCategoryModel(
                    key = UUIDUtils.randomKey,
                    category = categoryPreview,
                    sumOfPayments = value,
                    percentage = value / 100.0,
                    payments = paymentListPreview
                )
                categoriesModelList.add(categoryModel)
            }

            return categoriesModelList
        }

    val statisticByYearListPreviewData
        get():List<StatisticByYearModel> {
            return (1..5).map { statisticByYearModelPreviewData }
        }

    val statisticByMonthListPreviewData
        get():List<StatisticByMonthModel> {
            return (6..20).map { statisticByMonthModelPreviewData }
        }

    fun jsonString(context: Context, assetFile: String): String {
        val inputStream: InputStream = context.assets.open(assetFile)
        val size: Int = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        return String(buffer, Charsets.UTF_8)
    }

    val paymentListItemsPreview: List<PaymentListItemModel>
        get() = paymentListPreview.map {
            PaymentListItemModel.PaymentItemModel(
                it,
                true,
                generateKey()
            ) as PaymentListItemModel
        }
            .toMutableList()
            .apply {
                this.add(0, PaymentListItemModel.Header("start", generateKey()))
                val indexOfTheLastElement = this.indexOf(this.last())
                this.add(
                    indexOfTheLastElement / 2,
                    PaymentListItemModel.Header("end", generateKey())
                )
            }

    val categoriesListItemsPreview: List<MotListItemModel> = getCategoryList()

    val categoriesSelectListItemsPreview: List<Category> = categoryNames
        .sortedBy { it.first() }
        .mapIndexed { index, categoryName ->
            Category(
                categoryName,
                id = index,
                isFavorite = index < 2
            )
        }

    private fun getCategoryList(): List<MotListItemModel> {
        val map = categoryNames
            .sortedBy { it.first() }
            .mapIndexed { index, categoryName -> Category(categoryName, id = index) }
            .groupBy { category: Category -> category.name.first() }
        return createCategoryListViewRepresentation(map)
    }

    private fun createCategoryListViewRepresentation(value: Map<Char, List<Category>>): List<MotListItemModel> {
        return mutableListOf<MotListItemModel>()
            .apply {
                //no category item
                val noCategory = Category("No category")
                add(MotListItemModel.Item(noCategory, generateKey()))
                //add categories items
                value.forEach { (letter, categoryList) ->
                    add(MotListItemModel.Header(letter.toString(), generateKey()))
                    addAll(categoryList.map { category ->
                        MotListItemModel.Item(
                            category,
                            generateKey()
                        )
                    })
                }
            }
    }

    val categoryItemPreview =
        CategoryListItemModel.CategoryItemModel(categoryPreview, generateKey())

    val letterPreview = CategoryListItemModel.Letter("A", generateKey())

    val priceViewState = PriceViewState()

    private fun generateKey() = UUID.randomUUID().toString()
}

class PaymentPreviewProvider : PreviewParameterProvider<Payment> {
    val category = Category("category")
    override val values = sequenceOf(Payment("payment name", 100, category = category))
}

class CategoryPreviewProvider : PreviewParameterProvider<Category> {
    override val values = sequenceOf(Category("category"))
}

val categoryNames = listOf(
    "Support",
    "Legal",
    "Veeeeeeeeeee rrryyyyy llllooooongg caaaaategooorryyyy",
    "Veeeeeeeeeee rrryyyyy llllooooongg caaaaategooorryyyy",
    "Veeeeeeeeeee rrryyyyy llllooooongg caaaaategooorryyyy",
    "Veeeeeeeeeee rrryyyyy llllooooongg caaaaategooorryyyy",
    "Veeeeeeeeeee rrryyyyy llllooooongg caaaaategooorryyyy",
    "Veeeeeeeeeee rrryyyyy llllooooongg caaaaategooorryyyy",
    "Veeeeeeeeeee rrryyyyy llllooooongg caaaaategooorryyyy",
    "Veeeeeeeeeee rrryyyyy llllooooongg caaaaategooorryyyy",
    "Veeeeeeeeeee rrryyyyy llllooooongg caaaaategooorryyyy",
    "Veeeeeeeeeee rrryyyyy llllooooongg caaaaategooorryyyy",
    "Veeeeeeeeeee rrryyyyy llllooooongg caaaaategooorryyyy",
    "Veeeeeeeeeee rrryyyyy llllooooongg caaaaategooorryyyy",
    "Veeeeeeeeeee rrryyyyy llllooooongg caaaaategooorryyyy",
    "Veeeeeeeeeee rrryyyyy llllooooongg caaaaategooorryyyy",
    "Veeeeeeeeeee rrryyyyy llllooooongg caaaaategooorryyyy",
    "Veeeeeeeeeee rrryyyyy llllooooongg caaaaategooorryyyy",
    "Veeeeeeeeeee rrryyyyy llllooooongg caaaaategooorryyyy",
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