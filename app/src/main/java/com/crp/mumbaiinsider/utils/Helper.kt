package com.crp.mumbaiinsider.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.provider.Settings
import android.view.Window
import android.widget.Button
import com.crp.mumbaiinsider.R

class Helper {
    companion object {
        fun isConnectingToInternet(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var activeNetwork: NetworkInfo? = null
            if (cm != null) {
                activeNetwork = cm.activeNetworkInfo
            }
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }
    }

}