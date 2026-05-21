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

public class DoiBongAdapter extends BaseAdapter {
    private Context context;
    private List<DoiBong> doiBongList;
    private FirebaseFirestore db;

    public DoiBongAdapter(Context context, List<DoiBong> doiBongList) {
        this.context = context;
        this.doiBongList = doiBongList;
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    public int getCount() {
        return doiBongList.size();
    }

    @Override
    public Object getItem(int position) {
        return doiBongList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_doi_bong, parent, false);
        }

        DoiBong doiBong = doiBongList.get(position);

        TextView tvTenDoi = convertView.findViewById(R.id.tv_ten_doi);
        TextView tvMaDoi = convertView.findViewById(R.id.tv_ma_doi);
        TextView tvHlv = convertView.findViewById(R.id.tv_hlv);
        ImageView ivLogo = convertView.findViewById(R.id.iv_logo_list);
        ImageButton btnSua = convertView.findViewById(R.id.btn_sua);
        ImageButton btnXoa = convertView.findViewById(R.id.btn_xoa);

        tvTenDoi.setText(doiBong.getTenDoi());
        tvMaDoi.setText("ID: " + doiBong.getMaDoi());
        tvHlv.setText("HLV: " + doiBong.getHuanLuyenVien());

        if (doiBong.getLogoUrl() != null && !doiBong.getLogoUrl().isEmpty()) {
            ivLogo.setImageTintList(null);
            Glide.with(context)
                    .load(doiBong.getLogoUrl())
                    .placeholder(android.R.drawable.ic_menu_myplaces)
                    .into(ivLogo);
        } else {
            ivLogo.setImageResource(android.R.drawable.ic_menu_myplaces);
            ivLogo.setImageTintList(context.getColorStateList(R.color.neon_green));
        }

        btnSua.setOnClickListener(v -> {
            Intent intent = new Intent(context, SuaDoiActivity.class);
            intent.putExtra("DOI_BONG", doiBong);
            context.startActivity(intent);
        });

        btnXoa.setOnClickListener(v -> {
            if (doiBong.getLogoUrl() != null && !doiBong.getLogoUrl().isEmpty()) {
                try {
                    FirebaseStorage.getInstance().getReferenceFromUrl(doiBong.getLogoUrl()).delete();
                } catch (Exception ignored) {}
            }
            db.collection("DOIBONG").document(doiBong.getId()).delete()
                    .addOnSuccessListener(aVoid -> Toast.makeText(context, "Đã xóa đội bóng", Toast.LENGTH_SHORT).show());
        });

        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CauThuActivity.class);
            // Pass the whole team object for the new detail header design
            intent.putExtra("DOI_BONG", doiBong);
            context.startActivity(intent);
        });

        return convertView;
    }
}
