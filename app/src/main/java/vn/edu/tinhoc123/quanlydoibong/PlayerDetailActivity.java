package vn.edu.tinhoc123.quanlydoibong;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import vn.edu.tinhoc123.quanlydoibong.models.Player;
import vn.edu.tinhoc123.quanlydoibong.repositories.FirebaseRepository;

public class PlayerDetailActivity extends AppCompatActivity {
    private TextView tvPlayerNumber, tvPlayerName, tvPosition, tvRating;
    private Button btnEdit, btnDelete;
    private FirebaseRepository repository;
    private String teamId, playerId;
    private Player currentPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_detail);

        repository = new FirebaseRepository();
        teamId = getIntent().getStringExtra("teamId");
        playerId = getIntent().getStringExtra("playerId");

        if (teamId == null || playerId == null) {
            Toast.makeText(this, "Không tìm thấy thông tin cầu thủ", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvPlayerNumber = findViewById(R.id.tvPlayerNumber);
        tvPlayerName = findViewById(R.id.tvPlayerName);
        tvPosition = findViewById(R.id.tvPosition);
        tvRating = findViewById(R.id.tvRating);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);

        loadPlayerData();

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditPlayerActivity.class);
            intent.putExtra("teamId", teamId);
            intent.putExtra("playerId", playerId);
            startActivity(intent);
        });

        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa cầu thủ này không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    repository.deletePlayer(teamId, playerId).addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Xóa cầu thủ thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(this, "Lỗi khi xóa: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                })
                .setNegativeButton("Hủy", null)
                .show();
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void loadPlayerData() {
        repository.getPlayersRef(teamId).document(playerId).addSnapshotListener((value, error) -> {
            if (error != null) return;
            if (value != null && value.exists()) {
                currentPlayer = value.toObject(Player.class);
                if (currentPlayer != null) {
                    tvPlayerNumber.setText(String.valueOf(currentPlayer.getSoAo()));
                    tvPlayerName.setText(currentPlayer.getTenCauThu());
                    tvPosition.setText(currentPlayer.getViTri());
                    tvRating.setText(String.valueOf(currentPlayer.getChiSo()));
                }
            }
        });
    }
}