package com.example.assetscollector.UI.ExportFiles;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.assetscollector.Database.AssetsDatabase;
import com.example.assetscollector.Database.entities.AddDescription;
import com.example.assetscollector.Database.entities.AssetModel;
import com.example.assetscollector.R;
import com.example.assetscollector.UI.Home.HomeActivity;
import com.example.assetscollector.Util.DescriptionAndLocationExcelUtil;
import com.example.assetscollector.Util.ExportExcelUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExportfilesActivity extends AppCompatActivity {

    Context context ;
    private List<AssetModel> assetModelList = new ArrayList<>();
    private List<AddDescription> desList = new ArrayList<>();


    String FolderName ="AssetCollector";
    private  static final int PERMISSION_REQUEST_CODE = 7;

    private File filePath = new File(Environment.getExternalStorageDirectory() + "/AssetCollector"+"/Demo.xlsx");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exportfiles);
        context = this;
        Dialog();

        new Thread(() -> {
            assetModelList = AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAllAssets();
        }).start();


    }


    private void askPermission() {

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                createDirectory(FolderName);
            }else
            {
                Toast.makeText(ExportfilesActivity.this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createDirectory(String folderName) {

        File file = new File(Environment.getExternalStorageDirectory(),folderName);

        if (!file.exists()){

            file.mkdir();

            Toast.makeText(ExportfilesActivity.this,getString(R.string.folderCreatedToast),Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(ExportfilesActivity.this,getString(R.string.folderExistToast),Toast.LENGTH_SHORT).show();
        }

        createExcelSheet();

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createExcelSheet() {
        ExportExcelUtil.exportDataIntoWorkbook(context,"AssetCollector.xls" ,assetModelList );
       // DescriptionAndLocationExcelUtil.exportDataIntoWorkbook(context,"Loc&DESlIST .xls" ,desList );


        String path = Environment.getExternalStorageDirectory().toString()+"/AssetCollector";
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
//        Log.d("Files", "Size: "+ files.length);
//        for (int i = 0; i < files.length; i++)
//        {
//            Log.d("Files", "FileName:" + files[i].getName());
//            Log.d("Files", "FileName:" + files[i].getPath());
//
//        }
        Intent intent = new Intent(ExportfilesActivity.this , HomeActivity.class);
        startActivity(intent);

    }


    private void Dialog() {
        AlertDialog dialog = new MaterialAlertDialogBuilder(ExportfilesActivity.this, R.style.AlertDialogTheme).setTitle(R.string.dialogExportTitle)
                .setMessage(R.string.dialogExportMessage)
                .setPositiveButton(R.string.alertExportPostiveBtn,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (ContextCompat.checkSelfPermission(ExportfilesActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

                                    createDirectory(FolderName);

                                }else
                                {
                                    askPermission();
                                }


                            }
                        }).setNegativeButton(R.string.alertExportCancelBtn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(ExportfilesActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();                    }
                }).create();
        dialog.setCanceledOnTouchOutside(false);

        dialog.show();

    }



}







