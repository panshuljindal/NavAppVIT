package edu.vit.vtop.navapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

import edu.vit.vtop.navapp.R;
import edu.vit.vtop.navapp.Utils.DataModel;

public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private Context context;
    List<DataModel> markers;
    public MarkerInfoWindowAdapter(Context context,List<DataModel> markers) {
        this.context = context.getApplicationContext();
        this.markers = markers;
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        return null;
    }

    @Override
    public View getInfoContents(Marker arg0) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v =  inflater.inflate(R.layout.map_marker_info_window, null);

        LatLng latLng = arg0.getPosition();

        TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);
        TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);

        for(DataModel e : markers) {
            if (latLng.longitude == e.getLon() && latLng.latitude == e.getLat()) {
                tvLat.setText(e.getName());
                tvLng.setText(e.getCategory());
                break;
            }
        }
        return v;
    }
}
