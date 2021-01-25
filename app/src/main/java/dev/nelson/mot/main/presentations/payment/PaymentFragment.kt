package dev.nelson.mot.main.presentations.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.nelson.mot.main.R
import dev.nelson.mot.main.databinding.FragmentPaymentBinding
import dev.nelson.mot.main.presentations.base.EntryPointFragment
import dev.nelson.mot.main.util.extention.getDataBinding

class PaymentFragment: BottomSheetDialogFragment(){

    private lateinit var binding: FragmentPaymentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = getDataBinding(inflater, R.layout.fragment_payment, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}