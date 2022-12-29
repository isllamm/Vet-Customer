package com.tawajood.vet.ui.main.pharmacy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tawajood.vet.R
import com.tawajood.vet.adapters.CategoriesAdapter
import com.tawajood.vet.adapters.ProductsAdapter
import com.tawajood.vet.databinding.FragmentPharmacyBinding
import com.tawajood.vet.databinding.FragmentProductsBinding
import com.tawajood.vet.pojo.Category
import com.tawajood.vet.pojo.Product
import com.tawajood.vet.pojo.Subcategory
import com.tawajood.vet.ui.main.MainActivity
import com.tawajood.vet.utils.Constants
import com.tawajood.vet.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlin.properties.Delegates

@AndroidEntryPoint
class ProductsFragment : Fragment(R.layout.fragment_products) {


    private lateinit var binding: FragmentProductsBinding
    private lateinit var parent: MainActivity
    private val viewModel: PharmacyViewModel by viewModels()
    private var subcategories = mutableListOf<Subcategory>()
    private var products = mutableListOf<Product>()
    private var vendorId by Delegates.notNull<Int>()
    private lateinit var typeAdapter: ArrayAdapter<String>
    private lateinit var productsAdapter: ProductsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProductsBinding.bind(requireView())
        parent = requireActivity() as MainActivity
        vendorId = requireArguments().getInt(Constants.VENDOR_ID)

        setupUI()
        observeData()
        setupSpinner()
        setupProducts()
        onclick()


    }

    private fun onclick() {
        binding.typeSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.getProducts(subcategories[position].id.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }
    }

    private fun setupProducts() {
        productsAdapter = ProductsAdapter(object : ProductsAdapter.OnItemClick {
            override fun onItemClickListener(position: Int) {
                parent.navController.navigate(
                    R.id.productInfoFragment,
                    bundleOf(Constants.PRODUCT_ID to products[position].id)
                )
            }

            override fun onAddToCartClick(position: Int) {
                viewModel.addToCart(products[position].id.toString(), "1")
            }

        })
        binding.rvProducts.adapter = productsAdapter
    }

    private fun setupSpinner() {
        typeAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item)
        binding.typeSpinner.adapter = typeAdapter
    }

    private fun setupUI() {
        viewModel.getSubcategories(vendorId.toString())
        parent.isPharmacy(true)
        parent.setTitle(getString(R.string.pharmacy))
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getSubcategoriesFlow.collectLatest {
                parent.hideLoading()
                when (it) {
                    is Resource.Error -> {
                        ToastUtils.showToast(requireContext(), it.message.toString())
                    }
                    is Resource.Idle -> {

                    }
                    is Resource.Loading -> parent.showLoading()
                    is Resource.Success -> {
                        subcategories = it.data!!.subcategories

                        if (subcategories.isNotEmpty()) {
                            subcategories.forEach { item ->
                                typeAdapter.add(item.name)
                            }
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getProductsFlow.collectLatest {
                parent.hideLoading()
                when (it) {
                    is Resource.Error -> {
                        ToastUtils.showToast(requireContext(), it.message.toString())
                    }
                    is Resource.Idle -> {

                    }
                    is Resource.Loading -> parent.showLoading()
                    is Resource.Success -> {
                        products = it.data!!.products.data
                        if (products.isNotEmpty()) {
                            productsAdapter.products = products
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addToCartFlow.collectLatest {
                parent.hideLoading()
                when (it) {
                    is Resource.Error -> {
                        ToastUtils.showToast(requireContext(), it.message.toString())
                    }
                    is Resource.Idle -> {

                    }
                    is Resource.Loading -> parent.showLoading()
                    is Resource.Success -> {
                        ToastUtils.showToast(requireContext(), it.message.toString())
                    }
                }
            }
        }
    }


}