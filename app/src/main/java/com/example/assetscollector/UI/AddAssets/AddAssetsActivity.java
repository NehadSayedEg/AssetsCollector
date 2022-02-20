package com.example.assetscollector.UI.AddAssets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.assetscollector.Database.AssetsDatabase;
import com.example.assetscollector.Database.entities.AddDescription;
import com.example.assetscollector.Database.entities.AssetModel;
import com.example.assetscollector.Database.entities.DescriptionEntity;
import com.example.assetscollector.Database.entities.LocationEntity;
import com.example.assetscollector.R;
import com.example.assetscollector.UI.AllAssetsActivity.AllAssetsActivity;
import com.example.assetscollector.UI.MainActivity;
import com.example.assetscollector.databinding.ActivityAddAssetsBinding;
import com.example.assetscollector.databinding.ActivityAllAssetsBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddAssetsActivity extends AppCompatActivity   {
    AddAssetViewModel addAssetViewModel;
    private ActivityAddAssetsBinding binding;
    public static final String TAG = AddAssetsActivity.class.getSimpleName();
    private List<AddDescription> addDescriptionList = new ArrayList<>();
    private List<String> LocationlList = new ArrayList<>();
    private List<String> deslList = new ArrayList<>();
    List<AssetModel> assetslList = new ArrayList<>();
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    String location , Des , addDes ,addLoc,  barcode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddAssetsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
         addAssetViewModel = new ViewModelProvider(this).get(AddAssetViewModel.class);

        LocationlList.add("please select Location");
        deslList.add("please select description");
addAssetViewModel.getAllLocation().observe(this, new Observer<List<LocationEntity>>() {
    @Override
    public void onChanged(List<LocationEntity> locationEntities) {
        Log.i("DatabaseSize locations ", locationEntities.size() + " live Data");

        for (int i = 0; i < locationEntities.size(); i++) {
            String loc = locationEntities.get(i).getLocation();

                    if (!LocationlList.contains(loc)) {
                            LocationlList.add(loc);

                        Log.i("Locccc" + loc , "");
        }}
}
});

        addAssetViewModel.getAllDes().observe(this, new Observer<List<DescriptionEntity>>() {
            @Override
            public void onChanged(@Nullable  List<DescriptionEntity> tableList) {
                //  adapter.setWords(words);
                Log.i("DatabaseSize", tableList.size() + " live Data");
                for (int i = 0; i < tableList.size(); i++) {
                    //String loc = tableList.get(i).getLocation();
                    String des = tableList.get(i).getDescription();

//                    if (!LocationlList.contains(loc)) {
//                            LocationlList.add(loc);
//
//                        Log.i("Locccc" + loc , "");
//                    }
                    if (!deslList.contains(des)) {
                        deslList.add(des);
                        Log.i("Des" + des , "");

                    }
                }}
        });

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, LocationlList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        binding.locationSpinner.setAdapter(adapter);

        binding.locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Des = item;

                Toast.makeText(AddAssetsActivity.this, "Selected" + item, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, deslList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.SpinnerDescription.setAdapter(dataAdapter);
        binding.SpinnerDescription.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                location = item;

              //  Toast.makeText(AddAssetsActivity.this, "Selected" + item, Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });



        binding.barcodeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (!s.toString().isEmpty()) {
                    Log.v("on text change  Text  ", s.toString());

                    barcode = binding.barcodeEt.getText().toString();


                } else {
                    //Toast.makeText(getApplicationContext() , "Empty " , Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.addDesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddAssetsActivity.this, "Add Des Btn", Toast.LENGTH_SHORT).show();
                alertDialogAddDes();


            }
        });
        binding.addlocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogAddLoc();
            }
        });

        binding.saveAssetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddAssetsActivity.this, " Save Asset Btn", Toast.LENGTH_SHORT).show();
                AssetsScan();
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
                Log.e(TAG, "AssetsScan: " + assetslList.size() + "");
                if (assetslList.size() == 0) {
                    saveAsset();
                }
                //binding.assetItemLayout.setVisibility(View.VISIBLE);
                for (int i = 0; i < assetslList.size(); i++) {

                    String barcodeTxt = assetslList.get(i).getBarcode();
                    //   binding.itemBarcode.setText(barcodeTxt);
                    Toast.makeText(AddAssetsActivity.this, "item scanned before " + barcodeTxt, Toast.LENGTH_SHORT).show();


                    String desTxt = assetslList.get(i).getDescription();
                    //  binding.itemDes.setText(desTxt);

                    String locTxt = assetslList.get(i).getLocation();
                    //  binding.itemLoc.setText(locTxt);


                }

            });
        });

    }


    private void saveAsset() {

        AssetModel assetModel = new AssetModel();
        assetModel.setBarcode(barcode);
        assetModel.setDescription(Des);
        assetModel.setLocation(location);
        executor.execute(() -> {
            addAssetViewModel.insertAsset(assetModel);


        });
    }


    private void alertDialogAddDes() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(AddAssetsActivity.this , R.style.AlertDialogTheme);
        myDialog.setTitle("please Add New Asset description");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 10, 10, 10);
        final EditText desEditTxt = new EditText(AddAssetsActivity.this);
        desEditTxt.setInputType(InputType.TYPE_CLASS_TEXT);
        desEditTxt.setPadding(10, 10, 10, 10);
        desEditTxt.setLayoutParams(lp);
        myDialog.setView(desEditTxt);


        // set dialog message
        myDialog.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // get user input and set it to result
                        // edit text
                        addDes=  desEditTxt.getText().toString() ;
                        Toast.makeText(getApplicationContext(), "Entered: " + addDes, Toast.LENGTH_LONG).show();

                        DescriptionEntity descriptionEntity = new DescriptionEntity();
                        descriptionEntity.setDescription(addDes);

                        executor.execute(() -> {
                            addAssetViewModel.insertDescription(descriptionEntity);
                           // AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAllDescription(addDes);
                             List<String> deslList = new ArrayList<>();

                            for (int i = 0; i < deslList.size(); i++) {

                                String desTxt = deslList.get(i).toString();
                                //   binding.itemBarcode.setText(barcodeTxt);
                             //   Toast.makeText(AddAssetsActivity.this, "item scanned before " + desTxt, Toast.LENGTH_SHORT).show();
                             Log.i("item scanned before "+ desTxt,"");
                            }
                            if(deslList.size()>=0){
                               // Toast.makeText(AddAssetsActivity.this, "this description already exist " , Toast.LENGTH_SHORT).show();
                                Log.i("this description ","already exist");


                            }else {
                                descriptionEntity.setDescription(addDes);
                                AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().insertDescription(descriptionEntity);

                            }


                        });


                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
       myDialog.create();
        // show it
        myDialog.show();


    }


    private void alertDialogAddLoc() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(AddAssetsActivity.this , R.style.AlertDialogTheme);
        myDialog.setTitle("please Add New Asset Location");
        final EditText desEditTxt = new EditText(AddAssetsActivity.this);
        desEditTxt.setInputType(InputType.TYPE_CLASS_TEXT);
        desEditTxt.setPadding(10, 10, 10, 10);
        myDialog.setView(desEditTxt);


        // set dialog message
        myDialog.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // get user input and set it to result
                        // edit text
                        addDes=  desEditTxt.getText().toString() ;
                        Toast.makeText(getApplicationContext(), "Entered: " + addDes, Toast.LENGTH_LONG).show();

                        LocationEntity locationEntity = new LocationEntity();
                        locationEntity.setLocation(addDes);

                        executor.execute(() -> {
                            addAssetViewModel.insertLocation(locationEntity);
                            // AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAllDescription(addDes);
                            List<String> deslList = new ArrayList<>();

                            for (int i = 0; i < deslList.size(); i++) {

                                String desTxt = deslList.get(i).toString();
                                //   binding.itemBarcode.setText(barcodeTxt);
                                //   Toast.makeText(AddAssetsActivity.this, "item scanned before " + desTxt, Toast.LENGTH_SHORT).show();
                                Log.i("item scanned before "+ desTxt,"");
                            }
                            if(deslList.size()>=0){
                                // Toast.makeText(AddAssetsActivity.this, "this description already exist " , Toast.LENGTH_SHORT).show();
                                Log.i("this description ","already exist");


                            }else {
                                locationEntity.setLocation(addDes);
                                AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().insertLocation(locationEntity);

                            }


                        });


                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        // create alert dialog
        myDialog.create();
        // show it
        myDialog.show();


    }


}


