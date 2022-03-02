package edu.vit.vtop.navapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.vit.vtop.navapp.NetworkUtils.NetworkUtil;
import edu.vit.vtop.navapp.R;
import edu.vit.vtop.navapp.Utils.DataHandling;
import edu.vit.vtop.navapp.Utils.DataModel;
import edu.vit.vtop.navapp.Utils.VersionModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences sharedPreferences = getSharedPreferences("Version", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
//        int version = -1;
        int version = sharedPreferences.getInt("version", -1);
        Call<List<VersionModel>> versionCall = NetworkUtil.networkAPI.getVersion("sj");
        versionCall.enqueue(new Callback<List<VersionModel>>() {

            @Override
            public void onResponse(Call<List<VersionModel>> call, Response<List<VersionModel>> response) {
                if (!response.isSuccessful()) {
                    Log.i("Version: ", "Not Successfull");
//                    Toast.makeText(getApplicationContext(), "Version not success", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(response.body().get(0).get__v() > version){
                    editor.putInt("version",response.body().get(0).get__v());
                    editor.apply();
//                    Toast.makeText(getApplicationContext(), "Version success", Toast.LENGTH_SHORT).show();
                    Log.i("Version: ", "Successfull");

                        Call<List<DataModel>> dataCall = NetworkUtil.networkAPI.getIndependent();

                        dataCall.enqueue(new Callback<List<DataModel>>() {
                            @Override
                            public void onResponse(Call<List<DataModel>> call, Response<List<DataModel>> response) {
                                if (!response.isSuccessful()) {
//                                    Log.i("Datamodel: ", "Not Successfull");
//                                    Toast.makeText(getApplicationContext(), "DataModel not success", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                for(int i=0;i<=5;i++){
                                    DataHandling.addPlace(response.body().get(i),MainActivity.this);
                                }
                                DataHandling.saveList(response.body(),MainActivity.this);
//                                Log.i("DataModel: ", "Successfull");
//                                Toast.makeText(getApplicationContext(), "DataModel Success", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                finish();
                            }

                            @Override
                            public void onFailure(Call<List<DataModel>> call, Throwable t) {
                                Log.i("DataModel: ", "error");
//                                Toast.makeText(getApplicationContext(), "DataModel error", Toast.LENGTH_SHORT).show();
                            }
                        });

                }
                else{

                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<List<VersionModel>> call, Throwable t) {
                Log.i("Version: ", "fail");
                Toast.makeText(getApplicationContext(), "Version error", Toast.LENGTH_SHORT).show();
            }
        });


        SharedPreferences mPrefs = getSharedPreferences("THEME", 0);
        String theme=mPrefs.getString("theme","");
        if (theme.equals("dark")) {
            // Set theme to white
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if(theme.equals("light")) {
            // Set theme to black
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }


    }



}