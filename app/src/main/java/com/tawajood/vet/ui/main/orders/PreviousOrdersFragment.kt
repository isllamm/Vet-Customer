package com.tawajood.vet.ui.main.orders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.tawajood.vet.R
import com.tawajood.vet.adapters.CartAdapter
import com.tawajood.vet.databinding.FragmentCartBinding
import com.tawajood.vet.databinding.FragmentPreviousOrdersBinding
import com.tawajood.vet.pojo.Cart
import com.tawajood.vet.ui.main.MainActivity
import com.tawajood.vet.ui.main.cart.CartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreviousOrdersFragment : Fragment(R.layout.fragment_previous_orders) {


    private lateinit var binding: FragmentPreviousOrdersBinding
    private lateinit var parent: MainActivity
    private val viewModel: OrdersViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPreviousOrdersBinding.bind(requireView())
        parent = requireActivity() as MainActivity


        setupUI()
        onClick()
        observeData()
    }

    private fun onClick() {

    }

    private fun setupUI() {
        parent.showBottomNav(false)
        parent.setTitle(getString(R.string.previous_orders))
    }

    private fun observeData() {

    }


}