package dev.nelson.mot.main.util.extention

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun <T : ViewDataBinding> FragmentActivity.getDataBinding(@LayoutRes layoutId: Int): T =
        DataBindingUtil.setContentView(this, layoutId)

fun <T : ViewDataBinding> Fragment.getDataBinding(inflater: LayoutInflater, @LayoutRes layoutId: Int, container: ViewGroup?): T =
        DataBindingUtil.inflate(inflater, layoutId, container, false)
