import 'package:flutter_bluetooth/bluetooth_class.dart';

class PairedDevice {
  final String address;
  final String name;
  final List<String> uuids;
  final int bondState;
  final int type;
  final BluetoothClass bluetoothClass;

  PairedDevice(
      {this.address,
      this.name,
      this.uuids,
      this.bondState,
      this.type,
      this.bluetoothClass});

  factory PairedDevice.fromMap(Map<String, dynamic> map) {
    return PairedDevice(
        address: map['address'],
        name: map['name'],
        uuids: map['uuids'],
        bondState: map['bondState'],
        type: map['type'],
        bluetoothClass: BluetoothClass.fromMap(map['bluetoothClass']));
  }
}
