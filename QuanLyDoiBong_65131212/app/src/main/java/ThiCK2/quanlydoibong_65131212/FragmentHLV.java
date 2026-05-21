package ThiCK2.quanlydoibong_65131212;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FragmentHLV extends Fragment {
    private ListView lvHLV;
    private HLVAdapter adapter;
    private List<HLV> hlvList;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hlv, container, false);

        lvHLV = view.findViewById(R.id.lv_hlv);
        FloatingActionButton fabAdd = view.findViewById(R.id.fab_add_hlv);

        db = FirebaseFirestore.getInstance();
        hlvList = new ArrayList<>();
        adapter = new HLVAdapter(getContext(), hlvList);
        lvHLV.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ThemHLVActivity.class));
        });

        loadData();
        return view;
    }

    private void loadData() {
        db.collection("HLV").addSnapshotListener((value, error) -> {
            if (error != null) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Lỗi tải dữ liệu HLV", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            if (value != null) {
                hlvList.clear();
                for (DocumentSnapshot doc : value.getDocuments()) {
                    HLV hlv = doc.toObject(HLV.class);
                    if (hlv != null) {
                        hlv.setId(doc.getId());
                        hlvList.add(hlv);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}
