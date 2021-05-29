package app.dinhcuong.diplay.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;

import app.dinhcuong.diplay.Adapter.AlbumAdapter;
import app.dinhcuong.diplay.Adapter.AlbumAdapter;
import app.dinhcuong.diplay.Model.Album;
import app.dinhcuong.diplay.R;
import app.dinhcuong.diplay.Service.APIService;
import app.dinhcuong.diplay.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LibraryAlbumsFragment extends Fragment {
    View view;
    AlbumAdapter albumAdapter;
    RecyclerView recyclerViewFollowed;
    LottieAnimationView lottieAnimationView;
    TextView noDataText;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_library_albums, container, false);
        mapping();
        getDataFollowed();

        return view;
    }

    private void mapping() {
        recyclerViewFollowed = view.findViewById(R.id.recycler_view_library_albums_followed);
        lottieAnimationView = view.findViewById(R.id.animation_loading);
        noDataText = view.findViewById(R.id.text_nodata_item);
    }

    private void getDataFollowed() {
        DataService dataService = APIService.getService();

        SharedPreferences pref = getActivity().getSharedPreferences("Auth", getActivity().MODE_PRIVATE);
        String id_user_SP = pref.getString("id_user","0");

        Call<List<Album>> callback = dataService.getDataLibraryAlbum(id_user_SP);
        callback.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                ArrayList<Album> albumArrayList = (ArrayList<Album>) response.body();
                if(albumArrayList.size() > 0){
                    albumAdapter = new AlbumAdapter(getActivity(), albumArrayList, "LIBRARY");
                    lottieAnimationView.setVisibility(View.GONE);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerViewFollowed.setLayoutManager(linearLayoutManager);
                    recyclerViewFollowed.setAdapter(albumAdapter);
                } else {
                    lottieAnimationView.setVisibility(View.GONE);
                    noDataText.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {

            }
        });
    }
}