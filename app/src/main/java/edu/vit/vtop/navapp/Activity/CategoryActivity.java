package edu.vit.vtop.navapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.vit.vtop.navapp.R;
import edu.vit.vtop.navapp.Recyclerview.PlacesAdapter;
import edu.vit.vtop.navapp.Utils.DataHandling;
import edu.vit.vtop.navapp.Utils.DataModel;

public class CategoryActivity extends AppCompatActivity {

    List<DataModel> list,sortedList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        try {
            Intent i = getIntent();
            String category = i.getStringExtra("category");
            RecyclerView categories = findViewById(R.id.cRecyclerView);
            list=new ArrayList<>();
            sortedList=new ArrayList<>();
            list= DataHandling.getList(CategoryActivity.this);
            for(DataModel model:list){
                if(model.getCategory().equals(category)){
                    sortedList.add(model);
                }
            }
            PlacesAdapter adapter = new PlacesAdapter(sortedList,CategoryActivity.this);
            LinearLayoutManager manager = new LinearLayoutManager(CategoryActivity.this);
            manager.setOrientation(RecyclerView.VERTICAL);
            categories.setLayoutManager(manager);
            categories.setAdapter(adapter);
            TextView text = findViewById(R.id.cTextView);
            text.setText(category);
        }catch (Exception e){

        }
    }
}