package app.dinhcuong.diplay.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;

import app.dinhcuong.diplay.Adapter.MoodAdapter;
import app.dinhcuong.diplay.Model.Mood;
import app.dinhcuong.diplay.R;
import app.dinhcuong.diplay.Service.APIService;
import app.dinhcuong.diplay.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeMoodsFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    MoodAdapter moodAdapter;
    LottieAnimationView lottieAnimationView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_moods, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_items);
        lottieAnimationView = view.findViewById(R.id.animation_loading);
        getData();
        return view;
    }

    private void getData() {
        DataService dataService = APIService.getService();
        Call<List<Mood>> callback = dataService.getDataMood();
        callback.enqueue(new Callback<List<Mood>>() {
            @Override
            public void onResponse(Call<List<Mood>> call, Response<List<Mood>> response) {
                ArrayList<Mood> moodArrayList = (ArrayList<Mood>) response.body();
                moodAdapter = new MoodAdapter(getActivity(), moodArrayList);
                lottieAnimationView.setVisibility(View.GONE);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(moodAdapter);
            }

            @Override
            public void onFailure(Call<List<Mood>> call, Throwable t) {

            }
        });
    }
}