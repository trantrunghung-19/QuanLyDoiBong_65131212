package ThiCK2.quanlydoibong_65131212;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CauThuActivity extends AppCompatActivity {
    private ListView lvCauThu;
    private CauThuAdapter adapter;
    private List<CauThu> cauThuList;
    private List<CauThu> fullList;
    private FirebaseFirestore db;
    private DoiBong doiBong;
    private TextInputEditText etSearch;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cauthu);

        doiBong = (DoiBong) getIntent().getSerializableExtra("DOI_BONG");
        if (doiBong == null) {
            finish();
            return;
        }

        db = FirebaseFirestore.getInstance();
        initViews();
        bindHeaderData();
        setupEvents();
        loadData();
    }

    private void initViews() {
        lvCauThu = findViewById(R.id.lv_cau_thu);
        etSearch = findViewById(R.id.et_search_cau_thu);
        tvEmpty = findViewById(R.id.tv_empty_players);
        
        cauThuList = new ArrayList<>();
        fullList = new ArrayList<>();
        adapter = new CauThuAdapter(this, cauThuList, doiBong.getId());
        lvCauThu.setAdapter(adapter);
    }

    private void bindHeaderData() {
        TextView tvName = findViewById(R.id.tv_detail_team_name);
        TextView tvCoach = findViewById(R.id.tv_detail_coach);
        TextView tvStadium = findViewById(R.id.tv_detail_stadium);
        TextView tvCount = findViewById(R.id.tv_detail_member_count);
        ImageView ivLogo = findViewById(R.id.iv_detail_team_logo);

        tvName.setText(doiBong.getTenDoi());
        tvCoach.setText(doiBong.getHuanLuyenVien());
        tvStadium.setText(doiBong.getSanNha());
        
        if (doiBong.getLogoUrl() != null && !doiBong.getLogoUrl().isEmpty()) {
            ivLogo.setImageTintList(null);
            Glide.with(this).load(doiBong.getLogoUrl()).placeholder(android.R.drawable.ic_menu_myplaces).into(ivLogo);
        }
    }

    private void setupEvents() {
        findViewById(R.id.btn_back_detail).setOnClickListener(v -> finish());
        
        findViewById(R.id.fab_add_cau_thu).setOnClickListener(v -> {
            Intent intent = new Intent(this, ThemCauThuActivity.class);
            intent.putExtra("DOI_BONG_ID", doiBong.getId());
            startActivity(intent);
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterPlayers(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        TabLayout tabLayout = findViewById(R.id.tab_layout_team);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() != 0) {
                    Toast.makeText(CauThuActivity.this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void loadData() {
        db.collection("DOIBONG").document(doiBong.getId())
                .collection("CAUTHU").addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
                return;
            }

            if (value != null) {
                cauThuList.clear();
                fullList.clear();
                for (DocumentSnapshot doc : value.getDocuments()) {
                    CauThu ct = doc.toObject(CauThu.class);
                    if (ct != null) {
                        ct.setId(doc.getId());
                        cauThuList.add(ct);
                        fullList.add(ct);
                    }
                }
                
                TextView tvCount = findViewById(R.id.tv_detail_member_count);
                tvCount.setText(cauThuList.size() + " Cầu thủ");

                updateEmptyState();
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void filterPlayers(String text) {
        cauThuList.clear();
        if (text.isEmpty()) {
            cauThuList.addAll(fullList);
        } else {
            for (CauThu player : fullList) {
                if (player.getTenCauThu().toLowerCase().contains(text.toLowerCase())) {
                    cauThuList.add(player);
                }
            }
        }
        updateEmptyState();
        adapter.notifyDataSetChanged();
    }

    private void updateEmptyState() {
        if (cauThuList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
        }
    }
}
