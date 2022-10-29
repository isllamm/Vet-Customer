package com.tawajood.vet.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.tawajood.vet.R
import com.tawajood.vet.databinding.ActivitySplashBinding
import com.tawajood.vet.ui.MainActivity
import com.tawajood.vet.ui.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private var lat: Double? = null
    private var lng: Double? = null
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var currentLatLng: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)




        Glide.with(this)
            .load(R.drawable.logo)
            .into(binding.logo)

        Handler(Looper.myLooper()!!).postDelayed({

            startActivity(Intent(this, AuthActivity::class.java))


            finish()
        }, 2000)
    }


}
