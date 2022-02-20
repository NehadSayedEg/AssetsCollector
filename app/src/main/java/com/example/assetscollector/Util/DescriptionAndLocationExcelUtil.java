package com.example.assetscollector.Util;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.assetscollector.Database.AssetsDatabase;
import com.example.assetscollector.Database.entities.AddDescription;
import com.example.assetscollector.Database.entities.AssetModel;
import com.example.assetscollector.Database.entities.DescriptionEntity;
import com.example.assetscollector.Database.entities.LocationEntity;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DescriptionAndLocationExcelUtil {

    public static final String TAG = "ExcelUtil";
    private static Cell cell;
    private static Sheet sheet;
    private static Workbook workbook;
    private static Row rowData ;
    private static CellStyle headerCellStyle;



    @RequiresApi(api = Build.VERSION_CODES.N)
    public static boolean exportDataIntoWorkbook(Context context, String fileName,
                                                 List<DescriptionEntity> dataList) {
        boolean isWorkbookWrittenIntoStorage;

        // Check if available and not read only
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e(TAG, "Storage not available or read only");
            return false;
        }

        // Creating a New HSSF Workbook (.xls format)
        workbook = new HSSFWorkbook();

        setHeaderCellStyle();

        // Creating a New Sheet and Setting width for each column
        sheet = workbook.createSheet(" Des & locationNewList ");
        sheet.setColumnWidth(0, (15 * 400));
        sheet.setColumnWidth(1, (15 * 400));


        setHeaderRow();
        fillDataIntoExcel( context , dataList );
        // isWorkbookWrittenIntoStorage = storeExcelInStorage(context, fileName);

        return true;
    }

    private static boolean isExternalStorageReadOnly() {
        String externalStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(externalStorageState);
    }

    /**Checks if Storage is Available*/
    private static boolean isExternalStorageAvailable() {
        String externalStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(externalStorageState);
    }

    /**Setup header cell style */
    private static void setHeaderCellStyle() {
        headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFillForegroundColor(HSSFColor.AQUA.index);
        headerCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER); }

    /** Setup Header Row */
    private static void setHeaderRow() {
        Row headerRow = sheet.createRow(0);

        cell = headerRow.createCell(0);
        cell.setCellValue("location");
        cell.setCellStyle(headerCellStyle);

        cell = headerRow.createCell(1);
        cell.setCellValue("Description");
        cell.setCellStyle(headerCellStyle);


    }

    /**Fills Data into Excel Sheet*/
    @RequiresApi(api = Build.VERSION_CODES.N)
    private static void fillDataIntoExcel (Context context , List<DescriptionEntity> dataList) {


        new Thread(() -> {
            Log.i(TAG, "doInBackground:  grt data base size  database Table ...");
            List<DescriptionEntity> desList = new ArrayList<>();
            List<LocationEntity> locList = new ArrayList<>();


            desList = AssetsDatabase.getAssetsDatabase(context.getApplicationContext()).assetsDao().getAllDesSize();
            locList = AssetsDatabase.getAssetsDatabase(context.getApplicationContext()).assetsDao().getAllLocationSize();

            for (int i = 0; i < desList.size(); i++) {
                Log.i(TAG, " index " + i);

                rowData = sheet.createRow(i + 1);
                // Create Cells for each row
                cell = rowData.createCell(0);
                cell.setCellValue(locList.get(i).getLocation());
                Log.i(TAG, " location cell " + locList.get(i).getLocation());

                cell = rowData.createCell(1);
                cell.setCellValue(desList.get(i).getDescription());
                Log.i(TAG, " des cell " + desList.get(i).getDescription());




                Log.i(TAG, " in  LOOP ");
            }
            Log.i(TAG, " OUT LOOP ");


            //      File file = new File(Environment.getExternalStorageDirectory() + "/AssetTracking" + "/MissingAssets.xlsx");
            Calendar c = Calendar.getInstance();
            System.out.println("Current time => "+c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd HH_mm_ss");
            String formattedDate = df.format(c.getTime());

//                final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//                String   time = dateFormat.format(new Date());

            String FileName = "/"+ formattedDate +"FoundAssets.xls";

            //   File file = new File(Environment.getExternalStorageDirectory() + "/AssetTracking" + "/changeLocation.xlsx");
            File file = new File(Environment.getExternalStorageDirectory() + "/AssetTracking" + FileName);


            FileOutputStream fileOutputStream = null;
            boolean success = false;
            try {
                fileOutputStream = new FileOutputStream(file);
                workbook.write(fileOutputStream);
                Log.w("FileUtils", "Writing file" + file);
                success = true;
            } catch (IOException e) {
                Log.w("FileUtils", "Error writing " + file, e);
            } catch (Exception e) {
                Log.w("FileUtils", "Failed to save file", e);
            } finally {
                try {
                    if (null != fileOutputStream)
                        fileOutputStream.close();
                } catch (Exception ex) {
                }
            }

            Log.w("FileUtils", "success " + success);


        }).start();

    }
}

