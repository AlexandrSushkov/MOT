package dev.nelson.mot.db.model.paymentjoin

import androidx.room.Embedded
import androidx.room.Relation
import dev.nelson.mot.db.model.category.CategoryEntity
import dev.nelson.mot.db.model.category.CategoryTable
import dev.nelson.mot.db.model.payment.PaymentEntity
import dev.nelson.mot.db.model.payment.PaymentTable

data class PaymentWithCategory(
    @Embedded var paymentEntity: PaymentEntity,
    @Relation(
        parentColumn = PaymentTable.CATEGORY_ID_KEY,
        entityColumn = CategoryTable.ID
    ) val categoryEntity: CategoryEntity? = null
)
