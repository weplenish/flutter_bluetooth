class BluetoothClass {
  final int deviceClass;
  final int majorDeviceClass;

  BluetoothClass({this.deviceClass, this.majorDeviceClass});

  factory BluetoothClass.fromMap(Map<String, int> map) {
    return BluetoothClass(
        deviceClass: map['deviceClass'],
        majorDeviceClass: map['majorDeviceClass']);
  }
}
