package dev.nelson.mot.main.util.extention

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showToast(message: String) {
    this.activity?.let { Toast.makeText(it, message, Toast.LENGTH_SHORT).show() }
}
