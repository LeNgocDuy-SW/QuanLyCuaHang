package ProjectJava;
import java.util.Date;
import java.sql.*;
import java.util.ArrayList;
public class KhachHang {
    private String makh;
    private String tenkh;
    private String sdt;
    private String diachi;
    private Date ngaysinh;
    private int diemtichluy;
    public KhachHang(String makh, String tenkh, String sdt, String diachi, Date ngaysinh, int diemtichluy) {
        this.makh = makh;
        this.tenkh = tenkh;
        this.sdt = sdt;
        this.diachi = diachi;
        this.ngaysinh = ngaysinh;
        this.diemtichluy = diemtichluy;
    }
    public String getMaKH(){return makh;}
    public String getTenKH(){return tenkh;}
    public String getSDT(){return sdt;}
    public String getDiaChi(){return diachi;}
    public Date getNgaySinh(){return ngaysinh;}
    public int getDiemTL(){return diemtichluy;}

    public static ArrayList<KhachHang> getAllKhachHang(){
        ArrayList<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang";
        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    KhachHang kh = new KhachHang(
                        rs.getString("makh"),
                        rs.getString("tenkh"),
                        rs.getString("sdt"),
                        rs.getString("diachi"),
                        rs.getDate("ngaysinh"),
                        rs.getInt("diemtichluy")
                    );
                    list.add(kh);
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
            return list;
    }
    public static ArrayList<KhachHang> searchKhachHang(String sdt){
        ArrayList<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang WHERE sdt =?";
        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
                ps.setString(1, sdt);
                try(ResultSet rs = ps.executeQuery()){
                    while(rs.next()){
                        KhachHang kh = new KhachHang(
                            rs.getString("makh"),
                            rs.getString("tenkh"),
                            rs.getString("sdt"),
                            rs.getString("diachi"),
                            rs.getDate("ngaysinh"),
                            rs.getInt("diemtichluy")
                        );
                        list.add(kh);
                    }
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
            return list;
    }
    // add
    public static boolean addKhachHangToDB(KhachHang kh){
        String sql = "INSERT INTO KhachHang(tenkh, sdt, diachi, ngaysinh, diemtichluy) VALUES(?,?,?,?,?)";
        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
                ps.setString(1, kh.getTenKH());
                ps.setString(2, kh.getSDT());
                ps.setString(3, kh.getDiaChi());
                ps.setDate(4, new java.sql.Date(kh.getNgaySinh().getTime()));
                ps.setInt(5, kh.getDiemTL());
                int rowsAffected = ps.executeUpdate();
                return rowsAffected > 0; // true nếu thêm thành công ;
            }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public static boolean congDiemTichLuy(String sdt, int diemcong){
        String sql = "UPDATE KhachHang SET diemtichluy = diemtichluy + ? WHERE sdt = ?";
        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
                ps.setInt(1, diemcong);
                ps.setString(2, sdt);
                int rowsAffected = ps.executeUpdate();
                return rowsAffected > 0;
            }
            catch(SQLException e){
                e.printStackTrace();
                return false;
            }
    }
}
