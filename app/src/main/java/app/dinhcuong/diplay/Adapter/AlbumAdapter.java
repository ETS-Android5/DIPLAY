package app.dinhcuong.diplay.Adapter;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

import app.dinhcuong.diplay.Fragment.DetailHomeAlbumItemFragment;
import app.dinhcuong.diplay.Fragment.DetailHomePlaylistItemFragment;
import app.dinhcuong.diplay.Model.Album;
import app.dinhcuong.diplay.R;

import static java.lang.Integer.parseInt;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {


    Context context;
    ArrayList<Album> albumArrayList;
    String selection;

    public AlbumAdapter(Context context, ArrayList<Album> albumArrayList, String selection) {
        this.context = context;
        this.albumArrayList = albumArrayList;
        this.selection = selection;
    }

    public AlbumAdapter(Context context, ArrayList<Album> albumArrayList) {
        this.context = context;
        this.albumArrayList = albumArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        //= LayoutInflater.from(context).inflate(R.layout.item_home_rounded_square, parent, false);

        //If selection == ALL -> use item_view_all_rounded_square
        if (selection == "ALL"){
            view = LayoutInflater.from(context).inflate(R.layout.item_view_all_rounded_square, parent, false);
        } else if (selection == "LIBRARY") {
            view = LayoutInflater.from(context).inflate(R.layout.item_library_rounded_square, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_home_rounded_square, parent, false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Album album = albumArrayList.get(position);
        holder.nameAlbum.setText(album.getNameAlbum());
        //holder.followers.setText(album.getFollowsAlbum());
        Picasso.get().load(album.getImageAlbum()).into(holder.imageAlbum);
        if (holder.numbersOfAlbum != null){
            String numbersOfSongsText = context.getResources().getQuantityString(R.plurals.numberOfSongs, album.getSizeAlbum(), album.getSizeAlbum());
            holder.numbersOfAlbum.setText(numbersOfSongsText);
        }


        String followsOfPlaylistText = context.getResources().getQuantityString(R.plurals.numberOfFollows, parseInt(album.getFollowsAlbum()), parseInt(album.getFollowsAlbum()));
        holder.followers.setText(followsOfPlaylistText);
    }

    @Override
    public int getItemCount() {
        return albumArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageAlbum;
        TextView nameAlbum, followers, numbersOfAlbum;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageAlbum = itemView.findViewById(R.id.image_item);
            nameAlbum = itemView.findViewById(R.id.name_item);
            followers = itemView.findViewById(R.id.follows_item);
            numbersOfAlbum = itemView.findViewById(R.id.numbersOf_item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    DetailHomeAlbumItemFragment detailHomeAlbumItemFragment = new DetailHomeAlbumItemFragment();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("album", albumArrayList.get(getAdapterPosition()));
                    detailHomeAlbumItemFragment.setArguments(bundle);


                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, detailHomeAlbumItemFragment).addToBackStack(null).commit();
                }
            });
        }
    }
}
