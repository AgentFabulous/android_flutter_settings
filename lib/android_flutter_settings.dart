import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

class AndroidFlutterSettings {
  static const MethodChannel _channel =
      const MethodChannel('android_flutter_settings/methods');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  /// Get methods
  static Future<String> getString(SettingKey<String> setting) => _get(setting);

  static Future<int> getInt(SettingKey<int> setting) => _get(setting);

  static Future<bool> getBool(SettingKey<bool> setting) => _get(setting);

  static Future<dynamic> _get(SettingKey setting) async {
    String method;

    switch (setting.valueType) {
      case SettingValueType.BOOLEAN:
        method = 'getBoolean';
        break;
      case SettingValueType.INT:
        method = 'getInt';
        break;
      case SettingValueType.STRING:
        method = 'getString';
        break;
    }

    return await _channel.invokeMethod(method, {
      'type': resolveEnum(setting.type),
      'setting': setting.name,
    });
  }

  /// Put methods
  static Future<bool> putString(SettingKey<String> setting, String value) =>
      _put(setting, value);

  static Future<bool> putInt(SettingKey<int> setting, int value) =>
      _put(setting, value);

  static Future<bool> putBool(SettingKey<bool> setting, bool value) =>
      _put(setting, value);

  static Future<bool> _put(SettingKey setting, dynamic value) async {
    String method;

    switch (setting.valueType) {
      case SettingValueType.BOOLEAN:
        method = 'putBoolean';
        break;
      case SettingValueType.INT:
        method = 'putBoolean';
        break;
      case SettingValueType.STRING:
        method = 'putBoolean';
        break;
    }

    return await _channel.invokeMethod(method, {
      'type': resolveEnum(setting.type),
      'value': value,
      'setting': setting.name,
    });
  }

  /// Prop methods
  static Future<void> setProp(PropKey prop, String value) async =>
      await _channel.invokeMethod('setProp', {
        'key': prop.name,
        'value': value,
      });

  static Future<String> getProp(PropKey prop) async =>
      await _channel.invokeMethod('getProp', {
        'key': prop.name,
      });

  /// Overlay methods
  static Future<bool> reloadAssets(String pkg) async =>
      await _channel.invokeMethod('reloadAssets', {
        'pkg': pkg,
      });

  static Future<bool> overlaySetEnabled(String pkg, bool enable) async =>
      await _channel.invokeMethod('overlaySetEnabled', {
        'pkg': pkg,
        'enable': enable,
      });

  static Future<bool> overlaySetEnabledExclusive(
    String pkg,
    bool enable,
  ) async =>
      await _channel.invokeMethod('overlaySetEnabledExclusive', {
        'pkg': pkg,
        'enable': enable,
      });
  static Future<bool> overlaySetEnabledExclusiveInCategory(String pkg) async =>
      await _channel.invokeMethod('overlaySetEnabledExclusiveInCategory', {
        'pkg': pkg,
      });

  /// Utils
  static String resolveEnum(SettingType type) => type.toString().split('.')[1];
}

@immutable
class BaseKey {
  final String name;

  BaseKey._(this.name);
}

@immutable
class SettingKey<T> extends BaseKey {
  final SettingType type;
  final SettingValueType valueType;

  SettingKey({
    @required String name,
    @required this.type,
  })  : valueType = _getValueTypeFromT(T),
        super._(name);

  static SettingValueType _getValueTypeFromT(Type t) {
    if (t is bool) {
      return SettingValueType.BOOLEAN;
    }
    if (t is int || t is double) {
      return SettingValueType.INT;
    }
    return SettingValueType.STRING;
  }
}

@immutable
class PropKey extends BaseKey {
  PropKey({
    @required String name,
  }) : super._(name);
}

enum SettingType {
  SECURE,
  SYSTEM,
  GLOBAL,
}

enum SettingValueType {
  STRING,
  INT,
  BOOLEAN,
}
