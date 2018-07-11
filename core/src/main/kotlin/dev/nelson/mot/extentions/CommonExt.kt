package dev.nelson.mot.extentions

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import dev.nelson.mot.presentations.base.BaseViewModel

fun <T : ViewDataBinding> FragmentActivity.getDataBinding(@LayoutRes layoutId: Int): T = DataBindingUtil.setContentView(this, layoutId)
fun <T : ViewDataBinding> AppCompatActivity.getDataBinding(@LayoutRes layoutId: Int): T = DataBindingUtil.setContentView(this, layoutId)

inline fun <reified T : BaseViewModel> FragmentActivity.getViewModel(factory: ViewModelProvider.Factory = ViewModelProviders.DefaultFactory(application))
        : T = ViewModelProviders.of(this, factory).get(T::class.java)

inline fun <reified T : BaseViewModel> Fragment.getViewModel(factory: ViewModelProvider.Factory = ViewModelProviders.DefaultFactory(activity!!.application))
        : T = ViewModelProviders.of(this, factory).get(T::class.java)

