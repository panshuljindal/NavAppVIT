package edu.vit.vtop.navapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

    List<DataModel> list,sortedList;
    ImageView back,cancel;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        progressDialog = new ProgressDialog(getApplicationContext());
        try {
            progressDialog.show();
            Intent i = getIntent();
            String category = i.getStringExtra("category");

            TextView text = findViewById(R.id.cTextView);
            text.setText(category);
            RecyclerView categories = findViewById(R.id.cRecyclerView);
            list=new ArrayList<>();
            Call<List<DataModel>> call = NetworkUtil.networkAPI.getCategory(category);
            call.enqueue(new Callback<List<DataModel>>() {
                @Override
                public void onResponse(Call<List<DataModel>> call, Response<List<DataModel>> response) {
                    if(!response.isSuccessful()){
                        progressDialog.dismiss();
                        return;
                    }
                    try {
                        list=response.body();
                        PlacesAdapter adapter = new PlacesAdapter(list,CategoryActivity.this);
                        LinearLayoutManager manager = new LinearLayoutManager(CategoryActivity.this);
                        manager.setOrientation(RecyclerView.VERTICAL);
                        categories.setLayoutManager(manager);
                        categories.setAdapter(adapter);
                        progressDialog.dismiss();
                    }catch (Exception e){
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<List<DataModel>> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });

        }catch (Exception e){
            progressDialog.dismiss();
        }
        setOnclick();
    }
    public void setOnclick(){
        back=findViewById(R.id.categoryBack);
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