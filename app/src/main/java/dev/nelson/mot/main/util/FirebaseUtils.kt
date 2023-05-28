package dev.nelson.mot.main.util

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig

object FirebaseUtils {
    val remoteConfig: FirebaseRemoteConfig
        get() = Firebase.remoteConfig
}
