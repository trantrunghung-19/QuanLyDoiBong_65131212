package vn.edu.tinhoc123.quanlydoibong;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import vn.edu.tinhoc123.quanlydoibong.models.Player;
import vn.edu.tinhoc123.quanlydoibong.repositories.FirebaseRepository;

public class AddPlayerActivity extends AppCompatActivity {
    private EditText etPlayerName, etPlayerNumber, etPosition, etRating;
    private Button btnSave, btnCancel;
    private FirebaseRepository repository;
    private String teamId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);

        teamId = getIntent().getStringExtra("teamId");
        repository = new FirebaseRepository();

        etPlayerName = findViewById(R.id.etPlayerName);
        etPlayerNumber = findViewById(R.id.etPlayerNumber);
        etPosition = findViewById(R.id.etPosition);
        etRating = findViewById(R.id.etRating);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(v -> {
            String name = etPlayerName.getText().toString().trim();
            String numberStr = etPlayerNumber.getText().toString().trim();
            String position = etPosition.getText().toString().trim();
            String ratingStr = etRating.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(numberStr) || TextUtils.isEmpty(position) || TextUtils.isEmpty(ratingStr)) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            int number = Integer.parseInt(numberStr);
            int rating = Integer.parseInt(ratingStr);

            Player player = new Player(null, number, name, position, rating);
            
            repository.addPlayer(teamId, player).addOnSuccessListener(docRef -> {
                // Update team player count
                repository.getTeamsRef().document(teamId).get().addOnSuccessListener(snapshot -> {
                    long currentCount = snapshot.getLong("soLuongCauThu");
                    repository.getTeamsRef().document(teamId).update("soLuongCauThu", currentCount + 1);
                });
                
                Toast.makeText(this, "Thêm cầu thủ thành công", Toast.LENGTH_SHORT).show();
                finish();
            });
        });

        btnCancel.setOnClickListener(v -> finish());
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }
}