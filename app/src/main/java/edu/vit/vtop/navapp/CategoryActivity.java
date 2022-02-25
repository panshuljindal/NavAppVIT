package edu.vit.vtop.navapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

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