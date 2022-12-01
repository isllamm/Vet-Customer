package com.tawajood.vet.ui.auth

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.tawajood.vet.R
import com.tawajood.vet.databinding.ActivityAuthBinding
import com.tawajood.vet.ui.main.MainActivity
import com.tawajood.vet.utils.LoadingUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    lateinit var navController: NavController
    private lateinit var loadingUtil: LoadingUtil


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingUtil = LoadingUtil(this)

        setupUI()
        setupNavController()
        onClick()
        setupNav()
    }

    private fun setupNav() {
        binding.tvLogin.setOnClickListener {
            binding.tvLogin.setBackgroundResource(R.drawable.login_shape_active)
            binding.tvLogin.setTextColor(Color.parseColor("#ffffff"))
            binding.tvRegister.setBackgroundResource(R.drawable.register_shape)

            navController.navigate(R.id.loginFragment)
        }

        binding.tvRegister.setOnClickListener {
            binding.tvLogin.setBackgroundResource(R.drawable.login_shape)
            binding.tvLogin.setTextColor(Color.parseColor("#464646"))
            binding.tvRegister.setBackgroundResource(R.drawable.register_shape_active)

            navController.navigate(R.id.registerFragment)
        }
    }

    private fun onClick() {
        binding.toolbar.ivBack.setOnClickListener {
            navController.popBackStack()
        }
    }

    private fun setupNavController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.auth_nav_host_fragment)
                as NavHostFragment
        navController = navHostFragment.navController
    }

    fun imInOTP(isVisible: Boolean) {
        binding.toolbar.logo.isVisible = !isVisible
        binding.toolbar.hello.isVisible = !isVisible
        binding.llAuth.isVisible = !isVisible
        binding.toolbar.tvEnterCode.isVisible = isVisible
        binding.toolbar.tvNumber.isVisible = isVisible
        binding.toolbar.tvMessageSent.isVisible = isVisible

    }


    private fun setupUI() {
        Glide.with(this)
            .load(R.drawable.logofinal)
            .into(binding.toolbar.logo)
    }

    fun showBackImage(isVisible: Boolean) {
        binding.toolbar.ivBack.isVisible = isVisible
    }

    fun gotoMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun showLoading() {
        loadingUtil.showLoading()
    }

    fun hideLoading() {
        loadingUtil.hideLoading()
    }

}