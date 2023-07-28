package dev.nelson.mot.main.presentations.screen.dashboard

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.data.model.MotListItemModel
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.util.compose.PreviewData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor() : BaseViewModel() {

    private val initialItems: List<MotListItemModel> = (1..20).map {
        MotListItemModel.Item(PreviewData.categoryPreview, isShow = true)
    }
    val itemsState: Flow<List<MotListItemModel>>
        get() = _items.asStateFlow()
    private val _items = MutableStateFlow(initialItems)

    fun onItemSwiped(item: MotListItemModel.Item) {
        _items.update {
            _items.value.map {
                when (it) {
                    is MotListItemModel.Item -> {
                        if (it.key == item.key) {
                            it.copy(isShow = false)
                        } else {
                            it.copy()
                        }
                    }

                    is MotListItemModel.Header -> {
                        it.copy()
                    }

                    is MotListItemModel.Footer -> {
                        it.copy()
                    }
                }
            }
        }
    }

    fun onUndoClicked() = launch {
        _items.update {
            _items.value.map {
                when (it) {
                    is MotListItemModel.Item -> it.copy(isShow = true)
                    is MotListItemModel.Header -> it.copy()
                    is MotListItemModel.Footer -> it.copy()
                }
            }
        }
    }
}
