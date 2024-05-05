package com.example.spotifyapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.spotifyapp.R;
import com.example.spotifyapp.adapters.StudentAdapter;
import com.example.spotifyapp.databinding.ActivityStudentBinding;
import com.example.spotifyapp.models.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentActivity extends BaseActivity {
    private ActivityStudentBinding binding;
    private ArrayList<Student> list;
    private StudentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadStudents();
        initListener();

    }

    private void initListener() {
        binding.btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentActivity.this, AddStudentActivity.class));
            }
        });
    }

    private void loadStudents() {
        list = new ArrayList<>();
        DatabaseReference ref = database.getReference("Students");
        // startAt()	Trả về các mục lớn hơn hoặc bằng khóa hoặc giá trị được chỉ định tùy thuộc vào phương pháp theo thứ tự đã chọn.
        // Nguồn : https://firebase.google.com/docs/database/android/lists-of-data?hl=vi
        ref.orderByChild("average").startAt(8).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Log.d("student", "onDataChange: " + ds);
                        Student student = ds.getValue(Student.class);
                        list.add(student);
                    }
                    if (list.size() > 0) {
                        adapter = new StudentAdapter(StudentActivity.this, list);
                        binding.rcvListStudent.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}