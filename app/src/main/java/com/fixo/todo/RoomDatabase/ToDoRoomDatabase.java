package com.fixo.todo.RoomDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.fixo.todo.DAO.ToDoDao;
import com.fixo.todo.models.ToDo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {ToDo.class}, version = 1, exportSchema = false)
public abstract class ToDoRoomDatabase extends RoomDatabase {

    public abstract ToDoDao toDoDao();

    private static volatile ToDoRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ToDoRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ToDoRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ToDoRoomDatabase.class, "to_do_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
