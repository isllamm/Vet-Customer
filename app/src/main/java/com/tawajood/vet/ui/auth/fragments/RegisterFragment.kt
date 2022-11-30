package com.tawajood.vet.ui.auth.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.tawajood.vet.R
import com.tawajood.vet.databinding.FragmentRegisterBinding
import com.tawajood.vet.pojo.RegisterBody
import com.tawajood.vet.ui.auth.AuthActivity
import com.tawajood.vet.ui.auth.AuthViewModel

import com.tawajood.vetclinic.utils.Resource
import com.tawajood.vetclinic.utils.getAddressForTextView
import dagger.hilt.android.AndroidEntryPoint
import io.nlopez.smartlocation.SmartLocation
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {


    private lateinit var binding: FragmentRegisterBinding
    private lateinit var parent: AuthActivity
    private lateinit var registerBody: RegisterBody
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var currentLatLng: LatLng
    private var lat: Double? = null
    private var lng: Double? = null
    private val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(requireView())
        parent = requireActivity() as AuthActivity
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        setupUI()
        onClick()
        observeData()
    }

    private val locationPermissionResult = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { result ->
        Log.d("islam", "Request Per: Hi")
        if (result) {
            getCurrentLocation()
        } else {
            Log.e("islam", "onActivityResult: PERMISSION DENIED")
            Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onClick() {
        binding.ivLoc.setOnClickListener {
            if (!SmartLocation.with(requireContext()).location().state()
                    .locationServicesEnabled()
            ) {
                ToastUtils.showToast(
                    requireContext(),
                    getString(R.string.location),
                )
            } else {
                parent.showLoading()
                locationPermissionResult.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                Log.d("islam", "onClick: Hi")
            }
        }


        binding.btnCheck.setOnClickListener {

            if (validate()) {
                registerBody = RegisterBody(
                    binding.clinicNameEt.text.toString(),
                    "+" + binding.ccp.selectedCountryCode.toString(),
                    binding.phoneEt.text.toString(),
                    binding.emailEt.text.toString(),
                    binding.addressEt.text.toString(),
                    binding.licenseNumEt.text.toString(),
                    binding.passwordEt.text.toString()
                )
            }
            viewModel.register(registerBody)

            /*viewModel.checkPhone(
                "+" + binding.ccp.selectedCountryCode.toString(),
                binding.phoneEt.text.toString()
            )*/
        }
    }

    private fun setupUI() {
        parent.imInOTP(false)
    }

    private fun validate(): Boolean {
        if (TextUtils.isEmpty(binding.clinicNameEt.text)) {
            ToastUtils.showToast(requireContext(), getString(R.string.name_is_required))

            return false
        }

        if (TextUtils.isEmpty(binding.phoneEt.text)) {
            ToastUtils.showToast(requireContext(), getString(R.string.phone_is_required))

            return false
        }


        if (TextUtils.isEmpty(binding.passwordEt.text)) {
            ToastUtils.showToast(requireContext(), getString(R.string.password_is_required))

            return false
        }
        if (TextUtils.isEmpty(binding.emailEt.text)) {
            ToastUtils.showToast(requireContext(), getString(R.string.email_required))

            return false
        }
        if (TextUtils.isEmpty(binding.licenseNumEt.text)) {
            ToastUtils.showToast(requireContext(), getString(R.string.license_required))

            return false
        }
        if (TextUtils.isEmpty(binding.addressEt.text)) {
            ToastUtils.showToast(requireContext(), getString(R.string.address_required))

            return false
        }
        if (binding.passwordEt.text.toString() != binding.cPasswordEt.text.toString()) {
            ToastUtils.showToast(requireContext(), getString(R.string.password_not_match))

            return false
        }

        if (binding.passwordEt.text.toString().length < 6) {
            ToastUtils.showToast(
                requireContext(),
                getString(R.string.short_pass)
            )
            return false
        }

        if (!binding.switch1.isChecked) {
            return false
        }




        return true
    }

    private fun observeData() {
        lifecycleScope.launchWhenStarted {
            viewModel.checkPhone.collectLatest {
                parent.hideLoading()
                when (it) {
                    is Resource.Error -> ToastUtils.showToast(
                        requireContext(),
                        it.message.toString()
                    )
                    is Resource.Idle -> {
                    }
                    is Resource.Loading -> parent.showLoading()
                    is Resource.Success -> {
                        //Log.d("islam", "observeData: " + it.message)
                        if (it.data != null) {
                            ToastUtils.showToast(
                                requireContext(),
                                it.data.message
                            )
                        }
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.userRegisterFlow.collectLatest {
                parent.hideLoading()
                when (it) {
                    is Resource.Error -> ToastUtils.showToast(
                        requireContext(),
                        it.message.toString()
                    )
                    is Resource.Idle -> {}
                    is Resource.Loading -> parent.showLoading()
                    is Resource.Success -> {
                        PrefsHelper.setToken(it.data!!.token)

                        PrefsHelper.setFirst(false)

                        parent.gotoMain()
                    }
                }
            }
        }
    }

    private fun getCurrentLocation() {
        parent.showLoading()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mFusedLocationClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
            .addOnCompleteListener {
                parent.hideLoading()
                it.addOnSuccessListener { location ->
                    if (location != null) {
                        currentLatLng = LatLng(location.latitude, location.longitude)
                        lat = location.latitude
                        lng = location.longitude

                        getAddressForTextView(
                            requireContext(),
                            location.latitude,
                            location.longitude,
                            binding.addressEt
                        )
                    } else {
                        getCurrentLocation()
                    }
                }
                it.addOnFailureListener { e ->
                    Log.d("islam", "getLastKnownLocation: ${e.message}")
                }
            }
    }
}