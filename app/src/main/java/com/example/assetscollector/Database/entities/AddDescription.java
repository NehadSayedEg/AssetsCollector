package com.example.assetscollector.Database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "asset_Des")
public class AddDescription {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "number")
    private int numberInTable;

    @ColumnInfo(name = "locationList")
    private String location;

    @ColumnInfo(name = "descriptionList")
    private String description;

    public int getNumberInTable() {
        return numberInTable;
    }

    public void setNumberInTable(int numberInTable) {
        this.numberInTable = numberInTable;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
