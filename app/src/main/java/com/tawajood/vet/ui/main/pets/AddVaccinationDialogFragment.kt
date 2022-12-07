package com.tawajood.vet.ui.main.pets

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tawajood.vet.R
import com.tawajood.vet.databinding.FragmentAddVaccinationDialogBinding
import com.tawajood.vet.pojo.VaccinationType
import com.tawajood.vet.pojo.VaccinationTypes
import com.tawajood.vet.ui.main.MainActivity
import com.tawajood.vet.ui.main.dialogs.ResultDialogFragment
import com.tawajood.vet.utils.Constants
import com.tawajood.vet.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.util.*
import kotlin.properties.Delegates


@AndroidEntryPoint
class AddVaccinationDialogFragment : DialogFragment(R.layout.fragment_add_vaccination_dialog) {
    companion object {
        fun newInstance(): AddVaccinationDialogFragment {
            return AddVaccinationDialogFragment()
        }
    }


    private lateinit var binding: FragmentAddVaccinationDialogBinding
    private lateinit var parent: MainActivity
    private val viewModel: MyPetsViewModel by viewModels()
    private var petId by Delegates.notNull<String>()
    private var date: String = ""
    private var vaccinationTypes = mutableListOf<VaccinationType>()
    private lateinit var typeAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddVaccinationDialogBinding.inflate(inflater)
        parent = requireActivity() as MainActivity
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corners_dialog);
        petId = requireArguments().getString(Constants.PET_ID).toString()

        onClick()
        observeData()
        setupPetTypes()
        return binding.root
    }

    private fun setupPetTypes() {
        typeAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item)
        binding.vaccinationSpinner.adapter = typeAdapter
    }

    private fun setupUI() {
        viewModel.getVaccinationTypes()
        val width = (resources.displayMetrics.widthPixels * 0.88).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onStart() {
        super.onStart()
        setupUI()
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getVaccinationTypes.collectLatest {
                parent.hideLoading()
                when (it) {
                    is Resource.Error -> {
                        ToastUtils.showToast(requireContext(), it.message.toString())
                    }
                    is Resource.Idle -> {

                    }
                    is Resource.Loading -> parent.showLoading()
                    is Resource.Success -> {
                        vaccinationTypes.clear()
                        val items = it.data!!.vaccination_types
                        vaccinationTypes = items
                        items.forEach { item ->
                            typeAdapter.add(item.name)
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addVaccination.collectLatest {
                parent.hideLoading()
                when (it) {
                    is Resource.Error -> {
                        ToastUtils.showToast(requireContext(), it.message.toString())
                    }
                    is Resource.Idle -> {

                    }
                    is Resource.Loading -> parent.showLoading()
                    is Resource.Success -> {
                        ResultDialogFragment.newInstance(
                            getString(R.string.vaccination_added),
                            R.drawable.done
                        )
                            .show(
                                parentFragmentManager,
                                ResultDialogFragment::class.java.canonicalName
                            )
                    }
                }
            }
        }
    }

    private fun onClick() {
        binding.btnSave.setOnClickListener {
            viewModel.addVaccination(
                petId,
                date,
                vaccinationTypes[binding.vaccinationSpinner.selectedItemPosition].id.toString()
            )
        }
        binding.monthEt.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(
                requireContext(),
                R.style.DialogThemeCalender,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->

                    // Display Selected date
                    binding.yearEt.setText(year.toString())
                    binding.monthEt.setText(monthOfYear.toString())
                    binding.dayEt.setText(dayOfMonth.toString())

                    date = "$year-$monthOfYear-$dayOfMonth"
                },
                year,
                month,
                day
            )

            dpd.show()
        }
    }


}