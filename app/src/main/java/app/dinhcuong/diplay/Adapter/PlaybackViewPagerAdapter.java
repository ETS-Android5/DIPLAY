package app.dinhcuong.diplay.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class PlaybackViewPagerAdapter extends FragmentPagerAdapter {

    public final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

    public PlaybackViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }

    public void addFragment(Fragment fragment){
        fragmentArrayList.add(fragment);
    }

    public void clearFragment(){
        fragmentArrayList.clear();
    }
}
