package dev.nelson.mot.extentions

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dev.nelson.mot.presentations.base.BaseViewModel

fun <T : ViewDataBinding> FragmentActivity.getDataBinding(@LayoutRes layoutId: Int): T = DataBindingUtil.setContentView(this, layoutId)
fun <T : ViewDataBinding> AppCompatActivity.getDataBinding(@LayoutRes layoutId: Int): T = DataBindingUtil.setContentView(this, layoutId)

inline fun <reified T : BaseViewModel> FragmentActivity.getViewModel(factory: ViewModelProvider.Factory = ViewModelProviders.DefaultFactory(application))
        : T = ViewModelProviders.of(this, factory).get(T::class.java)

inline fun <reified T : BaseViewModel> Fragment.getViewModel(factory: ViewModelProvider.Factory = ViewModelProviders.DefaultFactory(activity!!.application))
        : T = ViewModelProviders.of(this, factory).get(T::class.java)

