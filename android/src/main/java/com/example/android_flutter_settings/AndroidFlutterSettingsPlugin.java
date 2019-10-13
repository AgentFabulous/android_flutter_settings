package com.example.android_flutter_settings;

import android.annotation.NonNull;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.SystemProperties;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;

/**
 * AndroidFlutterSettingsPlugin
 */

@SuppressLint({"NewApi", "LongLogTag"})
public class AndroidFlutterSettingsPlugin implements MethodCallHandler {

    private static final String TAG = "AndroidFlutterSettingsPlugin";
    private final Activity mActivity;

    private AndroidFlutterSettingsPlugin(Activity activity) {
        mActivity = activity;
    }


    public static void registerWith(Registrar registrar) {
        final MethodChannel methodProvider = new MethodChannel(registrar.messenger(), "android_flutter_updater/methods");
        methodProvider.setMethodCallHandler(new AndroidFlutterSettingsPlugin(registrar.activity()));
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        switch (call.method) {
            case "getPlatformVersion":
                resultSuccess(result, "Android " + android.os.Build.VERSION.RELEASE);
                break;
            case "getString": {
                String type = call.argument("type");
                String setting = call.argument("setting");
                resultSuccess(result, getString(setting, SettingType.valueOf(type)));
                break;
            }
            case "getInt": {
                String type = call.argument("type");
                String setting = call.argument("setting");
                resultSuccess(result, getInt(setting, SettingType.valueOf(type)));
                break;
            }
            case "getBoolean": {
                String type = call.argument("type");
                String setting = call.argument("setting");
                resultSuccess(result, getBoolean(setting, SettingType.valueOf(type)));
                break;
            }
            case "getFloat": {
                String type = call.argument("type");
                String setting = call.argument("setting");
                resultSuccess(result, getFloat(setting, SettingType.valueOf(type)));
                break;
            }
            case "getLong": {
                String type = call.argument("type");
                String setting = call.argument("setting");
                resultSuccess(result, getLong(setting, SettingType.valueOf(type)));
                break;
            }
            case "putString": {
                String type = call.argument("type");
                String value = call.argument("value");
                String setting = call.argument("setting");
                resultSuccess(result, putString(setting, value, SettingType.valueOf(type)));
                break;
            }
            case "putInt": {
                String type = call.argument("type");
                Integer value = call.argument("value");
                String setting = call.argument("setting");
                resultSuccess(result, value != null &&
                        putInt(setting, value, SettingType.valueOf(type)));
                break;
            }
            case "putBoolean": {
                String type = call.argument("type");
                Boolean value = call.argument("value");
                String setting = call.argument("setting");
                resultSuccess(result, value != null &&
                        putBoolean(setting, value, SettingType.valueOf(type)));
                break;
            }
            case "putFloat": {
                String type = call.argument("type");
                Float value = call.argument("value");
                String setting = call.argument("setting");
                resultSuccess(result, value != null &&
                        putFloat(setting, value, SettingType.valueOf(type)));
                break;
            }
            case "putLong": {
                String type = call.argument("type");
                Long value = call.argument("value");
                String setting = call.argument("setting");
                resultSuccess(result, value != null &&
                        putLong(setting, value, SettingType.valueOf(type)));
                break;
            }
            case "setProp": {
                String key = call.argument("key");
                String value = call.argument("value");
                SystemProperties.set(key, value);
                resultSuccess(result, null);
                break;
            }
            case "getProp": {
                String key = call.argument("key");
                resultSuccess(result, SystemProperties.get(key));
                break;
            }
            default:
                result.notImplemented();
        }
    }

