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
    // Method for inserting a task into the database
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertToDo(ToDo toDo);

    // Method for deleting task from the database
    @Delete
    void deleteToDo(ToDo todo);

    // Method for updating a task
    @Update
    void updateToDo(ToDo toDo);


    // Method for getting active tasks
    @Query("SELECT * FROM to_do_table WHERE status='Active' ORDER BY id DESC")
    LiveData<List<ToDo>> getActiveToDos();

    // Method for getting complete tasks
    @Query("SELECT * FROM to_do_table WHERE status='Completed' ORDER BY id DESC")
    LiveData<List<ToDo>> getCompletedToDos();


}
