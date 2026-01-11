package ProjectJava;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Map;
public class HoaDonPanel extends JPanel {
    private Main main;
    private Nhap nhap;
    private JTextField txtsdt, txtten, txtdiachi, txtdob;
    private JLabel lbDiemTichLuyValue;
    private JComboBox<String> combo ;
    private JLabel lbQRCode;
 
    public HoaDonPanel(Main main, Nhap nhap, HoaDon hoadon) { 
        this.main = main;
        this.nhap = nhap;
        setLayout(new BorderLayout());
        
       
        setPreferredSize(new Dimension(800, 500)); 
        
        setBackground(Color.decode("#FF9966"));
      
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

        JLabel title = new JLabel("CHI TIẾT THANH TOÁN", SwingConstants.CENTER);
        title.setFont(new Font("Times New Roman", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Tạo khoảng cách cho đẹp

        // Nút tắt
        JButton btnDong = new JButton("x");
        //btnDong.setForeground(Color.RED);
        //btnDong.setBorderPainted(false);
        //btnDong.setContentAreaFilled(false);
        btnDong.setFocusPainted(false);
        btnDong.setMargin(new Insets(0, 0, 0, 0));
        btnDong.setPreferredSize(new Dimension(30, 20));
        btnDong.setFont(new Font("Arial", Font.BOLD, 16));
        
        
        btnDong.addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(this);
            if (w != null) {
                w.dispose();
            }
        });

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.decode("#FF9966"));
        top.add(title, BorderLayout.CENTER);
        top.add(btnDong, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);
        
