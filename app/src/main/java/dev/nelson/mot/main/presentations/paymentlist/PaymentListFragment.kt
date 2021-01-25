 package dev.nelson.mot.main.presentations.paymentlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dev.nelson.mot.main.R
import dev.nelson.mot.main.databinding.FragmentPaymentListBinding
import dev.nelson.mot.main.presentations.base.EntryPointFragment
import dev.nelson.mot.main.presentations.home.HomeActivity
import dev.nelson.mot.main.util.extention.getDataBinding

 class PaymentListFragment : EntryPointFragment() {

    private lateinit var binding: FragmentPaymentListBinding
    private val viewModel: PaymentListViewModel by viewModels()
    private val navController by lazy { findNavController() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = getDataBinding(inflater, R.layout.fragment_payment_list, container)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            drawerButton.setOnClickListener {
                if (activity is HomeActivity) {
                    (activity as HomeActivity).openNavigation()
                }
            }
            newPaymentFab.setOnClickListener {
                navController.navigate(R.id.paymentFragment)
            }

        }

        viewModel.onPaymentItemEvent.observe(viewLifecycleOwner, Observer { navController.navigate(R.id.paymentFragment) })
    }

}


