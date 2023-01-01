package com.tawajood.vet.ui.main.orders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tawajood.vet.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreviousOrdersFragment : Fragment(R.layout.fragment_previous_orders) {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_previous_orders, container, false)
    }


}