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

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class SuaDoiActivity extends AppCompatActivity {
    private TextInputEditText etMaDoi, etTenDoi, etHlv, etSanNha, etSoLuong;
    private MaterialButton btnUpdate, btnDoiLogo;
    private CircleImageView ivLogo;
    private ProgressBar pbUpload;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private DoiBong doiBong;
    private Uri newImageUri;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    newImageUri = result.getData().getData();
                    ivLogo.setImageURI(newImageUri);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_doi);

        etMaDoi = findViewById(R.id.et_sua_ma_doi);
        etTenDoi = findViewById(R.id.et_sua_ten_doi);
        etHlv = findViewById(R.id.et_sua_hlv);
        etSanNha = findViewById(R.id.et_sua_san_nha);
        etSoLuong = findViewById(R.id.et_sua_so_luong);
        btnUpdate = findViewById(R.id.btn_update);
        btnDoiLogo = findViewById(R.id.btn_doi_logo);
        ivLogo = findViewById(R.id.iv_sua_logo);
        pbUpload = findViewById(R.id.pb_upload_logo);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        doiBong = (DoiBong) getIntent().getSerializableExtra("DOI_BONG");
        if (doiBong != null) {
            etMaDoi.setText(doiBong.getMaDoi());
            etTenDoi.setText(doiBong.getTenDoi());
            etHlv.setText(doiBong.getHuanLuyenVien());
            etSanNha.setText(doiBong.getSanNha());
            etSoLuong.setText(String.valueOf(doiBong.getSoLuongCauThu()));
            
            if (doiBong.getLogoUrl() != null && !doiBong.getLogoUrl().isEmpty()) {
                Glide.with(this).load(doiBong.getLogoUrl()).placeholder(android.R.drawable.ic_menu_myplaces).into(ivLogo);
            }
        }

        btnDoiLogo.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        btnUpdate.setOnClickListener(v -> {
            String tenDoi = etTenDoi.getText().toString().trim();
            String hlv = etHlv.getText().toString().trim();
            String sanNha = etSanNha.getText().toString().trim();
            String soLuongStr = etSoLuong.getText().toString().trim();

            if (tenDoi.isEmpty() || hlv.isEmpty() || sanNha.isEmpty() || soLuongStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            doiBong.setTenDoi(tenDoi);
            doiBong.setHuanLuyenVien(hlv);
            doiBong.setSanNha(sanNha);
            doiBong.setSoLuongCauThu(Integer.parseInt(soLuongStr));

            if (newImageUri != null) {
                updateLogoAndSave();
            } else {
                saveToFirestore();
            }
        });
    }

    private void updateLogoAndSave() {
        pbUpload.setVisibility(View.VISIBLE);
        btnUpdate.setEnabled(false);

        // Delete old logo if exists
        if (doiBong.getLogoUrl() != null && !doiBong.getLogoUrl().isEmpty()) {
            try {
                FirebaseStorage.getInstance().getReferenceFromUrl(doiBong.getLogoUrl()).delete();
            } catch (Exception e) {
                // Ignore if deletion fails (e.g. file doesn't exist)
            }
        }

        StorageReference storageRef = storage.getReference().child("team_logos/logo_" + doiBong.getMaDoi() + "_" + System.currentTimeMillis() + ".png");
        storageRef.putFile(newImageUri)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    doiBong.setLogoUrl(uri.toString());
                    saveToFirestore();
                }))
                .addOnFailureListener(e -> {
                    pbUpload.setVisibility(View.GONE);
                    btnUpdate.setEnabled(true);
                    Toast.makeText(this, "Lỗi upload ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveToFirestore() {
        db.collection("DOIBONG").document(doiBong.getId())
                .set(doiBong)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    pbUpload.setVisibility(View.GONE);
                    btnUpdate.setEnabled(true);
                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
