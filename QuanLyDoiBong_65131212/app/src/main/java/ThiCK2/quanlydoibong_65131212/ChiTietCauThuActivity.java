package ThiCK2.quanlydoibong_65131212;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;

public class ChiTietCauThuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet_cauthu);

        MaterialToolbar toolbar = findViewById(R.id.toolbar_chi_tiet);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        CauThu ct = (CauThu) getIntent().getSerializableExtra("CAU_THU");

        if (ct != null) {
            // Header stats
            ((TextView) findViewById(R.id.tv_det_ten)).setText(ct.getTenCauThu().toUpperCase());
            ((TextView) findViewById(R.id.tv_det_chi_so)).setText(String.valueOf(ct.getChiSo()));
            ((TextView) findViewById(R.id.tv_det_vi_tri)).setText(ct.getViTri());
            ((TextView) findViewById(R.id.tv_det_so_ao)).setText(getString(R.string.player_number_format, ct.getSoAo()));
            ((TextView) findViewById(R.id.tv_det_quoc_tich)).setText(ct.getQuocTich().toUpperCase());

            // Details section
            ((TextView) findViewById(R.id.tv_det_tuoi)).setText(getString(R.string.player_age_format, ct.getTuoi()));
            ((TextView) findViewById(R.id.tv_det_ma)).setText(getString(R.string.player_id_format, ct.getMaCauThu()));

            // Progress bars
            ProgressBar pbOvr = findViewById(R.id.pb_det_ovr);
            ProgressBar pbGoals = findViewById(R.id.pb_det_goals);
            ProgressBar pbAssists = findViewById(R.id.pb_det_assists);

            if (pbOvr != null) pbOvr.setProgress(ct.getChiSo());
            if (pbGoals != null) pbGoals.setProgress(Math.min(ct.getBanThang(), pbGoals.getMax()));
            if (pbAssists != null) pbAssists.setProgress(Math.min(ct.getKienTao(), pbAssists.getMax()));

            ImageView ivPlayer = findViewById(R.id.iv_det_player);
            if (ivPlayer != null && ct.getImageUrl() != null && !ct.getImageUrl().isEmpty()) {
                ivPlayer.setImageTintList(null);
                Glide.with(this)
                        .load(ct.getImageUrl())
                        .placeholder(android.R.drawable.ic_menu_report_image)
                        .into(ivPlayer);
            }
        }
    }
}
