import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

class AndroidFlutterSettings {
  static const MethodChannel _channel =
      const MethodChannel('android_flutter_settings/methods');

  /// Get methods
  static Future<String?> getString(SettingKey<String> setting) =>
      _get<String>(setting);

  static Future<int?> getInt(SettingKey<int> setting) => _get<int>(setting);

  static Future<bool?> getBool(SettingKey<bool> setting) => _get<bool>(setting);

  static Future<T?> _get<T>(SettingKey setting) async {
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

    return await _channel.invokeMethod<T?>(method, {
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
        method = 'putInt';
        break;
      case SettingValueType.STRING:
        method = 'putString';
        break;
    }

    return await _channel.invokeMethod(method, {
          'type': resolveEnum(setting.type),
          'value': value,
          'setting': setting.name,
        }) ??
        false;
  }

  /// Prop methods
  static Future<void> setProp(PropKey prop, String value) async =>
      await _channel.invokeMethod('setProp', {
        'key': prop.name,
        'value': value,
      });

  static Future<String?> getProp(PropKey prop) async =>
      await _channel.invokeMethod('getProp', {
        'key': prop.name,
      });

  static Future<void> setPropByName(String name, String value) async =>
      await _channel.invokeMethod('setProp', {
        'key': name,
        'value': value,
      });

  static Future<String?> getPropByName(String name) async =>
      await _channel.invokeMethod('getProp', {
        'key': name,
      });

  /// Utils
  static String resolveEnum(SettingType type) => type.toString().split('.')[1];
}

@immutable
class BaseKey {
  final String name;

  BaseKey._(this.name);

  @override
  bool operator ==(Object other) {
    if (other is BaseKey) {
      return this.name == other.name;
    }
    return false;
  }

  @override
  int get hashCode => name.hashCode;
}

@immutable
class SettingKey<T> extends BaseKey {
  final SettingType type;
  final SettingValueType valueType;

  SettingKey(
    String name,
    this.type,
  )   : valueType = _getValueTypeFromT(T),
        super._(name);

  static SettingValueType _getValueTypeFromT(Type t) {
    if (t == bool) {
      return SettingValueType.BOOLEAN;
    }
    if (t == int || t == double) {
      return SettingValueType.INT;
    }
    return SettingValueType.STRING;
  }

  @override
  bool operator ==(Object other) {
    if (other is SettingKey<T>) {
      return this.name == other.name &&
          this.type == other.type &&
          this.valueType == other.valueType;
    }
    return false;
  }

  @override
  int get hashCode => name.hashCode ^ type.hashCode ^ valueType.hashCode;
}

@immutable
class PropKey extends BaseKey {
  PropKey(String name) : super._(name);
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
