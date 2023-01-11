package com.tawajood.vet.ui.main.doctor_profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.tawajood.vet.R
import com.tawajood.vet.adapters.ReviewAdapter
import com.tawajood.vet.databinding.FragmentDoctorProfileBinding
import com.tawajood.vet.databinding.FragmentDoctorReviewsBinding
import com.tawajood.vet.databinding.FragmentReviewDoctorBinding
import com.tawajood.vet.pojo.Clinic
import com.tawajood.vet.pojo.Recommendation
import com.tawajood.vet.ui.main.MainActivity
import com.tawajood.vet.utils.Constants
import com.tawajood.vet.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlin.properties.Delegates

@AndroidEntryPoint
class DoctorReviewsFragment : Fragment(R.layout.fragment_doctor_reviews) {


    private lateinit var binding: FragmentDoctorReviewsBinding
    private lateinit var parent: MainActivity
    private val viewModel: DoctorProfileViewModel by viewModels()
    private var id by Delegates.notNull<String>()
    private var doctors = mutableListOf<Clinic>()
    private var recommendations = mutableListOf<Recommendation>()
    private lateinit var doctorInfo: Clinic
    private lateinit var reviewAdapter: ReviewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDoctorReviewsBinding.bind(requireView())
        parent = requireActivity() as MainActivity
        id = requireArguments().getString(Constants.CLINIC).toString()

        viewModel.getDoctorProfile(id)

        setupUI()
        setupReviews()
        observeData()
    }

    private fun setupReviews() {
        reviewAdapter = ReviewAdapter()
        binding.rvReviews.adapter = reviewAdapter
    }

    private fun setupUI() {
        parent.showBottomNav(false)
        parent.setTitle(getString(R.string.users_reviews))
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

                            if (doctorInfo.recommendations.isNotEmpty()) {
                                recommendations = doctorInfo.recommendations
                                reviewAdapter.recommendations = recommendations

                            }
                        }
                    }
                }
            }
        }
    }
}