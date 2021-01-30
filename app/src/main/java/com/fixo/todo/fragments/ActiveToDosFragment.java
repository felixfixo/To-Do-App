package com.fixo.todo.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fixo.todo.R;
import com.fixo.todo.ViewModels.ToDoViewModel;
import com.fixo.todo.adapters.ToDoListAdapter;

public class ActiveToDosFragment extends Fragment {

    // Member variables
    private ToDoViewModel toDoViewModel;
    public static final int NEW_TO_DO_ACTIVITY_REQUEST_CODE = 1;
    ToDoListAdapter toDoListAdapterAdapter;

    // Required empty public constructor
    public ActiveToDosFragment(ToDoViewModel toDoViewModel) {
        this.toDoViewModel = toDoViewModel;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_active_to_dos, container, false);
        TextView noTasksTextView = view.findViewById(R.id.noTasksTextView);

        // Get active tasks using view model
        toDoViewModel.getActiveToDos().observe(this, toDos -> {

            toDoListAdapterAdapter = new ToDoListAdapter(getContext(), getActivity(), toDos, toDoViewModel);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
//            if( toDoListAdapterAdapter.getItemCount() < 1){
//                noTasksTextView.setVisibility(View.VISIBLE);
//            }

            // Set the adapter to the recycler view
            recyclerView.setAdapter(toDoListAdapterAdapter);
            // set layout manager
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        });

        return view;
    }
}