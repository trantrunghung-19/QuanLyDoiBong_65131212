package ThiCK2.quanlydoibong_65131212;

import java.io.Serializable;

public class DoiBong implements Serializable {
    private String id; // Firestore document ID
    private String maDoi;
    private String tenDoi;
    private String huanLuyenVien;
    private String sanNha;
    private int soLuongCauThu;
    private String logoUrl;

    public DoiBong() {
        // Required for Firestore
    }

    public DoiBong(String maDoi, String tenDoi, String huanLuyenVien, String sanNha, int soLuongCauThu, String logoUrl) {
        this.maDoi = maDoi;
        this.tenDoi = tenDoi;
        this.huanLuyenVien = huanLuyenVien;
        this.sanNha = sanNha;
        this.soLuongCauThu = soLuongCauThu;
        this.logoUrl = logoUrl;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getMaDoi() { return maDoi; }
    public void setMaDoi(String maDoi) { this.maDoi = maDoi; }

    public String getTenDoi() { return tenDoi; }
    public void setTenDoi(String tenDoi) { this.tenDoi = tenDoi; }

    public String getHuanLuyenVien() { return huanLuyenVien; }
    public void setHuanLuyenVien(String huanLuyenVien) { this.huanLuyenVien = huanLuyenVien; }

    public String getSanNha() { return sanNha; }
    public void setSanNha(String sanNha) { this.sanNha = sanNha; }

    public int getSoLuongCauThu() { return soLuongCauThu; }
    public void setSoLuongCauThu(int soLuongCauThu) { this.soLuongCauThu = soLuongCauThu; }

    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
}
