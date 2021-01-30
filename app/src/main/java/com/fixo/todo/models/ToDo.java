package com.fixo.todo.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

// Table name "to_do_table"
@Entity(tableName = "to_do_table")

public class ToDo {
    // Defining table column names
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "description")
    private String description;

    @NonNull
    @ColumnInfo(name = "status")
    private String status;

    @NonNull
    @ColumnInfo(name = "dateCreated")
    private String dateCreated;

    // Constructor
    public ToDo(@NonNull String description, @NonNull String status, @NonNull String dateCreated) {
        this.description = description;
        this.status = status;
        this.dateCreated = dateCreated;
    }



    //Getter methods
    public int getId() {
        return id;
    }
    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    @NonNull
    public String getDateCreated() {
        return dateCreated;
    }

    // Setter methods
    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public void setStatus(@NonNull String status) {
        this.status = status;
    }
}
