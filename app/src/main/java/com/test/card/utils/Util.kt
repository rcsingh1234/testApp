package com.test.card.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AlertDialog
import com.test.card.R
import com.test.card.model.Result

object Util {

    fun getName(result: Result): String {
        return result.name.title + " " + result.name.first + " " + result.name.last
    }

    fun getDescription(result: Result): String {
        return result.location.street.number.toString() + "," + result.location.street.name + ",\n" + result.location.city + "," + result.location.state + "," + result.location.country + "," + result.location.state + "," + result.location.postcode
    }

    fun isInternetConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        return isConnected
    }

    fun showNoNetworkDialog(context: Activity) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.app_name)
        builder.setMessage(R.string.no_internet_connected)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Close") { dialogInterface, which ->
           context.finish()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}