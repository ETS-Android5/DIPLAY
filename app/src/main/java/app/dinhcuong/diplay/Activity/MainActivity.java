package app.dinhcuong.diplay.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;

import app.dinhcuong.diplay.Adapter.ListSongsAdapter;
import app.dinhcuong.diplay.Adapter.MainViewPagerAdapter;
import app.dinhcuong.diplay.Fragment.Fragment_Home;
import app.dinhcuong.diplay.Fragment.Fragment_Library;
import app.dinhcuong.diplay.Fragment.Fragment_Search;
import app.dinhcuong.diplay.Model.Song;
import app.dinhcuong.diplay.PlaybackFragment;
import app.dinhcuong.diplay.R;
import app.dinhcuong.diplay.ViewFullPlaylistFragment;

public class MainActivity extends AppCompatActivity implements ListSongsAdapter.ISendDataListener, ViewFullPlaylistFragment.ISendDataListenerArray {
    private static final String TAG = MainActivity.class.getSimpleName();
    ChipNavigationBar bottomNavigationView;
    FragmentManager fragmentManager;

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
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {


        /*
        bottomNavigationView = findViewById(R.id.navigationBar);

        bottomNavigationView.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i) {
                    case R.id.home:
                        fragment = new Fragment_Home();
                        break;
                    case R.id.search:
                        fragment = new Fragment_Search();
                        break;
                    case R.id.library:
                        fragment = new Fragment_Library();
                        break;
                    case R.id.test:
                        fragment = new ViewFullPlaylistFragment();
                        break;
                }
                if (fragment != null){
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit();
                } else {
                    Log.e(TAG, "ERROR in creating fragment");
                }

            }
        });

         */

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


    //Function send data to playbackFragment
    @Override
    public void sendDataForPlayback(Song songArrayListSend) {
        PlaybackFragment playbackFragment = (PlaybackFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_playback_screen);
        playbackFragment.reloadData(songArrayListSend);
    }

    @Override
    public void sendDataForPlaybackArray(ArrayList<Song> songArrayListSend) {
        PlaybackFragment playbackFragment = (PlaybackFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_playback_screen);
        playbackFragment.reloadData(songArrayListSend);
    }
}