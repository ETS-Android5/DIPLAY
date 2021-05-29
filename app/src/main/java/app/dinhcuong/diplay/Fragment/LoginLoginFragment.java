package app.dinhcuong.diplay.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import app.dinhcuong.diplay.Activity.LoginActivity;
import app.dinhcuong.diplay.Activity.MainActivity;
import app.dinhcuong.diplay.Adapter.AlbumAdapter;
import app.dinhcuong.diplay.Model.Album;
import app.dinhcuong.diplay.Model.User;
import app.dinhcuong.diplay.R;
import app.dinhcuong.diplay.Service.APIService;
import app.dinhcuong.diplay.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginLoginFragment extends Fragment {
    View view;
    TextView text_bottom, button_login;
    EditText input_email, input_password;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login_login, container, false);
        mapping();

        text_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginRegisterFragment loginRegisterFragment = new LoginRegisterFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, loginRegisterFragment).addToBackStack(null).commit();
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(input_email.getText()) 
                        || TextUtils.isEmpty(input_password.getText())){
                    Toast.makeText(getContext(), "Email/Password required", Toast.LENGTH_SHORT).show();
                } else {
                    //Proceed to login
                    login();
                }
            }
        });
        return view;
    }

    private void mapping() {
        text_bottom = view.findViewById(R.id.text_bottom);
        input_email = view.findViewById(R.id.text_login_input_email);
        input_password = view.findViewById(R.id.text_login_input_password);
        button_login = view.findViewById(R.id.button_login);
    }

    public void login(){
        DataService dataService = APIService.getService();
        Call<User> callback = dataService.handlerLogin(input_email.getText().toString(), input_password.getText().toString(), "login");
        callback.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User result = response.body();
                if (result.getResult()){
                    Toast.makeText(getContext(), "Login success!", Toast.LENGTH_SHORT).show();

                    SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("Auth", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("isLogin", true);
                    editor.putString("id_user", result.getIdUser());
                    editor.putString("name_user", result.getNameUser());
                    editor.putString("email_user", result.getEmailUser());
                    editor.putString("password_user", result.getPasswordUser());
                    editor.commit();

                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), "Throwable"+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}