package com.fixo.todo.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fixo.todo.models.ToDo;
import com.fixo.todo.repository.ToDoRepository;

import java.util.List;

public class ToDoViewModel extends AndroidViewModel {

    private ToDoRepository toDoRepository;

    private final LiveData<List<ToDo>> activeToDos;
    private final LiveData<List<ToDo>> completedToDos;

    public ToDoViewModel (Application application) {
        super(application);
        toDoRepository = new ToDoRepository(application);
        activeToDos = toDoRepository.getActiveToDos();
        completedToDos = toDoRepository.getCompletedToDos();
    }

    public LiveData<List<ToDo>> getActiveToDos() { return activeToDos; }

    //get completed to dos
    public LiveData<List<ToDo>> getCompletedToDos() { return completedToDos; }

    public void insert(ToDo toDo) { toDoRepository.insert(toDo); }

    public void deleteTodo(ToDo toDo) { toDoRepository.deleteTodo(toDo); }

    /**
     * Method to update a to-do
     * @param toDo The task to be updated
     */
    public void updateTodo(ToDo toDo) { toDoRepository.updateTodo(toDo); }

    /**
     * Method to update a to-do status to 'Completed'
     * @param toDo The task to be updated
     */
//    public void completeTodo(ToDo toDo) { toDoRepository.completeTodo(toDo); }

}
