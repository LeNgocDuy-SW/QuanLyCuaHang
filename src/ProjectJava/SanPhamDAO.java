package ProjectJava;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SanPhamDAO {
//Function 1.Chọn toàn bộ để hiển thị
    public ArrayList<SanPham> selectAll() {
        ArrayList<SanPham> ketQua = new ArrayList<>();
        String sql = "SELECT * FROM san_pham";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement prStmt = conn.prepareStatement(sql);
             ResultSet rs = prStmt.executeQuery()) {
            while (rs.next()) {
                String ma = rs.getString("ma_sp").trim();
                String ten = rs.getString("ten_sp").trim();
                int sl = rs.getInt("so_luong");
                String dv = rs.getString("donvi_tinh").trim();
                double giaNhap = rs.getDouble("gia_nhap");
                double giaBan = rs.getDouble("gia_ban");
                String hinhAnh = rs.getString("hinh_anh");
                if (hinhAnh == null) hinhAnh = "no_image.png";

                ketQua.add(new SanPham(ma, ten, sl, dv, giaNhap, giaBan, hinhAnh));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }
//Function 2. Thêm sản phẩm mới
    public void insert(SanPham sp) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO san_pham(ma_sp, ten_sp, so_luong, donvi_tinh, gia_nhap, gia_ban, hinh_anh) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement prStmt = conn.prepareStatement(sql);
            prStmt.setString(1, sp.getMaSP());
            prStmt.setString(2, sp.getTenSP());
            prStmt.setInt(3, sp.getSoLuong());
            prStmt.setString(4, sp.getDonViTinh());
            prStmt.setDouble(5, sp.getGiaNhap());
            prStmt.setDouble(6, sp.getGiaBan());
            prStmt.setString(7, sp.getHinhAnh());
            prStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//Function 3. Sửa thông tin sản phẩm
    public void capNhatThongTin(String maSP, int soLuong, double giaNhap, double giaBan, String hinhAnh) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "UPDATE san_pham SET so_luong=?, gia_nhap=?, gia_ban=?, hinh_anh=? WHERE ma_sp=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, soLuong);
            pst.setDouble(2, giaNhap);
            pst.setDouble(3, giaBan);
            pst.setString(4, hinhAnh);
            pst.setString(5, maSP);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//Function 4. Xóa sản phẩm
    public void delete(String maSP) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "DELETE FROM san_pham WHERE ma_sp = ?";
            PreparedStatement prStmt = conn.prepareStatement(sql);
            prStmt.setString(1, maSP);
            prStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
// Function 5. Kiểm tra tồn tại
    public boolean checkExist(String maSP) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM san_pham WHERE ma_sp = ?";
            PreparedStatement prStmt = conn.prepareStatement(sql);
            prStmt.setString(1, maSP);
            ResultSet rs = prStmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
//Function 6. Nhập số lượng sản phẩm
    public void nhapHang(String maSP, int soLuong, double giaNhap) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "UPDATE san_pham SET so_luong = so_luong + ?, gia_nhap = ? WHERE ma_sp = ?";
            PreparedStatement prStmt = conn.prepareStatement(sql);
            prStmt.setInt(1, soLuong);
            prStmt.setDouble(2, giaNhap);
            prStmt.setString(3, maSP);
            prStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//Function 7. Tìm kiếm theo tên
    public ArrayList<SanPham> findByName(String keyword) {
        ArrayList<SanPham> ketQua = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM san_pham WHERE ten_sp LIKE ?";
            PreparedStatement prStmt = conn.prepareStatement(sql);
            prStmt.setString(1, "%" + keyword + "%");
            ResultSet rs = prStmt.executeQuery();
            while (rs.next()) {
                String hinhAnh = rs.getString("hinh_anh");
                if(hinhAnh == null) hinhAnh = "no_image.png";
                ketQua.add(new SanPham(
                        rs.getString("ma_sp"), rs.getString("ten_sp"), rs.getInt("so_luong"),
                        rs.getString("donvi_tinh"), rs.getDouble("gia_nhap"), rs.getDouble("gia_ban"), hinhAnh
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }
}
