package com.tawajood.vet.ui.main.requests

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.tawajood.vet.R
import com.tawajood.vet.databinding.FragmentAddRequestBinding
import com.tawajood.vet.databinding.FragmentDoctorProfileBinding
import com.tawajood.vet.pojo.Clinic
import com.tawajood.vet.pojo.Recommendation
import com.tawajood.vet.pojo.RequestType
import com.tawajood.vet.ui.main.MainActivity
import com.tawajood.vet.ui.main.doctor_profile.DoctorProfileViewModel
import com.tawajood.vet.utils.Constants
import com.tawajood.vet.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_request.*
import kotlinx.coroutines.flow.collectLatest
import kotlin.properties.Delegates
import kotlin.time.Duration.Companion.days

@AndroidEntryPoint
class AddRequestFragment : Fragment(R.layout.fragment_add_request) {


    private lateinit var binding: FragmentAddRequestBinding
    private lateinit var parent: MainActivity
    private val viewModel: RequestsViewModel by viewModels()
    private var id by Delegates.notNull<String>()
    private var doctors = mutableListOf<Clinic>()
    private lateinit var doctorInfo: Clinic
    private var types = mutableListOf<RequestType>()
    private lateinit var typesAdapter: ArrayAdapter<String>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddRequestBinding.bind(requireView())
        parent = requireActivity() as MainActivity
        id = requireArguments().getString(Constants.CLINIC).toString()
        viewModel.getDoctorProfile(id)
        viewModel.getRequestTypes()

        setupUI()
        onClick()
        setupSpinners()
        observeData()
    }

    private fun setupSpinners() {
        typesAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item)
        binding.typeSpinner.adapter = typesAdapter
    }

    private fun onClick() {
        binding.calender.setOnDateChangeListener { calendarView, i, i2, i3 ->
            Log.d("islam", "onClick: ${calendarView.date}")
            Log.d("islam", "onClick: ${calendarView.weekDayTextAppearance.days}")


        }
    }

    private fun setupUI() {
        parent.showBottomNav(false)
        parent.setTitle(getString(R.string.consultation_request))
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

                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getRequestTypesFlow.collectLatest {
                parent.hideLoading()
                when (it) {
                    is Resource.Error -> {
                        ToastUtils.showToast(requireContext(), it.message.toString())
                    }
                    is Resource.Idle -> {

                    }
                    is Resource.Loading -> parent.showLoading()
                    is Resource.Success -> {
                        types = it.data!!.request_types
                        if (types.isNotEmpty()){
                            types.forEach { item ->
                                typesAdapter.add(item.name)
                            }
                        }

                    }
                }
            }
        }

    }


}