package app.dinhcuong.diplay.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;
import com.vansuita.gaussianblur.GaussianBlur;

import java.util.Locale;

import app.dinhcuong.diplay.Activity.LoginActivity;
import app.dinhcuong.diplay.Activity.MainActivity;
import app.dinhcuong.diplay.Activity.SplashActivity;
import app.dinhcuong.diplay.R;
import butterknife.BindView;
import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import jp.wasabeef.blurry.Blurry;
import jp.wasabeef.picasso.transformations.BlurTransformation;


public class SettingFragment extends Fragment {

    View view;
    TextView  button, text_name_user, text_email_user, button_about_app, button_language, button_logout;
    Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //loadLocale(getActivity());
        view = inflater.inflate(R.layout.fragment_main_setting, container, false);

        mapping();

        init();



        return view;
    }

    private void init() {


        SharedPreferences pref = getActivity().getSharedPreferences("Auth", getActivity().MODE_PRIVATE);
        String name_user_SP = pref.getString("name_user","USER");
        String email_user_SP = pref.getString("email_user", "mail@diplay.com");

        text_name_user.setText(name_user_SP);
        text_email_user.setText(email_user_SP);


        dialog = new Dialog(getContext());
        button_about_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        button_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogLanguage();
            }
        });

        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences settings = getContext().getSharedPreferences("Auth", Context.MODE_PRIVATE);
                settings.edit().clear().commit();

                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void mapping() {
        button_about_app = view.findViewById(R.id.button_about_app);
        button_language = view.findViewById(R.id.button_language);
        button_logout = view.findViewById(R.id.button_log_out);
        text_name_user = view.findViewById(R.id.name_user);
        text_email_user = view.findViewById(R.id.email_user);
    }

    private void openDialogLanguage() {
        final String[] listLanguage = {
                "English",
                "Việt Nam",
                "Русский"
        };
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        mBuilder.setTitle(R.string.Choose_language);
        mBuilder.setSingleChoiceItems(listLanguage, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    setLocale(getActivity(), "en", 0);
                    Intent refresh = new Intent(getActivity(), MainActivity.class);
                    getActivity().finish();
                    startActivity(refresh);
                }
                if (which == 1) {
                    setLocale(getActivity(), "vi", 1);
                    Intent refresh = new Intent(getActivity(), MainActivity.class);
                    getActivity().finish();
                    startActivity(refresh);
                }
                if (which == 2) {
                    setLocale(getActivity(), "ru", 2);
                    Intent refresh = new Intent(getActivity(), MainActivity.class);
                    getActivity().finish();
                    startActivity(refresh);
                }

                //dismiss alert dialog when language selected
                dialog.dismiss();
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
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

    private void openDialog() {
        dialog.setContentView(R.layout.dialog_setting_about_app);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

}