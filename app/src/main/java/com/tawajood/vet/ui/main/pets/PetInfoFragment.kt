package com.tawajood.vet.ui.main.pets

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.tawajood.vet.R
import com.tawajood.vet.adapters.VaccinationAdapter
import com.tawajood.vet.databinding.FragmentAddPetBinding
import com.tawajood.vet.databinding.FragmentPetInfoBinding
import com.tawajood.vet.pojo.Pet
import com.tawajood.vet.pojo.PetTypes
import com.tawajood.vet.pojo.Vaccinations
import com.tawajood.vet.ui.main.MainActivity
import com.tawajood.vet.utils.Constants
import com.tawajood.vet.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.io.File
import kotlin.properties.Delegates


@AndroidEntryPoint
class PetInfoFragment : Fragment(R.layout.fragment_pet_info) {


    private lateinit var binding: FragmentPetInfoBinding
    private lateinit var parent: MainActivity
    private val viewModel: MyPetsViewModel by viewModels()
    private var petId by Delegates.notNull<String>()
    private lateinit var myPet: Pet
    private var vaccinations = mutableListOf<Vaccinations>()
    private lateinit var vaccinationAdapter: VaccinationAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPetInfoBinding.bind(requireView())
        parent = requireActivity() as MainActivity
        petId = requireArguments().getString(Constants.PET_ID).toString()

        onClick()
        setupUI()
        observeData()
        setupVaccinations()

    }

    private fun setupVaccinations() {
        vaccinationAdapter = VaccinationAdapter(object : VaccinationAdapter.OnItemClick {
            override fun onItemClickListener(position: Int) {

            }

        })
        binding.rvVaccination.adapter = vaccinationAdapter
    }

    private fun onClick() {
        binding.vaccinationFrame.setOnClickListener {
            binding.vaccinationFrame.background =
                requireContext().getDrawable(R.drawable.full_border_radius_16_blue)
            binding.tvVaccination.setTextColor(Color.parseColor("#FFFFFFFF"))

            binding.previousConsultantFrame.background =
                requireContext().getDrawable(R.drawable.full_border_radius_16)
            binding.tvPreviousConsultant.setTextColor(Color.parseColor("#2D4B50"))

            binding.medicinesFrame.background =
                requireContext().getDrawable(R.drawable.full_border_radius_16)
            binding.tvMedicines.setTextColor(Color.parseColor("#2D4B50"))

            binding.rvVaccination.isVisible = true
            binding.rvPreviousConsultant.isVisible = false
            binding.rvMedicine.isVisible = false
            binding.btnAddVaccination.isVisible = true

        }

        binding.previousConsultantFrame.setOnClickListener {
            binding.previousConsultantFrame.background =
                requireContext().getDrawable(R.drawable.full_border_radius_16_blue)
            binding.tvPreviousConsultant.setTextColor(Color.parseColor("#FFFFFFFF"))

            binding.vaccinationFrame.background =
                requireContext().getDrawable(R.drawable.full_border_radius_16)
            binding.tvVaccination.setTextColor(Color.parseColor("#2D4B50"))

            binding.medicinesFrame.background =
                requireContext().getDrawable(R.drawable.full_border_radius_16)
            binding.tvMedicines.setTextColor(Color.parseColor("#2D4B50"))

            binding.rvVaccination.isVisible = false
            binding.rvPreviousConsultant.isVisible = true
            binding.rvMedicine.isVisible = false
            binding.btnAddVaccination.isVisible = false

        }

        binding.medicinesFrame.setOnClickListener {
            binding.medicinesFrame.background =
                requireContext().getDrawable(R.drawable.full_border_radius_16_blue)
            binding.tvMedicines.setTextColor(Color.parseColor("#FFFFFFFF"))

            binding.previousConsultantFrame.background =
                requireContext().getDrawable(R.drawable.full_border_radius_16)
            binding.tvPreviousConsultant.setTextColor(Color.parseColor("#2D4B50"))

            binding.vaccinationFrame.background =
                requireContext().getDrawable(R.drawable.full_border_radius_16)
            binding.tvVaccination.setTextColor(Color.parseColor("#2D4B50"))

            binding.rvVaccination.isVisible = false
            binding.rvPreviousConsultant.isVisible = false
            binding.rvMedicine.isVisible = true
            binding.btnAddVaccination.isVisible = false
        }

        binding.btnAddVaccination.setOnClickListener {
            parent.navController.navigate(
                R.id.addVaccinationDialogFragment, bundleOf(
                    Constants.PET_ID to petId
                )
            )
        }
    }

    private fun setupUI() {
        viewModel.getPetById(petId)
        parent.showBottomNav(false)
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.myPetInfo.collectLatest {
                parent.hideLoading()
                when (it) {
                    is Resource.Error -> {
                        ToastUtils.showToast(requireContext(), it.message.toString())
                    }
                    is Resource.Idle -> {

                    }
                    is Resource.Loading -> parent.showLoading()
                    is Resource.Success -> {
                        vaccinations.clear()
                        myPet = it.data!!.pet

                        Glide.with(requireContext()).load(myPet.image).into(binding.img)
                        binding.name.text = myPet.name
                        binding.details.text =
                            myPet.type + ", " + myPet.breed + ", " + "\n" + myPet.gender + ", " + myPet.weight + ", " + myPet.age

                        vaccinations = myPet.vaccinations!!
                        vaccinationAdapter.vaccinations = vaccinations
                    }
                }
            }
        }
    }


}