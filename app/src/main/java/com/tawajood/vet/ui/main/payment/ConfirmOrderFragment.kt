package com.tawajood.vet.ui.main.payment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.myfatoorah.sdk.entity.executepayment.MFExecutePaymentRequest
import com.myfatoorah.sdk.entity.paymentstatus.MFGetPaymentStatusResponse
import com.myfatoorah.sdk.utils.MFAPILanguage
import com.myfatoorah.sdk.views.MFResult
import com.myfatoorah.sdk.views.MFSDK
import com.tawajood.vet.R
import com.tawajood.vet.adapters.ConfirmOrderAdapter
import com.tawajood.vet.databinding.FragmentConfirmOrderBinding
import com.tawajood.vet.pojo.Cart
import com.tawajood.vet.ui.main.MainActivity
import com.tawajood.vet.ui.main.cart.CartViewModel
import com.tawajood.vet.utils.Constants
import com.tawajood.vet.utils.Resource
import com.tawajood.vet.utils.WebViewLocaleHelper
import com.tawajood.vet.utils.getAddressForTextView
import dagger.hilt.android.AndroidEntryPoint
import io.nlopez.smartlocation.SmartLocation
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ConfirmOrderFragment : Fragment(R.layout.fragment_confirm_order) {


    private lateinit var binding: FragmentConfirmOrderBinding
    private lateinit var parent: MainActivity
    private val viewModel: ConfirmOrderViewModel by viewModels()
    private var carts = mutableListOf<Cart>()

    private var lat: Double? = null
    private var lng: Double? = null
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var currentLatLng: LatLng

    private lateinit var confirmOrderAdapter: ConfirmOrderAdapter

    private var payment: String = "0"
    private var total = 0f
    private lateinit var methods: Array<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentConfirmOrderBinding.bind(requireView())
        parent = requireActivity() as MainActivity
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        onClick()
        setupUI()
        observeData()
        setupRec()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        methods  = resources.getStringArray(R.array.Methods)
    }
    private fun setupRec() {
        confirmOrderAdapter = ConfirmOrderAdapter()
        binding.rvCheckoutProducts.adapter = confirmOrderAdapter
    }

    private fun onClick() {

        binding.imgBtn.setOnClickListener {
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
        binding.btnCheckOut.setOnClickListener {
            if (validate()) {
                if (binding.methodSpinner.selectedItemPosition == 0) {
                    addOrder()
                } else if (binding.methodSpinner.selectedItemPosition == 1) {
                    checkOut()

                }

            }
        }
    }

    private fun addOrder() {

        viewModel.addOrder(
            binding.usernameEt.text.toString(),
            binding.phoneEt.text.toString(),
            binding.ccp.selectedCountryCode.toString(),
            binding.addressEt.text.toString(),
            payment,
            lat!!.toString(),
            lng!!.toString()

        )


    }

    private fun validate(): Boolean {
        if (TextUtils.isEmpty(binding.usernameEt.text)) {
            ToastUtils.showToast(requireContext(), "الاسم مطلوب")
            return false
        }
        if (TextUtils.isEmpty(binding.phoneEt.text)) {
            ToastUtils.showToast(requireContext(), "رقم الجوال مطلوب")

            return false
        }
        if (TextUtils.isEmpty(binding.addressEt.text)) {
            ToastUtils.showToast(requireContext(), "العنوان مطلوب")

            return false
        }

        if (lat == null || lng == null) {
            ToastUtils.showToast(requireContext(), "الرجاء تفعيل GPS للحصول على العنوان")
            return false
        }


        return true
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getCartFlow.collectLatest {
                parent.hideLoading()
                when (it) {
                    is Resource.Error -> {
                        ToastUtils.showToast(requireContext(), it.message.toString())
                    }
                    is Resource.Idle -> {

                    }
                    is Resource.Loading -> parent.showLoading()
                    is Resource.Success -> {
                        val prices = it.data
                        carts = it.data!!.carts
                        confirmOrderAdapter.carts = carts

                        binding.totalPrice.text = prices!!.total + getString(R.string.Rs)
                        binding.tax.text = prices.tax + getString(R.string.Rs)
                        binding.shipping.text = prices.delivery_cost + getString(R.string.Rs)
                        binding.total.text = prices.finalTotal + getString(R.string.Rs)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addOrderFlow.collectLatest {
                parent.hideLoading()
                when (it) {
                    is Resource.Error -> {
                        ToastUtils.showToast(requireContext(), it.message.toString())
                    }
                    is Resource.Idle -> {

                    }
                    is Resource.Loading -> parent.showLoading()
                    is Resource.Success -> {
                        parent.navController.navigate(
                            R.id.successfulOrderFragment,
                            bundleOf(Constants.TYPE to 1)
                        )
                    }
                }
            }
        }
    }

    private fun setupUI() {
        parent.setTitle(getString(R.string.confirem_order))
        parent.showBottomNav(false)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item, methods
        )
        binding.methodSpinner.adapter = adapter
    }

    private fun checkOut() {
        WebViewLocaleHelper(parent).implementWorkaround()
        parent.showLoading()
        val request = MFExecutePaymentRequest(2, total.toDouble())
        MFSDK.executePayment(
            parent,
            request,
            MFAPILanguage.EN,
            onInvoiceCreated = {
                Log.d("islam", "invoiceId: $it")
            }
        ) { _: String, result: MFResult<MFGetPaymentStatusResponse> ->
            parent.hideLoading()
            when (result) {
                is MFResult.Success -> {
                    Log.d("islam", "Response: " + Gson().toJson(result.response))
                    addOrder()
                    payment = "1"
                }
                is MFResult.Fail -> {
                    ToastUtils.showToast(
                        requireContext(),
                        result.error.message.toString()
                    )
                    Log.d("islam", "Fail: " + Gson().toJson(result.error))
                }
                MFResult.Loading -> parent.showLoading()
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
                            binding.addressEt
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
}