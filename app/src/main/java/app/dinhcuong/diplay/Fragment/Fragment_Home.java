package app.dinhcuong.diplay.Fragment;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import app.dinhcuong.diplay.R;

public class Fragment_Home extends Fragment {
    View view;
    TextView text_getting;
    ImageButton buttonSetting;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_home, container, false);
        mapping();
        getGettingMessage();
        init();
        return view;
    }

    private void init() {
        buttonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingFragment settingFragment = new SettingFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, settingFragment).addToBackStack(null).commit();
            }
        });
    }

    private void mapping() {
        text_getting = view.findViewById(R.id.text_getting);
        buttonSetting = view.findViewById(R.id.settingButton);
    }



    private void getGettingMessage(){
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            text_getting.setText(R.string.good_morning);
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            text_getting.setText(R.string.good_afternoon);
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            text_getting.setText(R.string.good_evening);
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            text_getting.setText(R.string.good_night);
        }
    }
}
