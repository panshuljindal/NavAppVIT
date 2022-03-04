package edu.vit.vtop.navapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import edu.vit.vtop.navapp.R;
import edu.vit.vtop.navapp.Recyclerview.SettingsAdapter;
import edu.vit.vtop.navapp.RecyclerviewModels.SettingsModel;

public class Settings extends AppCompatActivity {

    private RecyclerView recy2,recy3;
    private List<SettingsModel> lst3,lst2;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        lst3 = new ArrayList<>();
        lst2 = new ArrayList<>();
        context = Settings.this;
        recy2 = findViewById(R.id.rcy2);
        recy3=findViewById(R.id.rcy3);

        recy2.setLayoutManager(new LinearLayoutManager(context));
        recy3.setLayoutManager(new LinearLayoutManager(context));
        lst2.add(new SettingsModel(R.drawable.ic_appearance,"Appearance"));

        lst2.add(new SettingsModel(R.drawable.ic_contact_us,"Contact Us"));
        lst2.add(new SettingsModel(R.drawable.ic_share,"Share with Peers"));
        lst2.add(new SettingsModel(R.drawable.ic_ig,"Our Instagram"));
        lst2.add(new SettingsModel(R.drawable.ic_fb,"Our Facebook"));

        recy2.setAdapter(new SettingsAdapter(context,lst2));

        lst3.add(new SettingsModel(R.drawable.ic_privacy,"Privacy Policy"));
        lst3.add(new SettingsModel(R.drawable.ic_aboutus,"About Us"));

        recy3.setAdapter(new SettingsAdapter(context,lst3));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Settings.this,HomeActivity.class));
        finish();

    }
}