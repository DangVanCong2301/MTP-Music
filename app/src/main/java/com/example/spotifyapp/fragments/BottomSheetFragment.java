package com.example.spotifyapp.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifyapp.R;
import com.example.spotifyapp.activities.ListeningActivity;
import com.example.spotifyapp.adapters.CommentAdapter;
import com.example.spotifyapp.models.Comment;
import com.example.spotifyapp.models.Song;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    private ArrayList<Comment> commentArrayList;
    private CommentAdapter commentAdapter;
    private ListeningActivity mListeningActivity;
    private Button btnComment;
    private ImageButton btnCloseBottomSheet;
    private EditText edtComment;
    private RecyclerView rcvComment;
    private ProgressDialog progressDialog;
    private ProgressBar progressBarBottomSheet;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_bottom_sheet, null);
        bottomSheetDialog.setContentView(view);

        mListeningActivity = (ListeningActivity) getActivity();

        initUI(view);
        initProgressDialog();
        loadComments();
        initListener();

        return bottomSheetDialog;
    }

    private void initUI(View view) {
        btnComment = view.findViewById(R.id.btn_comment_bottom);
        btnCloseBottomSheet = view.findViewById(R.id.btn_close_bottom_sheet);
        edtComment = view.findViewById(R.id.edt_comment);
        rcvComment = view.findViewById(R.id.rcv_comment);
        progressBarBottomSheet = view.findViewById(R.id.prg_bottom_sheet);
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(mListeningActivity);
        progressDialog.setTitle("Làm ơn đợi");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void initListener() {
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListeningActivity.getUid() == null) {
                    Toast.makeText(mListeningActivity, "Bạn phải đăng nhập mới được bình luận", Toast.LENGTH_SHORT).show();
                } else {
                    addCommentSong();
                }
            }
        });

        btnCloseBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("send_action_close");
                Bundle bundle = new Bundle();
                bundle.putInt("close", 1);
                intent.putExtras(bundle);
                LocalBroadcastManager.getInstance(mListeningActivity).sendBroadcast(intent);
            }
        });
    }

    String comment = "";
    private void addCommentSong() {
        comment = edtComment.getText().toString().trim();
        if (TextUtils.isEmpty(comment)) {
            Toast.makeText(mListeningActivity, "Bạn phải nhập bình luận!!!", Toast.LENGTH_SHORT).show();
        } else {
            addComment();
        }
    }

    private void addComment() {
        progressDialog.setMessage("Đang thêm bình luận");
        progressDialog.show();

        // Lấy thời gian thực
        String timestamp = "" + System.currentTimeMillis();
        // Lấy id bài hát
        String songId = mListeningActivity.getSongId();
        // Lấy uid người dùng
        String uid = mListeningActivity.getUid();

        // Đặt lại dữ liệu
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", "" + timestamp);
        hashMap.put("songId", "" + songId);
        hashMap.put("timestamp", "" + timestamp);
        hashMap.put("comment", "" + comment);
        hashMap.put("uid", "" + uid);

        // Lưu dữ liệu trên Firebase
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Songs");
        ref.child(songId).child("Comments").child(timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(mListeningActivity, "Thêm bình luận thành công!", Toast.LENGTH_SHORT).show();
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(mListeningActivity, "Thêm bình luận thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadComments() {
        commentArrayList = new ArrayList<>();
        progressBarBottomSheet.setVisibility(View.VISIBLE);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Songs");
        ref.child(mListeningActivity.getSongId())
                .child("Comments")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        commentArrayList.clear();
                        if (snapshot.exists()) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                Log.d("cong", "commentData: " + ds);
                                Comment comment = ds.getValue(Comment.class);
                                commentArrayList.add(comment);
                            }
                            commentAdapter = new CommentAdapter(mListeningActivity, commentArrayList);
                            rcvComment.setAdapter(commentAdapter);
                            progressBarBottomSheet.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
