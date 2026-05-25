package vn.edu.tinhoc123.quanlydoibong;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import vn.edu.tinhoc123.quanlydoibong.adapters.PlayerAdapter;
import vn.edu.tinhoc123.quanlydoibong.models.Player;
import vn.edu.tinhoc123.quanlydoibong.models.Team;
import vn.edu.tinhoc123.quanlydoibong.repositories.FirebaseRepository;

public class TeamDetailActivity extends AppCompatActivity {
    private TextView tvTeamName, tvCoach, tvStadium, tvPlayerCount, tvCountry;
    private RecyclerView rvPlayers;
    private FloatingActionButton fabAddPlayer;
    private Button btnEditTeam, btnDeleteTeam;
    private FirebaseRepository repository;
    private String teamId;
    private Team currentTeam;
    private PlayerAdapter adapter;
    private List<Player> playerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);

        teamId = getIntent().getStringExtra("teamId");
        repository = new FirebaseRepository();

        tvTeamName = findViewById(R.id.tvTeamName);
        tvCoach = findViewById(R.id.tvCoach);
        tvStadium = findViewById(R.id.tvStadium);
        tvPlayerCount = findViewById(R.id.tvPlayerCount);
        tvCountry = findViewById(R.id.tvCountry);
        rvPlayers = findViewById(R.id.rvPlayers);
        fabAddPlayer = findViewById(R.id.fabAddPlayer);
        btnEditTeam = findViewById(R.id.btnEditTeam);
        btnDeleteTeam = findViewById(R.id.btnDeleteTeam);

        setupRecyclerView();
        loadTeamDetails();
        loadPlayers();

        fabAddPlayer.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddPlayerActivity.class);
            intent.putExtra("teamId", teamId);
            startActivity(intent);
        });

        btnEditTeam.setOnClickListener(v -> {
            if (teamId != null) {
                Intent intent = new Intent(TeamDetailActivity.this, EditTeamActivity.class);
                intent.putExtra("teamId", teamId);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Không tìm thấy ID đội bóng", Toast.LENGTH_SHORT).show();
            }
        });

        btnDeleteTeam.setOnClickListener(v -> {
            // Kiểm tra quyền sở hữu trước khi xóa
            if (currentTeam != null && currentTeam.getUserId().equals(repository.getCurrentUserId())) {
                new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa đội bóng này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        repository.deleteTeam(teamId).addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Đã xóa đội bóng", Toast.LENGTH_SHORT).show();
                            finish();
                        });
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
            } else {
                Toast.makeText(this, "Bạn không có quyền xóa đội bóng này", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        playerList = new ArrayList<>();
        adapter = new PlayerAdapter(playerList, teamId);
        rvPlayers.setLayoutManager(new LinearLayoutManager(this));
        rvPlayers.setAdapter(adapter);
    }

    private void loadTeamDetails() {
        repository.getTeamsRef().document(teamId).addSnapshotListener((value, error) -> {
            if (error != null) return;
            if (value != null && value.exists()) {
                currentTeam = value.toObject(Team.class);
                if (currentTeam != null) {
                    // Kiểm tra quyền truy cập: Chỉ cho xem nếu đúng userId
                    String currentUid = repository.getCurrentUserId();
                    if (currentTeam.getUserId() != null && !currentTeam.getUserId().equals(currentUid)) {
                        Toast.makeText(this, "Bạn không có quyền xem đội bóng này", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }

                    tvTeamName.setText(currentTeam.getTenDoi());
                    tvCoach.setText("HLV: " + currentTeam.getHuanLuyenVien());
                    tvStadium.setText(currentTeam.getSanNha());
                    tvPlayerCount.setText(String.valueOf(currentTeam.getSoLuongCauThu()));
                    tvCountry.setText(currentTeam.getQuocGia());

                    if ("FC Barcelona".equals(currentTeam.getTenDoi())) {
                        checkAndSeedPlayers();
                    }
                }
            }
        });
    }

    private void loadPlayers() {
        repository.getPlayersByTeam(teamId).addSnapshotListener((value, error) -> {
            if (error != null) return;
            if (value != null) {
                playerList.clear();
                for (QueryDocumentSnapshot doc : value) {
                    playerList.add(doc.toObject(Player.class));
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void checkAndSeedPlayers() {
        repository.getPlayersRef(teamId).get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots.isEmpty()) {
                seedPlayers();
            }
        });
    }

    private void seedPlayers() {
        repository.addPlayer(teamId, new Player(null, 7, "KimRiCha", "Tiền đạo", 91));
        repository.addPlayer(teamId, new Player(null, 8, "Iniesta", "Tiền vệ", 88));
        repository.addPlayer(teamId, new Player(null, 1, "Ter Stegen", "Thủ môn", 87));
        repository.addPlayer(teamId, new Player(null, 21, "F. de Jong", "Tiền vệ", 86));
        repository.addPlayer(teamId, new Player(null, 9, "Lewandowski", "Tiền đạo", 90));
    }
}