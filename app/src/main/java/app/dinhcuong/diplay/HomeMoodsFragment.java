package app.dinhcuong.diplay;

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

import app.dinhcuong.diplay.Adapter.MoodAdapter;
import app.dinhcuong.diplay.Model.Mood;
import app.dinhcuong.diplay.Service.APIService;
import app.dinhcuong.diplay.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeMoodsFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    MoodAdapter moodAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_moods, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_moods);
        
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