package com.example.assetscollector.UI.AddAssets;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.assetscollector.Database.AssetsDatabase;
import com.example.assetscollector.Database.Dao.AssetsDao;
import com.example.assetscollector.Database.entities.AddDescription;
import com.example.assetscollector.Database.entities.AssetModel;
import com.example.assetscollector.Database.entities.DescriptionEntity;
import com.example.assetscollector.Database.entities.LocationEntity;

import java.util.List;

public class AddAssetViewModel extends ViewModel {
    AssetsDao assetsDao ;
     LiveData<List<AddDescription>> mAllDes;
    LiveData<List<DescriptionEntity>> desList;
    LiveData<List<LocationEntity>> locationList;



    public AddAssetViewModel(){
        assetsDao = AssetsDatabase.assetsDatabase.assetsDao();
        //.getInstance(application).dao();
        mAllDes = assetsDao.getAlllocAndDesList();
        desList = assetsDao.getAllDesList();
        locationList = assetsDao.getAllLocationList();
    }


    LiveData<List<DescriptionEntity>> getAllDes() {
       // mAllDes = assetsDao.getAlllocAndDesList() ;
        return desList; }


    LiveData<List<LocationEntity>> getAllLocation() {
        // mAllDes = assetsDao.getAllLocAndDesList() ;
        return locationList; }

    public void insertDescription(DescriptionEntity descriptionEntity) { assetsDao.insertDescription(descriptionEntity); }
    public void insertLocation(LocationEntity locationEntity) { assetsDao.insertLocation(locationEntity); }
    public void insertDes(AddDescription addDescription) { assetsDao.insertDes(addDescription); }

    public void insertAsset(AssetModel assetModel) { assetsDao.insertAsset(assetModel); }


}

