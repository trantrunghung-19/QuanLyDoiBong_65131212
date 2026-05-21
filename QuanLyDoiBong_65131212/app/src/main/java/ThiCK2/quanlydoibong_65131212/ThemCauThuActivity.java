package ThiCK2.quanlydoibong_65131212;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

public class ThemCauThuActivity extends AppCompatActivity {
    private TextInputEditText etTen, etSoAo, etViTri, etTuoi, etQuocTich, etChiSo, etMa;
    private MaterialButton btnSave, btnChonAnh;
    private ImageView ivPreview;
    private ProgressBar pbUpload;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private String doiBongId;
    private Uri imageUri;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    ivPreview.setImageURI(imageUri);
                    ivPreview.setImageTintList(null); // Xóa tint màu xám của placeholder
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_cauthu);

        doiBongId = getIntent().getStringExtra("DOI_BONG_ID");

        etMa = findViewById(R.id.et_ma_cau_thu);
        etTen = findViewById(R.id.et_ten_cau_thu);
        etSoAo = findViewById(R.id.et_so_ao);
        etViTri = findViewById(R.id.et_vi_tri);
        etTuoi = findViewById(R.id.et_tuoi);
        etQuocTich = findViewById(R.id.et_quoc_tich);
        etChiSo = findViewById(R.id.et_chi_so);
        btnSave = findViewById(R.id.btn_save_cau_thu);
        btnChonAnh = findViewById(R.id.btn_chon_anh_cau_thu);
        ivPreview = findViewById(R.id.iv_them_anh_cau_thu);
        pbUpload = findViewById(R.id.pb_upload_anh);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        btnChonAnh.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        btnSave.setOnClickListener(v -> {
            String ma = etMa.getText().toString().trim();
            String ten = etTen.getText().toString().trim();
            String soAo = etSoAo.getText().toString().trim();
            String viTri = etViTri.getText().toString().trim();
            String tuoi = etTuoi.getText().toString().trim();
            String quocTich = etQuocTich.getText().toString().trim();
            String chiSo = etChiSo.getText().toString().trim();

            if (ma.isEmpty() || ten.isEmpty() || soAo.isEmpty() || viTri.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin bắt buộc", Toast.LENGTH_SHORT).show();
                return;
            }

            if (imageUri != null) {
                uploadImageAndSave(ma, ten, soAo, viTri, tuoi, quocTich, chiSo);
            } else {
                saveToFirestore(ma, ten, soAo, viTri, tuoi, quocTich, chiSo, "");
            }
        });
    }

    private void uploadImageAndSave(String ma, String ten, String soAo, String viTri, String tuoi, String quocTich, String chiSo) {
        pbUpload.setVisibility(View.VISIBLE);
        btnSave.setEnabled(false);

        StorageReference ref = storage.getReference().child("player_images/" + ma + "_" + System.currentTimeMillis() + ".png");
        ref.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                    saveToFirestore(ma, ten, soAo, viTri, tuoi, quocTich, chiSo, uri.toString());
                }))
                .addOnFailureListener(e -> {
                    pbUpload.setVisibility(View.GONE);
                    btnSave.setEnabled(true);
                    Toast.makeText(this, "Lỗi upload: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveToFirestore(String ma, String ten, String soAo, String viTri, String tuoi, String quocTich, String chiSo, String imageUrl) {
        CauThu ct = new CauThu(ma, ten, 
                Integer.parseInt(soAo), 
                viTri, 
                Integer.parseInt(tuoi.isEmpty() ? "0" : tuoi), 
                quocTich, 
                Integer.parseInt(chiSo.isEmpty() ? "0" : chiSo), 
                0, 0, imageUrl);

        db.collection("DOIBONG").document(doiBongId)
                .collection("CAUTHU").add(ct)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Đã ký hợp đồng với cầu thủ mới", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    pbUpload.setVisibility(View.GONE);
                    btnSave.setEnabled(true);
                    Toast.makeText(this, "Lỗi Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
