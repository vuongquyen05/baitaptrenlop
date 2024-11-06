package com.example;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;

public class EditUserActivity extends AppCompatActivity {
    private EditText etEditEmail, etEditName;
    private Button btnUpdateUser;
    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        // Lấy thông tin người dùng từ Intent
        userId = getIntent().getStringExtra("USER_ID");
        String email = getIntent().getStringExtra("USER_EMAIL");
        String name = getIntent().getStringExtra("USER_NAME");

        etEditEmail = findViewById(R.id.etEditEmail);
        etEditName = findViewById(R.id.etEditName);
        btnUpdateUser = findViewById(R.id.btnUpdateUser);

        // Điền thông tin hiện tại vào EditText
        etEditEmail.setText(email);
        etEditName.setText(name);

        // Cập nhật người dùng khi nhấn nút
        btnUpdateUser.setOnClickListener(view -> updateUser());
    }

    private void updateUser() {
        String email = etEditEmail.getText().toString().trim();
        String name = etEditName.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        DocumentReference userRef = db.collection("users").document(userId);
        userRef.update("email", email, "name", name)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EditUserActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    finish(); // Trở lại activity trước đó
                })
                .addOnFailureListener(e -> Toast.makeText(EditUserActivity.this, "Cập nhật thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
