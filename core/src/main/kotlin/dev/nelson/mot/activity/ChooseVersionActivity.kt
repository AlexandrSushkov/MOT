package dev.nelson.mot.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import dev.nelson.mot.R
import dev.nelson.mot.presentations.screen.DataBaseTransferActivity

class ChooseVersionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_version)
        val btnVersion1: Button = findViewById(R.id.button_version_1)
        val btnVersion2: Button = findViewById(R.id.button_version_2)
        val btnVersion3: Button = findViewById(R.id.transfer_database)

        btnVersion1.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }
        btnVersion2.setOnClickListener {  }
        btnVersion3.setOnClickListener { startActivity(Intent(this, DataBaseTransferActivity::class.java)) }
    }
}
