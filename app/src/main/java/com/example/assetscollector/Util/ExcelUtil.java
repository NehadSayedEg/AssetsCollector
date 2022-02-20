package com.example.assetscollector.Util;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.assetscollector.Database.AssetsDatabase;
import com.example.assetscollector.Database.entities.AddDescription;
import com.example.assetscollector.Database.entities.AssetModel;
import com.example.assetscollector.Database.entities.DescriptionEntity;
import com.example.assetscollector.Database.entities.LocationEntity;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class ExcelUtil {

    private static final String TAG = ExcelUtil.class.getSimpleName();

    public static List<Map<Integer, Object>> readExcelNew(Context context, Uri uri, String filePath) {
        List<Map<Integer, Object>> list = null;
        Workbook wb;


        if (filePath == null) {
            return null;
        }
        String extString;
        if (!filePath.endsWith(".xls") && !filePath.endsWith(".xlsx")) {
            Log.e(TAG, "Please select the correct Excel file");
            return null;
        }
        extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is;
        try {
            is = context.getContentResolver().openInputStream(uri);
            Log.i(TAG, "readExcel: " + extString);
            if (".xls".equals(extString)) {
                wb = new HSSFWorkbook(is);
            } else if (".xlsx".equals(extString)) {
                wb = new XSSFWorkbook(is);
            } else {
                wb = null;
            }
            if (wb != null) {
                // used to store data
                list = new ArrayList<>();
                // get the first sheet
                Sheet sheet = wb.getSheetAt(0);
                // get the first line header
                Row rowHeader = sheet.getRow(0);
                int cellsCount = rowHeader.getPhysicalNumberOfCells();
                //store header to the map
                Map<Integer, Object> headerMap = new HashMap<>();
                for (int c = 0; c < cellsCount; c++) {
                    Object value = getCellFormatValue(rowHeader.getCell(c));
                    String cellInfo = "header " + "; c:" + c + "; v:" + value;
                    Log.i(TAG, "readExcelNew: " + cellInfo);

                    headerMap.put(c, value);
                }
                //add  headermap to list
                list.add(headerMap);

                // get the maximum number of rows
                int rownum = sheet.getPhysicalNumberOfRows();
                // get the maximum number of columns
                int colnum = headerMap.size();
                //index starts from 1,exclude header.
                //if you want read line by line, index should from 0.
                for (int i = 1; i < rownum; i++) {
                    Row row = sheet.getRow(i);
                    //storing subcontent
                    Map<String, Object> itemMap = new HashMap<>();
                    final AddDescription addDescription = new AddDescription();
                    final DescriptionEntity descriptionEntity = new DescriptionEntity();
                    final LocationEntity locationEntity = new LocationEntity();


                    if (row != null) {
                        for (int j = 0; j < colnum; j++) {

                            Object value = getCellFormatValue(row.getCell(j));
                            String cellInfo = "r: " + i + "; c:" + j + "; v:" + value;
                            Log.i(TAG, "readExcelNew: " + cellInfo);


                            if (j ==  0){
                                String key = "descList";
                                itemMap.put(key, value);
                                descriptionEntity.setDescription(value.toString());
                              //  addDescription.setDescription(value.toString());
                                }




                            if (j ==  1){
                                String key = "locList";
                                itemMap.put(key, value);
                                locationEntity.setLocation(value.toString());
                                //addDescription.setLocation(value.toString());

                            }

                            //itemMap.put(j, value);
                            Log.i(TAG, "item Map Size: " + itemMap.size());
                            Log.i(TAG, "item Map  " + itemMap);


                        }
                    } else {
                        break;
                    }
                    // insert assets data to database
//                    assetModel.setScannedBefore(false);
//
//                    assetModel.setFound(false);

                    AssetsDatabase.getAssetsDatabase(context.getApplicationContext()).assetsDao().insertLocation(locationEntity);
                    AssetsDatabase.getAssetsDatabase(context.getApplicationContext()).assetsDao().insertDesList(descriptionEntity);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "readExcelNew: import error " + e);
            Toast.makeText(context, "import error " + e, Toast.LENGTH_SHORT).show();
        }
        return list;
    }

    public static void writeExcelNew(Context context, List<Map<Integer, Object>> exportExcel, Uri uri) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName("Sheet1"));

            int colums = exportExcel.get(0).size();
            for (int i = 0; i < colums; i++) {
                //set the cell default width to 15 characters
                sheet.setColumnWidth(i, 15 * 256);
            }

            for (int i = 0; i < exportExcel.size(); i++) {
                Row row = sheet.createRow(i);
                Map<Integer, Object> integerObjectMap = exportExcel.get(i);
                for (int j = 0; j < colums; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellValue(String.valueOf(integerObjectMap.get(j)));
                }
            }

            OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            Log.i(TAG, "writeExcel: export successful");
            Toast.makeText(context, "export successful", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "writeExcel: error" + e);
            Toast.makeText(context, "export error" + e, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * get single cell data
     *
     * @param cell </>
     * @return cell
     */
    private static Object getCellFormatValue(Cell cell) {
        Object cellValue;
        if (cell != null) {
            // 判断cell类型
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    cellValue = cell.getBooleanCellValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC: {
                    cellValue = String.valueOf(cell.getNumericCellValue());

                    break;
                }
                case Cell.CELL_TYPE_FORMULA: {
                    // determine if the cell is in date format
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // Convert to date format YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    } else {
                        // Numeric
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING: {
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        } else {
            cellValue = "";
        }
        return cellValue;
    }
}

