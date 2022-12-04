package com.tawajood.vet.ui.main.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.tawajood.vet.R
import com.tawajood.vet.databinding.FragmentProfileBinding
import com.tawajood.vet.databinding.FragmentProfileDetailsBinding
import com.tawajood.vet.ui.main.MainActivity
import com.tawajood.vet.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProfileDetailsFragment : Fragment(R.layout.fragment_profile_details) {


    private lateinit var binding: FragmentProfileDetailsBinding
    private lateinit var parent: MainActivity
    private val viewModel: ProfileViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileDetailsBinding.bind(requireView())
        parent = requireActivity() as MainActivity


        setupUI()
        onClick()
        observeData()
    }

    private fun setupUI() {
        parent.showBottomNav(false)
        parent.setTitle(getString(R.string.profile))

    }

    private fun onClick() {

    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getProfileFlow.collectLatest {
                parent.hideLoading()
                when (it) {
                    is Resource.Error -> {
                        ToastUtils.showToast(requireContext(), it.message.toString())
                    }
                    is Resource.Idle -> {

                    }
                    is Resource.Loading -> parent.showLoading()
                    is Resource.Success -> {
                        val user = it.data!!.profile
                        Glide.with(requireContext()).load(user.image).into(binding.imgUser)
                        binding.tvName.text = user.name
                        binding.tvNumber.text = user.country_code + user.phone
                        binding.tvEmail.text = user.email
                    }
                }
            }
        }
    }

}