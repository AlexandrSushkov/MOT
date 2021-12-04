package dev.nelson.mot.main.presentations.payment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.nelson.mot.main.R
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.databinding.FragmentPaymentDetailsBinding
import dev.nelson.mot.main.presentations.category_details.CategoryDetailsFragment
import dev.nelson.mot.main.util.extention.getDataBinding

@AndroidEntryPoint
class PaymentDetailsFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentPaymentDetailsBinding
    private val viewModel: PaymentDetailsViewModel by viewModels()
    private val navController by lazy { findNavController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = getDataBinding(inflater, R.layout.fragment_payment_details, container)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        with(binding.paymentTitle){
            requestFocus()
//            setSelection(this.text.length)
        }
    }

    private fun initListeners() {
        viewModel.finishAction.observe(viewLifecycleOwner, { dismiss() })
    }

    private fun showKeyboard() {
        context?.let {
            val imm: InputMethodManager? = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
        }

    }

    fun hideSoftKeyboard(mEtSearch: EditText, context: Context) {
        mEtSearch.clearFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEtSearch.windowToken, 0)
    }


    companion object{
        fun getInstance(payment: Payment? = null) : PaymentDetailsFragment {
            val bundle = Bundle().apply {
                putParcelable("payment", payment)
            }

            return PaymentDetailsFragment().apply { arguments = bundle }
        }
    }
}