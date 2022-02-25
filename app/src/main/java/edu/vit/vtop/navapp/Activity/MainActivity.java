package edu.vit.vtop.navapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import edu.vit.vtop.navapp.R;
import edu.vit.vtop.navapp.Utils.DataHandling;
import edu.vit.vtop.navapp.Utils.DataModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<DataModel> list = new ArrayList<>();
        list.add(new DataModel("Silver Jubliee Tower","SJT",10.00,11.00,"Academic Blocks"));
        list.add(new DataModel("Technology Tower","TT",10.00,11.00,"Academic Blocks"));
        list.add(new DataModel("Food Court","FC",10.00,11.00,"Restaurants"));
        list.add(new DataModel("Cafe Coffee Day","CCD",10.00,11.00,"Coffee Shops"));
        list.add(new DataModel("Silver Jubliee Tower","SJT",10.00,11.00,"Academic Blocks"));
        list.add(new DataModel("Silver Jubliee Tower","SJT",10.00,11.00,"Academic Blocks"));
        list.add(new DataModel("Silver Jubliee Tower","SJT",10.00,11.00,"Academic Blocks"));
        DataHandling.saveList(list,MainActivity.this);
        startActivity(new Intent(MainActivity.this,BottomSheet.class));
    }
}