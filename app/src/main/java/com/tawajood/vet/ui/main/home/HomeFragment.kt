package com.tawajood.vet.ui.main.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tawajood.vet.R
import com.tawajood.vet.adapters.MostRatedDoctorsAdapter
import com.tawajood.vet.adapters.OnlineDoctorsAdapter
import com.tawajood.vet.adapters.SpecialtiesAdapter
import com.tawajood.vet.databinding.FragmentHomeBinding
import com.tawajood.vet.pojo.Clinic
import com.tawajood.vet.pojo.Specialties
import com.tawajood.vet.pojo.SpecialtiesName
import com.tawajood.vet.ui.main.MainActivity
import com.tawajood.vet.utils.Constants
import com.tawajood.vet.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {


    private lateinit var binding: FragmentHomeBinding
    private lateinit var parent: MainActivity
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var specialtiesAdapter: SpecialtiesAdapter
    private var specialties = mutableListOf<SpecialtiesName>()
    private var onlineDoctors = mutableListOf<Clinic>()
    private var mostDoctors = mutableListOf<Clinic>()
    private lateinit var onlineDoctorsAdapter: OnlineDoctorsAdapter
    private lateinit var mostRatedDoctorsAdapter: MostRatedDoctorsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(requireView())
        parent = requireActivity() as MainActivity

        setupSpecialties()
        setupOnlineDoctors()
        setupMostRatedDoctors()
        setupUI()
        onClick()
        observeData()
    }

    private fun setupMostRatedDoctors() {
        mostRatedDoctorsAdapter =
            MostRatedDoctorsAdapter(object : MostRatedDoctorsAdapter.OnItemClick {
                override fun onItemClickListener(position: Int) {
                    Log.d("islam", "onItemClickListener: ${mostDoctors[position].id}")
                    parent.navController.navigate(
                        R.id.doctorProfileFragment, bundleOf(
                            Constants.CLINIC to mostDoctors[position].id
                        )
                    )
                }

            })

        binding.rvMostRatedDoctors.adapter = mostRatedDoctorsAdapter
    }

    private fun setupOnlineDoctors() {
        onlineDoctorsAdapter = OnlineDoctorsAdapter(object : OnlineDoctorsAdapter.OnItemClick {
            override fun onItemClickListener(position: Int) {
                parent.navController.navigate(
                    R.id.doctorProfileFragment, bundleOf(
                        Constants.CLINIC to onlineDoctors[position].id
                    )
                )
            }

        })

        binding.rvOnlineDoctors.adapter = onlineDoctorsAdapter
    }

    private fun setupSpecialties() {
        specialtiesAdapter = SpecialtiesAdapter(object : SpecialtiesAdapter.OnItemClick {
            override fun onItemClickListener(position: Int) {
                parent.navController.navigate(
                    R.id.specialtiesResultsFragment, bundleOf(
                        Constants.TITLE to specialties[position].specialization.name,
                        Constants.SPE_ID to specialties[position].specialization.id,
                    )
                )
            }

        })

        binding.rvSpecialties.adapter = specialtiesAdapter
    }

    private fun setupUI() {
        parent.showBottomNav(true)
    }

    private fun onClick() {
        binding.mostFrame.setOnClickListener {
            binding.mostFrame.background =
                requireContext().getDrawable(R.drawable.full_border_radius_12_blue)
            binding.tvMost.setTextColor(Color.parseColor("#FFFFFFFF"))

            binding.avilFrame.background =
                requireContext().getDrawable(R.drawable.full_border_radius_12_grey)
            binding.tvAvil.setTextColor(Color.parseColor("#9BDAE3"))



            binding.rvMostRatedDoctors.isVisible = true
            binding.rvOnlineDoctors.isVisible = false

        }

        binding.avilFrame.setOnClickListener {
            binding.avilFrame.background =
                requireContext().getDrawable(R.drawable.full_border_radius_12_blue)
            binding.tvAvil.setTextColor(Color.parseColor("#FFFFFFFF"))

            binding.mostFrame.background =
                requireContext().getDrawable(R.drawable.full_border_radius_12_grey)
            binding.tvMost.setTextColor(Color.parseColor("#9BDAE3"))


            binding.rvMostRatedDoctors.isVisible = false
            binding.rvOnlineDoctors.isVisible = true

        }


        binding.ivLoc.setOnClickListener {
            parent.navController.navigate(
                R.id.searchFragment, bundleOf(
                    Constants.NAME_ID to binding.addressEt.text.toString()
                )
            )
        }

    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getSpecialtiesFlow.collectLatest {
                parent.hideLoading()
                when (it) {
                    is Resource.Error -> {
                        ToastUtils.showToast(requireContext(), it.message.toString())
                    }
                    is Resource.Idle -> {

                    }
                    is Resource.Loading -> parent.showLoading()
                    is Resource.Success -> {
                        specialtiesAdapter.specialties = it.data!!.specializations
                        specialties = it.data.specializations

                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getOnlineClinicsFlow.collectLatest {
                parent.hideLoading()
                when (it) {
                    is Resource.Error -> {
                        ToastUtils.showToast(requireContext(), it.message.toString())
                    }
                    is Resource.Idle -> {

                    }
                    is Resource.Loading -> parent.showLoading()
                    is Resource.Success -> {
                        onlineDoctorsAdapter.doctors = it.data!!.clinics
                        onlineDoctors = it.data.clinics
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getMostRatedClinicsFlow.collectLatest {
                parent.hideLoading()
                when (it) {
                    is Resource.Error -> {
                        ToastUtils.showToast(requireContext(), it.message.toString())
                    }
                    is Resource.Idle -> {

                    }
                    is Resource.Loading -> parent.showLoading()
                    is Resource.Success -> {
                        mostRatedDoctorsAdapter.doctors = it.data!!.clinics
                        mostDoctors = it.data.clinics
                    }
                }
            }
        }
    }


}