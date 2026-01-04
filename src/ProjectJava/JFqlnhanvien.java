package ProjectJava;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class JFqlnhanvien extends javax.swing.JPanel{
    DefaultTableModel tableModel;
    
    List<nhanvien> nhanvienList = new ArrayList<>();
    
    int selectedIndex = -1;
    int check = 0;
    int idCu = -1;
    
    public static nhanvien userSession = null; // Nhận dữ liệu từ Form Login

    String selectedImagePath = "";
    String oldAvatarPath;
   
    public JFqlnhanvien(nhanvien loginUser, Main main) {
        
        
        this.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.white);

        JButton back =  new JButton("👈 Trang Chủ");
        back.setBackground(Color.decode("#FF9966"));
        back.addActionListener(e->main.showScreen("HOME"));
        topPanel.add(back, BorderLayout.WEST);
        this.add(topPanel, BorderLayout.NORTH);
        initComponents();
        buttonGroup1.add(cbNam); 
        buttonGroup1.add(cbNu); 

         // căn giữa (an toàn)

        tblnhanvien.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);


        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        tableModel = (DefaultTableModel) tblnhanvien.getModel();
        userSession = loginUser;
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                shownhanvien();
                setImage(null);
            }
        });
    }
    
    private String getGenderFromCheckBox() {
        if (cbNam.isSelected()) return "Nam";
        if (cbNu.isSelected()) return "Nữ";
        return "";
    }
    
    private void setGenderToCheckBox(String gender) {
        if ("Nam".equalsIgnoreCase(gender)) {
            cbNam.setSelected(true);
        } else if ("Nữ".equalsIgnoreCase(gender)) {
            cbNu.setSelected(true);
        } else {
            buttonGroup1.clearSelection();
        }
    }

    private String formatLocalDate(LocalDate date) {
        if (date == null) return "";
        return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }
    
    private LocalDate parseLocalDate(String text) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            return LocalDate.parse(text, formatter);
        } catch (Exception e) {
            return null;
        }
    }
    
    private void shownhanvien(){
        boolean isAdmin = userSession.getRole().equalsIgnoreCase("admin");
        
        if (isAdmin) {
            // ADMIN: Xem tất cả và hiện bảng
            nhanvienList = DBConnectionNV.findAll();
            jScrollPane1.setVisible(true); 
            tblnhanvien.setVisible(true);
            lblTitle.setText("HỆ THỐNG QUẢN LÝ NHÂN VIÊN");
        } else {
            // NHÂN VIÊN: Chỉ lấy chính mình và ẩn bảng
            nhanvienList = new ArrayList<>();
            nhanvienList.add(userSession);
            
            jScrollPane1.setVisible(false); // ẨN HOÀN TOÀN BẢNG
            tblnhanvien.setVisible(false);
            lblTitle.setText("HỒ SƠ NHÂN VIÊN CÁ NHÂN");
            
            // Đổ dữ liệu của mình lên các ô nhập liệu để xem/sửa
            showData(userSession); 
        }
        
        // Đổ dữ liệu vào bảng (Chỉ có tác dụng với Admin)
        tableModel.setRowCount(0);
        if (nhanvienList != null) {
            nhanvienList.forEach(nv -> {
                tableModel.addRow(new Object[] {nv.getID_employee(), nv.getName_employee(), formatLocalDate(nv.getDate_of_birth()), 
                    nv.getGender(), nv.getPhonenumber(), nv.getAddress(), nv.getEmail(), nv.getPart(), formatLocalDate(nv.getStart_date()), nv.getRole()} );
            });
        };
        
        tblnhanvien.setRowHeight(30);
        
        OnOff(true, false);
    }
    
    private void showData(nhanvien nv) {
        txtID_employee.setText(String.valueOf(nv.getID_employee()));
        txtname_employee.setText(nv.getName_employee());
        txtdate_of_birth.setText(formatLocalDate(nv.getDate_of_birth()));
        txtphonenumber.setText(nv.getPhonenumber());
        txtaddress.setText(nv.getAddress());
        txtemail.setText(nv.getEmail());
        txtpart.setText(nv.getPart());
        txtstart_date.setText(formatLocalDate(nv.getStart_date()));
        txtrole.setText(nv.getRole());
        txtusername.setText(nv.getUsername());
        txtpassword.setText(nv.getPassword());
        setImage(nv.getAvatar());
        //Luu lai anh cu
        oldAvatarPath = nv.getAvatar();
        //reset lua chon anh moi
        selectedImagePath = "";

        setGenderToCheckBox(nv.getGender());
    }
    
    private void showNhanVienDetail() {
        if (selectedIndex < 0 || selectedIndex >= nhanvienList.size()) return;

        nhanvien nv = nhanvienList.get(selectedIndex);

        txtID_employee.setText(String.valueOf(nv.getID_employee()));
        txtname_employee.setText(nv.getName_employee());
        txtdate_of_birth.setText(formatLocalDate(nv.getDate_of_birth()));
        txtphonenumber.setText(nv.getPhonenumber());
        txtaddress.setText(nv.getAddress());
        txtemail.setText(nv.getEmail());
        txtpart.setText(nv.getPart());
        txtstart_date.setText(formatLocalDate(nv.getStart_date()));
        txtrole.setText(nv.getRole());
        txtusername.setText(nv.getUsername());
        txtpassword.setText(nv.getPassword());
        setImage(nv.getAvatar());
        //Luu lai anh cu
        oldAvatarPath = nv.getAvatar();
        //reset lua chon anh moi
        selectedImagePath = "";

        setGenderToCheckBox(nv.getGender()); 
        // HIỂN THỊ ẢNH
        setImage(nv.getAvatar()); // nếu null → ảnh mặc định
    }

    
    public void OnOff(boolean a, boolean b){
        boolean isAdmin = userSession.getRole().equalsIgnoreCase("admin");
        
        btnSave.setVisible(b);
        btnCancel.setVisible(b);
        
        // PHÂN QUYỀN NÚT BẤM
        btnAdd.setVisible(a && isAdmin);
        btnDelete.setVisible(a && isAdmin);
        btnSearch.setVisible(a && isAdmin);
        
        btnEdit.setVisible(a); // Nhân viên vẫn có thể bấm sửa thông tin cá nhân
        txtrole.setEditable(isAdmin);
        txtusername.setEditable(isAdmin);
        txtID_employee.setEditable(isAdmin); // Khóa ID không cho nhân viên tự đổi mã số của mình
    }
    
    private void setImage(String imagePath) {
    try {
        ImageIcon icon;
        String pathToCheck;

        if (imagePath == null || imagePath.isEmpty()) {
            // ẢNH MẶC ĐỊNH
            icon = new ImageIcon(getClass().getResource("/images/default.png"));
        } else {
            // ẢNH CỦA NHÂN VIÊN
            icon = new ImageIcon(imagePath);
        }

        Image img = icon.getImage().getScaledInstance(
                lblavatar.getWidth(),
                lblavatar.getHeight(),
                Image.SCALE_SMOOTH
        );

        lblavatar.setIcon(new ImageIcon(img));
    } catch (Exception e) {
        // nếu lỗi vẫn hiện ảnh mặc định
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/default.png"));
        lblavatar.setIcon(icon);
    }
}


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        JPanel pnCenter = new JPanel();
        jMenu1 = new javax.swing.JMenu();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu4 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();
        jMenu7 = new javax.swing.JMenu();
        jMenuBar3 = new javax.swing.JMenuBar();
        jMenu8 = new javax.swing.JMenu();
        jMenu9 = new javax.swing.JMenu();
        jMenuBar4 = new javax.swing.JMenuBar();
        jMenu10 = new javax.swing.JMenu();
        jMenu11 = new javax.swing.JMenu();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lbladdress = new javax.swing.JLabel();
        lblID_employee = new javax.swing.JLabel();
        txtID_employee = new javax.swing.JTextField();
        lblname_employee = new javax.swing.JLabel();
        lblgender = new javax.swing.JLabel();
        lbldate_of_birth = new javax.swing.JLabel();
        lblpart = new javax.swing.JLabel();
        lblstart_date = new javax.swing.JLabel();
        lblemail = new javax.swing.JLabel();
        lblphonenumber = new javax.swing.JLabel();
        txtdate_of_birth = new javax.swing.JTextField();
        txtphonenumber = new javax.swing.JTextField();
        txtemail = new javax.swing.JTextField();
        txtname_employee = new javax.swing.JTextField();
        txtpart = new javax.swing.JTextField();
        txtstart_date = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        cbNam = new javax.swing.JCheckBox();
        cbNu = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtaddress = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblnhanvien = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        lblrole = new javax.swing.JLabel();
        txtrole = new javax.swing.JTextField();
        lblpassword = new javax.swing.JLabel();
        txtusername = new javax.swing.JTextField();
        lblusername = new javax.swing.JLabel();
        txtpassword = new javax.swing.JTextField();
        lblavatar = new javax.swing.JLabel();
        btnBrowse = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();

        jMenu1.setText("jMenu1");

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("jCheckBoxMenuItem1");

        jMenu4.setText("File");
        jMenuBar2.add(jMenu4);

        jMenu5.setText("Edit");
        jMenuBar2.add(jMenu5);

        jMenu6.setText("jMenu6");

        jMenu7.setText("jMenu7");

        jMenu8.setText("File");
        jMenuBar3.add(jMenu8);

        jMenu9.setText("Edit");
        jMenuBar3.add(jMenu9);

        jMenu10.setText("File");
        jMenuBar4.add(jMenu10);

        jMenu11.setText("Edit");
        jMenuBar4.add(jMenu11);

        

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel4.setBorder(javax.swing.BorderFactory.createCompoundBorder());

        lblTitle.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        lblTitle.setText("THÔNG TIN NHÂN VIÊN");
        lblTitle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        lbladdress.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbladdress.setText("Địa chỉ:");

        lblID_employee.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblID_employee.setText("Mã nhân viên:");

        txtID_employee.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtID_employee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtID_employeeActionPerformed(evt);
            }
        });

        lblname_employee.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblname_employee.setText("Họ và tên:");

        lblgender.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblgender.setText("Giới tính:");

        lbldate_of_birth.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbldate_of_birth.setText("Ngày sinh:");

        lblpart.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblpart.setText("Bộ phận:");

        lblstart_date.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblstart_date.setText("Ngày bắt đầu:");

        lblemail.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblemail.setText("Email:");

        lblphonenumber.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblphonenumber.setText("Số điện thoại:");

        txtdate_of_birth.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtdate_of_birth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtdate_of_birthActionPerformed(evt);
            }
        });

        txtphonenumber.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtphonenumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtphonenumberActionPerformed(evt);
            }
        });

        txtemail.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtemail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtemailActionPerformed(evt);
            }
        });

        txtname_employee.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtname_employee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtname_employeeActionPerformed(evt);
            }
        });

        txtpart.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtpart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtpartActionPerformed(evt);
            }
        });

        txtstart_date.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtstart_date.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtstart_dateActionPerformed(evt);
            }
        });

        btnSearch.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnSearch.setText("Tìm kiếm");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        btnDelete.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnDelete.setText("Xóa");
        btnDelete.setMaximumSize(new java.awt.Dimension(74, 32));
        btnDelete.setMinimumSize(new java.awt.Dimension(74, 32));
        btnDelete.setPreferredSize(new java.awt.Dimension(74, 32));
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnEdit.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnEdit.setText("Sửa");
        btnEdit.setMaximumSize(new java.awt.Dimension(74, 32));
        btnEdit.setMinimumSize(new java.awt.Dimension(74, 32));
        btnEdit.setPreferredSize(new java.awt.Dimension(74, 32));
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnAdd.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnAdd.setText("Thêm");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnCancel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnCancel.setText("Hủy");
        btnCancel.setMaximumSize(new java.awt.Dimension(74, 32));
        btnCancel.setMinimumSize(new java.awt.Dimension(74, 32));
        btnCancel.setPreferredSize(new java.awt.Dimension(74, 32));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnSave.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnSave.setText("Lưu");
        btnSave.setMaximumSize(new java.awt.Dimension(74, 32));
        btnSave.setMinimumSize(new java.awt.Dimension(74, 32));
        btnSave.setPreferredSize(new java.awt.Dimension(74, 32));
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        cbNam.setText("Nam");
        cbNam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbNamActionPerformed(evt);
            }
        });

        cbNu.setText("Nữ");

        txtaddress.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtaddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtaddressActionPerformed(evt);
            }
        });
        jScrollPane2.setViewportView(txtaddress);

        tblnhanvien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã nhân viên", "Họ và tên", "Ngày sinh", "Giới tính", "SĐT", "Địa chỉ", "Email", "Bộ phận", "Ngày bắt đầu", "Vai trò"
            }
        ));
        tblnhanvien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblnhanvienMouseClicked(evt);
            }
        });
        tblnhanvien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblnhanvienKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblnhanvien);
        if (tblnhanvien.getColumnModel().getColumnCount() > 0) {
            tblnhanvien.getColumnModel().getColumn(0).setPreferredWidth(100);
            tblnhanvien.getColumnModel().getColumn(0).setMaxWidth(200);
            tblnhanvien.getColumnModel().getColumn(1).setPreferredWidth(150);
            tblnhanvien.getColumnModel().getColumn(1).setMaxWidth(300);
            tblnhanvien.getColumnModel().getColumn(2).setPreferredWidth(100);
            tblnhanvien.getColumnModel().getColumn(2).setMaxWidth(200);
            tblnhanvien.getColumnModel().getColumn(3).setPreferredWidth(80);
            tblnhanvien.getColumnModel().getColumn(4).setPreferredWidth(80);
            tblnhanvien.getColumnModel().getColumn(5).setPreferredWidth(400);
            tblnhanvien.getColumnModel().getColumn(5).setMaxWidth(1000);
            tblnhanvien.getColumnModel().getColumn(6).setPreferredWidth(150);
            tblnhanvien.getColumnModel().getColumn(6).setMaxWidth(500);
            tblnhanvien.getColumnModel().getColumn(7).setPreferredWidth(150);
            tblnhanvien.getColumnModel().getColumn(7).setMaxWidth(500);
            tblnhanvien.getColumnModel().getColumn(8).setPreferredWidth(100);
            tblnhanvien.getColumnModel().getColumn(8).setMaxWidth(150);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        lblrole.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblrole.setText("Vai trò:");

        txtrole.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtrole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtroleActionPerformed(evt);
            }
        });

        lblpassword.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblpassword.setText("Mật khẩu:");

        txtusername.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtusername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtusernameActionPerformed(evt);
            }
        });

        lblusername.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblusername.setText("Tên đăng nhập:");

        txtpassword.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtpassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtpasswordActionPerformed(evt);
            }
        });

        lblavatar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        btnBrowse.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        btnBrowse.setText("Chọn ảnh");
        btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(509, 509, 509))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addGap(81, 81, 81)
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(lblusername, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(lblpassword, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(txtusername, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(txtpassword, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addGap(478, 478, 478)
                                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(76, 76, 76))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                                .addGap(173, 173, 173)
                                                .addComponent(lblavatar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                                .addGap(218, 218, 218)
                                                .addComponent(btnBrowse)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblname_employee, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblphonenumber, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblgender, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbldate_of_birth, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblID_employee))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtID_employee, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtname_employee, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtdate_of_birth, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtphonenumber, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(cbNam, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(45, 45, 45)
                                        .addComponent(cbNu, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(74, 74, 74)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblpart, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblemail, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lbladdress, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(36, 36, 36)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(txtpart, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtemail, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(83, 83, 83)
                                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(1, 1, 1))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(lblstart_date, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtstart_date, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addComponent(lblrole, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtrole, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1350, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(101, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(lblTitle)
                .addGap(41, 41, 41)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblID_employee, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtID_employee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbladdress, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(79, 79, 79)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblname_employee, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtname_employee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblemail, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtemail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(56, 56, 56)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lbldate_of_birth, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtdate_of_birth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblpart, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtpart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(lblavatar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnBrowse)))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblgender, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbNam)
                            .addComponent(cbNu)
                            .addComponent(lblstart_date, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtstart_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtusername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblusername, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(25, 25, 25))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(47, 47, 47)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblphonenumber, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtphonenumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblrole, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtrole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtpassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblpassword, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 165, Short.MAX_VALUE)))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

       

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(pnCenter);
        pnCenter.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        this.add(pnCenter, java.awt.BorderLayout.CENTER);

        
    }// </editor-fold>//GEN-END:initComponents

    private void tblnhanvienKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblnhanvienKeyReleased
        if (!userSession.getRole().equalsIgnoreCase("admin")) return;
        selectedIndex = tblnhanvien.getSelectedRow();
        showNhanVienDetail();
    }//GEN-LAST:event_tblnhanvienKeyReleased

    private void tblnhanvienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblnhanvienMouseClicked
        // Nếu là nhân viên → không cho click bảng
        if (!userSession.getRole().equalsIgnoreCase("admin")) {
            return;
        }
        selectedIndex = tblnhanvien.getSelectedRow();
        if (selectedIndex >= 0) {
            // Lấy đối tượng từ list dựa trên dòng được chọn
            nhanvien nv = nhanvienList.get(selectedIndex);

            // LƯU QUAN TRỌNG: Lưu lại ID gốc vào biến idCu
            idCu = nv.getID_employee();

            showNhanVienDetail();
        }
    }//GEN-LAST:event_tblnhanvienMouseClicked

    private void cbNamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbNamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbNamActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        try{
            int ID = Integer.parseInt(txtID_employee.getText());
            String name_employee = txtname_employee.getText();
            LocalDate date_of_birth = parseLocalDate(txtdate_of_birth.getText());
            String gender = getGenderFromCheckBox();
            String phonenumber = txtphonenumber.getText();
            String address = txtaddress.getText();
            String email = txtemail.getText();
            String part = txtpart.getText();
            LocalDate start_date = parseLocalDate(txtstart_date.getText());
            String role = txtrole.getText();
            String username = txtusername.getText();
            String password = txtpassword.getText();
            String avatar;


            //Kiem tra dieu kien truoc khi luu
            // --- 1. KIỂM TRA TRỐNG (Basic Validation) ---
            if (txtID_employee.getText().trim().isEmpty() || txtname_employee.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Mã và Tên nhân viên không được để trống!");
                return;
            }

            if (date_of_birth == null || start_date == null) {
                JOptionPane.showMessageDialog(this, "Ngày tháng không hợp lệ! Vui lòng nhập định dạng: dd-MM-yyyy");
                return;
            }

            if (!phonenumber.matches("^0\\d{9}$")) {
                JOptionPane.showMessageDialog(this, "Số điện thoại phải có 10 chữ số và bắt đầu bằng số 0!");
                return;
            }

            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                JOptionPane.showMessageDialog(this, "Email không đúng định dạng (ví dụ: abc@gmail.com)!");
                return;
            }

            // --- 4. KIỂM TRA LOGIC NGÀY THÁNG ---
            if (start_date.isBefore(date_of_birth)) {
                JOptionPane.showMessageDialog(this, "Ngày bắt đầu làm việc không thể trước ngày sinh!");
                return;
            }
            
            // nếu có chọn ảnh mới
            if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
                avatar = selectedImagePath;
            } else {
                avatar = oldAvatarPath; // giữ ảnh cũ
            }

            nhanvien nv = new nhanvien(ID, name_employee, date_of_birth, getGenderFromCheckBox(), phonenumber, address, email, part, start_date, role, username, password, avatar);
            if(check == 1){
                if (DBConnectionNV.existsById(ID)) {
                    JOptionPane.showMessageDialog(this, "Mã nhân viên này đã tồn tại!");
                } else {
                    DBConnectionNV.insert(nv);
                    JOptionPane.showMessageDialog(this, "Thêm mới thành công!");
                }
            }
            if(check == -1){
                DBConnectionNV.update(nv, idCu);
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            }
            shownhanvien();
            OnOff(true, false);
            check = 0;
            idCu = -1;

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Mã nhân viên phải là số nguyên!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra: " + e.getMessage());
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        shownhanvien();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        txtID_employee.setText("");
        txtname_employee.setText("");
        txtdate_of_birth.setText("");
        buttonGroup1.clearSelection();
        txtphonenumber.setText("");
        txtaddress.setText("");
        txtemail.setText("");
        txtpart.setText("");
        txtstart_date.setText("");
        txtrole.setText("");
        txtusername.setText("");
        txtpassword.setText("");
        selectedImagePath = "";
        oldAvatarPath = "";
        setImage(null);
        
        OnOff(false, true);

        check = 1;
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        OnOff(false, true);
        if (!userSession.getRole().equalsIgnoreCase("admin")) {
            txtID_employee.setEditable(false);
        }

        check = -1;
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        int selectedIndex = tblnhanvien.getSelectedRow();
        if(selectedIndex >= 0){
            nhanvien nv = nhanvienList.get(selectedIndex);

            int option = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa nhân viên này không?");
            System.out.println("option: " + option);

            if(option == 0){
                DBConnectionNV.delete(nv.getID_employee());
                shownhanvien();
            }
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
        String input = JOptionPane.showInputDialog(this, "Nhập tên để tìm kiếm");
        if(input != null && input.length() > 0){
            nhanvienList = DBConnectionNV.findByFullname(input);

            tableModel.setRowCount(0);

            if (nhanvienList != null) {
                nhanvienList.forEach(nv -> {
                    tableModel.addRow(new Object[] {nv.getID_employee(), nv.getName_employee(), formatLocalDate(nv.getDate_of_birth()),
                        nv.getGender(), nv.getPhonenumber(), nv.getAddress(), nv.getEmail(), nv.getPart(), formatLocalDate(nv.getStart_date()), nv.getRole()} );
                });
            };
        } else{
            shownhanvien();
        }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void txtstart_dateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtstart_dateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtstart_dateActionPerformed

    private void txtpartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtpartActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtpartActionPerformed

    private void txtname_employeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtname_employeeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtname_employeeActionPerformed

    private void txtemailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtemailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtemailActionPerformed

    private void txtphonenumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtphonenumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtphonenumberActionPerformed

    private void txtdate_of_birthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtdate_of_birthActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtdate_of_birthActionPerformed

    private void txtID_employeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtID_employeeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtID_employeeActionPerformed

    private void txtaddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtaddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtaddressActionPerformed

    private void txtroleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtroleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtroleActionPerformed

    private void txtusernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtusernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtusernameActionPerformed

    private void txtpasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtpasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtpasswordActionPerformed

    private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Hình ảnh", "jpg", "png", "jpeg"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedImagePath = selectedFile.getAbsolutePath(); // Lấy đường dẫn tuyệt đối
            setImage(selectedImagePath); // Hiển thị lên Label ngay lập tức 
        }
    }//GEN-LAST:event_btnBrowseActionPerformed

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnBrowse;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSearch;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.JCheckBox cbNam;
    private javax.swing.JCheckBox cbNu;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu11;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuBar jMenuBar3;
    private javax.swing.JMenuBar jMenuBar4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblID_employee;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lbladdress;
    private javax.swing.JLabel lblavatar;
    private javax.swing.JLabel lbldate_of_birth;
    private javax.swing.JLabel lblemail;
    private javax.swing.JLabel lblgender;
    private javax.swing.JLabel lblname_employee;
    private javax.swing.JLabel lblpart;
    private javax.swing.JLabel lblpassword;
    private javax.swing.JLabel lblphonenumber;
    private javax.swing.JLabel lblrole;
    private javax.swing.JLabel lblstart_date;
    private javax.swing.JLabel lblusername;
    private javax.swing.JTable tblnhanvien;
    private javax.swing.JTextField txtID_employee;
    private javax.swing.JTextField txtaddress;
    private javax.swing.JTextField txtdate_of_birth;
    private javax.swing.JTextField txtemail;
    private javax.swing.JTextField txtname_employee;
    private javax.swing.JTextField txtpart;
    private javax.swing.JTextField txtpassword;
    private javax.swing.JTextField txtphonenumber;
    private javax.swing.JTextField txtrole;
    private javax.swing.JTextField txtstart_date;
    private javax.swing.JTextField txtusername;
    // End of variables declaration//GEN-END:variables
}

