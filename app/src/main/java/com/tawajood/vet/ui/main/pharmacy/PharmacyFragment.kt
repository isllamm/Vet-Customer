package com.tawajood.vet.ui.main.pharmacy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tawajood.vet.R
import com.tawajood.vet.adapters.CategoriesAdapter
import com.tawajood.vet.adapters.NotificationAdapter
import com.tawajood.vet.databinding.FragmentNotificationsBinding
import com.tawajood.vet.databinding.FragmentPharmacyBinding
import com.tawajood.vet.pojo.Category
import com.tawajood.vet.ui.main.MainActivity
import com.tawajood.vet.ui.main.notifications.NotificationsViewModel
import com.tawajood.vet.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class PharmacyFragment : Fragment(R.layout.fragment_pharmacy) {


    private lateinit var binding: FragmentPharmacyBinding
    private lateinit var parent: MainActivity
    private val viewModel: PharmacyViewModel by viewModels()
    private lateinit var categoriesAdapter: CategoriesAdapter
    private var categories = mutableListOf<Category>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPharmacyBinding.bind(requireView())
        parent = requireActivity() as MainActivity

        setupUI()
        observeData()
        setupCategories()

    }

    private fun setupCategories() {
        categoriesAdapter = CategoriesAdapter(object : CategoriesAdapter.OnItemClick {
            override fun onItemClickListener(position: Int) {

            }

        })

        binding.rvCats.adapter = categoriesAdapter
    }

    private fun setupUI() {
        parent.isPharmacy(true)
        parent.setTitle(getString(R.string.pharmacy))
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getCategoriesFlow.collectLatest {
                parent.hideLoading()
                when (it) {
                    is Resource.Error -> {
                        ToastUtils.showToast(requireContext(), it.message.toString())
                    }
                    is Resource.Idle -> {

                    }
                    is Resource.Loading -> parent.showLoading()
                    is Resource.Success -> {
                        categories.clear()
                        categories = it.data!!.cats
                        categoriesAdapter.categories = categories
                    }
                }
            }
        }
    }


}