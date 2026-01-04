package ProjectJava;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    private Nhap nhap;
    private HoaDon hoadon;
    JPanel contentCard;
    CardLayout card;
    private nhanvien nhanvien;
    private QuanLyKhoView quanlykho;

    public Main(nhanvien userLogin) {
        nhap = new Nhap(this);
        this.nhanvien = userLogin;
        
        hoadon = new HoaDon(0, null, null, null, 0.0, 0, null, null);
        
        setTitle("HỆ THỐNG QUẢN LÝ CỬA HÀNG");
        //setSize(900, 600);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        //this.setResizable(true);

        card = new CardLayout();
        contentCard = new JPanel(card);

        // ====== ADD CÁC MÀN HÌNH ======
        contentCard.add(nhap, "HOME");
        contentCard.add(new ReportPanel(this), "REPORT");
        contentCard.add(new FrmKhachHang(this), "KHACHHANG");
        quanlykho = new QuanLyKhoView(this);
        contentCard.add(new QuanLyKhoView(this), "KHO");
        contentCard.add(new JFqlnhanvien(nhanvien, this), "NHANVIEN");
        contentCard.add(new HoaDonPanel(this, nhap, hoadon), "HOADON");

        add(contentCard);
        card.show(contentCard, "HOME");

        setVisible(true);
    }

    public void showScreen(String name) {
        card.show(contentCard, name);
    }

    public static void main(String[] args) {
        new DangNhap().setVisible(true);
    }
    public nhanvien getNhanVien() {
    return this.nhanvien;
    }
    public QuanLyKhoView getQuanLyKho(){
        return this.quanlykho;
    }
}
