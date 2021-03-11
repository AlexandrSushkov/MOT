package dev.nelson.mot.main.data.room.model.paymentjoin

import androidx.room.Embedded
import androidx.room.Relation
import dev.nelson.mot.main.data.room.model.category.CategoryEntity
import dev.nelson.mot.main.data.room.model.category.CategoryTable
import dev.nelson.mot.main.data.room.model.payment.PaymentEntity
import dev.nelson.mot.main.data.room.model.payment.PaymentTable

data class PaymentWithCategory(
    @Embedded var paymentEntity: PaymentEntity,
    @Relation(
        parentColumn = PaymentTable.CATEGORY_ID_KEY,
        entityColumn = CategoryTable.ID
    ) val categoryEntity: CategoryEntity? = null
)
