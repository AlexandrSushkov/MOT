package dev.nelson.mot.main.presentations.movieslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import dev.nelson.mot.main.R
import dev.nelson.mot.main.databinding.FragmentMoviesListBinding
import dev.nelson.mot.main.presentations.base.BaseFragment
import dev.nelson.mot.main.presentations.payment.PaymentActivity
import dev.nelson.mot.main.util.extention.getDataBinding
import dev.nelson.mot.main.widget.BottomSheetBehavior

@AndroidEntryPoint
class MoviesListFragment : BaseFragment() {

    private lateinit var binding: FragmentMoviesListBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private val viewModel: MoviesListViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = getDataBinding(inflater, R.layout.fragment_movies_list, container)
        binding.viewModel = viewModel
        viewModel.apply {
            onItemClickEvent.observe(viewLifecycleOwner, Observer {
//            val options = ActivityOptions.makeSceneTransitionAnimation(this, binding.fab, "new_payment")
                context?.let { startActivity(PaymentActivity.getIntent(it)) }
            })
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        bottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.filter_sheet))
    }

    fun expandFilterFragment() {
//        when (bottomSheetBehavior.state) {
//            BottomSheetBehavior.STATE_EXPANDED -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
//            BottomSheetBehavior.STATE_HIDDEN -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
//            BottomSheetBehavior.STATE_HALF_EXPANDED -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
//            BottomSheetBehavior.STATE_COLLAPSED -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
//        }
    }

    fun collapseFilterFragment() {
//        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    fun isFilterOpen(): Boolean {
//        bottomSheetBehavior.state == BottomSheetBehavior.STATE_HALF_EXPANDED || bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED
        return false
    }
}
