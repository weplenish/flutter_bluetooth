class DeviceAttributes {
  final bool singleDevice;
  final String nameRegex;
  final String deviceUUID;

  DeviceAttributes({this.nameRegex, this.deviceUUID, this.singleDevice = true});

  Map<String, dynamic> toMap() {
    var result = <String, dynamic>{"singleDevice": singleDevice};
    if (nameRegex != null) {
      result.putIfAbsent("namePattern", () => nameRegex);
    }
    if (deviceUUID != null) {
      result.putIfAbsent("uuidString", () => deviceUUID);
    }
    return result;
  }
}
