package dev.nelson.mot.main.presentations.nav

import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.util.FirebaseUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DrawerViewModel @Inject constructor() : BaseViewModel() {

    val drawerViewState: Flow<DrawerViewState>
        get() = _drawerViewState.asStateFlow()
    private val _drawerViewState = MutableStateFlow(DrawerViewState())

    init {
        val remoteConfig: FirebaseRemoteConfig = FirebaseUtils.remoteConfig
        updateStatisticFF(remoteConfig)
        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                if (configUpdate.updatedKeys.contains("feature_statistic_enabled")) {
                    remoteConfig.activate()
                        .addOnCompleteListener { updateStatisticFF(remoteConfig) }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                Timber.e("Config update error with code: " + error.code, error)
            }
        })
    }

    fun onDestinationChanged(newRoute: String) {
        _drawerViewState.value
            .drawerItems
            .filter { drawerItem -> drawerItem.route == newRoute }
            .map { _drawerViewState.value.copy(selectedItem = it.route) }
            .map { newDrawerState -> _drawerViewState.value = newDrawerState }
    }

    private fun updateStatisticFF(remoteConfig: FirebaseRemoteConfig) {
        remoteConfig.all.getValue("feature_statistic_enabled").asBoolean()
            .let { isEnabled ->
                if (isEnabled) {
                    addDrawerItem(Statistic)
                } else {
                    removeDrawerItem(Statistic)
                }
            }
    }

    private fun addDrawerItem(destination: MotDestinations) {
        val newItems = _drawerViewState.value.drawerItems.toMutableList()
        val index = newItems.indexOf(Categories)
        newItems.add(index + 1, destination)
        _drawerViewState.value = _drawerViewState.value.copy(drawerItems = newItems)
    }

    private fun removeDrawerItem(destination: MotDestinations) {
        val newItems = _drawerViewState.value.drawerItems.toMutableList()
        newItems.remove(destination)
        _drawerViewState.value = _drawerViewState.value.copy(drawerItems = newItems)
    }
}