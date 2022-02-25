package edu.vit.vtop.navapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import edu.vit.vtop.navapp.R;

public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        try {
            Intent i = getIntent();
            String category = i.getStringExtra("category");
        }catch (Exception e){

        }
    }
}