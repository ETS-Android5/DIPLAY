package app.dinhcuong.diplay.Adapter;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import app.dinhcuong.diplay.Model.Song;
import app.dinhcuong.diplay.R;

public class PlaybackAdapter extends RecyclerView.Adapter<PlaybackAdapter.ViewHolder> {

    Context context;
    ArrayList<Song> songArrayList;

    public PlaybackAdapter(Context context, ArrayList<Song> songArrayList) {
        this.context = context;
        this.songArrayList = songArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_song_circle_small_left_with_number_nofull, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = songArrayList.get(position);
        holder.index_song.setText(position + 1 + "");

            if (song.getLinkSong() != null){
                holder.name_song.setText(song.getNameSong());
                holder.name_singer.setText(song.getNameSinger());
                Picasso.get().load(song.getImageSong()).into(holder.image_song);
                getDurationSong(song.getLinkSong(), holder.duration_song);
            } else {
                byte[] image = getAlbumArt(song.getPath());
                if (image != null) {
                    Glide.with(context).asBitmap()
                            .load(image)
                            .into(holder.image_song);
                } else {
                    Glide.with(context)
                            .load(R.drawable.homnaytoibuon)
                            .into(holder.image_song);
                }
                holder.name_song.setText(song.getTitle());
                holder.name_singer.setText(song.getArtist());
                getDurationSongOffline(song.getDuration(), holder.duration_song);
            }



    }
    public byte[] getAlbumArt (String uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }

    private void getDurationSong(String linkSong, TextView duration_song){
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

                    duration_song.setText(durationText);

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getDurationSongOffline(String duration, TextView duration_song){
        int durationTotal = Integer.parseInt(duration)/1000;
        String durationText = DateUtils.formatElapsedTime(durationTotal);
        duration_song.setText(durationText);
    }

    @Override
    public int getItemCount() {
        return songArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name_song, name_singer, index_song, duration_song;
        ImageView image_song;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name_singer = itemView.findViewById(R.id.name_singer);
            name_song = itemView.findViewById(R.id.name_song);
            index_song = itemView.findViewById(R.id.index_song);
            image_song = itemView.findViewById(R.id.image_song);
            duration_song = itemView.findViewById(R.id.duration_song);
        }
    }
}
