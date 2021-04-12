package app.dinhcuong.diplay.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.dinhcuong.diplay.Model.Mood;
import app.dinhcuong.diplay.R;

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
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_mood, parent, false);

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
            imageMood = itemView.findViewById(R.id.image_mood);
            nameMood = itemView.findViewById(R.id.name_mood);

        }
    }
}
