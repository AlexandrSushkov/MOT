package dev.nelson.mot.interactor

import dev.nelson.mot.data.repository.RoomTestRepository
import javax.inject.Inject

class RoomTestInteractor @Inject constructor(private val roomTestRepository: RoomTestRepository) {

    fun testFun() = roomTestRepository.testFun()

    fun getAllCategories() = roomTestRepository.getCategories()

    fun initTransfer() = roomTestRepository.transferCategories()

}