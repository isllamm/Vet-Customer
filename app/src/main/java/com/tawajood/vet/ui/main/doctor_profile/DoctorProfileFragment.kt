package com.tawajood.vet.ui.main.doctor_profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.tawajood.vet.R
import com.tawajood.vet.databinding.FragmentDoctorProfileBinding
import com.tawajood.vet.databinding.FragmentProfileDetailsBinding
import com.tawajood.vet.pojo.Clinic
import com.tawajood.vet.pojo.Recommendation
import com.tawajood.vet.ui.main.MainActivity
import com.tawajood.vet.ui.main.profile.ProfileViewModel
import com.tawajood.vet.utils.Constants
import com.tawajood.vet.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlin.properties.Delegates

@AndroidEntryPoint
class DoctorProfileFragment : Fragment(R.layout.fragment_doctor_profile) {


    private lateinit var binding: FragmentDoctorProfileBinding
    private lateinit var parent: MainActivity
    private val viewModel: DoctorProfileViewModel by viewModels()
    private var id by Delegates.notNull<String>()
    private var doctors = mutableListOf<Clinic>()
    private var recommendations = mutableListOf<Recommendation>()
    private lateinit var doctorInfo: Clinic


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDoctorProfileBinding.bind(requireView())
        parent = requireActivity() as MainActivity
        id = requireArguments().getInt(Constants.CLINIC).toString()

        viewModel.getDoctorProfile(id)

        setupUI()
        onClick()
        observeData()
    }

    private fun setupUI() {
        parent.showBottomNav(false)
        parent.setTitle(getString(R.string.doctor_profile))
    }

    private fun onClick() {
        binding.btn.setOnClickListener {
            parent.navController.navigate(
                R.id.addRequestFragment, bundleOf(
                    Constants.CLINIC to id
                )
            )
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getDoctorProfileFlow.collectLatest {
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
                        if (doctors.isNotEmpty()) {
                            doctorInfo = doctors[0]
                            Glide.with(requireContext()).load(doctorInfo.image)
                                .into(binding.imgUser)
                            binding.tvName.text = doctorInfo.name
                            binding.tvRate.text = doctorInfo.rate.toString()
                            binding.tvLicenceNumber.text = doctorInfo.registration_number
                            binding.tvAddress.text = doctorInfo.address
                            binding.fees.text =
                                doctorInfo.consultation_fees.toString() + getString(R.string.Rs)
                            binding.time.text = doctorInfo.consultation_duration + " دقيقة "
                            binding.tvDetails.text = doctorInfo.details
                            if (doctorInfo.recommendations.isNotEmpty() && (doctorInfo.recommendations.size) >= 2) {
                                recommendations = doctorInfo.recommendations
                                binding.llComments.isVisible = true
                                binding.tv3.isVisible = true
                                binding.tv2.isVisible = true
                                ////1
                                binding.comment1.commentt.text = recommendations[0].comment
                                binding.comment1.tvRate.text = recommendations[0].rate.toString()
                                binding.comment1.name.text = recommendations[0].user.name
                                Glide.with(requireContext()).load(recommendations[0].user.image)
                                    .into(binding.comment1.img)
                                ///2
                                binding.comment2.commentt.text = recommendations[1].comment
                                binding.comment2.tvRate.text = recommendations[1].rate.toString()
                                binding.comment2.name.text = recommendations[1].user.name
                                Glide.with(requireContext()).load(recommendations[1].user.image)
                                    .into(binding.comment2.img)

                            } else {
                                binding.llComments.isVisible = false
                                binding.tv3.isVisible = false
                                binding.tv2.isVisible = false

                            }
                        }
                    }
                }
            }
        }
    }

}