package dev.nelson.mot.main.presentations.screen.payments.actions

sealed class OpenPaymentDetailsAction {
    class ExistingPayment(val paymentId: Int) : OpenPaymentDetailsAction()
    data object NewPayment : OpenPaymentDetailsAction()
    class NewPaymentForCategory(val categoryId: Int?) : OpenPaymentDetailsAction()
}
