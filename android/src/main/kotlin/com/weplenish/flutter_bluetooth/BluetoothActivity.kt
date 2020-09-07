package com.weplenish.flutter_bluetooth

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.companion.AssociationRequest
import android.companion.BluetoothDeviceFilter
import android.companion.CompanionDeviceManager
import android.content.Intent
import android.content.IntentSender
import android.os.*
import androidx.annotation.RequiresApi
import io.flutter.plugin.common.MethodChannel
import java.util.*
import java.util.regex.Pattern

class BluetoothActivity(private val channel: MethodChannel) : Activity() {
    companion object {
        @JvmStatic
        private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE){
            BluetoothAdapter.getDefaultAdapter()
        }

        @JvmStatic
        fun cancelDiscovery(){
            bluetoothAdapter?.cancelDiscovery()
        }

        const val ENABLE_BLUETOOTH_REQUEST_CODE = 142

        // channel response methods
        const val BT_ENABLED = "BT_ENABLED"
        const val BT_CANCELLED = "BT_CANCELLED"

        val isSupported = bluetoothAdapter != null
        val isEnabled = bluetoothAdapter?.isEnabled
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            ENABLE_BLUETOOTH_REQUEST_CODE -> when(resultCode) {
                RESULT_OK -> Handler(Looper.getMainLooper()).post {
                    channel.invokeMethod(BT_ENABLED, null)
                }
                RESULT_CANCELED -> Handler(Looper.getMainLooper()).post {
                    channel.invokeMethod(BT_CANCELLED, null)
                }
            }
        }
    }
}