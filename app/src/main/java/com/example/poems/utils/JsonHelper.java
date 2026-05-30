package com.example.poems.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.poems.model.Poem;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Вспомогательный класс для дисковых операций чтения и записи JSON-базы данных.
 */
public class JsonHelper {
    private static final String FILE_NAME = "poems.json";
    private static final String TAG = "JsonHelper";

    /**
     * Копирует базу данных из папки assets во внутреннюю память устройства при первом запуске.
     */
    public static void initDatabase(Context context) {
        File file = new File(context.getFilesDir(), FILE_NAME);
        if (!file.exists()) {
            try {
                InputStream is = context.getAssets().open(FILE_NAME);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int read;
                while ((read = is.read(buffer))!= -1) {
                    fos.write(buffer, 0, read);
                }
                is.close();
                fos.flush();
                fos.close();
                Log.d(TAG, "База данных успешно перенесена во внутреннюю память.");
            } catch (IOException e) {
                Log.e(TAG, "Критическая ошибка инициализации базы данных", e);
            }
        }
    }

    /**
     * Читает и парсит список стихотворений из локального JSON-файла.
     */
    public static List<Poem> readPoems(Context context) {
        List<Poem> list = new ArrayList<>();
        File file = new File(context.getFilesDir(), FILE_NAME);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine())!= null) {
                sb.append(line);
            }
            reader.close();

            JSONArray array = new JSONArray(sb.toString());
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                list.add(new Poem(
                        obj.getInt("id"),
                        obj.getString("title"),
                        obj.getString("author"),
                        obj.getString("category"),
                        obj.getString("text"),
                        obj.getBoolean("isFavorite"),
                        obj.getString("note")
                ));
            }
        } catch (Exception e) {
            Log.e(TAG, "Ошибка чтения или парсинга JSON-файла", e);
            Toast.makeText(context, "Ошибка чтения базы данных. Проверьте файл poems.json", Toast.LENGTH_SHORT).show();
        }
        return list;
    }

    /**
     * Перезаписывает JSON-файл актуальным набором данных из оперативной памяти.
     */
    public static void savePoems(Context context, List<Poem> list) {
        try {
            JSONArray array = new JSONArray();
            for (Poem p : list) {
                JSONObject obj = new JSONObject();
                obj.put("id", p.getId());
                obj.put("title", p.getTitle());
                obj.put("author", p.getAuthor());
                obj.put("category", p.getCategory());
                obj.put("text", p.getText());
                obj.put("isFavorite", p.isFavorite());
                obj.put("note", p.getNote());
                array.put(obj);
            }
            File file = new File(context.getFilesDir(), FILE_NAME);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(array.toString(4)); // Запись с форматированием в 4 пробела [26]
            writer.close();
        } catch (Exception e) {
            Log.e(TAG, "Ошибка сохранения изменений в локальный файл", e);
        }
    }
}