package com.fixo.todo.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.fixo.todo.DAO.ToDoDao;
import com.fixo.todo.RoomDatabase.ToDoRoomDatabase;
import com.fixo.todo.models.ToDo;

import java.util.List;

public class ToDoRepository {
    private ToDoDao toDoDao;
    private LiveData<List<ToDo>> activeToDos;
    private LiveData<List<ToDo>> completedToDos;


    public ToDoRepository(Application application) {
        ToDoRoomDatabase db = ToDoRoomDatabase.getDatabase(application);
        toDoDao = db.toDoDao();
        activeToDos = toDoDao.getActiveToDos();
        completedToDos = toDoDao.getCompletedToDos();
    }

    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<ToDo>> getActiveToDos() {
        return activeToDos;
    }
    public LiveData<List<ToDo>> getCompletedToDos() {
        return completedToDos;
    }

    /**
     * Method for saving new task in the database
     * @param toDo The task to be saved
     */
    public void insert(ToDo toDo) {
        // Save the task in the background thread
        ToDoRoomDatabase.databaseWriteExecutor.execute(() -> {
            toDoDao.insertToDo(toDo);
        });
    }

    /**
     * Method for deleting a task
     * @param toDo The task to be deleted
     */
    public void deleteTodo(ToDo toDo) {
        // Delete the task in the background thread
        ToDoRoomDatabase.databaseWriteExecutor.execute(() -> {
            toDoDao.deleteToDo(toDo);
        });
    }

    /**
     * Method for updating a task
     * @param toDo The task to be saved
     */
    public void updateTodo(ToDo toDo) {
        // Update on the background thread
        ToDoRoomDatabase.databaseWriteExecutor.execute(() -> {
            toDoDao.updateToDo(toDo);
        });
    }


}
