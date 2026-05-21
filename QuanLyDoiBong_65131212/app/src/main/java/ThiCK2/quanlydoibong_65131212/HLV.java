package ThiCK2.quanlydoibong_65131212;

import java.io.Serializable;

public class HLV implements Serializable {
    private String id;
    private String maHLV;
    private String tenHLV;
    private int tuoi;
    private String quocTich;
    private String doiBong;
    private int kinhNghiem;
    private String thanhTich;
    private String avatarUrl;

    public HLV() {}

    public HLV(String maHLV, String tenHLV, int tuoi, String quocTich, String doiBong, int kinhNghiem, String thanhTich, String avatarUrl) {
        this.maHLV = maHLV;
        this.tenHLV = tenHLV;
        this.tuoi = tuoi;
        this.quocTich = quocTich;
        this.doiBong = doiBong;
        this.kinhNghiem = kinhNghiem;
        this.thanhTich = thanhTich;
        this.avatarUrl = avatarUrl;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getMaHLV() { return maHLV; }
    public void setMaHLV(String maHLV) { this.maHLV = maHLV; }
    public String getTenHLV() { return tenHLV; }
    public void setTenHLV(String tenHLV) { this.tenHLV = tenHLV; }
    public int getTuoi() { return tuoi; }
    public void setTuoi(int tuoi) { this.tuoi = tuoi; }
    public String getQuocTich() { return quocTich; }
    public void setQuocTich(String quocTich) { this.quocTich = quocTich; }
    public String getDoiBong() { return doiBong; }
    public void setDoiBong(String doiBong) { this.doiBong = doiBong; }
    public int getKinhNghiem() { return kinhNghiem; }
    public void setKinhNghiem(int kinhNghiem) { this.kinhNghiem = kinhNghiem; }
    public String getThanhTich() { return thanhTich; }
    public void setThanhTich(String thanhTich) { this.thanhTich = thanhTich; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
}
