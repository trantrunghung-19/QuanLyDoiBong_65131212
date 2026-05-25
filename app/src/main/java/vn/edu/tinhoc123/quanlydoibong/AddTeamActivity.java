package vn.edu.tinhoc123.quanlydoibong;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import vn.edu.tinhoc123.quanlydoibong.models.Team;
import vn.edu.tinhoc123.quanlydoibong.repositories.FirebaseRepository;

public class AddTeamActivity extends AppCompatActivity {
    private EditText etTeamName, etCoachName, etStadium, etPlayerCount, etCountry;
    private Button btnSave, btnCancel;
    private FirebaseRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);

        repository = new FirebaseRepository();

        etTeamName = findViewById(R.id.etTeamName);
        etCoachName = findViewById(R.id.etCoachName);
        etStadium = findViewById(R.id.etStadium);
        etPlayerCount = findViewById(R.id.etPlayerCount);
        etCountry = findViewById(R.id.etCountry);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(v -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser == null) {
                Toast.makeText(this, "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return;
            }

            String name = etTeamName.getText().toString().trim();
            String coach = etCoachName.getText().toString().trim();
            String stadium = etStadium.getText().toString().trim();
            String countStr = etPlayerCount.getText().toString().trim();
            String country = etCountry.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(coach) || TextUtils.isEmpty(stadium) || TextUtils.isEmpty(countStr) || TextUtils.isEmpty(country)) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            int count;
            try {
                count = Integer.parseInt(countStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Số lượng cầu thủ phải là số", Toast.LENGTH_SHORT).show();
                return;
            }

            Team team = new Team(null, name, coach, stadium, count, country, currentUser.getUid());
            
            repository.addTeam(team).addOnSuccessListener(documentReference -> {
                Toast.makeText(this, "Thêm đội bóng thành công", Toast.LENGTH_SHORT).show();
                finish();
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });

        btnCancel.setOnClickListener(v -> finish());
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }
}