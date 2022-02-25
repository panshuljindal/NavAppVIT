package edu.vit.vtop.navapp.Recyclerview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.vit.vtop.navapp.R;
import edu.vit.vtop.navapp.Utils.DataModel;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.MyViewHolder> {
    List<DataModel> list;
    Context context;

    public PlacesAdapter(List<DataModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.places_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DataModel model = list.get(position);
        holder.placeName.setText(model.getName());
        holder.categoryName.setText(model.getCategory());
        if(model.getCategory().contains("Academic")){
            holder.categoryName.setTextColor(ContextCompat.getColor(context,R.color.academic));
            holder.icon.setImageResource(R.drawable.ic_academics);
            holder.categories.setCardBackgroundColor(ContextCompat.getColor(context,R.color.academic));
            holder.iconCategory.setCardBackgroundColor(ContextCompat.getColor(context,R.color.academic));

        }if(model.getCategory().contains("Hostel")){
            holder.categoryName.setTextColor(ContextCompat.getColor(context,R.color.hostel));
            holder.icon.setImageResource(R.drawable.ic_hostel);
            holder.categories.setCardBackgroundColor(ContextCompat.getColor(context,R.color.hostel));
            holder.iconCategory.setCardBackgroundColor(ContextCompat.getColor(context,R.color.hostel));

        }if(model.getCategory().contains("Shopping")){
            holder.categoryName.setTextColor(ContextCompat.getColor(context,R.color.shopping));
            holder.icon.setImageResource(R.drawable.ic_shopping);
            holder.categories.setCardBackgroundColor(ContextCompat.getColor(context,R.color.shopping));
            holder.iconCategory.setCardBackgroundColor(ContextCompat.getColor(context,R.color.shopping));
        }if(model.getCategory().contains("Admin")){
            holder.categoryName.setTextColor(ContextCompat.getColor(context,R.color.admin));
            holder.icon.setImageResource(R.drawable.ic_admin);
            holder.categories.setCardBackgroundColor(ContextCompat.getColor(context,R.color.admin));
            holder.iconCategory.setCardBackgroundColor(ContextCompat.getColor(context,R.color.admin));

        }if(model.getCategory().contains("Coffee")){
            holder.categoryName.setTextColor(ContextCompat.getColor(context,R.color.coffee));
            holder.icon.setImageResource(R.drawable.ic_coffee);
            holder.categories.setCardBackgroundColor(ContextCompat.getColor(context,R.color.coffee));
            holder.iconCategory.setCardBackgroundColor(ContextCompat.getColor(context,R.color.coffee));

        }if(model.getCategory().contains("Restaurant")){
            holder.categoryName.setTextColor(ContextCompat.getColor(context,R.color.restaurant));
            holder.icon.setImageResource(R.drawable.ic_restaurant);
            holder.categories.setCardBackgroundColor(ContextCompat.getColor(context,R.color.restaurant));
            holder.iconCategory.setCardBackgroundColor(ContextCompat.getColor(context,R.color.restaurant));

        }
        if(position==0){
            holder.cl.setBackground(ContextCompat.getDrawable(context,R.drawable.places_first));
        }
        if(position==list.size()-1){
            holder.cl.setBackground(ContextCompat.getDrawable(context,R.drawable.places_end));
//            Log.i("Position",model.getPlaceName());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView iconCategory,categories;
        TextView placeName,categoryName;
        ImageView icon;
        ConstraintLayout cl;
        public MyViewHolder(View itemView) {
            super(itemView);
            iconCategory = itemView.findViewById(R.id.placesCategoriesBackground);
            categories = itemView.findViewById(R.id.cardCategory);
            placeName = itemView.findViewById(R.id.placesCategoriesBuildingName);
            categoryName=itemView.findViewById(R.id.placesCategoriesTextview);
            icon = itemView.findViewById(R.id.placesCategoriesImage);
            cl=itemView.findViewById(R.id.placesItem);

        }
    }
}
