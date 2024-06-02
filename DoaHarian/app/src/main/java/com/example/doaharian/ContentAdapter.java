package com.example.doaharian;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {
    private List<Content> contentList;
    private List<Content> filteredContents; // Daftar konten yang difilter
    private Context context;
    private Consumer<Content> removeFavoriteCallback;

    public ContentAdapter(List<Content> contentList, Context context, Consumer<Content> removeFavoriteCallback) {
        this.contentList = contentList;
        this.filteredContents = new ArrayList<>(contentList); // Inisialisasi filteredContents
        this.context = context;
        this.removeFavoriteCallback = removeFavoriteCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doa_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Content content = filteredContents.get(position); // Gunakan filteredContents
        holder.doaTextView.setText(content.getDoa());
        holder.ayatTextView.setText(content.getAyat());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("doa", content.getDoa());
            intent.putExtra("ayat", content.getAyat());
            intent.putExtra("latin", content.getLatin());
            intent.putExtra("artinya", content.getArtinya());
            context.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(v -> {
            removeFavoriteCallback.accept(content);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return filteredContents.size(); // Gunakan filteredContents untuk mendapatkan jumlah item
    }

    public void updateData(List<Content> newContentList) {
        contentList = newContentList;
        filterByDoaPrefix(""); // Perbarui tampilan setelah data diperbarui
    }

    public void filterByDoaPrefix(String keyword) {
        if (keyword.isEmpty()) {
            filteredContents.clear();
            filteredContents.addAll(contentList);
        } else {
            filteredContents.clear();
            String lowerCaseKeyword = keyword.toLowerCase();
            for (Content content : contentList) {
                String[] words = content.getDoa().toLowerCase().split("\\s+");
                for (String word : words) {
                    if (word.startsWith(lowerCaseKeyword)) {
                        filteredContents.add(content);
                        break;
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView doaTextView;
        TextView ayatTextView;

        ViewHolder(View itemView) {
            super(itemView);
            doaTextView = itemView.findViewById(R.id.doaTV);
            ayatTextView = itemView.findViewById(R.id.ayatTV);
        }
    }
}
