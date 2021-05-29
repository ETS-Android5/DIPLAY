package app.dinhcuong.diplay.Fragment;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.dinhcuong.diplay.Adapter.AlbumAdapter;
import app.dinhcuong.diplay.Adapter.ListSongsAdapter;
import app.dinhcuong.diplay.Adapter.PlaylistAdapter;
import app.dinhcuong.diplay.Model.Album;
import app.dinhcuong.diplay.Model.Playlist;
import app.dinhcuong.diplay.Model.Singer;
import app.dinhcuong.diplay.Model.Song;
import app.dinhcuong.diplay.R;
import app.dinhcuong.diplay.Service.APIService;
import app.dinhcuong.diplay.Service.DataService;
import jp.wasabeef.picasso.transformations.BlurTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Integer.parseInt;


public class DetailHomeSingerItemFragment extends Fragment {

    CollapsingToolbarLayout collapsingToolbarLayout;
    CoordinatorLayout coordinatorLayout;
    RecyclerView recyclerViewAlbums, recyclerViewSongs;
    Toolbar toolbar;
    View view;
    ImageView imageViewBackground, imageViewSinger;
    Singer singer;
    ArrayList<Song> songArrayList;
    ListSongsAdapter listSongsAdapter;
    ArrayList<Album> albumArrayList;
    AlbumAdapter albumAdapter;

    TextView followButton, nameSinger, numbersOfFollows, numbersOfSongs, numbersOfAlbums, noDataSongText, noDataAlbumText;

    LottieAnimationView lottieAnimationView, lottieAnimationView2;

    TextView name_singer;

    int followsOfSinger;

    private ISendDataListenerArray iSendDataListenerArray;
    public interface ISendDataListenerArray {
        //Function send data to playbackFragment
        void sendDataForPlaybackArray(ArrayList<Song> songArrayListSend, Boolean MODE_OFFLINE, String name_singer, String source);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_detail_home_item_singer, container, false);

        mapping();
        getData();
        init();

