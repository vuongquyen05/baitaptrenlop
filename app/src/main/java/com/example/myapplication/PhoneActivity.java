package com.example.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneActivity extends AppCompatActivity {

    private EditText etPhoneNumber, etOtp;
    private Button btnGetOtp, btnLogin;
    private FirebaseAuth mAuth;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        // Khởi tạo FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etOtp = findViewById(R.id.etOtp);
        btnGetOtp = findViewById(R.id.btnGetOtp);
        btnLogin = findViewById(R.id.btnLogin);

        // Khởi tạo PhoneAuthProvider
        btnGetOtp.setOnClickListener(view -> getOTP());

        btnLogin.setOnClickListener(view -> {
            String otp = etOtp.getText().toString().trim();
            if (TextUtils.isEmpty(otp)) {
                Toast.makeText(PhoneActivity.this, "Vui lòng nhập OTP", Toast.LENGTH_SHORT).show();
                return;
            }
            verifyCode(otp);
        });
    }

    // Hàm getOTP để yêu cầu Firebase gửi OTP
    private void getOTP() {
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(PhoneActivity.this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
            return;
        }

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+84" + phoneNumber) // Số điện thoại để xác minh
                .setTimeout(60L, TimeUnit.SECONDS) // Giới hạn thời gian
                .setActivity(this) // Activity khởi tạo xác minh
                .setCallbacks(mCallbacks) // Callbacks khi có kết quả
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    // Callbacks xử lý kết quả từ Firebase
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            // Tự động xác thực thành công
            String code = credential.getSmsCode();
            if (code != null) {
                etOtp.setText(code); // Hiển thị OTP tự động điền (nếu có)
                verifyCode(code); // Xác minh mã OTP
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(PhoneActivity.this, "Xác minh thất bại: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(s, token);
            verificationId = s; // Lưu mã xác minh để dùng sau
            Toast.makeText(PhoneActivity.this, "Mã OTP đã được gửi", Toast.LENGTH_SHORT).show();
        }
    };

    // Hàm xác thực OTP
    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    // Đăng nhập với PhoneAuthCredential
    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(PhoneActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        // Chuyển sang màn hình chính hoặc xử lý sau khi đăng nhập thành công
                    } else {
                        Toast.makeText(PhoneActivity.this, "Đăng nhập thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
