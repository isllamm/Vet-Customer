package com.tawajood.vet.ui.main.review

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tawajood.vet.R
import com.tawajood.vet.adapters.DoctorsAdapter
import com.tawajood.vet.databinding.FragmentReviewDoctorBinding
import com.tawajood.vet.databinding.FragmentSearchBinding
import com.tawajood.vet.ui.main.MainActivity
import com.tawajood.vet.ui.main.dialogs.ResultDialogFragment
import com.tawajood.vet.ui.main.search.SearchViewModel
import com.tawajood.vet.utils.Constants
import com.tawajood.vet.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlin.properties.Delegates

@AndroidEntryPoint
class ReviewDoctorFragment : Fragment(R.layout.fragment_review_doctor) {

    private lateinit var binding: FragmentReviewDoctorBinding
    private lateinit var parent: MainActivity
    private val viewModel: ReviewDoctorViewModel by viewModels()
    private var doctorId by Delegates.notNull<String>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentReviewDoctorBinding.bind(requireView())
        parent = requireActivity() as MainActivity
        doctorId = requireArguments().getString(Constants.DOCTOR_ID).toString()

        setupUI()
        onClick()

        observeData()
    }

    private fun setupUI() {
        parent.showBottomNav(false)

    }

    private fun onClick() {
        binding.btnCheck.setOnClickListener {
            viewModel.reviewDoctor(
                doctorId,
                binding.ratingBar.rating.toString(),
                binding.comment.text.toString()
            )
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.reviewFlow.collectLatest {
                parent.hideLoading()
                when (it) {
                    is Resource.Error -> {
                        ToastUtils.showToast(requireContext(), it.message.toString())
                    }
                    is Resource.Idle -> {

                    }
                    is Resource.Loading -> parent.showLoading()
                    is Resource.Success -> {
                        ResultDialogFragment.newInstance(
                            "تم التقييم بنجاح",
                            R.drawable.done
                        )
                            .show(
                                parentFragmentManager,
                                ResultDialogFragment::class.java.canonicalName
                            )

                    }
                }
            }
        }
    }


}