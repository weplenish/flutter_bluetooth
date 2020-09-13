# flutter_bluetooth

Low dependency modern bluetooth plugin written in swift and kotlin. The current implementation will only allow connecting to one bluetooth device at a time and only have one socket per uuid open.

## Getting Started

In Android O this plugin leverages the Companion Device Manager which allows easy bluetooth device connections by pattern matching against available bluetooth devices and/or matching the service uuids reported by bluetooth devices.

There are tests for if bluetooth is supported `await FlutterBluetooth.isSupported` and if it is enabled `await FlutterBluetooth.isEnabled`. On android it is possible to enable bluetooth programatically, to do so `await FlutterBluetooth.enable()`.

To connect to a device first call `await FlutterBluetooth.connect()` with the Device Attributes of the device you wish to connect to. Either the name pattern or a service uuid must be present.

Once the future returns a PairdDevice it's properties should be propagated with the device it connected with.

After connected to a device `FlutterBluetooth.write()` with socket data containing the uuid to write to and data to write will write to the provided uuid. `FlutterBluetooth.read()` with the socket uuid will provide a stream of byte arrays as they are returned by the device. If expecting a response from the device after a write, it is suggested the read stream be opened first.

Once you are done you can call `FlutterBluetooth.disconnect()` and all open sockets will be closed. Freeing flutter bluetooth resources.

## Current Support

- Android O+

## Future Support

- iOS
- Pre Android O