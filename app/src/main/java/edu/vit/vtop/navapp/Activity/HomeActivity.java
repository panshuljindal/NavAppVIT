package edu.vit.vtop.navapp.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.vit.vtop.navapp.R;
import edu.vit.vtop.navapp.Recyclerview.CategoriesAdapter;
import edu.vit.vtop.navapp.Recyclerview.PlacesAdapter;
import edu.vit.vtop.navapp.RecyclerviewModels.CategoriesModel;
import edu.vit.vtop.navapp.Utils.DataHandling;
import edu.vit.vtop.navapp.Utils.DataModel;
import edu.vit.vtop.navapp.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityHomeBinding binding;
    private BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView categories,places,searchRecyclerview;
    private List<CategoriesModel> categoriesList;
    private List<DataModel> placesList;
    private EditText search;
    private TextView cat,plac;
    ConstraintLayout bottomSheetLayout;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityResultLauncher<String[]> locationPermissionRequest =
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
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // get the bottom sheet view
        bottomSheetLayout = findViewById(R.id.bottom_sheet);

        search=findViewById(R.id.searchEditText);
        categories = findViewById(R.id.categoriesRecyclerView);
        places=findViewById(R.id.placesRecyclerView);
        cat=findViewById(R.id.categoriesTextView);
        plac=findViewById(R.id.placesTextView);
        searchRecyclerview=findViewById(R.id.searchRecyclerView);
        categoriesList=new ArrayList<>();
        placesList=new ArrayList<>();

        addCategories();
        addPlaces();
        Search();

        // init the bottom sheet behavior
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);

//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
//                        Toast.makeText(getApplicationContext(),"STATE HIDDEN",Toast.LENGTH_LONG).show();
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
//                        Toast.makeText(getApplicationContext(),"STATE EXPANDED",Toast.LENGTH_LONG).show();
                        // update toggle button text
                        bottomSheetLayout.setBackground(ContextCompat.getDrawable(HomeActivity.this,R.drawable.bottom_sheet_back));
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
//                        Toast.makeText(getApplicationContext(),"STATE COLLAPSED",Toast.LENGTH_LONG).show();
                        // update collapsed button text
                        bottomSheetLayout.setBackground(ContextCompat.getDrawable(HomeActivity.this,R.drawable.bottom_sheet_background));

                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
//                        Toast.makeText(getApplicationContext(),"STATE DRAGGING",Toast.LENGTH_LONG).show();

                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
//                        Toast.makeText(getApplicationContext(),"STATE SETTLING",Toast.LENGTH_LONG).show();
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        binding.changeTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences mPrefs = getSharedPreferences("THEME", 0);
                SharedPreferences.Editor mEditor = mPrefs.edit();


                switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                    case Configuration.UI_MODE_NIGHT_YES:

                        mEditor.putString("theme", "light").apply();
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        break;
                    case Configuration.UI_MODE_NIGHT_NO:
//
                        mEditor.putString("theme", "dark").apply();
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        break;
                }





            }
        });
        binding.userLocationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                locationPermissionRequest.launch(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION

                });
                LatLng center = mMap.getCameraPosition().target;
                String sLatitude = String.format("%.6f", center.latitude);
                String sLongitude = String.format("%.6f", center.longitude);
                StringBuilder mLatLng = new StringBuilder();
                mLatLng.append(sLatitude);
                mLatLng.append("°");
                mLatLng.append(" ");
                mLatLng.append(sLongitude);
                mLatLng.append("°");


                Location location = getLastKnownLocation();
                if (location != null) {

                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
//                    mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    mMap.getUiSettings().setAllGesturesEnabled(true);
                    //delay is for after map loaded animation starts
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));

                        }
                    }, 2000);
                }
            }
        });
    }
    private Location getLastKnownLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission") Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.map_style));

        // Create a LatLngBounds that includes the VIT Campus bounds
        LatLngBounds vitBounds = new LatLngBounds(
                new LatLng(12.967077, 79.152291), // SW bounds
                new LatLng(12.978755, 79.167387)  // NE bounds
        );


