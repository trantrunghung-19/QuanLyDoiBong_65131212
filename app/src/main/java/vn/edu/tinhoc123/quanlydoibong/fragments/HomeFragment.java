package vn.edu.tinhoc123.quanlydoibong.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import vn.edu.tinhoc123.quanlydoibong.MainActivity;
import vn.edu.tinhoc123.quanlydoibong.R;
import vn.edu.tinhoc123.quanlydoibong.adapters.TeamAdapter;
import vn.edu.tinhoc123.quanlydoibong.models.Team;
import vn.edu.tinhoc123.quanlydoibong.repositories.FirebaseRepository;

public class HomeFragment extends Fragment {
    private TextView tvTotalTeams, tvTotalPlayers;
    private RecyclerView rvRecentTeams;
    private Button btnViewAllTeams;
    private FirebaseRepository repository;
    private TeamAdapter adapter;
    private List<Team> teamList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        repository = new FirebaseRepository();
        tvTotalTeams = view.findViewById(R.id.tvTotalTeams);
        tvTotalPlayers = view.findViewById(R.id.tvTotalPlayers);
        rvRecentTeams = view.findViewById(R.id.rvRecentTeams);
        btnViewAllTeams = view.findViewById(R.id.btnViewAllTeams);

        setupRecyclerView();
        loadStatistics();
        loadRecentTeams();

        btnViewAllTeams.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToTeamTab();
            }
        });

        return view;
    }

    private void setupRecyclerView() {
        teamList = new ArrayList<>();
        adapter = new TeamAdapter(teamList, true);
        rvRecentTeams.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRecentTeams.setAdapter(adapter);
    }

    private void loadStatistics() {
        // Lọc theo User hiện tại
        repository.getTeamsByCurrentUser().addSnapshotListener((value, error) -> {
            if (error != null || value == null) return;
            
            int totalTeams = value.size();
            tvTotalTeams.setText(String.valueOf(totalTeams));
            
            long totalPlayers = 0;
            for (QueryDocumentSnapshot doc : value) {
                Long count = doc.getLong("soLuongCauThu");
                if (count != null) totalPlayers += count;
            }
            tvTotalPlayers.setText(String.valueOf(totalPlayers));
            
            // Nếu rỗng, có thể seed dữ liệu mẫu cho user này
            if (totalTeams == 0) {
                seedInitialData();
            }
        });
    }

    private void loadRecentTeams() {
        // Lọc 3 đội bóng mới nhất của User hiện tại
        repository.getLatestTeamsByCurrentUser().addSnapshotListener((value, error) -> {
            if (error != null) return;
            if (value != null) {
                teamList.clear();
                for (QueryDocumentSnapshot doc : value) {
                    Team team = doc.toObject(Team.class);
                    if (team.getId() == null) team.setId(doc.getId());
                    teamList.add(team);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
    
    private void seedInitialData() {
        String uid = repository.getCurrentUserId();
        if (uid == null) return;
        
        Team sampleTeam = new Team(null, "Manchester United", "Trần Trung Hưng", "Van Binh", 2, "Anh", uid);
        repository.addTeam(sampleTeam);
    }
}