        // Thanh Chọn Phương Thức Thanh Toán 
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);

        

        

        String[] options = {
            "Tiền Mặt",
            "Chuyển Khoản",
            "Thẻ Tín Dụng",
        };
        combo = new JComboBox<>(options);
        combo.setFont(new Font("Time New Roman", Font.BOLD, 15));
        JLabel pttt = new JLabel("Chọn phương thức thanh toán:");
        pttt.setFont(new Font("Time New Roman", Font.BOLD, 16));
        
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        selectionPanel.setBackground(Color.white);
        selectionPanel.add(pttt);
        selectionPanel.add(combo);

        JPanel qrPanel = new JPanel();
        qrPanel.setBackground(Color.WHITE);
        qrPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20)); // Căn lề
        qrPanel.setLayout(new BoxLayout(qrPanel, BoxLayout.Y_AXIS));
        lbQRCode = new JLabel();
        lbQRCode.setPreferredSize(new Dimension(200, 200));
        lbQRCode.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lbQRCode.setHorizontalAlignment(SwingConstants.CENTER);
        lbQRCode.setVisible(false);

        try {
            java.io.File f = new java.io.File("ProjectJava/images/qrcode.png"); 

            if (f.exists()) {
                ImageIcon iconQR = new ImageIcon(f.getAbsolutePath());
                // Scale ảnh xuống 200x200 để vừa khung mới
                Image imgQR = iconQR.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                lbQRCode.setIcon(new ImageIcon(imgQR));
                lbQRCode.setText(""); 
            } else {
                lbQRCode.setText("Không thấy ảnh!");
                lbQRCode.setForeground(Color.RED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JLabel lbGuide = new JLabel("Quét mã để thanh toán");
        lbGuide.setFont(new Font("Arial", Font.ITALIC, 12));
        lbGuide.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbQRCode.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel qrContainer = new JPanel();
        qrContainer.setLayout(new BoxLayout(qrContainer, BoxLayout.Y_AXIS));
        qrContainer.setBackground(Color.WHITE);
        qrContainer.add(lbQRCode);
        // --- ĐOẠN CODE ĐÃ SỬA LẠI ---

        // 1. Tạo một panel phụ để căn giữa QR Code
        JPanel qrCenterWrapper = new JPanel(new GridBagLayout()); 
        qrCenterWrapper.setBackground(Color.WHITE);
        qrCenterWrapper.add(qrContainer); // Bỏ cụm QR vào đây để nó tự động ra giữa

        // 2. Sắp xếp vào centerPanel (đang là BorderLayout)
        centerPanel.add(selectionPanel, BorderLayout.NORTH); // Thanh chọn nằm trên cùng
        centerPanel.add(qrCenterWrapper, BorderLayout.CENTER); // Ảnh QR nằm giữa khoảng trống còn lại

        // 3. Thêm centerPanel vào giao diện chính
        add(centerPanel, BorderLayout.CENTER);

        // QR
        
        combo.addActionListener(e->{
            String selected = (String) combo.getSelectedItem();
            if("Chuyển Khoản".equals(selected)){
                lbQRCode.setVisible(true);
                qrContainer.setVisible(true);
            }else{
                lbQRCode.setVisible(false);
                qrContainer.setVisible(false);
            }
            this.revalidate();
            this.repaint();
        });

        // Add tích điểm thành viên
        JPanel tichdiemPanel = new JPanel();
        tichdiemPanel.setLayout(new BoxLayout(tichdiemPanel, BoxLayout.Y_AXIS));
        tichdiemPanel.setBackground(Color.WHITE);
        tichdiemPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 0));
        JLabel lbtichdiem = new JLabel("Tích Điểm Thành Viên");
        lbtichdiem.setFont(new Font("Time New Roman", Font.BOLD, 16));
        
        Font font = new Font("Time New Roman", Font.PLAIN, 14);
        JLabel lbsdt = new JLabel("Số Điện Thoại:");
        lbsdt.setFont(font);
        txtsdt = new JTextField(10);
        txtsdt.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtsdt.getPreferredSize().height));
        
        tichdiemPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        tichdiemPanel.add(lbsdt);
        tichdiemPanel.add(txtsdt);

    
        JLabel lbten = new JLabel("Họ và Tên:");
        JLabel lbdiachi = new JLabel("Địa Chỉ:");
        JLabel dob = new JLabel("Ngày Sinh:");
        lbten.setFont(font);
        lbsdt.setFont(font);
        lbdiachi.setFont(font);
        dob.setFont(font);

        txtten = new JTextField(10);
        txtten.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtten.getPreferredSize().height));
        
        txtdiachi = new JTextField(10);
        txtdiachi.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtdiachi.getPreferredSize().height));
        txtdob = new JTextField(10);
        txtdob.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtdob.getPreferredSize().height));
        
        JButton btxacnhan = new JButton("Xác Nhận");
        btxacnhan.setAlignmentX(Component.CENTER_ALIGNMENT);
        btxacnhan.setFont(new Font("Time New Roman", Font.BOLD, 14));
        btxacnhan.setBackground(Color.decode("#FF9966"));
        btxacnhan.setMaximumSize(new Dimension(Integer.MAX_VALUE, btxacnhan.getPreferredSize().height));
        btxacnhan.setFocusPainted(false);
        btxacnhan.setMargin(new Insets(0, 0, 0, 0));
        
        JButton btThemKH = new JButton("Lưu Khách Hàng Mới");
        btThemKH.setAlignmentX(Component.CENTER_ALIGNMENT);
        btThemKH.setFont(new Font("Time New Roman", Font.BOLD, 14));
        btThemKH.setBackground(Color.decode("#FF9966"));
        btThemKH.setMaximumSize(new Dimension(Integer.MAX_VALUE, btThemKH.getPreferredSize().height));
        btThemKH.setFocusPainted(false);
        btThemKH.setMargin(new Insets(0, 0, 0, 0));
        btThemKH.setVisible(false);

        

        btxacnhan.addActionListener(e->{
            String sdt = txtsdt.getText().trim();
            if(sdt.isEmpty()){
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại để tìm");
                return;
            }
            ArrayList<KhachHang> listResult = KhachHang.searchKhachHang(sdt);

            if(!listResult.isEmpty()){
                KhachHang kh = listResult.get(0);
                txtten.setText(kh.getTenKH());
                txtdiachi.setText(kh.getDiaChi());
                txtdob.setText(kh.getNgaySinh().toString());

                if(kh.getNgaySinh() != null){
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    txtdob.setText(sdf.format(kh.getNgaySinh()));
                }
                else{
                    txtdob.setText("");
                }

                lbDiemTichLuyValue.setText(String.valueOf(kh.getDiemTL()));
                JOptionPane.showMessageDialog(this, "Khách hàng đã tồn tại. Thông tin đã được điền tự động.");
            } 
            else{
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Không tìm thấy khách hàng. Bạn có muốn tạo mới không?",
                    "Thông báo", JOptionPane.YES_NO_OPTION);
                if(confirm == JOptionPane.YES_OPTION){
                    txtten.setText("");
                    txtdiachi.setText("");
                    txtdob.setText("");
                    lbDiemTichLuyValue.setText("0");

                    btThemKH.setVisible(true);
                    txtten.requestFocus();
                }else{
                    btThemKH.setVisible(false);
                }
            }
            this.revalidate();
            this.repaint();
            
        });

        
        btThemKH.addActionListener(e->{
            String ten = txtten.getText().trim();
            String sdt = txtsdt.getText().trim();
            String diachi = txtdiachi.getText().trim();
            String ngaysinhStr = txtdob.getText().trim();
            if(ten.isEmpty() || sdt.isEmpty() || diachi.isEmpty() || ngaysinhStr.isEmpty()){
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin khách hàng.");
                return;
            }
            Date ngaysinh = null;
            try{
                if(!ngaysinhStr.isEmpty()){
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    ngaysinh = sdf.parse(ngaysinhStr);
                }else{
                    // Nếu không nhập ngày sinh thì mặc định lấy ngày hiện tại hoặc null
                    ngaysinh = new Date();
                }
            }catch(Exception ex){
                JOptionPane.showMessageDialog(this, "Định dạng ngày sinh không hợp lệ. Vui lòng sử dụng định dạng dd-MM-yyyy.");
                return;
            }
            KhachHang newKH = new KhachHang("", ten, sdt, diachi, ngaysinh, 0);
            boolean success = KhachHang.addKhachHangToDB(newKH);
            if(success){
                JOptionPane.showMessageDialog(this, "Thêm khách hàng mới thành công."); 
            } else{
                JOptionPane.showMessageDialog(this, "Thêm khách hàng mới thất bại.");
            }
        });
        JLabel lbDiem = new JLabel("Điểm tích lũy hiện có:");
        lbDiemTichLuyValue = new JLabel("0");
        lbDiemTichLuyValue.setFont(new Font("Times New Roman", Font.BOLD, 14));
        lbDiemTichLuyValue.setForeground(Color.RED);

        
        tichdiemPanel.add(lbtichdiem);
        tichdiemPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        tichdiemPanel.add(lbten);
        tichdiemPanel.add(txtten);
        tichdiemPanel.add(lbdiachi);
        tichdiemPanel.add(txtdiachi);
        tichdiemPanel.add(dob);
        tichdiemPanel.add(txtdob);
        
        JPanel inHDPanel = new JPanel();
        inHDPanel.setLayout(new BorderLayout());
        inHDPanel.setBackground(Color.white);
        JButton btInHD = new JButton("In Hóa Đơn");
        btInHD.setFont(new Font("Time New Roman", Font.BOLD, 16));
        btInHD.setBackground(Color.decode("#FF9966"));
        btInHD.setFocusPainted(false);
        btInHD.setMargin(new Insets(10, 10, 10, 10));
        btInHD.addActionListener(e->{
            String sdt = txtsdt.getText().trim();
            double total = nhap.updateTotal();
            

            if(total <= 0){
                JOptionPane.showMessageDialog(this, "Giỏ hàng đang rỗng");
                return;
            }
            int diemTL = (int)(total / 10000);
            String ptThanhToan = combo.getSelectedItem().toString();
            int maNV = Session.userId;
            if(main.getNhanVien() != null){
                maNV = main.getNhanVien().getID_employee();
            }else{
                maNV = 1;
            }
            if(!sdt.isEmpty()){
                boolean isUpdated = KhachHang.congDiemTichLuy(sdt, diemTL);
                if(isUpdated){
                    
                    int diemHienTai = Integer.parseInt(lbDiemTichLuyValue.getText());
                    lbDiemTichLuyValue.setText(String.valueOf(diemHienTai + diemTL));
                }
            }
            
            boolean saved = HoaDon.saveInvoice(sdt, total, nhap.cartMap, maNV, ptThanhToan);
            if(saved){
                String mess = "Thanh toán thành công!\n" +
                  "Tổng tiền: " + total + " VNĐ";
                if(!sdt.isEmpty()){
                    mess+= "\nĐiểm tích lũy cộng thêm: " + diemTL;
                }
                JOptionPane.showMessageDialog(this, mess);
                nhap.clearCart();
                nhap.loadProductsFromDB();
                main.getQuanLyKho().loadDataToTable();
                Window w = SwingUtilities.getWindowAncestor(this);
                if (w != null) {
                    w.dispose();
                }
            }else{
                JOptionPane.showMessageDialog(this, "Lưu hóa đơn thất bại!\\n" + //
                                        "Có thể do lỗi kết nối hoặc dữ liệu sản phẩm/nhân viên không khớp.\", \n" + //
                                        "                    \"Lỗi Thanh Toán\", JOptionPane.ERROR_MESSAGE");
            }
            
            
            
        });
        
        inHDPanel.add(btInHD, BorderLayout.CENTER);
        inHDPanel.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.NORTH);
        inHDPanel.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.SOUTH);
        add(inHDPanel, BorderLayout.SOUTH);
        
        tichdiemPanel.add(lbDiem);
        tichdiemPanel.add(lbDiemTichLuyValue);
        tichdiemPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        tichdiemPanel.add(btThemKH);
        tichdiemPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        tichdiemPanel.add(btxacnhan);
        add(tichdiemPanel, BorderLayout.WEST);
    }
    public void loadKhachHangFromDB(){
            ArrayList<KhachHang> khs = KhachHang.getAllKhachHang();
            renderKhachHang(khs);
        }
        public void renderKhachHang(ArrayList<KhachHang> khs){
            if(khs.isEmpty()){
                JLabel lbEmpty = new JLabel("Không tìm thấy khách hàng nào.");
                add(lbEmpty);
            }
            
            for(KhachHang kh : khs){
                System.out.println("Mã KH: " + kh.getMaKH());
                System.out.println("Tên KH: " + kh.getTenKH());
                System.out.println("SĐT: " + kh.getSDT());
                System.out.println("Địa Chỉ: " + kh.getDiaChi());
                System.out.println("Ngày Sinh: " + kh.getNgaySinh());
                System.out.println("Điểm Tích Lũy: " + kh.getDiemTL());
                System.out.println("---------------------------");
            }
            revalidate();
            repaint();
        }   
    
}