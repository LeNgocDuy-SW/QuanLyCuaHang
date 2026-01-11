package ProjectJava;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FrmKhachHang extends JPanel {

    private Main main;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmKhachHang.class.getName());

    public FrmKhachHang(Main main) {
        this.main = main;

        this.setLayout(new BorderLayout());
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.setBackground(Color.white);
        JButton btBack = new JButton("👈 Trang Chủ");
        btBack.setBackground(Color.decode("#FF9966"));
        btBack.setFocusPainted(false);
        btBack.addActionListener(e -> main.showScreen("HOME"));

        topBar.add(btBack);
        this.add(topBar, BorderLayout.NORTH);
        initComponents();

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                loadTable();
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        JPanel pnCenter = new JPanel();
        pnCenter.setBackground(Color.white);

        pnButton = new javax.swing.JPanel();
        // btnThem đã bị xóa theo yêu cầu
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnTracuu = new javax.swing.JButton();
        btnMoi = new javax.swing.JButton();
        lblQLTTKH = new javax.swing.JLabel();
        pnNhap = new javax.swing.JPanel();
        lblMaKH = new javax.swing.JLabel();
        lblHoTen = new javax.swing.JLabel();
        // lblDiem đã bị xóa
        lblSDT = new javax.swing.JLabel();
        lblDiaChi = new javax.swing.JLabel();
        txtMaKH = new javax.swing.JTextField();
        txtHoTen = new javax.swing.JTextField();
        txtSDT = new javax.swing.JTextField();
        txtDiaChi = new javax.swing.JTextField();
        // txtDiem đã bị xóa
        lblPhanLoai = new javax.swing.JLabel();
        cboLoaiKH = new javax.swing.JComboBox<>();
        jScrollPane = new javax.swing.JScrollPane();
        tblKhachHang = new javax.swing.JTable();

        // --- CẤU HÌNH CÁC NÚT BẤM ---
        
        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnXoa.setText("Xóa");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnTracuu.setText("Tra cứu");
        btnTracuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTracuuActionPerformed(evt);
            }
        });

        btnMoi.setText("Làm mới");
        btnMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiActionPerformed(evt);
            }
        });

        // --- LAYOUT PANEL BUTTON ---
        javax.swing.GroupLayout pnButtonLayout = new javax.swing.GroupLayout(pnButton);
        pnButton.setLayout(pnButtonLayout);
        pnButtonLayout.setHorizontalGroup(
            pnButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnButtonLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnTracuu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnMoi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSua, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    // btnThem đã xóa
                    .addComponent(btnXoa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnButtonLayout.setVerticalGroup(
            pnButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnButtonLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                // btnThem đã xóa
                .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnTracuu, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        lblQLTTKH.setFont(new Font("Arial", Font.BOLD, 18));
        lblQLTTKH.setText("QUẢN LÝ THÔNG TIN KHÁCH HÀNG");

        lblMaKH.setText("Mã KH");
        lblHoTen.setText("Họ tên KH");
        // lblDiem đã xóa
        lblSDT.setText("Số điện thoại");
        lblDiaChi.setText("Địa chỉ");

        txtMaKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaKHActionPerformed(evt);
            }
        });

        lblPhanLoai.setText("Loại KH");

        cboLoaiKH.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Thường", "Bạc", "Vàng", "Kim cương" }));
        cboLoaiKH.setEnabled(false); // Loại KH tự động tính, không cho sửa tay

        // --- LAYOUT PANEL NHẬP LIỆU ---
        javax.swing.GroupLayout pnNhapLayout = new javax.swing.GroupLayout(pnNhap);
        pnNhap.setLayout(pnNhapLayout);
        pnNhapLayout.setHorizontalGroup(
            pnNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnNhapLayout.createSequentialGroup()
                .addGroup(pnNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lblPhanLoai, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblSDT, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                    .addComponent(lblHoTen, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblMaKH, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblDiaChi, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(pnNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMaKH)
                    .addComponent(txtSDT)
                    .addComponent(txtHoTen)
                    .addComponent(txtDiaChi)
                    // txtDiem đã xóa
                    .addComponent(cboLoaiKH, 0, 302, Short.MAX_VALUE)))
        );
        pnNhapLayout.setVerticalGroup(
            pnNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnNhapLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(pnNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMaKH)
                    .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHoTen)
                    .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSDT)
                    .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDiaChi)
                    .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                // txtDiem đã xóa
                .addGroup(pnNhapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPhanLoai)
                    .addComponent(cboLoaiKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblKhachHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "Mã KH", "Họ tên KH", "Số điện thoại", "Địa chỉ", "Điểm tích lũy", "Loại KH"
            }
        ));
        tblKhachHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblKhachHangMouseClicked(evt);
            }
        });
        jScrollPane.setViewportView(tblKhachHang);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(pnCenter);
        pnCenter.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(276, 276, 276)
                                .addComponent(lblQLTTKH))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(100, 100, 100)
                                .addComponent(pnNhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(60, 60, 60)
                                .addComponent(pnButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 94, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(lblQLTTKH)
                .addGap(35, 35, 35)
                .addComponent(jScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnNhap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        this.add(pnCenter, BorderLayout.CENTER);

    } // </editor-fold>

    private boolean kiemTraSDT(String sdt) {
        return sdt.matches("^0\\d{9}$");
    }

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {
        if (txtHoTen.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Họ tên không được để trống");
            return;
        }
        if (!kiemTraSDT(txtSDT.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ");
            return;
        }
        // Đã xóa kiểm tra txtDiem vì trường này không còn tồn tại

        try {
            // Câu lệnh Update không còn cập nhật diemtichluy nữa
            String sql = "UPDATE KhachHang SET tenkh=?, sdt=?, diachi=? WHERE makh=?";
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, txtHoTen.getText());
            ps.setString(2, txtSDT.getText());
            ps.setString(3, txtDiaChi.getText());
            // ps.setInt(4, Integer.parseInt(txtDiem.getText())); -> Xóa dòng này
            ps.setInt(4, Integer.parseInt(txtMaKH.getText())); // Index đổi thành 4

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Sửa thành công (Giữ nguyên điểm tích lũy)");
            loadTable();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi sửa dữ liệu");
            e.printStackTrace();
        }
    }

    private void btnTracuuActionPerformed(java.awt.event.ActionEvent evt) {
        if (txtMaKH.getText().trim().isEmpty() &&
            txtHoTen.getText().trim().isEmpty() &&
            txtSDT.getText().trim().isEmpty() &&
            txtDiaChi.getText().trim().isEmpty()
            // && txtDiem.getText().trim().isEmpty() -> Xóa điều kiện này
        ) {
            JOptionPane.showMessageDialog(this, "Nhập ít nhất 1 thông tin để tra cứu");
            return;
        }

        try {
            DefaultTableModel model = (DefaultTableModel) tblKhachHang.getModel();
            model.setRowCount(0);

            StringBuilder sql = new StringBuilder("SELECT * FROM KhachHang WHERE 1=1");
            java.util.List<Object> params = new java.util.ArrayList<>();

            if (!txtMaKH.getText().trim().isEmpty()) {
                sql.append(" AND makh = ?");
                params.add(Integer.parseInt(txtMaKH.getText().trim()));
            }
            if (!txtHoTen.getText().trim().isEmpty()) {
                sql.append(" AND tenkh LIKE ?");
                params.add("%" + txtHoTen.getText().trim() + "%");
            }
            if (!txtSDT.getText().trim().isEmpty()) {
                sql.append(" AND sdt LIKE ?");
                params.add("%" + txtSDT.getText().trim() + "%");
            }
            if (!txtDiaChi.getText().trim().isEmpty()) {
                sql.append(" AND diachi LIKE ?");
                params.add("%" + txtDiaChi.getText().trim() + "%");
            }
            // Đã xóa logic tìm theo điểm tích lũy

            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql.toString());

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int diem = rs.getInt("diemtichluy");
                String loai = phanLoaiKhachHang(diem);

                model.addRow(new Object[]{
                    rs.getInt("Makh"),
                    rs.getString("tenkh"),
                    rs.getString("sdt"),
                    rs.getString("diachi"),
                    rs.getInt("diemtichluy"),
                    loai
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tra cứu");
            e.printStackTrace();
        }
    }

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {
        int chon = JOptionPane.showConfirmDialog(
            this, "Xóa khách hàng này?", "Xác nhận",
            JOptionPane.YES_NO_OPTION
        );

        if (chon == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM KhachHang WHERE makh=?";
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(txtMaKH.getText()));

                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Xóa thành công");
                loadTable();
                btnMoiActionPerformed(null);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi xóa");
            }
        }
    }

    private void txtHoTenActionPerformed(java.awt.event.ActionEvent evt) {}
    private void txtMaKHActionPerformed(java.awt.event.ActionEvent evt) {}
    private void cboLoaiKHActionPerformed(java.awt.event.ActionEvent evt) {}

    private void btnMoiActionPerformed(java.awt.event.ActionEvent evt) {
        txtMaKH.setText("");
        txtHoTen.setText("");
        txtSDT.setText("");
        txtDiaChi.setText("");
        // txtDiem.setText(""); -> Xóa
        cboLoaiKH.setSelectedIndex(0);

        txtMaKH.setEnabled(true);
        tblKhachHang.clearSelection();
        loadTable(); 
    }

    private void formWindowOpened(java.awt.event.WindowEvent evt) {
        txtMaKH.setEnabled(false);
        cboLoaiKH.setEnabled(false);
        loadTable();
    }

    private void tblKhachHangMouseClicked(java.awt.event.MouseEvent evt) {
        int row = tblKhachHang.getSelectedRow();

        txtMaKH.setText(tblKhachHang.getValueAt(row, 0).toString());
        txtHoTen.setText(tblKhachHang.getValueAt(row, 1).toString());
        txtSDT.setText(tblKhachHang.getValueAt(row, 2).toString());
        txtDiaChi.setText(tblKhachHang.getValueAt(row, 3).toString());
        // txtDiem.setText(...) -> Xóa dòng này
        cboLoaiKH.setSelectedItem(tblKhachHang.getValueAt(row, 5).toString());

        txtMaKH.setEnabled(false);
    }

    private String phanLoaiKhachHang(int diem) {
        if (diem >= 500) return "Kim cương";
        if (diem >= 300) return "Vàng";
        if (diem >= 100) return "Bạc";
        return "Thường";
    }

    private void loadTable() {
        try {
            DefaultTableModel model = (DefaultTableModel) tblKhachHang.getModel();
            model.setRowCount(0);

            String sql = "SELECT * FROM KhachHang";
            Connection conn = DBConnection.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                int diem = rs.getInt("diemtichluy");
                String loai = phanLoaiKhachHang(diem);

                model.addRow(new Object[]{
                    rs.getInt("makh"),
                    rs.getString("tenkh"),
                    rs.getString("sdt"),
                    rs.getString("diachi"),
                    diem,
                    loai
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    // Variables declaration
    private javax.swing.JButton btnMoi;
    private javax.swing.JButton btnSua;
    // private javax.swing.JButton btnThem; -> Đã xóa
    private javax.swing.JButton btnTracuu;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cboLoaiKH;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JLabel lblDiaChi;
    // private javax.swing.JLabel lblDiem; -> Đã xóa
    private javax.swing.JLabel lblHoTen;
    private javax.swing.JLabel lblMaKH;
    private javax.swing.JLabel lblPhanLoai;
    private javax.swing.JLabel lblQLTTKH;
    private javax.swing.JLabel lblSDT;
    private javax.swing.JPanel pnButton;
    private javax.swing.JPanel pnNhap;
    private javax.swing.JTable tblKhachHang;
    private javax.swing.JTextField txtDiaChi;
    // private javax.swing.JTextField txtDiem; -> Đã xóa
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JTextField txtMaKH;
    private javax.swing.JTextField txtSDT;
    // End of variables declaration
}