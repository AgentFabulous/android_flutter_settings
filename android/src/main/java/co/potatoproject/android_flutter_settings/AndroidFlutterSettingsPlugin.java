package co.potatoproject.android_flutter_settings;

import android.annotation.NonNull;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.om.IOverlayManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.provider.Settings;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.FlutterPlugin.FlutterPluginBinding;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * AndroidFlutterSettingsPlugin
 */
public class AndroidFlutterSettingsPlugin implements FlutterPlugin, ActivityAware {
    private MethodChannel mMethodProvider;

    public static void registerWith(Registrar registrar) {
        final MethodChannel methodProvider = new MethodChannel(registrar.messenger(), "android_flutter_settings/methods");
        methodProvider.setMethodCallHandler(new SettingsCallHandler(registrar.activity()));
    }
    
    @Override
    public void onAttachedToEngine(FlutterPluginBinding binding) {
        mMethodProvider = new MethodChannel(binding.getBinaryMessenger(), "android_flutter_settings/methods");
    }
    
    @Override
    public void onDetachedFromEngine(FlutterPluginBinding binding) {}

    @Override
    public void onAttachedToActivity(ActivityPluginBinding binding) {
        mMethodProvider.setMethodCallHandler(new SettingsCallHandler(binding.getActivity()));
    }

    @Override
    public void onDetachedFromActivity() {}

    @Override
    public void onReattachedToActivityForConfigChanges(ActivityPluginBinding binding) {}

    @Override
    public void onDetachedFromActivityForConfigChanges() {}
}
