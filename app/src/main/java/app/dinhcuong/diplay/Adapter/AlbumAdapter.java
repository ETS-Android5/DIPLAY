package app.dinhcuong.diplay.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.dinhcuong.diplay.Model.Album;
import app.dinhcuong.diplay.R;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    Context context;
    ArrayList<Album> albumArrayList;

    public AlbumAdapter(Context context, ArrayList<Album> albumArrayList) {
        this.context = context;
        this.albumArrayList = albumArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_rounded_square, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Album album = albumArrayList.get(position);
        holder.nameAlbum.setText(album.getNameAlbum());
        holder.likesAlbum.setText(album.getLikesAlbum());
        Picasso.get().load(album.getImageAlbum()).into(holder.imageAlbum);
    }

    @Override
    public int getItemCount() {
        return albumArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageAlbum;
        TextView nameAlbum;
        TextView likesAlbum;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageAlbum = itemView.findViewById(R.id.image_item);
            nameAlbum = itemView.findViewById(R.id.name_item);
            likesAlbum = itemView.findViewById(R.id.likes_item);

        }
    }
}
