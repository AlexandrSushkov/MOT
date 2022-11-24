package dev.nelson.mot.main.presentations.home

import androidx.databinding.ObservableField
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.presentations.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel() {

    val title = ObservableField<String>()

}
