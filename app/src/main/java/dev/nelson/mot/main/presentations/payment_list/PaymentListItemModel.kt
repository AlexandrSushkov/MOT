package dev.nelson.mot.main.presentations.payment_list

import dev.nelson.mot.main.data.model.Payment

sealed class PaymentListItemModel {

    class PaymentItemModel(val payment: Payment) : PaymentListItemModel()

    object Header : PaymentListItemModel()

    object Footer : PaymentListItemModel()

}