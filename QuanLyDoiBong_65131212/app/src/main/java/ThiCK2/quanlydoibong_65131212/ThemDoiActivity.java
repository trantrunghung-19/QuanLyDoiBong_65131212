package ThiCK2.quanlydoibong_65131212;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class ThemDoiActivity extends AppCompatActivity {
    private TextInputEditText etMaDoi, etTenDoi, etHlv, etSanNha, etSoLuong;
    private MaterialButton btnSave, btnChonLogo;
    private CircleImageView ivLogo;
    private ProgressBar pbUpload;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private Uri imageUri;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    ivLogo.setImageURI(imageUri);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_doi);

        etMaDoi = findViewById(R.id.et_ma_doi);
        etTenDoi = findViewById(R.id.et_ten_doi);
        etHlv = findViewById(R.id.et_hlv);
        etSanNha = findViewById(R.id.et_san_nha);
        etSoLuong = findViewById(R.id.et_so_luong);
        btnSave = findViewById(R.id.btn_save);
        btnChonLogo = findViewById(R.id.btn_chon_logo);
        ivLogo = findViewById(R.id.iv_them_logo);
        pbUpload = findViewById(R.id.pb_upload_logo);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        btnChonLogo.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        btnSave.setOnClickListener(v -> {
            String maDoi = etMaDoi.getText().toString().trim();
            String tenDoi = etTenDoi.getText().toString().trim();
            String hlv = etHlv.getText().toString().trim();
            String sanNha = etSanNha.getText().toString().trim();
            String soLuongStr = etSoLuong.getText().toString().trim();

            if (maDoi.isEmpty() || tenDoi.isEmpty() || hlv.isEmpty() || sanNha.isEmpty() || soLuongStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            int soLuong = Integer.parseInt(soLuongStr);
            
            if (imageUri != null) {
                uploadLogo(maDoi, tenDoi, hlv, sanNha, soLuong);
            } else {
                saveDoiBongToFirestore(maDoi, tenDoi, hlv, sanNha, soLuong, "");
            }
        });
    }

    private void uploadLogo(String maDoi, String tenDoi, String hlv, String sanNha, int soLuong) {
        pbUpload.setVisibility(View.VISIBLE);
        btnSave.setEnabled(false);

        StorageReference storageRef = storage.getReference().child("team_logos/logo_" + maDoi + "_" + System.currentTimeMillis() + ".png");
        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String logoUrl = uri.toString();
                    saveDoiBongToFirestore(maDoi, tenDoi, hlv, sanNha, soLuong, logoUrl);
                }))
                .addOnFailureListener(e -> {
                    pbUpload.setVisibility(View.GONE);
                    btnSave.setEnabled(true);
                    Toast.makeText(this, "Upload logo thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveDoiBongToFirestore(String maDoi, String tenDoi, String hlv, String sanNha, int soLuong, String logoUrl) {
        DoiBong doiBong = new DoiBong(maDoi, tenDoi, hlv, sanNha, soLuong, logoUrl);

        db.collection("DOIBONG").add(doiBong)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Thêm đội bóng thành công", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    pbUpload.setVisibility(View.GONE);
                    btnSave.setEnabled(true);
                    Toast.makeText(this, "Lỗi khi thêm: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
