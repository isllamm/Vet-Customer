package com.tawajood.vet.ui.main.consultants

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.myfatoorah.sdk.entity.executepayment.MFExecutePaymentRequest
import com.myfatoorah.sdk.entity.paymentstatus.MFGetPaymentStatusResponse
import com.myfatoorah.sdk.utils.MFAPILanguage
import com.myfatoorah.sdk.views.MFResult
import com.myfatoorah.sdk.views.MFSDK
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
import com.tawajood.vet.utils.WebViewLocaleHelper
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

    private var total = 0
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
            checkOut()
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
                        binding.tvTime.text = consultant.time.from + " to " + consultant.time.to
                        binding.tvType.text = consultant.requestType.name
                        total = consultant.consultation_fees!!
                        Glide.with(requireContext()).load(consultant.clinic.image_clinic)
                            .into(binding.ivDoctor)
                        binding.tvDoctorName.text = consultant.clinic.name

                        binding.tvReport.text = consultant.clinic_report
                        binding.tvMedicine.text = consultant.clinic_report
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.payFeesFlow.collectLatest {
                parent.hideLoading()
                when (it) {
                    is Resource.Error -> {
                        ToastUtils.showToast(requireContext(), it.message.toString())
                    }
                    is Resource.Idle -> {

                    }
                    is Resource.Loading -> parent.showLoading()
                    is Resource.Success -> {

                        parent.navController.navigate(
                            R.id.successfulOrderFragment,
                            bundleOf(Constants.TYPE to 2)
                        )
                    }
                }
            }
        }
    }

    private fun checkOut() {
        WebViewLocaleHelper(parent).implementWorkaround()
        parent.showLoading()
        val request = MFExecutePaymentRequest(2, total.toDouble())
        MFSDK.executePayment(
            parent,
            request,
            MFAPILanguage.EN,
            onInvoiceCreated = {
                Log.d("islam", "invoiceId: $it")
            }
        ) { _: String, result: MFResult<MFGetPaymentStatusResponse> ->
            parent.hideLoading()
            when (result) {
                is MFResult.Success -> {
                    Log.d("islam", "Response: " + Gson().toJson(result.response))
                    viewModel.payFees(consultant.id.toString(), consultant.clinic_id.toString())

                }
                is MFResult.Fail -> {
                    ToastUtils.showToast(
                        requireContext(),
                        result.error.message.toString()
                    )
                    Log.d("islam", "Fail: " + Gson().toJson(result.error))
                }
                MFResult.Loading -> parent.showLoading()
            }
        }
    }

}