package app.dinhcuong.diplay.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.dinhcuong.diplay.Model.Mood;
import app.dinhcuong.diplay.R;
import app.dinhcuong.diplay.Fragment.ViewAllMoodItemsFragment;

public class MoodAdapter extends RecyclerView.Adapter<MoodAdapter.ViewHolder> {
    Context context;
    ArrayList<Mood> moodArrayList;

    public MoodAdapter(Context context, ArrayList<Mood> moodArrayList) {
        this.context = context;
        this.moodArrayList = moodArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_rounded_rectangle, parent, false);



        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mood mood = moodArrayList.get(position);
        holder.nameMood.setText(mood.getNameMood());
        Picasso.get().load(mood.getImageMood()).into(holder.imageMood);
    }

    @Override
    public int getItemCount() {
        return moodArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageMood;
        TextView nameMood;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageMood = itemView.findViewById(R.id.image_item);
            nameMood = itemView.findViewById(R.id.name_item);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    Bundle bundle = new Bundle();
                    ViewAllMoodItemsFragment viewAllMoodItemsFragment = new ViewAllMoodItemsFragment();
                    bundle.putString("id_mood", moodArrayList.get(getAdapterPosition()).getIdMood());
                    bundle.putString("name_mood", moodArrayList.get(getAdapterPosition()).getNameMood());
                    bundle.putString("image_mood", moodArrayList.get(getAdapterPosition()).getImageMood());
                    viewAllMoodItemsFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, viewAllMoodItemsFragment).addToBackStack(null).commit();
                }
            });
        }
    }
}
