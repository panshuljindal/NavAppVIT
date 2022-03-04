package edu.vit.vtop.navapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import edu.vit.vtop.navapp.R;

public class Appearance extends AppCompatActivity {
    private RadioGroup radioGroupThemeChanger;
    private RadioButton radioButtonLight, radioButtonDark;
    private ImageView back;
    private int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appearance);
        SharedPreferences.Editor editor = getSharedPreferences("edu.vit.vtop.navapp", MODE_PRIVATE).edit();
        radioGroupThemeChanger = findViewById(R.id.radio_group_theme_changer);
        radioButtonLight = findViewById(R.id.rb_light_mode);
        radioButtonDark = findViewById(R.id.rb_dark_mode);
        back = findViewById(R.id.noti_back);

        SharedPreferences.Editor edi = getSharedPreferences("edu.vit.vtop.navapp", MODE_PRIVATE).edit();
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                edi.putString("theme","dark");
                edi.apply();
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                edi.putString("theme","light");
                edi.apply();
                break;
        }
        String themeChosen = getSharedPreferences("edu.vit.vtop.navapp", MODE_PRIVATE)
                .getString("theme", "");
        if (themeChosen.equals("light")) {
            flag=1;
            radioButtonLight.setChecked(true);
        }
        else if (themeChosen.equals("dark")) {
            radioButtonDark.setChecked(true);
            flag=1;
        }
        try{
            radioGroupThemeChanger.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.rb_light_mode){
//                        radioButtonLight.setChecked(true);
                        editor.putString("theme", "light");
                        editor.apply();
                        flag=1;

                        Intent intent = new Intent(Appearance.this,Appearance.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        finish();
                    }
                    else if (checkedId == R.id.rb_dark_mode){
//                        radioButtonDark.setChecked(true);
                        editor.putString("theme", "dark");
                        editor.apply();
                        flag=1;

                        Intent intent = new Intent(Appearance.this,Appearance.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        finish();

                    }
                }
            });
        }
        catch (Exception e){
            Log.i("Theme error", e.getMessage());
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Appearance.this, Settings.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.i("flag",String.valueOf(flag));
        if(flag == 0){
            finish();
        }
        else{

            Intent intent = new Intent(Appearance.this, Settings.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}