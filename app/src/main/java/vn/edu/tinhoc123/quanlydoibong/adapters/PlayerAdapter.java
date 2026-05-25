package vn.edu.tinhoc123.quanlydoibong.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import vn.edu.tinhoc123.quanlydoibong.PlayerDetailActivity;
import vn.edu.tinhoc123.quanlydoibong.R;
import vn.edu.tinhoc123.quanlydoibong.models.Player;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {
    private List<Player> players;
    private String teamId;

    public PlayerAdapter(List<Player> players, String teamId) {
        this.players = players;
        this.teamId = teamId;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        Player player = players.get(position);
        holder.tvPlayerNumber.setText(String.valueOf(player.getSoAo()));
        holder.tvPlayerName.setText(player.getTenCauThu());
        holder.tvPosition.setText(player.getViTri());
        holder.tvRating.setText(String.valueOf(player.getChiSo()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), PlayerDetailActivity.class);
            intent.putExtra("teamId", teamId);
            intent.putExtra("playerId", player.getId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    static class PlayerViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlayerNumber, tvPlayerName, tvPosition, tvRating;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlayerNumber = itemView.findViewById(R.id.tvPlayerNumber);
            tvPlayerName = itemView.findViewById(R.id.tvPlayerName);
            tvPosition = itemView.findViewById(R.id.tvPosition);
            tvRating = itemView.findViewById(R.id.tvRating);
        }
    }
}