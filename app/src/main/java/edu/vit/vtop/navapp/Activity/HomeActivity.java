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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import edu.vit.vtop.navapp.Adapter.MarkerInfoWindowAdapter;
import edu.vit.vtop.navapp.NetworkUtils.NetworkUtil;
import edu.vit.vtop.navapp.R;
import edu.vit.vtop.navapp.Recyclerview.CategoriesAdapter;
import edu.vit.vtop.navapp.Recyclerview.PlacesAdapter;
import edu.vit.vtop.navapp.RecyclerviewModels.CategoriesModel;
import edu.vit.vtop.navapp.Utils.DataHandling;
import edu.vit.vtop.navapp.Utils.DataModel;
import edu.vit.vtop.navapp.databinding.ActivityHomeBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityHomeBinding binding;
    private BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView categories,places,searchRecyclerview;
    private List<CategoriesModel> categoriesList;
    private List<DataModel> placesList;
    private EditText search;
    private TextView cat,plac;
    private ProgressBar progressBar;
    private List<DataModel> list;
    private TextView noResult;
    private ImageView cancelSearch;
    private ConstraintLayout bottomSheetLayout;
    private ActivityResultLauncher<String[]> locationPermissionRequest;
    private SharedPreferences sharedpreferences;
    // Create a LatLngBounds that includes the VIT Campus bounds
    private LatLngBounds vitBounds;
    //    SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try
        {

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
            binding = ActivityHomeBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());


            progressBar = findViewById(R.id.progressBar);

            locationPermissionRequest.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION

            });

            // get the bottom sheet view
            bottomSheetLayout = findViewById(R.id.bottom_sheet);

            search=findViewById(R.id.searchEditText);
            categories = findViewById(R.id.categoriesRecyclerView);
            places=findViewById(R.id.placesRecyclerView);
            cat=findViewById(R.id.categoriesTextView);
            plac=findViewById(R.id.placesTextView);
            searchRecyclerview=findViewById(R.id.searchRecyclerView);
            noResult=findViewById(R.id.no_Search);
            cancelSearch=findViewById(R.id.cancelSearch);
            categoriesList=new ArrayList<>();
            placesList=new ArrayList<>();

            addCategories();
            addPlaces();
            Search();

            // init the bottom sheet behavior
            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);

            vitBounds = new LatLngBounds(
                    new LatLng(12.967077, 79.152291), // SW bounds
                    new LatLng(12.978755, 79.167387)  // NE bounds
            );

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

        }
        catch (Exception e)
        {
            Log.i("Exception",e.getLocalizedMessage());
//            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), NoNetworkActivity.class);
            startActivity(intent);
            finish();
        }

        binding.changeTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,Settings.class));
                finish();

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
                String sLatitude = String.format("%.16f", center.latitude);
                String sLongitude = String.format("%.16f", center.longitude);
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
//                    if (!vitBounds.contains(new LatLng(location.getLatitude(),location.getLongitude()))) {
//                        Toast.makeText(getApplicationContext(), R.string.onlyVIT, Toast.LENGTH_LONG).show();
////                        editor.putBoolean("isOnCampus",true).commit();
////                        editor.apply();
//                    }
//                    else {
                        mMap.setMyLocationEnabled(true);
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        mMap.getUiSettings().setAllGesturesEnabled(true);
                        //delay is for after map loaded animation starts
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));

                            }
                        }, 2000);
//                    }
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

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            public void onMapLoaded() {
            }
        });

        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:

                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.map_style_night));
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.map_style));
                break;
        }


