import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:android_flutter_settings/android_flutter_settings.dart';

void main() {
  const MethodChannel channel = MethodChannel('android_flutter_settings');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await AndroidFlutterSettings.platformVersion, '42');
  });
}
