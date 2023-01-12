package com.tawajood.vet.ui.main.orders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tawajood.vet.R
import com.tawajood.vet.adapters.CartAdapter
import com.tawajood.vet.adapters.PreviousOrdersAdapter
import com.tawajood.vet.databinding.FragmentCartBinding
import com.tawajood.vet.databinding.FragmentPreviousOrdersBinding
import com.tawajood.vet.pojo.Cart
import com.tawajood.vet.pojo.Order
import com.tawajood.vet.ui.main.MainActivity
import com.tawajood.vet.ui.main.cart.CartViewModel
import com.tawajood.vet.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class PreviousOrdersFragment : Fragment(R.layout.fragment_previous_orders) {


    private lateinit var binding: FragmentPreviousOrdersBinding
    private lateinit var parent: MainActivity
    private val viewModel: OrdersViewModel by viewModels()
    private lateinit var previousOrdersAdapter: PreviousOrdersAdapter
    private var orders = mutableListOf<Order>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPreviousOrdersBinding.bind(requireView())
        parent = requireActivity() as MainActivity


        setupUI()
        onClick()
        observeData()
        setupRec()
    }

    private fun setupRec() {
        previousOrdersAdapter = PreviousOrdersAdapter(object :PreviousOrdersAdapter.OnItemClick{
            override fun onItemClickListener(position: Int) {

            }

        })

        binding.rvOrders.adapter = previousOrdersAdapter
    }

    private fun onClick() {

    }

    private fun setupUI() {
        parent.showBottomNav(false)
        parent.setTitle(getString(R.string.previous_orders))
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getOrdersFlow.collectLatest {
                parent.hideLoading()
                when (it) {
                    is Resource.Error -> {
                        ToastUtils.showToast(requireContext(), it.message.toString())
                    }
                    is Resource.Idle -> {

                    }
                    is Resource.Loading -> parent.showLoading()
                    is Resource.Success -> {
                        orders = it.data!!.orders
                        previousOrdersAdapter.orders = orders
                    }
                }
            }
        }
    }


}