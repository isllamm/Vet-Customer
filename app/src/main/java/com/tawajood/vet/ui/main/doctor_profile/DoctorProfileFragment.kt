package com.tawajood.vet.ui.main.doctor_profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.tawajood.vet.R
import com.tawajood.vet.databinding.FragmentDoctorProfileBinding
import com.tawajood.vet.databinding.FragmentProfileDetailsBinding
import com.tawajood.vet.ui.main.MainActivity
import com.tawajood.vet.ui.main.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DoctorProfileFragment : Fragment(R.layout.fragment_doctor_profile) {


    private lateinit var binding: FragmentDoctorProfileBinding
    private lateinit var parent: MainActivity
    private val viewModel: DoctorProfileViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDoctorProfileBinding.bind(requireView())
        parent = requireActivity() as MainActivity


        setupUI()
        onClick()
        observeData()
    }

    private fun setupUI() {
        parent.showBottomNav(false)
        parent.setTitle(getString(R.string.doctor_profile))
    }

    private fun onClick() {

    }

    private fun observeData() {

    }

}