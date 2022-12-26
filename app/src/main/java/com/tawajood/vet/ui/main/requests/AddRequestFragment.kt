package com.tawajood.vet.ui.main.requests

import ToastUtils
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CalendarView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.tawajood.vet.R
import com.tawajood.vet.adapters.MyPetsAdapter
import com.tawajood.vet.adapters.SelectPetAdapter
import com.tawajood.vet.databinding.FragmentAddRequestBinding
import com.tawajood.vet.databinding.FragmentDoctorProfileBinding
import com.tawajood.vet.pojo.*
import com.tawajood.vet.ui.main.MainActivity
import com.tawajood.vet.ui.main.doctor_profile.DoctorProfileViewModel
import com.tawajood.vet.utils.Constants
import com.tawajood.vet.utils.Resource
import com.tawajood.vet.utils.getAddressForTextView
import dagger.hilt.android.AndroidEntryPoint
import io.nlopez.smartlocation.SmartLocation
import kotlinx.android.synthetic.main.fragment_add_request.*
import kotlinx.coroutines.flow.collectLatest
import java.io.File
import java.time.DayOfWeek
import java.util.Calendar
import kotlin.properties.Delegates
import kotlin.time.Duration.Companion.days
import kotlin.time.days

@AndroidEntryPoint
class AddRequestFragment : Fragment(R.layout.fragment_add_request) {


    private lateinit var binding: FragmentAddRequestBinding
    private lateinit var parent: MainActivity
    private val viewModel: RequestsViewModel by viewModels()
    private var id by Delegates.notNull<String>()
    private var doctors = mutableListOf<Clinic>()
    private lateinit var doctorInfo: Clinic
    private var clinicDay = mutableListOf<ClinicDay>()
    private var types = mutableListOf<RequestType>()
    private var times = mutableListOf<Times>()

    private lateinit var typesAdapter: ArrayAdapter<String>
    private lateinit var timesAdapter: ArrayAdapter<String>

