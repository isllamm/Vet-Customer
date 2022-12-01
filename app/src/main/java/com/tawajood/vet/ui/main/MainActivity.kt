package com.tawajood.vet.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.tawajood.vet.R
import com.tawajood.vet.databinding.ActivityMainBinding
import com.tawajood.vet.ui.auth.AuthActivity
import com.tawajood.vet.utils.LoadingUtil
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    private lateinit var loadingUtil: LoadingUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingUtil = LoadingUtil(this)

        onClick()
        setupNavController()
    }

    private fun setupNavController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavView.setupWithNavController(navController)
    }

    private fun onClick() {
        binding.toolbar.ivBack.setOnClickListener {
            navController.popBackStack()
        }
    }

    fun logout() {
        PrefsHelper.setToken("")
        PrefsHelper.setUserId(-1)
        PrefsHelper.setPhone("")
        PrefsHelper.setUsername("")
        PrefsHelper.setUserImage("")
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }

    fun showLoading() {
        loadingUtil.showLoading()
    }

    fun hideLoading() {
        loadingUtil.hideLoading()
    }

    fun setTitle(title: String) {
        binding.toolbar.title.text = title
    }


    fun showBottomNav(isVisible: Boolean) {
        binding.bottomNavView.isVisible = isVisible
        binding.toolbar.ivBack.isVisible = !isVisible
    }
}