        if (singer != null && !singer.getNameSinger().equals("")){
            setValueInView(R.string.VIEW_INFOMATION_OF_SINGER, singer.getNameSinger(), singer.getFollowsSinger(), singer.getSizeSinger(), singer.getImageSinger(), singer.getSizeAlbumSinger());
            getDataAlbumSinger(singer.getIdSinger());
        }
        return view;
    }

    private void mapping() {
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar_layout_item);
        coordinatorLayout = view.findViewById(R.id.coordinator_layout_item);
        recyclerViewAlbums = view.findViewById(R.id.recycler_view_singer_albums);
        recyclerViewSongs = view.findViewById(R.id.recycler_view_singer_songs);
        toolbar = view.findViewById(R.id.toolbar_item);
        imageViewBackground = view.findViewById(R.id.image_item_background);
        imageViewSinger = view.findViewById(R.id.image_item);
        followButton = view.findViewById(R.id.button_follow);
        nameSinger = view.findViewById(R.id.nameItem);
        numbersOfFollows = view.findViewById(R.id.followsOfItem);
        numbersOfSongs = view.findViewById(R.id.numbersOfItem);
        numbersOfAlbums = view.findViewById(R.id.numbersOfItem2);
        lottieAnimationView = view.findViewById(R.id.animation_loading);
        lottieAnimationView2 = view.findViewById(R.id.animation_loading_2);
        noDataSongText = view.findViewById(R.id.text_nodata_song);
        noDataAlbumText = view.findViewById(R.id.text_nodata_album);

    }

    private void init() {
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable backArrow = getResources().getDrawable(R.drawable.ic_fluent_chevron_left_28_filled);
        backArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(backArrow);


        //toolbar.setNavigationIcon(R.drawable.ic_fluent_chevron_left_28_filled);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });

        followsOfSinger = parseInt(singer.getFollowsSinger());

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataService dataService = APIService.getService();
                SharedPreferences pref = getActivity().getSharedPreferences("Auth", getActivity().MODE_PRIVATE);
                String id_user_SP = pref.getString("id_user","0");



                Call<String> callback = dataService.handlerFollowForSinger(id_user_SP, singer.getIdSinger(), "0", "checkFollowing");
                callback.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String result = response.body();
                        if (result.equals("FOLLOWING")){
                            DataService dataService = APIService.getService();
                            SharedPreferences pref = getActivity().getSharedPreferences("Auth", getActivity().MODE_PRIVATE);
                            String id_user_SP = pref.getString("id_user","0");

                            Call<String> callback = dataService.handlerFollowForSinger(id_user_SP, singer.getIdSinger(), "1", "unfollow");
                            callback.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    String result = response.body();
                                    if (result.equals("SUCCESS")){
                                        followButton.setText(R.string.FOLLOW);
                                        followsOfSinger = followsOfSinger-1;
                                        String followsOfSingerText = getResources().getQuantityString(R.plurals.numberOfFollows, followsOfSinger, followsOfSinger);
                                        numbersOfFollows.setText(followsOfSingerText);
                                        Toast.makeText(getActivity(), "Unfollow", Toast.LENGTH_SHORT).show();

                                    } else  {
                                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {

                                }
                            });
                        } else  {
                            DataService dataService = APIService.getService();
                            SharedPreferences pref = getActivity().getSharedPreferences("Auth", getActivity().MODE_PRIVATE);
                            String id_user_SP = pref.getString("id_user","0");

                            Call<String> callback = dataService.handlerFollowForSinger(id_user_SP, singer.getIdSinger(), "1", "follow");
                            callback.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    String result = response.body();
                                    if (result.equals("SUCCESS")){
                                        followButton.setText(R.string.FOLLOWING);

                                        followsOfSinger = followsOfSinger+1;
                                        String followsOfSingerText = getResources().getQuantityString(R.plurals.numberOfFollows, followsOfSinger, followsOfSinger);
                                        numbersOfFollows.setText(followsOfSingerText);

                                        Toast.makeText(getActivity(), "Following", Toast.LENGTH_SHORT).show();

                                    } else  {
                                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });

                //followButton.setText(R.string.FOLLOWING);
            }
        });
    }
    private void getDataAlbumSinger(String id_singer) {
        DataService dataService = APIService.getService();

        Call<List<Album>> callbackAlbum = dataService.handlerGetDataAlbumForSinger(singer.getIdSinger(), "album");
        callbackAlbum.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                ArrayList<Album> albumArrayList = (ArrayList<Album>) response.body();
                if(albumArrayList.size() > 0){
                    albumAdapter = new AlbumAdapter(getActivity(), albumArrayList, "LIBRARY");
                    lottieAnimationView2.setVisibility(View.GONE);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerViewAlbums.setLayoutManager(linearLayoutManager);
                    recyclerViewAlbums.setAdapter(albumAdapter);
                } else {
                    lottieAnimationView2.setVisibility(View.GONE);
                    noDataAlbumText.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {

            }
        });

        Call<List<Song>> callbackSong = dataService.handlerGetDataSongForSinger(singer.getIdSinger(), "song");
        callbackSong.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                ArrayList<Song> songArrayList = (ArrayList<Song>) response.body();
                if (songArrayList.size() > 0){
                    listSongsAdapter = new ListSongsAdapter(getActivity(), songArrayList);
                    lottieAnimationView.setVisibility(View.GONE);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerViewSongs.setLayoutManager(linearLayoutManager);
                    recyclerViewSongs.setAdapter(listSongsAdapter);
                } else {
                    lottieAnimationView.setVisibility(View.GONE);
                    noDataSongText.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {

            }
        });


    }

    private void setValueInView(int singer,String nameSingerInput, String followsSinger, int sizeSinger,  String image, int sizeAlbumSinger) {
        setButtonFollow();
        String title = getResources().getString(singer);
        nameSinger.setText(nameSingerInput);

        String numbersOfSongsText = getResources().getQuantityString(R.plurals.numberOfSongs, sizeSinger, sizeSinger);
        numbersOfSongs.setText(numbersOfSongsText);

        String numbersOfAlbumsText = getResources().getQuantityString(R.plurals.numberOfAlbums, sizeAlbumSinger, sizeAlbumSinger);
        numbersOfAlbums.setText(numbersOfAlbumsText);

        String followsOfSingerText = getResources().getQuantityString(R.plurals.numberOfFollows, parseInt(followsSinger), parseInt(followsSinger));
        numbersOfFollows.setText(followsOfSingerText);

        collapsingToolbarLayout.setTitle(title);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        Picasso.get().load(image).transform(new BlurTransformation(getContext(), 15, 1)).into(imageViewBackground);
        Picasso.get().load(image).into(imageViewSinger);

    }

    private void setButtonFollow() {

        DataService dataService = APIService.getService();
        SharedPreferences pref = getActivity().getSharedPreferences("Auth", getActivity().MODE_PRIVATE);
        String id_user_SP = pref.getString("id_user","0");

        Call<String> callback = dataService.handlerFollowForSinger(id_user_SP, singer.getIdSinger(), "0", "checkFollowing");
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                if (result.equals("FOLLOWING")){
                    followButton.setText(R.string.FOLLOWING);
                } else  {
                    followButton.setText(R.string.FOLLOW);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    private void getData() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            singer = (Singer) bundle.getSerializable("singer");
            Log.e("TEST1234", singer.getNameSinger());
        }
    }

}