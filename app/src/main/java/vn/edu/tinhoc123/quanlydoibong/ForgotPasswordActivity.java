package vn.edu.tinhoc123.quanlydoibong;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private TextView tvEmail;
    private Button btnSendReset;
    private FirebaseAuth mAuth;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();
        email = getIntent().getStringExtra("email");

        if (email == null || email.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy email", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvEmail = findViewById(R.id.tvEmail);
        btnSendReset = findViewById(R.id.btnSendReset);

        tvEmail.setText(email);

        btnSendReset.setOnClickListener(v -> {
            btnSendReset.setEnabled(false);

            mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    btnSendReset.setEnabled(true);

                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Đã gửi email đặt lại mật khẩu", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Không thể gửi email. Vui lòng kiểm tra lại.", Toast.LENGTH_SHORT).show();
                    }
                });
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }
}