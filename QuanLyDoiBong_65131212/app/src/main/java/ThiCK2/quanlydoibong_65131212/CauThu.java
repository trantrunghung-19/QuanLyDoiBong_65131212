package ThiCK2.quanlydoibong_65131212;

import java.io.Serializable;

public class CauThu implements Serializable {
    private String id;
    private String maCauThu;
    private String tenCauThu;
    private int soAo;
    private String viTri;
    private int tuoi;
    private String quocTich;
    private int chiSo;
    private int banThang;
    private int kienTao;
    private String imageUrl;

    public CauThu() {}

    public CauThu(String maCauThu, String tenCauThu, int soAo, String viTri, int tuoi, String quocTich, int chiSo, int banThang, int kienTao, String imageUrl) {
        this.maCauThu = maCauThu;
        this.tenCauThu = tenCauThu;
        this.soAo = soAo;
        this.viTri = viTri;
        this.tuoi = tuoi;
        this.quocTich = quocTich;
        this.chiSo = chiSo;
        this.banThang = banThang;
        this.kienTao = kienTao;
        this.imageUrl = imageUrl;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getMaCauThu() { return maCauThu; }
    public void setMaCauThu(String maCauThu) { this.maCauThu = maCauThu; }
    public String getTenCauThu() { return tenCauThu; }
    public void setTenCauThu(String tenCauThu) { this.tenCauThu = tenCauThu; }
    public int getSoAo() { return soAo; }
    public void setSoAo(int soAo) { this.soAo = soAo; }
    public String getViTri() { return viTri; }
    public void setViTri(String viTri) { this.viTri = viTri; }
    public int getTuoi() { return tuoi; }
    public void setTuoi(int tuoi) { this.tuoi = tuoi; }
    public String getQuocTich() { return quocTich; }
    public void setQuocTich(String quocTich) { this.quocTich = quocTich; }
    public int getChiSo() { return chiSo; }
    public void setChiSo(int chiSo) { this.chiSo = chiSo; }
    public int getBanThang() { return banThang; }
    public void setBanThang(int banThang) { this.banThang = banThang; }
    public int getKienTao() { return kienTao; }
    public void setKienTao(int kienTao) { this.kienTao = kienTao; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
