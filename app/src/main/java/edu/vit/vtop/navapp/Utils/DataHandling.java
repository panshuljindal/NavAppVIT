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
}
