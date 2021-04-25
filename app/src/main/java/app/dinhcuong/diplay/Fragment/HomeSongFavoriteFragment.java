package app.dinhcuong.diplay.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.dinhcuong.diplay.Adapter.SongFavoriteAdapter;
import app.dinhcuong.diplay.Model.Song;
import app.dinhcuong.diplay.R;
import app.dinhcuong.diplay.Service.APIService;
import app.dinhcuong.diplay.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeSongFavoriteFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    SongFavoriteAdapter songFavoriteAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_favorite_song, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_items);
        
        getData();
        return view;
    }

    private void getData() {
        DataService dataService = APIService.getService();
        Call<List<Song>> callback = dataService.getSongFavorite();
        callback.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                ArrayList<Song> songFavoriteArrayList = (ArrayList<Song>) response.body();
                songFavoriteAdapter = new SongFavoriteAdapter(getActivity(), songFavoriteArrayList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(songFavoriteAdapter);
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {

            }
        });
    }
}