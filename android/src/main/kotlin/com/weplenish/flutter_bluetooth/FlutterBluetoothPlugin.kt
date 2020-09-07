package com.weplenish.flutter_bluetooth

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat.startActivityForResult
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import java.util.*

/** FlutterBluetoothPlugin */
class FlutterBluetoothPlugin : FlutterPlugin, MethodCallHandler, EventChannel.StreamHandler {
  private lateinit var methodChannel: MethodChannel
  private lateinit var eventChannel: EventChannel

  private val deviceActivity: DeviceActivity by lazy(LazyThreadSafetyMode.NONE) {
    when {
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O -> {
          DeviceActivity(methodChannel)
        }
        else -> {
          TODO("VERSION.SDK_INT < O")
        }
    }
  }

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    methodChannel = MethodChannel(flutterPluginBinding.flutterEngine.dartExecutor, METHOD_CHANNEL)
    methodChannel.setMethodCallHandler(this)

    eventChannel = EventChannel(flutterPluginBinding.flutterEngine.dartExecutor, EVENT_CHANNEL)
    eventChannel.setStreamHandler(this)
  }

  companion object {
    const val METHOD_CHANNEL = "com.weplenish.flutter_bluetooth"
    const val EVENT_CHANNEL = "com.weplenish.flutter_bluetooth.event"

    // channel methods
    const val IS_SUPPORTED = "isSupported"
    const val IS_ENABLED = "isEnabled"
    const val ENABLE_BT = "enableBT"
    const val CONNECT_BT = "connectBT"
    const val WRITE_BT = "writeBT"
    const val CLOSE_SOCKET = "closeSocket"
    const val DISCONNECT = "disconnect"

    // connect bt attributes
    const val SINGLE_DEVICE = "singleDevice"
    const val NAME_PATTERN = "namePattern"
    const val UUID_STRING = "uuidString"

    // socket attributes
    const val SOCKET_UUID = "socketUUID"
    const val WRITE_DATA = "writeData"

    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val btPlugin = FlutterBluetoothPlugin()
      val channel = MethodChannel(registrar.messenger(), METHOD_CHANNEL)
      channel.setMethodCallHandler(btPlugin)

      val eventChannel = EventChannel(registrar.messenger(), EVENT_CHANNEL)
      eventChannel.setStreamHandler(btPlugin)
    }
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    when (call.method) {
      IS_SUPPORTED -> result.success(BluetoothActivity.isSupported)
      IS_ENABLED -> result.success(BluetoothActivity.isEnabled)
      CLOSE_SOCKET -> when {
          android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O -> {
            deviceActivity.getSocket(UUID.fromString(call.argument(SOCKET_UUID))).close()
          }
        else -> TODO("ADD Android support pre O")
      }
      DISCONNECT -> when {
          android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O -> {
            deviceActivity.disconnect()
          }
        else -> TODO ("ADD Android support pre O")
      }
      ENABLE_BT -> startActivityForResult(BluetoothActivity(methodChannel),
        Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),
        BluetoothActivity.ENABLE_BLUETOOTH_REQUEST_CODE,
        null)
      WRITE_BT -> when {
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O -> {
          deviceActivity.getSocket(UUID.fromString(call.argument(SOCKET_UUID))).write(call.argument(WRITE_DATA)!!)
        }
        else -> {
          TODO("Add Android support pre O")
        }
      }
      CONNECT_BT -> when {
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O -> {
          deviceActivity.connectToDevice(
                  call.argument(SINGLE_DEVICE),
                  call.argument(NAME_PATTERN),
                  call.argument(UUID_STRING)
          )
        }
        else -> {
          result.notImplemented()
          TODO("Add Android support pre O")
        }
      }

      else -> result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    methodChannel.setMethodCallHandler(null)
    eventChannel.setStreamHandler(null)
  }

  override fun onListen(arguments: Any?, emitter: EventChannel.EventSink?) {
    if(arguments is String){
      val uuidInt = arguments.split('|')
      when {
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O -> {
          deviceActivity.getSocket(UUID.fromString(uuidInt.first())).addReadEmitter(uuidInt.last(), emitter!!)
        }
        else -> {
          TODO("Add Android support pre O")
        }
      }
    }
  }

  override fun onCancel(arguments: Any?) {
    if(arguments is String){
      val uuidInt = arguments.split('|')
      when {
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O -> {
          deviceActivity.getSocket(UUID.fromString(uuidInt.first())).removeReadEmitter(uuidInt.last())
        }
        else -> {
          TODO("Add Android support pre O")
        }
      }
    }
  }
}
