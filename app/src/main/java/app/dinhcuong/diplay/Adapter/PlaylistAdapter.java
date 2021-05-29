package app.dinhcuong.diplay.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.dinhcuong.diplay.Fragment.DetailHomePlaylistItemFragment;
import app.dinhcuong.diplay.Fragment.DetailPlaylistCreatedItemFragment;
import app.dinhcuong.diplay.Model.Playlist;
import app.dinhcuong.diplay.R;
import app.dinhcuong.diplay.Service.APIService;
import app.dinhcuong.diplay.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Integer.parseInt;

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
        } else if (selection == "LIBRARY") {
            view = LayoutInflater.from(context).inflate(R.layout.item_library_rounded_square, parent, false);
        } else if (selection == "CREATED") {
            view = LayoutInflater.from(context).inflate(R.layout.item_library_rounded_square_full, parent, false);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.item_home_rounded_square, parent, false);
        }



        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist playlist = playlistArrayList.get(position);
        holder.namePlaylist.setText(playlist.getNamePlaylist());
        Picasso.get().load(playlist.getImagePlaylist()).into(holder.imagePlaylist);
        //holder.followers.setText(playlist.getFollowsPlaylist());

        if (holder.numbersOfPlaylist != null){
            String numbersOfSongsText = context.getResources().getQuantityString(R.plurals.numberOfSongs, playlist.getSizePlaylist(), playlist.getSizePlaylist());
            holder.numbersOfPlaylist.setText(numbersOfSongsText);
        }

        if (holder.deleteButton != null){
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialogConfirm(playlist.getIdPlaylist(), position);
                }
            });
        }

        String followsOfPlaylistText = context.getResources().getQuantityString(R.plurals.numberOfFollows, parseInt(playlist.getFollowsPlaylist()), parseInt(playlist.getFollowsPlaylist()));
        holder.followers.setText(followsOfPlaylistText);
    }

    @Override
    public int getItemCount() {
        return playlistArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imagePlaylist;
        TextView namePlaylist , followers, numbersOfPlaylist;
        ImageButton deleteButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePlaylist = itemView.findViewById(R.id.image_item);
            namePlaylist = itemView.findViewById(R.id.name_item);
            followers = itemView.findViewById(R.id.follows_item);
            numbersOfPlaylist = itemView.findViewById(R.id.numbersOf_item);
            deleteButton = itemView.findViewById(R.id.deleteButton);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    Bundle bundle = new Bundle();
                    Log.e("ACBDSJ" + selection, "sđjs");
                    if (selection == "CREATED") {
                        DetailPlaylistCreatedItemFragment detailPlaylistCreatedItemFragment = new DetailPlaylistCreatedItemFragment();
                        bundle.putSerializable("playlist", playlistArrayList.get(getAdapterPosition()));
                        detailPlaylistCreatedItemFragment.setArguments(bundle);
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, detailPlaylistCreatedItemFragment).addToBackStack(null).commit();
                    } else {
                        DetailHomePlaylistItemFragment detailHomePlaylistItemFragment = new DetailHomePlaylistItemFragment();
                        bundle.putSerializable("playlist", playlistArrayList.get(getAdapterPosition()));
                        detailHomePlaylistItemFragment.setArguments(bundle);
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, detailHomePlaylistItemFragment).addToBackStack(null).commit();
                    }

                }
            });
        }
    }

    private void openDialogConfirm(String id_playlist, int position) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_confirm, null);


        TextView button_cancel = dialogView.findViewById(R.id.button_cancel);
        TextView button_confirm = dialogView.findViewById(R.id.button_confirm);
        TextView content = dialogView.findViewById(R.id.text_confirm);

        String text_content = context.getResources().getString(R.string.do_you_want_to_delete_this_playlist);
        content.setText(text_content);


        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataService dataService = APIService.getService();
                SharedPreferences pref = context.getSharedPreferences("Auth", context.MODE_PRIVATE);
                String id_user_SP = pref.getString("id_user","0");

                Call<String> callback = dataService.handlerCreatePlaylistRemove(id_user_SP, id_playlist);
                callback.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String result = response.body();
                        if (result.equals("SUCCESS")){

                            playlistArrayList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, playlistArrayList.size());
                            notifyDataSetChanged();

                            Toast.makeText(context, "Remove playlist successfully !", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();

                        } else  {
                            Toast.makeText(context, "Remove playlist failed!", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });


        alertDialog.getWindow().setContentView(dialogView);
        alertDialog.getWindow().setDimAmount(0.85f);
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
}
