package app.dinhcuong.diplay.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
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
import app.dinhcuong.diplay.R;
import app.dinhcuong.diplay.Service.APIService;
import app.dinhcuong.diplay.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongFavoriteAdapter extends RecyclerView.Adapter<SongFavoriteAdapter.ViewHolder> {
    Context context;
    ArrayList<Song> songFavoriteArrayList;

    //Set Interface (Use send data from here to MainActivity -> to PlaybackFragment
    private ISendDataListener iSendDataListener;
    public interface ISendDataListener {
        //Function send data to playbackFragment

        void sendDataForPlayback(Song songArrayListSend, boolean MODE_OFFLINE, String name_playlist, String source);
    }

    public SongFavoriteAdapter(Context context, ArrayList<Song> songFavoriteArrayList) {
        this.context = context;
        this.songFavoriteArrayList = songFavoriteArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_song_circle_big_center, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song songFavorite = songFavoriteArrayList.get(position);
        holder.nameSongFavorite.setText(songFavorite.getNameSong());
        holder.nameSinger.setText(songFavorite.getNameSinger());
        Picasso.get().load(songFavorite.getImageSong()).into(holder.imageSongFavorite);

        //Set ImageButton like
        setButtonLike(songFavorite, holder.heartButton);
    }

    private void setButtonLike(Song song, ImageButton heartButton) {

        DataService dataService = APIService.getService();
        SharedPreferences pref = context.getSharedPreferences("Auth", context.MODE_PRIVATE);
        String id_user_SP = pref.getString("id_user","0");

        Call<String> callback = dataService.handlerLikeForSong(id_user_SP, song.getIdSong(), "0", "checkLiked");
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                if (result.equals("LIKED")){
                    heartButton.setBackgroundResource(R.drawable.ic_fluent_heart_24_filled);
                } else  {
                    heartButton.setBackgroundResource(R.drawable.ic_fluent_heart_24_regular);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return songFavoriteArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageSongFavorite;
        TextView nameSongFavorite;
        TextView nameSinger;
        ImageButton heartButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageSongFavorite = itemView.findViewById(R.id.image_song_favorite);
            nameSongFavorite = itemView.findViewById(R.id.name_song_favorite);
            nameSinger = itemView.findViewById(R.id.name_singer);
            heartButton = itemView.findViewById(R.id.heartButton);
            heartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DataService dataService = APIService.getService();
                    SharedPreferences pref = context.getSharedPreferences("Auth", context.MODE_PRIVATE);
                    String id_user_SP = pref.getString("id_user","0");

                    Call<String> callback = dataService.handlerLikeForSong(id_user_SP, songFavoriteArrayList.get(getPosition()).getIdSong(), "0", "checkLiked");
                    callback.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String result = response.body();
                            if (result.equals("LIKED")){
                                DataService dataService = APIService.getService();
                                SharedPreferences pref = context.getSharedPreferences("Auth", context.MODE_PRIVATE);
                                String id_user_SP = pref.getString("id_user","0");

                                Call<String> callback = dataService.handlerLikeForSong(id_user_SP, songFavoriteArrayList.get(getPosition()).getIdSong(), "1", "unlike");
                                callback.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        String result = response.body();
                                        if (result.equals("SUCCESS")){
                                            heartButton.setBackgroundResource(R.drawable.ic_fluent_heart_24_regular);
                                            Toast.makeText(context, "Disliked", Toast.LENGTH_SHORT).show();

                                        } else  {
                                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {

                                    }
                                });
                            } else  {
                                DataService dataService = APIService.getService();
                                SharedPreferences pref = context.getSharedPreferences("Auth", context.MODE_PRIVATE);
                                String id_user_SP = pref.getString("id_user","0");

                                Call<String> callback = dataService.handlerLikeForSong(id_user_SP, songFavoriteArrayList.get(getPosition()).getIdSong(), "1", "like");
                                callback.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        String result = response.body();
                                        if (result.equals("SUCCESS")){
                                            heartButton.setBackgroundResource(R.drawable.ic_fluent_heart_24_filled);
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

                    /*
                    PlaybackFragment playbackFragment = new PlaybackFragment();

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("song", songFavoriteArrayList.get(getAdapterPosition()));
                    playbackFragment.setArguments(bundle);


                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.bottomPlaybackFragment, playbackFragment).addToBackStack(null).commit();

                     */

                    //Declare Interface
                    iSendDataListener = (ISendDataListener) activity;
                    //Use Interface to send data to MainActivity
                    iSendDataListener.sendDataForPlayback(songFavoriteArrayList.get(getAdapterPosition()), false, "HOT", "SONGS");
                }
            });


        }
    }
}
