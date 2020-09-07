import 'dart:async';
import 'dart:typed_data';

import 'package:flutter/services.dart';
import 'package:flutter_bluetooth/device_attributes.dart';
import 'package:flutter_bluetooth/pair_device_response.dart';
import 'package:flutter_bluetooth/paired_attributes.dart';
import 'package:flutter_bluetooth/socket_data.dart';

import 'enable_bluetooth_response.dart';
import 'outgoing_method.dart';

class FlutterBluetooth {
  static const MethodChannel _channel =
      const MethodChannel('com.weplenish.flutter_bluetooth');
  static const EventChannel _eventChannel =
      const EventChannel("com.weplenish.flutter_bluetooth.event");

  static Future<bool> get isSupported =>
      _channel.invokeMethod(OutgoingMethod.isSupported);

  static Future<bool> get isEnabled =>
      _channel.invokeMethod(OutgoingMethod.isEnabled);

  static Stream<Uint8List> read(String socketUUID) =>
      _eventChannel.receiveBroadcastStream("$socketUUID|1");

  static Future<void> write(SocketData socketData) =>
      _channel.invokeMethod(OutgoingMethod.writeBt, socketData.toMap());

  static Future<void> closeSocket(SocketData socketData) =>
      _channel.invokeMethod(OutgoingMethod.closeSocket, socketData.toMap());

  static Future<void> disconnect() =>
      _channel.invokeMethod(OutgoingMethod.disconnect);

  static Future<PairedDeviceAttributes> connect(
      DeviceAttributes attributes) async {
    var completer = new Completer<PairedDeviceAttributes>();

    _channel.setMethodCallHandler((call) {
      switch (call.method) {
        case PairDeviceResponse.devicePaired:
          completer.complete(PairedDeviceAttributes.fromMap(call.arguments));
          break;
        case PairDeviceResponse.cannotFindDevice:
          completer.completeError("Cannot find device");
          break;
        case PairDeviceResponse.deviceSelectCancelled:
          completer.completeError("Device select cancelled by user");
          break;
        default:
          completer.completeError("Unknown");
      }
      return;
    });

    await _channel.invokeMethod(OutgoingMethod.connectBT, attributes.toMap());

    return completer.future;
  }

  static Future<void> enable(Function() successCallback) async {
    var completer = new Completer();

    _channel.setMethodCallHandler((call) {
      switch (call.method) {
        case EnableBluetoothResponse.enabled:
          completer.complete();
          break;
        case EnableBluetoothResponse.cancelled:
          completer.completeError("User cancelled");
          break;
        default:
          completer.completeError("Unknown");
      }
      return;
    });

    await _channel.invokeMethod(OutgoingMethod.enableBT);

    return completer.future;
  }
}
