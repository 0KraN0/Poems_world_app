package com.example.poems.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.poems.R;
import com.example.poems.model.Poem;
import com.example.poems.utils.JsonHelper;
import java.util.List;

/**
 * Экран детального просмотра стихотворения, реализующий системные вызовы.
 */
public class PoemDetailActivity extends AppCompatActivity {
    private Poem poem;
    private int poemId;
    private TextView titleTv, authorTv, textTv, noteTv;
    private ImageView favoriteIcon;
    private Button addNoteBtn, deleteNoteBtn;
    private static final String CHANNEL_ID = "favorite_channel";

    // Регистрация контракта Activity Result API для безопасного получения заметки
    private final ActivityResultLauncher<Intent> addNoteLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData()!= null) {
                    String noteText = result.getData().getStringExtra("NOTE_TEXT");
                    if (noteText!= null) {
                        poem.setNote(noteText);
                        updateNoteUI();
                        saveCurrentPoemState();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poem_detail);

        poemId = getIntent().getIntExtra("POEM_ID", -1);
        titleTv = findViewById(R.id.detailTitle);
        authorTv = findViewById(R.id.detailAuthor);
        textTv = findViewById(R.id.detailText);
        noteTv = findViewById(R.id.detailNote);
        favoriteIcon = findViewById(R.id.favoriteIcon);
        addNoteBtn = findViewById(R.id.btnAndNote);
        deleteNoteBtn = findViewById(R.id.btnDeleteNote);

        loadPoemData();

        favoriteIcon.setOnClickListener(v -> toggleFavorite());

        addNoteBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddNoteActivity.class);
            intent.putExtra("CURRENT_NOTE", poem.getNote());
            addNoteLauncher.launch(intent); // Безопасный запуск дочернего экрана
        });

        deleteNoteBtn.setOnClickListener(v -> showDeleteConfirmDialog());
    }

    private void loadPoemData() {
        List<Poem> poems = JsonHelper.readPoems(this);
        for (Poem p : poems) {
            if (p.getId() == poemId) {
                poem = p;
                break;
            }
        }
        if (poem!= null) {
            titleTv.setText(poem.getTitle());
            authorTv.setText(poem.getAuthor());
            textTv.setText(poem.getText());
            updateNoteUI();
            updateFavoriteIcon();
        }
    }

    private void updateNoteUI() {
        if (poem.getNote() == null || poem.getNote().trim().isEmpty()) {
            noteTv.setVisibility(View.GONE);
            deleteNoteBtn.setVisibility(View.GONE);
            addNoteBtn.setText("Добавить заметку");
        } else {
            noteTv.setText("Моя заметка:\n" + poem.getNote());
            noteTv.setVisibility(View.VISIBLE);
            deleteNoteBtn.setVisibility(View.VISIBLE);
            addNoteBtn.setText("Изменить заметку");
        }
    }

    private void updateFavoriteIcon() {
        if (poem.isFavorite()) {
            favoriteIcon.setImageResource(R.drawable.ic_heart_filled);
        } else {
            favoriteIcon.setImageResource(R.drawable.ic_heart_outline);
        }
    }

    private void toggleFavorite() {
        boolean newState =!poem.isFavorite();
        poem.setFavorite(newState);
        updateFavoriteIcon();
        saveCurrentPoemState();

    }

    /**
     * Отображает предупреждающий диалог перед деструктивным действием (FR-07).
     */
    private void showDeleteConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Удалить заметку?")
                .setMessage("Данное действие необратимо. Вы действительно хотите удалить заметку к стихотворению?")
                .setPositiveButton("Удалить", (dialog, which) -> {
                    poem.setNote("");
                    updateNoteUI();
                    saveCurrentPoemState();
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void saveCurrentPoemState() {
        List<Poem> poems = JsonHelper.readPoems(this);
        for (int i = 0; i < poems.size(); i++) {
            if (poems.get(i).getId() == poem.getId()) {
                poems.set(i, poem);
                break;
            }
        }
        JsonHelper.savePoems(this, poems);
    }
}