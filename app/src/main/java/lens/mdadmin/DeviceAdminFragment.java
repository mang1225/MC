package lens.mdadmin;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

/**
 * Created by floatingmuseum on 2016/6/6.
 */
public class DeviceAdminFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener, PhoneSettingsManager.MessageListener {

  private static PhoneSettingsManager phoneSettingsManager;
  private DevicePolicyManager dpm;
  private PackageManager pm;
  private ComponentName mComponentName;
  private Activity activity;

  @Override
  public void onToast(String msg) {
    ToastUtil.show(msg);
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.pref_deviceadmin);
    initListener();
    phoneSettingsManager = new PhoneSettingsManager(this);
    activity = DeviceAdminFragment.this.getActivity();
    if (dpm == null) {
      dpm = (DevicePolicyManager) activity.getSystemService(Context.DEVICE_POLICY_SERVICE);
    }

    if (pm == null) {
      pm = activity.getPackageManager();
    }
    mComponentName = MyDeviceAdminReceiver.getComponentName(activity);
//        LauncherApps la = (LauncherApps) activity.getSystemService(Context.LAUNCHER_APPS_SERVICE);
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//      Logger.d("isProfileOwnerApp:" + dpm.isProfileOwnerApp(activity.getPackageName()));
//      Logger.d("isDeviceOwnerApp:" + dpm.isDeviceOwnerApp(activity.getPackageName()));
//    }
  }

  @Override
  public void onResume() {
    super.onResume();
    setEnabel(checkDeviceAdminEnabled() ? true : false);
  }

  private void setEnabel(boolean isEnable) {
    findPreference("disabled_camera").setEnabled(isEnable);
    findPreference("disabled_usb").setEnabled(isEnable);
    findPreference("disabled_wifi").setEnabled(isEnable);
    findPreference("disabled_bluetooth").setEnabled(isEnable);
    findPreference("lock_screen").setEnabled(isEnable);
  }

  private void initListener() {
//        findPreference("deviceadmin_support").setOnPreferenceClickListener(this);
    findPreference("deviceadmin_enabled").setOnPreferenceClickListener(this);
    findPreference("get_deviceadmin").setOnPreferenceClickListener(this);
    findPreference("remove_deviceadmin").setOnPreferenceClickListener(this);
//    findPreference("camera_status").setOnPreferenceClickListener(this);
    findPreference("disabled_camera").setOnPreferenceClickListener(this);
//    findPreference("usb_status").setOnPreferenceClickListener(this);
    findPreference("disabled_usb").setOnPreferenceClickListener(this);
//    findPreference("wifi_status").setOnPreferenceClickListener(this);
    findPreference("disabled_wifi").setOnPreferenceClickListener(this);
//    findPreference("bluetooth_status").setOnPreferenceClickListener(this);
    findPreference("disabled_bluetooth").setOnPreferenceClickListener(this);
    findPreference("lock_screen").setOnPreferenceClickListener(this);
//    findPreference("reset_password").setOnPreferenceChangeListener(this);
//    findPreference("password_quality").setOnPreferenceChangeListener(this);
//    findPreference("disable_keyguard_feature").setOnPreferenceClickListener(this);
  }


  @Override
  public boolean onPreferenceClick(Preference preference) {
    if (!preference.getKey().equals("get_deviceadmin")) {
      if (!checkDeviceAdminEnabled()) {
        ToastUtil.show("权限未获取");
        return true;
      }
    }
//    if(preference instanceof CheckBoxPreference){
//      CheckBoxPreference preference1 = (CheckBoxPreference)preference;
//      if(preference1.isChecked()){
//        preference1.setChecked(true);
//      }else {
//        preference1.setChecked(false);
//      }
//    }

    switch (preference.getKey()) {
//            case "deviceadmin_support":
//                ToastUtil.show("是否支持DeviceAdmin:"+checkSupportDeviceAdmin());
//            break;
      case "deviceadmin_enabled":
        ToastUtil.show("是否已激活Device Admin权限:" + checkDeviceAdminEnabled());
        break;
      case "remove_deviceadmin":
        removeAdmin();
        break;
      case "get_deviceadmin":
        getDeviceAdmin();
        break;
      case "camera_status":
        if (checkCameraState()) {
          ToastUtil.show("相机已禁用");
        } else {
          ToastUtil.show("相机已开启");
        }
        break;
      case "disabled_camera":
        disableCamera();
        break;
      case "lock_screen":
        lockScreen();
        break;

      case "usb_status":
        break;

      case "disabled_usb":
        break;

      case "wifi_status":
        break;

      case "disabled_wifi":
        phoneSettingsManager.toggleWifi(getActivity());
//        WifiOpenHelper helper = new WifiOpenHelper(getActivity().getApplicationContext());
//        helper.creatWifiLock("tag");
//        helper.releaseWifiLock();
        break;

      case "bluetooth_status":
        break;

      case "disabled_bluetooth":
        phoneSettingsManager.toggleBluetooth();
        break;
//      case "disable_keyguard_feature":
//        disableKeyguardFeatures();
//        break;
    }
    return true;
  }

  /**
   * To create a work profile on a device that already has a personal profile,
   * first find out whether the device can support a work profile. To do this,
   * check whether the device supports the FEATURE_MANAGED_USERS system feature:
   */
  private boolean checkSupportDeviceAdmin() {
    return pm.hasSystemFeature(PackageManager.FEATURE_MANAGED_USERS);
  }

  private boolean checkDeviceAdminEnabled() {
    return dpm.isAdminActive(mComponentName);
  }

  private void getDeviceAdmin() {
    if (checkDeviceAdminEnabled()) {
      ToastUtil.show("已激活");
      return;
    }
    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, getString(R.string.device_admin_description));
    startActivity(intent);
  }

  private void removeAdmin() {
    dpm.removeActiveAdmin(mComponentName);
    setEnabel(false);
  }

  private boolean checkCameraState() {
    return dpm.getCameraDisabled(mComponentName);
  }


  private void disableCamera() {
    if (checkDeviceAdminEnabled()) {
      dpm.setCameraDisabled(mComponentName, !checkCameraState());
      if (checkCameraState()) {
        ToastUtil.show("相机已禁用");
      } else {
        ToastUtil.show("相机已开启");
      }
    } else {
      ToastUtil.show("权限未获取");
    }
  }

  private void lockScreen() {
    if (!checkDeviceAdminEnabled()) {
      ToastUtil.show("权限未获取");
      return;
    }
    dpm.lockNow();
  }

  /**
   * seems no effect
   */
  private void disableKeyguardFeatures() {
//    Logger.d("which:" + dpm.getKeyguardDisabledFeatures(mComponentName));
    dpm.setKeyguardDisabledFeatures(mComponentName, DevicePolicyManager.KEYGUARD_DISABLE_WIDGETS_ALL);
//    Logger.d("which:" + dpm.getKeyguardDisabledFeatures(mComponentName));
  }
}
