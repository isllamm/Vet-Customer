package com.tawajood.vet.ui.main.pets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tawajood.vet.R
import com.tawajood.vet.adapters.MyPetsAdapter
import com.tawajood.vet.adapters.NotificationAdapter
import com.tawajood.vet.databinding.FragmentMyPetsBinding
import com.tawajood.vet.databinding.FragmentNotificationsBinding
import com.tawajood.vet.pojo.Pet
import com.tawajood.vet.ui.main.MainActivity
import com.tawajood.vet.ui.main.notifications.NotificationsViewModel
import com.tawajood.vet.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MyPetsFragment : Fragment(R.layout.fragment_my_pets) {


    private lateinit var binding: FragmentMyPetsBinding
    private lateinit var parent: MainActivity
    private val viewModel: MyPetsViewModel by viewModels()
    private lateinit var myPetsAdapter: MyPetsAdapter
    private var myPets = mutableListOf<Pet>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMyPetsBinding.bind(requireView())
        parent = requireActivity() as MainActivity

        onClick()
        setupUI()
        observeData()
        setupPets()
    }

    private fun onClick() {
        binding.cardAdd.setOnClickListener {
            parent.navController.navigate(R.id.addPetFragment)
        }
    }

    private fun setupPets() {
        myPetsAdapter = MyPetsAdapter(object : MyPetsAdapter.OnItemClick {
            override fun onItemClickListener(position: Int) {

            }

        })

        binding.rvPets.adapter = myPetsAdapter
    }

    private fun setupUI() {
        parent.showBottomNav(true)
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.myPets.collectLatest {
                parent.hideLoading()
                when (it) {
                    is Resource.Error -> {
                        ToastUtils.showToast(requireContext(), it.message.toString())
                    }
                    is Resource.Idle -> {

                    }
                    is Resource.Loading -> parent.showLoading()
                    is Resource.Success -> {
                        myPets.add(
                            Pet(
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null
                            )
                        )
                        it.data!!.pets.forEach { item ->
                            myPets.add(item)
                        }
                        myPetsAdapter.myPets = myPets
                    }
                }
            }
        }
    }

}