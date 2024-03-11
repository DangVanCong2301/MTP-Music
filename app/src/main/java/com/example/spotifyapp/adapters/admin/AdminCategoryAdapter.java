package com.example.spotifyapp.adapters.admin;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.spotifyapp.databinding.ItemCategoryAdminBinding;
import com.example.spotifyapp.models.Category;

import java.util.ArrayList;

public class AdminCategoryAdapter extends RecyclerView.Adapter<AdminCategoryAdapter.ViewHolder> {
    private Context context;
    ArrayList<Category> items;

    public AdminCategoryAdapter(Context context, ArrayList<Category> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public AdminCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ItemCategoryAdminBinding binding = ItemCategoryAdminBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminCategoryAdapter.ViewHolder holder, int position) {
        Category category = items.get(position);
        if (category == null) {
            return;
        }
        String categoryName = category.getCategoryName();
        String id = category.getId();
        String imageUrl = category.getImageUrl();

        holder.binding.tvCategoryName.setText(categoryName);
        Glide.with(context)
                .load(imageUrl)
                .transform(new CenterCrop(), new RoundedCorners(10))
                .into(holder.binding.imgCategory);
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemCategoryAdminBinding binding;
        public ViewHolder(@NonNull ItemCategoryAdminBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
