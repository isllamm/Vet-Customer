package com.tawajood.vet.ui.main.pharmacy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tawajood.vet.R
import com.tawajood.vet.adapters.VendorsAdapter
import com.tawajood.vet.databinding.FragmentVendorsBinding
import com.tawajood.vet.pojo.Vendor
import com.tawajood.vet.ui.main.MainActivity
import com.tawajood.vet.utils.Constants
import com.tawajood.vet.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlin.properties.Delegates

@AndroidEntryPoint
class VendorsFragment : Fragment(R.layout.fragment_vendors) {

    private lateinit var binding: FragmentVendorsBinding
    private lateinit var parent: MainActivity
    private val viewModel: PharmacyViewModel by viewModels()
    private var catId by Delegates.notNull<Int>()
    private var vendors = mutableListOf<Vendor>()
    private lateinit var vendorsAdapter: VendorsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentVendorsBinding.bind(requireView())
        parent = requireActivity() as MainActivity
        catId = requireArguments().getInt(Constants.CAT_ID)

        setupUI()
        observeData()
        setupVendors()

    }

    private fun setupVendors() {
        vendorsAdapter = VendorsAdapter(object : VendorsAdapter.OnItemClick {
            override fun onItemClickListener(position: Int) {

            }

        })
        binding.rvVendors.adapter = vendorsAdapter
    }

    private fun setupUI() {
        viewModel.getVendors(catId.toString())
        parent.isPharmacy(true)
        parent.setTitle(getString(R.string.pharmacy))
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getVendorsFlow.collectLatest {
                parent.hideLoading()
                when (it) {
                    is Resource.Error -> {
                        ToastUtils.showToast(requireContext(), it.message.toString())
                    }
                    is Resource.Idle -> {

                    }
                    is Resource.Loading -> parent.showLoading()
                    is Resource.Success -> {

                        vendors = it.data!!.vendors.data
                        vendorsAdapter.vendors = vendors
                    }
                }
            }
        }
    }


}