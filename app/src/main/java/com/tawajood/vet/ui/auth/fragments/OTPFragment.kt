package com.tawajood.vet.ui.auth.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tawajood.vet.R
import com.tawajood.vet.databinding.FragmentOTPBinding
import com.tawajood.vet.ui.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OTPFragment : Fragment(R.layout.fragment_o_t_p) {


    private lateinit var binding: FragmentOTPBinding
    private lateinit var parent: AuthActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOTPBinding.bind(requireView())
        parent = requireActivity() as AuthActivity

        setupUI()
        onClick()
        observeData()
    }


    private fun onClick() {
        binding.activateBtn.setOnClickListener {
            parent.gotoMain()
        }
    }

    private fun setupUI() {
        parent.imInOTP(true)
    }

    private fun observeData() {

    }


}