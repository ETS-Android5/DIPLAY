package app.dinhcuong.diplay.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

import app.dinhcuong.diplay.Fragment.DetailHomePlaylistItemFragment;
import app.dinhcuong.diplay.Fragment.DetailHomeSingerItemFragment;
import app.dinhcuong.diplay.Fragment.DetailPlaylistCreatedItemFragment;
import app.dinhcuong.diplay.Model.Singer;
import app.dinhcuong.diplay.Model.Song;
import app.dinhcuong.diplay.R;
import app.dinhcuong.diplay.Service.APIService;
import app.dinhcuong.diplay.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Integer.parseInt;

public class SingerAdapter extends RecyclerView.Adapter<SingerAdapter.ViewHolder> {
    Context context;
    ArrayList<Singer> singerArrayList;

    public SingerAdapter(Context context, ArrayList<Singer> singerArrayList) {
        this.context = context;
        this.singerArrayList = singerArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_singer_circle_big_center, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Singer singer = singerArrayList.get(position);
        holder.nameSinger.setText(singer.getNameSinger());
        String followsOfSingerText = context.getResources().getQuantityString(R.plurals.numberOfFollows, parseInt(singer.getFollowsSinger()), parseInt(singer.getFollowsSinger()));
        holder.followsSinger.setText(followsOfSingerText);

        Picasso.get().load(singer.getImageSinger()).into(holder.imageSinger);
    }



    @Override
    public int getItemCount() {
        return singerArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageSinger;
        TextView nameSinger, followsSinger;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageSinger = itemView.findViewById(R.id.image_item);
            nameSinger = itemView.findViewById(R.id.name_item);
            followsSinger = itemView.findViewById(R.id.follows_item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    Bundle bundle = new Bundle();
                        DetailHomeSingerItemFragment detailHomeSingerItemFragment = new DetailHomeSingerItemFragment();
                        bundle.putSerializable("singer", (Serializable) singerArrayList.get(getAdapterPosition()));
                        detailHomeSingerItemFragment.setArguments(bundle);
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, detailHomeSingerItemFragment).addToBackStack(null).commit();
                }
            });


        }
    }
}
