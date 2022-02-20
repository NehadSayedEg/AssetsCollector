package com.example.assetscollector.UI.Home;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.assetscollector.Database.AssetsDatabase;
import com.example.assetscollector.Database.entities.AssetModel;
import com.example.assetscollector.R;
import com.example.assetscollector.UI.AddAssets.AddAssetsActivity;
import com.example.assetscollector.UI.AllAssetsActivity.AllAssetsActivity;
import com.example.assetscollector.UI.ExportFiles.ExportfilesActivity;
import com.example.assetscollector.UI.FindAsset.FindAssetActivity;
import com.example.assetscollector.UI.MainActivity;
import com.example.assetscollector.databinding.ActivityHomeBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeActivity extends AppCompatActivity {


    private ActivityHomeBinding binding;
    public static final String TAG = HomeActivity.class.getSimpleName();
    private List<AssetModel> assetModelList = new ArrayList<>();
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());


//
//            //Background work here
//            handler.post(() -> {
//                //UI Thread work here
//
//                ArrayAdapter<String> arrayAdapter = new ArrayAdapter( this , R.layout.option_item_location ,LocationlList);
//
//                binding.autoComplete.setAdapter(arrayAdapter);
//                binding.autoComplete.setOnItemClickListener( new AdapterView.OnItemClickListener(){
//                    @Override
//                    public void onItemClick(AdapterView<?> parent , View view , int position , long id){
//                        String item  = parent.getItemAtPosition(position).toString();
//                        location = item ;
//                        Toast.makeText(getApplicationContext() , "Item :" + location , Toast.LENGTH_LONG).show();
//
//                    }
//
//
//                });
//            });
//        });


        binding.allAssetsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AllAssetsActivity.class);
                startActivity(intent);
            }
        });

        binding.searchAssetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FindAssetActivity.class);
                startActivity(intent);
            }
        });

        binding.AddAssetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddAssetsActivity.class);
                startActivity(intent);
            }
        });


        binding.exportFilesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity.this , ExportfilesActivity.class);
                startActivity(intent);


            }

        });


        binding.clearDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog dialog = new MaterialAlertDialogBuilder(HomeActivity.this, R.style.AlertDialogTheme).setTitle(R.string.deleteTitle)
                        .setMessage(R.string.deleteMessage)
                        .setPositiveButton(R.string.deleteBtn,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        clearAssetsDB();
//                                Toast.makeText(getApplicationContext(), "Bach to Main", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();


                                    }
                                }).setNegativeButton(R.string.cancelBtn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();

            }

        });

    }

        private void clearAssetsDB () {

            new Thread(() -> {
                Log.i(TAG, "doInBackground: Clear database Table ...");

                AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().DeleteAllLocation();
                AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().DeleteAllDescription();

                Log.i("DatabaseSize",
                        AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAllDesSize().size() + "");

            }).start();

        }


    }

