package edu.vit.vtop.navapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.vit.vtop.navapp.R;
import edu.vit.vtop.navapp.Recyclerview.CategoriesAdapter;
import edu.vit.vtop.navapp.Recyclerview.PlacesAdapter;
import edu.vit.vtop.navapp.RecyclerviewModels.CategoriesModel;
import edu.vit.vtop.navapp.Utils.DataModel;

public class BottomSheet extends AppCompatActivity {

    RecyclerView categories,places;
    List<CategoriesModel> categoriesList;
    List<DataModel> placesList;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheet);
        categories = findViewById(R.id.categoriesRecyclerView);
        places=findViewById(R.id.placesRecyclerView);
        categoriesList=new ArrayList<>();
        placesList=new ArrayList<>();

        context=BottomSheet.this;
        addCategories();
        addPlaces();
    }
    void addCategories(){
        categoriesList.add(new CategoriesModel("Restaurants",R.color.restaurant,R.drawable.ic_restaurant));
        categoriesList.add(new CategoriesModel("Shopping",R.color.shopping,R.drawable.ic_shopping));
        categoriesList.add(new CategoriesModel("Hostel Blocks",R.color.hostel,R.drawable.ic_hostel));
        categoriesList.add(new CategoriesModel("Coffee Shops",R.color.coffee,R.drawable.ic_coffee));
        categoriesList.add(new CategoriesModel("Admin Offices",R.color.admin,R.drawable.ic_admin));
        categoriesList.add(new CategoriesModel("Academic Blocks",R.color.academic,R.drawable.ic_academics));
        Collections.reverse(categoriesList);
        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(categoriesList,context);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(RecyclerView.HORIZONTAL);
        categories.setAdapter(categoriesAdapter);
        categories.setLayoutManager(manager);
    }
    void addPlaces(){
//        placesList.add(new PlacesModel("SJT","Academic Block"));
//        placesList.add(new PlacesModel("SW Office","Admin Offices"));
//        placesList.add(new PlacesModel("CCD","Coffee Shops"));
//        placesList.add(new PlacesModel("M Block","Hostel Blocks"));
//        placesList.add(new PlacesModel("All Mart","Shopping"));
//        placesList.add(new PlacesModel("FC","Restaurant"));
        PlacesAdapter adapter = new PlacesAdapter(placesList,context);
        LinearLayoutManager manager1 = new LinearLayoutManager(context);
        manager1.setOrientation(RecyclerView.VERTICAL);
        places.setAdapter(adapter);
        places.setLayoutManager(manager1);
    }
}