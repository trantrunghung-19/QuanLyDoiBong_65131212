package vn.edu.tinhoc123.quanlydoibong.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import vn.edu.tinhoc123.quanlydoibong.LoginActivity;
import vn.edu.tinhoc123.quanlydoibong.R;
import vn.edu.tinhoc123.quanlydoibong.models.Coach;
import vn.edu.tinhoc123.quanlydoibong.repositories.FirebaseRepository;

public class CoachFragment extends Fragment {
    private TextView tvCoachName, tvCoachAge, tvCoachNationality, tvCoachTeam, tvCoachExp, tvCoachAchievements;
    private Button btnDeleteCoach, btnEditCoach, btnLogout;
    private FirebaseRepository repository;
    private Coach currentCoach;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coach, container, false);

        repository = new FirebaseRepository();
        tvCoachName = view.findViewById(R.id.tvCoachName);
        tvCoachAge = view.findViewById(R.id.tvCoachAge);
        tvCoachNationality = view.findViewById(R.id.tvCoachNationality);
        tvCoachTeam = view.findViewById(R.id.tvCoachTeam);
        tvCoachExp = view.findViewById(R.id.tvCoachExp);
        tvCoachAchievements = view.findViewById(R.id.tvCoachAchievements);
        btnDeleteCoach = view.findViewById(R.id.btnDeleteCoach);
        btnEditCoach = view.findViewById(R.id.btnEditCoach);
        btnLogout = view.findViewById(R.id.btnLogout);

        loadCoachInfo();

        btnEditCoach.setOnClickListener(v -> showEditCoachDialog());
        btnDeleteCoach.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext())
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa thông tin HLV này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    repository.getCoachRef().document("main_hlv").delete().addOnSuccessListener(aVoid -> {
                        loadCoachInfo();
                    });
                })
                .setNegativeButton("Hủy", null)
                .show();
        });

        btnLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext())
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc muốn đăng xuất không?")
                .setPositiveButton("Đăng xuất", (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .setNegativeButton("Hủy", null)
                .show();
        });

        return view;
    }

    private void loadCoachInfo() {
        repository.getCoachRef().document("main_hlv").addSnapshotListener((value, error) -> {
            if (error != null) return;
            if (value != null && value.exists()) {
                currentCoach = value.toObject(Coach.class);
                updateUI(currentCoach);
            } else {
                seedDefaultCoach();
            }
        });
    }

    private void seedDefaultCoach() {
        Coach coach = new Coach("main_hlv", "Xavi Hernández", 44, "Tây Ban Nha", "FC Barcelona", "15 năm", "2 La Liga, 1 Champions League");
        repository.saveCoach(coach);
    }

    private void updateUI(Coach coach) {
        if (coach == null) return;
        tvCoachName.setText(coach.getTenHLV());
        tvCoachAge.setText(String.valueOf(coach.getTuoi()));
        tvCoachNationality.setText(coach.getQuocTich());
        tvCoachTeam.setText(coach.getDoiBong());
        tvCoachExp.setText(coach.getKinhNghiem());
        tvCoachAchievements.setText(coach.getThanhTich());
    }

    private void showEditCoachDialog() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_coach, null);
        EditText etName = view.findViewById(R.id.etCoachName);
        EditText etAge = view.findViewById(R.id.etCoachAge);
        EditText etNat = view.findViewById(R.id.etCoachNationality);
        EditText etTeam = view.findViewById(R.id.etCoachTeam);
        EditText etExp = view.findViewById(R.id.etCoachExp);
        EditText etAch = view.findViewById(R.id.etCoachAchievements);

        if (currentCoach != null) {
            etName.setText(currentCoach.getTenHLV());
            etAge.setText(String.valueOf(currentCoach.getTuoi()));
            etNat.setText(currentCoach.getQuocTich());
            etTeam.setText(currentCoach.getDoiBong());
            etExp.setText(currentCoach.getKinhNghiem());
            etAch.setText(currentCoach.getThanhTich());
        }

        new AlertDialog.Builder(getContext())
            .setTitle("Sửa thông tin HLV")
            .setView(view)
            .setPositiveButton("Lưu", (dialog, which) -> {
                String name = etName.getText().toString();
                int age = Integer.parseInt(etAge.getText().toString());
                Coach updated = new Coach("main_hlv", name, age, etNat.getText().toString(), etTeam.getText().toString(), etExp.getText().toString(), etAch.getText().toString());
                repository.saveCoach(updated).addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show());
            })
            .setNegativeButton("Hủy", null)
            .show();
    }
}