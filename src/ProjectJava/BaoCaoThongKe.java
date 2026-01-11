package ProjectJava;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class BaoCaoThongKe extends JPanel {
    private Main main;
    
    // Tab 1: Doanh thu
    private JComboBox<String> cbFromDay, cbFromMonth, cbFromYear;
    private JComboBox<String> cbToDay, cbToMonth, cbToYear;
    private JTable jTable1;
    private DefaultTableModel modelDoanhThu;
    private JTextField txtTongDoanhThu;

    // Tab 2: Sản phẩm bán chạy (Thay thế cho Chi phí)
    private JComboBox<String> cbFromDay1, cbFromMonth1, cbFromYear1;
    private JComboBox<String> cbToDay1, cbToMonth1, cbToYear1;
    private JTable jTable2;
    private DefaultTableModel modelSanPham;
    private JTextField txtTongSoLuong;

    public BaoCaoThongKe(Main main) {
        this.main = main;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // --- 1. THANH TIÊU ĐỀ ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.decode("#FF9966"));
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnBack = new JButton("👈 Trang Chủ");
        btnBack.setBackground(Color.decode("#FF9966"));
        btnBack.setForeground(Color.BLACK);
        btnBack.setFont(new Font("Arial", Font.BOLD, 12));
        btnBack.setFocusPainted(false);
        btnBack.addActionListener(e -> main.showScreen("HOME"));

        JLabel lblTitle = new JLabel("HỆ THỐNG BÁO CÁO THỐNG KÊ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);

        topBar.add(btnBack, BorderLayout.WEST);
        topBar.add(lblTitle, BorderLayout.CENTER);
        add(topBar, BorderLayout.NORTH);

        // --- 2. TABBED PANE ---
        JTabbedPane tabBaoCao = new JTabbedPane();
        tabBaoCao.setFont(new Font("Arial", Font.BOLD, 14));
        
        tabBaoCao.addTab("Thống kê doanh thu", createDoanhThuTab());
        tabBaoCao.addTab("Top Sản phẩm bán chạy", createSanPhamTab());

        add(tabBaoCao, BorderLayout.CENTER);
        
        // Khởi tạo dữ liệu ComboBox
        initComboBox();
        if(Session.role == null || !"admin".equalsIgnoreCase(Session.role.trim())){
            renderAccessDenied();
            return;
        }
    }

    private void initComboBox() {
        cbFromDay.removeAllItems(); cbToDay.removeAllItems();
        cbFromMonth.removeAllItems(); cbToMonth.removeAllItems();
        cbFromYear.removeAllItems(); cbToYear.removeAllItems();
        
        cbFromDay1.removeAllItems(); cbToDay1.removeAllItems();
        cbFromMonth1.removeAllItems(); cbToMonth1.removeAllItems();
        cbFromYear1.removeAllItems(); cbToYear1.removeAllItems();

        for (int i = 1; i <= 31; i++) {
            String val = String.valueOf(i);
            cbFromDay.addItem(val); cbToDay.addItem(val);
            cbFromDay1.addItem(val); cbToDay1.addItem(val);
        }
        for (int i = 1; i <= 12; i++) {
            String val = String.valueOf(i);
            cbFromMonth.addItem(val); cbToMonth.addItem(val);
            cbFromMonth1.addItem(val); cbToMonth1.addItem(val);
        }
        int currentYear = LocalDate.now().getYear();
        for (int i = 2023; i <= currentYear; i++) {
            String val = String.valueOf(i);
            cbFromYear.addItem(val); cbToYear.addItem(val);
            cbFromYear1.addItem(val); cbToYear1.addItem(val);
        }
        
        // Mặc định chọn ngày hiện tại
        String d = String.valueOf(LocalDate.now().getDayOfMonth());
        String m = String.valueOf(LocalDate.now().getMonthValue());
        String y = String.valueOf(LocalDate.now().getYear());
        
        cbToDay.setSelectedItem(d); cbToMonth.setSelectedItem(m); cbToYear.setSelectedItem(y);
        cbToDay1.setSelectedItem(d); cbToMonth1.setSelectedItem(m); cbToYear1.setSelectedItem(y);
    }

    // ================== TAB 1: DOANH THU ==================
    private JPanel createDoanhThuTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);

        // Filter Panel
        JPanel filter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filter.setBackground(Color.WHITE);
        
        cbFromDay = new JComboBox<>(); cbFromMonth = new JComboBox<>(); cbFromYear = new JComboBox<>();
        cbToDay = new JComboBox<>(); cbToMonth = new JComboBox<>(); cbToYear = new JComboBox<>();
        
        JButton btnXem = new JButton("Xem thống kê");
        btnXem.setBackground(Color.decode("#00994C"));
        btnXem.setForeground(Color.WHITE);
        btnXem.setFont(new Font("Arial", Font.BOLD, 12));
        btnXem.addActionListener(e -> loadDoanhThu());

        filter.add(new JLabel("Từ ngày: ")); filter.add(cbFromDay); filter.add(cbFromMonth); filter.add(cbFromYear);
        filter.add(new JLabel(" Đến ngày: ")); filter.add(cbToDay); filter.add(cbToMonth); filter.add(cbToYear);
        filter.add(btnXem);

        // Table
        String[] cols = {"STT", "Ngày", "Số hóa đơn", "Doanh thu (VNĐ)"};
        modelDoanhThu = new DefaultTableModel(cols, 0);
        jTable1 = new JTable(modelDoanhThu);
        jTable1.setRowHeight(25);
        jTable1.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(Color.WHITE);
        txtTongDoanhThu = new JTextField(15);
        txtTongDoanhThu.setEditable(false);
        txtTongDoanhThu.setFont(new Font("Arial", Font.BOLD, 14));
        txtTongDoanhThu.setForeground(Color.RED);
        
        footer.add(new JLabel("Tổng doanh thu: "));
        footer.add(txtTongDoanhThu);

        panel.add(filter, BorderLayout.NORTH);
        panel.add(new JScrollPane(jTable1), BorderLayout.CENTER);
        panel.add(footer, BorderLayout.SOUTH);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return panel;
    }

    // ================== TAB 2: SẢN PHẨM BÁN CHẠY (MỚI) ==================
    private JPanel createSanPhamTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);

        // Filter Panel
        JPanel filter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filter.setBackground(Color.WHITE);
        
        cbFromDay1 = new JComboBox<>(); cbFromMonth1 = new JComboBox<>(); cbFromYear1 = new JComboBox<>();
        cbToDay1 = new JComboBox<>(); cbToMonth1 = new JComboBox<>(); cbToYear1 = new JComboBox<>();
        
        JButton btnXem = new JButton("Xem Top Bán Chạy");
        btnXem.setBackground(Color.decode("#00994C"));
        btnXem.setForeground(Color.WHITE);
        btnXem.setFont(new Font("Arial", Font.BOLD, 12));
        btnXem.addActionListener(e -> loadSanPhamBanChay());

        filter.add(new JLabel("Từ ngày: ")); filter.add(cbFromDay1); filter.add(cbFromMonth1); filter.add(cbFromYear1);
        filter.add(new JLabel(" Đến ngày: ")); filter.add(cbToDay1); filter.add(cbToMonth1); filter.add(cbToYear1);
        filter.add(btnXem);

        // Table
        String[] cols = {"Hạng", "Mã SP", "Tên Sản Phẩm", "Số lượng bán", "Doanh thu đem lại"};
        modelSanPham = new DefaultTableModel(cols, 0);
        jTable2 = new JTable(modelSanPham);
        jTable2.setRowHeight(25);
        jTable2.setFont(new Font("Arial", Font.PLAIN, 14));
        jTable2.getColumnModel().getColumn(0).setPreferredWidth(50);
        jTable2.getColumnModel().getColumn(2).setPreferredWidth(200);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(Color.WHITE);
        txtTongSoLuong = new JTextField(15);
        txtTongSoLuong.setEditable(false);
        txtTongSoLuong.setFont(new Font("Arial", Font.BOLD, 14));
        txtTongSoLuong.setForeground(Color.BLUE);
        
        footer.add(new JLabel("Tổng số lượng bán ra: "));
        footer.add(txtTongSoLuong);

        panel.add(filter, BorderLayout.NORTH);
        panel.add(new JScrollPane(jTable2), BorderLayout.CENTER);
        panel.add(footer, BorderLayout.SOUTH);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return panel;
    }
    private void renderAccessDenied() {
        JPanel deniedPanel = new JPanel(new GridBagLayout());
        deniedPanel.setBackground(Color.white);
        JLabel lblMessage = new JLabel("BẠN KHÔNG CÓ QUYỀN TRUY CẬP TRANG NÀY!");
        lblMessage.setFont(new Font("Arial", Font.BOLD, 20));
        lblMessage.setForeground(Color.RED);
        deniedPanel.add(lblMessage);
        add(deniedPanel, BorderLayout.CENTER);
    }


    // ================== LOGIC TẢI DỮ LIỆU TỪ SQL ==================

    private void loadDoanhThu() {
        modelDoanhThu.setRowCount(0);
        long tongDoanhThu = 0;

        String sql = "SELECT CAST(ngaytao AS DATE) AS ngay, COUNT(*) AS so_hoa_don, SUM(tongtien) AS doanh_thu " +
                     "FROM HoaDon " +
                     "WHERE ngaytao BETWEEN ? AND ? " +
                     "GROUP BY CAST(ngaytao AS DATE) " +
                     "ORDER BY ngay";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            LocalDate from = LocalDate.of(
                Integer.parseInt(cbFromYear.getSelectedItem().toString()),
                Integer.parseInt(cbFromMonth.getSelectedItem().toString()),
                Integer.parseInt(cbFromDay.getSelectedItem().toString())
            );
            LocalDate to = LocalDate.of(
                Integer.parseInt(cbToYear.getSelectedItem().toString()),
                Integer.parseInt(cbToMonth.getSelectedItem().toString()),
                Integer.parseInt(cbToDay.getSelectedItem().toString())
            );

            ps.setDate(1, java.sql.Date.valueOf(from));
            ps.setDate(2, java.sql.Date.valueOf(to.plusDays(1))); // Cộng 1 ngày để lấy trọn ngày cuối

            ResultSet rs = ps.executeQuery();
            int stt = 1;
            while (rs.next()) {
                long doanhThu = rs.getLong("doanh_thu");
                tongDoanhThu += doanhThu;
                modelDoanhThu.addRow(new Object[]{
                    stt++,
                    rs.getDate("ngay").toString(),
                    rs.getInt("so_hoa_don"),
                    String.format("%,d VNĐ", doanhThu)
                });
            }
            txtTongDoanhThu.setText(String.format("%,d VNĐ", tongDoanhThu));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải doanh thu: " + e.getMessage());
        }
    }

    private void loadSanPhamBanChay() {
        modelSanPham.setRowCount(0);
        long tongSoLuong = 0;

        // SQL JOIN 3 bảng để lấy sản phẩm bán chạy trong khoảng thời gian
        // Thêm điều kiện HAVING SUM(ct.soluong) > 5
        String sql = "SELECT sp.ma_sp, sp.ten_sp, " +
                     "SUM(ct.soluong) AS tong_soluong, " +
                     "SUM(ct.soluong * ct.dongia) AS tong_tien " +
                     "FROM ChiTietHoaDon ct " +
                     "JOIN HoaDon hd ON ct.mahd = hd.mahd " +
                     "JOIN san_pham sp ON ct.masp = sp.ma_sp " +
                     "WHERE hd.ngaytao BETWEEN ? AND ? " +
                     "GROUP BY sp.ma_sp, sp.ten_sp " +
                     "HAVING SUM(ct.soluong) > 5 " + // Điều kiện mới: bán trên 5 sản phẩm
                     "ORDER BY tong_soluong DESC"; // Sắp xếp giảm dần theo số lượng bán

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            LocalDate from = LocalDate.of(
                Integer.parseInt(cbFromYear1.getSelectedItem().toString()),
                Integer.parseInt(cbFromMonth1.getSelectedItem().toString()),
                Integer.parseInt(cbFromDay1.getSelectedItem().toString())
            );
            LocalDate to = LocalDate.of(
                Integer.parseInt(cbToYear1.getSelectedItem().toString()),
                Integer.parseInt(cbToMonth1.getSelectedItem().toString()),
                Integer.parseInt(cbToDay1.getSelectedItem().toString())
            );

            ps.setDate(1, java.sql.Date.valueOf(from));
            ps.setDate(2, java.sql.Date.valueOf(to.plusDays(1)));

            ResultSet rs = ps.executeQuery();
            int stt = 1;
            while (rs.next()) {
                int sl = rs.getInt("tong_soluong");
                double tien = rs.getDouble("tong_tien");
                
                tongSoLuong += sl;
                
                modelSanPham.addRow(new Object[]{
                    stt++, // Hạng
                    rs.getString("ma_sp"),
                    rs.getString("ten_sp"),
                    sl,
                    String.format("%,.0f VNĐ", tien)
                });
            }
            txtTongSoLuong.setText(tongSoLuong + " sản phẩm");
            
            if (stt == 1) {
                JOptionPane.showMessageDialog(this, "Không có dữ liệu bán hàng trong khoảng thời gian này (hoặc sản phẩm bán < 6)!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi truy vấn sản phẩm bán chạy: " + e.getMessage());
        }
    }
}