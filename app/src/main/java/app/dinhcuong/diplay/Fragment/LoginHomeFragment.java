package app.dinhcuong.diplay.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import app.dinhcuong.diplay.R;


public class LoginHomeFragment extends Fragment {
    View view;
    TextView buttonLogin, buttonRegister;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login_home, container, false);
        mapping();
        init();
        return view;
    }

    private void init() {
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginLoginFragment loginLoginFragment = new LoginLoginFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, loginLoginFragment).addToBackStack(null).commit();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginRegisterFragment loginRegisterFragment = new LoginRegisterFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, loginRegisterFragment).addToBackStack(null).commit();
            }
        });
    }

    private void mapping() {
        buttonLogin = view.findViewById(R.id.button_login);
        buttonRegister = view.findViewById(R.id.button_register);
    }
}