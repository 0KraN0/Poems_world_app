package com.example.poems.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.poems.R;
import com.example.poems.model.Poem;
import java.util.ArrayList;
import java.util.List;

/**
 * Кастомный адаптер RecyclerView с поддержкой оптимизации DiffUtil.
 */
public class PoemAdapter extends RecyclerView.Adapter<PoemAdapter.ViewHolder> {
    private final List<Poem> poems = new ArrayList<>();
    private final OnPoemClickListener listener;

    public interface OnPoemClickListener {
        void onPoemClick(Poem poem);
    }

    public PoemAdapter(OnPoemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Обновляет список элементов с расчетом минимальной разницы изменений.
     */
    public void setPoems(List<Poem> newPoems) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() { return poems.size(); }
            @Override
            public int getNewListSize() { return newPoems.size(); }
            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return poems.get(oldItemPosition).getId() == newPoems.get(newItemPosition).getId();
            }
            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                Poem oldPoem = poems.get(oldItemPosition);
                Poem newPoem = newPoems.get(newItemPosition);
                return oldPoem.getTitle().equals(newPoem.getTitle()) &&
                        oldPoem.getAuthor().equals(newPoem.getAuthor()) &&
                        oldPoem.isFavorite() == newPoem.isFavorite() &&
                        oldPoem.getNote().equals(newPoem.getNote());
            }
        });
        this.poems.clear();
        this.poems.addAll(newPoems);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_poem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Poem poem = poems.get(position);
        holder.titleText.setText(poem.getTitle());
        holder.authorText.setText(poem.getAuthor());
        holder.categoryText.setText(poem.getCategory());
        holder.itemView.setOnClickListener(v -> listener.onPoemClick(poem));
    }

    @Override
    public int getItemCount() { return poems.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, authorText, categoryText;
        ViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.poemTitle);
            authorText = itemView.findViewById(R.id.poemAuthor);
            categoryText = itemView.findViewById(R.id.poemCategory);
        }
    }
}