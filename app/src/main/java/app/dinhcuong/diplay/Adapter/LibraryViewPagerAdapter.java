package app.dinhcuong.diplay.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import app.dinhcuong.diplay.Fragment.LibraryAlbumsFragment;
import app.dinhcuong.diplay.Fragment.LibraryArtistsFragment;
import app.dinhcuong.diplay.Fragment.LibraryOfflineFragment;
import app.dinhcuong.diplay.Fragment.LibraryPlaylistsFragment;
import app.dinhcuong.diplay.R;

public class LibraryViewPagerAdapter extends FragmentStatePagerAdapter{

    final int PAGE_COUNT = 4;
    Context context;

    public LibraryViewPagerAdapter(@NonNull FragmentManager fm, int behavior, Context mContext) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        context = mContext;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return new LibraryArtistsFragment();
            case 2:
                return new LibraryAlbumsFragment();
            case 3:
                return new LibraryOfflineFragment();
            default:
                return new LibraryPlaylistsFragment();
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 1:
                title = context.getString(R.string.ARTISTS);
                break;
            case 2:
                title = context.getString(R.string.ALBUMS);
                break;
            case 3:
                title = context.getString(R.string.STORAGE);
                break;
            default:
                title = context.getString(R.string.PLAYLISTS);
                break;
        }
        return title;
    }
}
