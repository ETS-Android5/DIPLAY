package app.dinhcuong.diplay.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.dinhcuong.diplay.Fragment.DetailHomePlaylistItemFragment;
import app.dinhcuong.diplay.Model.Playlist;
import app.dinhcuong.diplay.R;
import app.dinhcuong.diplay.Service.APIService;
import app.dinhcuong.diplay.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Integer.parseInt;

public class PlaylistSelectAdapter extends RecyclerView.Adapter<PlaylistSelectAdapter.ViewHolder> {
    Context context;
    ArrayList<Playlist> playlistArrayList;
    String selection;
    String id_song;

    public PlaylistSelectAdapter(Context context, ArrayList<Playlist> playlistArrayList, String id_song) {
        this.context = context;
        this.playlistArrayList = playlistArrayList;
        this.id_song = id_song;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(context).inflate(R.layout.item_select_playlist_rounded_square_full, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist playlist = playlistArrayList.get(position);
        holder.namePlaylist.setText(playlist.getNamePlaylist());
        Picasso.get().load(playlist.getImagePlaylist()).into(holder.imagePlaylist);
        String numbersOfSongsText = context.getResources().getQuantityString(R.plurals.numberOfSongs, playlist.getSizePlaylist(), playlist.getSizePlaylist());
        holder.numbersOfPlaylist.setText(numbersOfSongsText);

        String followsOfPlaylistText = context.getResources().getQuantityString(R.plurals.numberOfFollows, parseInt(playlist.getFollowsPlaylist()), parseInt(playlist.getFollowsPlaylist()));
        holder.followers.setText(followsOfPlaylistText);
        setButtonAdd(playlistArrayList.get(position).getIdPlaylist(), id_song, holder.addButton, holder.animation_loading);

    }

    @Override
    public int getItemCount() {
        return playlistArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imagePlaylist;
        TextView namePlaylist , followers, numbersOfPlaylist;
        ImageButton addButton;
        LottieAnimationView animation_loading;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePlaylist = itemView.findViewById(R.id.image_item);
            namePlaylist = itemView.findViewById(R.id.name_item);
            followers = itemView.findViewById(R.id.follows_item);
            numbersOfPlaylist = itemView.findViewById(R.id.numbersOf_item);
            addButton = itemView.findViewById(R.id.addButton);
            animation_loading = itemView.findViewById(R.id.animation_loading);

        }
    }

    private void setButtonAdd(String id_playlist, String id_song, ImageButton addButton, LottieAnimationView animation_loading) {

        DataService dataService = APIService.getService();
        SharedPreferences pref = context.getSharedPreferences("Auth", context.MODE_PRIVATE);
        String id_user_SP = pref.getString("id_user","0");

        Call<String> callback = dataService.handlerAddForPlaylistCreated(id_user_SP, id_playlist, id_song, "checkAdded");
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                Log.e("ABC" + result, "CDF");
                if (result.equals("ADDED")){
                    addButton.setImageResource(R.drawable.ic_fluent_checkmark_circle_24_filled);

                    addButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(context, "The song has been added to this playlist!", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else  {

                    addButton.setImageResource(R.drawable.ic_fluent_add_circle_24_regular);

                    addButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            animation_loading.setVisibility(View.VISIBLE);
                            addButton.setVisibility(View.GONE);
                            DataService dataService = APIService.getService();
                            SharedPreferences pref = context.getSharedPreferences("Auth", context.MODE_PRIVATE);
                            String id_user_SP = pref.getString("id_user","0");

                            Call<String> callback = dataService.handlerAddForPlaylistCreated(id_user_SP, id_playlist, id_song, "add");
                            callback.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    String result = response.body();
                                    if (result.equals("SUCCESS")){
                                        addButton.setImageResource(R.drawable.ic_fluent_checkmark_circle_24_filled);
                                        animation_loading.setVisibility(View.GONE);
                                        addButton.setVisibility(View.VISIBLE);
                                        Toast.makeText(context, "Add song to playlist successfully!", Toast.LENGTH_SHORT).show();
                                        addButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Toast.makeText(context, "The song has been added to this playlist!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else  {
                                        animation_loading.setVisibility(View.GONE);
                                        addButton.setVisibility(View.VISIBLE);
                                        Toast.makeText(context, "Can not add this song to playlist", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {

                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

}
