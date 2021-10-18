package dev.nelson.mot.main.presentations.payment_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.nelson.mot.main.R
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.databinding.FragmentPaymentListBinding
import dev.nelson.mot.main.presentations.base.BaseFragment
import dev.nelson.mot.main.presentations.payment.PaymentDetailsFragment
import dev.nelson.mot.main.util.extention.getDataBinding

@AndroidEntryPoint
class PaymentListFragment : BaseFragment() {

    private lateinit var binding: FragmentPaymentListBinding
    private val viewModel: PaymentListViewModel by viewModels()
    private val navController by lazy { findNavController() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = getDataBinding(inflater, R.layout.fragment_payment_list, container)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val firstVisiblePosition: Int = (binding.paymentList.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        viewModel.onScrollChanged.accept(firstVisiblePosition)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
//        binding.apply {
//            drawerButton.setOnClickListener {
//                if (activity is HomeActivity) {
//                    (activity as HomeActivity).openNavigation()
//                }
//            }
//            newPaymentFab.setOnClickListener {
//                navController.navigate(R.id.paymentFragment)
//                activity?.let {
//                    val paymentFragment = PaymentFragment()
//                    paymentFragment.show(it.supportFragmentManager, paymentFragment.tag)
//                }

//            }

//        }
//        val adapter = PaymentListAdapter(viewModel.onPaymentEntityItemClickPublisher)
//        binding.paymentList.adapter = adapter
//        viewModel.paymentList.observe(viewLifecycleOwner, { paymentList ->
//            binding.paymentList.setPayments(it, viewModel.onPaymentEntityItemClickPublisher)
//            adapter.submitData(paymentList.map { PaymentListItemModel.PaymentItemModel(it) })
//            adapter.setData(paymentList.map { PaymentListItemModel.PaymentItemModel(it) })
//        })

//        viewModel.paymentListLivaData.observe(viewLifecycleOwner, { paymentList ->
//            binding.paymentList.setPayments(it, viewModel.onPaymentEntityItemClickPublisher)
//            adapter.submitData(paymentList.map { PaymentListItemModel.PaymentItemModel(it) })
//            adapter.setData(paymentList.map { PaymentListItemModel.PaymentItemModel(it) })
//        })
        with(binding) {
            paymentList.addOnScrollListener(object : OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visiblePosition: Int = (paymentList.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                    viewModel?.onScrollChanged?.accept(visiblePosition)
                }
            })
        }

        with(viewModel){
            swipeToDeleteCallbackLiveData.observe(viewLifecycleOwner, {
                val itemTouchHelper = ItemTouchHelper(it)
                itemTouchHelper.attachToRecyclerView(binding.paymentList)
            })

            swipeToDeleteAction.observe(viewLifecycleOwner, {
//                Toast.makeText(context, "sdf", Toast.LENGTH_SHORT).show()
                showUndoSnackbar()
            })

            onPaymentEntityItemEvent.observe(viewLifecycleOwner, {
//            val openPaymentDetailsAction = PaymentListFragmentDirections.goToPaymentFragment()
//                .apply { payment = it }
//            val extras = FragmentNavigatorExtras(binding.newPaymentFab to "new_payment")
//
//            navController.navigate(openPaymentDetailsAction, extras)

                val paymentFragment = PaymentDetailsFragment()
                paymentFragment.show(childFragmentManager, paymentFragment.tag)
            })
        }

    }

    private fun showUndoSnackbar() {

        val snackbar: Snackbar = Snackbar.make(
            binding.paymentListCoordinatorLayout, "afaf",
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction("R.string.snack_bar_undo") { undoDelete() }
        snackbar.show()
    }

    private fun undoDelete() {
//        mListItems.add(
//            mRecentlyDeletedItemPosition,
//            mRecentlyDeletedItem
//        )
//        notifyItemInserted(mRecentlyDeletedItemPosition)
    }

}
