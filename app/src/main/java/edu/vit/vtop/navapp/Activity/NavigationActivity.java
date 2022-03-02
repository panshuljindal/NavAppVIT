package edu.vit.vtop.navapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Arrays;
import java.util.List;

import edu.vit.vtop.navapp.R;
import edu.vit.vtop.navapp.Utils.DataHandling;
import edu.vit.vtop.navapp.Utils.DataModel;
import edu.vit.vtop.navapp.databinding.ActivityNavigationBinding;

public class NavigationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityNavigationBinding binding;
    private TextView name,address;
    private ImageView cancel;
    DataModel marker_model;
    private CardView go;
    double lat,lng,ulat,ulng;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent i = getIntent();
        marker_model = (DataModel) i.getSerializableExtra("marker_object");
        DataHandling.addPlace(marker_model,NavigationActivity.this);
        lat = marker_model.getLat();
        lng = marker_model.getLon();

//        ulat =i.getDoubleExtra("ulat",0.0);
//        ulng =i.getDoubleExtra("ulon",0.0);

            ulat = 12.969845;
            ulng = 79.158639;


//        Log.i("lat",Double.toString(lat));
//        Log.i("long",Double.toString(lng));

            binding.navDestination.setText(marker_model.getName());
            binding.navAddress.setText(marker_model.getAddress());

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            findID();

//            progressDialog.dismiss();

//        }
//        catch (Exception e)
//        {
//            progressDialog.dismiss();
//            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(NavigationActivity.this,HomeActivity.class);
//                startActivity(i);
                finish();
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+lat+","+lng);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
    }
    private void findID(){
        name=findViewById(R.id.navDestination);
        address=findViewById(R.id.navAddress);
        cancel=findViewById(R.id.navCancel);
        go=findViewById(R.id.navGo);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            public void onMapLoaded() {
                binding.animationLayout.setVisibility(View.INVISIBLE);
            }
        });

//        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.map_style));
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:

                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.map_style_night));
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.map_style));
                break;
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        //the include method will calculate the min and max bound.
        LatLng loc = new LatLng(marker_model.getLat(),marker_model.getLon());
        LatLng loc1 = new LatLng(ulat,ulng);
        builder.include(loc);
        builder.include(loc1);

        LatLngBounds bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

        mMap.animateCamera(cu);

        SharedPreferences mPrefs = getSharedPreferences("THEME", 0);
        String theme=mPrefs.getString("theme","");

        // Create a LatLngBounds that includes the VIT Campus bounds
        LatLngBounds vitBounds = new LatLngBounds(
                new LatLng(12.967077, 79.152291), // SW bounds
                new LatLng(12.978755, 79.167387)  // NE bounds
        );


// Constrain the camera target to the VIT Campus bounds.
        mMap.setLatLngBoundsForCameraTarget(vitBounds);
        mMap.setMinZoomPreference(16.0f); // Set a preference for minimum zoom (Zoom out).
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(lat, lng);
        LatLng user = new LatLng(ulat,ulng);
        int vector = 0;
        switch (marker_model.getCategory()) {
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
                vector = R.drawable.ic_marker_academic;
                break;
            case "Sports":
                // change to sports
                vector = R.drawable.ic_marker_academic;
                break;
            case "Gates":
                // change to gate
                vector = R.drawable.ic_marker_academic;
                break;

            case "Utilities":
                vector = R.drawable.ic_marker_academic;
                break;
            case "Restaurants":
                vector = R.drawable.ic_marker_food;
                break;
            case "Administrative Offices":
                vector = R.drawable.ic_marker_admin;
                break;
            default:
                vector = R.drawable.ic_marker_food;
                break;


        }
        mMap.addMarker(new MarkerOptions().position(new LatLng(marker_model.getLat(), marker_model.getLon()))
                .title(marker_model.getName())
                .icon(BitmapFromVector(getApplicationContext(), vector)));

//        mMap.addMarker(new MarkerOptions().position(sydney).title("Sydney"));

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        //delay is for after map loaded animation starts
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(ulat, ulng), 16));

            }
        }, 2000);

//        mMap.addMarker(new MarkerOptions().position(user).title("User"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(marker_model.getLat(),marker_model.getLon())));


        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);

        LatLng point = new LatLng(user.latitude, user.longitude);
        LatLng point1 = new LatLng(marker_model.getLat(),marker_model.getLon());
        options.add(point);
        options.add(point1);
        Polyline line = mMap.addPolyline(options);
        List<PatternItem> pattern = Arrays.asList(
                  new Dash(50));
        line.setPattern(pattern);
        mMap.addPolyline(options);
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
}