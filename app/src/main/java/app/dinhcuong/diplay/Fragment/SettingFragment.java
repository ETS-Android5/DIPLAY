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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Locale;

import app.dinhcuong.diplay.Activity.LoginActivity;
import app.dinhcuong.diplay.Activity.MainActivity;
import app.dinhcuong.diplay.R;
import app.dinhcuong.diplay.Service.APIService;
import app.dinhcuong.diplay.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SettingFragment extends Fragment {

    View view;
    TextView  text_name_user, text_email_user, button_edit_info, button_edit_password, button_about_app, button_language, button_logout, button_delete_account;
    Dialog dialog;
    int selectLanguage;

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
        button_edit_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogEditInfo();
            }
        });

        button_edit_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogEditPassword();
            }
        });

        button_about_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogAboutApp();
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
                openDialogLogout();

            }
        });

        button_delete_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogDeleteAccount();
            }
        });
    }

    private void openDialogDeleteAccount() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_confirm_delete_account, null);


        TextView button_cancel = dialogView.findViewById(R.id.button_cancel);
        TextView button_confirm = dialogView.findViewById(R.id.button_confirm);

        final EditText password_confirm = dialogView.findViewById(R.id.text_confirm_password);


        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password_confirm.getText().toString().trim().length() > 0){
                    SharedPreferences pref = getActivity().getSharedPreferences("Auth", getActivity().MODE_PRIVATE);
                    String password_user_SP = pref.getString("password_user","");
                    String id_user_SP = pref.getString("id_user","0");

                    if(password_confirm.getText().toString().equals(password_user_SP)){

                        DataService dataService = APIService.getService();
                        Call<String> callback = dataService.handlerDeleteUser(id_user_SP, "delete");
                        callback.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String result = response.body();
                                if(result.equals("SUCCESS")){

                                    Toast.makeText(getContext(), "Account deleted successfully!", Toast.LENGTH_SHORT).show();
                                    SharedPreferences settings = getContext().getSharedPreferences("Auth", Context.MODE_PRIVATE);
                                    settings.edit().clear().commit();
                                    Intent intent = new Intent(getContext(), LoginActivity.class);
                                    startActivity(intent);

                                    alertDialog.dismiss();
                                } else {
                                    Toast.makeText(getContext(), "Account deletion failed!", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "Incorrect password!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "You need enter your password to delete this account!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        alertDialog.getWindow().setContentView(dialogView);
        alertDialog.getWindow().setDimAmount(0.85f);
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private void openDialogLogout() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_confirm, null);


        TextView button_cancel = dialogView.findViewById(R.id.button_cancel);
        TextView button_confirm = dialogView.findViewById(R.id.button_confirm);
        TextView content = dialogView.findViewById(R.id.text_confirm);

        String text_content = getContext().getResources().getString(R.string.do_you_want_to_logout);
        content.setText(text_content);


        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getContext().getSharedPreferences("Auth", Context.MODE_PRIVATE);
                settings.edit().clear().commit();

                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });


        alertDialog.getWindow().setContentView(dialogView);
        alertDialog.getWindow().setDimAmount(0.85f);
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


    }

    private void openDialogEditPassword() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_setting_edit_profile_password, null);

        final EditText password_current = dialogView.findViewById(R.id.text_edit_input_current_password);
        final EditText password_new = dialogView.findViewById(R.id.text_edit_input_new_password);
        final EditText password_repeat = dialogView.findViewById(R.id.text_edit_input_repeat_password);

        TextView button_cancel = dialogView.findViewById(R.id.button_cancel);
        TextView button_update = dialogView.findViewById(R.id.button_update);

        SharedPreferences pref = getActivity().getSharedPreferences("Auth", getActivity().MODE_PRIVATE);
        String password_user_SP = pref.getString("password_user","");
        String id_user_SP = pref.getString("id_user","0");




        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password_current.getText().toString().trim().length() > 0
                        && password_new.getText().toString().trim().length() > 0
                        && password_repeat.getText().toString().trim().length() > 0){

                    if(password_current.getText().toString().equals(password_user_SP)){
                        if (!password_current.getText().toString().equals(password_new.getText().toString())){
                            if (password_new.getText().toString().equals(password_repeat.getText().toString())){

                                DataService dataService = APIService.getService();
                                Call<String> callback = dataService.handlerChangePassword(id_user_SP, password_new.getText().toString(), "changePassword");
                                callback.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        String result = response.body();
                                        if(result.equals("SUCCESS")){
                                            Toast.makeText(getContext(), "Password successfully changed!", Toast.LENGTH_SHORT).show();

                                            SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("Auth", getActivity().MODE_PRIVATE); // 0 - for private mode
                                            SharedPreferences.Editor editor = pref.edit();
                                            editor.putString("password_user", password_new.getText().toString());
                                            editor.commit();

                                            alertDialog.dismiss();
                                        } else {
                                            Toast.makeText(getContext(), "Password change failed!", Toast.LENGTH_SHORT).show();
                                            alertDialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {

                                    }
                                });
                            } else {
                                Toast.makeText(getContext(), "Repeated password does not match!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "New password matches current password!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Current password is incorrect!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "You need to fill in all the fields!", Toast.LENGTH_SHORT).show();
                }

            }
        });


        alertDialog.getWindow().setContentView(dialogView);
        alertDialog.getWindow().setDimAmount(0.85f);
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private void openDialogEditInfo() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_setting_edit_profile_info, null);


        final EditText name_user = dialogView.findViewById(R.id.text_edit_input_name);
        final EditText email_user = dialogView.findViewById(R.id.text_edit_input_email);
        final EditText password_current = dialogView.findViewById(R.id.text_edit_input_current_password);

        TextView button_cancel = dialogView.findViewById(R.id.button_cancel);
        TextView button_update = dialogView.findViewById(R.id.button_update);

        SharedPreferences pref = getActivity().getSharedPreferences("Auth", getActivity().MODE_PRIVATE);
        String name_user_SP = pref.getString("name_user","USER");
        String email_user_SP = pref.getString("email_user", "mail@diplay.com");
        String password_user_SP = pref.getString("password_user","");
        String id_user_SP = pref.getString("id_user","0");

        name_user.setText(name_user_SP);
        email_user.setText(email_user_SP);

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password_current.getText().toString().trim().length() > 0
                        && name_user.getText().toString().trim().length() > 0
                        && email_user.getText().toString().trim().length() > 0){

                    if(password_current.getText().toString().equals(password_user_SP)){
                        if (name_user.getText().toString().equals(name_user_SP) && email_user.getText().toString().equals(email_user_SP)){
                            Toast.makeText(getContext(), "No changes detected!", Toast.LENGTH_SHORT).show();
                        } else {
                            DataService dataService = APIService.getService();
                            Call<String> callback = dataService.handlerEditInfo(id_user_SP, name_user.getText().toString(), email_user.getText().toString(), "editInfo");
                            callback.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    String result = response.body();
                                    if(result.equals("SUCCESS")){
                                        Toast.makeText(getContext(), "Information changed successfully!", Toast.LENGTH_SHORT).show();

                                        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("Auth", getActivity().MODE_PRIVATE); // 0 - for private mode
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("email_user", email_user.getText().toString());
                                        editor.putString("name_user", name_user.getText().toString());
                                        editor.commit();

                                        updateInfo(name_user.getText().toString(),email_user.getText().toString());

                                        alertDialog.dismiss();
                                    } else {
                                        Toast.makeText(getContext(), "The information was changed unsuccessfully!", Toast.LENGTH_SHORT).show();
                                        alertDialog.dismiss();
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {

                                }
                            });
                        }
                    } else {
                        Toast.makeText(getContext(), "Current password is incorrect!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "You need to fill in all the fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        alertDialog.getWindow().setContentView(dialogView);
        alertDialog.getWindow().setDimAmount(0.85f);
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

    }

    private void mapping() {
        button_edit_info = view.findViewById(R.id.button_edit_profile_info);
        button_edit_password = view.findViewById(R.id.button_edit_profile_password);
        button_about_app = view.findViewById(R.id.button_about_app);
        button_language = view.findViewById(R.id.button_language);
        button_logout = view.findViewById(R.id.button_log_out);
        button_delete_account = view.findViewById(R.id.button_delete_account);
        text_name_user = view.findViewById(R.id.name_user);
        text_email_user = view.findViewById(R.id.email_user);
    }

    /*
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
    */
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
            Intent refresh = new Intent(getActivity(), MainActivity.class);
            getActivity().finish();
            startActivity(refresh);
        }
        if (selectLanguage == 1) {
            setLocale(getActivity(), "vi", 1);
            Intent refresh = new Intent(getActivity(), MainActivity.class);
            getActivity().finish();
            startActivity(refresh);
        }
        if (selectLanguage == 2) {
            setLocale(getActivity(), "ru", 2);
            Intent refresh = new Intent(getActivity(), MainActivity.class);
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

    private void openDialogAboutApp() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_setting_about_app, null);

        TextView button_hide = dialogView.findViewById(R.id.button_hide);

        button_hide.setOnClickListener(new View.OnClickListener() {
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

    private void updateInfo(String name_user, String email_user){
        text_name_user.setText(name_user);
        text_email_user.setText(email_user);
    }

}