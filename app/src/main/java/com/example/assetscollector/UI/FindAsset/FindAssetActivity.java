package com.example.assetscollector.UI.FindAsset;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.example.assetscollector.Database.AssetsDatabase;
import com.example.assetscollector.Database.entities.AssetModel;
import com.example.assetscollector.R;
import com.example.assetscollector.UI.Home.HomeActivity;
import com.example.assetscollector.databinding.ActivityFindAssetBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FindAssetActivity extends AppCompatActivity {


    private ActivityFindAssetBinding binding;
    public static final String TAG = FindAssetActivity.class.getSimpleName();

    List<AssetModel> assetslList = new ArrayList<>();
    String barcode;
    Activity activity;
    Context context;
    boolean found = false;
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    String location;
    String scannedLocation;
    String Des;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_search_asset);
        binding = ActivityFindAssetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.barcodeEt.requestFocus();
        binding.barcodeEt.setFocusable(true);
        binding.barcodeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (!s.toString().isEmpty()) {
                    Log.v("on text change  Text  ", s.toString());

                    barcode = binding.barcodeEt.getText().toString();
                    AssetsScan();


                } else {
                    //Toast.makeText(getApplicationContext() , "Empty " , Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindAssetActivity.this , HomeActivity.class);
                startActivity(intent);
                finish();

            }
        });


    }

    private void AssetsScan() {
        executor.execute(() -> {
            AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAssetByBarcode(barcode);

            assetslList.clear();
            assetslList.addAll(AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAssetByBarcode(barcode));




            //Background work here
            handler.post(() -> {
                //UI Thread work here
                binding.barcodeEt.setText("");

                for (int i = 0 ; i< assetslList.size() ; i++ ) {
                    if(assetslList.size()>=0){

                        binding.assetItemLayout.setVisibility(View.VISIBLE);

                        String  barcodeTxt = assetslList.get(i).getBarcode() ;
                        binding.itemBarcode.setText(barcodeTxt);

                        String  desTxt = assetslList.get(i).getDescription() ;
                        binding.itemDes.setText(desTxt);

                        String  locTxt = assetslList.get(i).getLocation() ;
                        binding.itemLoc.setText(locTxt);

                    }
                    else {
                        binding.notfoundtxt.setVisibility(View.VISIBLE);


                    }





                }

            });
        });

    }



}