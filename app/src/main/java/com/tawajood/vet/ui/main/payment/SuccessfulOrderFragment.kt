package com.tawajood.vet.ui.main.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.tawajood.vet.R
import com.tawajood.vet.adapters.DoctorsAdapter
import com.tawajood.vet.databinding.FragmentSearchBinding
import com.tawajood.vet.databinding.FragmentSuccessfulOrderBinding
import com.tawajood.vet.ui.main.MainActivity
import com.tawajood.vet.ui.main.search.SearchViewModel
import com.tawajood.vet.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class SuccessfulOrderFragment : Fragment(R.layout.fragment_successful_order) {


    private lateinit var binding: FragmentSuccessfulOrderBinding
    private lateinit var parent: MainActivity

    private var type by Delegates.notNull<Int>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSuccessfulOrderBinding.bind(requireView())
        parent = requireActivity() as MainActivity

        type = requireArguments().getInt(Constants.TYPE)

        setupUI()
        onClick()
    }

    private fun onClick() {
        parent.onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parent.navController.popBackStack(R.id.pharmacyFragment, false)
            }

        })
    }

    private fun setupUI() {
        if (type == 1) {
            binding.text.text = getString(R.string.your_order_has_been_confirmed_successfully)
        } else if (type == 2) {
            binding.text.text = getString(R.string.fees_have_been_paid_successfully)

        }

        parent.setTitle(getString(R.string.Fee_Payment))
        parent.showBottomNav(false)

    }


}