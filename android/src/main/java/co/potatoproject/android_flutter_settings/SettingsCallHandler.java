package co.potatoproject.android_flutter_settings;

import android.annotation.NonNull;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.om.IOverlayManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.provider.Settings;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

@SuppressLint({"NewApi", "LongLogTag"})
public class SettingsCallHandler implements MethodCallHandler {
    private static final String TAG = "AndroidFlutterSettingsPlugin";
    
    private Activity mActivity;

    public SettingsCallHandler(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        switch (call.method) {
            case "getString": {
                String type = call.argument("type");
                String setting = call.argument("setting");
                resultSuccess(result, getString(setting, SettingType.valueOf(type)));
                break;
            }
            case "getInt": {
                String type = call.argument("type");
                String setting = call.argument("setting");
                int retInt = getInt(setting, SettingType.valueOf(type));
                Integer ret;
                if (retInt != Integer.MIN_VALUE) ret = retInt;
                else ret = null;
                resultSuccess(result, ret);
                break;
            }
            case "getBoolean": {
                String type = call.argument("type");
                String setting = call.argument("setting");
                resultSuccess(result, getBoolean(setting, SettingType.valueOf(type)));
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
                Integer value;
                Object val = call.argument("value");
                if (val instanceof Long) {
                    value = ((Long) val).intValue();
                } else {
                    value = (Integer) val;
                }
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

    private void resultSuccess(final Result result, final Object object) {
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
        switch (type) {
            case SYSTEM:
                return Settings.System.getInt(mActivity.getContentResolver(), setting, Integer.MIN_VALUE);
            case SECURE:
                return Settings.Secure.getInt(mActivity.getContentResolver(), setting, Integer.MIN_VALUE);
            case GLOBAL:
                return Settings.Global.getInt(mActivity.getContentResolver(), setting, Integer.MIN_VALUE);
            default:
                return -1;
        }
    }

    private Boolean getBoolean(String setting, SettingType type) {
        int retInt = getInt(setting, type);
        if (retInt == Integer.MIN_VALUE)
            return null;
        else
            return getInt(setting, type) != 0;
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

    enum SettingType {
        SYSTEM,
        SECURE,
        GLOBAL,
    }
}