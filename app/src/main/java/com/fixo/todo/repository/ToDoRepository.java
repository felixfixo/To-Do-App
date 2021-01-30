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

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public ToDoRepository(Application application) {
        ToDoRoomDatabase db = ToDoRoomDatabase.getDatabase(application);
        toDoDao = db.toDoDao();
        activeToDos = toDoDao.getActiveToDos();
        completedToDos = toDoDao.getCompletedToDos();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<ToDo>> getActiveToDos() {
        return activeToDos;
    }
    public LiveData<List<ToDo>> getCompletedToDos() {
        return completedToDos;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(ToDo toDo) {
        ToDoRoomDatabase.databaseWriteExecutor.execute(() -> {
            toDoDao.insertToDo(toDo);
        });
    }

    public void deleteTodo(ToDo toDo) {
        ToDoRoomDatabase.databaseWriteExecutor.execute(() -> {
            toDoDao.deleteToDo(toDo);
        });
    }

    public void updateTodo(ToDo toDo) {
        ToDoRoomDatabase.databaseWriteExecutor.execute(() -> {
            toDoDao.updateToDo(toDo);
        });
    }

    /**
     * Method that updates a to-do status to 'Completed'
     * @param toDo The task to be updated
     */
//    public void completeTodo(ToDo toDo) {
//        ToDoRoomDatabase.databaseWriteExecutor.execute(() -> {
//            toDoDao.completeToDo(toDo);
//        });
//    }

}
