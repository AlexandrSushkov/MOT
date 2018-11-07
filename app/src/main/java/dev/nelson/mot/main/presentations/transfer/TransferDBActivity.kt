package dev.nelson.mot.main.presentations.transfer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.nelson.mot.main.R
import dev.nelson.mot.main.data.repository.DBTransferRepository

class TransferDBActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer_db)
        val dbTransferRepository = DBTransferRepository(this)
//        dbTransferRepository.transferPayments()
    }
}