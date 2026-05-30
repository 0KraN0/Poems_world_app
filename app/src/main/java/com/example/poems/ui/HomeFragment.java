package com.example.poems.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.poems.R;
import com.example.poems.model.Poem;
import com.example.poems.utils.JsonHelper;
import java.util.List;

/**
 * Фрагмент стартового экрана «Произведение дня».
 */
public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        TextView titleTv = view.findViewById(R.id.featuredTitle);
        TextView authorTv = view.findViewById(R.id.featuredAuthor);
        TextView textTv = view.findViewById(R.id.featuredText);
        View card = view.findViewById(R.id.featuredCard);

        List<Poem> poems = JsonHelper.readPoems(requireContext());
        if (!poems.isEmpty()) {
            // Вывод первого элемента в качестве заглавного произведения дня
            Poem featured = poems.get(0);
            titleTv.setText(featured.getTitle());
            authorTv.setText(featured.getAuthor());
            textTv.setText(featured.getText());
            card.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), PoemDetailActivity.class);
                intent.putExtra("POEM_ID", featured.getId());
                startActivity(intent);
            });
        }
        return view;
    }
}