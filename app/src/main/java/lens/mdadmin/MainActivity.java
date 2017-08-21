package lens.mdadmin;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  private DeviceAdminFragment mFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setDefaultFragment();
  }


  private void setDefaultFragment() {
    FragmentManager manager = getFragmentManager();
    FragmentTransaction transaction = manager.beginTransaction();

    mFragment = new DeviceAdminFragment();
    transaction.replace(R.id.container, mFragment);
    transaction.commit();
  }

}
