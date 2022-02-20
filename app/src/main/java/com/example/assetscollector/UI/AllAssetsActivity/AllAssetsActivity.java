package com.example.assetscollector.UI.AllAssetsActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.assetscollector.Database.AssetsDatabase;
import com.example.assetscollector.Database.entities.AssetModel;
import com.example.assetscollector.R;
import com.example.assetscollector.databinding.ActivityAllAssetsBinding;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AllAssetsActivity extends AppCompatActivity {

    private ActivityAllAssetsBinding binding;
    public static final String TAG = AllAssetsActivity.class.getSimpleName();
    private List<AssetModel> assetModelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_all_assets);


        binding = ActivityAllAssetsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new Thread(() -> {
            Log.i(TAG, "doInBackground: Importing...");

            assetModelList =
                    AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAllAssets();

        }).start();





        getAllAssets();


        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            assetModelList = AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAllAssets();
            Log.i("DatabaseSize",
                    assetModelList.get(0).getLocation()
                            + "");




            //Background work here
            handler.post(() -> {
                //UI Thread work here

                LinearLayoutManager layoutManager = new LinearLayoutManager(this);

                binding.allAssetsRV.setLayoutManager(layoutManager );
                AllAssetsAdapter adapter = new AllAssetsAdapter(assetModelList);
                binding.allAssetsRV.setAdapter(adapter);


            });
        });

    }


    private void getAllAssets() {

        new Thread(() -> {
            Log.i(TAG, "doInBackground: get All assets from Assets Table ...");


            Log.i("DatabaseSize",
                    AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAllAssets().size()
                            + "");
            assetModelList =
                    AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAllAssets();







            Log.i("Database string",
                    assetModelList.get(0).getDescription()+ "");
//            Log.i("Database string",
//                    assetModelList.get(1).getBarcode()+ "");

        }).start();

    }}

