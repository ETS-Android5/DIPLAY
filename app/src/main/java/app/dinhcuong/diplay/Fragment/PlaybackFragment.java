package app.dinhcuong.diplay.Fragment;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import app.dinhcuong.diplay.Adapter.LocalSongAdapter;
import app.dinhcuong.diplay.Adapter.PlaylistAdapter;
import app.dinhcuong.diplay.Adapter.PlaylistSelectAdapter;
import app.dinhcuong.diplay.Model.Playlist;
import app.dinhcuong.diplay.Model.Song;
import app.dinhcuong.diplay.R;
import app.dinhcuong.diplay.Service.APIService;
import app.dinhcuong.diplay.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaybackFragment extends Fragment {

    View view;
    CardView cardView;
    ImageView image_song;
    ObjectAnimator objectAnimator;

    MediaPlayer mediaPlayer;

    TextView time_start, time_end, name_song, name_singer, textSource, nameSource, nameNextSong, singerNextSong;

    SeekBar seekBar;

    ImageButton buttonPlay, buttonNext, buttonPrevious, repeatButton, shuffleButton, heartButton, addPlaylistButton, menuButton;

    ViewPager viewPager;

    ChipNavigationBar bottomNavigationView;
    FragmentManager fragmentManager;

    PlaybackBottomScreenFragment playbackBottomScreenFragment;


    public boolean THIS_MODE_OFFLINE = false;

    public static ArrayList<Song> songArrayList = new ArrayList<>();

    //Value
    int position = 0;
    boolean repeat = false;
    boolean checkRandom = false;
    boolean next = false;


    //Play offline

    static Uri uri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_playback, container, false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build();
        StrictMode.setThreadPolicy(policy);
        cardView = view.findViewById(R.id.imageSong);

        handler_bottom();
        mapping();



        return view;
    }

    private void handler_bottom() {
        bottomNavigationView = view.findViewById(R.id.navigationBar);

        bottomNavigationView.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i) {
                    case R.id.home:
                        fragment = new Fragment_Home();
                        break;
                    case R.id.search:
                        fragment = new Fragment_Search();
                        break;
                    case R.id.library:
                        fragment = new Fragment_Library();
                        break;
                }
                if (fragment != null){
                    fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit();
                } else {
                    Log.e("TAG", "ERROR in creating fragment");
                }

            }
        });

        bottomNavigationView.setItemSelected(R.id.home, true);
    }
    //Function wil be call by MainActivity to send data and change data
    public void reloadData(Song songArrayListSend, boolean MODE_OFFLINE, String name_playlist, String source) {
        THIS_MODE_OFFLINE = MODE_OFFLINE;
        getData(songArrayListSend);
        init(name_playlist, source);
        if(MODE_OFFLINE){
            eventClickL();
        } else {
            eventClick();
        }

    }
    public void reloadData(ArrayList<Song> songArrayListSend, boolean MODE_OFFLINE, String name_playlist, String source) {
        THIS_MODE_OFFLINE = MODE_OFFLINE;
        getData(songArrayListSend);
        init(name_playlist, source);
        if(MODE_OFFLINE){
            eventClickL();
        } else {
            eventClick();
        }
    }

    private void eventClickL() {

        //Button

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer != null && mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    buttonPlay.setBackgroundResource(R.drawable.ic_fluent_play_48_filled);
                    pauseAnimation();
                } else {
                    mediaPlayer.start();
                    buttonPlay.setBackgroundResource(R.drawable.ic_fluent_pause_48_filled);
                    startAnimation();
                }
            }
        });

        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeat == false){
                    if (checkRandom == true) {
                        checkRandom = false;
                        repeatButton.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.color_active));
                        shuffleButton.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.white));
                    }
                    repeatButton.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.color_active));
                    repeat = true;
                } else {
                    repeatButton.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.white));
                    repeat = false;
                }
            }
        });

        shuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkRandom == false){
                    if (repeat == true) {
                        repeat = false;
                        repeatButton.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.white));
                        shuffleButton.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.color_active));
                    }
                    shuffleButton.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.color_active));
                    checkRandom = true;
                } else {
                    shuffleButton.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.white));
                    checkRandom = false;
                }
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(songArrayList.size() > 0) {
                    if (mediaPlayer.isPlaying() || mediaPlayer != null){
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer= null;
                    }
                    if (position < (songArrayList.size())){
                        buttonPlay.setBackgroundResource(R.drawable.ic_fluent_pause_48_filled);
                        position++;

                        if (repeat == true){
                            if (position == 0){
                                position = songArrayList.size();
                            }
                            position -= 1;
                        }

                        if (checkRandom == true){
                            Random random = new Random();
                            int index = random.nextInt(songArrayList.size());
                            if (index == position){
                                position = index - 1;
                            }
                            position = index;
                        }

                        if(position > songArrayList.size() - 1){
                            position = 0;
                        }
                        name_singer.setText(songArrayList.get(position).getArtist());
                        name_song.setText(songArrayList.get(position).getTitle());

                        byte[] image = getAlbumArt(songArrayList.get(position).getPath());
                        if (image != null) {
                            Glide.with(getContext()).asBitmap()
                                    .load(image)
                                    .into(image_song);
                        } else {
                            Glide.with(getContext())
                                    .load(R.drawable.homnaytoibuon)
                                    .into(image_song);
                        }
                        PlayL(position);
                        getNextSong();

                    }
                }


                //Set disable button after click 3s
                buttonNext.setClickable(false);
                buttonPrevious.setClickable(false);

                Handler handlerDisableButton = new Handler();
                handlerDisableButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        buttonNext.setClickable(true);
                        buttonPrevious.setClickable(true);
                    }
                }, 3000);
            }
        });

        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(songArrayList.size() > 0) {
                    if (mediaPlayer.isPlaying() || mediaPlayer != null){
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer= null;
                    }
                    if (position < (songArrayList.size())){
                        buttonPlay.setBackgroundResource(R.drawable.ic_fluent_pause_48_filled);
                        position--;



                        if (repeat == true){
                            position += 1;
                        }

                        if (checkRandom == true){
                            Random random = new Random();
                            int index = random.nextInt(songArrayList.size());
                            if (index == position){
                                position = index - 1;
                            }
                            position = index;
                        }

                        if (position < 0){
                            position = songArrayList.size()-1;
                        }
                        name_singer.setText(songArrayList.get(position).getArtist());
                        name_song.setText(songArrayList.get(position).getTitle());

                        byte[] image = getAlbumArt(songArrayList.get(position).getPath());
                        if (image != null) {
                            Glide.with(getContext()).asBitmap()
                                    .load(image)
                                    .into(image_song);
                        } else {
                            Glide.with(getContext())
                                    .load(R.drawable.homnaytoibuon)
                                    .into(image_song);
                        }
                        PlayL(position);
                        getNextSong();

                    }
                }


                //Set disable button after click 3s
                buttonNext.setClickable(false);
                buttonPrevious.setClickable(false);

                Handler handlerDisableButton = new Handler();
                handlerDisableButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        buttonNext.setClickable(true);
                        buttonPrevious.setClickable(true);
                    }
                }, 3000);
            }
        });

        //SeekBar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
    }
    private void eventClick() {

        //Button
        addPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogAddPlaylist(songArrayList.get(position).getIdSong());
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogLyricSong(songArrayList.get(position).getLyricSong());
            }
        });

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer != null && mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    buttonPlay.setBackgroundResource(R.drawable.ic_fluent_play_48_filled);
                    pauseAnimation();
                } else {
                    mediaPlayer.start();
                    buttonPlay.setBackgroundResource(R.drawable.ic_fluent_pause_48_filled);
                    startAnimation();
                }
            }
        });

        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeat == false){
                    if (checkRandom == true) {
                        checkRandom = false;
                        repeatButton.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.color_active));
                        shuffleButton.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.white));
                    }
                    repeatButton.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.color_active));
                    repeat = true;
                } else {
                    repeatButton.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.white));
                    repeat = false;
                }
            }
        });

        shuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkRandom == false){
                    if (repeat == true) {
                        repeat = false;
                        repeatButton.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.white));
                        shuffleButton.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.color_active));
                    }
                    shuffleButton.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.color_active));
                    checkRandom = true;
                } else {
                    shuffleButton.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.white));
                    checkRandom = false;
                }
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(songArrayList.size() > 0) {
                    if (mediaPlayer.isPlaying() || mediaPlayer != null){
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer= null;
                    }
                    if (position < (songArrayList.size())){
                        buttonPlay.setBackgroundResource(R.drawable.ic_fluent_pause_48_filled);
                        position++;

                        if (repeat == true){
                            if (position == 0){
                                position = songArrayList.size();
                            }
                            position -= 1;
                        }

                        if (checkRandom == true){
                            Random random = new Random();
                            int index = random.nextInt(songArrayList.size());
                            if (index == position){
                                position = index - 1;
                            }
                            position = index;
                        }

                        if(position > songArrayList.size() - 1){
                            position = 0;
                        }
                        Picasso.get().load(songArrayList.get(position).getImageSong()).into(image_song);
                        name_singer.setText(songArrayList.get(position).getNameSinger());
                        name_song.setText(songArrayList.get(position).getNameSong());
                        setButtonLike(songArrayList.get(position),heartButton);
                        new PlaySong().execute(songArrayList.get(position).getLinkSong());
                        getNextSong();

                    }
                }


                //Set disable button after click 3s
                buttonNext.setClickable(false);
                buttonPrevious.setClickable(false);

                Handler handlerDisableButton = new Handler();
                handlerDisableButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        buttonNext.setClickable(true);
                        buttonPrevious.setClickable(true);
                    }
                }, 3000);
            }
        });

        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(songArrayList.size() > 0) {
                    if (mediaPlayer.isPlaying() || mediaPlayer != null){
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer= null;
                    }
                    if (position < (songArrayList.size())){
                        buttonPlay.setBackgroundResource(R.drawable.ic_fluent_pause_48_filled);
                        position--;



                        if (repeat == true){
                            position += 1;
                        }

                        if (checkRandom == true){
                            Random random = new Random();
                            int index = random.nextInt(songArrayList.size());
                            if (index == position){
                                position = index - 1;
                            }
                            position = index;
                        }

                        if (position < 0){
                            position = songArrayList.size()-1;
                        }
                        Picasso.get().load(songArrayList.get(position).getImageSong()).into(image_song);
                        name_singer.setText(songArrayList.get(position).getNameSinger());
                        name_song.setText(songArrayList.get(position).getNameSong());
                        setButtonLike(songArrayList.get(position),heartButton);
                        new PlaySong().execute(songArrayList.get(position).getLinkSong());
                        getNextSong();

                    }
                }


                //Set disable button after click 3s
                buttonNext.setClickable(false);
                buttonPrevious.setClickable(false);

                Handler handlerDisableButton = new Handler();
                handlerDisableButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        buttonNext.setClickable(true);
                        buttonPrevious.setClickable(true);
                    }
                }, 3000);
            }
        });

        //SeekBar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        heartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataService dataService = APIService.getService();
                SharedPreferences pref = getActivity().getSharedPreferences("Auth", getContext().MODE_PRIVATE);
                String id_user_SP = pref.getString("id_user","0");

                Call<String> callback = dataService.handlerLikeForSong(id_user_SP, songArrayList.get(position).getIdSong(), "0", "checkLiked");
                callback.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String result = response.body();
                        if (result.equals("LIKED")){
                            DataService dataService = APIService.getService();
                            SharedPreferences pref = getActivity().getSharedPreferences("Auth", getActivity().MODE_PRIVATE);
                            String id_user_SP = pref.getString("id_user","0");

                            Call<String> callback = dataService.handlerLikeForSong(id_user_SP, songArrayList.get(position).getIdSong(), "1", "unlike");
                            callback.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    String result = response.body();
                                    if (result.equals("SUCCESS")){
                                        heartButton.setBackgroundResource(R.drawable.ic_fluent_heart_24_regular);
                                        Toast.makeText(getActivity(), "Disliked", Toast.LENGTH_SHORT).show();

                                    } else  {
                                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {

                                }
                            });
                        } else  {
                            DataService dataService = APIService.getService();
                            SharedPreferences pref = getActivity().getSharedPreferences("Auth", getActivity().MODE_PRIVATE);
                            String id_user_SP = pref.getString("id_user","0");

                            Call<String> callback = dataService.handlerLikeForSong(id_user_SP, songArrayList.get(position).getIdSong(), "1", "like");
                            callback.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    String result = response.body();
                                    if (result.equals("SUCCESS")){
                                        heartButton.setBackgroundResource(R.drawable.ic_fluent_heart_24_filled);
                                        Toast.makeText(getActivity(), "Liked", Toast.LENGTH_SHORT).show();

                                    } else  {
                                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });




            }
        });
    }

    private void openDialogLyricSong(String lyricText) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_lyric, null);

        TextView button_hide = dialogView.findViewById(R.id.button_hide);
        TextView lyric = dialogView.findViewById(R.id.lyric);
        if (lyricText == null || lyricText.equals("")){
            String no_data = getContext().getResources().getString(R.string.not_found_data);
            lyric.setText(no_data);
        } else {
            lyric.setText(lyricText);
        }








        button_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        alertDialog.getWindow().setContentView(dialogView);
        alertDialog.getWindow().setDimAmount(0.85f);
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


    private void startAnimation(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                cardView.animate().rotationBy(360).setDuration(15000).withEndAction(this).setInterpolator(new LinearInterpolator()).start();
            }
        };
        cardView.animate().rotationBy(360).setDuration(15000).withEndAction(runnable).setInterpolator(new LinearInterpolator()).start();
    }

    private void pauseAnimation(){
        cardView.animate().cancel();

    }
    private void mapping() {

        //SeekBar
        seekBar = view.findViewById(R.id.seekbar);

        //Button
        buttonPlay = view.findViewById(R.id.buttonPlay);
        buttonNext = view.findViewById(R.id.buttonNext);
        buttonPrevious = view.findViewById(R.id.buttonPrevious);
        repeatButton = view.findViewById(R.id.repeatButton);
        shuffleButton = view.findViewById(R.id.shuffleButton);
        heartButton = view.findViewById(R.id.heartButton);
        addPlaylistButton = view.findViewById(R.id.addPlaylistButton);
        menuButton = view.findViewById(R.id.menuButton);

        //TextView
        time_start = view.findViewById(R.id.time_textStart);
        time_end = view.findViewById(R.id.time_textEnd);

        name_song = view.findViewById(R.id.nameSong);
        name_singer = view.findViewById(R.id.nameSinger);

        textSource = view.findViewById(R.id.textSource);
        nameSource = view.findViewById(R.id.nameSource);

        //Image
        image_song = view.findViewById(R.id.imageSong_source);



    }

    private void setButtonLike(Song song, ImageButton heartButton) {

        DataService dataService = APIService.getService();
        SharedPreferences pref = getActivity().getSharedPreferences("Auth", getActivity().MODE_PRIVATE);
        String id_user_SP = pref.getString("id_user","0");

        Call<String> callback = dataService.handlerLikeForSong(id_user_SP, song.getIdSong(), "0", "checkLiked");
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                if (result.equals("LIKED")){
                    heartButton.setBackgroundResource(R.drawable.ic_fluent_heart_24_filled);
                } else  {
                    heartButton.setBackgroundResource(R.drawable.ic_fluent_heart_24_regular);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void init(String name_playlist, String source) {

        removeFragment();

        if (THIS_MODE_OFFLINE){

            resetHeartButton();
            //Play with offline
            if(songArrayList.size() > 0) {

                if(songArrayList.size() > 1){
                    addFragment();
                }

                startAnimation();
                String text_playing_from = getContext().getResources().getString(R.string.PLAYING_FROM);
                String text_playing = getContext().getResources().getString(R.string.PLAYING);
                String text_a_single_song = getContext().getResources().getString(R.string.A_SINGLE_SONG);
                if(source.equals("")){
                    textSource.setText(text_playing);
                } else {
                    textSource.setText(text_playing_from + source);
                }
                if(name_playlist.equals("")){
                    nameSource.setText(text_a_single_song);
                } else {
                    nameSource.setText(name_playlist);
                }

                name_singer.setText(songArrayList.get(0).getArtist());
                name_song.setText(songArrayList.get(0).getTitle());

                byte[] image = getAlbumArt(songArrayList.get(0).getPath());
                if (image != null) {
                    Glide.with(getContext()).asBitmap()
                            .load(image)
                            .into(image_song);
                } else {
                    Glide.with(getContext())
                            .load(R.drawable.homnaytoibuon)
                            .into(image_song);
                }

                PlayL(0);
            }

        } else {
            //Play with online
            if(songArrayList.size() > 0){

            /*

            playbackViewPagerAdapter = new PlaybackViewPagerAdapter(getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

            //Clear fragment
            playbackViewPagerAdapter.clearFragment();
            viewPager.setAdapter(playbackViewPagerAdapter);




             */


                // Add list song if array of songs > 1
                if(songArrayList.size() > 1){
                    addFragment();
                /*
                playbackBottomScreenFragment = new PlaybackBottomScreenFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_playback_screen_song_list, playbackBottomScreenFragment).commit();
                /*


                PlaybackSongListFragment playbackSongListFragment = new PlaybackSongListFragment();
                playbackViewPagerAdapter = new PlaybackViewPagerAdapter(getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
                playbackViewPagerAdapter.addFragment(playbackSongListFragment);
                viewPager.setAdapter(playbackViewPagerAdapter);

                 */
                } else {
                    //PlaybackBottomScreenFragment playbackBottomScreenFragment = new PlaybackBottomScreenFragment();
                    //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_playback_screen_song_list, playbackBottomScreenFragment).commit();
                    //add test

                /*
                playbackViewPagerAdapter = new PlaybackViewPagerAdapter(getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
                playbackViewPagerAdapter.addFragment(playbackBottomScreenFragment);
                viewPager.setAdapter(playbackViewPagerAdapter);

                 */
                }
                startAnimation();
                String text_playing_from = getContext().getResources().getString(R.string.PLAYING_FROM);
                String text_playing = getContext().getResources().getString(R.string.PLAYING);
                String text_a_single_song = getContext().getResources().getString(R.string.A_SINGLE_SONG);
                if(source.equals("")){
                    textSource.setText(text_playing);
                } else {
                    textSource.setText(text_playing_from + source);
                }
                if(name_playlist.equals("")){
                    nameSource.setText(text_a_single_song);
                } else {
                    nameSource.setText(name_playlist);
                }
                Picasso.get().load(songArrayList.get(0).getImageSong()).into(image_song);
                name_singer.setText(songArrayList.get(0).getNameSinger());
                name_song.setText(songArrayList.get(0).getNameSong());
                setButtonLike(songArrayList.get(0),heartButton);
                new PlaySong().execute(songArrayList.get(0).getLinkSong());


            }
        }

    }

    public void HideButtonTopOfSeekBar(){

    }

    public byte[] getAlbumArt (String uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }

    private void getData(Song songArrayListSend) {
        songArrayList.clear();
        Song song = songArrayListSend;
        songArrayList.add(song);

        /*
        Bundle bundle = this.getArguments();
        songArrayList.clear();
        if (bundle != null) {
            if (bundle.getString("key") == "single"){
                Song song = (Song) bundle.getParcelable("song");
                songArrayList.add(song);
                Log.e("TEST123467789", song.getNameSong());
            }
            if (bundle.getString("key") == "multi"){
                ArrayList<Song> songArrayList_get = bundle.getParcelableArrayList("song");
                songArrayList = songArrayList_get;
                //

                for (int i = 0; i < songArrayList.size(); i++ ){
                    Log.d("TEST CUONG", songArrayList.get(i).getNameSong());
                }
                 //
            }

        }
        */
    }

    private void getData(ArrayList<Song> songArrayListSend) {
        songArrayList.clear();
        ArrayList<Song> songArrayList_get = songArrayListSend;
        songArrayList = songArrayList_get;
    }

    class PlaySong extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            return strings[0];
        }

        @Override
        protected void onPostExecute(String song) {
            super.onPostExecute(song);

            //Check if has 1 mediaPlayer is playing -> stop

            if (mediaPlayer != null){
                mediaPlayer.stop();
                mediaPlayer.reset();
            }

            //Create new mediaPlayer
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioAttributes(
                        new AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .build());
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                });
                mediaPlayer.setDataSource(song);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
            buttonPlay.setBackgroundResource(R.drawable.ic_fluent_pause_48_filled);

            timeSong();
            updateTime();
        }
    }

    //Set time for seekBar
    private void timeSong() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        Log.e("Time Song",simpleDateFormat.format(mediaPlayer.getDuration()) );
        time_end.setText(simpleDateFormat.format(mediaPlayer.getDuration()));
        seekBar.setMax(mediaPlayer.getDuration());
    }

    private void timeSongL() {
        int durationTotal = Integer.parseInt(songArrayList.get(position).getDuration())/1000;
        time_end.setText(fromMinutesToHHmm(durationTotal));
        seekBar.setMax(mediaPlayer.getDuration());
    }

    private void updateTime(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                    time_start.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                    handler.postDelayed(this, 200);
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            next = true;
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }, 200);

        Handler nextSongHandler = new Handler();
        nextSongHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (next == true){
                    if (position < (songArrayList.size())){
                        buttonPlay.setBackgroundResource(R.drawable.ic_fluent_pause_48_filled);
                        position++;

                        if (repeat == true){
                            if (position == 0){
                                position = songArrayList.size();
                            }
                            position -= 1;
                        }

                        if (checkRandom == true){
                            Random random = new Random();
                            int index = random.nextInt(songArrayList.size());
                            if (index == position){
                                position = index - 1;
                            }
                            position = index;
                        }

                        if(position > songArrayList.size() - 1){
                            position = 0;
                        }

                        Picasso.get().load(songArrayList.get(position).getImageSong()).into(image_song);
                        name_singer.setText(songArrayList.get(position).getNameSinger());
                        name_song.setText(songArrayList.get(position).getNameSong());
                        new PlaySong().execute(songArrayList.get(position).getLinkSong());
                        getNextSong();

                    }

                    //Set disable button after click 3s
                    buttonNext.setClickable(false);
                    buttonPrevious.setClickable(false);

                    Handler handlerDisableButton = new Handler();
                    handlerDisableButton.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            buttonNext.setClickable(true);
                            buttonPrevious.setClickable(true);
                        }
                    }, 3000);
                    next = false;
                    nextSongHandler.removeCallbacks(this);
                } else {
                    nextSongHandler.postDelayed(this, 500);
                }
            }
        }, 1000);
    }


    private void updateTimeL(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    int durationCurrent = mediaPlayer.getCurrentPosition()/1000;
                    time_start.setText(fromMinutesToHHmm(durationCurrent));
                    handler.postDelayed(this, 200);
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            next = true;
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }, 200);

        Handler nextSongHandler = new Handler();
        nextSongHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (next == true){
                    if (position < (songArrayList.size())){
                        buttonPlay.setBackgroundResource(R.drawable.ic_fluent_pause_48_filled);
                        position++;

                        if (repeat == true){
                            if (position == 0){
                                position = songArrayList.size();
                            }
                            position -= 1;
                        }

                        if (checkRandom == true){
                            Random random = new Random();
                            int index = random.nextInt(songArrayList.size());
                            if (index == position){
                                position = index - 1;
                            }
                            position = index;
                        }

                        if(position > songArrayList.size() - 1){
                            position = 0;
                        }

                        name_singer.setText(songArrayList.get(position).getArtist());
                        name_song.setText(songArrayList.get(position).getTitle());

                        byte[] image = getAlbumArt(songArrayList.get(position).getPath());
                        if (image != null) {
                            Glide.with(getContext()).asBitmap()
                                    .load(image)
                                    .into(image_song);
                        } else {
                            Glide.with(getContext())
                                    .load(R.drawable.homnaytoibuon)
                                    .into(image_song);
                        }
                        PlayL(position);
                        getNextSong();

                    }

                    //Set disable button after click 3s
                    buttonNext.setClickable(false);
                    buttonPrevious.setClickable(false);

                    Handler handlerDisableButton = new Handler();
                    handlerDisableButton.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            buttonNext.setClickable(true);
                            buttonPrevious.setClickable(true);
                        }
                    }, 3000);
                    next = false;
                    nextSongHandler.removeCallbacks(this);
                } else {
                    nextSongHandler.postDelayed(this, 500);
                }
            }
        }, 1000);
    }

    //Add Fragment
    public void addFragment(){
        playbackBottomScreenFragment = new PlaybackBottomScreenFragment();
        FragmentTransaction transation = getActivity().getSupportFragmentManager().beginTransaction();
        transation.replace(R.id.fragment_playback_screen_song_list, playbackBottomScreenFragment);
        transation.commit();
    }

    //Remove Fragment
    public void removeFragment(){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        if (playbackBottomScreenFragment != null) {
            transaction.remove(playbackBottomScreenFragment);
            transaction.commit();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            playbackBottomScreenFragment = null;
        }
    }

    public String fromMinutesToHHmm(int minutes) {
        long hours = TimeUnit.MINUTES.toHours(Long.valueOf(minutes));
        long remainMinutes = minutes - TimeUnit.HOURS.toMinutes(hours);
        return String.format("%02d:%02d", hours, remainMinutes);
    }

    public void PlayL(int position){
        uri = Uri.parse(songArrayList.get(position).getPath());
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getContext().getApplicationContext(), uri);
            mediaPlayer.start();
            buttonPlay.setBackgroundResource(R.drawable.ic_fluent_pause_48_filled);
        } else {
            mediaPlayer = MediaPlayer.create(getContext().getApplicationContext(), uri);
            mediaPlayer.start();
            buttonPlay.setBackgroundResource(R.drawable.ic_fluent_pause_48_filled);
        }
        timeSongL();
        updateTimeL();
    }
    public void getNextSong(){
        int currentPosition = 0;
        if (position + 1 < (songArrayList.size())) {
            currentPosition = position;
        }

        if (position + 1 > songArrayList.size() - 1) {
                currentPosition = -1;
        }
        if (playbackBottomScreenFragment != null) {
            playbackBottomScreenFragment.updateNextSong(currentPosition);
        }
    }

    public void resetHeartButton(){
        heartButton.setBackgroundResource(R.drawable.ic_fluent_heart_24_regular);
    }

    private void openDialogAddPlaylist(String id_song) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_select_playlist, null);

        TextView button_cancel = dialogView.findViewById(R.id.button_cancel);
        LottieAnimationView lottieAnimationView = dialogView.findViewById(R.id.animation_loading);
        RecyclerView recyclerViewCreated = dialogView.findViewById(R.id.recycler_view_select_playlists_created);


        DataService dataService = APIService.getService();

        SharedPreferences pref = getActivity().getSharedPreferences("Auth", getActivity().MODE_PRIVATE);
        String id_user_SP = pref.getString("id_user","0");

        Call<List<Playlist>> callback = dataService.getDataLibraryPlaylist(id_user_SP, "created");
        callback.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                ArrayList<Playlist> playlistArrayList = (ArrayList<Playlist>) response.body();
                PlaylistSelectAdapter playlistAdapterCreated = new PlaylistSelectAdapter(getActivity(), playlistArrayList, id_song);
                lottieAnimationView.setVisibility(View.GONE);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerViewCreated.setLayoutManager(linearLayoutManager);
                recyclerViewCreated.setAdapter(playlistAdapterCreated);
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {

            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        alertDialog.getWindow().setContentView(dialogView);
        alertDialog.getWindow().setDimAmount(0.85f);
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
}