    private void resultSuccess(final MethodChannel.Result result, final Object object) {
        if (mActivity == null) return;
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.success(object);
            }
        });
    }


    /**
     * Get methods
     */
    private String getString(String setting, SettingType type) {
        switch (type) {
            case SYSTEM:
                return Settings.System.getString(mActivity.getContentResolver(), setting);
            case SECURE:
                return Settings.Secure.getString(mActivity.getContentResolver(), setting);
            case GLOBAL:
                return Settings.Global.getString(mActivity.getContentResolver(), setting);
            default:
                return null;
        }
    }

    private int getInt(String setting, SettingType type) {
        try {
            switch (type) {
                case SYSTEM:
                    return Settings.System.getInt(mActivity.getContentResolver(), setting);
                case SECURE:
                    return Settings.Secure.getInt(mActivity.getContentResolver(), setting);
                case GLOBAL:
                    return Settings.Global.getInt(mActivity.getContentResolver(), setting);
                default:
                    return -1;
            }
        } catch (SettingNotFoundException e) {
            Log.e(TAG, "Setting not found: " + type.toString() + '/' + setting);
            return -1;
        }
    }

    private boolean getBoolean(String setting, SettingType type) {
        return getInt(setting, type) != 0;
    }

    private float getFloat(String setting, SettingType type) {
        try {
            switch (type) {
                case SYSTEM:
                    return Settings.System.getFloat(mActivity.getContentResolver(), setting);
                case SECURE:
                    return Settings.Secure.getFloat(mActivity.getContentResolver(), setting);
                case GLOBAL:
                    return Settings.Global.getFloat(mActivity.getContentResolver(), setting);
                default:
                    return -1;
            }
        } catch (SettingNotFoundException e) {
            Log.e(TAG, "Setting not found: " + type.toString() + '/' + setting);
            return -1;
        }
    }

    private long getLong(String setting, SettingType type) {
        try {
            switch (type) {
                case SYSTEM:
                    return Settings.System.getLong(mActivity.getContentResolver(), setting);
                case SECURE:
                    return Settings.Secure.getLong(mActivity.getContentResolver(), setting);
                case GLOBAL:
                    return Settings.Global.getLong(mActivity.getContentResolver(), setting);
                default:
                    return -1;
            }
        } catch (SettingNotFoundException e) {
            Log.e(TAG, "Setting not found: " + type.toString() + '/' + setting);
            return -1;
        }
    }

    /**
     * Put methods
     */
    private boolean putString(String setting, String value, SettingType type) {
        switch (type) {
            case SYSTEM:
                return Settings.System.putString(mActivity.getContentResolver(), setting, value);
            case SECURE:
                return Settings.Secure.putString(mActivity.getContentResolver(), setting, value);
            case GLOBAL:
                return Settings.Global.putString(mActivity.getContentResolver(), setting, value);
            default:
                return false;
        }
    }

    private boolean putInt(String setting, int value, SettingType type) {
        switch (type) {
            case SYSTEM:
                return Settings.System.putInt(mActivity.getContentResolver(), setting, value);
            case SECURE:
                return Settings.Secure.putInt(mActivity.getContentResolver(), setting, value);
            case GLOBAL:
                return Settings.Global.putInt(mActivity.getContentResolver(), setting, value);
            default:
                return false;
        }
    }

    private boolean putBoolean(String setting, boolean value, SettingType type) {
        return putInt(setting, value ? 1 : 0, type);
    }

    private boolean putFloat(String setting, float value, SettingType type) {
        switch (type) {
            case SYSTEM:
                return Settings.System.putFloat(mActivity.getContentResolver(), setting, value);
            case SECURE:
                return Settings.Secure.putFloat(mActivity.getContentResolver(), setting, value);
            case GLOBAL:
                return Settings.Global.putFloat(mActivity.getContentResolver(), setting, value);
            default:
                return false;
        }
    }

    private boolean putLong(String setting, long value, SettingType type) {
        switch (type) {
            case SYSTEM:
                return Settings.System.putLong(mActivity.getContentResolver(), setting, value);
            case SECURE:
                return Settings.Secure.putLong(mActivity.getContentResolver(), setting, value);
            case GLOBAL:
                return Settings.Global.putLong(mActivity.getContentResolver(), setting, value);
            default:
                return false;
        }
    }

    enum SettingType {
        SYSTEM,
        SECURE,
        GLOBAL,
    }
}
