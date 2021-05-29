package app.dinhcuong.diplay.Fragment;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.dinhcuong.diplay.Adapter.AlbumAdapter;
import app.dinhcuong.diplay.Adapter.MoodAdapter;
import app.dinhcuong.diplay.Adapter.PlaylistAdapter;
import app.dinhcuong.diplay.Model.Album;
import app.dinhcuong.diplay.Model.Playlist;
import app.dinhcuong.diplay.R;
import app.dinhcuong.diplay.Service.APIService;
import app.dinhcuong.diplay.Service.DataService;
import jp.wasabeef.picasso.transformations.BlurTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewAllMoodItemsFragment extends Fragment {
    View view;
    CollapsingToolbarLayout collapsingToolbarLayout;
    CoordinatorLayout coordinatorLayout;
    RecyclerView recyclerViewAlbums, recyclerViewPlaylists;
    TextView noDataAlbum, noDataPlaylist;
    Toolbar toolbar;
    PlaylistAdapter playlistAdapter;
    AlbumAdapter albumAdapter;
    String id_mood, name_mood, image_mood;

    ImageView imageBackground;

    LottieAnimationView lottieAnimationView, lottieAnimationView2;

    MoodAdapter moodAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_view_all_mood_or_genre_items, container, false);

        mapping();


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id_mood = bundle.getString("id_mood");
            name_mood = bundle.getString("name_mood");
            image_mood = bundle.getString("image_mood");
        }

        init();
        getData(id_mood);

        return view;
    }

    private void init() {


        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Set icon for toolbar
        final Drawable backArrow = getResources().getDrawable(R.drawable.ic_fluent_chevron_left_28_filled);
        backArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(backArrow);

        //Set onclick for backButton
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });

        //Set title for Toolbar
        collapsingToolbarLayout.setTitle(getResources().getString(R.string.MOODS) + " | " + name_mood);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBarBig);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        Picasso.get().load(image_mood).transform(new BlurTransformation(getContext(), 10, 1)).into(imageBackground);
    }

    private void mapping() {
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar_layout_VAI);
        coordinatorLayout = view.findViewById(R.id.coordinator_layout_VAI);
        recyclerViewAlbums = view.findViewById(R.id.recycler_view_albums);
        recyclerViewPlaylists = view.findViewById(R.id.recycler_view_playlists);
        toolbar = view.findViewById(R.id.toolbar_VAI);
        lottieAnimationView = view.findViewById(R.id.animation_loading);
        lottieAnimationView2 = view.findViewById(R.id.animation_loading_2);
        noDataAlbum = view.findViewById(R.id.text_nodata_album);
        noDataPlaylist = view.findViewById(R.id.text_nodata_playlist);
        imageBackground = view.findViewById(R.id.image_collapsing_toolbar_VAI);
    }


    private void getData(String id_mood) {


        DataService dataService = APIService.getService();

        Call<List<Album>> callbackAlbum = dataService.handlerGetDataAlbumsOfMood(id_mood, "album");
        callbackAlbum.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                ArrayList<Album> albumArrayList = (ArrayList<Album>) response.body();
                if (albumArrayList.size() > 0){
                    albumAdapter = new AlbumAdapter(getActivity(), albumArrayList, "LIBRARY");
                    lottieAnimationView.setVisibility(View.GONE);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerViewAlbums.setLayoutManager(linearLayoutManager);
                    recyclerViewAlbums.setAdapter(albumAdapter);
                } else {
                    noDataAlbum.setVisibility(View.VISIBLE);
                    lottieAnimationView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {

            }
        });


        Call<List<Playlist>> callbackPlaylist = dataService.handlerGetDataPlaylistsOfMood(id_mood, "playlist");
        callbackPlaylist.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                ArrayList<Playlist> playlistArrayList = (ArrayList<Playlist>) response.body();
                if (playlistArrayList.size() > 0 ){
                    playlistAdapter = new PlaylistAdapter(getActivity(), playlistArrayList, "LIBRARY");
                    lottieAnimationView2.setVisibility(View.GONE);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerViewPlaylists.setLayoutManager(linearLayoutManager);
                    recyclerViewPlaylists.setAdapter(playlistAdapter);
                } else {
                    noDataPlaylist.setVisibility(View.VISIBLE);
                    lottieAnimationView2.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {

            }
        });
    }
}