package app.dinhcuong.diplay.Fragment;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;

import app.dinhcuong.diplay.Adapter.PlaylistAdapter;
import app.dinhcuong.diplay.Model.Playlist;
import app.dinhcuong.diplay.R;
import app.dinhcuong.diplay.Service.APIService;
import app.dinhcuong.diplay.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewAllPlaylistsItemsFragment extends Fragment {
    View view;
    CollapsingToolbarLayout collapsingToolbarLayout;
    CoordinatorLayout coordinatorLayout;
    RecyclerView recyclerView;
    Toolbar toolbar;
    LottieAnimationView animationLoading;

    PlaylistAdapter playlistAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_view_all_items, container, false);

        mapping();
        init();

        getData();

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
        collapsingToolbarLayout.setTitle(getResources().getString(R.string.PLAYLISTS));
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBarBig);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
    }

    private void mapping() {
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar_layout_VAI);
        coordinatorLayout = view.findViewById(R.id.coordinator_layout_VAI);
        recyclerView = view.findViewById(R.id.recycler_view_VAI);
        toolbar = view.findViewById(R.id.toolbar_VAI);
        animationLoading = view.findViewById(R.id.animation_loading);
    }

    private void getData() {
        DataService dataService = APIService.getService();
        Call<List<Playlist>> callback = dataService.getDataPlaylist();
        callback.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {

                ArrayList<Playlist> playlistArrayList = (ArrayList<Playlist>) response.body();
                animationLoading.setVisibility(View.GONE);
                playlistAdapter = new PlaylistAdapter(getActivity(), playlistArrayList, "ALL");
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(playlistAdapter);
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {

            }
        });
    }
}