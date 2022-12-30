package dev.nelson.mot.main.presentations.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.presentations.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor() : BaseViewModel() {

    val isLoading
        get() = _isLoading.asStateFlow()
    private val _isLoading = MutableStateFlow(true)

    val forceDark
        get() = _forceDark.asStateFlow()
    private val _forceDark = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            // do something
//            delay(1000)
            // TODO: load from preferences
//            _forceDark.value = true
            _isLoading.value = false
        }
    }
}
