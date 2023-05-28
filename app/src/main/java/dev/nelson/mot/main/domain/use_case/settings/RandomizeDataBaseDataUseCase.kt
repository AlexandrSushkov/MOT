package dev.nelson.mot.main.domain.use_case.settings

import dev.nelson.mot.main.data.repository.CategoryRepositoryImpl
import dev.nelson.mot.main.data.repository.PaymentRepositoryImpl
import dev.nelson.mot.main.domain.use_case.base.UseCaseSuspend
import javax.inject.Inject
import kotlin.random.Random

class RandomizeDataBaseDataUseCase @Inject constructor(
    private val categoryRepository: CategoryRepositoryImpl,
    private val paymentRepository: PaymentRepositoryImpl
) : UseCaseSuspend<Nothing?, Unit> {
    override suspend fun execute(params: Nothing?) {
        val cat = categoryRepository.getAllCategories()
        val newCat = cat.mapIndexed { index, category ->
            if (index < testCategoriesNames.size) {
                category.copy(name = testCategoriesNames[index])
            } else {
                category.copy(name = testCategoriesNames.random())

            }
        }
        categoryRepository.editCategories(newCat)

        val pay = paymentRepository.getAllPayments()
        val newPay = pay.map { payment ->
            payment.copy(
                title = "payment ${payment.id}",
                cost = Random.nextInt(1, 100000)
            )
        }
        paymentRepository.updatePayments(newPay)
    }

    private val testCategoriesNames = listOf(
        "Art",
        "Beauty",
        "Books",
        "Business",
        "Clothing",
        "Computers",
        "Crafts",
        "Education",
        "Electronics",
        "Entertainment",
        "Fashion",
        "Fitness",
        "Food",
        "Gaming",
        "Health",
        "Home",
        "Jewelry",
        "Kids",
        "Music",
        "Movies",
        "Outdoors",
        "Pets",
        "Photography",
        "Science",
        "Sports",
        "Technology",
        "Travel",
        "Vehicles",
        "Weddings",
        "Animals",
        "Architecture",
        "Automotive",
        "Baby",
        "Career",
        "Design",
        "DIY",
        "Finance",
        "Gardening",
        "History",
        "Hobbies",
        "Internet",
        "Languages",
        "Marketing",
        "News",
        "Parenting",
        "Politics",
        "Real Estate",
        "Religion",
        "Shopping",
        "Social Media",
        "Writing"
    )
}