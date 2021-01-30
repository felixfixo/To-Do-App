package com.fixo.todo.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ToDoViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;



    public ToDoViewModelFactory(Application application) {
        mApplication = application;

    }
//    @Override
//    public <T extends ViewModel> T create(Class<T> modelClass) {
//        return (T) new BookViewModel(mApplication);
//    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ToDoViewModel(mApplication);
    }
}
