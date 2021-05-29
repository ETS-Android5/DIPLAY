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

import app.dinhcuong.diplay.Fragment.ViewAllGenreItemsFragment;
import app.dinhcuong.diplay.Fragment.ViewAllMoodItemsFragment;
import app.dinhcuong.diplay.Model.Genre;
import app.dinhcuong.diplay.R;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {
    Context context;
    ArrayList<Genre> genreArrayList;

    public GenreAdapter(Context context, ArrayList<Genre> genreArrayList) {
        this.context = context;
        this.genreArrayList = genreArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_rounded_rectangle, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Genre genre = genreArrayList.get(position);
        holder.nameGenre.setText(genre.getNameGenre());
        Picasso.get().load(genre.getImageGenre()).into(holder.imageGenre);
    }

    @Override
    public int getItemCount() {
        return genreArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageGenre;
        TextView nameGenre;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageGenre = itemView.findViewById(R.id.image_item);
            nameGenre = itemView.findViewById(R.id.name_item);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    Bundle bundle = new Bundle();
                    ViewAllGenreItemsFragment viewAllGenreItemsFragment = new ViewAllGenreItemsFragment();
                    bundle.putString("id_genre", genreArrayList.get(getAdapterPosition()).getIdGenre());
                    bundle.putString("name_genre", genreArrayList.get(getAdapterPosition()).getNameGenre());
                    bundle.putString("image_genre", genreArrayList.get(getAdapterPosition()).getImageGenre());
                    viewAllGenreItemsFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, viewAllGenreItemsFragment).addToBackStack(null).commit();
                }
            });
        }
    }
}
