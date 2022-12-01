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
import com.tawajood.vet.ui.auth.AuthActivity
import com.tawajood.vet.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)




        Glide.with(this)
            .load(R.drawable.splash)
            .into(binding.logo)

        Handler(Looper.myLooper()!!).postDelayed({

            if (PrefsHelper.isFirst()) {
                startActivity(Intent(this, AuthActivity::class.java))
            } else {
                if (PrefsHelper.getToken().isEmpty()) {
                    startActivity(Intent(this, AuthActivity::class.java))
                } else {
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }


            finish()
        }, 2000)
    }

}
