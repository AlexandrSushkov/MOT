package dev.nelson.mot.main.presentations

data class AlertDialogParams(
    val message: String,
    val dismissClickCallback: () -> Unit,
    val onPositiveClickCallback: () -> Unit,
    val onNegativeClickCallback: (() -> Unit)? = null
)
