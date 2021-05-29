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

import app.dinhcuong.diplay.Adapter.AlbumAdapter;
import app.dinhcuong.diplay.Model.Album;
import app.dinhcuong.diplay.R;
import app.dinhcuong.diplay.Service.APIService;
import app.dinhcuong.diplay.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeAlbumsFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    AlbumAdapter albumAdapter;
    ImageButton viewMoreButton;
    LottieAnimationView animationView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_albums, container, false);


        mapping();
        init();

        getData();
        return view;
    }

    private void init() {
        viewMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewAllAlbumsItemsFragment viewAllAlbumsItemsFragment = new ViewAllAlbumsItemsFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, viewAllAlbumsItemsFragment).addToBackStack(null).commit();
            }
        });
    }

    private void mapping() {
        recyclerView = view.findViewById(R.id.recycler_view_items);
        viewMoreButton = view.findViewById(R.id.buttonViewMore);
        animationView = view.findViewById(R.id.animation_loading);
    }

    private void getData() {
        DataService dataService = APIService.getService();
        Call<List<Album>> callback = dataService.getDataAlbum();
        callback.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                ArrayList<Album> albumArrayList = (ArrayList<Album>) response.body();
                albumAdapter = new AlbumAdapter(getActivity(), albumArrayList);
                animationView.setVisibility(View.GONE);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(albumAdapter);
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {

            }
        });
    }
}