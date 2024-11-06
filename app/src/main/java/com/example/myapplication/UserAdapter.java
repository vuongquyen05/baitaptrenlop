package com.example;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.User;
import com.example.myapplication.R;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private final List<User> userList;
    private final Context context;
    private final FirebaseFirestore db;

    public UserAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.tvEmail.setText(user.getEmail());
        holder.tvName.setText(user.getName());

        // Xử lý sự kiện click để chuyển sang EditUserActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditUserActivity.class);
            intent.putExtra("USER_ID", user.getUid()); // Chuyển ID người dùng qua Intent
            intent.putExtra("USER_EMAIL", user.getEmail());
            intent.putExtra("USER_NAME", user.getName());
            context.startActivity(intent);
        });

        // Xử lý sự kiện long click để xóa người dùng
        holder.itemView.setOnLongClickListener(v -> {
            deleteUser(user, position); // Truyền vị trí để cập nhật danh sách sau khi xóa
            return true;
        });
    }

    private void deleteUser(User user, int position) {
        db.collection("users").document(user.getUid()).delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "User deleted", Toast.LENGTH_SHORT).show();
                    userList.remove(position);  // Xóa người dùng khỏi danh sách
                    notifyItemRemoved(position);  // Cập nhật RecyclerView sau khi xóa
                    notifyItemRangeChanged(position, userList.size()); // Làm mới các vị trí còn lại
                })
                .addOnFailureListener(e -> Toast.makeText(context, "Error deleting user: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmail, tvName;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}
