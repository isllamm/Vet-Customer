package com.tawajood.vet.ui.main.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tawajood.vet.R
import com.tawajood.vet.adapters.DoctorsAdapter
import com.tawajood.vet.databinding.FragmentSearchBinding
import com.tawajood.vet.databinding.FragmentSpecialtiesResultsBinding
import com.tawajood.vet.ui.main.MainActivity
import com.tawajood.vet.ui.main.specialties.SpecialtiesResultsViewModel
import com.tawajood.vet.utils.Constants
import com.tawajood.vet.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {


    private lateinit var binding: FragmentSearchBinding
    private lateinit var parent: MainActivity
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var doctorsAdapter: DoctorsAdapter
    private var title = "نتائج البحث"
    private var name = "-1"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(requireView())
        parent = requireActivity() as MainActivity

        name = requireArguments().getString(Constants.NAME_ID).toString()

        setupUI()
        onClick()
        setupDoctors()
        observeData()
    }

    private fun setupUI() {
        viewModel.searchClinics(name)
        parent.setTitle(title)
        parent.showBottomNav(false)
    }

    private fun onClick() {

    }

    private fun setupDoctors() {
        doctorsAdapter = DoctorsAdapter(object : DoctorsAdapter.OnItemClick {
            override fun onItemClickListener(position: Int) {

            }

        })

        binding.rvDoctors.adapter = doctorsAdapter
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getSearchResultFlow.collectLatest {
                parent.hideLoading()
                when (it) {
                    is Resource.Error -> {
                        ToastUtils.showToast(requireContext(), it.message.toString())
                    }
                    is Resource.Idle -> {

                    }
                    is Resource.Loading -> parent.showLoading()
                    is Resource.Success -> {
                        if (it.data!!.clinics.isNotEmpty()) {
                            doctorsAdapter.doctors = it.data.clinics
                        }

                    }
                }
            }
        }
    }


}