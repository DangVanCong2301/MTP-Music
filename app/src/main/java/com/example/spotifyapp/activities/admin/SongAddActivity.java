package com.example.spotifyapp.activities.admin;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.spotifyapp.R;
import com.example.spotifyapp.activities.BaseActivity;
import com.example.spotifyapp.databinding.ActivitySongAddBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SongAddActivity extends BaseActivity {
    private ActivitySongAddBinding binding;
    private ProgressDialog progressDialog;
    private ArrayList<String> categoryTitleArraylist, categoryIdArrayList;
    private static final int SONG_PICK_CODE = 1000;
    private Uri songUri = null;
    private Uri imgSongUri = null;
    private static final String TAG = "ADD_SONG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySongAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initListener();
        loadSongCategory();
        setUpProgressDialog();

    }

    private void initListener() {
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.tvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryPickDialog();
            }
        });

        binding.btnAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                songPickIntent();
            }
        });

        binding.imgSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageAttackMenu();
            }
        });

        binding.btnSubmitSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadSongToStorage();
            }
        });
    }

    private void setUpProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Làm ơn đợi giây lát....");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void showImageAttackMenu() {
        // Khởi tạo, đặt popup menu
        PopupMenu popupMenu = new PopupMenu(this, binding.imgSong);
        popupMenu.getMenu().add(Menu.NONE, 0, 0, "Máy ảnh");
        popupMenu.getMenu().add(Menu.NONE, 1, 1, "Thư viện");

        popupMenu.show();

        // Xử lý menu item cliks
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int which = menuItem.getItemId();
                if (which == 0) {
                    // máy ảnh
                    pickImageCamera();
                } else if (which == 1) {
                    // lấy ảnh trong thư viện
                    pickImageGallery();
                }
                return false;
            }
        });
    }

    private void pickImageCamera() {

    }

    private void pickImageGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> galleryActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    // lấy uri của ảnh
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        Log.d(TAG, "imgSongUri: " + imgSongUri);
                        Intent data = o.getData();
                        imgSongUri = data.getData();
                        Log.d(TAG, "imgSongUri Pick Gallery: " + imgSongUri);
                        binding.imgSong.setImageURI(imgSongUri);
                    } else {
                        Toast.makeText(SongAddActivity.this, "Đã thoát...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private String selectedCategoryId, selectedCategoryTitle;
    private void categoryPickDialog() {
        String[] categoriesArray = new String[categoryTitleArraylist.size()];
        for (int i = 0; i < categoryTitleArraylist.size(); i++) {
            categoriesArray[i] = categoryTitleArraylist.get(i);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn danh mục")
                .setItems(categoriesArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedCategoryTitle = categoryTitleArraylist.get(i);
                        selectedCategoryId = categoryIdArrayList.get(i);
                        // đặt lại text cho danh mục
                        binding.tvCategory.setText(selectedCategoryTitle);
                    }
                })
                .show();
    }

    private void loadSongCategory() {
        categoryTitleArraylist = new ArrayList<>();
        categoryIdArrayList = new ArrayList<>();

        DatabaseReference ref = database.getReference("Categories");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryTitleArraylist.clear();
                categoryIdArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    // Lấy dữ liệu
                    String categoryId = "" + ds.child("id").getValue();
                    String categoryTitle = "" + ds.child("categoryName").getValue();
                    // thêm vào list
                    categoryTitleArraylist.add(categoryTitle);
                    categoryIdArrayList.add(categoryId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void songPickIntent() {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn bài hát"), SONG_PICK_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SONG_PICK_CODE){
                songUri = data.getData();
                Log.d(TAG, "songUri: " + songUri);
            } else {
                Toast.makeText(this, "Đã huỷ chọn bài hát", Toast.LENGTH_SHORT).show();
            }
        }
    }

    String songName = "", songArtist = "";
    private void uploadSongToStorage() {
        // Lấy dữ liệu
        songName = binding.edtSongName.getText().toString().trim();
        songArtist = binding.edtSongArtist.getText().toString().trim();
        progressDialog.setMessage("Đang tải nhạc....");
        progressDialog.show();

        // thời gian thực
        long timestamp = System.currentTimeMillis();

        // Đường dẫn tệp nhạc trên firebase
        String filePathAndName = "SongImages/" + timestamp;
        Log.d(TAG, "filePathAndName: " + filePathAndName);
        // Kho luu trữ
        StorageReference storageReference = mStorage.getReference(filePathAndName);
        storageReference.putFile(imgSongUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "onSuccess: Song Upload");

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        String uploadImageSongUri = "" + uriTask.getResult();
                        Log.d(TAG, "uploadSongUri: " + uploadImageSongUri);

                        // Tải lên db
                        uploadSongIntoDatabase(uploadImageSongUri, timestamp);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(SongAddActivity.this, "Không tải được nhạc....", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadSongIntoDatabase(String uploadImageSongUri, long timestamp) {
        progressDialog.setMessage("Đang tải thông tin nhạc....");
        progressDialog.show();

        String uid = mAuth.getUid();

        //Đặt dữ liệu
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", "" + uid);
        hashMap.put("id", "" + timestamp);
        hashMap.put("songName", "" + songName);
        hashMap.put("songArtist", "" + songArtist);
        hashMap.put("categoryId", "" + selectedCategoryId);
        hashMap.put("imageUrl", "" + uploadImageSongUri);
        hashMap.put("url", "");
        hashMap.put("timestamp", timestamp);
        hashMap.put("isBest", true);
        hashMap.put("listensCount", 0);
        hashMap.put("downloadsCount", 0);

        // Dữ liệu -> Songs
        DatabaseReference ref = database.getReference("Songs");
        ref.child("" + timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(SongAddActivity.this, "Tải nhạc thành công....", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(SongAddActivity.this, "Tải nhạc lên thất bại....", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}