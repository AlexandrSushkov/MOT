package dev.nelson.mot.main.utils.extention

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dev.nelson.mot.main.presentations.base.BaseViewModel

fun <T : ViewDataBinding> FragmentActivity.getDataBinding(@LayoutRes layoutId: Int): T =
        DataBindingUtil.setContentView<T>(this, layoutId)

fun <T : ViewDataBinding> Fragment.getDataBinding(inflater: LayoutInflater, @LayoutRes layoutId: Int, container: ViewGroup?): T =
        DataBindingUtil.inflate(inflater, layoutId, container, false)

inline fun <reified T : BaseViewModel> Fragment.getViewModel(factory: ViewModelProvider.Factory = ViewModelProviders.DefaultFactory(activity!!.application)): T =
        ViewModelProviders.of(this, factory).get(T::class.java)