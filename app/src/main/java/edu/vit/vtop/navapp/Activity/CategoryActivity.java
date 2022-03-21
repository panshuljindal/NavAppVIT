package edu.vit.vtop.navapp.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.List;

import edu.vit.vtop.navapp.NetworkUtils.NetworkUtil;
import edu.vit.vtop.navapp.R;
import edu.vit.vtop.navapp.Recyclerview.PlacesAdapter;
import edu.vit.vtop.navapp.Utils.DataHandling;
import edu.vit.vtop.navapp.Utils.DataModel;
import edu.vit.vtop.navapp.Utils.VersionModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity {

    private List<DataModel> list, sortedList;
    private ImageView back, cancel;
    private ProgressBar progressBar;
    double ulat,ulng;
    boolean isUserLocationNull = true;
    ActivityResultLauncher<String[]> locationPermissionRequest;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            locationPermissionRequest =
                    registerForActivityResult(new ActivityResultContracts
                                    .RequestMultiplePermissions(), result -> {
                                Boolean fineLocationGranted = result.getOrDefault(
                                        Manifest.permission.ACCESS_FINE_LOCATION, false);
                                Boolean coarseLocationGranted = result.getOrDefault(
                                        Manifest.permission.ACCESS_COARSE_LOCATION, false);
                                if (fineLocationGranted != null && fineLocationGranted) {
                                    // Precise location access granted.
                                } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                    // Only approximate location access granted.
                                } else {
                                    // No location access granted.
                                }
                            }
                    );
        }
        catch (Exception e){

        }
        setContentView(R.layout.activity_category);

        progressBar = findViewById(R.id.progressBar1);
        try {
            progressBar.setVisibility(View.VISIBLE);
            Intent i = getIntent();
            String category = i.getStringExtra("category");
            ulat = i.getDoubleExtra("ulat",0.0);
            ulng = i.getDoubleExtra("ulon",0.0);
            isUserLocationNull = i.getBooleanExtra("isUserLocationNull",true);
            RecyclerView categories = findViewById(R.id.cRecyclerView);
            list = new ArrayList<>();
            Call<List<DataModel>> call = NetworkUtil.networkAPI.getCategory(category);
            call.enqueue(new Callback<List<DataModel>>() {
                @Override
                public void onResponse(Call<List<DataModel>> call, Response<List<DataModel>> response) {
                    if (!response.isSuccessful()) {
                        progressBar.setVisibility(View.INVISIBLE);
                        return;
                    }
                    try {
                        list = response.body();
                        PlacesAdapter adapter = new PlacesAdapter(list, CategoryActivity.this,ulat,ulng,isUserLocationNull,locationPermissionRequest);
                        LinearLayoutManager manager = new LinearLayoutManager(CategoryActivity.this);
                        manager.setOrientation(RecyclerView.VERTICAL);
                        categories.setLayoutManager(manager);
                        categories.setAdapter(adapter);
                        TextView text = findViewById(R.id.cTextView);
                        text.setText(category);
                        progressBar.setVisibility(View.INVISIBLE);
                    } catch (Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<List<DataModel>> call, Throwable t) {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });

        } catch (Exception e) {
            progressBar.setVisibility(View.INVISIBLE);
        }
        setOnclick();
    }

    public void setOnclick() {
        back = findViewById(R.id.categoryBack);
        cancel = findViewById(R.id.categoryCancel);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(CategoryActivity.this,HomeActivity.class));
                finish();
            }
        });
    }
}