package com.fixo.todo.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fixo.todo.R;
import com.fixo.todo.ViewModels.ToDoViewModel;
import com.fixo.todo.adapters.ToDoListAdapter;

public class CompletedToDosFragment extends Fragment {

    // Member variables
    private ToDoViewModel toDoViewModel;
    private ToDoListAdapter toDoListAdapterAdapter;


    // Required empty public constructor
    public CompletedToDosFragment(ToDoViewModel toDoViewModel) {
        this.toDoViewModel = toDoViewModel;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_completed_to_dos, container, false);

        toDoViewModel.getCompletedToDos().observe(this, toDos -> {

            toDoListAdapterAdapter = new ToDoListAdapter(getContext(), getActivity(), toDos, toDoViewModel);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
            //final ToDoListAdapter adapter = new ToDoListAdapter(new ToDoListAdapter.WordDiff());

            recyclerView.setAdapter(toDoListAdapterAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        });

        return view;
    }
}