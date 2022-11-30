package com.tawajood.vet.ui.auth.fragments

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tawajood.vet.R
import com.tawajood.vet.databinding.FragmentLoginBinding
import com.tawajood.vet.ui.auth.AuthActivity
import com.tawajood.vet.ui.auth.AuthViewModel
import com.tawajood.vetclinic.utils.Resource

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var parent: AuthActivity
    private val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(requireView())
        parent = requireActivity() as AuthActivity

        setupUI()
        onClick()
        observeData()
    }


    private fun onClick() {
        binding.tvForget.setOnClickListener {

        }
        binding.btnCheck.setOnClickListener {
            if (validate()) {

                viewModel.login(
                    "+" + binding.ccp.selectedCountryCode.toString(),
                    binding.phoneEt.text.toString(),
                    binding.passwordEt.text.toString()
                )
            }

        }
    }

    private fun setupUI() {
        parent.imInOTP(false)
    }

    private fun validate(): Boolean {
        if (TextUtils.isEmpty(binding.phoneEt.text)) {
            ToastUtils.showToast(requireContext(), getString(R.string.phone_is_required))

            return false
        }

        if (TextUtils.isEmpty(binding.passwordEt.text)) {

            ToastUtils.showToast(requireContext(), getString(R.string.password_is_required))

            return false
        }

        return true
    }

    private fun observeData() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.userLoginFlow.collectLatest {
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

                        PrefsHelper.setToken(it.data!!.token)

                        PrefsHelper.setFirst(false)

                        parent.gotoMain()
                    }
                }
            }
        }
    }

}