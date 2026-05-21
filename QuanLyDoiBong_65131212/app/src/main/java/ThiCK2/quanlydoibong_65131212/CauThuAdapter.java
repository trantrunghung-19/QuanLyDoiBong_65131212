package ThiCK2.quanlydoibong_65131212;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class CauThuAdapter extends BaseAdapter {
    private Context context;
    private List<CauThu> cauThuList;
    private String doiBongId;
    private FirebaseFirestore db;

    public CauThuAdapter(Context context, List<CauThu> cauThuList, String doiBongId) {
        this.context = context;
        this.cauThuList = cauThuList;
        this.doiBongId = doiBongId;
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    public int getCount() {
        return cauThuList.size();
    }

    @Override
    public Object getItem(int position) {
        return cauThuList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cauthu, parent, false);
        }

        CauThu cauThu = cauThuList.get(position);

        TextView tvTen = convertView.findViewById(R.id.tv_ten_cau_thu);
        TextView tvSoAo = convertView.findViewById(R.id.tv_so_ao);
        TextView tvViTri = convertView.findViewById(R.id.tv_vi_tri_chi_so);
        TextView tvChiSo = convertView.findViewById(R.id.tv_chi_so_value);
        TextView tvThongSo = convertView.findViewById(R.id.tv_thong_so);
        ImageView ivPlayer = convertView.findViewById(R.id.iv_cauthu_list);
        ImageButton btnSua = convertView.findViewById(R.id.btn_sua_cau_thu);
        ImageButton btnXoa = convertView.findViewById(R.id.btn_xoa_cau_thu);

        tvTen.setText(cauThu.getTenCauThu());
        tvSoAo.setText("#" + cauThu.getSoAo());
        tvViTri.setText(cauThu.getViTri());
        tvChiSo.setText(String.valueOf(cauThu.getChiSo()));
        tvThongSo.setText("G: " + cauThu.getBanThang() + " | A: " + cauThu.getKienTao());

        if (cauThu.getImageUrl() != null && !cauThu.getImageUrl().isEmpty()) {
            ivPlayer.setImageTintList(null); // Xóa bỏ màu xám để hiện ảnh thật
            Glide.with(context)
                    .load(cauThu.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(ivPlayer);
        } else {
            ivPlayer.setImageResource(android.R.drawable.ic_menu_report_image);
            ivPlayer.setImageTintList(context.getColorStateList(R.color.light_grey));
        }

        btnSua.setOnClickListener(v -> {
            Intent intent = new Intent(context, SuaCauThuActivity.class);
            intent.putExtra("CAU_THU", cauThu);
            intent.putExtra("DOI_BONG_ID", doiBongId);
            context.startActivity(intent);
        });

        btnXoa.setOnClickListener(v -> {
            if (cauThu.getImageUrl() != null && !cauThu.getImageUrl().isEmpty()) {
                try {
                    FirebaseStorage.getInstance().getReferenceFromUrl(cauThu.getImageUrl()).delete();
                } catch (Exception ignored) {}
            }
            db.collection("DOIBONG").document(doiBongId)
                    .collection("CAUTHU").document(cauThu.getId()).delete()
                    .addOnSuccessListener(aVoid -> Toast.makeText(context, "Đã chấm dứt hợp đồng", Toast.LENGTH_SHORT).show());
        });

        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChiTietCauThuActivity.class);
            intent.putExtra("CAU_THU", cauThu);
            context.startActivity(intent);
        });

        return convertView;
    }
}
