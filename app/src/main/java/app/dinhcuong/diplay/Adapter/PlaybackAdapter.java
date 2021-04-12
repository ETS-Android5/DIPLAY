package app.dinhcuong.diplay.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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
        View view = inflater.inflate(R.layout.item_playback_playlist_song, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = songArrayList.get(position);
        holder.name_song.setText(song.getNameSong());
        holder.name_singer.setText(song.getNameSinger());

    }

    @Override
    public int getItemCount() {
        return songArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name_song, name_singer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name_singer = itemView.findViewById(R.id.name_singer);
            name_song = itemView.findViewById(R.id.name_song);

        }
    }
}
