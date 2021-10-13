package dev.nelson.mot.main.presentations.payment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ListPopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import dev.nelson.mot.main.R
import dev.nelson.mot.main.presentations.base.BaseActivity
import dev.nelson.mot.main.presentations.category_details.AutocompleteAdapter
import timber.log.Timber

class PaymentActivity : BaseActivity() {

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, PaymentActivity::class.java)
    }

    lateinit var actv: AutoCompleteTextView
    private var isHideTypeahead = true

    override fun onCreate(savedInstanceState: Bundle?) {
        // Enable Activity Transitions. Optionally enable Activity transitions in your
        // theme with <item name=”android:windowActivityTransitions”>true</item>.
//        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)

        // Set the transition name, which matches Activity A’s start view transition name, on
        // the root view.
//        findViewById<View>(android.R.id.content).transitionName = "new_payment"

        // Attach a callback used to receive the shared elements from Activity A to be
        // used by the container transform transition.
//        val sharedElementCallback = MaterialContainerTransformSharedElementCallback()
//        setEnterSharedElementCallback(sharedElementCallback)

        // Set this Activity’s enter and return transition to a MaterialContainerTransform
//        window.sharedElementEnterTransition = MaterialContainerTransform().apply {
//            addTarget(android.R.id.content)
//            duration = 1000L
//        }
//        window.sharedElementReturnTransition = MaterialContainerTransform().apply {
//            addTarget(android.R.id.content)
//            duration = 250L
//        }


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_payment)


        val Countries = arrayOf("India", "USA", "Australia", "UK", "Italy", "Ireland", "Africa")
        val objects = listOf("India", "USA", "Australia", "UK", "Italy", "Ireland", "Africa","Italy", "Ireland","Italy", "Ireland","Italy", "Ireland","Italy", "Ireland","Italy", "Ireland","Italy", "Ireland")

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Countries)

        val footerAdapter = AutocompleteAdapter(this, android.R.layout.simple_dropdown_item_1line, objects)
//        val autocompleteHint = findViewById<View>(android.R.id.text1) as TextView


        val actv: AutoCompleteTextView = findViewById<View>(R.id.autocomplete_text) as AutoCompleteTextView
//        val myAuto: AutoCompleteTextView = findViewById<View>(R.id.my_auto_text) as AutoCompleteTextView
//        actv.threshold = 3
//        actv.setAdapter(footerAdapter)
        actv.setAdapter(adapter)
//        actv.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
//            Toast.makeText(applicationContext, "Selected Item: " + parent.selectedItem, Toast.LENGTH_SHORT).show()
//        }

//        val string = "my spannable sting"
//        val spanString = string.toSpannableString()
//        val clickableSpan: ClickableSpan = object : ClickableSpan() {
//            override fun onClick(textView: View) {
//                Toast.makeText(applicationContext, "on hint click", Toast.LENGTH_SHORT).show()
//                Timber.e("on hint click")
//            }
//
//            override fun updateDrawState(ds: TextPaint) {
//                super.updateDrawState(ds)
//                ds.isUnderlineText = false
//            }
//        }

//        val clickableSpan: ClickableSpan = object : ClickableSpan() {
//            override fun onClick(textView: View) {
//                Toast.makeText(applicationContext, "on hint click", Toast.LENGTH_SHORT).show()
//                Timber.e("on hint click")
//            }
//        }
//
//        spanString.setSpan(clickableSpan, 0, string.length , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


//        actv.completionHint = SpannableString(spanString)
//        val autocompleteHint = actv.findViewById<TextView>(android.R.id.text1)
//        autocompleteHint?.setOnClickListener { Toast.makeText(applicationContext, "Selected Item: FOOTER" , Toast.LENGTH_SHORT).show() }


//        val ss = SpannableString("Android is a Software stack")
//
//        val clickableSpan: ClickableSpan = object : ClickableSpan() {
//            override fun onClick(textView: View) {
//                Toast.makeText(applicationContext, "on hint click", Toast.LENGTH_SHORT).show()
//                Timber.e("on hint click")
//            }
//
//            override fun updateDrawState(ds: TextPaint) {
//                super.updateDrawState(ds)
//                ds.isUnderlineText = false
//            }
//        }
//
//        ss.setSpan(clickableSpan, 22, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//
        val text3 = findViewById<TextView>(R.id.text3)
//        text3.text = ss


        text3.movementMethod = LinkMovementMethod.getInstance()
//        actv.doOnTextChanged { text, start, before, count ->
//            Timber.e("text: $text, start: $start, before: $before, count: $count")
//            if(isHideTypeahead && count >= 3){
//                actv.setAdapter(adapter)
//            }
//        }


        val listPopup = ListPopupWindow(this)
        listPopup.setAdapter(adapter)
        listPopup.anchorView = text3

        val hintView = LayoutInflater.from(this).inflate(R.layout.item_footer, null).findViewById<View>(android.R.id.text1) as TextView

        listPopup.setPromptView(hintView)
        listPopup.promptPosition = ListPopupWindow.POSITION_PROMPT_BELOW


        text3.setOnClickListener {
            listPopup.show()

        }

    }

    fun completionHintViewClick(v: View?) {
        Toast.makeText(applicationContext, "on hint click", Toast.LENGTH_SHORT).show()
        Timber.e("on hint click")

//        actv.setAdapter(null)
//        actv.dismissDropDown()
        isHideTypeahead = false

    }


    private fun showKeyboard() {
        val imm: InputMethodManager? = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)


    }
}

