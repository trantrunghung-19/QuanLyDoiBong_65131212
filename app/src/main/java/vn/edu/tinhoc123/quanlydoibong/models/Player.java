package vn.edu.tinhoc123.quanlydoibong.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;
import java.io.Serializable;

public class Player implements Serializable {
    @DocumentId
    private String id;
    private int soAo;
    private String tenCauThu;
    private String viTri;
    private int chiSo;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Player() {}

    public Player(String id, int soAo, String tenCauThu, String viTri, int chiSo) {
        this.id = id;
        this.soAo = soAo;
        this.tenCauThu = tenCauThu;
        this.viTri = viTri;
        this.chiSo = chiSo;
        this.createdAt = Timestamp.now();
        this.updatedAt = Timestamp.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public int getSoAo() { return soAo; }
    public void setSoAo(int soAo) { this.soAo = soAo; }
    public String getTenCauThu() { return tenCauThu; }
    public void setTenCauThu(String tenCauThu) { this.tenCauThu = tenCauThu; }
    public String getViTri() { return viTri; }
    public void setViTri(String viTri) { this.viTri = viTri; }
    public int getChiSo() { return chiSo; }
    public void setChiSo(int chiSo) { this.chiSo = chiSo; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}