package app.dinhcuong.diplay.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.dinhcuong.diplay.Model.Slider;
import app.dinhcuong.diplay.R;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.ViewHolder> {
    Context context;
    ArrayList<Slider> sliderArrayList;
    private ViewPager2 viewPager2;

    public SliderAdapter(Context context, ArrayList<Slider> sliderArrayList, ViewPager2 viewPager2) {
        this.context = context;
        this.sliderArrayList = sliderArrayList;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_rounded_rectangle_big, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Slider slider = sliderArrayList.get(position);
        Picasso.get().load(slider.getImageSlider()).into(holder.imageSlider);
        if (position == sliderArrayList.size()-2){
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return sliderArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageSlider;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageSlider = itemView.findViewById(R.id.image_slider);

        }
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sliderArrayList.addAll(sliderArrayList);
            notifyDataSetChanged();
        }
    };
}
