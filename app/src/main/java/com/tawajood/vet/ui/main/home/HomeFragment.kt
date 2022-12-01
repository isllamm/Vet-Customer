package com.tawajood.vet.ui.main.home

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tawajood.vet.R
import com.tawajood.vet.adapters.SpecialtiesAdapter
import com.tawajood.vet.databinding.FragmentHomeBinding
import com.tawajood.vet.pojo.Specialties
import com.tawajood.vet.ui.main.MainActivity
import com.tawajood.vet.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {


    private lateinit var binding: FragmentHomeBinding
    private lateinit var parent: MainActivity
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var specialtiesAdapter: SpecialtiesAdapter
    private var specialties = mutableListOf<Specialties>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(requireView())
        parent = requireActivity() as MainActivity

        setupSpecialties()
        setupUI()
        onClick()
        observeData()
    }

    private fun setupSpecialties() {
        specialtiesAdapter = SpecialtiesAdapter(object : SpecialtiesAdapter.OnItemClick {
            override fun onItemClickListener(position: Int) {

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
                        specialtiesAdapter.specialties = it.data!!.specialties
                        specialties = it.data.specialties
                    }
                }
            }
        }
    }


}