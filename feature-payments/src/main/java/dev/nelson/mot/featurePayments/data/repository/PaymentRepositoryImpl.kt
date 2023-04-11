package dev.nelson.mot.featurePayments.data.repository

import dev.nelson.mot.core.TestCoreClass

class PaymentRepositoryImpl : PaymentRepository {

    override fun getPayment(): String {
        val testDb: TestDB = TestCoreClass()
        return testDb.getDB()
    }
}

interface PaymentRepository {
    fun getPayment(): String
}