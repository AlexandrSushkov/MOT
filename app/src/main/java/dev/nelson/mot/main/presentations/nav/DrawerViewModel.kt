package dev.nelson.mot.main.presentations.nav

import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.util.FirebaseUtils
import dev.nelson.mot.main.util.firebase.FirebaseFeatureFlagKeys
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
        setInitialFFForDrawerItems(remoteConfig)
        setOnRemoteConfigChangeListener(remoteConfig)
    }

    fun onDestinationChanged(newRoute: String) {
        _drawerViewState.value
            .motDrawerItems
            .filter { drawerItem -> drawerItem.destination.route == newRoute }
            .map { _drawerViewState.value.copy(selectedItem = it.destination.route) }
            .map { newDrawerState -> _drawerViewState.value = newDrawerState }
    }

    /**
     * Set initial visibility for the drawer items.
     */
    private fun setInitialFFForDrawerItems(remoteConfig: FirebaseRemoteConfig) {
        if (remoteConfig.all.contains(FirebaseFeatureFlagKeys.FEATURE_DASHBOARD_ENABLED)) {
            updateDashboardFeatureAvailability(remoteConfig)
        }
        if (remoteConfig.all.contains(FirebaseFeatureFlagKeys.FEATURE_STATISTIC_ENABLED)) {
            updateStatisticFeatureAvailability(remoteConfig)
        }
    }

    /**
     * Listen feature flags update in real time. Change Drawer items visibility depending on a flag.
     */
    private fun setOnRemoteConfigChangeListener(remoteConfig: FirebaseRemoteConfig) {
        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                if (configUpdate.updatedKeys.contains(FirebaseFeatureFlagKeys.FEATURE_STATISTIC_ENABLED)) {
                    remoteConfig.activate()
                        .addOnCompleteListener { updateStatisticFeatureAvailability(remoteConfig) }
                }
                if (configUpdate.updatedKeys.contains(FirebaseFeatureFlagKeys.FEATURE_DASHBOARD_ENABLED)) {
                    remoteConfig.activate()
                        .addOnCompleteListener { updateDashboardFeatureAvailability(remoteConfig) }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                Timber.e("Config update error with code: " + error.code, error)
            }
        })
    }

    private fun updateDashboardFeatureAvailability(remoteConfig: FirebaseRemoteConfig) {
        remoteConfig.all.getValue(FirebaseFeatureFlagKeys.FEATURE_DASHBOARD_ENABLED).asBoolean()
            .let { isEnabled ->
                val item =
                    _drawerViewState.value.motDrawerItems.find { it.destination is Dashboard }
                val updatedDrawerItem = item?.copy(isAvailable = isEnabled)
                updatedDrawerItem?.let {
                    replaceDrawerItem(
                        item,
                        updatedDrawerItem,
                        selectNewItem = true
                    )
                }
            }
    }

    private fun updateStatisticFeatureAvailability(remoteConfig: FirebaseRemoteConfig) {
        remoteConfig.all.getValue(FirebaseFeatureFlagKeys.FEATURE_STATISTIC_ENABLED).asBoolean()
            .let { isEnabled ->
                val item =
                    _drawerViewState.value.motDrawerItems.find { it.destination is Statistic }
                val updatedDrawerItem = item?.copy(isAvailable = isEnabled)
                updatedDrawerItem?.let { replaceDrawerItem(item, updatedDrawerItem) }
            }
    }

    private fun replaceDrawerItem(
        oldItem: MotDrawerItem,
        newItem: MotDrawerItem,
        selectNewItem: Boolean = false
    ) {
        val index = _drawerViewState.value.motDrawerItems.indexOf(oldItem)
        val newDrawerItems = _drawerViewState.value.motDrawerItems.toMutableList()
        newDrawerItems[index] = newItem
        _drawerViewState.value = if (selectNewItem) {
            _drawerViewState.value.copy(
                motDrawerItems = newDrawerItems,
                selectedItem = newItem.destination.route
            )
        } else {
            _drawerViewState.value.copy(motDrawerItems = newDrawerItems)
        }
    }
}
