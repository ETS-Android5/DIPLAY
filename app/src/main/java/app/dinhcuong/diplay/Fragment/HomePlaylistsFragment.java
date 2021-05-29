package app.dinhcuong.diplay.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

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


public class HomePlaylistsFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    PlaylistAdapter playlistAdapter;
    ImageButton viewMoreButton;
    LottieAnimationView animationView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_playlists, container, false);

        mapping();
        init();
        
        getData();
        return view;
    }



    private void mapping() {
        recyclerView = view.findViewById(R.id.recycler_view_items);
        viewMoreButton = view.findViewById(R.id.viewMoreButton);
        animationView = view.findViewById(R.id.animation_loading);
    }

    private void init() {
        viewMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewAllPlaylistsItemsFragment viewAllPlaylistsFragment = new ViewAllPlaylistsItemsFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, viewAllPlaylistsFragment).addToBackStack(null).commit();
            }
        });
    }

    private void getData() {
        DataService dataService = APIService.getService();
        Call<List<Playlist>> callback = dataService.getDataPlaylist();
        callback.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                ArrayList<Playlist> playlistArrayList = (ArrayList<Playlist>) response.body();
                playlistAdapter = new PlaylistAdapter(getActivity(), playlistArrayList);
                animationView.setVisibility(View.GONE);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(playlistAdapter);
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {

            }
        });
    }
}