    private lateinit var date: String
    private var lat: Double? = null
    private var lng: Double? = null
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var currentLatLng: LatLng
    private var petPic: File? = null
    private lateinit var myPetsAdapter: SelectPetAdapter
    private var myPets = mutableListOf<Pet>()
    private var petId:String = "0"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddRequestBinding.bind(requireView())
        parent = requireActivity() as MainActivity
        id = requireArguments().getString(Constants.CLINIC).toString()
        viewModel.getDoctorProfile(id)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        setupUI()
        onClick()
        setupSpinners()
        observeData()
        setupPets()
    }

    private fun setupPets() {
        myPetsAdapter = SelectPetAdapter(object : SelectPetAdapter.OnItemClick {
            override fun onItemClickListener(position: Int) {
                petId = myPets[position].id.toString()
            }

        })

        binding.rvPets.adapter = myPetsAdapter
    }

    private fun setupSpinners() {
        typesAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item)
        binding.typeSpinner.adapter = typesAdapter

        timesAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item)
        binding.timesSpinner.adapter = timesAdapter
    }

    private fun onClick() {

        binding.cardAdd.setOnClickListener {
            parent.navController.navigate(R.id.addPetFragment)
        }
        binding.calender.setOnDateChangeListener { view, year, month, dayOfMonth ->
            date = "$dayOfMonth/${month + 1}/$year"
            //Log.d("islam", "date: $date")
            getDay(year, month, dayOfMonth)

        }

        binding.addImg.setOnClickListener {
            imagePermissionResult.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        binding.locate.setOnClickListener {
            if (!SmartLocation.with(requireContext()).location().state()
                    .locationServicesEnabled()
            ) {
                ToastUtils.showToast(
                    requireContext(),
                    getString(R.string.location),
                )
            } else {
                parent.showLoading()
                locationPermissionResult.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                Log.d("islam", "onClick: Hi")
            }
        }


    }

    private fun getDay(year: Int, month: Int, dayOfMonth: Int) {
        val c: Calendar = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, month)
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val days =
            listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        val dayIndex = c.get(Calendar.DAY_OF_WEEK)

        for (item in clinicDay) {
            if (item.day.d_number == dayIndex) {
                ToastUtils.showToast(requireContext(), "اليوم متاح")

                viewModel.getTimesUnreserved(id, item.id.toString(), date)
            }

        }


    }

    private fun setupUI() {
        parent.showBottomNav(false)
        parent.setTitle(getString(R.string.consultation_request))
        binding.calender.minDate = System.currentTimeMillis() - 1000

    }

    @SuppressLint("SetTextI18n")
    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getDoctorProfileFlow.collectLatest {
                parent.hideLoading()
                when (it) {
                    is Resource.Error -> {
                        ToastUtils.showToast(requireContext(), it.message.toString())
                    }
                    is Resource.Idle -> {

                    }
                    is Resource.Loading -> parent.showLoading()
                    is Resource.Success -> {
                        doctors = it.data!!.clinics
                        if (doctors.isNotEmpty()) {
                            doctorInfo = doctors[0]
                            Glide.with(requireContext()).load(doctorInfo.image)
                                .into(binding.imgUser)
                            binding.tvName.text = doctorInfo.name
                            binding.tvRate.text = doctorInfo.rate.toString()
                            binding.tvLicenceNumber.text = doctorInfo.registration_number
                            binding.tvAddress.text = doctorInfo.address
                            binding.fees.text =
                                doctorInfo.consultation_fees.toString() + getString(R.string.Rs)
                            binding.time.text = doctorInfo.consultation_duration + " دقيقة "
                            if (doctorInfo.clinic_days.isNotEmpty()) {
                                clinicDay = doctorInfo.clinic_days

                            }

                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getRequestTypesFlow.collectLatest {
                parent.hideLoading()
                when (it) {
                    is Resource.Error -> {
                        ToastUtils.showToast(requireContext(), it.message.toString())
                    }
                    is Resource.Idle -> {

                    }
                    is Resource.Loading -> parent.showLoading()
                    is Resource.Success -> {
                        types = it.data!!.request_types
                        if (types.isNotEmpty()) {
                            types.forEach { item ->
                                typesAdapter.add(item.name)
                            }
                        }

                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getTimesUnreservedFlow.collectLatest {
                parent.hideLoading()
                when (it) {
                    is Resource.Error -> {
                        ToastUtils.showToast(requireContext(), it.message.toString())
                    }
                    is Resource.Idle -> {

                    }
                    is Resource.Loading -> parent.showLoading()
                    is Resource.Success -> {
                        times = it.data!!.times
                        if (times.isNotEmpty()) {
                            times.forEach { item ->
                                timesAdapter.add(item.from + " to " + item.to)
                            }
                            binding.textView5.isVisible = true
                            binding.textView55.isVisible = false
                            binding.rlEtTimes.isVisible = true
                        } else {
                            binding.textView5.isVisible = false
                            binding.textView55.isVisible = true
                            binding.rlEtTimes.isVisible = false
                        }

                    }
                }
            }
        }

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
                        myPets.clear()
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

    private val locationPermissionResult = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { result ->
        Log.d("islam", "Request Per: Hi")
        if (result) {
            getCurrentLocation()
        } else {
            Log.e("islam", "onActivityResult: PERMISSION DENIED")
            Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCurrentLocation() {
        parent.showLoading()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mFusedLocationClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
            .addOnCompleteListener {
                parent.hideLoading()
                it.addOnSuccessListener { location ->
                    if (location != null) {
                        currentLatLng = LatLng(location.latitude, location.longitude)
                        lat = location.latitude
                        lng = location.longitude

                        getAddressForTextView(
                            requireContext(),
                            location.latitude,
                            location.longitude,
                            binding.address
                        )
                    } else {
                        getCurrentLocation()
                    }
                }
                it.addOnFailureListener { e ->
                    Log.d("islam", "getLastKnownLocation: ${e.message}")
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
                    binding.imgName.text = petPic!!.name

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