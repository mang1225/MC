package lens.mdadmin;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

public class WifiReceiver extends BroadcastReceiver {

  String BTOOTH_ACTION_STATE_CHANGED = "android.bluetooth.adapter.action.STATE_CHANGED";

  private WifiManager wifiManager;//wifi
  private BluetoothAdapter bluetoothAdapter;//定义蓝牙适配器对象


  @Override
  public void onReceive(Context context, Intent intent) {

    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    if (wifiManager == null) {
      wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }
    // TODO Auto-generated method stub
    if (intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)) {
      //signal strength changed
    } else if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {//wifi连接上与否
//      System.out.println("网络状态改变");
      NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
      if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
//        System.out.println("wifi网络连接断开");
      } else if (info.getState().equals(NetworkInfo.State.CONNECTED)) {

        //连上就关闭
        wifiManager.setWifiEnabled(false);

//        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//        //获取当前wifi名称
//        System.out.println("连接到网络 " + wifiInfo.getSSID());

      }

    } else if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {//wifi打开与否
      int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
      if (wifistate == WifiManager.WIFI_STATE_DISABLED) {
//        System.out.println("系统关闭wifi");
      } else if (wifistate == WifiManager.WIFI_STATE_ENABLED) {
//        System.out.println("系统开启wifi");
      }
    } else if (intent.getAction().equals(BTOOTH_ACTION_STATE_CHANGED)) {//蓝牙
      int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_ON);

      if(state == BluetoothAdapter.STATE_TURNING_ON ||
      state == BluetoothAdapter.STATE_ON) {
        bluetoothAdapter.disable();
      }
    }
  }
}