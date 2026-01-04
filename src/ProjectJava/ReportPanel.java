package ProjectJava;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

public class ReportPanel extends JPanel {
    private Main main;
    private JComboBox<String> cboNgay, cboThang, cboNam;
    private JTable tblDoanhThu, tblTopSanPham;
    private DefaultTableModel modelDoanhThu, modelTopSP;
    private JLabel lbTongDoanhThu;
    private ChartPanel chartPanel; 

    public ReportPanel(Main main) {
        this.main = main;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // --- 1. THANH TIÊU ĐỀ & ĐIỀU HƯỚNG ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.LIGHT_GRAY);
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnBack = new JButton("👈 Trang Chủ");
        btnBack.setBackground(Color.decode("#FF9966"));
      
        btnBack.setForeground(Color.black);
        //btnBack.setFont(new Font("Arial", Font.BOLD, 12));
        btnBack.setFocusPainted(false);
        btnBack.addActionListener(e -> main.showScreen("HOME"));

        JLabel lblTitle = new JLabel("BÁO CÁO & THỐNG KÊ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.black);

        topBar.add(btnBack, BorderLayout.WEST);
        topBar.add(lblTitle, BorderLayout.CENTER);
        add(topBar, BorderLayout.NORTH);

        if(Session.role == null || !"admin".equalsIgnoreCase(Session.role.trim())){
            renderAccessDenied();
            return;
        }

        // --- 2. NỘI DUNG CHÍNH (TAB) ---
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        tabbedPane.addTab("Thống Kê Doanh Thu", createTabDoanhThu());
        tabbedPane.addTab("Top Sản Phẩm Bán Chạy", createTabTopSanPham());

        add(tabbedPane, BorderLayout.CENTER);
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

    // ================== TAB 1: DOANH THU ==================
    private JPanel createTabDoanhThu() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        // -- Bộ lọc --
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(Color.WHITE);
        
        cboNam = new JComboBox<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i >= currentYear - 5; i--) cboNam.addItem(String.valueOf(i));

        cboThang = new JComboBox<>();
        cboThang.addItem("Cả năm");
        for (int i = 1; i <= 12; i++) cboThang.addItem("Tháng " + i);

        cboNgay = new JComboBox<>();
        cboNgay.addItem("Cả tháng");
        // Mặc định ẩn hoặc để trống, sẽ update khi chọn tháng
        
        // Sự kiện thay đổi tháng/năm để cập nhật số ngày
        cboThang.addActionListener(e -> updateDays());
        cboNam.addActionListener(e -> updateDays());

        JButton btnLoc = new JButton("Xem Báo Cáo");
        btnLoc.setBackground(Color.decode("#00994C"));
        btnLoc.setForeground(Color.WHITE);
        btnLoc.setFont(new Font("Arial", Font.BOLD, 13));
        btnLoc.addActionListener(e -> loadDataDoanhThu());

        filterPanel.add(new JLabel("Năm: "));
        filterPanel.add(cboNam);
        filterPanel.add(new JLabel(" Tháng: "));
        filterPanel.add(cboThang);
        filterPanel.add(new JLabel(" Ngày: "));
        filterPanel.add(cboNgay);
        filterPanel.add(btnLoc);

        // -- Bảng số liệu --
        String[] cols = {"Thời gian", "Số Hóa Đơn", "Doanh Thu (VNĐ)"};
        modelDoanhThu = new DefaultTableModel(cols, 0);
        tblDoanhThu = new JTable(modelDoanhThu);
        tblDoanhThu.setRowHeight(25);
        JScrollPane scrollTable = new JScrollPane(tblDoanhThu);
        scrollTable.setPreferredSize(new Dimension(350, 0));

        // -- Biểu đồ --
        chartPanel = new ChartPanel();
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createTitledBorder("Biểu đồ tăng trưởng"));

