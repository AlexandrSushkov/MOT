package dev.nelson.mot.main.presentations.screen.payment_list.actions

sealed class OpenPaymentDetailsAction {
    class ExistingPayment(val id: Int) : OpenPaymentDetailsAction()
    object NewPayment : OpenPaymentDetailsAction()
}
