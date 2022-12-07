package com.tawajood.vet.ui.main.pets

import PrefsHelper
import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.tawajood.vet.R
import com.tawajood.vet.databinding.FragmentAddPetBinding
import com.tawajood.vet.pojo.PetBody
import com.tawajood.vet.pojo.PetTypes
import com.tawajood.vet.ui.main.MainActivity
import com.tawajood.vet.ui.main.dialogs.ResultDialogFragment
import com.tawajood.vet.utils.Constants
import com.tawajood.vet.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.io.File
import java.util.*

@AndroidEntryPoint
class AddPetFragment : Fragment(R.layout.fragment_add_pet) {


    private lateinit var binding: FragmentAddPetBinding
    private lateinit var parent: MainActivity
    private val viewModel: MyPetsViewModel by viewModels()

    private lateinit var typeAdapter: ArrayAdapter<String>

    private var petPic: File? = null
    private var date: String = ""

    private var types = mutableListOf<PetTypes>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddPetBinding.bind(requireView())
        parent = requireActivity() as MainActivity

        onClick()
        setupUI()
        observeData()
        setupPetTypes()


    }

    private fun setupUI() {
        parent.showBottomNav(false)
        parent.setTitle(getString(R.string.add_new_pet))
        viewModel.getPetTypes()
    }

    private fun onClick() {
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
        binding.btnSave.setOnClickListener {
            viewModel.addPet(
                PetBody(
                    PrefsHelper.getUserId(),
                    binding.petNameEt.text.toString(),
                    binding.petAgeEt.text.toString(),
                    binding.petWeightEt.text.toString(),
                    binding.genderSpinner.selectedItemPosition,
                    petPic,
                    types[binding.typeSpinner.selectedItemPosition].id,
                    date,
                    binding.sterileSpinner.selectedItemPosition,
                    binding.petBreedEt.text.toString()
                )
            )
        }

        binding.btnCancel.setOnClickListener {
            parent.navController.popBackStack()
        }

        binding.petImg.setOnClickListener {
            imagePermissionResult.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun setupPetTypes() {
        typeAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item)
        binding.typeSpinner.adapter = typeAdapter
    }


    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getPetTypes.collectLatest {
                parent.hideLoading()
                when (it) {
                    is Resource.Error -> {
                        ToastUtils.showToast(requireContext(), it.message.toString())
                    }
                    is Resource.Idle -> {

                    }
                    is Resource.Loading -> parent.showLoading()
                    is Resource.Success -> {
                        types = it.data!!.pet_types
                        types.forEach { item ->
                            typeAdapter.add(item.name)
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addPet.collectLatest {
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
                            getString(R.string.added_pet),
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

    private val imagePermissionResult = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { result ->
        if (result) {
            ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    pickImageResultLauncher.launch(intent)
                }
        } else {
            Log.e("isllam", "onActivityResult: PERMISSION DENIED")
            ToastUtils.showToast(requireContext(), "Permission Denied")
        }
    }

    private var pickImageResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            val resultCode = result.resultCode
            val data = result.data!!

            when (resultCode) {
                Activity.RESULT_OK -> {
                    val file = File(getRealPathFromURI(parent, data.data!!))
                    petPic = file
                    Glide.with(requireContext())
                        .load(file)
                        .into(binding.img)

                }
                ImagePicker.RESULT_ERROR -> {
                    ToastUtils.showToast(requireContext(), ImagePicker.getError(data))
                }
                else -> {
                    ToastUtils.showToast(requireContext(), "Task Cancelled")
                }
            }
        }

    private fun getRealPathFromURI(activity: Activity, contentURI: Uri): String {
        val result: String
        val cursor: Cursor? = activity.contentResolver
            .query(contentURI, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path.toString()
        } else {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(Constants._DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }
}