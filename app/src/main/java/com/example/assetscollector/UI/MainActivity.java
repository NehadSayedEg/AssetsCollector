package com.example.assetscollector.UI;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.assetscollector.AboutWinActivity;
import com.example.assetscollector.Database.AssetsDatabase;
import com.example.assetscollector.R;
import com.example.assetscollector.UI.Home.HomeActivity;
import com.example.assetscollector.Util.AppCompact;
import com.example.assetscollector.Util.ExcelUtil;
import com.example.assetscollector.Util.LanguageManager;
import com.example.assetscollector.databinding.ActivityMainBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompact {
   // EditText editText;

    private ActivityMainBinding binding;

    public static final String TAG = MainActivity.class.getSimpleName();
    private Context mContext;
    private int FILE_SELECTOR_CODE = 10000;
    private int DIR_SELECTOR_CODE = 20000;
    private List<Map<Integer, Object>> readExcelList = new ArrayList<>();

    private RecyclerView recyclerView;
    // private ExcelAdapter excelAdapter;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;



        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LanguageManager languageManager = new LanguageManager(this);


        binding.aboutWinTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , AboutWinActivity.class);
                startActivity(intent);
            }
        });


//         editText = findViewById(R.id.BarcodeEd);
//         editText.requestFocus();
//         editText.setFocusable(true);
//         editText.addTextChangedListener(new TextWatcher() {
//             @Override
//             public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//             }
//
//             @Override
//             public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//             }
//
//             @Override
//             public void afterTextChanged(Editable editable) {
//
//                 if (editable.length() > 0) {
//
//                     char lastCharacter = editable.charAt(editable.length() - 1);
//
//                     if (lastCharacter == '\n') {
//                         String barcode = editable.subSequence(0, editable.length() - 1).toString();
//                         editText.setText("");
//                         Log.d("afterTextChanged: ", barcode);
//                     }
//                 }
//             }
//         });

        binding.arabicLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageManager.updateResources("ar");
                recreate();

            }
        });


        binding.englishLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageManager.updateResources("en");
                recreate();

            }
        });

//        binding.importFileBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openFileSelector();
//
//            }
//        });

        binding.importFileBtn.setOnClickListener(new View.OnClickListener() {

            Handler handle = new Handler() {
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    progressDialog.incrementProgressBy(2); // Incremented By Value 2
                }
            };

            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(MainActivity.this ,ProgressDialog.THEME_HOLO_LIGHT
                );
                progressDialog.setMax(100); // Progress Dialog Max Value
                progressDialog.setMessage(getString(R.string.loadMesaage)); // Setting Message
                progressDialog.setTitle(getString(R.string.loadTitle)); // Setting Title
                progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));


                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); // Progress Dialog Style Horizontal
                progressDialog.show(); // Display Progress Dialog
                progressDialog.setCancelable(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            openFileSelector();

                            while (progressDialog.getProgress() <= progressDialog.getMax()) {
                                Thread.sleep(200);
                                handle.sendMessage(handle.obtainMessage());
                                if (progressDialog.getProgress() == progressDialog.getMax()) {
                                    progressDialog.dismiss();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });


        binding.HomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                assetsDBSize();

            }
        });

        binding.clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                clearAssetsDB();

                AlertDialog dialog = new MaterialAlertDialogBuilder(MainActivity.this, R.style.AlertDialogTheme).setTitle(R.string.deleteTitle)
                        .setMessage(R.string.deleteMessage)
                        .setPositiveButton(R.string.deleteBtn,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        clearAssetsDB();
                                    }
                                }).setNegativeButton(R.string.cancelBtn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog.setCanceledOnTouchOutside(false);

                dialog.show();

            }
        });


    }

    private void clearAssetsDB() {

        new Thread(() -> {
            Log.i(TAG, "doInBackground: Clear database Table ...");

            AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().DeleteAllLocation();
            AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().DeleteAllDescription();

//            Log.i("DatabaseSize",
//                    AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAllAssets().size()
//                            + "");

        }).start();

    }


    /**
     * open local filer to select file
     */
    private void openFileSelector() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/*");
        startActivityForResult(intent, FILE_SELECTOR_CODE);
    }

    /**
     * open the local filer and select the folder
     */
    private void openFolderSelector() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("application/*");
        intent.putExtra(Intent.EXTRA_TITLE,
                System.currentTimeMillis() + ".xlsx");
        startActivityForResult(intent, DIR_SELECTOR_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_SELECTOR_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if (uri == null) return;
            Log.i(TAG, "onActivityResult: " + "filePath：" + uri.getPath());
            //select file and import
            importExcelDeal(uri);
        } else if (requestCode == DIR_SELECTOR_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if (uri == null) return;
            Log.i(TAG, "onActivityResult: " + "filePath：" + uri.getPath());
            // Toast.makeText(mContext, "Exporting...", Toast.LENGTH_SHORT).show();
            //you can modify readExcelList, then write to excel.
            ExcelUtil.writeExcelNew(this, readExcelList, uri);
        }
    }

    private void importExcelDeal(final Uri uri) {
        new Thread(() -> {
            Log.i(TAG, "doInBackground: Importing...");
//            runOnUiThread(() ->
//                    Toast.makeText(mContext, "Importing...", Toast.LENGTH_SHORT).show());

            List<Map<Integer, Object>> readExcelNew = ExcelUtil.readExcelNew(mContext, uri, uri.getPath());

            Log.i(TAG, "onActivityResult:readExcelNew " + ((readExcelNew != null) ? readExcelNew.size() : ""));

            if (readExcelNew != null && readExcelNew.size() > 0) {
                readExcelList.clear();
                readExcelList.addAll(readExcelNew);
                goToHome();
                assetsDBSize();

                Log.i(TAG, "run: successfully imported");
                runOnUiThread(() -> Toast.makeText(mContext,  getString(R.string.toastimported), Toast.LENGTH_SHORT).show()



                );



            } else {
                runOnUiThread(() -> Toast.makeText(mContext, "no data", Toast.LENGTH_SHORT).show());
            }
        }).start();


    }



    /**
     *   go To Home Activity
     */
    private void goToHome() {
        runOnUiThread(() -> {

            Intent intent = new Intent(this , HomeActivity.class);
            startActivity(intent);

        });
    }

    /**
     * refresh RecyclerView
     */
    private void updateUI() {
        runOnUiThread(() -> {


            Intent intent = new Intent(this , HomeActivity.class);
            startActivity(intent);
//            if (readExcelList != null && readExcelList.size() > 0) {
//               // excelAdapter.notifyDataSetChanged();
//            }
        });
    }


    private void assetsDBSize() {

        new Thread(() -> {
            Log.i(TAG, "doInBackground: check database size...");

            int size =  AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAllDesSize().size();

            Log.i("DatabaseSize",
                    AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAllDesSize().size()
                            + "");

            if ( size > 0){
                goToHome();               }{

                runOnUiThread(() -> Toast.makeText(mContext, "Insert Data file First ", Toast.LENGTH_SHORT).show());

            }

        }).start();

    }



    private void getAssetsLocation() {

        new Thread(() -> {
            Log.i(TAG, "doInBackground: get location database Table ...");

            int size =  AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAllAssets().size();


//            Log.i("DatabaseSize",
//                    AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAllAssets().size()
//                            + "");

//            for (int i = 0 ; i>= size ; i++ ) {
//                String loc =
//                        AssetsDatabase.getAssetsDatabase(getApplicationContext()).assetsDao().getAllAssets().get(i).getLocation();
//
//               // Log.i("loc ", loc + "");
//
//            }




        }).start();


    }
}