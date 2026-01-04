package ProjectJava;



import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class QuanLyKhoView extends JPanel {
    private JTable tableSanPham;
    private DefaultTableModel tableModel;
    private JTextField txtMaSP, txtTenSP, txtSoLuong, txtDonVi, txtGia, txtGiaBan, txtTimKiem;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTim, btnChonAnh, btnTrangChu;
    private JLabel lblHinhAnh;
    private String duongDanAnhHienTai = "Drink1.jpg";

    private Main main;
    public QuanLyKhoView(Main main) {
        this.main = main;
        setLayout(new BorderLayout(10, 10));
        initUI();
        loadDataToTable();
        addEvents();
        
    }
    //Tạo GUI
    private void initUI() {
        //this.setLayout(new BorderLayout(10, 10));
        // === A. (NORTH): TÌM KIẾM & TRANG CHỦ ===
        JPanel topPanel = new JPanel(new BorderLayout());
        // Nút Trang chủ nằm góc trái
        btnTrangChu = new JButton("👈 Trang Chủ");
        btnTrangChu.setBackground(Color.decode("#FF9966")); // Xanh đậm
        btnTrangChu.setForeground(Color.BLACK);
        btnTrangChu.addActionListener(e -> main.showScreen("HOME"));
        JPanel homePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        homePanel.add(btnTrangChu);
        // Ô tìm kiếm nằm giữa
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.add(new JLabel("Tìm kiếm sản phẩm: "));
        txtTimKiem = new JTextField(30);
        btnTim = new JButton("Tìm Kiếm");
        btnTim.setBackground(Color.decode("#FF9966"));
        btnTim.setForeground(Color.BLACK);
        searchPanel.add(txtTimKiem);
        searchPanel.add(btnTim);

        topPanel.add(homePanel, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.CENTER);

        this.add(topPanel, BorderLayout.NORTH);
        // === B. (CENTER): BẢNG DỮ LIỆU ===
        String[] columns = {"Mã SP", "Tên Sản Phẩm", "Số Lượng", "Đơn Vị", "Giá Nhập", "Giá Bán", "Hình Ảnh"};
        tableModel = new DefaultTableModel(columns, 0);
        tableSanPham = new JTable(tableModel);
        tableSanPham.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableSanPham.setRowHeight(60);
        //  màu đỏ cảnh báo
        tableSanPham.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                try {
                    int sl = Integer.parseInt(value.toString());
                    if (sl < 10) {
                        c.setForeground(Color.RED);
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                    } else {
                        c.setForeground(Color.BLACK);
                    }
                } catch (Exception e) {}
                return c;
            }
        });
        // Render ảnh
        tableSanPham.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel();
                label.setHorizontalAlignment(JLabel.CENTER);
                if (isSelected) {
                    label.setOpaque(true);
                    label.setBackground(table.getSelectionBackground());
                }
                try {
                    String tenFile = (String) value;
                    if (tenFile != null && !tenFile.isEmpty()) {
                        File f = new File("images/" + tenFile);
                        if (f.exists()) {
                            ImageIcon icon = new ImageIcon(f.getAbsolutePath());
                            Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                            label.setIcon(new ImageIcon(img));
                        } else {
                            label.setText("No Image");
                        }
                    }
                } catch (Exception e) {}
                return label;
            }
        });
        //Thanh cuộn
        JScrollPane scrollPane = new JScrollPane(tableSanPham);
        this.add(scrollPane, BorderLayout.CENTER);

        // === C. (SOUTH): NHẬP LIỆU & NÚT ===
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        //Bên trái
        JPanel leftPanel = new JPanel(new GridLayout(0, 4, 15, 15));
        txtMaSP = new JTextField();
        txtTenSP = new JTextField();
        txtSoLuong = new JTextField();
        txtDonVi = new JTextField();
        txtGia = new JTextField();
        txtGiaBan = new JTextField();

        leftPanel.add(new JLabel("Mã Sản Phẩm:"));
        leftPanel.add(txtMaSP);
        leftPanel.add(new JLabel("Tên Sản Phẩm:"));
        leftPanel.add(txtTenSP);
        leftPanel.add(new JLabel("Số Lượng:"));
        leftPanel.add(txtSoLuong);
        leftPanel.add(new JLabel("Đơn Vị Tính:"));
        leftPanel.add(txtDonVi);
        leftPanel.add(new JLabel("Giá Nhập:"));
        leftPanel.add(txtGia);
        leftPanel.add(new JLabel("Giá Bán:"));
        leftPanel.add(txtGiaBan);
        txtGiaBan.setEditable(false);
        txtGiaBan.setBackground(new Color(230, 230, 230));
        //Bên phải
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Hình Ảnh SP"));
        lblHinhAnh = new JLabel();
        lblHinhAnh.setPreferredSize(new Dimension(150, 150));
        lblHinhAnh.setHorizontalAlignment(JLabel.CENTER);
        lblHinhAnh.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        btnChonAnh = new JButton("Chọn Ảnh");
        rightPanel.add(lblHinhAnh, BorderLayout.CENTER);
        rightPanel.add(btnChonAnh, BorderLayout.SOUTH);
        //Nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        Font fontButton = new Font("Segoe UI", Font.BOLD, 14);
        btnThem = new JButton("Thêm Mới");
        btnThem.setFont(fontButton);
        btnThem.setBackground(new Color(34, 139, 34));
        btnThem.setForeground(Color.BLACK);
        btnSua = new JButton("Cập Nhật");
        btnSua.setFont(fontButton);
        btnSua.setBackground(new Color(255, 165, 0));
        btnSua.setForeground(Color.BLACK);
        btnXoa = new JButton("Xóa");
        btnXoa.setFont(fontButton);
        btnXoa.setBackground(new Color(220, 20, 60));
        btnXoa.setForeground(Color.BLACK);
        btnLamMoi = new JButton("Làm Mới");
        btnLamMoi.setFont(fontButton);
        btnLamMoi.setBackground(new Color(70, 130, 180));
        btnLamMoi.setForeground(Color.BLACK);

        buttonPanel.add(btnThem); buttonPanel.add(btnSua); buttonPanel.add(btnXoa); buttonPanel.add(btnLamMoi);

        bottomPanel.add(leftPanel, BorderLayout.CENTER);
        bottomPanel.add(rightPanel, BorderLayout.EAST);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        this.add(bottomPanel, BorderLayout.SOUTH);

        setHinhAnh("Drink1.jpg");
    }
    // lấy dữ liệu
    public void loadDataToTable() {
        tableModel.setRowCount(0);
        SanPhamDAO dao = new SanPhamDAO();
        ArrayList<SanPham> list = dao.selectAll();
        for (SanPham sp : list) {
            tableModel.addRow(new Object[]{
                    sp.getMaSP(), sp.getTenSP(), sp.getSoLuong(), sp.getDonViTinh(),
                    sp.getGiaNhap(), sp.getGiaBan(), sp.getHinhAnh()
            });
        }
    }
    //Gửi cảnh báo ra màn hình
    private void checkTonKho() {
        StringBuilder sb = new StringBuilder();
        boolean alert = false;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            try {
                int sl = Integer.parseInt(tableModel.getValueAt(i, 2).toString());
                if (sl < 10) {
                    sb.append("- ").append(tableModel.getValueAt(i, 1)).append(" (Còn: ").append(sl).append(")\n");
                    alert = true;
                }
            } catch (Exception e) {}
        }
        if (alert) {
            JOptionPane.showMessageDialog(this, "⚠️ CẢNH BÁO SẮP HẾT HÀNG:\n" + sb.toString(), "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        }
    }
    // Xử lý event
    private void addEvents() {
        // Nút Trang Chủ (Để link với team)
        //btnTrangChu.addActionListener(e -> {
             // Đóng cửa sổ kho hiện tại
            //JOptionPane.showMessageDialog(this, "Đã quay về màn hình chính (Chờ team ghép code)!");
        
        tableSanPham.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableSanPham.getSelectedRow();
                if (row >= 0) {
                    txtMaSP.setText(tableModel.getValueAt(row, 0).toString());
                    txtTenSP.setText(tableModel.getValueAt(row, 1).toString());
                    txtSoLuong.setText(tableModel.getValueAt(row, 2).toString());
                    txtDonVi.setText(tableModel.getValueAt(row, 3).toString());
                    txtGia.setText(tableModel.getValueAt(row, 4).toString());
                    txtGiaBan.setText(tableModel.getValueAt(row, 5).toString());
                    setHinhAnh(tableModel.getValueAt(row, 6).toString());
                    txtMaSP.setEditable(false);
                }
            }
        });
        //Giá
        txtGia.addKeyListener(new java.awt.event.KeyAdapter(){
            public void keyReleased(java.awt.event.KeyEvent evt){
                try{
                    String text = txtGia.getText().trim();
                    if(!text.isEmpty()){
                        double giaNhap = Double.parseDouble(text);
                        txtGiaBan.setText(String.valueOf(Math.round(giaNhap * 1.1)));
                    }
                } catch (Exception e){}
            }
        });
        //Button thêm
        btnThem.addActionListener(e -> {
            String ma = txtMaSP.getText().trim();
            try {
                SanPhamDAO dao = new SanPhamDAO();
                if(dao.checkExist(ma)) {
                    // Hỏi nhập thêm
                    dao.nhapHang(ma, Integer.parseInt(txtSoLuong.getText()), Double.parseDouble(txtGia.getText()));
                } else {
                    // Thêm mới
                    SanPham sp = new SanPham(ma, txtTenSP.getText(), Integer.parseInt(txtSoLuong.getText()),
                            txtDonVi.getText(), Double.parseDouble(txtGia.getText()),
                            Double.parseDouble(txtGiaBan.getText()), duongDanAnhHienTai);
                    dao.insert(sp);
                }
                loadDataToTable();
                clearForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi nhập liệu: " + ex.getMessage());
            }
        });
        //Button sửa
        btnSua.addActionListener(e -> {
            try {
                new SanPhamDAO().capNhatThongTin(txtMaSP.getText(), Integer.parseInt(txtSoLuong.getText()),
                        Double.parseDouble(txtGia.getText()), Double.parseDouble(txtGiaBan.getText()), duongDanAnhHienTai);
                loadDataToTable();
            } catch (Exception ex) {}
        });
        //Button xóa
        btnXoa.addActionListener(e -> {
            if(JOptionPane.showConfirmDialog(this, "Xóa?") == JOptionPane.YES_OPTION) {
                new SanPhamDAO().delete(txtMaSP.getText());
                loadDataToTable();
                clearForm();
            }
        });
        //Button làm mới
        btnLamMoi.addActionListener(e -> { clearForm(); loadDataToTable(); });
        //Button tìm kiếm
        btnTim.addActionListener(e -> {
            ArrayList<SanPham> list = new SanPhamDAO().findByName(txtTimKiem.getText());
            tableModel.setRowCount(0);
            for (SanPham sp : list) tableModel.addRow(new Object[]{sp.getMaSP(), sp.getTenSP(), sp.getSoLuong(), sp.getDonViTinh(), sp.getGiaNhap(), sp.getGiaBan(), sp.getHinhAnh()});
        });
        //Button chọn ảnh
        // Button chọn ảnh
