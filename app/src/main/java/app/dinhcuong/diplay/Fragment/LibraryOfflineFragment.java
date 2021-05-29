package app.dinhcuong.diplay.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Magnifier;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.dinhcuong.diplay.Adapter.LocalSongAdapter;
import app.dinhcuong.diplay.Model.Song;
import app.dinhcuong.diplay.R;


public class LibraryOfflineFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    LocalSongAdapter localSongAdapter;
    TextView button_play_now;
    public static final int REQUEST_CODE = 1;
    ArrayList<Song> localSongArrayList;

    private ISendDataListenerArray iSendDataListenerArray;
    public interface ISendDataListenerArray {
        //Function send data to playbackFragment
        void sendDataForPlaybackArray(ArrayList<Song> songArrayListSend, Boolean MODE_OFFLINE, String name_playlist, String source);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_library_offline, container, false);
        permission();
        mapping();
        init();
        return view;
    }

    private void mapping() {
        recyclerView = view.findViewById(R.id.recycler_view_library_offline);
        button_play_now = view.findViewById(R.id.button_play_now);
    }
    private void init() {
        recyclerView.setHasFixedSize(true);
        if (!(localSongArrayList.size() < 1) ){
            localSongAdapter = new LocalSongAdapter(getContext(), localSongArrayList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(localSongAdapter);
        } else {

        }
        button_play_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Declare Interface
                iSendDataListenerArray = (ISendDataListenerArray) getActivity();
                //Use Interface to send data to MainActivity
                String text_source = getContext().getResources().getString(R.string.LIBRARY);
                String text_name_playlist = getContext().getResources().getString(R.string.LOCAL_STORAGE);
                iSendDataListenerArray.sendDataForPlaybackArray(localSongArrayList, true, text_name_playlist, text_source);
            }
        });
    }

    private void permission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else {
            Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_SHORT).show();
            localSongArrayList = getLocalAudio(getContext());
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                localSongArrayList = getLocalAudio(getContext());
            } else{
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        }
    }


    public static ArrayList<Song> getLocalAudio(Context context){
        ArrayList<Song> localSongArrayList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST
        };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        Log.e("TEST", "cursor");
        if (cursor != null ){
            while (cursor.moveToNext()){
                String album = cursor.getString(0);
                String title = cursor.getString(1);
                String duration = cursor.getString(2);
                String path = cursor.getString(3);
                String artist = cursor.getString(4);

                Song localSong = new Song(title, artist, duration, album, path);
                Log.e("Path: " + path, ". Album: " + album);
                localSongArrayList.add(localSong);
            }
            cursor.close();
        }
        return localSongArrayList;
     }
}