package app.dinhcuong.diplay.Fragment;

import android.animation.ObjectAnimator;
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
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import app.dinhcuong.diplay.Adapter.LocalSongAdapter;
import app.dinhcuong.diplay.Model.Playlist;
import app.dinhcuong.diplay.Model.Song;
import app.dinhcuong.diplay.R;

public class PlaybackFragment extends Fragment {

    View view;
    CardView cardView;
    ImageView image_song;
    ObjectAnimator objectAnimator;

    MediaPlayer mediaPlayer;

    TextView time_start, time_end, name_song, name_singer, textSource, nameSource, nameNextSong, singerNextSong;

    SeekBar seekBar;

    ImageButton buttonPlay, buttonNext, buttonPrevious, repeatButton, shuffleButton;

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
                    case R.id.test:
                        fragment = new SettingFragment();
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

    private void init(String name_playlist, String source) {

        removeFragment();

        if (THIS_MODE_OFFLINE){
            //Play with offline
            if(songArrayList.size() > 0) {

                if(songArrayList.size() > 1){
                    addFragment();
                }

                startAnimation();
                textSource.setText("PLAYING FROM " + source);
                nameSource.setText(name_playlist);
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
                textSource.setText("PLAYING FROM " + source);
                nameSource.setText(name_playlist);
                Picasso.get().load(songArrayList.get(0).getImageSong()).into(image_song);
                name_singer.setText(songArrayList.get(0).getNameSinger());
                name_song.setText(songArrayList.get(0).getNameSong());
                new PlaySong().execute(songArrayList.get(0).getLinkSong());


            }
        }

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
}