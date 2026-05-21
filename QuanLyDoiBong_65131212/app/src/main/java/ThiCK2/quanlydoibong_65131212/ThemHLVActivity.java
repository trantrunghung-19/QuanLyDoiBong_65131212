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

public class ThemHLVActivity extends AppCompatActivity {
    private TextInputEditText etMa, etTen, etTuoi, etQuocTich, etDoiBong, etKinhNghiem, etThanhTich;
    private MaterialButton btnSave, btnChonAnh;
    private CircleImageView ivAvatar;
    private ProgressBar pbUpload;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private Uri imageUri;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    ivAvatar.setImageURI(imageUri);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_hlv);

        etMa = findViewById(R.id.et_ma_hlv);
        etTen = findViewById(R.id.et_ten_hlv);
        etTuoi = findViewById(R.id.et_tuoi_hlv);
        etQuocTich = findViewById(R.id.et_quoc_tich_hlv);
        etDoiBong = findViewById(R.id.et_doi_bong_hlv);
        etKinhNghiem = findViewById(R.id.et_kinh_nghiem_hlv);
        etThanhTich = findViewById(R.id.et_thanh_tich_hlv);
        btnSave = findViewById(R.id.btn_save_hlv);
        btnChonAnh = findViewById(R.id.btn_chon_anh_hlv);
        ivAvatar = findViewById(R.id.iv_them_hlv_avatar);
        pbUpload = findViewById(R.id.pb_upload_hlv);

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
            String tuoi = etTuoi.getText().toString().trim();
            String quocTich = etQuocTich.getText().toString().trim();
            String doiBong = etDoiBong.getText().toString().trim();
            String kinhNghiem = etKinhNghiem.getText().toString().trim();
            String thanhTich = etThanhTich.getText().toString().trim();

            if (ma.isEmpty() || ten.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập Mã và Tên HLV", Toast.LENGTH_SHORT).show();
                return;
            }

            if (imageUri != null) {
                uploadImageAndSave(ma, ten, tuoi, quocTich, doiBong, kinhNghiem, thanhTich);
            } else {
                saveToFirestore(ma, ten, tuoi, quocTich, doiBong, kinhNghiem, thanhTich, "");
            }
        });
    }

    private void uploadImageAndSave(String ma, String ten, String tuoi, String quocTich, String doiBong, String kinhNghiem, String thanhTich) {
        pbUpload.setVisibility(View.VISIBLE);
        btnSave.setEnabled(false);

        StorageReference ref = storage.getReference().child("coach_images/" + ma + "_" + System.currentTimeMillis() + ".png");
        ref.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                    saveToFirestore(ma, ten, tuoi, quocTich, doiBong, kinhNghiem, thanhTich, uri.toString());
                }))
                .addOnFailureListener(e -> {
                    pbUpload.setVisibility(View.GONE);
                    btnSave.setEnabled(true);
                    Toast.makeText(this, "Lỗi upload: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveToFirestore(String ma, String ten, String tuoi, String quocTich, String doiBong, String kinhNghiem, String thanhTich, String avatarUrl) {
        HLV hlv = new HLV(ma, ten,
                Integer.parseInt(tuoi.isEmpty() ? "0" : tuoi),
                quocTich,
                doiBong,
                Integer.parseInt(kinhNghiem.isEmpty() ? "0" : kinhNghiem),
                thanhTich,
                avatarUrl);

        db.collection("HLV").add(hlv)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Đã thêm HLV thành công", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    pbUpload.setVisibility(View.GONE);
                    btnSave.setEnabled(true);
                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
