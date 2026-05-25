package vn.edu.tinhoc123.quanlydoibong.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;
import java.io.Serializable;

public class Team implements Serializable {
    @DocumentId
    private String id;
    private String tenDoi;
    private String huanLuyenVien;
    private String sanNha;
    private int soLuongCauThu;
    private String quocGia;
    private String userId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Team() {}

    public Team(String id, String tenDoi, String huanLuyenVien, String sanNha, int soLuongCauThu, String quocGia, String userId) {
        this.id = id;
        this.tenDoi = tenDoi;
        this.huanLuyenVien = huanLuyenVien;
        this.sanNha = sanNha;
        this.soLuongCauThu = soLuongCauThu;
        this.quocGia = quocGia;
        this.userId = userId;
        this.createdAt = Timestamp.now();
        this.updatedAt = Timestamp.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTenDoi() { return tenDoi; }
    public void setTenDoi(String tenDoi) { this.tenDoi = tenDoi; }
    public String getHuanLuyenVien() { return huanLuyenVien; }
    public void setHuanLuyenVien(String huanLuyenVien) { this.huanLuyenVien = huanLuyenVien; }
    public String getSanNha() { return sanNha; }
    public void setSanNha(String sanNha) { this.sanNha = sanNha; }
    public int getSoLuongCauThu() { return soLuongCauThu; }
    public void setSoLuongCauThu(int soLuongCauThu) { this.soLuongCauThu = soLuongCauThu; }
    public String getQuocGia() { return quocGia; }
    public void setQuocGia(String quocGia) { this.quocGia = quocGia; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}