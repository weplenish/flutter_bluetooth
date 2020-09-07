import 'dart:typed_data';

class SocketData {
  final String uuid;
  final Uint8List data;

  SocketData({this.uuid, this.data});

  factory SocketData.fromMap(Map<String, dynamic> map) {
    return SocketData(data: map['data'], uuid: map['socketUUID']);
  }

  Map<String, dynamic> toMap() {
    return <String, dynamic>{"writeData": data, "socketUUID": uuid};
  }
}
