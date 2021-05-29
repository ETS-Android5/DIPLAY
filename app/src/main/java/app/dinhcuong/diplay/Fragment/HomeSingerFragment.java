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

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;

import app.dinhcuong.diplay.Adapter.SingerAdapter;
import app.dinhcuong.diplay.Adapter.SongFavoriteAdapter;
import app.dinhcuong.diplay.Model.Singer;
import app.dinhcuong.diplay.Model.Song;
import app.dinhcuong.diplay.R;
import app.dinhcuong.diplay.Service.APIService;
import app.dinhcuong.diplay.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeSingerFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    SingerAdapter singerAdapter;
    LottieAnimationView animationView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_singers, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_items);

        animationView = view.findViewById(R.id.animation_loading);
        
        getData();
        return view;
    }

    private void getData() {
        DataService dataService = APIService.getService();
        Call<List<Singer>> callback = dataService.getDataSinger();
        callback.enqueue(new Callback<List<Singer>>() {
            @Override
            public void onResponse(Call<List<Singer>> call, Response<List<Singer>> response) {
                ArrayList<Singer> singerArrayList = (ArrayList<Singer>) response.body();
                animationView.setVisibility(View.GONE);
                singerAdapter = new SingerAdapter(getActivity(), singerArrayList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(singerAdapter);
            }

            @Override
            public void onFailure(Call<List<Singer>> call, Throwable t) {

            }
        });
    }
}