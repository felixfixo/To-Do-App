package com.fixo.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class EditToDo extends AppCompatActivity {
    EditText editToDoEditText;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_to_do);
        editToDoEditText = findViewById(R.id.editToDoEditText);
        saveButton = findViewById(R.id.button_save);

    }
}