// Constrain the camera target to the VIT Campus bounds.
        mMap.setLatLngBoundsForCameraTarget(vitBounds);
        mMap.setMinZoomPreference(16.0f); // Set a preference for minimum zoom (Zoom out).



        LatLngBounds.Builder builder = new LatLngBounds.Builder();



        List<DataModel> markers = DataHandling.getList(getApplicationContext());

        // Setting a custom info window adapter for the google map
        MarkerInfoWindowAdapter markerInfoWindowAdapter = new MarkerInfoWindowAdapter(getApplicationContext(),markers);
        mMap.setInfoWindowAdapter(markerInfoWindowAdapter);

        for(DataModel e: markers) {

            int vector = 0;
            switch (e.getCategory()) {
                case "Academic Blocks":
                    vector = R.drawable.ic_marker_academic;
                    break;

                case "Hostel Blocks":
                    vector = R.drawable.ic_marker_hostel;
                    break;
                case "Shops":
                    vector = R.drawable.ic_marker_shop;
                    break;
                case "Coffee Shops":
                    vector = R.drawable.ic_marker_coffee;
                    break;
                case "Halls":
                    // change to hall
                    vector = R.drawable.ic_marker_hall;
                    break;
                case "Sports":
                    // change to sports
                    vector = R.drawable.ic_marker_sports;
                    break;
                case "Gates":
                    // change to gate
                    vector = R.drawable.ic_marker_gates;
                    break;

                case "Utilities":
                    vector = R.drawable.ic_marker_utilities;
                    break;
                case "ATMs":
                    vector=R.drawable.ic_marker_atm;
                    break;
                case "Pickup Points":
                    vector=R.drawable.ic_marker_courier;
                    break;
                case "Restaurants":
                    vector = R.drawable.ic_marker_food;
                    break;
                case "Administrative Offices":
                    vector = R.drawable.ic_marker_admin;
                    break;
                default:
                    vector = R.drawable.ic_marker_academic;
                    break;


            }
            mMap.addMarker(new MarkerOptions().position(new LatLng(e.getLat(), e.getLon())).title(e.getName())
                    .icon(BitmapFromVector(getApplicationContext(), vector)));
            //the include method will calculate the min and max bound.
            LatLng loc = new LatLng(e.getLat(),e.getLon());

            builder.include(loc);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    Location lkl = getLastKnownLocation();

                    if(lkl!=null)
                    {
                        if (!vitBounds.contains(new LatLng(lkl.getLatitude(),lkl.getLongitude()))) {
                            SharedPreferences pref = getSharedPreferences("edu.vit.vtop.navapp",MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putBoolean("isOnCampus",false).commit();
                            editor.apply();
                        }

                        else
                        {
                            SharedPreferences pref = getSharedPreferences("edu.vit.vtop.navapp",MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putBoolean("isOnCampus",true).commit();
                            editor.apply();
                            mMap.setMyLocationEnabled(true);
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                            mMap.getUiSettings().setAllGesturesEnabled(true);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lkl.getLatitude(), lkl.getLongitude()), 15));
                            //delay is for after map loaded animation starts

                        }
                    }
                }
            }
        },2000);


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                for(DataModel e : markers)
                {
                    // The case when, we click on one marker to show the info window, and then click on another. This would keep the e.getInfoShown() to true.
                    if((marker.getPosition().longitude != e.getLon() || marker.getPosition().latitude != e.getLat()) && e.getInfoShown())
                    {
                        e.setInfoShown(false);
                    }
                }
                for(DataModel e : markers)
                {
                    if(marker.getPosition().longitude == e.getLon() && marker.getPosition().latitude == e.getLat())
                    {
                        if(!e.getInfoShown())
                        {
                            marker.showInfoWindow();
                            e.setInfoShown(true);
//                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(e.getLat(), e.getLon()), 18f));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(e.getLat(), e.getLon()), 16.5f));
                            //When we return false in onMarkerClick(), it performs its default function of showingInfoWindow and centering the map into the marker
                            return false;
//                                LatLng coordinate = new LatLng(e.getLon(), e.getLat());
//                                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 11.0f);
//                                mMap.animateCamera(yourLocation);
                        }
                        else
                        {
                            locationPermissionRequest.launch(new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION

                            });
                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                Location location = getLastKnownLocation();

                                if(location!=null)
                                {
//                                    if (!vitBounds.contains(new LatLng(location.getLatitude(),location.getLongitude()))) {
//                                        Toast.makeText(getApplicationContext(), R.string.onlyVIT, Toast.LENGTH_LONG).show();
//                                    }
//                                    else {
                                        marker.hideInfoWindow();
                                        e.setInfoShown(false);

//                                    Toast.makeText(HomeActivity.this, location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_SHORT).show();

                                        Intent i = new Intent(HomeActivity.this, NavigationActivity.class);
                                        i.putExtra("ulat", location.getLatitude());
                                        i.putExtra("ulon", location.getLongitude());
                                        i.putExtra("marker_object", e);
                                        startActivity(i);
//                                    }
                                }
                                else
                                {
                                    Toast.makeText(HomeActivity.this, "Unable to access your location", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(HomeActivity.this, "Please accept the location permissions", Toast.LENGTH_SHORT).show();
                                locationPermissionRequest.launch(new String[]{
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION

                                });
                            }
                        }
                    }
                }
                return true;
            }
        });
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setAllGesturesEnabled(true);

        }
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vit, 15f));

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

        categoriesList.add(new CategoriesModel("Halls",R.color.hall,R.drawable.ic_hall));
        categoriesList.add(new CategoriesModel("Gates",R.color.gates,R.drawable.ic_gates));
        categoriesList.add(new CategoriesModel("Sports",R.color.sports,R.drawable.ic_sports));
        categoriesList.add(new CategoriesModel("Utilities",R.color.utilities,R.drawable.ic_utilities));


        categoriesList.add(new CategoriesModel("Pickup Points",R.color.courier,R.drawable.ic_courier));
        categoriesList.add(new CategoriesModel("ATMs",R.color.bank,R.drawable.ic_bank));
        categoriesList.add(new CategoriesModel("Restaurants",R.color.restaurant,R.drawable.ic_restaurant));

        categoriesList.add(new CategoriesModel("Hostel Blocks",R.color.hostel,R.drawable.ic_hostel));
        categoriesList.add(new CategoriesModel("Coffee Shops",R.color.coffee,R.drawable.ic_coffee));
        categoriesList.add(new CategoriesModel("Shops",R.color.shopping,R.drawable.ic_shopping));
        categoriesList.add(new CategoriesModel("Administrative Offices",R.color.admin,R.drawable.ic_admin));
        categoriesList.add(new CategoriesModel("Academic Blocks",R.color.academic,R.drawable.ic_academic));
        Collections.reverse(categoriesList);

        Location lkl = getLastKnownLocation();


