import 'dart:async';

import 'package:flutter/services.dart';

enum SettingType {
  SECURE,
  SYSTEM,
  GLOBAL,
}

class AndroidFlutterSettings {
  static const MethodChannel _channel =
      const MethodChannel('android_flutter_settings/methods');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  /// Get methods
  static Future<String> getString(String setting, SettingType type) async =>
      await _channel.invokeMethod('getString', {
        'type': resolveEnum(type),
        'setting': setting,
      });

  static Future<int> getInt(String setting, SettingType type) async =>
      await _channel.invokeMethod('getInt', {
        'type': resolveEnum(type),
        'setting': setting,
      });

  static Future<bool> getBool(String setting, SettingType type) async =>
      await _channel.invokeMethod('getBoolean', {
        'type': resolveEnum(type),
        'setting': setting,
      });

  static Future<double> getDouble(String setting, SettingType type) async =>
      await _channel.invokeMethod('getFloat', {
        'type': resolveEnum(type),
        'setting': setting,
      });

  /// Put methods
  static Future<bool> putString(String setting, String value, SettingType type) async =>
      await _channel.invokeMethod('putString', {
        'type': resolveEnum(type),
        'value': value,
        'setting': setting,
      });

  static Future<bool> putInt(String setting, int value, SettingType type) async =>
      await _channel.invokeMethod('putInt', {
        'type': resolveEnum(type),
        'value': value,
        'setting': setting,
      });

  static Future<bool> putBool(String setting, bool value, SettingType type) async =>
      await _channel.invokeMethod('putBool', {
        'type': resolveEnum(type),
        'value': value,
        'setting': setting,
      });

  static Future<bool> putDouble(String setting, double value, SettingType type) async =>
      await _channel.invokeMethod('putFloat', {
        'type': resolveEnum(type),
        'value': value,
        'setting': setting,
      });

  static String resolveEnum(SettingType type) => type.toString().split('.')[1];
}