        lbTongDoanhThu = new JLabel("Tổng doanh thu: 0 VNĐ");
        lbTongDoanhThu.setFont(new Font("Arial", Font.BOLD, 20));
        lbTongDoanhThu.setForeground(Color.BLUE);
        lbTongDoanhThu.setHorizontalAlignment(SwingConstants.RIGHT);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollTable, chartPanel);
        splitPane.setResizeWeight(0.35);
        
        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(splitPane, BorderLayout.CENTER);
        panel.add(lbTongDoanhThu, BorderLayout.SOUTH);

        return panel;
    }

    private void updateDays() {
        int month = cboThang.getSelectedIndex();
        if (month == 0) {
            cboNgay.setEnabled(false);
            cboNgay.setSelectedIndex(0);
            return;
        }
        cboNgay.setEnabled(true);
        int year = Integer.parseInt(cboNam.getSelectedItem().toString());
        
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        int maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        Object selected = cboNgay.getSelectedItem();
        cboNgay.removeAllItems();
        cboNgay.addItem("Cả tháng");
        for (int i = 1; i <= maxDays; i++) cboNgay.addItem("Ngày " + i);
        
        if (selected != null) cboNgay.setSelectedItem(selected);
    }

    private void loadDataDoanhThu() {
        modelDoanhThu.setRowCount(0);
        int year = Integer.parseInt(cboNam.getSelectedItem().toString());
        int month = cboThang.getSelectedIndex(); // 0: Cả năm, 1-12: Tháng
        int day = cboNgay.getSelectedIndex();    // 0: Cả tháng, 1-31: Ngày cụ thể
        
        ArrayList<Integer> chartValues = new ArrayList<>();
        ArrayList<String> chartLabels = new ArrayList<>();
        double tongDoanhThuTotal = 0;

        try (Connection con = DBConnection.getConnection()) {
            String sql = "";
            String timePrefix = "";
            
            if (month == 0) {
                // Thống kê theo Tháng trong năm
                sql = "SELECT MONTH(ngaytao) as ThoiGian, COUNT(*) as SoHD, SUM(tongtien) as TongTien " +
                      "FROM HoaDon WHERE YEAR(ngaytao) = ? " +
                      "GROUP BY MONTH(ngaytao) ORDER BY MONTH(ngaytao)";
                timePrefix = "Tháng ";
            } else if (day == 0) {
                // Thống kê theo Ngày trong tháng
                sql = "SELECT DAY(ngaytao) as ThoiGian, COUNT(*) as SoHD, SUM(tongtien) as TongTien " +
                      "FROM HoaDon WHERE YEAR(ngaytao) = ? AND MONTH(ngaytao) = ? " +
                      "GROUP BY DAY(ngaytao) ORDER BY DAY(ngaytao)";
                timePrefix = "Ngày ";
            } else {
                // Thống kê theo GIỜ trong ngày
                sql = "SELECT DATEPART(HOUR, ngaytao) as ThoiGian, COUNT(*) as SoHD, SUM(tongtien) as TongTien " +
                      "FROM HoaDon WHERE YEAR(ngaytao) = ? AND MONTH(ngaytao) = ? AND DAY(ngaytao) = ? " +
                      "GROUP BY DATEPART(HOUR, ngaytao) ORDER BY DATEPART(HOUR, ngaytao)";
                timePrefix = "Giờ ";
            }

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, year);
            if (month > 0) ps.setInt(2, month);
            if (day > 0) ps.setInt(3, day);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int tg = rs.getInt("ThoiGian");
                int soHD = rs.getInt("SoHD");
                double tien = rs.getDouble("TongTien");
                
                String label = timePrefix + tg + (timePrefix.equals("Giờ ") ? "h" : "");
                modelDoanhThu.addRow(new Object[]{label, soHD, String.format("%,.0f", tien)});
                
                chartLabels.add(String.valueOf(tg));
                chartValues.add((int) tien);
                tongDoanhThuTotal += tien;
            }
            
            lbTongDoanhThu.setText("Tổng cộng: " + String.format("%,.0f VNĐ", tongDoanhThuTotal));
            chartPanel.updateChart(chartValues, chartLabels);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi truy vấn dữ liệu báo cáo!");
        }
    }

    // ================== TAB 2: TOP SP ==================
    private JPanel createTabTopSanPham() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        String[] cols = {"Mã SP", "Tên Sản Phẩm", "Số Lượng Bán", "Doanh Thu"};
        modelTopSP = new DefaultTableModel(cols, 0);
        tblTopSanPham = new JTable(modelTopSP);
        
        loadDataTopSanPham();

        JButton btnRefresh = new JButton("Làm mới danh sách");
        btnRefresh.addActionListener(e -> loadDataTopSanPham());

        panel.add(new JScrollPane(tblTopSanPham), BorderLayout.CENTER);
        panel.add(btnRefresh, BorderLayout.SOUTH);
        return panel;
    }

    private void loadDataTopSanPham() {
        modelTopSP.setRowCount(0);
        // Cập nhật điều kiện HAVING để lọc các sản phẩm bán trên 10 cái
        String sql = "SELECT sp.ma_sp, sp.ten_sp, SUM(ct.soluong) as DaBan, SUM(ct.soluong * ct.dongia) as TongTien " +
                     "FROM ChiTietHoaDon ct " +
                     "JOIN san_pham sp ON ct.masp = sp.ma_sp " +
                     "GROUP BY sp.ma_sp, sp.ten_sp " +
                     "HAVING SUM(ct.soluong) > 10 " + 
                     "ORDER BY DaBan DESC"; 

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                modelTopSP.addRow(new Object[]{
                    rs.getString("ma_sp"), rs.getString("ten_sp"),
                    rs.getInt("DaBan"), String.format("%,.0f VNĐ", rs.getDouble("TongTien"))
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // ================== BIỂU ĐỒ TỰ VẼ ==================
    class ChartPanel extends JPanel {
        private ArrayList<Integer> values = new ArrayList<>();
        private ArrayList<String> labels = new ArrayList<>();

        public void updateChart(ArrayList<Integer> v, ArrayList<String> l) {
            this.values = v; this.labels = l;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (values.isEmpty()) {
                g.drawString("Không có dữ liệu cho thời gian này.", 20, 20);
                return;
            }

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int margin = 40;
            int chartW = w - 2 * margin;
            int chartH = h - 2 * margin;

            int maxVal = 1;
            for (int v : values) if (v > maxVal) maxVal = v;

            int barW = Math.max(10, chartW / values.size() / 2);
            int step = chartW / values.size();

            for (int i = 0; i < values.size(); i++) {
                int val = values.get(i);
                int barH = (int) ((double) val / maxVal * (chartH - 20));
                int x = margin + i * step;
                int y = h - margin - barH;

                g2.setColor(new Color(70, 130, 180));
                g2.fillRect(x, y, barW, barH);
                g2.setColor(Color.DARK_GRAY);
                g2.drawRect(x, y, barW, barH);

                g2.setFont(new Font("Arial", Font.PLAIN, 10));
                g2.drawString(labels.get(i), x, h - margin + 15);
                if(val > 0) g2.drawString(val/1000 + "k", x, y - 5);
            }
            
            g2.setColor(Color.BLACK);
            g2.drawLine(margin, h - margin, w - margin, h - margin); // Trục X
        }
    }
}