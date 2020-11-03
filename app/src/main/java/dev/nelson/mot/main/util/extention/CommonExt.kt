package dev.nelson.mot.main.util.extention

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.nelson.mot.main.presentations.base.BaseViewModel

fun <T : ViewDataBinding> FragmentActivity.getDataBinding(@LayoutRes layoutId: Int): T =
        DataBindingUtil.setContentView(this, layoutId)

fun <T : ViewDataBinding> Fragment.getDataBinding(inflater: LayoutInflater, @LayoutRes layoutId: Int, container: ViewGroup?): T =
        DataBindingUtil.inflate(inflater, layoutId, container, false)

inline fun <reified T : BaseViewModel> FragmentActivity.getViewModel(factory: ViewModelProvider.Factory = ViewModelProvider.AndroidViewModelFactory(application)): T =
    ViewModelProvider(this, factory).get(T::class.java)

inline fun <reified T : BaseViewModel> Fragment.getViewModel(factory: ViewModelProvider.Factory = ViewModelProvider.AndroidViewModelFactory(activity!!.application)): T =
    ViewModelProvider(this, factory).get(T::class.java)

/**
 * Like [Fragment.viewModelProvider] for Fragments that want a [ViewModel] scoped to the Activity.
 */
inline fun <reified VM : ViewModel> Fragment.activityViewModelProvider(provider: ViewModelProvider.Factory) =
    ViewModelProvider(requireActivity(), provider).get(VM::class.java)
