package dev.nelson.mot.main.util.compose

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.Payment

object PreviewData {
    val previewCategory = Category("category")
    val previewPayment = Payment("payment name", 100, category = previewCategory)
}

class PaymentPreviewProvider : PreviewParameterProvider<Payment> {
    val category = Category("category")
    override val values = sequenceOf(Payment("payment name", 100, category = category))
}

class CategoryPreviewProvider : PreviewParameterProvider<Category> {
    override val values = sequenceOf(Category("category"))
}