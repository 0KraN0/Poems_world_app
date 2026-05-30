package com.example.poems.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.poems.R;
import com.example.poems.adapter.PoemAdapter;
import com.example.poems.model.Poem;
import com.example.poems.utils.JsonHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * Фрагмент полного каталога стихов со встроенным символьным поиском.
 */
public class PoemsListFragment extends Fragment {
    private PoemAdapter adapter;
    private List<Poem> allPoems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poems_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        EditText searchEt = view.findViewById(R.id.searchEditText);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PoemAdapter(poem -> {
            Intent intent = new Intent(getContext(), PoemDetailActivity.class);
            intent.putExtra("POEM_ID", poem.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { filter(s.toString()); }
            @Override public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        allPoems = JsonHelper.readPoems(requireContext());
        adapter.setPoems(allPoems);
    }

    private void filter(String query) {
        List<Poem> filtered = new ArrayList<>();
        for (Poem p : allPoems) {
            if (p.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    p.getAuthor().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(p);
            }
        }
        adapter.setPoems(filtered);
    }
}