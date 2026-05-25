package vn.edu.tinhoc123.quanlydoibong;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText etNewPassword, etConfirmPassword;
    private Button btnUpdatePassword;
    private FirebaseFirestore db;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        db = FirebaseFirestore.getInstance();
        email = getIntent().getStringExtra("email");

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Không tìm thấy email", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);

        btnUpdatePassword.setOnClickListener(v -> {
            updatePassword();
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void updatePassword() {
        String newPass = etNewPassword.getText().toString().trim();
        String confirmPass = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(newPass)) {
            etNewPassword.setError("Vui lòng nhập mật khẩu mới");
            return;
        }

        if (newPass.length() < 6) {
            etNewPassword.setError("Mật khẩu phải tối thiểu 6 ký tự");
            return;
        }

        if (TextUtils.isEmpty(confirmPass)) {
            etConfirmPassword.setError("Vui lòng xác nhận mật khẩu");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            etConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            return;
        }

        btnUpdatePassword.setEnabled(false);

        // Tìm user theo email và cập nhật mật khẩu demo
        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    String docId = task.getResult().getDocuments().get(0).getId();
                    
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("passwordDemo", newPass);
                    
                    db.collection("users").document(docId)
                        .update(updates)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Cập nhật mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            // Quay lại LoginActivity và xóa stack
                            Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            btnUpdatePassword.setEnabled(true);
                            Toast.makeText(this, "Lỗi cập nhật: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                } else {
                    btnUpdatePassword.setEnabled(true);
                    Toast.makeText(this, "Không tìm thấy tài khoản người dùng", Toast.LENGTH_SHORT).show();
                }
            });
    }
}