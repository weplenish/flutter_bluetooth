import 'package:flutter/services.dart';
import 'package:flutter_bluetooth/enable_bluetooth_response.dart';
import 'package:flutter_bluetooth/outgoing_method.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_bluetooth/flutter_bluetooth.dart';

void main() {
  const MethodChannel channel =
      MethodChannel('com.weplenish.flutter_bluetooth');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      switch (methodCall.method) {
        case OutgoingMethod.enableBT:
          channel.invokeMethod(EnableBluetoothResponse.enabled);
          break;
        default:
          return true;
      }
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('isEnabled', () async {
    expect(await FlutterBluetooth.isEnabled, true);
  });

  test('isSupported', () async {
    expect(await FlutterBluetooth.isSupported, true);
  });

  // test('enableBT', () async {
  //   await FlutterBluetooth.enableBT(expectAsync1((success) {
  //     expect(success, true);
  //   }));
  // });
}
