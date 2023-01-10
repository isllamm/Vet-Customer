package com.tawajood.vet.utils

import android.content.Context
import android.webkit.WebView
import androidx.annotation.MainThread
import com.yariksoffice.lingver.Lingver

class WebViewLocaleHelper(private val context: Context) {

    private var requireWorkaround = true

    @MainThread
    fun implementWorkaround() {
        if (requireWorkaround) {
            requireWorkaround = false
            WebView(context).destroy()
            val lingver = Lingver.getInstance()
            lingver.setLocale(context, lingver.getLocale())
        }
    }
}
