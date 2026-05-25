package vn.edu.tinhoc123.quanlydoibong;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import vn.edu.tinhoc123.quanlydoibong.models.Team;
import vn.edu.tinhoc123.quanlydoibong.repositories.FirebaseRepository;

public class EditTeamActivity extends AppCompatActivity {
    private EditText etTeamName, etCoachName, etStadium, etPlayerCount, etCountry;
    private Button btnSave, btnCancel;
    private FirebaseRepository repository;
    private String teamId;
    private Team currentTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_team);

        repository = new FirebaseRepository();
        teamId = getIntent().getStringExtra("teamId");

        if (TextUtils.isEmpty(teamId)) {
            Toast.makeText(this, "Không tìm thấy đội bóng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etTeamName = findViewById(R.id.etTeamName);
        etCoachName = findViewById(R.id.etCoachName);
        etStadium = findViewById(R.id.etStadium);
        etPlayerCount = findViewById(R.id.etPlayerCount);
        etCountry = findViewById(R.id.etCountry);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        loadTeamData();

        btnSave.setOnClickListener(v -> {
            updateTeam();
        });

        btnCancel.setOnClickListener(v -> finish());
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void loadTeamData() {
        repository.getTeamsRef().document(teamId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                currentTeam = documentSnapshot.toObject(Team.class);
                if (currentTeam != null) {
                    etTeamName.setText(currentTeam.getTenDoi());
                    etCoachName.setText(currentTeam.getHuanLuyenVien());
                    etStadium.setText(currentTeam.getSanNha());
                    etPlayerCount.setText(String.valueOf(currentTeam.getSoLuongCauThu()));
                    etCountry.setText(currentTeam.getQuocGia());
                }
            } else {
                Toast.makeText(this, "Không tìm thấy dữ liệu đội bóng", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Lỗi khi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void updateTeam() {
        String name = etTeamName.getText().toString().trim();
        String coach = etCoachName.getText().toString().trim();
        String stadium = etStadium.getText().toString().trim();
        String countStr = etPlayerCount.getText().toString().trim();
        String country = etCountry.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(coach) || TextUtils.isEmpty(stadium) || 
            TextUtils.isEmpty(countStr) || TextUtils.isEmpty(country)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        int playerCount;
        try {
            playerCount = Integer.parseInt(countStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Số lượng cầu thủ phải là số", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentTeam == null) currentTeam = new Team();
        currentTeam.setTenDoi(name);
        currentTeam.setHuanLuyenVien(coach);
        currentTeam.setSanNha(stadium);
        currentTeam.setSoLuongCauThu(playerCount);
        currentTeam.setQuocGia(country);

        repository.updateTeam(teamId, currentTeam).addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "Cập nhật đội bóng thành công", Toast.LENGTH_SHORT).show();
            finish();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Cập nhật thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}