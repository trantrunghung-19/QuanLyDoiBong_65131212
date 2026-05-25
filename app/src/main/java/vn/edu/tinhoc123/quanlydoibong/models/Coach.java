package vn.edu.tinhoc123.quanlydoibong.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;
import java.io.Serializable;

public class Coach implements Serializable {
    @DocumentId
    private String id;
    private String tenHLV;
    private int tuoi;
    private String quocTich;
    private String doiBong;
    private String kinhNghiem;
    private String thanhTich;
    private Timestamp updatedAt;

    public Coach() {}

    public Coach(String id, String tenHLV, int tuoi, String quocTich, String doiBong, String kinhNghiem, String thanhTich) {
        this.id = id;
        this.tenHLV = tenHLV;
        this.tuoi = tuoi;
        this.quocTich = quocTich;
        this.doiBong = doiBong;
        this.kinhNghiem = kinhNghiem;
        this.thanhTich = thanhTich;
        this.updatedAt = Timestamp.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTenHLV() { return tenHLV; }
    public void setTenHLV(String tenHLV) { this.tenHLV = tenHLV; }
    public int getTuoi() { return tuoi; }
    public void setTuoi(int tuoi) { this.tuoi = tuoi; }
    public String getQuocTich() { return quocTich; }
    public void setQuocTich(String quocTich) { this.quocTich = quocTich; }
    public String getDoiBong() { return doiBong; }
    public void setDoiBong(String doiBong) { this.doiBong = doiBong; }
    public String getKinhNghiem() { return kinhNghiem; }
    public void setKinhNghiem(String kinhNghiem) { this.kinhNghiem = kinhNghiem; }
    public String getThanhTich() { return thanhTich; }
    public void setThanhTich(String thanhTich) { this.thanhTich = thanhTich; }
    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}