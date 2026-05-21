package ThiCK2.quanlydoibong_65131212;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FragmentQuanLyDoiBong extends Fragment {
    private ListView lvDoiBong;
    private DoiBongAdapter adapter;
    private List<DoiBong> doiBongList;
    private List<DoiBong> fullList;
    private FirebaseFirestore db;
    private TextInputEditText etSearch;
    private TextView tvEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quan_ly_doi_bong, container, false);

        lvDoiBong = view.findViewById(R.id.lv_doi_bong);
        etSearch = view.findViewById(R.id.et_search);
        tvEmpty = view.findViewById(R.id.tv_empty_teams);
        FloatingActionButton fabAdd = view.findViewById(R.id.fab_add);

        db = FirebaseFirestore.getInstance();
        doiBongList = new ArrayList<>();
        fullList = new ArrayList<>();
        adapter = new DoiBongAdapter(getContext(), doiBongList);
        lvDoiBong.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ThemDoiActivity.class));
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterTeams(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        loadData();
        return view;
    }

    private void loadData() {
        db.collection("DOIBONG").addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(getContext(), "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
                return;
            }

            if (value != null) {
                doiBongList.clear();
                fullList.clear();
                for (DocumentSnapshot doc : value.getDocuments()) {
                    DoiBong dbModel = doc.toObject(DoiBong.class);
                    if (dbModel != null) {
                        dbModel.setId(doc.getId());
                        doiBongList.add(dbModel);
                        fullList.add(dbModel);
                    }
                }
                updateEmptyState();
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void filterTeams(String text) {
        doiBongList.clear();
        if (text.isEmpty()) {
            doiBongList.addAll(fullList);
        } else {
            for (DoiBong team : fullList) {
                if (team.getTenDoi().toLowerCase().contains(text.toLowerCase())) {
                    doiBongList.add(team);
                }
            }
        }
        updateEmptyState();
        adapter.notifyDataSetChanged();
    }

    private void updateEmptyState() {
        if (doiBongList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
        }
    }
}
