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

public class SuaHLVActivity extends AppCompatActivity {
    private TextInputEditText etMa, etTen, etTuoi, etQuocTich, etDoiBong, etKinhNghiem, etThanhTich;
    private MaterialButton btnUpdate, btnDoiAnh;
    private CircleImageView ivAvatar;
    private ProgressBar pbUpload;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private HLV hlv;
    private Uri newImageUri;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    newImageUri = result.getData().getData();
                    ivAvatar.setImageURI(newImageUri);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_hlv);

        etMa = findViewById(R.id.et_sua_ma_hlv);
        etTen = findViewById(R.id.et_sua_ten_hlv);
        etTuoi = findViewById(R.id.et_sua_tuoi_hlv);
        etQuocTich = findViewById(R.id.et_sua_quoc_tich_hlv);
        etDoiBong = findViewById(R.id.et_sua_doi_bong_hlv);
        etKinhNghiem = findViewById(R.id.et_sua_kinh_nghiem_hlv);
        etThanhTich = findViewById(R.id.et_sua_thanh_tich_hlv);
        btnUpdate = findViewById(R.id.btn_update_hlv);
        btnDoiAnh = findViewById(R.id.btn_sua_chon_anh_hlv);
        ivAvatar = findViewById(R.id.iv_sua_hlv_avatar);
        pbUpload = findViewById(R.id.pb_sua_upload_hlv);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        hlv = (HLV) getIntent().getSerializableExtra("HLV");
        if (hlv != null) {
            etMa.setText(hlv.getMaHLV());
            etTen.setText(hlv.getTenHLV());
            etTuoi.setText(String.valueOf(hlv.getTuoi()));
            etQuocTich.setText(hlv.getQuocTich());
            etDoiBong.setText(hlv.getDoiBong());
            etKinhNghiem.setText(String.valueOf(hlv.getKinhNghiem()));
            etThanhTich.setText(hlv.getThanhTich());

            if (hlv.getAvatarUrl() != null && !hlv.getAvatarUrl().isEmpty()) {
                Glide.with(this).load(hlv.getAvatarUrl()).placeholder(android.R.drawable.ic_menu_myplaces).into(ivAvatar);
            }
        }

        btnDoiAnh.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        btnUpdate.setOnClickListener(v -> {
            String ten = etTen.getText().toString().trim();
            String tuoi = etTuoi.getText().toString().trim();
            String quocTich = etQuocTich.getText().toString().trim();
            String doiBong = etDoiBong.getText().toString().trim();
            String kinhNghiem = etKinhNghiem.getText().toString().trim();
            String thanhTich = etThanhTich.getText().toString().trim();

            if (ten.isEmpty()) {
                Toast.makeText(this, "Tên HLV không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            hlv.setTenHLV(ten);
            hlv.setTuoi(Integer.parseInt(tuoi.isEmpty() ? "0" : tuoi));
            hlv.setQuocTich(quocTich);
            hlv.setDoiBong(doiBong);
            hlv.setKinhNghiem(Integer.parseInt(kinhNghiem.isEmpty() ? "0" : kinhNghiem));
            hlv.setThanhTich(thanhTich);

            if (newImageUri != null) {
                updateAvatarAndSave();
            } else {
                saveToFirestore();
            }
        });
    }

    private void updateAvatarAndSave() {
        pbUpload.setVisibility(View.VISIBLE);
        btnUpdate.setEnabled(false);

        // Delete old avatar if exists
        if (hlv.getAvatarUrl() != null && !hlv.getAvatarUrl().isEmpty()) {
            try {
                FirebaseStorage.getInstance().getReferenceFromUrl(hlv.getAvatarUrl()).delete();
            } catch (Exception ignored) {}
        }

        StorageReference storageRef = storage.getReference().child("coach_images/" + hlv.getMaHLV() + "_" + System.currentTimeMillis() + ".png");
        storageRef.putFile(newImageUri)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    hlv.setAvatarUrl(uri.toString());
                    saveToFirestore();
                }))
                .addOnFailureListener(e -> {
                    pbUpload.setVisibility(View.GONE);
                    btnUpdate.setEnabled(true);
                    Toast.makeText(this, "Lỗi upload ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveToFirestore() {
        db.collection("HLV").document(hlv.getId())
                .set(hlv)
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
