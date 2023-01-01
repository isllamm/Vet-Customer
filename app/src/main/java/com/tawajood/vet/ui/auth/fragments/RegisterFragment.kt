package com.tawajood.vet.ui.auth.fragments

import PrefsHelper
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
import com.tawajood.vet.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
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


    private fun onClick() {

        binding.btnCheck.setOnClickListener {

            if (validate()) {
                registerBody = RegisterBody(
                    binding.clinicNameEt.text.toString(),
                    "+" + binding.ccp.selectedCountryCode.toString(),
                    binding.phoneEt.text.toString(),
                    binding.emailEt.text.toString(),
                    "\$@#%12345AaBb\$@#%"
                )
                viewModel.register(registerBody)
            }


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
                        val user = it.data!!.user

                        PrefsHelper.setToken(user.token)
                        PrefsHelper.setUserImage(user.image)
                        PrefsHelper.setUserId(user.id)
                        PrefsHelper.setUsername(user.name)
                        PrefsHelper.setPhone(user.phone)
                        PrefsHelper.setCountryCode(user.country_code)

                        PrefsHelper.setFirst(false)

                        parent.gotoMain()
                    }
                }
            }
        }
    }


}