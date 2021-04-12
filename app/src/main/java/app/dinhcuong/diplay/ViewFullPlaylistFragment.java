package app.dinhcuong.diplay;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import app.dinhcuong.diplay.Adapter.AlbumAdapter;
import app.dinhcuong.diplay.Adapter.ListSongsAdapter;
import app.dinhcuong.diplay.Model.Playlist;
import app.dinhcuong.diplay.Model.Song;
import app.dinhcuong.diplay.Service.APIService;
import app.dinhcuong.diplay.Service.DataService;
import jp.wasabeef.picasso.transformations.BlurTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewFullPlaylistFragment extends Fragment {

    CollapsingToolbarLayout collapsingToolbarLayout;
    CoordinatorLayout coordinatorLayout;
    RecyclerView recyclerView;
    Toolbar toolbar;
    ExtendedFloatingActionButton extendedFloatingActionButton;
    View view;
    ImageView imageViewBackground, imageViewPlaylist;
    Playlist playlist;
    ArrayList<Song> songArrayList;
    ListSongsAdapter listSongsAdapter;

    private ISendDataListenerArray iSendDataListenerArray;
    public interface ISendDataListenerArray {
        //Function send data to playbackFragment
        void sendDataForPlaybackArray(ArrayList<Song> songArrayListSend);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_view_full_playlist, container, false);

        mapping();
        getData();
        init();

        if (playlist != null && !playlist.getNamePlaylist().equals("")){
            setValueInView(playlist.getNamePlaylist(), playlist.getImagePlaylist());
            getDataPlaylist(playlist.getIdPlaylist());
            eventClick();
        }
        return view;
    }

    private void mapping() {
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar_layout_playlist);
        coordinatorLayout = view.findViewById(R.id.coordinator_layout_playlist);
        recyclerView = view.findViewById(R.id.recycler_playlist);
        toolbar = view.findViewById(R.id.toolbar_playlist);
        extendedFloatingActionButton = view.findViewById(R.id.playlist_floating_action_button);
        imageViewBackground = view.findViewById(R.id.image_playlist_background);
        imageViewPlaylist = view.findViewById(R.id.image_playlist);
    }

    private void init() {
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });
        extendedFloatingActionButton.setEnabled(false);
    }

    private void getDataPlaylist(String id_playlist) {
        DataService dataService = APIService.getService();
        Call<List<Song>> callback = dataService.getSongPlaylist(id_playlist);
        callback.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                songArrayList = (ArrayList<Song>) response.body();
                listSongsAdapter = new ListSongsAdapter(getActivity(), songArrayList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(listSongsAdapter);

            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {

            }
        });


    }

    private void setValueInView(String name, String image) {
        collapsingToolbarLayout.setTitle(name);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        Picasso.get().load(image).transform(new BlurTransformation(getContext(), 15, 1)).into(imageViewBackground);
        Picasso.get().load(image).into(imageViewPlaylist);
    }

    private void getData() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            playlist = (Playlist) bundle.getSerializable("playlist");
            Log.e("TEST1234", playlist.getNamePlaylist());
        }
    }

    private void eventClick() {
        extendedFloatingActionButton.setEnabled(true);
        extendedFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                PlaybackFragment playbackFragment = new PlaybackFragment();

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("song", songArrayList);
                bundle.putString("key", "multi");
                playbackFragment.setArguments(bundle);
                */

                //Declare Interface
                iSendDataListenerArray = (ISendDataListenerArray) getActivity();
                //Use Interface to send data to MainActivity
                iSendDataListenerArray.sendDataForPlaybackArray(songArrayList);

                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.bottomPlaybackFragment, playbackFragment).addToBackStack(null).commit();
            }
        });
    }
}