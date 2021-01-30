package com.fixo.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.fixo.todo.utils.ConstantValues;

import java.util.Calendar;
import java.util.Date;

public class NewToDoActivity extends AppCompatActivity {

    // Member variables
    public static final String DESCRIPTION_EXTRA_REPLY = "com.fixo.todo.DESCRIPTION_EXTRA_REPLY";
    public static final String STATUS_EXTRA_REPLY = "com.fixo.todo.STATUS_EXTRA_REPLY";
    public static final String DATE_EXTRA_REPLY = "com.fixo.todo.DATE_EXTRA_REPLY";
    private ImageView backImageView;
    private EditText editToDoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_to_do);
        editToDoView = findViewById(R.id.edit_word);
        backImageView = findViewById(R.id.backImageView);

        final Button button = findViewById(R.id.button_save);

        // Get today's date
        Date dateToday = Calendar.getInstance().getTime();

        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            // Check for empty string
            if (TextUtils.isEmpty(editToDoView.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                // Get description from edit text
                String toDoDescription = editToDoView.getText().toString();

                // Set data to be returned back to MainActivity
                replyIntent.putExtra(DESCRIPTION_EXTRA_REPLY, toDoDescription);
                replyIntent.putExtra(STATUS_EXTRA_REPLY, ConstantValues.ACTIVE_STATUS);
                replyIntent.putExtra(DATE_EXTRA_REPLY, dateToday.toString());

                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });


        backImageView.setOnClickListener(v -> {
            // Finish and go back MainActivity
            finish();
        });
    }

}
