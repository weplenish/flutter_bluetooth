class DeviceAttributes {
  final bool singleDevice;
  final String nameRegex;
  final String serviceUUID;

  DeviceAttributes(
      {this.nameRegex, this.serviceUUID, this.singleDevice = true});

  Map<String, dynamic> toMap() {
    var result = <String, dynamic>{"singleDevice": singleDevice};
    if (nameRegex != null) {
      result.putIfAbsent("namePattern", () => nameRegex);
    }
    if (serviceUUID != null) {
      result.putIfAbsent("uuidString", () => serviceUUID);
    }
    return result;
  }
}
