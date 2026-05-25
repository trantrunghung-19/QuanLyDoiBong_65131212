package vn.edu.tinhoc123.quanlydoibong.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.HashMap;
import java.util.Map;
import vn.edu.tinhoc123.quanlydoibong.models.Coach;
import vn.edu.tinhoc123.quanlydoibong.models.Player;
import vn.edu.tinhoc123.quanlydoibong.models.Team;
import vn.edu.tinhoc123.quanlydoibong.models.User;

public class FirebaseRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    public FirebaseUser getCurrentUser() { return auth.getCurrentUser(); }
    public String getCurrentUserId() {
        FirebaseUser user = auth.getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

    public CollectionReference getUsersRef() { return db.collection("users"); }
    public CollectionReference getTeamsRef() { return db.collection("doi_bong"); }
    public CollectionReference getCoachRef() { return db.collection("hlv"); }

    public Task<Void> saveUser(User user) {
        return getUsersRef().document(user.getUid()).set(user);
    }

    public Task<DocumentReference> addTeam(Team team) {
        return getTeamsRef().add(team);
    }

    public Task<Void> updateTeam(String id, Team team) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("tenDoi", team.getTenDoi());
        updates.put("huanLuyenVien", team.getHuanLuyenVien());
        updates.put("sanNha", team.getSanNha());
        updates.put("soLuongCauThu", team.getSoLuongCauThu());
        updates.put("quocGia", team.getQuocGia());
        // Giữ nguyên userId, chỉ cập nhật thông tin và thời gian
        updates.put("updatedAt", FieldValue.serverTimestamp());
        return getTeamsRef().document(id).update(updates);
    }

    public Task<Void> deleteTeam(String teamId) {
        return getTeamsRef().document(teamId).delete();
    }

    public CollectionReference getPlayersRef(String teamId) {
        return getTeamsRef().document(teamId).collection("cau_thu");
    }

    public Task<DocumentSnapshot> getPlayerById(String teamId, String playerId) {
        return getPlayersRef(teamId).document(playerId).get();
    }

    public Task<DocumentReference> addPlayer(String teamId, Player player) {
        return getPlayersRef(teamId).add(player);
    }

    public Task<Void> updatePlayer(String teamId, String playerId, Player player) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("soAo", player.getSoAo());
        updates.put("tenCauThu", player.getTenCauThu());
        updates.put("viTri", player.getViTri());
        updates.put("chiSo", player.getChiSo());
        updates.put("updatedAt", FieldValue.serverTimestamp());
        return getPlayersRef(teamId).document(playerId).update(updates);
    }

    public Task<Void> deletePlayer(String teamId, String playerId) {
        return getPlayersRef(teamId).document(playerId).delete();
    }

    public Task<Void> saveCoach(Coach coach) {
        return getCoachRef().document("main_hlv").set(coach);
    }

    // Lấy danh sách đội bóng của User hiện tại
    public Query getTeamsByCurrentUser() {
        String userId = getCurrentUserId();
        if (userId == null) return getTeamsRef().limit(0);
        return getTeamsRef().whereEqualTo("userId", userId).orderBy("createdAt", Query.Direction.DESCENDING);
    }

    // Lấy 3 đội bóng mới nhất của User hiện tại cho trang chủ
    public Query getLatestTeamsByCurrentUser() {
        String userId = getCurrentUserId();
        if (userId == null) return getTeamsRef().limit(0);
        return getTeamsRef().whereEqualTo("userId", userId).orderBy("createdAt", Query.Direction.DESCENDING).limit(3);
    }

    public Query getPlayersByTeam(String teamId) {
        return getPlayersRef(teamId).orderBy("soAo", Query.Direction.ASCENDING);
    }
}