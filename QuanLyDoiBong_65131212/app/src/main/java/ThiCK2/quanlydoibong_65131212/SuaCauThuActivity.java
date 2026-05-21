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

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SuaCauThuActivity extends AppCompatActivity {
    private TextInputEditText etMa, etTen, etSoAo, etViTri, etTuoi, etQuocTich, etChiSo, etBanThang, etKienTao;
    private MaterialButton btnUpdate, btnDoiAnh;
    private ImageView ivPlayer;
    private ProgressBar pbUpload;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private String doiBongId;
    private CauThu cauThu;
    private Uri newImageUri;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    newImageUri = result.getData().getData();
                    ivPlayer.setImageURI(newImageUri);
                    ivPlayer.setImageTintList(null); // Xóa tint mặc định
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_cauthu);

        doiBongId = getIntent().getStringExtra("DOI_BONG_ID");
        cauThu = (CauThu) getIntent().getSerializableExtra("CAU_THU");

        etMa = findViewById(R.id.et_sua_ma_cau_thu);
        etTen = findViewById(R.id.et_sua_ten_cau_thu);
        etSoAo = findViewById(R.id.et_sua_so_ao);
        etViTri = findViewById(R.id.et_sua_vi_tri);
        etTuoi = findViewById(R.id.et_sua_tuoi);
        etQuocTich = findViewById(R.id.et_sua_quoc_tich);
        etChiSo = findViewById(R.id.et_sua_chi_so);
        etBanThang = findViewById(R.id.et_sua_ban_thang);
        etKienTao = findViewById(R.id.et_sua_kien_tao);
        btnUpdate = findViewById(R.id.btn_update_cau_thu);
        btnDoiAnh = findViewById(R.id.btn_sua_chon_anh);
        ivPlayer = findViewById(R.id.iv_sua_anh_cau_thu);
        pbUpload = findViewById(R.id.pb_sua_upload_anh);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        if (cauThu != null) {
            etMa.setText(cauThu.getMaCauThu());
            etTen.setText(cauThu.getTenCauThu());
            etSoAo.setText(String.valueOf(cauThu.getSoAo()));
            etViTri.setText(cauThu.getViTri());
            etTuoi.setText(String.valueOf(cauThu.getTuoi()));
            etQuocTich.setText(cauThu.getQuocTich());
            etChiSo.setText(String.valueOf(cauThu.getChiSo()));
            etBanThang.setText(String.valueOf(cauThu.getBanThang()));
            etKienTao.setText(String.valueOf(cauThu.getKienTao()));

            if (cauThu.getImageUrl() != null && !cauThu.getImageUrl().isEmpty()) {
                ivPlayer.setImageTintList(null);
                Glide.with(this).load(cauThu.getImageUrl()).placeholder(android.R.drawable.ic_menu_report_image).into(ivPlayer);
            }
        }

        btnDoiAnh.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        btnUpdate.setOnClickListener(v -> {
            String ten = etTen.getText().toString().trim();
            String soAo = etSoAo.getText().toString().trim();
            String viTri = etViTri.getText().toString().trim();
            String tuoi = etTuoi.getText().toString().trim();
            String quocTich = etQuocTich.getText().toString().trim();
            String chiSo = etChiSo.getText().toString().trim();
            String banThang = etBanThang.getText().toString().trim();
            String kienTao = etKienTao.getText().toString().trim();

            if (ten.isEmpty() || soAo.isEmpty() || viTri.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập các thông tin cơ bản", Toast.LENGTH_SHORT).show();
                return;
            }

            cauThu.setTenCauThu(ten);
            cauThu.setSoAo(Integer.parseInt(soAo));
            cauThu.setViTri(viTri);
            cauThu.setTuoi(Integer.parseInt(tuoi.isEmpty() ? "0" : tuoi));
            cauThu.setQuocTich(quocTich);
            cauThu.setChiSo(Integer.parseInt(chiSo.isEmpty() ? "0" : chiSo));
            cauThu.setBanThang(Integer.parseInt(banThang.isEmpty() ? "0" : banThang));
            cauThu.setKienTao(Integer.parseInt(kienTao.isEmpty() ? "0" : kienTao));

            if (newImageUri != null) {
                updateImageAndSave();
            } else {
                saveToFirestore();
            }
        });
    }

    private void updateImageAndSave() {
        pbUpload.setVisibility(View.VISIBLE);
        btnUpdate.setEnabled(false);

        // Delete old image if exists
        if (cauThu.getImageUrl() != null && !cauThu.getImageUrl().isEmpty()) {
            try {
                FirebaseStorage.getInstance().getReferenceFromUrl(cauThu.getImageUrl()).delete();
            } catch (Exception ignored) {}
        }

        StorageReference storageRef = storage.getReference().child("player_images/ct_" + cauThu.getMaCauThu() + "_" + System.currentTimeMillis() + ".png");
        storageRef.putFile(newImageUri)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    cauThu.setImageUrl(uri.toString());
                    saveToFirestore();
                }))
                .addOnFailureListener(e -> {
                    pbUpload.setVisibility(View.GONE);
                    btnUpdate.setEnabled(true);
                    Toast.makeText(this, "Lỗi upload ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveToFirestore() {
        db.collection("DOIBONG").document(doiBongId)
                .collection("CAUTHU").document(cauThu.getId())
                .set(cauThu)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    pbUpload.setVisibility(View.GONE);
                    btnUpdate.setEnabled(true);
                    Toast.makeText(this, "Lỗi database: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
