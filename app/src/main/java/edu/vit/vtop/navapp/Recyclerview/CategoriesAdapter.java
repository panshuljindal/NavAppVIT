package edu.vit.vtop.navapp.Recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;

import java.util.List;

import edu.vit.vtop.navapp.Activity.CategoryActivity;
import edu.vit.vtop.navapp.R;
import edu.vit.vtop.navapp.RecyclerviewModels.CategoriesModel;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyViewholder> {

    List<CategoriesModel> list;
    Context context;
    Location location;
    GoogleMap mMap;
    ActivityResultLauncher<String[]> locationPermissionRequest;

    public CategoriesAdapter(List<CategoriesModel> list, Context context,Location location, GoogleMap mMap, ActivityResultLauncher<String[]> locationPermissionRequest) {
        this.list = list;
        this.context = context;
        this.location = location;
        this.mMap = mMap;
        this.locationPermissionRequest = locationPermissionRequest;
    }

    @Override
    public MyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.categories_item,parent,false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(MyViewholder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(list.get(position).getName());
        holder.icon.setImageResource(list.get(position).getIcon());
        holder.background.setCardBackgroundColor(ContextCompat.getColor(context,list.get(position).getColor()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, CategoryActivity.class);
                i.putExtra("category",list.get(position).getName());
//                i.putExtra("locationPermission", (Parcelable) locationPermissionRequest);
//                i.putExtra("location",location);
//                i.putExtra("map", (Parcelable) mMap);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView icon;
        CardView background;
        public MyViewholder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.categoriesName);
            icon = itemView.findViewById(R.id.categorieImage);
            background=itemView.findViewById(R.id.categorieBackground);
        }
    }
}
