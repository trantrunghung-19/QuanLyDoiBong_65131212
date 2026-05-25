package vn.edu.tinhoc123.quanlydoibong;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import vn.edu.tinhoc123.quanlydoibong.models.Player;
import vn.edu.tinhoc123.quanlydoibong.repositories.FirebaseRepository;

public class EditPlayerActivity extends AppCompatActivity {
    private EditText etPlayerName, etPlayerNumber, etPosition, etRating;
    private Button btnSave, btnCancel;
    private FirebaseRepository repository;
    private String teamId, playerId;
    private Player currentPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_player);

        repository = new FirebaseRepository();
        teamId = getIntent().getStringExtra("teamId");
        playerId = getIntent().getStringExtra("playerId");

        if (TextUtils.isEmpty(teamId) || TextUtils.isEmpty(playerId)) {
            Toast.makeText(this, "Không tìm thấy thông tin cầu thủ", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etPlayerName = findViewById(R.id.etPlayerName);
        etPlayerNumber = findViewById(R.id.etPlayerNumber);
        etPosition = findViewById(R.id.etPosition);
        etRating = findViewById(R.id.etRating);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        loadPlayerData();

        btnSave.setOnClickListener(v -> {
            saveChanges();
        });

        btnCancel.setOnClickListener(v -> finish());
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void loadPlayerData() {
        repository.getPlayerById(teamId, playerId).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                currentPlayer = documentSnapshot.toObject(Player.class);
                if (currentPlayer != null) {
                    etPlayerName.setText(currentPlayer.getTenCauThu());
                    etPlayerNumber.setText(String.valueOf(currentPlayer.getSoAo()));
                    etPosition.setText(currentPlayer.getViTri());
                    etRating.setText(String.valueOf(currentPlayer.getChiSo()));
                }
            }
        });
    }

    private void saveChanges() {
        String name = etPlayerName.getText().toString().trim();
        String numberStr = etPlayerNumber.getText().toString().trim();
        String position = etPosition.getText().toString().trim();
        String ratingStr = etRating.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(numberStr) || TextUtils.isEmpty(position) || TextUtils.isEmpty(ratingStr)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        int number, rating;
        try {
            number = Integer.parseInt(numberStr);
            rating = Integer.parseInt(ratingStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Số áo và chỉ số phải là số", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentPlayer == null) currentPlayer = new Player();
        currentPlayer.setTenCauThu(name);
        currentPlayer.setSoAo(number);
        currentPlayer.setViTri(position);
        currentPlayer.setChiSo(rating);

        repository.updatePlayer(teamId, playerId, currentPlayer).addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "Cập nhật cầu thủ thành công", Toast.LENGTH_SHORT).show();
            finish();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}