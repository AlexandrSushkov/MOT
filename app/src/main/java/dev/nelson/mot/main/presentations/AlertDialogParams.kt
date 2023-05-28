package dev.nelson.mot.main.presentations

import androidx.annotation.StringRes

data class AlertDialogParams(
    @StringRes val message: Int,
    val dismissClickCallback: () -> Unit,
    val onPositiveClickCallback: () -> Unit,
    val onNegativeClickCallback: (() -> Unit)? = null
)
