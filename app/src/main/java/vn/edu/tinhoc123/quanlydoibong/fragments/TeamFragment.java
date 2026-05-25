package vn.edu.tinhoc123.quanlydoibong.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import vn.edu.tinhoc123.quanlydoibong.AddTeamActivity;
import vn.edu.tinhoc123.quanlydoibong.R;
import vn.edu.tinhoc123.quanlydoibong.adapters.TeamAdapter;
import vn.edu.tinhoc123.quanlydoibong.models.Team;
import vn.edu.tinhoc123.quanlydoibong.repositories.FirebaseRepository;

public class TeamFragment extends Fragment {
    private EditText etSearch;
    private RecyclerView rvTeams;
    private FloatingActionButton fabAddTeam;
    private FirebaseRepository repository;
    private TeamAdapter adapter;
    private List<Team> teamList;
    private List<Team> filteredList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team, container, false);

        repository = new FirebaseRepository();
        etSearch = view.findViewById(R.id.etSearch);
        rvTeams = view.findViewById(R.id.rvTeams);
        fabAddTeam = view.findViewById(R.id.fabAddTeam);

        setupRecyclerView();
        loadTeams();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        fabAddTeam.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AddTeamActivity.class));
        });

        return view;
    }

    private void setupRecyclerView() {
        teamList = new ArrayList<>();
        filteredList = new ArrayList<>();
        adapter = new TeamAdapter(filteredList, false);
        rvTeams.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTeams.setAdapter(adapter);
    }

    private void loadTeams() {
        // Lọc danh sách đội bóng theo User hiện tại
        repository.getTeamsByCurrentUser().addSnapshotListener((value, error) -> {
            if (error != null) return;
            if (value != null) {
                teamList.clear();
                for (QueryDocumentSnapshot doc : value) {
                    Team team = doc.toObject(Team.class);
                    if (team.getId() == null) team.setId(doc.getId());
                    teamList.add(team);
                }
                filter(etSearch.getText().toString());
            }
        });
    }

    private void filter(String text) {
        filteredList.clear();
        for (Team team : teamList) {
            if (team.getTenDoi().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(team);
            }
        }
        adapter.notifyDataSetChanged();
    }
}