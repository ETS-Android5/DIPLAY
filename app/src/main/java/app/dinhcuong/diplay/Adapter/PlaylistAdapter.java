package app.dinhcuong.diplay.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.dinhcuong.diplay.Model.Playlist;
import app.dinhcuong.diplay.R;
import app.dinhcuong.diplay.ViewFullPlaylistFragment;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {
    Context context;
    ArrayList<Playlist> playlistArrayList;

    public PlaylistAdapter(Context context, ArrayList<Playlist> playlistArrayList) {
        this.context = context;
        this.playlistArrayList = playlistArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_playlist, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist playlist = playlistArrayList.get(position);
        holder.namePlaylist.setText(playlist.getNamePlaylist());
        Picasso.get().load(playlist.getImagePlaylist()).into(holder.imagePlaylist);
    }

    @Override
    public int getItemCount() {
        return playlistArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imagePlaylist;
        TextView namePlaylist;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePlaylist = itemView.findViewById(R.id.image_playlist);
            namePlaylist = itemView.findViewById(R.id.name_playlist);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    ViewFullPlaylistFragment viewFullPlaylistFragment = new ViewFullPlaylistFragment();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("playlist", playlistArrayList.get(getAdapterPosition()));
                    viewFullPlaylistFragment.setArguments(bundle);


                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, viewFullPlaylistFragment).addToBackStack(null).commit();
                }
            });
        }
    }
}