// Button chọn ảnh
        btnChonAnh.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    java.io.File src = fileChooser.getSelectedFile();
                    String name = src.getName();
                    
                    // --- 1. HIỆN ẢNH XEM TRƯỚC NGAY LẬP TỨC (PREVIEW) ---
                    // Lấy ảnh trực tiếp từ đường dẫn file gốc để hiển thị luôn
                    ImageIcon icon = new ImageIcon(src.getAbsolutePath());
                    Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                    lblHinhAnh.setIcon(new ImageIcon(img));
                    lblHinhAnh.setText(""); // Xóa chữ "No Image" nếu có
                    
                    // Cập nhật tên file vào biến toàn cục để tí nữa bấm Lưu nó biết tên gì
                    this.duongDanAnhHienTai = name; 

                    // --- 2. COPY FILE VÀO THƯ MỤC DỰ ÁN (XỬ LÝ NGẦM) ---
                    java.io.File folder = new java.io.File("images");
                    if (!folder.exists()) {
                        folder.mkdirs(); 
                    }

                    java.io.File dst = new java.io.File("images/" + name);

                    // Dùng luồng Stream để copy (An toàn với OneDrive/Windows)
                    try (java.io.InputStream in = new java.io.FileInputStream(src);
                        java.io.OutputStream out = new java.io.FileOutputStream(dst)) {
                        
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = in.read(buffer)) > 0) {
                            out.write(buffer, 0, length);
                        }
                    }
                    
                    // Không cần gọi setHinhAnh(name) ở đây nữa vì đã set icon ở bước 1 rồi
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi khi chọn ảnh: " + ex.getMessage());
                }
            }
        });
    }
    //Hàm làm mới
    private void clearForm() {
        txtMaSP.setText(""); txtMaSP.setEditable(true);
        txtTenSP.setText(""); txtSoLuong.setText("");
        txtDonVi.setText(""); txtGia.setText(""); txtGiaBan.setText("");
        setHinhAnh("Drink1.jpg");
    }
    //Hàm lấy ảnh
    private void setHinhAnh(String tenFile) {
        this.duongDanAnhHienTai = tenFile;
        try {
            ImageIcon icon = new ImageIcon("images/" + tenFile);
            if (icon.getImageLoadStatus() == MediaTracker.COMPLETE){
                lblHinhAnh.setText("");
                return;
            }
            Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            lblHinhAnh.setIcon(new ImageIcon(img));
            lblHinhAnh.setText("");
        } catch (Exception e) { lblHinhAnh.setIcon(null); }
    }
}