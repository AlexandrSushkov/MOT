package dev.nelson.mot.main.presentations.payment

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListPopupWindow
import androidx.activity.viewModels
import androidx.compose.foundation.rememberScrollState
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
//import dev.nelson.mot.legacy.MotApplication.Companion.context
import dev.nelson.mot.main.R
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.databinding.FragmentPaymentDetailsBinding
import dev.nelson.mot.main.presentations.base.BaseActivity
import dev.nelson.mot.main.util.extention.getDataBinding
import java.util.Calendar


@AndroidEntryPoint
class PaymentDetailsActivity : BaseActivity() {

    private lateinit var binding: FragmentPaymentDetailsBinding
    private val viewModel: PaymentDetailsViewModel by viewModels()

    //    private val navController by lazy { findNavController() }
    lateinit var listPopupWindow: ListPopupWindow

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        binding = getDataBinding(R.layout.fragment_payment_details)
        binding.viewModel = viewModel


//        with(binding.paymentTitle) {
//            requestFocus()
//            setSelection(this.text.length)
//        }
        binding.category.setOnClickListener {
            listPopupWindow.show()
        }

        initListeners()

    }

//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
//        binding = getDataBinding(inflater, R.layout.fragment_payment_details, container)
//        binding.viewModel = viewModel
//        return binding.root
//    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        initListeners()
//        with(binding.paymentTitle) {
//            requestFocus()
////            setSelection(this.text.length)
//        }
//        binding.category.setOnClickListener {
//            listPopupWindow.show()
//        }
//    }

    private fun initListeners() {
//        viewModel.finishAction.observe(viewLifecycleOwner, { dismiss() })
        viewModel.finishAction.observe(this, { finish() })
        viewModel.categories.observe(this, { categoriesList ->
//            context?.let {
            listPopupWindow = ListPopupWindow(this).apply {
                isModal = true
                anchorView = binding.category
                setOnItemClickListener { parent, _, position, _ -> onAutocompleteItemClick(parent, position) }
            }
            val adapter = CategoryListAdapter(this, android.R.layout.simple_dropdown_item_1line, categoriesList)
            listPopupWindow.setAdapter(adapter)
//                listPopupWindow.show()
//            }
        })

        with(binding.paymentTitle) {
            requestFocus()
            setSelection(this.text.length)
        }
        binding.category.setOnClickListener {
            listPopupWindow.show()
        }

//        context?.let { context ->
        binding.date.setOnClickListener {
            val cldr: Calendar = Calendar.getInstance()
            val day: Int = cldr.get(Calendar.DAY_OF_MONTH)
            val month: Int = cldr.get(Calendar.MONTH)
            val year: Int = cldr.get(Calendar.YEAR)
            val picker = DatePickerDialog(
                this,
                { _, selectedYear, monthOfYear, dayOfMonth ->
                    run {
                        binding.date.text = (dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + selectedYear)
                        val selectedDateCalendar = Calendar.getInstance().apply { set(selectedYear, monthOfYear, dayOfMonth) }
                        val selectedDate = selectedDateCalendar.time
                        viewModel.dateInMills = selectedDate.time
                    }
                }, year, month, day
            )
            picker.show()
        }
//        }
        viewModel.requestTitleFieldFocusAction.observe(this, {
//            binding.paymentTitle.focu
            binding.paymentTitle.setSelection(binding.paymentTitle.text.length)
        })

    }

    private fun onAutocompleteItemClick(parent: AdapterView<*>, position: Int) {
        val selectedItem = parent.adapter.let { it as CategoryListAdapter }.getItem(position)
        selectedItem?.let {
            viewModel.onCategoryItemClick(it)
//            isSuggestionAddressSelected = true
//            aeoLobAutocompleteEditTextHelper.sendSelectedSuggestionEvent()
//            onItemClickListener.accept(it.suggestion)
//            setText(selectedItem.suggestion.primaryLine)
//            binding.category.text = it.name
            listPopupWindow.dismiss()
        }
    }

    private fun showKeyboard() {
//        context?.let {
        val imm: InputMethodManager? = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
//        }

    }

    fun hideSoftKeyboard(mEtSearch: EditText, context: Context) {
        mEtSearch.clearFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEtSearch.windowToken, 0)
    }


//    companion object {
//        fun getInstance(payment: Payment? = null): PaymentDetailsFragment {
//            val bundle = Bundle().apply {
//                putParcelable("payment", payment)
//            }
//
//            return PaymentDetailsFragment().apply { arguments = bundle }
//        }
//    }
}