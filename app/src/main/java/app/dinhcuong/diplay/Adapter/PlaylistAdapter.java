package app.dinhcuong.diplay.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.dinhcuong.diplay.Fragment.DetailHomePlaylistItemFragment;
import app.dinhcuong.diplay.Model.Playlist;
import app.dinhcuong.diplay.R;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {
    Context context;
    ArrayList<Playlist> playlistArrayList;
    String selection;

    public PlaylistAdapter(Context context, ArrayList<Playlist> playlistArrayList, String selection) {
        this.context = context;
        this.playlistArrayList = playlistArrayList;
        this.selection = selection;
    }

    public PlaylistAdapter(Context context, ArrayList<Playlist> playlistArrayList) {
        this.context = context;
        this.playlistArrayList = playlistArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        //If selection == ALL -> use item_view_all_rounded_square
        if (selection == "ALL"){
            view = LayoutInflater.from(context).inflate(R.layout.item_view_all_rounded_square, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_home_rounded_square, parent, false);
        }



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
            imagePlaylist = itemView.findViewById(R.id.image_item);
            namePlaylist = itemView.findViewById(R.id.name_item);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    DetailHomePlaylistItemFragment detailHomePlaylistItemFragment = new DetailHomePlaylistItemFragment();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("playlist", playlistArrayList.get(getAdapterPosition()));
                    detailHomePlaylistItemFragment.setArguments(bundle);


                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, detailHomePlaylistItemFragment).addToBackStack(null).commit();
                }
            });
        }
    }
}
