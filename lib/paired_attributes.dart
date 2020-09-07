import 'package:flutter_bluetooth/bluetooth_class.dart';

class PairedDeviceAttributes {
  final String address;
  final String name;
  final List<String> uuids;
  final int bondState;
  final int type;
  final BluetoothClass bluetoothClass;

  PairedDeviceAttributes(
      {this.address,
      this.name,
      this.uuids,
      this.bondState,
      this.type,
      this.bluetoothClass});

  factory PairedDeviceAttributes.fromMap(Map<String, dynamic> map) {
    return PairedDeviceAttributes(
        address: map['address'],
        name: map['name'],
        uuids: map['uuids'],
        bondState: map['bondState'],
        type: map['type'],
        bluetoothClass: BluetoothClass.fromMap(map['bluetoothClass']));
  }
}
