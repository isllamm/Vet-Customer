package com.tawajood.vet.ui.main.specialties

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tawajood.vet.R
import com.tawajood.vet.adapters.DoctorsAdapter
import com.tawajood.vet.databinding.FragmentHomeBinding
import com.tawajood.vet.databinding.FragmentSpecialtiesResultsBinding
import com.tawajood.vet.pojo.Clinic
import com.tawajood.vet.ui.main.MainActivity
import com.tawajood.vet.ui.main.home.HomeViewModel
import com.tawajood.vet.utils.Constants
import com.tawajood.vet.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SpecialtiesResultsFragment : Fragment(R.layout.fragment_specialties_results) {

    private lateinit var binding: FragmentSpecialtiesResultsBinding
    private lateinit var parent: MainActivity
    private val viewModel: SpecialtiesResultsViewModel by viewModels()
    private lateinit var doctorsAdapter: DoctorsAdapter
    private var title = ""
    private var specialtiesId = -1
    private var doctors = mutableListOf<Clinic>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSpecialtiesResultsBinding.bind(requireView())
        parent = requireActivity() as MainActivity
        title = requireArguments().getString(Constants.TITLE).toString()
        Log.d("islam", "onViewCreated: $title")
        specialtiesId = requireArguments().getInt(Constants.SPE_ID)

        setupUI()
        onClick()
        setupDoctors()
        observeData()
    }

    private fun setupDoctors() {
        doctorsAdapter = DoctorsAdapter(object : DoctorsAdapter.OnItemClick {
            override fun onItemClickListener(position: Int) {
                parent.navController.navigate(
                    R.id.doctorProfileFragment, bundleOf(
                        Constants.CLINIC to doctors[position].id
                    )
                )
            }

        })

        binding.rvDoctors.adapter = doctorsAdapter
    }

    private fun setupUI() {
        viewModel.getClinicsBySpecialization(specialtiesId.toString())
        parent.setTitle(title)
        parent.showBottomNav(false)
    }

    private fun onClick() {
        binding.ivSearch.setOnClickListener {
            parent.navController.navigate(
                R.id.searchFragment, bundleOf(
                    Constants.NAME_ID to binding.addressEt.text.toString()
                )
            )
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getSpecialtiesResultFlow.collectLatest {
                parent.hideLoading()
                when (it) {
                    is Resource.Error -> {
                        ToastUtils.showToast(requireContext(), it.message.toString())
                    }
                    is Resource.Idle -> {

                    }
                    is Resource.Loading -> parent.showLoading()
                    is Resource.Success -> {
                        doctors = it.data!!.clinics
                        doctorsAdapter.doctors = doctors

                    }
                }
            }
        }
    }


}