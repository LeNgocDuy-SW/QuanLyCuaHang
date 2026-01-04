package ProjectJava;
import java.sql.*;

public class DBConnection {
    // Cấu hình chuỗi kết nối
    private static final String URL =
        "jdbc:sqlserver://localhost:1433;"
        + "databaseName=QLKH;"     
        + "user=sa;"               
        + "password=123456;"        
        + "encrypt=true;"
        + "trustServerCertificate=true;";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}