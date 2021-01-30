package com.fixo.todo.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.fixo.todo.models.ToDo;
import com.fixo.todo.utils.ConstantValues;

import java.util.List;

@Dao
public interface ToDoDao {
    String activeStatus = ConstantValues.ACTIVE_STATUS;

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertToDo(ToDo toDo);

    @Delete
    void deleteToDo(ToDo todo);

    @Update
    void updateToDo(ToDo toDo);

//    @Query("UPDATE to_do_table SET status='Completed' WHERE status='Active' ")
//    void completeToDo(ToDo toDo);

    @Query("SELECT * FROM to_do_table WHERE status='Active' ORDER BY id DESC")
    LiveData<List<ToDo>> getActiveToDos();

    @Query("SELECT * FROM to_do_table WHERE status='Completed' ORDER BY id DESC")
    LiveData<List<ToDo>> getCompletedToDos();


}
