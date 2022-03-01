package edu.vit.vtop.navapp.Utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;

public class DataHandling {
    public static void saveList(List<DataModel> saveLike, Context context){
        Gson gson = new Gson();
        String json = gson.toJson(saveLike);
        SharedPreferences pref = context.getSharedPreferences("edu.vit.vtop.navapp", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("list",json);
        //Log.i("save_json",json);
        editor.apply();
    }
    public static List<DataModel> getList(Context context){
        SharedPreferences pref = context.getSharedPreferences("edu.vit.vtop.navapp", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String json = pref.getString("list","");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<DataModel>>() {}.getType();
        ArrayList<DataModel> list=new ArrayList<>();
        list= gson.fromJson(json,type);
        if (list==null){
            list = new ArrayList<>();
        }
        return list;
    }
    public static void addPlace(DataModel model, Context context){
        Gson gson = new Gson();
        List<DataModel> list = getPlaces(context);
        if(list.size()>5){
            list.remove(list.get(0));
            list.add(model);
        }else{
            list.add(model);
        }
        String json = gson.toJson(list);
        SharedPreferences pref = context.getSharedPreferences("edu.vit.vtop.navapp", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("placesList",json);
        //Log.i("save_json",json);
        editor.apply();
    }
    public static List<DataModel> getPlaces(Context context){
        SharedPreferences pref = context.getSharedPreferences("edu.vit.vtop.navapp", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String json = pref.getString("placesList","");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<DataModel>>() {}.getType();
        ArrayList<DataModel> list=new ArrayList<>();
        list= gson.fromJson(json,type);
        if (list==null){
            list = new ArrayList<>();
        }
        return list;
    }
    public static List<DataModel> searchData(String searchString){
        List<DataModel> list=new ArrayList<>();
//        list.add(new DataModel("Silver Jubliee Tower","SJT",10.00,11.00,"Academic Blocks"));
//        list.add(new DataModel("Technology Tower","TT",10.00,11.00,"Academic Blocks"));

        return list;
    }
}
