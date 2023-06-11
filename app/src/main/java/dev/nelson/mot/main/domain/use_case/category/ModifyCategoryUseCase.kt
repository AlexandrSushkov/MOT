package dev.nelson.mot.main.domain.use_case.category

import dev.nelson.mot.main.data.mapers.toCategoryEntity
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.repository.CategoryRepositoryImpl
import dev.nelson.mot.main.domain.use_case.base.UseCaseSuspend
import javax.inject.Inject

/**
 * Used to add, edit or delete [Category]
 */
class ModifyCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepositoryImpl
) : UseCaseSuspend<ModifyCategoryParams, Unit> {

    override suspend fun execute(params: ModifyCategoryParams) {
        val categoryEntity = params.category.toCategoryEntity()
        when (params.action) {
            ModifyCategoryAction.Add -> categoryRepository.addCategory(categoryEntity)
            ModifyCategoryAction.Edit -> categoryRepository.editCategory(categoryEntity)
            ModifyCategoryAction.Delete -> categoryRepository.deleteCategory(categoryEntity)
        }
    }
}

data class ModifyCategoryParams(val category: Category, val action: ModifyCategoryAction)

sealed class ModifyCategoryAction {
    object Add : ModifyCategoryAction()
    object Edit : ModifyCategoryAction()
    object Delete : ModifyCategoryAction()
}