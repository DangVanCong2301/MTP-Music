package com.example.spotifyapp.activities.admin;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.example.spotifyapp.databinding.ActivityCategoryAddBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class CategoryAddActivity extends BaseActivity {

    private ActivityCategoryAddBinding binding;
    private ProgressDialog progressDialog;
    private Uri imgUri = null;
    private static final String TAG = "ADD_CATEGORY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpProgressDialog();
        initListener();

    }

    private void initListener() {
        binding.btnBackCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.imgCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageAttackMenu();
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImageToStorage();
            }
        });
    }

    private void setUpProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Làm ơn đợi");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    String categoryName = "";
    private void uploadImageToStorage() {
        Log.d(TAG, "uploadImageToStorage: running");
        progressDialog.setMessage("Đang tải ảnh lên....");
        progressDialog.show();

        categoryName = binding.edtCategory.getText().toString().trim();
        // lấy thời gian thực
        long timestamp = System.currentTimeMillis();

        // Tên ường dẫn ảnh, sử dụng uid
        String filePathAndName = "CategoryImages/" + mAuth.getUid();

        // Luu trữ
        StorageReference ref = mStorage.getReference(filePathAndName);
        ref.putFile(imgUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "onSuccess: Ảnh danh mục được tải lên");
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        String uploadImageUri = "" + uriTask.getResult();
                        Log.d(TAG, "onSuccess: Tải đường dẫn ảnh " + uploadImageUri);

                        // Tải thông tin lên db
                        uploadCategoryIntoDatabase(uploadImageUri, timestamp);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(CategoryAddActivity.this, "Tải ảnh thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
        ;
    }

    private void uploadCategoryIntoDatabase(String uploadImageCategoryUri, long timestamp) {
        progressDialog.setMessage("Đang thêm thể loại....");
        progressDialog.show();

        // Đặt thông tin lưu trên Firebase
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", "" + timestamp);
        hashMap.put("categoryName", "" + categoryName);
        hashMap.put("imageUrl", "" + uploadImageCategoryUri);
        hashMap.put("timestamp", timestamp);
        hashMap.put("uid", "" + mAuth.getUid());

        // Thêm thông tin trên cơ sở dữ liệu: Root -> Categories -> categoryID -> categoryInfo
        DatabaseReference ref = database.getReference("Categories");
        ref.child("" + timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(CategoryAddActivity.this, "Thêm danh muc thành công...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CategoryAddActivity.this, DashboardAdminActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CategoryAddActivity.this, "Thêm thất bại! Mã lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showImageAttackMenu() {
        // Khởi tạo/đặt popup menu
        PopupMenu popupMenu = new PopupMenu(this, binding.imgCategory);
        popupMenu.getMenu().add(Menu.NONE, 0, 0, "Máy ảnh");
        popupMenu.getMenu().add(Menu.NONE, 1, 1, "Thư viện");

        popupMenu.show();

        // Xử lý menu item clicks
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
        // intent chọn ảnh từ thư viện
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    // sử dụng xử lý kết quả của ý định lấy trong thư viện
                    // lấy uri của ảnh
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        Log.d(TAG, "imgUri: " + imgUri);
                        Intent data = o.getData();
                        imgUri = data.getData();
                        Log.d(TAG, "imgUri Pick Gallery: " + imgUri);
                        binding.imgCategory.setImageURI(imgUri);
                    } else {
                        Toast.makeText(CategoryAddActivity.this, "Đã thoát", Toast.LENGTH_SHORT).show();
                    }
                }
            }

    );
}