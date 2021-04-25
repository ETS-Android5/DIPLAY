package app.dinhcuong.diplay.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import app.dinhcuong.diplay.R;


public class LoginRegisterFragment extends Fragment {
    View view;
    TextView text_bottom;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login_register, container, false);
        text_bottom = view.findViewById(R.id.text_bottom);
        text_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginLoginFragment loginLoginFragment = new LoginLoginFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, loginLoginFragment).addToBackStack(null).commit();
            }
        });
        return view;
    }
}