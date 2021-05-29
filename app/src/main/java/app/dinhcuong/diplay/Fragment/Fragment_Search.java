package app.dinhcuong.diplay.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;

import app.dinhcuong.diplay.Adapter.AlbumAdapter;
import app.dinhcuong.diplay.Adapter.ListSongsAdapter;
import app.dinhcuong.diplay.Adapter.PlaylistAdapter;
import app.dinhcuong.diplay.Model.Album;
import app.dinhcuong.diplay.Model.Playlist;
import app.dinhcuong.diplay.Model.Song;
import app.dinhcuong.diplay.R;
import app.dinhcuong.diplay.Service.APIService;
import app.dinhcuong.diplay.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Search extends Fragment {
    View view;
    EditText search_src_text;
    AlbumAdapter albumAdapter;
    ListSongsAdapter listSongsAdapter;
    PlaylistAdapter playlistAdapter;

    ArrayList<Album> albumArrayList;
    ArrayList<Playlist> playlistArrayList;
    ArrayList<Song> songArrayList;

    LottieAnimationView lottieAnimationView, lottieAnimationView2, lottieAnimationView3;
    RecyclerView recyclerViewSongs, recyclerViewAlbums, recyclerViewPlaylists;
    ConstraintLayout constraintLayoutIconSearch, constraintLayoutResult;
    TextView noDataSong, noDataAlbum,noDataPlaylist, resultSearch;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_search, container, false);


        mapping();

        search_src_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    getDataSearch();
                    constraintLayoutIconSearch.setVisibility(View.GONE);
                    constraintLayoutResult.setVisibility(View.VISIBLE);
                return false;
            }
        });
        search_src_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(search_src_text.getText().toString().equals("")){
                    constraintLayoutIconSearch.setVisibility(View.VISIBLE);
                    constraintLayoutResult.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    private void mapping() {
        search_src_text = view.findViewById(R.id.search_src_text);
        lottieAnimationView = view.findViewById(R.id.animation_loading);
        lottieAnimationView2 = view.findViewById(R.id.animation_loading_2);
        lottieAnimationView3 = view.findViewById(R.id.animation_loading_3);
        recyclerViewSongs = view.findViewById(R.id.recycler_view_search_songs);
        recyclerViewAlbums = view.findViewById(R.id.recycler_view_search_albums);
        recyclerViewPlaylists = view.findViewById(R.id.recycler_view_search_playlists);
        constraintLayoutIconSearch = view.findViewById(R.id.layout_icon_search);
        constraintLayoutResult = view.findViewById(R.id.result);
        noDataAlbum = view.findViewById(R.id.text_nodata_album);
        noDataPlaylist = view.findViewById(R.id.text_nodata_playlist);
        noDataSong = view.findViewById(R.id.text_nodata_song);
        resultSearch = view.findViewById(R.id.result_search);

    }

    private void getDataSearch() {

        clear();


        resultSearch.setVisibility(View.VISIBLE);
        String text_content = getContext().getResources().getString(R.string.search_with);
        resultSearch.setText(text_content + " \""+ search_src_text.getText().toString() + "\"");

        DataService dataService = APIService.getService();

        Call<List<Album>> callbackAlbum = dataService.handlerSearchAlbum(search_src_text.getText().toString(), "album");
        callbackAlbum.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                albumArrayList = (ArrayList<Album>) response.body();
                if (albumArrayList.size() > 0){
                    albumAdapter = new AlbumAdapter(getActivity(), albumArrayList, "LIBRARY");
                    lottieAnimationView2.setVisibility(View.GONE);
                    recyclerViewAlbums.setVisibility(View.VISIBLE);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerViewAlbums.setLayoutManager(linearLayoutManager);
                    recyclerViewAlbums.setAdapter(albumAdapter);
                } else {
                    noDataAlbum.setVisibility(View.VISIBLE);
                    lottieAnimationView2.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {

            }
        });

        Call<List<Song>> callbackSong = dataService.handlerSearchSong(search_src_text.getText().toString(), "song");
        callbackSong.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                songArrayList = (ArrayList<Song>) response.body();
                if (songArrayList.size() > 0 ){
                    listSongsAdapter = new ListSongsAdapter(getActivity(), songArrayList);
                    lottieAnimationView.setVisibility(View.GONE);
                    recyclerViewSongs.setVisibility(View.VISIBLE);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerViewSongs.setLayoutManager(linearLayoutManager);
                    recyclerViewSongs.setAdapter(listSongsAdapter);
                } else {
                    noDataSong.setVisibility(View.VISIBLE);
                    lottieAnimationView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {

            }
        });

        Call<List<Playlist>> callbackPlaylist = dataService.handlerSearchPlaylist(search_src_text.getText().toString(), "playlist");
        callbackPlaylist.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                playlistArrayList = (ArrayList<Playlist>) response.body();
                if (playlistArrayList.size() > 0 ){
                    playlistAdapter = new PlaylistAdapter(getActivity(), playlistArrayList, "LIBRARY");
                    lottieAnimationView3.setVisibility(View.GONE);
                    recyclerViewPlaylists.setVisibility(View.VISIBLE);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerViewPlaylists.setLayoutManager(linearLayoutManager);
                    recyclerViewPlaylists.setAdapter(playlistAdapter);
                } else {
                    noDataPlaylist.setVisibility(View.VISIBLE);
                    lottieAnimationView3.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {

            }
        });
    }

    public void clear() {

        lottieAnimationView2.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView3.setVisibility(View.VISIBLE);
        noDataAlbum.setVisibility(View.GONE);
        noDataPlaylist.setVisibility(View.GONE);
        noDataSong.setVisibility(View.GONE);
        recyclerViewAlbums.setVisibility(View.GONE);
        recyclerViewPlaylists.setVisibility(View.GONE);
        recyclerViewSongs.setVisibility(View.GONE);

    }
}
