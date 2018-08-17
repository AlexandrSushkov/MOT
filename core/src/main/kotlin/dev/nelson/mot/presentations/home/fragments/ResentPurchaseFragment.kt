package dev.nelson.mot.presentations.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.nelson.mot.R
import dev.nelson.mot.presentations.base.BaseFragment

class ResentPurchaseFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recent_purchase, container, false)
    }

}
