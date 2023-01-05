package com.tawajood.vet.ui.main.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tawajood.vet.R
import com.tawajood.vet.adapters.CartAdapter
import com.tawajood.vet.adapters.DoctorsAdapter
import com.tawajood.vet.databinding.FragmentCartBinding
import com.tawajood.vet.databinding.FragmentSearchBinding
import com.tawajood.vet.pojo.Cart
import com.tawajood.vet.ui.main.MainActivity
import com.tawajood.vet.ui.main.search.SearchViewModel
import com.tawajood.vet.utils.Constants
import com.tawajood.vet.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart) {

    private lateinit var binding: FragmentCartBinding
    private lateinit var parent: MainActivity
    private val viewModel: CartViewModel by viewModels()
    private var carts = mutableListOf<Cart>()
    private lateinit var cartAdapter: CartAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCartBinding.bind(requireView())
        parent = requireActivity() as MainActivity

        setupCart()
        setupUI()
        onClick()
        observeData()
    }

    private fun setupCart() {
        cartAdapter = CartAdapter(object : CartAdapter.OnItemClick {
            override fun onDeleteClicked(position: Int) {
                viewModel.deleteItemCart(carts[position].id.toString())
            }

        })
        binding.rvItems.adapter = cartAdapter
    }

    private fun setupUI() {
        parent.showBottomNav(false)
        parent.setTitle(getString(R.string.cart))
    }

    private fun onClick() {

    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getCartFlow.collectLatest {
                parent.hideLoading()
                when (it) {
                    is Resource.Error -> {
                        ToastUtils.showToast(requireContext(), it.message.toString())
                    }
                    is Resource.Idle -> {

                    }
                    is Resource.Loading -> parent.showLoading()
                    is Resource.Success -> {
                        val prices = it.data
                        carts = it.data!!.carts
                        cartAdapter.carts = carts

                        binding.totalPrice.text = prices!!.total + getString(R.string.Rs)
                        binding.tax.text = prices.tax + getString(R.string.Rs)
                        binding.shipping.text = prices.delivery_cost + getString(R.string.Rs)
                        binding.total.text = prices.finalTotal + getString(R.string.Rs)
                    }
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.deleteItemFlow.collectLatest {
                parent.hideLoading()
                when (it) {
                    is Resource.Error -> {
                        ToastUtils.showToast(requireContext(), it.message.toString())
                    }
                    is Resource.Idle -> {

                    }
                    is Resource.Loading -> parent.showLoading()
                    is Resource.Success -> {

                    }
                }
            }
        }
    }


}