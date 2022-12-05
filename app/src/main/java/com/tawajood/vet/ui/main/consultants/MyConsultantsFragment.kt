package com.tawajood.vet.ui.main.consultants

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tawajood.vet.R
import com.tawajood.vet.adapters.CurrentConsultAdapter
import com.tawajood.vet.adapters.PendingConsultAdapter
import com.tawajood.vet.adapters.PreviousConsultAdapter
import com.tawajood.vet.databinding.FragmentMyConsultantsBinding
import com.tawajood.vet.pojo.Consultant
import com.tawajood.vet.ui.main.MainActivity
import com.tawajood.vet.utils.Constants
import com.tawajood.vet.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MyConsultantsFragment : Fragment(R.layout.fragment_my_consultants) {


    private lateinit var binding: FragmentMyConsultantsBinding
    private lateinit var parent: MainActivity
    private val viewModel: MyConsultantsViewModel by viewModels()
    private lateinit var previousConsultAdapter: PreviousConsultAdapter
    private lateinit var currentConsultAdapter: CurrentConsultAdapter
    private lateinit var pendingConsultAdapter: PendingConsultAdapter
    var prevConsultants = mutableListOf<Consultant>()
    var pendingConsultants = mutableListOf<Consultant>()
    var currentConsultants = mutableListOf<Consultant>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMyConsultantsBinding.bind(requireView())
        parent = requireActivity() as MainActivity

        setupUI()
        observeData()
        onClick()

        setupPendingConsult()
        setupCurrentConsult()
        setupPreviousConsult()
    }

    private fun setupPendingConsult() {
        pendingConsultAdapter = PendingConsultAdapter(object : PendingConsultAdapter.OnItemClick {
            override fun onItemClickListener(position: Int) {
                parent.navController.navigate(
                    R.id.myConsultantsInfoFragment, bundleOf(
                        Constants.CONSULTANT_ID to pendingConsultants[position].id.toString(),
                        Constants.TITLE to getString(R.string.pending_consultant1),
                        Constants.STATUS to pendingConsultants[position].status
                    )
                )
            }
        })
        binding.rvPending.adapter = pendingConsultAdapter
    }

    private fun setupCurrentConsult() {
        currentConsultAdapter = CurrentConsultAdapter(object : CurrentConsultAdapter.OnItemClick {
            override fun onItemClickListener(position: Int) {
                parent.navController.navigate(
                    R.id.myConsultantsInfoFragment, bundleOf(
                        Constants.CONSULTANT_ID to currentConsultants[position].id.toString(),
                        Constants.TITLE to getString(R.string.current_consultant1),
                        Constants.STATUS to currentConsultants[position].status
                    )
                )
            }
        })
        binding.rvCurrent.adapter = currentConsultAdapter
    }

    private fun setupPreviousConsult() {
        previousConsultAdapter =
            PreviousConsultAdapter(object : PreviousConsultAdapter.OnItemClick {
                override fun onItemClickListener(position: Int) {
                    parent.navController.navigate(
                        R.id.myConsultantsInfoFragment, bundleOf(
                            Constants.CONSULTANT_ID to prevConsultants[position].id.toString(),
                            Constants.TITLE to getString(R.string.previous_consultant1),
                            Constants.STATUS to prevConsultants[position].status
                        )
                    )
                }

            })

        binding.rvPrevious.adapter = previousConsultAdapter
    }

    private fun onClick() {
        binding.currentFrame.setOnClickListener {
            binding.currentFrame.background =
                requireContext().getDrawable(R.drawable.full_border_radius_16_blue)
            binding.tvCurrent.setTextColor(Color.parseColor("#FFFFFFFF"))

            binding.pendingFrame.background =
                requireContext().getDrawable(R.drawable.full_border_radius_16)
            binding.tvPending.setTextColor(Color.parseColor("#2D4B50"))

            binding.previousFrame.background =
                requireContext().getDrawable(R.drawable.full_border_radius_16)
            binding.tvPrevious.setTextColor(Color.parseColor("#2D4B50"))

            binding.rvPrevious.isVisible = false
            binding.rvCurrent.isVisible = true
            binding.rvPending.isVisible = false

        }

        binding.pendingFrame.setOnClickListener {
            binding.pendingFrame.background =
                requireContext().getDrawable(R.drawable.full_border_radius_16_blue)
            binding.tvPending.setTextColor(Color.parseColor("#FFFFFFFF"))

            binding.currentFrame.background =
                requireContext().getDrawable(R.drawable.full_border_radius_16)
            binding.tvCurrent.setTextColor(Color.parseColor("#2D4B50"))

            binding.previousFrame.background =
                requireContext().getDrawable(R.drawable.full_border_radius_16)
            binding.tvPrevious.setTextColor(Color.parseColor("#2D4B50"))

            binding.rvPrevious.isVisible = false
            binding.rvCurrent.isVisible = false
            binding.rvPending.isVisible = true

        }

        binding.previousFrame.setOnClickListener {
            binding.previousFrame.background =
                requireContext().getDrawable(R.drawable.full_border_radius_16_blue)
            binding.tvPrevious.setTextColor(Color.parseColor("#FFFFFFFF"))

            binding.pendingFrame.background =
                requireContext().getDrawable(R.drawable.full_border_radius_16)
            binding.tvPending.setTextColor(Color.parseColor("#2D4B50"))

            binding.currentFrame.background =
                requireContext().getDrawable(R.drawable.full_border_radius_16)
            binding.tvCurrent.setTextColor(Color.parseColor("#2D4B50"))

            binding.rvPrevious.isVisible = true
            binding.rvCurrent.isVisible = false
            binding.rvPending.isVisible = false
        }
    }

    private fun setupUI() {
        parent.showBottomNav(true)
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getMyRequestsFlow.collectLatest {
                parent.hideLoading()
                when (it) {
                    is Resource.Error -> {
                        ToastUtils.showToast(requireContext(), it.message.toString())
                    }
                    is Resource.Idle -> {

                    }
                    is Resource.Loading -> parent.showLoading()
                    is Resource.Success -> {

                        pendingConsultants.clear()
                        prevConsultants.clear()
                        currentConsultants.clear()

                        val items = it.data!!.consultant
                        for (item in items) {
                            when (item.status) {
                                0 -> {
                                    pendingConsultants.add(item)
                                }
                                4 -> {
                                    currentConsultants.add(item)
                                }
                                2 -> {
                                    prevConsultants.add(item)
                                }
                            }
                        }

                        currentConsultAdapter.consultants = currentConsultants
                        pendingConsultAdapter.consultants = pendingConsultants
                        previousConsultAdapter.consultants = prevConsultants
                    }
                }
            }
        }
    }

}