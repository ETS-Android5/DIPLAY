package app.dinhcuong.diplay.Adapter;

import android.content.Context;
import android.media.MediaMetadata;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import app.dinhcuong.diplay.Model.Song;
import app.dinhcuong.diplay.R;

public class LocalSongAdapter extends RecyclerView.Adapter<LocalSongAdapter.ViewHolder> {
    Context context;
    ArrayList<Song> localSongArrayList;

    //Set Interface (Use send data from here to MainActivity -> to PlaybackFragment
    private ISendDataListener iSendDataListener;
    public interface ISendDataListener {
        //Function send data to playbackFragment
        void sendDataForPlayback(Song songArrayListSend, boolean MODE_OFFLINE, String name_playlist, String source);
    }

    public LocalSongAdapter(Context context, ArrayList<Song> localSongArrayList) {
        this.context = context;
        this.localSongArrayList = localSongArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_library_song_cirlce_left, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameSong.setText(localSongArrayList.get(position).getTitle());
        holder.nameArtist.setText(localSongArrayList.get(position).getArtist());

        int durationTotal = Integer.parseInt(localSongArrayList.get(position).getDuration())/1000;
        holder.durationSong.setText(fromMinutesToHHmm(durationTotal));


        byte[] image = getAlbumArt(localSongArrayList.get(position).getPath());
        if (image != null) {
            Glide.with(context).asBitmap()
                    .load(image)
                    .into(holder.imageSong);
        } else {
            Glide.with(context)
                    .load(R.drawable.homnaytoibuon)
                    .into(holder.imageSong);
        }


    }

    public String fromMinutesToHHmm(int minutes) {
        long hours = TimeUnit.MINUTES.toHours(Long.valueOf(minutes));
        long remainMinutes = minutes - TimeUnit.HOURS.toMinutes(hours);
        return String.format("%02d:%02d", hours, remainMinutes);
    }


    @Override
    public int getItemCount() {
        return localSongArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageSong;
        TextView nameSong, nameArtist, durationSong;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageSong = itemView.findViewById(R.id.image_song);
            nameSong = itemView.findViewById(R.id.name_song);
            nameArtist = itemView.findViewById(R.id.name_artist);
            durationSong = itemView.findViewById(R.id.duration_song);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    iSendDataListener = (ISendDataListener) activity;
                    //Use Interface to send data to MainActivity
                    iSendDataListener.sendDataForPlayback(localSongArrayList.get(getAdapterPosition()), true, "LOCAL STORAGE", "LIBRARY");
                }
            });
        }
    }

    public byte[] getAlbumArt (String uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}
