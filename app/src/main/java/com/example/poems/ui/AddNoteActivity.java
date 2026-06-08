package com.example.poems.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.poems.R;

/**
 * Изолированная активность для ввода пользовательских примечаний к стихам.
 */
public class AddNoteActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        EditText editText = findViewById(R.id.noteEditText);
        Button saveBtn = findViewById(R.id.btnSaveNote);

        String currentNote = getIntent().getStringExtra("CURRENT_NOTE");
        if (currentNote!= null) {
            editText.setText(currentNote);
            editText.setSelection(currentNote.length()); // Позиционирование курсора в конец строки
        }

        saveBtn.setOnClickListener(v -> {
            String resultText = editText.getText().toString();
            Intent resultIntent = new Intent();
            resultIntent.putExtra("NOTE_TEXT", resultText);
            setResult(RESULT_OK, resultIntent); // Возврат результата вызывающему экрану
            finish();
        });
    }
}
