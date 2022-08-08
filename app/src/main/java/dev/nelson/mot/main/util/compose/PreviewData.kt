package dev.nelson.mot.main.util.compose

import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.Payment

object PreviewData {
    val previewCategory = Category("category")
    val previewPayment = Payment("payment name", 100, category = previewCategory)
}