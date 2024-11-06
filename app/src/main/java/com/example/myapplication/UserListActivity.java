package com.example;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.User;
import com.example.myapplication.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {
    private RecyclerView recyclerViewUsers;
    private UserAdapter userAdapter;
    private List<User> userList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        // Khởi tạo FirebaseFirestore
        db = FirebaseFirestore.getInstance();
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        Button btnAddUser = findViewById(R.id.btnAddUser);

        // Setup RecyclerView
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList, this);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewUsers.setAdapter(userAdapter);

        btnAddUser.setOnClickListener(view -> {
            Intent intent = new Intent(UserListActivity.this, AddUserActivity.class);
            startActivity(intent);
        });

        loadUsers();
    }

    private void loadUsers() {
        db.collection("users").addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                Toast.makeText(UserListActivity.this, "Error loading users", Toast.LENGTH_SHORT).show();
                return;
            }

            if (snapshot != null) {
                userList.clear();
                for (DocumentSnapshot document : snapshot.getDocuments()) { // Sửa tại đây
                    User user = document.toObject(User.class);
                    userList.add(user);
                }
                userAdapter.notifyDataSetChanged();
            }
        });
    }
}
