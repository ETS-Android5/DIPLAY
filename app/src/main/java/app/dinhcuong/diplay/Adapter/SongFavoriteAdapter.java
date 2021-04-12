package app.dinhcuong.diplay.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.dinhcuong.diplay.Model.Song;
import app.dinhcuong.diplay.PlaybackFragment;
import app.dinhcuong.diplay.R;
import app.dinhcuong.diplay.Service.APIService;
import app.dinhcuong.diplay.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongFavoriteAdapter extends RecyclerView.Adapter<SongFavoriteAdapter.ViewHolder> {
    Context context;
    ArrayList<Song> songFavoriteArrayList;

    public SongFavoriteAdapter(Context context, ArrayList<Song> songFavoriteArrayList) {
        this.context = context;
        this.songFavoriteArrayList = songFavoriteArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_song_favorite, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song songFavorite = songFavoriteArrayList.get(position);
        holder.nameSongFavorite.setText(songFavorite.getNameSong());
        holder.nameSinger.setText(songFavorite.getNameSinger());
        Picasso.get().load(songFavorite.getImageSong()).into(holder.imageSongFavorite);
    }

    @Override
    public int getItemCount() {
        return songFavoriteArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageSongFavorite;
        TextView nameSongFavorite;
        TextView nameSinger;
        ImageButton imageButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageSongFavorite = itemView.findViewById(R.id.image_song_favorite);
            nameSongFavorite = itemView.findViewById(R.id.name_song_favorite);
            nameSinger = itemView.findViewById(R.id.name_singer);
            imageButton = itemView.findViewById(R.id.heartButton);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataService dataService = APIService.getService();
                    Call<String> callback =dataService.handlerLikeForSong(songFavoriteArrayList.get(getPosition()).getIdSong(), "1", "like");
                    callback.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String result = response.body();
                            if (result.equals("SUCCESS")){
                                imageButton.setBackgroundResource(R.drawable.ic_fluent_heart_24_filled);
                                Toast.makeText(context, "Liked", Toast.LENGTH_SHORT).show();

                            } else  {
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    PlaybackFragment playbackFragment = new PlaybackFragment();

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("song", songFavoriteArrayList.get(getAdapterPosition()));
                    playbackFragment.setArguments(bundle);


                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.bottomPlaybackFragment, playbackFragment).addToBackStack(null).commit();
                }
            });


        }
    }
}
