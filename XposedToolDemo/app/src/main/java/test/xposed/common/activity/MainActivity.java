package test.xposed.common.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.hlm.xposed.common.BuildConfig;
import com.hu.myxposeddemo.R;

import test.xposed.common.dialog.SettingsDialog;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test_common);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (BuildConfig.DEBUG) {
            getMenuInflater().inflate(R.menu.activity_main_menu, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (R.id.menu_settings == item.getItemId()) {
            SettingsDialog dialog = new SettingsDialog();
            dialog.show(getFragmentManager(), "settings");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
