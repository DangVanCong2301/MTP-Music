package com.example.spotifyapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.spotifyapp.R;
import com.example.spotifyapp.databinding.ActivityAddStudentBinding;
import com.example.spotifyapp.databinding.ActivityStudentBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AddStudentActivity extends BaseActivity {
    private ActivityAddStudentBinding binding;
    private int studentCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference ref = database.getReference("Students");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                studentCount = (int) ds.getChildrenCount();
                            }
                            addStudent(studentCount);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    private void addStudent(int studentCount) {
        Log.d("add_student", "addStudent: " + studentCount);
        String name = binding.edrStudentName.getText().toString().trim();
        String birthday = binding.edtStudentBirth.getText().toString().trim();
        String email = binding.edtStudentEmail.getText().toString().trim();
        String agv = binding.edtStudentAvg.getText().toString().trim();

        long id = System.currentTimeMillis();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", studentCount + 1);
        hashMap.put("name", name);
        hashMap.put("birthday", birthday);
        hashMap.put("email", email);
        hashMap.put("average", Double.parseDouble(agv));
        hashMap.put("address", "");

        DatabaseReference ref = database.getReference("Students");
        ref.child("" + id)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddStudentActivity.this, "Thêm sinh viên thành công!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddStudentActivity.this, StudentActivity.class));
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddStudentActivity.this, "Thêm sinh viên thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}