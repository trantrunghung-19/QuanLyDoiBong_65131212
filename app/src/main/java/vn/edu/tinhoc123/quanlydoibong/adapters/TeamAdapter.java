package vn.edu.tinhoc123.quanlydoibong.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import vn.edu.tinhoc123.quanlydoibong.R;
import vn.edu.tinhoc123.quanlydoibong.TeamDetailActivity;
import vn.edu.tinhoc123.quanlydoibong.models.Team;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {
    private List<Team> teams;
    private boolean isHomeLayout;

    public TeamAdapter(List<Team> teams, boolean isHomeLayout) {
        this.teams = teams;
        this.isHomeLayout = isHomeLayout;
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = isHomeLayout ? R.layout.item_team_home : R.layout.item_team;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new TeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        Team team = teams.get(position);
        holder.tvTeamName.setText(team.getTenDoi());
        holder.tvPlayersCount.setText("Cầu thủ: " + team.getSoLuongCauThu());

        if (!isHomeLayout) {
            holder.tvCoach.setText("HLV: " + team.getHuanLuyenVien());
            holder.tvStadium.setText("Sân: " + team.getSanNha());
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), TeamDetailActivity.class);
            intent.putExtra("teamId", team.getId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    static class TeamViewHolder extends RecyclerView.ViewHolder {
        TextView tvTeamName, tvPlayersCount, tvCoach, tvStadium;

        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTeamName = itemView.findViewById(R.id.tvTeamName);
            tvPlayersCount = itemView.findViewById(R.id.tvPlayersCount);
            tvCoach = itemView.findViewById(R.id.tvCoach);
            tvStadium = itemView.findViewById(R.id.tvStadium);
        }
    }
}