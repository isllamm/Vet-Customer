package com.tawajood.vet.ui.main.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.tawajood.vet.R
import com.tawajood.vet.databinding.FragmentFeePaymentBinding
import com.tawajood.vet.databinding.FragmentSupportBinding
import com.tawajood.vet.ui.main.MainActivity
import com.tawajood.vet.ui.main.support.SupportViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeePaymentFragment : Fragment(R.layout.fragment_fee_payment) {


    private lateinit var binding: FragmentFeePaymentBinding
    private lateinit var parent: MainActivity


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFeePaymentBinding.bind(requireView())
        parent = requireActivity() as MainActivity

        setupUI()
        observeData()
    }

    private fun observeData() {

    }

    private fun setupUI() {
        parent.setTitle(getString(R.string.technical_support))
        parent.showBottomNav(false)

    }


}