// Constrain the camera target to the VIT Campus bounds.
        mMap.setLatLngBoundsForCameraTarget(vitBounds);
        mMap.setMinZoomPreference(16.0f); // Set a preference for minimum zoom (Zoom out).


        // Add a marker in VIT and move the camera
        LatLng vit = new LatLng(12.974714, 79.164227);
        mMap.addMarker(new MarkerOptions().position(vit).title("Admin")
                .icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_marker_admin)));
         mMap.addMarker(new MarkerOptions().position(new LatLng(12.974512,79.164327)).title("Academic")
                .icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_marker_academic)));
         mMap.addMarker(new MarkerOptions().position(new LatLng(12.974912,79.164127)).title("Coffee")
                .icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_marker_coffee)));
         mMap.addMarker(new MarkerOptions().position(new LatLng(12.974522,79.164357)).title("Hostel")
                .icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_marker_hostel)));
         mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
             @Override
             public boolean onMarkerClick(@NonNull Marker marker) {
                 Intent i = new Intent(HomeActivity.this, NavigationActivity.class);
                 i.putExtra("lat", marker.getPosition().latitude);
                 i.putExtra("long", marker.getPosition().longitude);
                 startActivity(i);
                 return true;
             }
         });
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vit, 15f));
    }


    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    void addCategories(){
        categoriesList.add(new CategoriesModel("Restaurants",R.color.restaurant,R.drawable.ic_restaurant));
        categoriesList.add(new CategoriesModel("Shopping",R.color.shopping,R.drawable.ic_shopping));
        categoriesList.add(new CategoriesModel("Hostel Blocks",R.color.hostel,R.drawable.ic_hostel));
        categoriesList.add(new CategoriesModel("Coffee Shops",R.color.coffee,R.drawable.ic_coffee));
        categoriesList.add(new CategoriesModel("Admin Offices",R.color.admin,R.drawable.ic_admin));
        categoriesList.add(new CategoriesModel("Academic Blocks",R.color.academic,R.drawable.ic_academics));
        Collections.reverse(categoriesList);
        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(categoriesList,getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(RecyclerView.HORIZONTAL);
        categories.setAdapter(categoriesAdapter);
        categories.setLayoutManager(manager);
    }
    void addPlaces(){
        placesList= DataHandling.getList(HomeActivity.this);
        PlacesAdapter adapter = new PlacesAdapter(placesList,getApplicationContext());
        LinearLayoutManager manager1 = new LinearLayoutManager(getApplicationContext());
        manager1.setOrientation(RecyclerView.VERTICAL);
        places.setAdapter(adapter);
        places.setLayoutManager(manager1);
    }
    void Search(){
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchString = search.getText().toString();
                if(searchString==""){
                    cat.setVisibility(View.VISIBLE);
                    plac.setText("Places");
                    places.setVisibility(View.VISIBLE);
                    categories.setVisibility(View.VISIBLE);
                    searchRecyclerview.setVisibility(View.INVISIBLE);
                }else {
                    cat.setVisibility(View.INVISIBLE);
                    plac.setText("Results");
                    places.setVisibility(View.INVISIBLE);
                    categories.setVisibility(View.INVISIBLE);
                    searchRecyclerview.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!bottomSheetBehavior.equals(BottomSheetBehavior.STATE_EXPANDED)){
                    bottomSheetLayout.setState(BottomSheetBehavior.STATE_EXPANDED,0,0);
                }
                String searchString = search.getText().toString();
                if(searchString.equals("")){
                    cat.setVisibility(View.VISIBLE);
                    plac.setText("Places");
                    places.setVisibility(View.VISIBLE);
                    categories.setVisibility(View.VISIBLE);
                    searchRecyclerview.setVisibility(View.INVISIBLE);
                }else{
                    cat.setVisibility(View.GONE);
                    plac.setText("Results");
                    places.setVisibility(View.GONE);
                    categories.setVisibility(View.GONE);
                    searchRecyclerview.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(search.getText().toString().equals(searchString)){
                                List<DataModel> list = DataHandling.searchData(searchString);
                                PlacesAdapter adapter = new PlacesAdapter(list,getApplicationContext());
                                LinearLayoutManager manager1 = new LinearLayoutManager(getApplicationContext());
                                manager1.setOrientation(RecyclerView.VERTICAL);
                                searchRecyclerview.setAdapter(adapter);
                                searchRecyclerview.setLayoutManager(manager1);
                            }
                        }
                    },400);

                }
            }
        });
    }

}