package app.dinhcuong.diplay.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.Locale;

import app.dinhcuong.diplay.Activity.LoginActivity;
import app.dinhcuong.diplay.Activity.MainActivity;
import app.dinhcuong.diplay.R;


public class LoginHomeFragment extends Fragment {
    View view;
    TextView buttonLogin, buttonRegister, buttonChangeLanguage;
    int selectLanguage;
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

        buttonChangeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogLanguage();
            }
        });
    }

    private void mapping() {
        buttonLogin = view.findViewById(R.id.button_login);
        buttonRegister = view.findViewById(R.id.button_register);
        buttonChangeLanguage = view.findViewById(R.id.change_language);
    }

    private void openDialogLanguage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_select_language, null);

        final TextView en = dialogView.findViewById(R.id.langlague_en);
        final TextView ru = dialogView.findViewById(R.id.langlague_ru);
        final TextView vi = dialogView.findViewById(R.id.langlague_vi);

        TextView button_cancel = dialogView.findViewById(R.id.button_cancel);


        en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectLanguage = 0;
                handlerSelectLanguageNext(selectLanguage);
                //dismiss alert dialog when language selected
                alertDialog.dismiss();
            }
        });

        vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectLanguage = 1;
                handlerSelectLanguageNext(selectLanguage);
                //dismiss alert dialog when language selected
                alertDialog.dismiss();
            }
        });

        ru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectLanguage = 2;
                handlerSelectLanguageNext(selectLanguage);
                //dismiss alert dialog when language selected
                alertDialog.dismiss();
            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        alertDialog.getWindow().setContentView(dialogView);
        alertDialog.getWindow().setDimAmount(0.85f);
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
    private void handlerSelectLanguageNext(int selectLanguage) {
        if (selectLanguage == 0) {
            setLocale(getActivity(), "en", 0);
            Intent refresh = new Intent(getActivity(), LoginActivity.class);
            getActivity().finish();
            startActivity(refresh);
        }
        if (selectLanguage == 1) {
            setLocale(getActivity(), "vi", 1);
            Intent refresh = new Intent(getActivity(), LoginActivity.class);
            getActivity().finish();
            startActivity(refresh);
        }
        if (selectLanguage == 2) {
            setLocale(getActivity(), "ru", 2);
            Intent refresh = new Intent(getActivity(), LoginActivity.class);
            getActivity().finish();
            startActivity(refresh);
        }
    }

    public static void setLocale(Activity activity, String lang, int i) {
        Locale myLocale = new Locale(lang);
        Resources res = activity.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        SharedPreferences pref = activity.getApplicationContext().getSharedPreferences("Setting", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("lang", i);

        editor.commit();
    }

}