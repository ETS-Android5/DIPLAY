package app.dinhcuong.diplay.Fragment;

import android.drm.DrmStore;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import app.dinhcuong.diplay.Adapter.PlaybackAdapter;
import app.dinhcuong.diplay.Model.Song;
import app.dinhcuong.diplay.R;


public class PlaybackBottomScreenFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    PlaybackAdapter playbackAdapter;
    TextView nameNextSong, singerNextSong;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_playback_bottom_screen, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_playback_song_list);
        if (PlaybackFragment.songArrayList.size() > 0){
            playbackAdapter = new PlaybackAdapter(getActivity(), PlaybackFragment.songArrayList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(playbackAdapter);
            updateNextSong(0);
        }


        return view;
    }

    public void updateNextSong(int currentPosition) {
        int positionNext = currentPosition + 1;
        Log.e("TEXT POSITION" + positionNext, "AVC");
        nameNextSong = view.findViewById(R.id.name_song_next_song);
        singerNextSong = view.findViewById(R.id.name_singer_next_song);
        if (PlaybackFragment.songArrayList.get(positionNext).getLinkSong() != null){
            singerNextSong.setText(PlaybackFragment.songArrayList.get(positionNext).getNameSinger());
            nameNextSong.setText(PlaybackFragment.songArrayList.get(positionNext).getNameSong());
        } else {
            singerNextSong.setText(PlaybackFragment.songArrayList.get(positionNext).getArtist());
            nameNextSong.setText(PlaybackFragment.songArrayList.get(positionNext).getTitle());
        }
    }
}