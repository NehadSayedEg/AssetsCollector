package com.example.assetscollector.Database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "asset_AddDescription")

public class DescriptionEntity {

    @NonNull
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "descriptionList")
    private String description;



    public String getDescription() {
        return description;
    }

    public void setDescription (String description) {
        this.description = description;
    }


}
