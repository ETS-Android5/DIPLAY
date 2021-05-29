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

import app.dinhcuong.diplay.Adapter.ListSongsAdapter;
import app.dinhcuong.diplay.Model.Album;
import app.dinhcuong.diplay.Model.Song;
import app.dinhcuong.diplay.R;
import app.dinhcuong.diplay.Service.APIService;
import app.dinhcuong.diplay.Service.DataService;
import jp.wasabeef.picasso.transformations.BlurTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Integer.parseInt;


public class DetailHomeAlbumItemFragment extends Fragment {

    CollapsingToolbarLayout collapsingToolbarLayout;
    CoordinatorLayout coordinatorLayout;
    RecyclerView recyclerView;
    Toolbar toolbar;
    ExtendedFloatingActionButton extendedFloatingActionButton;
    View view;
    ImageView imageViewBackground, imageViewAlbum;
    Album album;
    ArrayList<Song> songArrayList;
    ListSongsAdapter listSongsAdapter;

    TextView followButton, nameAlbum, numbersOfFollows, numbersOfSongs, noDataText;

    LottieAnimationView lottieAnimationView;


    TextView name_album;

    int followsOfAlbum;

    private ISendDataListenerArray iSendDataListenerArray;
    public interface ISendDataListenerArray {
        //Function send data to playbackFragment
        void sendDataForPlaybackArray(ArrayList<Song> songArrayListSend, Boolean MODE_OFFLINE, String name_album, String source);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_detail_home_item, container, false);

        mapping();
        getData();
        init();

        if (album != null && !album.getNameAlbum().equals("")){
            setValueInView(R.string.VIEW_IN_ALBUM, album.getNameAlbum(), album.getFollowsAlbum(), album.getSizeAlbum(), album.getImageAlbum());
            getDataAlbum(album.getIdAlbum());
            eventClick();
        }
        return view;
    }

    private void mapping() {
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar_layout_item);
        coordinatorLayout = view.findViewById(R.id.coordinator_layout_item);
        recyclerView = view.findViewById(R.id.recycler_item);
        toolbar = view.findViewById(R.id.toolbar_item);
        extendedFloatingActionButton = view.findViewById(R.id.item_floating_action_button);
        imageViewBackground = view.findViewById(R.id.image_item_background);
        imageViewAlbum = view.findViewById(R.id.image_item);
        followButton = view.findViewById(R.id.button_follow);
        nameAlbum = view.findViewById(R.id.nameItem);
        numbersOfFollows = view.findViewById(R.id.followsOfItem);
        numbersOfSongs = view.findViewById(R.id.numbersOfItem);
        lottieAnimationView = view.findViewById(R.id.animation_loading);
        noDataText = view.findViewById(R.id.text_nodata_item);
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
        extendedFloatingActionButton.setEnabled(false);

        followsOfAlbum = parseInt(album.getFollowsAlbum());
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataService dataService = APIService.getService();
                SharedPreferences pref = getActivity().getSharedPreferences("Auth", getActivity().MODE_PRIVATE);
                String id_user_SP = pref.getString("id_user","0");



                Call<String> callback = dataService.handlerFollowForAlbum(id_user_SP, album.getIdAlbum(), "0", "checkFollowing");
                callback.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String result = response.body();
                        if (result.equals("FOLLOWING")){
                            DataService dataService = APIService.getService();
                            SharedPreferences pref = getActivity().getSharedPreferences("Auth", getActivity().MODE_PRIVATE);
                            String id_user_SP = pref.getString("id_user","0");

                            Call<String> callback = dataService.handlerFollowForAlbum(id_user_SP, album.getIdAlbum(), "1", "unfollow");
                            callback.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    String result = response.body();
                                    if (result.equals("SUCCESS")){
                                        followButton.setText(R.string.FOLLOW);
                                        followsOfAlbum = followsOfAlbum-1;
                                        String followsOfAlbumText = getResources().getQuantityString(R.plurals.numberOfFollows, followsOfAlbum, followsOfAlbum);
                                        numbersOfFollows.setText(followsOfAlbumText);
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

                            Call<String> callback = dataService.handlerFollowForAlbum(id_user_SP, album.getIdAlbum(), "1", "follow");
                            callback.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    String result = response.body();
                                    if (result.equals("SUCCESS")){
                                        followButton.setText(R.string.FOLLOWING);

                                        followsOfAlbum = followsOfAlbum+1;
                                        String followsOfAlbumText = getResources().getQuantityString(R.plurals.numberOfFollows, followsOfAlbum, followsOfAlbum);
                                        numbersOfFollows.setText(followsOfAlbumText);

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

    private void getDataAlbum(String id_album) {
        DataService dataService = APIService.getService();
        Call<List<Song>> callback = dataService.getSongAlbum(id_album);
        callback.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                songArrayList = (ArrayList<Song>) response.body();
                if (songArrayList.size()> 0){
                    listSongsAdapter = new ListSongsAdapter(getActivity(), songArrayList);
                    lottieAnimationView.setVisibility(View.GONE);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(listSongsAdapter);
                } else {
                    noDataText.setVisibility(View.VISIBLE);
                    lottieAnimationView.setVisibility(View.GONE);
                }


            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {

            }
        });


    }

    private void setValueInView(int album,String nameAlbumInput, String followsAlbum, int sizeAlbum,  String image) {
        setButtonFollow();
        String title = getResources().getString(album);
        nameAlbum.setText(nameAlbumInput);

        String numbersOfSongsText = getResources().getQuantityString(R.plurals.numberOfSongs, sizeAlbum, sizeAlbum);
        numbersOfSongs.setText(numbersOfSongsText);

        String followsOfAlbumText = getResources().getQuantityString(R.plurals.numberOfFollows, parseInt(followsAlbum), parseInt(followsAlbum));
        numbersOfFollows.setText(followsOfAlbumText);

        collapsingToolbarLayout.setTitle(title);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        Picasso.get().load(image).transform(new BlurTransformation(getContext(), 15, 1)).into(imageViewBackground);
        Picasso.get().load(image).into(imageViewAlbum);

    }

    private void setButtonFollow() {

        DataService dataService = APIService.getService();
        SharedPreferences pref = getActivity().getSharedPreferences("Auth", getActivity().MODE_PRIVATE);
        String id_user_SP = pref.getString("id_user","0");

        Call<String> callback = dataService.handlerFollowForAlbum(id_user_SP, album.getIdAlbum(), "0", "checkFollowing");
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
            album = (Album) bundle.getSerializable("album");
            Log.e("TEST1234", album.getNameAlbum());
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
                iSendDataListenerArray.sendDataForPlaybackArray(songArrayList, false, album.getNameAlbum(), getActivity().getResources().getString(R.string.ALBUM));

                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.bottomPlaybackFragment, playbackFragment).addToBackStack(null).commit();
            }
        });
    }

}