package com.fixo.todo.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ToDoViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;

    // Factory constructor
    public ToDoViewModelFactory(Application application) {
        mApplication = application;

    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ToDoViewModel(mApplication);
    }
}
