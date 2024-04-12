package com.example.spotifyapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifyapp.databinding.ItemCategoryDiscoverBinding;
import com.example.spotifyapp.models.Category;

import java.util.ArrayList;

public class DiscoverCategoryAdapter extends RecyclerView.Adapter<DiscoverCategoryAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Category> items;

    public DiscoverCategoryAdapter(Context context, ArrayList<Category> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public DiscoverCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ItemCategoryDiscoverBinding binding = ItemCategoryDiscoverBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoverCategoryAdapter.ViewHolder holder, int position) {
        Category category = items.get(position);
        if (category == null) {
            return;
        }
        String categoryName = category.getCategoryName();
        String id = category.getId();
        String imageUrl = category.getImageUrl();
        
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemCategoryDiscoverBinding binding;
        public ViewHolder(@NonNull ItemCategoryDiscoverBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
