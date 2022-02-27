package edu.vit.vtop.navapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import edu.vit.vtop.navapp.R;
import edu.vit.vtop.navapp.Utils.DataHandling;
import edu.vit.vtop.navapp.Utils.DataModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences mPrefs = getSharedPreferences("THEME", 0);
        String theme=mPrefs.getString("theme","");
        if (theme.equals("dark")) {
            // Set theme to white
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if(theme.equals("light")) {
            // Set theme to black
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }


        List<DataModel> list = new ArrayList<>();
        list.add(new DataModel("Silver Jubliee Tower","SJT",10.00,11.00,"Academic Blocks"));
        list.add(new DataModel("Technology Tower","TT",10.00,11.00,"Academic Blocks"));
        list.add(new DataModel("Food Court","FC",10.00,11.00,"Restaurants"));
        list.add(new DataModel("Cafe Coffee Day","CCD",10.00,11.00,"Coffee Shops"));
        list.add(new DataModel("HOD Scope","SJT",10.00,11.00,"Admin Offices"));
        list.add(new DataModel("Silver Jubliee Tower","SJT",10.00,11.00,"Academic Blocks"));
        list.add(new DataModel("Silver Jubliee Tower","SJT",10.00,11.00,"Academic Blocks"));
        DataHandling.saveList(list,MainActivity.this);
        startActivity(new Intent(MainActivity.this,HomeActivity.class));
        finish();
    }
}