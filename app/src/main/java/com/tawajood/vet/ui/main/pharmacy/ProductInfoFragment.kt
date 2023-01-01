package com.tawajood.vet.ui.main.pharmacy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.tawajood.vet.R
import com.tawajood.vet.adapters.ProductsAdapter
import com.tawajood.vet.databinding.FragmentProductInfoBinding
import com.tawajood.vet.databinding.FragmentProductsBinding
import com.tawajood.vet.pojo.Product
import com.tawajood.vet.pojo.Subcategory
import com.tawajood.vet.ui.main.MainActivity
import com.tawajood.vet.utils.Constants
import com.tawajood.vet.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlin.properties.Delegates

@AndroidEntryPoint
class ProductInfoFragment : Fragment(R.layout.fragment_product_info) {


    private lateinit var binding: FragmentProductInfoBinding
    private lateinit var parent: MainActivity
    private val viewModel: PharmacyViewModel by viewModels()
    private lateinit var product: Product
    private var productId by Delegates.notNull<Int>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProductInfoBinding.bind(requireView())
        parent = requireActivity() as MainActivity
        productId = requireArguments().getInt(Constants.PRODUCT_ID)

        viewModel.getProductById(productId.toString())

        setupUI()
        observeData()
        onclick()


    }

    private fun onclick() {
        binding.btn.setOnClickListener {
            viewModel.addToCart(productId.toString(), "1")
        }
    }

    private fun setupUI() {
        parent.isPharmacy(true)
        parent.setTitle(getString(R.string.pharmacy))
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getProductFlow.collectLatest {
                parent.hideLoading()
                when (it) {
                    is Resource.Error -> {
                        ToastUtils.showToast(requireContext(), it.message.toString())
                    }
                    is Resource.Idle -> {

                    }
                    is Resource.Loading -> parent.showLoading()
                    is Resource.Success -> {
                        product = it.data!!.product

                        Glide.with(requireContext()).load(product.images[0].image).into(binding.img)
                        binding.name.text = product.name
                        binding.details.text = product.desc
                        binding.price.text = product.price.toString() + getString(R.string.Rs)

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
                        ToastUtils.showToast(requireContext(), "تمت اضافة المنتج الى سلة المشتريات")
                    }
                }
            }
        }
    }


}