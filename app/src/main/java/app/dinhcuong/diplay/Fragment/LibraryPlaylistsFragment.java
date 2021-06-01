package app.dinhcuong.diplay.Fragment;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;

import app.dinhcuong.diplay.Adapter.PlaylistAdapter;
import app.dinhcuong.diplay.Model.Playlist;
import app.dinhcuong.diplay.R;
import app.dinhcuong.diplay.Service.APIService;
import app.dinhcuong.diplay.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LibraryPlaylistsFragment extends Fragment {
    View view;

    LottieAnimationView lottieAnimationView, lottieAnimationView2, animationLoading;
    PlaylistAdapter playlistAdapter;
    RecyclerView recyclerViewCreated, recyclerViewFollowed;
    TextView buttonCreatePlaylist, textNumberOfLikeSongs, noDataText;
    ConstraintLayout itemLikedSongs;
    String sizePlaylist;
    ArrayList<Playlist> playlistArrayList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_library_playlists, container, false);
        mapping();
        getDataLikeSongs();
        getDataCreated();
        getDataFollowed();

        buttonCreatePlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogCreatePlaylist();
            }
        });

        /*
        if (itemLikedSongs != null) {
            itemLikedSongs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    AppCompatActivity activity = (AppCompatActivity) getContext();
                    Bundle bundle = new Bundle();
                    String imagePlaylist = "https://ledhcg.000webhostapp.com/android_diplay/images/playlists/lovedSongs.jpg";
                    bundle.putString("sizePlaylist", sizePlaylist);
                    bundle.putString("imagePlaylist", imagePlaylist);
                    DetailPlaylistLikedSongsItemFragment detailPlaylistLikedSongsItemFragment = new DetailPlaylistLikedSongsItemFragment();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, detailPlaylistLikedSongsItemFragment).addToBackStack(null).commit();

                }
            });
        }
        */


        return view;
    }

    private void getDataLikeSongs() {
        DataService dataService = APIService.getService();
        SharedPreferences pref = getActivity().getSharedPreferences("Auth", getActivity().MODE_PRIVATE);
        String id_user_SP = pref.getString("id_user","0");
        Call<String> callback = dataService.handlerGetNumberOfLikeSongs(id_user_SP, "getNumberOfLikeSongs");
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                sizePlaylist = result;
                String numbersOfSongsText = getActivity().getResources().getQuantityString(R.plurals.numberOfSongs, Integer.parseInt(result), Integer.parseInt(result));
                textNumberOfLikeSongs.setText(numbersOfSongsText);
                itemLikedSongs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppCompatActivity activity = (AppCompatActivity) getContext();
                        Bundle bundle = new Bundle();
                        String imagePlaylist = "https://ledhcg.000webhostapp.com/android_diplay/images/playlists/lovedSongs.jpg";
                        bundle.putString("sizePlaylist", sizePlaylist);
                        bundle.putString("imagePlaylist", imagePlaylist);
                        DetailPlaylistLikedSongsItemFragment detailPlaylistLikedSongsItemFragment = new DetailPlaylistLikedSongsItemFragment();
                        detailPlaylistLikedSongsItemFragment.setArguments(bundle);
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, detailPlaylistLikedSongsItemFragment).addToBackStack(null).commit();
                    }
                });
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
        Call<List<Playlist>> callback2 = dataService.getDataLibraryPlaylist(id_user_SP, "likedSongs");
        callback2.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                playlistArrayList = (ArrayList<Playlist>) response.body();
            }
            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
            }
        });
    }

    private void openDialogCreatePlaylist() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_library_create_playlist, null);
        EditText text_name_new_playlist = dialogView.findViewById(R.id.text_name_new_playlist);
        TextView button_cancel = dialogView.findViewById(R.id.button_cancel);
        TextView button_create = dialogView.findViewById(R.id.button_create);
        SharedPreferences pref = getActivity().getSharedPreferences("Auth", getActivity().MODE_PRIVATE);
        String name_user_SP = pref.getString("name_user","USER");
        String email_user_SP = pref.getString("email_user", "mail@diplay.com");
        String id_user_SP = pref.getString("id_user", "0");
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        button_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationLoading.setVisibility(View.VISIBLE);
                if (text_name_new_playlist.getText().toString().trim().length() > 0){
                    String text_name = text_name_new_playlist.getText().toString();
                    DataService dataService = APIService.getService();
                    Call<String> callback = dataService.handlerCreatePlaylist(id_user_SP, text_name, "https://ledhcg.000webhostapp.com/android_diplay/images/playlists/playlist_created.png");
                    callback.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String result = response.body();
                            if(result.equals("SUCCESS")){
                                Toast.makeText(getContext(), "Create playlist success!", Toast.LENGTH_SHORT).show();
                                animationLoading.setVisibility(View.INVISIBLE);
                                getDataCreated();
                                alertDialog.dismiss();
                            } else {
                                animationLoading.setVisibility(View.INVISIBLE);
                                Toast.makeText(getContext(), "Create playlist fail!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                        }
                    });
                } else {
                    animationLoading.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "Invalid playlist's name!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.getWindow().setContentView(dialogView);
        alertDialog.getWindow().setDimAmount(0.85f);
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private void mapping() {
        recyclerViewCreated = view.findViewById(R.id.recycler_view_library_playlists_created);
        recyclerViewFollowed = view.findViewById(R.id.recycler_view_library_playlists_followed);
        lottieAnimationView = view.findViewById(R.id.animation_loading);
        lottieAnimationView2 = view.findViewById(R.id.animation_loading_2);
        buttonCreatePlaylist = view.findViewById(R.id.button_create_playlist);
        animationLoading = view.findViewById(R.id.animation_loading);
        textNumberOfLikeSongs = view.findViewById(R.id.text_number_of_like_songs);
        itemLikedSongs = view.findViewById(R.id.item_liked_songs);
        noDataText = view.findViewById(R.id.text_nodata_item);
    }

    private void getDataFollowed() {
        DataService dataService = APIService.getService();

        SharedPreferences pref = getActivity().getSharedPreferences("Auth", getActivity().MODE_PRIVATE);
        String id_user_SP = pref.getString("id_user","0");

        Call<List<Playlist>> callback = dataService.getDataLibraryPlaylist(id_user_SP, "followed");
        callback.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                ArrayList<Playlist> playlistArrayList = (ArrayList<Playlist>) response.body();
                if(playlistArrayList.size() > 0){
                    playlistAdapter = new PlaylistAdapter(getActivity(), playlistArrayList, "LIBRARY");
                    lottieAnimationView2.setVisibility(View.GONE);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerViewFollowed.setLayoutManager(linearLayoutManager);
                    recyclerViewFollowed.setAdapter(playlistAdapter);
                } else {
                    noDataText.setVisibility(View.VISIBLE);
                    lottieAnimationView2.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {

            }
        });
    }
    private void getDataCreated() {
        DataService dataService = APIService.getService();

        SharedPreferences pref = getActivity().getSharedPreferences("Auth", getActivity().MODE_PRIVATE);
        String id_user_SP = pref.getString("id_user","0");

        Call<List<Playlist>> callback = dataService.getDataLibraryPlaylist(id_user_SP, "created");
        callback.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                ArrayList<Playlist> playlistArrayList = (ArrayList<Playlist>) response.body();
                playlistAdapter = new PlaylistAdapter(getActivity(), playlistArrayList, "CREATED");
                lottieAnimationView.setVisibility(View.GONE);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerViewCreated.setLayoutManager(linearLayoutManager);
                recyclerViewCreated.setAdapter(playlistAdapter);
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {

            }
        });
    }


}