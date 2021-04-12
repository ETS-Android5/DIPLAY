package app.dinhcuong.diplay.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import app.dinhcuong.diplay.Fragment.Fragment_Home;
import app.dinhcuong.diplay.Fragment.Fragment_Library;
import app.dinhcuong.diplay.Fragment.Fragment_Search;
import app.dinhcuong.diplay.ViewFullPlaylistFragment;

public class MainViewPagerAdapter extends FragmentStatePagerAdapter{

    public MainViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return new Fragment_Search();
            case 2:
                return new Fragment_Library();
            default:
                return new Fragment_Home();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }


}
