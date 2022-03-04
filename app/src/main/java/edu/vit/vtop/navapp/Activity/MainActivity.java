package edu.vit.vtop.navapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.maps.model.MapStyleOptions;

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
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("edu.vit.vtop.navapp", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        double version = Double.valueOf(sharedPreferences.getString("version", "-1"));
        Log.i("version",String.valueOf(version));

        Call<VersionModel> versionCall = NetworkUtil.networkAPI.getVersion();
        versionCall.enqueue(new Callback<VersionModel>() {

            @Override
            public void onResponse(Call<VersionModel> call, Response<VersionModel> response) {
                if (!response.isSuccessful()) {
//                    Toast.makeText(getApplicationContext(), "Version not success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, NoNetworkActivity.class));
                    finish();
                    return;
                }
                if(response.body().getVersion() > version){
                    editor.putString("version", String.valueOf(response.body().getVersion()));
                    Log.i("version",String.valueOf(response.body().getVersion()));

                    editor.apply();
//                    Toast.makeText(getApplicationContext(), "Version success", Toast.LENGTH_SHORT).show();

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
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                        finish();
                                    }
                                },700);
                            }

                            @Override
                            public void onFailure(Call<List<DataModel>> call, Throwable t) {
                                Log.i("DataModel: ", "error");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        Intent intent = new Intent(getApplicationContext(), NoNetworkActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                },700);
//                                Toast.makeText(getApplicationContext(), "DataModel error", Toast.LENGTH_SHORT).show();
                            }
                        });

                }
                else{

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
                            finish();
                        }
                    },900);

                }
            }

            @Override
            public void onFailure(Call<VersionModel> call, Throwable t) {
                Log.i("Version: ", "fail");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), NoNetworkActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },1100);

//                Toast.makeText(getApplicationContext(), "Version error", Toast.LENGTH_SHORT).show();
            }
        });


        SharedPreferences mPrefs = getSharedPreferences("edu.vit.vtop.navapp", MODE_PRIVATE);
        SharedPreferences.Editor editor1 = mPrefs.edit();
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                editor1.putString("theme","dark");
                editor1.apply();
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                editor1.putString("theme","light");
                editor1.apply();
                break;
        }
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