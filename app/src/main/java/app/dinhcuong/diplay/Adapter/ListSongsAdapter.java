package app.dinhcuong.diplay.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.text.format.DateUtils;
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
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import app.dinhcuong.diplay.Fragment.DetailPlaylistCreatedItemFragment;
import app.dinhcuong.diplay.Fragment.PlaybackFragment;
import app.dinhcuong.diplay.Model.Song;
import app.dinhcuong.diplay.R;
import app.dinhcuong.diplay.Service.APIService;
import app.dinhcuong.diplay.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.PendingIntent.getActivity;

public class ListSongsAdapter extends RecyclerView.Adapter<ListSongsAdapter.ViewHolder>{
    Context context;
    ArrayList<Song> songArrayList;
    String selection;
    String id_playlist;




    public ListSongsAdapter(FragmentActivity context, ArrayList<Song> songArrayList) {
        this.context = context;
        this.songArrayList = songArrayList;
    }

    public ListSongsAdapter(FragmentActivity context, ArrayList<Song> songArrayList, String selection) {
        this.context = context;
        this.songArrayList = songArrayList;
        this.selection = selection;
    }

    public ListSongsAdapter(FragmentActivity context, ArrayList<Song> songArrayList, String selection, String id_playlist) {
        this.context = context;
        this.songArrayList = songArrayList;
        this.selection = selection;
        this.id_playlist = id_playlist;
    }


    //Set Interface (Use send data from here to MainActivity -> to PlaybackFragment
    private ISendDataListener iSendDataListener;
    public interface ISendDataListener {
        //Function send data to playbackFragment
        void updateSizePlaylist(int sizePlaylist);
        void sendDataForPlayback(Song songArrayListSend,boolean MODE_OFFLINE, String name_playlist, String source);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view;
        if (selection== "CREATED") {
            view = inflater.inflate(R.layout.item_song_circle_small_left_with_number_full_for_playlist_created, parent,false);
        } else {
            view = inflater.inflate(R.layout.item_song_circle_small_left_with_number_full, parent,false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = songArrayList.get(position);
        holder.name_song.setText(song.getNameSong());
        holder.name_singer.setText(song.getNameSinger());
        holder.index_song.setText(position + 1 + "");
        Picasso.get().load(song.getImageSong()).into(holder.image_song);
        getDurationSong(song.getLinkSong(), holder.duration_song, holder.lottieAnimationView);

        if(holder.deleteButton != null){
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataService dataService = APIService.getService();
                    SharedPreferences pref = context.getSharedPreferences("Auth", context.MODE_PRIVATE);
                    String id_user_SP = pref.getString("id_user","0");

                    Call<String> callback = dataService.handlerAddForPlaylistCreated(id_user_SP, id_playlist, songArrayList.get(position).getIdSong(), "delete");
                    callback.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String result = response.body();
                            if (result.equals("SUCCESS")){

                                songArrayList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, songArrayList.size());
                                notifyDataSetChanged();

                                //Declare Interface
                                iSendDataListener = (ISendDataListener) context;
                                //Use Interface to send data to MainActivity
                                iSendDataListener.updateSizePlaylist(songArrayList.size());

                                Toast.makeText(context, "Remove song from playlist successfully!", Toast.LENGTH_SHORT).show();

                            } else  {
                                Toast.makeText(context, "Remove song from playlist failed!", Toast.LENGTH_SHORT).show();
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

    //Not use
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

    private void getDurationSong(String linkSong, TextView duration_song, LottieAnimationView lottieAnimationView){
        String url = linkSong; // your URL here
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url);

            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {

                    int length = mediaPlayer.getDuration(); // duration in time in millis

                    String durationText = DateUtils.formatElapsedTime(length / 1000); // converting time in millis to minutes:second format eg 14:15 min

                    lottieAnimationView.setVisibility(View.GONE);
                    duration_song.setText(durationText);

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return songArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView index_song, name_song, name_singer, duration_song;
        ImageView image_song;
        LottieAnimationView lottieAnimationView;
        ImageButton deleteButton;

        //ImageButton heartButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name_song = itemView.findViewById(R.id.name_song);
            name_singer = itemView.findViewById(R.id.name_singer);
            image_song = itemView.findViewById(R.id.image_song);
            index_song = itemView.findViewById(R.id.index_song);
            //heartButton = itemView.findViewById(R.id.heartButton);
            duration_song = itemView.findViewById(R.id.duration_song);
            lottieAnimationView = itemView.findViewById(R.id.animation_loading);

            deleteButton = itemView.findViewById(R.id.deleteButton);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();

                    /*
                    PlaybackFragment playbackFragment = new PlaybackFragment();

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("song", songArrayList.get(getAdapterPosition()));
                    bundle.putString("key", "single");
                    playbackFragment.setArguments(bundle);
                    */

                    //Declare Interface
                    iSendDataListener = (ISendDataListener) activity;
                    //Use Interface to send data to MainActivity
                    iSendDataListener.sendDataForPlayback(songArrayList.get(getAdapterPosition()), false, "", "");

                    //activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_test, playbackFragment).addToBackStack(null).commit();
                }
            });

            /*
            heartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DataService dataService = APIService.getService();
                    SharedPreferences pref = context.getSharedPreferences("Auth", context.MODE_PRIVATE);
                    String id_user_SP = pref.getString("id_user","0");

                    Call<String> callback = dataService.handlerLikeForSong(id_user_SP, songArrayList.get(getPosition()).getIdSong(), "0", "checkLiked");
                    callback.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String result = response.body();
                            if (result.equals("LIKED")){
                                DataService dataService = APIService.getService();
                                SharedPreferences pref = context.getSharedPreferences("Auth", context.MODE_PRIVATE);
                                String id_user_SP = pref.getString("id_user","0");

                                Call<String> callback = dataService.handlerLikeForSong(id_user_SP, songArrayList.get(getPosition()).getIdSong(), "1", "unlike");
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

                                Call<String> callback = dataService.handlerLikeForSong(id_user_SP, songArrayList.get(getPosition()).getIdSong(), "1", "like");
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
            */
        }
    }
}
