package app.dinhcuong.diplay.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.Locale;

import app.dinhcuong.diplay.Adapter.ListSongsAdapter;
import app.dinhcuong.diplay.Adapter.LocalSongAdapter;
import app.dinhcuong.diplay.Adapter.SongFavoriteAdapter;
import app.dinhcuong.diplay.Fragment.LibraryOfflineFragment;
import app.dinhcuong.diplay.Fragment.PlaybackBottomScreenFragment;
import app.dinhcuong.diplay.Model.Song;
import app.dinhcuong.diplay.Fragment.PlaybackFragment;
import app.dinhcuong.diplay.R;
import app.dinhcuong.diplay.Fragment.DetailHomePlaylistItemFragment;

public class MainActivity extends AppCompatActivity implements
        ListSongsAdapter.ISendDataListener,
        DetailHomePlaylistItemFragment.ISendDataListenerArray,
        SongFavoriteAdapter.ISendDataListener,
        LocalSongAdapter.ISendDataListener,
        LibraryOfflineFragment.ISendDataListenerArray{

    private static final String TAG = MainActivity.class.getSimpleName();
    ChipNavigationBar bottomNavigationView;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale(this);

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowsFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }

        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
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
                        fragment = new DetailHomePlaylistItemFragment();
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
    public void sendDataForPlaybackArray(ArrayList<Song> songArrayListSend, Boolean MODE_OFFLINE, String name_playlist, String source) {
        PlaybackFragment playbackFragment = (PlaybackFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_playback_screen);
        playbackFragment.reloadData(songArrayListSend, MODE_OFFLINE, name_playlist, source);
    }

    @Override
    public void sendDataForPlayback(Song songArrayListSend, boolean MODE_OFFLINE, String name_playlist, String source) {
        PlaybackFragment playbackFragment = (PlaybackFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_playback_screen);
        playbackFragment.reloadData(songArrayListSend, MODE_OFFLINE, name_playlist, source);
    }

    public static void setLocale(Activity activity, String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = activity.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }


    //Load language saved in shared preferences
    public static void loadLocale(Activity activity){
        SharedPreferences sharedPreferences = activity.getSharedPreferences("Setting", activity.MODE_PRIVATE);
        int language = sharedPreferences.getInt("lang",0);
        if (language == 0){
            setLocale(activity, "en");
        } else if (language == 1){
            setLocale(activity, "vi");
        } else if (language == 2){
            setLocale(activity, "ru");
        }
        Log.e("ACB" + language, "ANC");
    }
}