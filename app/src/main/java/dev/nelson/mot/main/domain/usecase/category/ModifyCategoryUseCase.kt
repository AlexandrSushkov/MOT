package dev.nelson.mot.main.domain.usecase.category

import dev.nelson.mot.main.data.mapers.toCategoryEntity
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.repository.CategoryRepositoryImpl
import dev.nelson.mot.main.domain.usecase.base.UseCaseSuspend
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
    data object Add : ModifyCategoryAction()
    data object Edit : ModifyCategoryAction()
    data object Delete : ModifyCategoryAction()
}
