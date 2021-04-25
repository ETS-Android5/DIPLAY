package app.dinhcuong.diplay.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.dinhcuong.diplay.Model.Song;
import app.dinhcuong.diplay.R;

import static android.app.PendingIntent.getActivity;

public class ListSongsAdapter extends RecyclerView.Adapter<ListSongsAdapter.ViewHolder>{
    Context context;
    ArrayList<Song> songArrayList;

    public ListSongsAdapter(FragmentActivity context, ArrayList<Song> songArrayList) {
        this.context = context;
        this.songArrayList = songArrayList;
    }


    //Set Interface (Use send data from here to MainActivity -> to PlaybackFragment
    private ISendDataListener iSendDataListener;
    public interface ISendDataListener {
        //Function send data to playbackFragment
        void sendDataForPlayback(Song songArrayListSend,boolean MODE_OFFLINE, String name_playlist, String source);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_song_cirlce_left, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = songArrayList.get(position);
        holder.name_song.setText(song.getNameSong());
        holder.name_singer.setText(song.getNameSinger());
        holder.index_song.setText(position + 1 + "");
        Picasso.get().load(song.getImageSong()).into(holder.image_song);
    }

    @Override
    public int getItemCount() {
        return songArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView index_song, name_song, name_singer;
        ImageView image_song;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name_song = itemView.findViewById(R.id.name_song);
            name_singer = itemView.findViewById(R.id.name_singer);
            image_song = itemView.findViewById(R.id.image_song);
            index_song = itemView.findViewById(R.id.index_song);

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
                    iSendDataListener.sendDataForPlayback(songArrayList.get(getAdapterPosition()), false, "", "PLAYLISTS");

                    //activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_test, playbackFragment).addToBackStack(null).commit();
                }
            });
        }
    }
}
