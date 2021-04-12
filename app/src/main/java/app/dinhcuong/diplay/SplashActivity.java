package app.dinhcuong.diplay;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import app.dinhcuong.diplay.Activity.MainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowsFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }

        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            setWindowsFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        // Start home activity
        startActivity(new Intent(app.dinhcuong.diplay.SplashActivity.this, MainActivity.class));

        // close splash activity
        finish();
    }
    private static void setWindowsFlag(Activity activity, final int Bits, Boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if(on) {
            winParams.flags |= Bits;
        } else {
            winParams.flags &= ~Bits;
        }
        win.setAttributes(winParams);
    }
}