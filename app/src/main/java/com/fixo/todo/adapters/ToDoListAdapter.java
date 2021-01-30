package com.fixo.todo.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fixo.todo.R;
import com.fixo.todo.ViewModels.ToDoViewModel;
import com.fixo.todo.models.ToDo;
import com.fixo.todo.utils.ConstantValues;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ViewHolder> {
    // Member variables
    private List<ToDo> toDoList;
    private ToDoViewModel toDoViewModel;
    private Context context;
    private Activity activity;

    // Constructor
    public ToDoListAdapter(Context context, Activity activity, List<ToDo> toDoList, ToDoViewModel toDoViewModel){
        this.context = context;
        this.activity = activity;
        this.toDoList = toDoList;
        this.toDoViewModel = toDoViewModel;
    }

    @NonNull
    @Override
    public ToDoListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.to_do_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoListAdapter.ViewHolder holder, int position) {
        final ToDo toDo =  toDoList.get(position);
        holder.toDoDescriptionTextView.setText(toDo.getDescription());


        // Convert the date from the database to the format 'MMM d, yyyy HH:mm'
        String dateString = toDo.getDateCreated();
        String formattedDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",
                    Locale.ENGLISH);
            Date parsedDate = sdf.parse(dateString);
            SimpleDateFormat sdf2 = new SimpleDateFormat("MMM d, yyyy HH:mm", Locale.ENGLISH);
            if (parsedDate != null) {
                formattedDate = sdf2.format(parsedDate);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Displayed the formatted date
        holder.dateTextView.setText(formattedDate);

        if( toDo.getStatus().equals(ConstantValues.COMPLETED_STATUS)){
            holder.editButton.setVisibility(View.GONE);
            holder.completeButton.setVisibility(View.GONE);
            holder.linearLayout.setBackgroundColor(Color.parseColor("#008000"));
        }

        holder.deleteButton.setOnClickListener(v -> {
            // Call method for deleting a to-do
            deleteTodo(toDo);
        });

        holder.editButton.setOnClickListener(v -> {
            // Call method for updating a to-do
            editToDo(toDo);
        });

        holder.completeButton.setOnClickListener(v -> {
            // Call method for completing a to-do
            markToDoAsComplete(toDo);
        });



    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // Views
        TextView toDoDescriptionTextView, dateTextView;
        ImageButton deleteButton,editButton, completeButton;
        LinearLayout linearLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the views
            toDoDescriptionTextView = itemView.findViewById(R.id.toDescriptionTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton = itemView.findViewById(R.id.editButton);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            completeButton = itemView.findViewById(R.id.completeButton);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }

    /**
     * Method for deleting a to-do
     * @param toDo The to-do to be deleted
     */
    private void deleteTodo(ToDo toDo){
        // Alert the user before deleting the to-do
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.delete_confirmation);
        builder.setMessage(R.string.sure_to_delete_to_do);
        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
            toDoViewModel.deleteTodo(toDo);
        });
        builder.setNegativeButton(R.string.cancel,null);
        builder.create().show();

    }

    /**
     * Method for updating a to-do
     * @param toDo To-do to be updated
     */
    private  void editToDo(ToDo toDo){
        // Displaying a dialog with the to-do to be updated
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_alert_dialog, null);
        EditText toDoDescriptionEditText = view.findViewById(R.id.toDescriptionEditText);
        Button saveButton = view.findViewById(R.id.saveButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        // Display to-do description
        toDoDescriptionEditText.setText(toDo.getDescription());

        builder.setCancelable(false);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        // Cancel update
        cancelButton.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        // Save update
        saveButton.setOnClickListener(v -> {
            String description = toDoDescriptionEditText.getText().toString().trim();
            toDo.setDescription(description);
            toDoViewModel.updateTodo(toDo);
            alertDialog.dismiss();
        });


    }

    private void markToDoAsComplete(ToDo toDo){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to mark this task as complete?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            toDo.setStatus(ConstantValues.COMPLETED_STATUS);
            toDoViewModel.updateTodo(toDo);
        });
        builder.setNegativeButton("Cancel",null);
        builder.create().show();
    }
}
