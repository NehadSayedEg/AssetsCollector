package com.example.assetscollector.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.assetscollector.Database.Dao.AssetsDao;
import com.example.assetscollector.Database.entities.AddDescription;
import com.example.assetscollector.Database.entities.AssetModel;
import com.example.assetscollector.Database.entities.DescriptionEntity;
import com.example.assetscollector.Database.entities.LocationEntity;


@Database(entities = { AddDescription.class , AssetModel.class , LocationEntity.class , DescriptionEntity.class}, version = 3 , exportSchema = false)
public  abstract  class AssetsDatabase extends RoomDatabase {
    public static AssetsDatabase assetsDatabase ;
    public static synchronized AssetsDatabase getAssetsDatabase(Context context){
        if(assetsDatabase == null){
            assetsDatabase = Room.databaseBuilder(context , AssetsDatabase.class , "assets_db").fallbackToDestructiveMigration()
                    .build();
        }
        return assetsDatabase;
    }

    public abstract AssetsDao assetsDao();
}
