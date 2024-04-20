package com.kursach.ckursach;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import org.jetbrains.annotations.NotNull;

import java.sql.Array;
import java.util.ArrayList;

@Entity
public class TableEntity {
    private int columnCount;
    public TableEntity(int columnCount){
        this.columnCount = columnCount;

    }
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "№")
    @NonNull
    public Integer id;

    @ColumnInfo (name = "")
    public ArrayList<Object> data = new ArrayList<Object>(){{
        for(int i=0; i<columnCount;i++){
           
              Object object = new Object();
            data.add(object);
        }
    }};

}
