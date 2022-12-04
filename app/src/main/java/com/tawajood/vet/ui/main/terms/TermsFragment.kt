package com.tawajood.vet.ui.main.terms

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tawajood.vet.R
import com.tawajood.vet.databinding.FragmentTermsBinding
import com.tawajood.vet.ui.main.MainActivity
import com.tawajood.vet.utils.Resource

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class TermsFragment : Fragment(R.layout.fragment_terms) {


    private lateinit var binding: FragmentTermsBinding
    private lateinit var parent: MainActivity
    private val viewModel: TermsViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTermsBinding.bind(requireView())
        parent = requireActivity() as MainActivity

        setupUI()
        observeData()
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.terms.collectLatest {
                parent.hideLoading()
                when (it) {
                    is Resource.Error -> {
                        ToastUtils.showToast(requireContext(), it.message.toString())
                    }
                    is Resource.Idle -> {

                    }
                    is Resource.Loading -> parent.showLoading()
                    is Resource.Success -> {
                        binding.tvTerms.text = it.data!!.terms
                    }
                }
            }
        }
    }

    private fun setupUI() {
        parent.setTitle(getString(R.string.terms_and_conditions))
        parent.showBottomNav(false)

    }


}