package com.tawajood.vetclinic.utils

import android.app.Dialog
import android.content.Context
import com.tawajood.vetclinic.R

class LoadingUtil(context: Context) : Dialog(context) {

    fun showLoading() {
        show()
    }

    fun hideLoading() {
        dismiss()
        cancel()
    }

    init {
        setContentView(R.layout.dailog_loading)
        setCancelable(false)
        window!!.setBackgroundDrawableResource(R.color.transparent)
    }
}