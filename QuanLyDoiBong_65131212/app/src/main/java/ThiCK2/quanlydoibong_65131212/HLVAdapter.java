package ThiCK2.quanlydoibong_65131212;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HLVAdapter extends BaseAdapter {
    private Context context;
    private List<HLV> hlvList;
    private FirebaseFirestore db;

    public HLVAdapter(Context context, List<HLV> hlvList) {
        this.context = context;
        this.hlvList = hlvList;
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    public int getCount() {
        return hlvList.size();
    }

    @Override
    public Object getItem(int position) {
        return hlvList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_hlv, parent, false);
        }

        HLV hlv = hlvList.get(position);

        CircleImageView ivAvatar = convertView.findViewById(R.id.iv_hlv_avatar);
        TextView tvName = convertView.findViewById(R.id.tv_hlv_name);
        TextView tvTeam = convertView.findViewById(R.id.tv_hlv_team);
        TextView tvInfo = convertView.findViewById(R.id.tv_hlv_info);
        ImageButton btnDelete = convertView.findViewById(R.id.btn_delete_hlv);

        tvName.setText(hlv.getTenHLV());
        tvTeam.setText("Đội bóng: " + hlv.getDoiBong());
        tvInfo.setText("Kinh nghiệm: " + hlv.getKinhNghiem() + " năm | QT: " + hlv.getQuocTich());

        if (hlv.getAvatarUrl() != null && !hlv.getAvatarUrl().isEmpty()) {
            Glide.with(context).load(hlv.getAvatarUrl()).placeholder(android.R.drawable.ic_menu_myplaces).into(ivAvatar);
        } else {
            ivAvatar.setImageResource(android.R.drawable.ic_menu_myplaces);
        }

        btnDelete.setOnClickListener(v -> {
            if (hlv.getAvatarUrl() != null && !hlv.getAvatarUrl().isEmpty()) {
                try {
                    FirebaseStorage.getInstance().getReferenceFromUrl(hlv.getAvatarUrl()).delete();
                } catch (Exception ignored) {}
            }
            db.collection("HLV").document(hlv.getId()).delete()
                    .addOnSuccessListener(aVoid -> Toast.makeText(context, "Đã xóa HLV", Toast.LENGTH_SHORT).show());
        });

        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SuaHLVActivity.class);
            intent.putExtra("HLV", hlv);
            context.startActivity(intent);
        });

        return convertView;
    }
}
