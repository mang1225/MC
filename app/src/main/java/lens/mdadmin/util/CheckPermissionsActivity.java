/**
 *
 */
package lens.mdadmin.util;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;

/**
 * 继承了Activity，实现Android6.0的运行时权限检测
 * 需要进行运行时权限检测的Activity可以继承这个类
 *
 * @author hongming.wang
 * @创建时间：2016年5月27日 下午3:01:31
 * @项目名称： AMapLocationDemo
 * @文件名称：PermissionsChecker.java
 * @类型名称：PermissionsChecker
 * @since 2.5.0
 */
public abstract class CheckPermissionsActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {

  private static final int PERMISSON_REQUESTCODE = 0;
  /**
   * 需要进行检测的权限数组
   */
  protected String[] needPermissions = {
      Manifest.permission.CAMERA
  };
  /**
   * 判断是否需要检测，防止不停的弹框
   */
  private boolean isNeedCheck = true;

  @Override
  protected void onResume() {
    super.onResume();
    if (isNeedCheck) {
      checkPermissions(needPermissions);
    }
  }

  /**
   * 检查权限
   */
  private void checkPermissions(String... permissions) {
    List<String> needRequestPermissonList = findDeniedPermissions(permissions);
    if (null != needRequestPermissonList && needRequestPermissonList.size() > 0) {
      ActivityCompat.requestPermissions(this, needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]), PERMISSON_REQUESTCODE);
    }
  }

  /**
   * 获取权限集中需要申请权限的列表
   */
  private List<String> findDeniedPermissions(String[] permissions) {
    List<String> needRequestPermissonList = new ArrayList<String>();
    for (String perm : permissions) {
      if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED || ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
        needRequestPermissonList.add(perm);
      }
    }
    return needRequestPermissonList;
  }

  /**
   * 检测是否说有的权限都已经授权
   */
  private boolean verifyPermissions(int[] grantResults) {
    for (int result : grantResults) {
      if (result != PackageManager.PERMISSION_GRANTED) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] paramArrayOfInt) {
    if (requestCode == PERMISSON_REQUESTCODE) {
      if (!verifyPermissions(paramArrayOfInt)) {
//        showMissingPermissionDialog();
        isNeedCheck = false;
      }
    }
  }

  /**
   * 显示提示信息
   */
//  private void showMissingPermissionDialog() {
//    final NiftyDialogBuilder builder = NiftyDialogBuilder.getInstance(this);
//    builder.withTitle(getString(R.string.notifyTitle))
//        .withMessage(getString(R.string.notifyMsg))
//        .withDuration(200)
//        .withButton1Text(getString(R.string.cancel))
//        .withButton2Text(getString(R.string.setting))
//        .setButton1Click(new View.OnClickListener() {
//          @Override public void onClick(View v) {
//            builder.dismiss();
//            finish();
//          }
//        })
//        .setButton2Click(new View.OnClickListener() {
//          @Override public void onClick(View v) {
//            builder.dismiss();
//            startAppSettings();
//          }
//        })
//        .show();
//  }

  /**
   * 启动应用的设置
   */
  private void startAppSettings() {
    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
    intent.setData(Uri.parse("package:" + getPackageName()));
    startActivity(intent);
  }
}
