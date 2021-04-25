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

import app.dinhcuong.diplay.Activity.MainActivity;
import app.dinhcuong.diplay.Model.User;
import app.dinhcuong.diplay.R;
import app.dinhcuong.diplay.Service.APIService;
import app.dinhcuong.diplay.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginRegisterFragment extends Fragment {
    View view;
    TextView text_bottom, button_create;

    EditText input_name, input_email, input_password, input_password_repeat;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login_register, container, false);
        mapping();
        init();


        return view;
    }

    private void init() { 
        text_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginLoginFragment loginLoginFragment = new LoginLoginFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, loginLoginFragment).addToBackStack(null).commit();
            }
        });

        button_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(input_email.getText())
                        || TextUtils.isEmpty(input_password.getText())
                        || TextUtils.isEmpty(input_name.getText())
                        || TextUtils.isEmpty(input_password_repeat.getText())){
                    Toast.makeText(getContext(), "Name/Email/Password required", Toast.LENGTH_SHORT).show();
                } else {
                    //Proceed to register
                    register();
                }
            }
        });
    }

    private void register() {
        DataService dataService = APIService.getService();
        Call<User> callback = dataService.handlerLogin(input_email.getText().toString(), "", "checkEmail");
        callback.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User result = response.body();
                if (result.getResult()){  //Email exits
                    Toast.makeText(getContext(), "Email already exists!", Toast.LENGTH_SHORT).show();
                } else {
                    if(checkPasswordMatch()){
                        DataService dataService = APIService.getService();
                        Call<String> callbackRegister = dataService.handlerRegister(input_name.getText().toString(), input_email.getText().toString(),input_password.getText().toString());
                        callbackRegister.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String result = response.body();
                                if (result.equals("SUCCESS")){
                                    Toast.makeText(getContext(), "Account successfully created!", Toast.LENGTH_SHORT).show();
                                    LoginLoginFragment loginLoginFragment = new LoginLoginFragment();
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, loginLoginFragment).addToBackStack(null).commit();
                                } else {
                                    Toast.makeText(getContext(), "Account creation failed!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Toast.makeText(getContext(), "Throwable"+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "Password does not match!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), "Throwable"+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkPasswordMatch() {
        if (input_password.getText().toString().equals(input_password_repeat.getText().toString())) {
            return true;
        } else{
            return false;
        }
    }


    private void mapping() {
        text_bottom = view.findViewById(R.id.text_bottom);
        input_name = view.findViewById(R.id.text_register_input_name);
        input_email = view.findViewById(R.id.text_register_input_email);
        input_password = view.findViewById(R.id.text_register_input_password);
        input_password_repeat = view.findViewById(R.id.text_register_input_repeat_password);
        button_create = view.findViewById(R.id.button_create);
    }
}