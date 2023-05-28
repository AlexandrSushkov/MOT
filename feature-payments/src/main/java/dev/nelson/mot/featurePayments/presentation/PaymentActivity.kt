package dev.nelson.mot.featurePayments.presentation

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import dev.nelson.mot.featurePayments.R
import dev.nelson.mot.featurePayments.domain.useCase.PaymentUseCase

class PaymentActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feature_payment)
        val textView: TextView = findViewById(R.id.payment_text)
        val paymentUseCase = PaymentUseCase()
        textView.text = paymentUseCase.getPayment()
    }
}
