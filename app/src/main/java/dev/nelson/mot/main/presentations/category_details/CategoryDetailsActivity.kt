package dev.nelson.mot.main.presentations.category_details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.annotation.LayoutRes
import dev.nelson.mot.main.R
import dev.nelson.mot.main.presentations.base.BaseActivity

class CategoryDetailsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_details)

        val Countries = arrayOf("India", "USA", "Australia", "UK", "Italy", "Ireland", "Africa")
        val objects = listOf("India", "USA", "Australia", "UK", "Italy", "Ireland", "Africa")

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Countries)

        val footerAdapter = AutocompleteAdapter(this,android.R.layout.simple_dropdown_item_1line, objects)

        val actv: AutoCompleteTextView = findViewById<View>(R.id.autocomplete_text) as AutoCompleteTextView
        actv.threshold = 1
        actv.setAdapter(footerAdapter)
//        actv.setAdapter(adapter)
//        actv.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
//            Toast.makeText(applicationContext, "Selected Item: " + parent.selectedItem, Toast.LENGTH_SHORT).show()
//        }
//        actv.compl

    }

}

class AutocompleteAdapter(context: Context, @LayoutRes resource: Int, objects: List<String>) : ArrayAdapter<String>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return if (position == 0) {
            LayoutInflater.from(context).inflate(R.layout.item_footer, parent, false)
        } else {
            super.getView(position, convertView, parent)
        }
    }

}
