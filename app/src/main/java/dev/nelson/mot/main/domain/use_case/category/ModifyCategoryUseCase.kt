package dev.nelson.mot.main.domain.use_case.category

import dev.nelson.mot.main.data.mapers.toCategoryEntity
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.repository.CategoryRepository
import javax.inject.Inject

/**
 * User to add, edit or delete [Category]
 */
class ModifyCategoryUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {

    suspend fun execute(category: Category, action: ModifyCategoryAction) {
        val categoryEntity = category.toCategoryEntity()
        when (action) {
            ModifyCategoryAction.Add -> categoryRepository.addCategory(categoryEntity)
            ModifyCategoryAction.Edit -> categoryRepository.editCategory(categoryEntity)
            ModifyCategoryAction.Delete -> categoryRepository.deleteCategory(categoryEntity)
        }
    }


}

sealed class ModifyCategoryAction {
    object Add : ModifyCategoryAction()
    object Edit : ModifyCategoryAction()
    object Delete : ModifyCategoryAction()
}
