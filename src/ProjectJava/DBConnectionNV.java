package ProjectJava;

import java.sql.Connection;
import java.time.LocalDate;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.sql.Date;



public class DBConnectionNV {
    private static LocalDate toLocalDate(Date date) {
        return (date != null) ? date.toLocalDate() : null;
    }
    public static List<nhanvien> findAll() {
        List<nhanvien> nhanvienList = new ArrayList<>();
        
    
        
        try(Connection connection = DBConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet  = statement.executeQuery("SELECT * FROM NhanVien")) {
            //lay tat ca danh sach nhan vien
            //connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/qlnhanvien", "root", "");
            
            //quer
            
            while(resultSet.next()){
                LocalDate dob = resultSet.getObject("date_of_birth", LocalDate.class);
                LocalDate sd = resultSet.getObject("start_date", LocalDate.class);
                
                nhanvien nv;
                nv = new nhanvien(resultSet.getInt("ID_employee"), resultSet.getString("name_employee"), dob, 
                        resultSet.getString("gender"),resultSet.getString("phonenumber"), resultSet.getString("address"), resultSet.getString("email"),
                        resultSet.getString("part"), sd, resultSet.getString("role"), resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("avatar"));
                nhanvienList.add(nv);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        //ket thuc
        return nhanvienList;
    }
    
    public static void insert(nhanvien nv){
        String sql = "INSERT INTO NhanVien(ID_employee, name_employee, date_of_birth, gender, phonenumber, address, email, part, start_date, role, username, password, avatar) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try(Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            //lay tat ca danh sach nhan vien
            
            //query
            
            statement.setInt(1, nv.getID_employee());
            statement.setString(2, nv.getName_employee());
            statement.setObject(3, nv.getDate_of_birth());
            statement.setString(4, nv.getGender());
            statement.setString(5, nv.getPhonenumber());
            statement.setString(6, nv.getAddress());
            statement.setString(7, nv.getEmail());
            statement.setString(8, nv.getPart());
            statement.setObject(9, nv.getStart_date());
            statement.setString(10, nv.getRole());
            statement.setString(11, nv.getUsername());
            statement.setString(12, nv.getPassword());
            statement.setString(13, nv.getAvatar());
            
            statement.executeUpdate();
           
        } catch (SQLException ex) {
            ex.printStackTrace();
        } 
        //ket thuc
    }
    
    public static boolean existsById(int ID_employee) {
        String sql = "SELECT 1 FROM nhanvien WHERE ID_employee = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ID_employee);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            return false;
        }
    }

    
     public static void update(nhanvien nv, int idCu){
        String sql = "UPDATE NhanVien SET ID_employee=?, name_employee=?, date_of_birth=?, gender=?, phonenumber=?, address=?, email=?, part=?, start_date=?, role=?, username=?, password=?, avatar=? WHERE ID_employee=?";
        
        try(Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            //lay tat ca danh sach nhan vien
        
            
            statement.setInt(1, nv.getID_employee());
            statement.setString(2, nv.getName_employee());
            statement.setObject(3, nv.getDate_of_birth());
            statement.setString(4, nv.getGender());
            statement.setString(5, nv.getPhonenumber());
            statement.setString(6, nv.getAddress());
            statement.setString(7, nv.getEmail());
            statement.setString(8, nv.getPart());
            statement.setObject(9, nv.getStart_date());
            statement.setString(10, nv.getRole());
            statement.setString(11, nv.getUsername());
            statement.setString(12, nv.getPassword());
            statement.setString(13, nv.getAvatar());
     
            statement.setInt(14, idCu);
            
           int rowsAffected = statement.executeUpdate(); 
           if (rowsAffected > 0) {
                System.out.println("Cập nhật thành công từ ID " + idCu + " sang ID " + nv.getID_employee());
                System.out.println("Cập nhật thành công (có avatar)");
            } else {
                System.out.println("Không tìm thấy nhân viên với ID cũ: " + idCu);
            }
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1062) { // Mã lỗi trùng khóa chính trong MySQL
                System.err.println("Lỗi: ID mới đã tồn tại, không thể cập nhật!");
            } else {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        
        //ket thuc
    }
     
     public static void delete(int ID_employee){
        
        
        try(Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM NhanVien WHERE ID_employee = ?")) {
            //lay tat ca danh sach nhan vien
           
            
            statement.setInt(1, ID_employee);
            
            statement.execute();
           
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
     
     public static List<nhanvien> findByFullname(String name_employee) {
        List<nhanvien> nhanvienList = new ArrayList<>();
        
        String sql = "SELECT * FROM NhanVien Where name_employee LIKE ?";
        
        try (Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            //lay tat ca danh sach nhan vien
            
            
            statement.setString(1, "%" + name_employee + "%");
            
            ResultSet resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                LocalDate dob = resultSet.getObject("date_of_birth", LocalDate.class);
                LocalDate sd = resultSet.getObject("start_date", LocalDate.class);
                
                nhanvien nv = new nhanvien(resultSet.getInt("ID_employee"), resultSet.getString("name_employee"), dob, 
                        resultSet.getString("gender"),resultSet.getString("phonenumber"), resultSet.getString("address"), resultSet.getString("email"), 
                        resultSet.getString("part"), sd, resultSet.getString("role"), resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("avatar"));
                nhanvienList.add(nv);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nhanvienList;
    }
     
     public static nhanvien checkLogin(String user, String pass) {
        nhanvien nv = null;
        String sql = "SELECT * FROM nhanvien WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                nv = new nhanvien(
                    rs.getInt("ID_employee"), rs.getString("name_employee"),
                    rs.getObject("date_of_birth", LocalDate.class), rs.getString("gender"),
                    rs.getString("phonenumber"), rs.getString("address"),
                    rs.getString("email"), rs.getString("part"),
                    rs.getObject("start_date", LocalDate.class),
                     rs.getString("role"), rs.getString("username"), rs.getString("password"), rs.getString("avatar")
                );
            }
        } catch (Exception e) { e.printStackTrace(); }
        return nv;
    }
}