//        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            Location location = getLastKnownLocation();
//
//            if (location != null) {
//                if (!vitBounds.contains(new LatLng(location.getLatitude(), location.getLongitude()))) {
//                    editor.putBoolean("isOnCampus", false);
//                } else {
//                    editor.putBoolean("isOnCampus", true);
//                }
//            }
//        }

        boolean isUserLocationNull = false;
        double ulat1 = 0.0;
        double ulng1 = 0.0;
        if(lkl == null){isUserLocationNull = true;}
        else{ulat1 = lkl.getLatitude();
            ulng1 = lkl.getLongitude();}
        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(categoriesList,HomeActivity.this,ulat1,ulng1,isUserLocationNull);
        LinearLayoutManager manager = new LinearLayoutManager(HomeActivity.this);
        manager.setOrientation(RecyclerView.HORIZONTAL);
        categories.setAdapter(categoriesAdapter);
        categories.setLayoutManager(manager);

    }
    void addPlaces(){
        Location lkl = getLastKnownLocation();

        placesList = DataHandling.getPlaces(HomeActivity.this);

//        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            Location location = getLastKnownLocation();
//
//            if (location != null) {
//                if (!vitBounds.contains(new LatLng(location.getLatitude(), location.getLongitude()))) {
//                    editor.putBoolean("isOnCampus", false);
//                } else {
//                    editor.putBoolean("isOnCampus", true);
//                }
//            }
//        }
        boolean isUserLocationNull = false;
        double ulat1 = 0.0;
        double ulng1 = 0.0;
        if(lkl == null){isUserLocationNull = true;}
        else{ulat1 = lkl.getLatitude();
            ulng1 = lkl.getLongitude();}
        PlacesAdapter adapter = new PlacesAdapter(placesList, HomeActivity.this,ulat1,ulng1,isUserLocationNull,locationPermissionRequest);
        LinearLayoutManager manager1 = new LinearLayoutManager(HomeActivity.this);
        manager1.setOrientation(RecyclerView.VERTICAL);
        places.setAdapter(adapter);
        places.setLayoutManager(manager1);

    }
    void Search(){
        cancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.GONE);
                cat.setVisibility(View.VISIBLE);
                plac.setText("Places");
                places.setVisibility(View.VISIBLE);
                categories.setVisibility(View.VISIBLE);
                noResult.setVisibility(View.INVISIBLE);
                cancelSearch.setVisibility(View.GONE);
                searchRecyclerview.setVisibility(View.INVISIBLE);
                search.setText("");
            }
        });
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
                    progressBar.setVisibility(View.GONE);
                }else {
                    cat.setVisibility(View.INVISIBLE);
                    plac.setText("Results");
                    places.setVisibility(View.INVISIBLE);
                    categories.setVisibility(View.INVISIBLE);
                    searchRecyclerview.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!bottomSheetBehavior.equals(BottomSheetBehavior.STATE_EXPANDED)){
//                    bottomSheetLayout.setState(BottomSheetBehavior.STATE_EXPANDED,100,100);
//                    Log.i("Pressed","Bottom");
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    progressBar.setVisibility(View.GONE);
                }
                String searchString = search.getText().toString();
                searchRecyclerview.setVisibility(View.GONE);
                noResult.setVisibility(View.INVISIBLE);
                if(searchString.equals("")){
                    progressBar.setVisibility(View.GONE);
                    cat.setVisibility(View.VISIBLE);
                    plac.setText("Places");
                    places.setVisibility(View.VISIBLE);
                    categories.setVisibility(View.VISIBLE);
                    noResult.setVisibility(View.INVISIBLE);
                    cancelSearch.setVisibility(View.GONE);
                    searchRecyclerview.setVisibility(View.INVISIBLE);
                }else{
                    cancelSearch.setVisibility(View.VISIBLE);
//                    progressDialog.show();
                    progressBar.setVisibility(View.VISIBLE);


                    cat.setVisibility(View.GONE);
                    plac.setText("Results");
                    places.setVisibility(View.GONE);
                    categories.setVisibility(View.GONE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(search.getText().toString().equals(searchString)){

                                Call<List<DataModel>> call = NetworkUtil.networkAPI.search(searchString);
                                call.enqueue(new Callback<List<DataModel>>() {
                                    @Override
                                    public void onResponse(Call<List<DataModel>> call, Response<List<DataModel>> response) {
                                        if(!response.isSuccessful()){
                                            progressBar.setVisibility(View.GONE);
                                            return ;
                                        }
                                        list=new ArrayList<>();
                                        try {
                                            list=response.body().subList(0,8);
                                        } catch (Exception e){
                                            list=response.body();
                                        }
                                        if(list.size()==0){
                                            progressBar.setVisibility(View.GONE);
                                            noResult.setVisibility(View.VISIBLE);
                                        }else{
                                            Location lkl = getLastKnownLocation();
                                            boolean isUserLocationNull = false;
                                            double ulat1 = 0.0;
                                            double ulng1 = 0.0;
                                            if(lkl == null){isUserLocationNull = true;}
                                            else{ulat1 = lkl.getLatitude();
                                                ulng1 = lkl.getLongitude();}
                                            PlacesAdapter adapter = new PlacesAdapter(list, HomeActivity.this,ulat1,ulng1,isUserLocationNull,locationPermissionRequest);
                                            LinearLayoutManager manager1 = new LinearLayoutManager(getApplicationContext());
                                            manager1.setOrientation(RecyclerView.VERTICAL);
                                            searchRecyclerview.setAdapter(adapter);
                                            searchRecyclerview.setLayoutManager(manager1);
                                            progressBar.setVisibility(View.GONE);
                                            searchRecyclerview.setVisibility(View.VISIBLE);
                                        }

                                    }
                                    @Override
                                    public void onFailure(Call<List<DataModel>> call, Throwable t) {
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }
                    },300);

                }
            }
        });
    }
    boolean doubleback;
    @Override
    public void onBackPressed() {
        if(bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }else{
            if (cancelSearch.getVisibility() == View.VISIBLE) {
                progressBar.setVisibility(View.GONE);
                cat.setVisibility(View.VISIBLE);
                plac.setText("Places");
                places.setVisibility(View.VISIBLE);
                categories.setVisibility(View.VISIBLE);
                noResult.setVisibility(View.INVISIBLE);
                cancelSearch.setVisibility(View.GONE);
                searchRecyclerview.setVisibility(View.INVISIBLE);
                search.setText("");
            }else {


                if (doubleback) {
                    //super.onBackPressed();
                    moveTaskToBack(true);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);
                    //Log.i("doubleback", doubleback.toString());
                } else {
                    doubleback = true;
                    Toast.makeText(this, "Please once again BACK to exit", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            doubleback = false;
                        }
                    }, 2000);
                }
            }
        }

    }

}