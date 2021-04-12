package app.dinhcuong.diplay.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class Fragment_Home extends Fragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }
}
