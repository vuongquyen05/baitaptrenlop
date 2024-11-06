package com.example;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.duyle.lab1md19304.model.User;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddUserActivity extends AppCompatActivity {

    private EditText etEmail, etName;
    private Button btnSave;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        // Khởi tạo Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Liên kết các view
        etEmail = findViewById(R.id.etEmail);
        etName = findViewById(R.id.etName);
        btnSave = findViewById(R.id.btnSave);

        // Thiết lập sự kiện click cho nút Lưu
        btnSave.setOnClickListener(v -> addUser());
    }

    private void addUser() {
        String email = etEmail.getText().toString().trim();
        String name = etName.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thêm người dùng vào Firestore với một tài liệu mới
        User user = new User(null, email, name); // Tạo User với uid tạm thời là null
        db.collection("users")
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    String uid = documentReference.getId(); // Lấy ID từ tài liệu mới
                    documentReference.update("uid", uid); // Lưu ID vào trường uid
                    Toast.makeText(AddUserActivity.this, "Người dùng đã được thêm thành công", Toast.LENGTH_SHORT).show();
                    finish(); // Đóng Activity sau khi thêm thành công
                })
                .addOnFailureListener(e -> Toast.makeText(AddUserActivity.this, "Thêm người dùng thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
