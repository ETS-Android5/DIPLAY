package app.dinhcuong.diplay;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import app.dinhcuong.diplay.Adapter.SliderAdapter;
import app.dinhcuong.diplay.Model.Slider;
import app.dinhcuong.diplay.Service.APIService;
import app.dinhcuong.diplay.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeSliderFragment extends Fragment {
    View view;
    ViewPager2 viewPager2;
    SliderAdapter sliderAdapter;

    private Handler sliderHandler = new Handler();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_slider, container, false);

        viewPager2 = view.findViewById(R.id.viewPagerSlider);

        getData();
        return view;
    }

    private void getData() {
        DataService dataService = APIService.getService();
        Call<List<Slider>> callback = dataService.getDataSlider();
        callback.enqueue(new Callback<List<Slider>>() {
            @Override
            public void onResponse(Call<List<Slider>> call, Response<List<Slider>> response) {
                ArrayList<Slider> sliderArrayList = (ArrayList<Slider>) response.body();
                sliderAdapter = new SliderAdapter(getActivity(), sliderArrayList, viewPager2);
                viewPager2.setAdapter(sliderAdapter);
                viewPager2.setClipToPadding(false);
                viewPager2.setClipChildren(false);
                viewPager2.setOffscreenPageLimit(3);
                viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

                CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
                compositePageTransformer.addTransformer(new MarginPageTransformer(40));
                compositePageTransformer.addTransformer((page, position) -> {
                    float r = 1 - Math.abs(position);
                    page.setScaleY(0.85f + r * 0.15f);
                });
                viewPager2.setPageTransformer(compositePageTransformer);

                viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        sliderHandler.removeCallbacks(sliderRunable);
                        sliderHandler.postDelayed(sliderRunable, 3000);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Slider>> call, Throwable t) {

            }
        });
    }
    private Runnable sliderRunable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunable, 3000);
    }
}