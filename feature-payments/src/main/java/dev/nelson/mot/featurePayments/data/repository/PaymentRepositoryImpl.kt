package dev.nelson.mot.featurePayments.data.repository

import dev.nelson.mot.core.TestDB
import dev.nelson.mot.core.TestDBImpl

class PaymentRepositoryImpl : PaymentRepository {

    override fun getPayment(): String {
        val testDb: TestDB = TestDBImpl()
        return testDb.getDB()
    }
}

interface PaymentRepository {
    fun getPayment(): String
}