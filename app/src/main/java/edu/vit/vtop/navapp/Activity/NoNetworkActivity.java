package edu.vit.vtop.navapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.vit.vtop.navapp.Activity.MainActivity;
import edu.vit.vtop.navapp.R;

public class NoNetworkActivity extends AppCompatActivity {
    private Button tryagain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_network);

        tryagain = findViewById(R.id.clubTryAgain);
        tryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }
}