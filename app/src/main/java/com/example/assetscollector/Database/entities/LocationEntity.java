package com.example.assetscollector.Database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "asset_AddLocation")
public class LocationEntity {


    @NonNull
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "locationList")
    private String location;



    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


}