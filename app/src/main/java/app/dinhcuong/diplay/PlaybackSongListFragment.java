package app.dinhcuong.diplay;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.dinhcuong.diplay.Adapter.PlaybackAdapter;


public class PlaybackSongListFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    PlaybackAdapter playbackAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_playback_song_list, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_playback_song_list);
        if (PlaybackFragment.songArrayList.size() > 0){
            playbackAdapter = new PlaybackAdapter(getActivity(), PlaybackFragment.songArrayList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(playbackAdapter);

        }


        return view;
    }
}