package com.example.assetscollector.Database.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.assetscollector.Database.entities.AddDescription;
import com.example.assetscollector.Database.entities.AssetModel;
import com.example.assetscollector.Database.entities.DescriptionEntity;
import com.example.assetscollector.Database.entities.LocationEntity;

import java.util.List;
@Dao
public interface AssetsDao {


    @Query("SELECT * FROM  asset_table")
    List<AssetModel> getAllAssets();


    @Query("SELECT * from asset_table WHERE barcode = :barcode  AND  location =:loc  ")
    List<AssetModel> getAssetsLocation(String barcode  , String loc );

    @Query("SELECT * from asset_table WHERE barcode = :barcode   ")
    List<AssetModel> getAssetByBarcode(String barcode  );


    @Query("SELECT * from asset_table WHERE found = :found   ")
    List<AssetModel> getAssetMissing(boolean found  );



    @Query("SELECT * from asset_table WHERE   location =:loc  AND found =:found ")
    List<AssetModel> getAssetsStatus( boolean found  , String loc );


    @Query("SELECT * from asset_table WHERE    location =:loc  AND found =:found ")
    List<AssetModel> getAssetsFound(  boolean found  , String loc );




    @Query("UPDATE asset_table  SET  found = :found  WHERE  barcode  = :barcode AND  location =:loc ")
    void  setAssetFound(String barcode  , Boolean found , String loc);
    //
    @Query("UPDATE asset_table  SET  scannedBefore =:scannedBefore WHERE  barcode  = :barcode AND  location =:loc ")
    void  setScannedFound(String barcode  , Boolean scannedBefore , String loc);


//
//    @Query("UPDATE asset_table  SET  scannedBefore =:scannedBefore  AND found = :found  WHERE  barcode  = :barcode AND  location =:loc ")
//    void  setScannedFound(String barcode  , Boolean scannedBefore ,Boolean found , String loc);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAsset(AssetModel assetModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertList(AddDescription addDescription);

    @Query("DELETE  FROM  asset_table")
    public void DeleteAllAssets();



//
//    @Query("DELETE  FROM  asset_Des")
//    public void DeleteAllLocation();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAssetNewLocation(AddDescription addDescription);

//    @Query("SELECT * FROM  asset_Des")
//    List<AddDescription> getAllLocationList();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllDesList(DescriptionEntity descriptionEntity);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllLocationList(LocationEntity locationEntity);


    @Query("SELECT * FROM  asset_Des")
    LiveData<List<AddDescription> > getAlllocAndDesList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDes(AddDescription addDescription);

    @Query("SELECT * from asset_Des WHERE    locationList =:loc  ")
    List<AddDescription> getAllLocation(  String loc );


    @Query("SELECT * from asset_Des WHERE    descriptionList =:des  ")
    List<AddDescription> getAllDescription(  String des );

//    @Query("UPDATE asset_location_change  SET   first_location = :firstLocation  WHERE  barcode  = :barcode AND  last_location =:secondLocation ")
//    void  setAssetLocChanged(String barcode  , String firstLocation , String secondLocation);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDesList(DescriptionEntity descriptionEntity);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLocationList(LocationEntity locationEntity);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDescription(DescriptionEntity descriptionEntity);

//    @Query("SELECT * FROM  asset_AddDescription")
//    List<AddDescription> getAllDesList();
//

    @Query("SELECT * FROM  asset_AddDescription")
    List<DescriptionEntity> getAllDesSize();

    @Query("SELECT * FROM  asset_AddLocation")
    List<LocationEntity> getAllLocationSize();


    @Query("SELECT * FROM  asset_AddDescription")
    LiveData<List<DescriptionEntity> > getAllDesList();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLocation(LocationEntity locationEntity);

//    @Query("SELECT * FROM  asset_AddDescription")
//    List<AddDescription> getAllDesList();
//

    @Query("SELECT * FROM  asset_AddLocation")
    LiveData<List<LocationEntity> > getAllLocationList();
    @Query("DELETE  FROM  asset_AddDescription")
    public void DeleteAllDescription();

    @Query("DELETE  FROM  asset_AddLocation")
    public void DeleteAllLocation();



}
