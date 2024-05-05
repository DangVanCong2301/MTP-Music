package com.example.spotifyapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifyapp.databinding.ItemStudentBinding;
import com.example.spotifyapp.models.Student;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    private ItemStudentBinding binding;
    private Context context;
    private ArrayList<Student> items;

    public StudentAdapter(Context context, ArrayList<Student> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public StudentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        binding = ItemStudentBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAdapter.ViewHolder holder, int position) {
        Student student = items.get(position);
        if (student == null) {
            return;
        }
        holder.binding.tvStudentId.setText(student.getId() + "");
        holder.binding.tvStudentName.setText(student.getName());
        holder.binding.tvAverage.setText(student.getAverage() + "");
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemStudentBinding binding;
        public ViewHolder(@NonNull ItemStudentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
