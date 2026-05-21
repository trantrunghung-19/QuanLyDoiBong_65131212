package ThiCK2.quanlydoibong_65131212;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentTrangChu extends Fragment {
    private TextView tvTongSoDoi, tvTongSoCauThu, tvWelcome, tvNewestName, tvNewestHlv;
    private CircleImageView ivNewestLogo;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private MaterialButton btnViewTeams, btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trang_chu, container, false);
        
        tvTongSoDoi = view.findViewById(R.id.tv_tong_so_doi);
        tvTongSoCauThu = view.findViewById(R.id.tv_tong_so_cau_thu);
        tvWelcome = view.findViewById(R.id.tv_welcome);
        tvNewestName = view.findViewById(R.id.tv_newest_team_name);
        tvNewestHlv = view.findViewById(R.id.tv_newest_team_hlv);
        ivNewestLogo = view.findViewById(R.id.iv_newest_team_logo);
        btnViewTeams = view.findViewById(R.id.btn_view_teams);
        btnLogout = view.findViewById(R.id.btn_logout_main);
        
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            tvWelcome.setText("Chào mừng trở lại, " + (name != null && !name.isEmpty() ? name : "Coach") + "!");
        }

        btnViewTeams.setOnClickListener(v -> {
            if (getActivity() != null) {
                BottomNavigationView bottomNav = getActivity().findViewById(R.id.bottom_navigation);
                bottomNav.setSelectedItemId(R.id.nav_teams);
            }
        });

        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        loadDashboardData();
        return view;
    }

    private void loadDashboardData() {
        db.collection("DOIBONG").addSnapshotListener((value, error) -> {
            if (value != null) {
                int totalTeams = value.size();
                int totalPlayers = 0;
                DoiBong newestTeam = null;

                for (DocumentSnapshot doc : value.getDocuments()) {
                    DoiBong team = doc.toObject(DoiBong.class);
                    if (team != null) {
                        totalPlayers += team.getSoLuongCauThu();
                        newestTeam = team; // Lấy đội cuối cùng trong snapshot làm đội mới nhất
                    }
                }

                tvTongSoDoi.setText(String.valueOf(totalTeams));
                tvTongSoCauThu.setText(String.valueOf(totalPlayers));

                if (newestTeam != null) {
                    tvNewestName.setText(newestTeam.getTenDoi());
                    tvNewestHlv.setText("HLV: " + newestTeam.getHuanLuyenVien());
                    if (newestTeam.getLogoUrl() != null && !newestTeam.getLogoUrl().isEmpty()) {
                        Glide.with(this).load(newestTeam.getLogoUrl()).into(ivNewestLogo);
                    }
                }
            }
        });
    }
}
