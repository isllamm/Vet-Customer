package com.tawajood.vet.ui.main.consultants

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
import com.tawajood.vet.adapters.BodyPartsAdapter
import com.tawajood.vet.adapters.CurrentConsultAdapter
import com.tawajood.vet.adapters.PendingConsultAdapter
import com.tawajood.vet.adapters.PreviousConsultAdapter
import com.tawajood.vet.databinding.FragmentMyConsultantsBinding
import com.tawajood.vet.databinding.FragmentMyConsultantsInfoBinding
import com.tawajood.vet.pojo.Consultant
import com.tawajood.vet.ui.main.MainActivity
import com.tawajood.vet.utils.Constants
import com.tawajood.vet.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlin.properties.Delegates

@AndroidEntryPoint
class MyConsultantsInfoFragment : Fragment(R.layout.fragment_my_consultants_info) {


    private lateinit var binding: FragmentMyConsultantsInfoBinding
    private lateinit var parent: MainActivity
    private val viewModel: MyConsultantsViewModel by viewModels()
    private lateinit var bodyPartsAdapter: BodyPartsAdapter
    private lateinit var consultant: Consultant


    private var status by Delegates.notNull<Int>()
    private var requestId by Delegates.notNull<String>()
    private var title by Delegates.notNull<String>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMyConsultantsInfoBinding.bind(requireView())
        parent = requireActivity() as MainActivity

        status = requireArguments().getInt(Constants.STATUS)
        requestId = requireArguments().getString(Constants.CONSULTANT_ID).toString()
        title = requireArguments().getString(Constants.TITLE).toString()

        setupUI()
        observeData()
        onClick()

        setupBodyParts()

    }

    private fun setupUI() {
        viewModel.getRequestById(requestId)
        parent.showBottomNav(false)
        parent.setTitle(title)

        when (status) {
            0 -> {
                isPending(true)
            }
            4 -> {
                isCurrent(true)
            }
            2 -> {
                isPrevious(true)
            }
        }
    }

    private fun onClick() {
        binding.btnPay.setOnClickListener {
            parent.navController.navigate(R.id.feePaymentFragment)
        }
    }

    private fun isPending(isTrue: Boolean) {
        binding.clReport.isVisible = !isTrue
        binding.btnChat.isVisible = !isTrue
        binding.btnCancel.isVisible = isTrue
        binding.btnPay.isVisible = isTrue
    }

    private fun isCurrent(isTrue: Boolean) {
        binding.clReport.isVisible = isTrue
        binding.btnChat.isVisible = isTrue
        binding.btnCancel.isVisible = !isTrue
        binding.btnPay.isVisible = !isTrue
    }

    private fun isPrevious(isTrue: Boolean) {
        binding.clReport.isVisible = isTrue
        binding.btnChat.isVisible = !isTrue
        binding.btnCancel.isVisible = !isTrue
        binding.btnPay.isVisible = !isTrue
    }

    private fun setupBodyParts() {
        bodyPartsAdapter = BodyPartsAdapter()
        binding.rvBodyParts.adapter = bodyPartsAdapter
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getRequestByIdFlow.collectLatest {
                parent.hideLoading()
                when (it) {
                    is Resource.Error -> {
                        ToastUtils.showToast(requireContext(), it.message.toString())
                    }
                    is Resource.Idle -> {

                    }
                    is Resource.Loading -> parent.showLoading()
                    is Resource.Success -> {

                        consultant = it.data!!.consultant

                        bodyPartsAdapter.bodyParts = consultant.parts_body!!

                        binding.tvNumber.text = "#" + consultant.id.toString()
                        binding.tvDate.text = consultant.created_at.substring(0, 10)
                        binding.tvTime.text = consultant.time
                        binding.tvType.text = consultant.requestType.name

                        Glide.with(requireContext()).load(consultant.clinic.image)
                            .into(binding.ivDoctor)
                        binding.tvDoctorName.text = consultant.clinic.name

                        binding.tvReport.text = consultant.clinic_report
                        binding.tvMedicine.text = consultant.clinic_report
                    }
                }
            }
        }
    }


}