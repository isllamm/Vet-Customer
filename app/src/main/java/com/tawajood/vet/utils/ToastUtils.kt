package com.tawajood.snail.utils

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast

class ToastUtils{


    companion object{
        private var toastMessage: Toast? = null
        @SuppressLint("ShowToast")
        fun showToast(context: Context, msg: String){
            if (toastMessage != null) {
                toastMessage!!.cancel()
            }
            toastMessage = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
            toastMessage!!.show()
        }
    }
}