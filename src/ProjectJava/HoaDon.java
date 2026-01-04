package ProjectJava;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Date;

public class HoaDon {
    private int mahd;
    private Date ngaytao;

    private String sdt_kh;
    private String ten_kh;
    private double tongtien;
    private int manv;
    private String dsSanPham;
    private String phuongthuc;

    public HoaDon(int mahd, Date ngaytao, String sdt_kh,String ten_kh, double tongtien, int manv, String dsSanPham, String phuongthuc) {
        this.mahd = mahd;
        this.ngaytao = ngaytao;
        this.sdt_kh = sdt_kh;
        this.ten_kh = ten_kh;
        this.tongtien = tongtien;
        this.manv = manv;
        this.dsSanPham = dsSanPham;
        this.phuongthuc = phuongthuc;
    }

    // --- CÁC HÀM GETTER (Phải đúng tên như bên Nhap.java gọi) ---
    
    public int getMaHD() { 
        return mahd; 
    }

    // Sửa tên hàm này cho khớp với Nhap.java
    public Date getNgayTao() { 
        return ngaytao; 
    }

    // Nếu Nhap.java gọi getSdt() hoặc getSDT_KH() thì sửa cho khớp
    public String getSdt() { 
        return sdt_kh; 
    }
    public String getTenKH(){return ten_kh;}
    
    public double getTongTien() { 
        return tongtien; 
    }
    public int getMaNV() { 
        return manv; 
    }
    public String getDsSanPham() { 
        return dsSanPham; 
    }
    public String getPhuongThuc(){
        return phuongthuc;
    }

    // --- HÀM LƯU HÓA ĐƠN ---
    public static boolean saveInvoice(String sdt_kh, double tongtien, Map<String, CartItem> cartMap, int manv, String phuongthuc) {
        Connection con = null;
        PreparedStatement psHD = null;
        PreparedStatement psCT = null;
        PreparedStatement psKho = null;
        ResultSet rs = null;

        StringBuilder sb = new StringBuilder();
        for(CartItem item : cartMap.values()){
            if(sb.length() > 0) sb.append(",");
            sb.append(item.product.getTenSP()).append(" (x").append(item.quantity).append(")");
        }
        String chuoiDSSP = sb.toString();
        String sqlHD = "INSERT INTO HoaDon(sdt_kh, tongtien, ngaytao, manv, phuongthuc, dssanpham, ten_kh) VALUES(?, ?, GETDATE(), ?, ?, ?, ?)";
        String sqlCT = "INSERT INTO ChiTietHoaDon(mahd, masp, soluong, dongia) VALUES(?, ?, ?, ?)";
        String sqlKho = "UPDATE san_pham SET so_luong = so_luong - ? WHERE ma_sp = ?";
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            psHD = con.prepareStatement(sqlHD, Statement.RETURN_GENERATED_KEYS);
            psHD.setString(1, sdt_kh.isEmpty() ? "Khách Lẻ" : sdt_kh);
            psHD.setDouble(2, tongtien);
            psHD.setInt(3, manv);
            psHD.setString(4, phuongthuc);
            psHD.setString(5, chuoiDSSP);

            String tenKH = "Khách lẻ";
            if(!sdt_kh.isEmpty()){
                ArrayList<KhachHang> khs = KhachHang.searchKhachHang(sdt_kh);
                if(!khs.isEmpty()) tenKH = khs.get(0).getTenKH();
            }
            psHD.setString(6, tenKH);
            psHD.executeUpdate();

            rs = psHD.getGeneratedKeys();
            int mahdHienTai = 0;
            if (rs.next()) {
                mahdHienTai = rs.getInt(1);
            }


            psCT = con.prepareStatement(sqlCT);
            psKho = con.prepareStatement(sqlKho);
            for (CartItem item : cartMap.values()) {
                //System.out.println("Đang lưu sản phẩm có mã " + item.product.getMaSP());
                psCT.setInt(1, mahdHienTai);
                psCT.setString(2, item.product.getMaSP());
                psCT.setInt(3, item.quantity);
                psCT.setDouble(4, item.product.getGiaBan());
                psCT.addBatch();
                psKho.setInt(1, item.quantity);
                psKho.setString(2, item.product.getMaSP());
                psKho.addBatch();
            }
            psCT.executeBatch();
            psKho.executeBatch();
            con.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try { if (con != null) con.rollback(); } catch (SQLException ex) { }
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (psHD != null) psHD.close();
                if (psCT != null) psCT.close();
                if (psKho != null) psKho.close();
                if (con != null) { con.setAutoCommit(true); con.close(); }
            } catch (SQLException e) { }
        }
    }


public static ArrayList<HoaDon> getAllHoaDon(String maNhanVienFilter) {
    ArrayList<HoaDon> list = new ArrayList<>();

    // 1. CHUẨN BỊ SQL (Dùng đúng tên cột 'mahd' như bạn đã test thành công)
    StringBuilder sb = new StringBuilder();
    
    sb.append("SELECT hd.mahd, hd.ngaytao, hd.sdt_kh, hd.tongtien, hd.manv, kh.tenkh, hd.phuongthuc, nv.name_employee, ");
    
    // Hàm gộp sản phẩm
    sb.append("STRING_AGG(CONCAT(sp.ten_sp, ' (x', ct.soluong, ')'), ', ') WITHIN GROUP (ORDER BY sp.ten_sp) AS ds_sanpham ");
    
    sb.append("FROM HoaDon hd ");
    
    // JOIN các bảng
    sb.append("LEFT JOIN KhachHang kh ON hd.sdt_kh = kh.sdt ");
    sb.append("LEFT JOIN ChiTietHoaDon ct ON hd.mahd = ct.mahd "); 
    sb.append("LEFT JOIN san_pham sp ON ct.masp = sp.ma_sp ");
    sb.append("LEFT JOIN NhanVien nv ON hd.manv = CAST(nv.ID_employee AS VARCHAR(50)) ");

    // --- QUAN TRỌNG: MÌNH ĐÃ TẮT BỘ LỌC ĐỂ KIỂM TRA DỮ LIỆU ---
    // if (maNhanVienFilter != null) {
    //    sb.append("WHERE hd.manv = ? ");
    // }
    // -----------------------------------------------------------

    sb.append("GROUP BY hd.mahd, hd.ngaytao, hd.sdt_kh, hd.tongtien, hd.manv, kh.tenkh, hd.phuongthuc, nv.name_employee ");
    sb.append("ORDER BY hd.ngaytao DESC");

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sb.toString())) {
        
        // --- CŨNG TẮT LUÔN PHẦN TRUYỀN THAM SỐ ---
        // if (maNhanVienFilter != null) {
        //    ps.setString(1, maNhanVienFilter);
        // }
        // ------------------------------------------

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            
            
            String sdt = rs.getString("sdt_kh"); if(sdt == null) sdt = "";
            String tenkh = rs.getString("tenkh"); if(tenkh == null) tenkh = "Khách Lẻ";
            String dssp = rs.getString("ds_sanpham"); if(dssp == null) dssp = "";

            list.add(new HoaDon(
                rs.getInt("mahd"),
                rs.getTimestamp("ngaytao"),
                sdt,
                tenkh,
                rs.getDouble("tongtien"),
                rs.getInt("manv"),
                dssp,
                rs.getString("phuongthuc")
            ));
        }
    } catch (SQLException e) {
        System.out.println("Lỗi SQL: " + e.getMessage());
        e.printStackTrace();
    }
    return list;
}
}