package com.example.poems.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Фрагмент экрана закладок «Избранное».
 */
public class FavoritesFragment extends Fragment {
    private PoemAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.favoritesRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PoemAdapter(poem -> {
            Intent intent = new Intent(getContext(), PoemDetailActivity.class);
            intent.putExtra("POEM_ID", poem.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        List<Poem> all = JsonHelper.readPoems(requireContext());
        List<Poem> favs = new ArrayList<>();
        for (Poem p : all) {
            if (p.isFavorite()) {
                favs.add(p);
            }
        }
        adapter.setPoems(favs);
    }
}