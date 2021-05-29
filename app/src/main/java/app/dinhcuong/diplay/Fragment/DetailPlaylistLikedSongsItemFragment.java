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
import app.dinhcuong.diplay.Model.Playlist;
import app.dinhcuong.diplay.Model.Song;
import app.dinhcuong.diplay.R;
import app.dinhcuong.diplay.Service.APIService;
import app.dinhcuong.diplay.Service.DataService;
import jp.wasabeef.picasso.transformations.BlurTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Integer.parseInt;


public class DetailPlaylistLikedSongsItemFragment extends Fragment {

    CollapsingToolbarLayout collapsingToolbarLayout;
    CoordinatorLayout coordinatorLayout;
    RecyclerView recyclerView;
    Toolbar toolbar;
    ExtendedFloatingActionButton extendedFloatingActionButton;
    View view;
    ImageView imageViewBackground, imageViewPlaylist;
    Playlist playlist;
    String sizePlaylist, imagePlaylist, namePlaylistText;
    ArrayList<Song> songArrayList;
    ListSongsAdapter listSongsAdapter;

    TextView followButton, namePlaylist, numbersOfFollows, numbersOfSongs, noDataText;

    LottieAnimationView lottieAnimationView;


    TextView name_playlist;

    int followsOfPlaylist;

    private ISendDataListenerArray iSendDataListenerArray;
    public interface ISendDataListenerArray {
        //Function send data to playbackFragment
        void sendDataForPlaybackArray(ArrayList<Song> songArrayListSend, Boolean MODE_OFFLINE, String name_playlist, String source);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_detail_home_item_lite, container, false);

        mapping();
        init();

        getData();
        getDataPlaylist();
        namePlaylistText = "Liked Songs";
        setValueInView(R.string.VIEW_IN_PLAYLIST, "Liked Songs", parseInt(sizePlaylist), imagePlaylist);

        eventClick(namePlaylistText);

        return view;
    }

    private void mapping() {
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar_layout_item);
        coordinatorLayout = view.findViewById(R.id.coordinator_layout_item);
        recyclerView = view.findViewById(R.id.recycler_item);
        toolbar = view.findViewById(R.id.toolbar_item);
        extendedFloatingActionButton = view.findViewById(R.id.item_floating_action_button);
        imageViewBackground = view.findViewById(R.id.image_item_background);
        imageViewPlaylist = view.findViewById(R.id.image_item);
        followButton = view.findViewById(R.id.button_follow);
        namePlaylist = view.findViewById(R.id.nameItem);
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



    }

    private void getDataPlaylist() {
        DataService dataService = APIService.getService();

        SharedPreferences pref = getActivity().getSharedPreferences("Auth", getActivity().MODE_PRIVATE);
        String id_user_SP = pref.getString("id_user","0");


        Call<List<Song>> callback = dataService.getLikedSongsPlaylist(id_user_SP,"getDataLikedSongs");
        callback.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                songArrayList = (ArrayList<Song>) response.body();
                if(songArrayList.size()> 0){
                    listSongsAdapter = new ListSongsAdapter(getActivity(), songArrayList);
                    lottieAnimationView.setVisibility(View.GONE);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(listSongsAdapter);
                }else {
                    noDataText.setVisibility(View.VISIBLE);
                    lottieAnimationView.setVisibility(View.GONE);
                }


            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {

            }
        });


    }

    public void setValueInView(int playlist,String namePlaylistInput, int sizePlaylist,  String image) {
        //setButtonFollow();
        String title = getResources().getString(playlist);
        namePlaylist.setText(namePlaylistInput);

        String numbersOfSongsText = getResources().getQuantityString(R.plurals.numberOfSongs, sizePlaylist, sizePlaylist);
        numbersOfSongs.setText(numbersOfSongsText);

        collapsingToolbarLayout.setTitle(title);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        Picasso.get().load(image).transform(new BlurTransformation(getContext(), 15, 1)).into(imageViewBackground);
        Picasso.get().load(image).into(imageViewPlaylist);

    }


    private void eventClick(String namePlaylist) {
        extendedFloatingActionButton.setEnabled(true);
        extendedFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Declare Interface
                iSendDataListenerArray = (ISendDataListenerArray) getActivity();
                //Use Interface to send data to MainActivity
                iSendDataListenerArray.sendDataForPlaybackArray(songArrayList, false, namePlaylist, getActivity().getResources().getString(R.string.PLAYLIST));

                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.bottomPlaybackFragment, playbackFragment).addToBackStack(null).commit();
            }
        });
    }

    private void getData() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {

            sizePlaylist = bundle.getString("sizePlaylist");
            imagePlaylist = bundle.getString("imagePlaylist");
            Log.e("TEST1234", sizePlaylist);
        }
    }


}