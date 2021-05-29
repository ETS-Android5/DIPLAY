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
import app.dinhcuong.diplay.Adapter.SingerAdapter;
import app.dinhcuong.diplay.Model.Album;
import app.dinhcuong.diplay.Model.Singer;
import app.dinhcuong.diplay.R;
import app.dinhcuong.diplay.Service.APIService;
import app.dinhcuong.diplay.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LibraryArtistsFragment extends Fragment {
    View view;
    SingerAdapter singerAdapter;
    RecyclerView recyclerViewFollowed;
    LottieAnimationView lottieAnimationView;
    TextView noDataText;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_library_artists, container, false);
        mapping();
        getDataFollowed();
        return view;
    }

    private void mapping() {
        recyclerViewFollowed = view.findViewById(R.id.recycler_view_library_singers_followed);
        lottieAnimationView = view.findViewById(R.id.animation_loading);
        noDataText = view.findViewById(R.id.text_nodata_item);
    }

    private void getDataFollowed() {
        DataService dataService = APIService.getService();

        SharedPreferences pref = getActivity().getSharedPreferences("Auth", getActivity().MODE_PRIVATE);
        String id_user_SP = pref.getString("id_user","0");

        Call<List<Singer>> callback = dataService.getDataLibrarySinger(id_user_SP);
        callback.enqueue(new Callback<List<Singer>>() {
            @Override
            public void onResponse(Call<List<Singer>> call, Response<List<Singer>> response) {
                ArrayList<Singer> singerArrayList = (ArrayList<Singer>) response.body();
                if(singerArrayList.size() > 0){
                    singerAdapter = new SingerAdapter(getActivity(), singerArrayList);
                    lottieAnimationView.setVisibility(View.GONE);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    recyclerViewFollowed.setLayoutManager(linearLayoutManager);
                    recyclerViewFollowed.setAdapter(singerAdapter);
                } else {
                    lottieAnimationView.setVisibility(View.GONE);
                    noDataText.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<List<Singer>> call, Throwable t) {

            }
        });
    }
}