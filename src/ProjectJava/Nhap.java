package ProjectJava;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Nhap extends JPanel{
    Main main;
    
    JPanel sidebar, pay, content; // Khung chứa nội dung
    JPanel sidebartruot, paytruot; // Khung trượt 
    JButton toggle, giohang; // Nút hôm & giỏ hàng
    JButton home, qlkho, qlkh, bc_tk, qlnv;
    JTextField search, txtsl;
    JPanel productPanel;
    Timer timer;
    JPanel payListPanel; 
    JButton btplus, btabt;
    JPanel bottomPanel;
    JLabel lbTotal;

    JLabel lbprice;
    int sidebarWidth = 0;
    int payWidth = 0;
    final int SIDEBAR_MAX = 180;
    final int PAY_MAX = 280;
    int x = 0;
    public Map<String, CartItem> cartMap = new HashMap<>();

    boolean isSidebarOpen = false;
    boolean isPayOpen = false;

    enum SlideTarget {SIDEBAR, PAY}
    SlideTarget currentTarget;
    
    


    public Nhap(Main main){
        this.main = main;
        setLayout(new BorderLayout());
        setinitLayout();
        setSidebar();
        setTopbar();
        //setPay();
        setContent();
        //setSearch();
        loadProductsFromDB();
        this.addComponentListener(new java.awt.event.ComponentAdapter(){
        @Override
        public void componentShown(java.awt.event.ComponentEvent e){
            loadProductsFromDB();
        }
    });

    }
    public void setContent(){
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("DANH MỤC SẢN PHẨM");
        title.setFont(new Font("Time New Roman", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        northPanel.add(title);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        search = new JTextField(30);

        JButton btsearch = new JButton("Tìm");
        btsearch.setBackground(Color.decode("#FF9966"));
        btsearch.setFocusPainted(false);
        //btsearch.setMargin(new Insets(0, 0 , 0 , 0));

        JButton btReload = new JButton("Tải lại");
        btReload.setBackground(Color.decode("#FF9966"));
        btReload.setFocusPainted(false);
        //btReload.setMargin(new Insets(0, 0 , 0 , 0));

        JButton btLichsu = new JButton("Lịch sử bán hàng");
        btLichsu.setBackground(Color.decode("#FF9966"));
        btLichsu.setFocusPainted(false);
        //btLichsu.setMargin(new Insets(0, 0 , 0 , 0));

        searchPanel.add(search);
        searchPanel.add(btsearch);
        searchPanel.add(btReload);
        searchPanel.add(btLichsu);
        northPanel.add(Box.createVerticalStrut(10)); // khoang cach
        northPanel.add(searchPanel);
        
        btsearch.addActionListener(e ->{
            String keyword = search.getText();
            ArrayList<SanPham> listResult = new SanPhamDAO().findByName(keyword);
            renderProductList(listResult);
        });
        btReload.addActionListener(e ->{
            search.setText("");
            loadProductsFromDB();
        });
        btLichsu.addActionListener(e ->{
            showHistoryDialog();
        });
        
        productPanel = new JPanel();
        productPanel.setLayout(new GridLayout(0, 4, 15, 15));
        productPanel.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(productPanel);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        content.add(scroll, BorderLayout.CENTER);
        content.add(northPanel, BorderLayout.NORTH);
    }
    
    private void setinitLayout(){
        sidebartruot = new JPanel(new BorderLayout());
        sidebartruot.setPreferredSize(new Dimension(0, 0));
        add(sidebartruot, BorderLayout.WEST);

        sidebar = new JPanel();
        sidebar.setBackground(Color.decode("#FF9966"));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebartruot.add(sidebar, BorderLayout.CENTER);

        paytruot = new JPanel(new BorderLayout());
        paytruot.setPreferredSize(new Dimension(0, 0));
        add(paytruot, BorderLayout.EAST);

        pay = new JPanel();
        pay.setLayout(new BoxLayout(pay, BoxLayout.Y_AXIS));
        pay.setBackground(Color.decode("#FF9966"));

        payListPanel = new JPanel();
        payListPanel.setLayout(new BoxLayout(payListPanel, BoxLayout.Y_AXIS));
        payListPanel.setBackground(Color.white);

        JScrollPane payScroll = new JScrollPane(payListPanel);
        payScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        payScroll.getVerticalScrollBar().setUnitIncrement(16);
        pay.add(payScroll, BorderLayout.CENTER);


        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.setBackground(Color.WHITE);

        lbTotal = new JLabel("Tổng tiền: 0 VND");
        lbTotal.setFont(new Font("Arial", Font.BOLD, 16));
        lbTotal.setForeground(Color.RED);
        lbTotal.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton thanhtoan = new JButton("THANH TOÁN");
        thanhtoan.setFont(new Font("Time New Roman", Font.BOLD, 18));
        thanhtoan.setBackground(new Color(255, 153, 102));
        thanhtoan.setForeground(Color.WHITE);
        thanhtoan.setFocusPainted(false);
        thanhtoan.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        thanhtoan.setAlignmentX(Component.CENTER_ALIGNMENT);
        thanhtoan.addActionListener(e->{
            if(cartMap.isEmpty()){
                JOptionPane.showMessageDialog(this, "Giỏ hàng trống!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            JDialog dialog = new JDialog(main, "Thanh Toán", true);
            dialog.setUndecorated(true);
            HoaDonPanel hdPanel = new HoaDonPanel(main, this, new HoaDon(0, null, null, null, 0.0, 0, null, null));
            dialog.getContentPane().add(hdPanel);
            dialog.pack();
            dialog.setLocationRelativeTo(main);
            dialog.setVisible(true);

        });

        bottomPanel.add(lbTotal);
        bottomPanel.add(Box.createVerticalStrut(8));
        bottomPanel.add(thanhtoan);

        pay.add(bottomPanel);
        paytruot.add(pay, BorderLayout.CENTER);
        Font fo = new Font("Time New Roman", Font.BOLD, 20);
        content = new JPanel(new BorderLayout(10, 10));
        //JLabel lb = new JLabel("DANH MỤC SẢN PHẨM");
        //lb.setFont(fo);
        //content.add(lb);
        add(content, BorderLayout.CENTER);
    }
    public void addToPay(Component c){
        pay.add(c);
        pay.revalidate();
        pay.repaint();
    }

    private void setTopbar(){
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.white);

        toggle = new JButton("☰");
        //toggle.setBackground(Color.decode("#FF9966"));
        toggle.setFocusPainted(false);
        toggle.addActionListener(e ->{currentTarget = SlideTarget.SIDEBAR;toggleSlide();});
        
        giohang = new JButton("🛒");
        //giohang.setBackground(Color.decode("#FF9966"));
        giohang.setFocusPainted(false);
        giohang.addActionListener(e->{currentTarget = SlideTarget.PAY;toggleSlide();});

        topBar.add(toggle, BorderLayout.WEST);
        topBar.add(giohang, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);
    }
    private void setSidebar(){
        sidebar.add(Box.createVerticalStrut(40));
        home = new JButton("Đơn hàng");
        qlkho = new JButton("Quản lý kho");
        qlkh = new JButton("Quản Lý Khách Hàng");
        qlnv = new JButton("Quản lý nhân viên");
        bc_tk = new JButton("Báo cáo & Thống kê");
        for (JButton b : new JButton[]{home, qlkho, qlkh, qlnv, bc_tk}) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            sidebar.add(b);
            sidebar.add(Box.createVerticalStrut(40));
        }
        qlkho.addActionListener(e -> main.showScreen("KHO"));
        qlkh.addActionListener(e -> main.showScreen("KHACHHANG"));
        qlnv.addActionListener(e -> main.showScreen("NHANVIEN"));
        bc_tk.addActionListener(e -> main.showScreen("REPORT"));

        JButton btnDangXuat = new JButton("Đăng Xuất");
        btnDangXuat.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDangXuat.setBackground(Color.RED); // Màu đỏ để dễ nhận biết
        btnDangXuat.setForeground(Color.WHITE);
        btnDangXuat.setFont(new Font("Arial", Font.BOLD, 12));
        
        btnDangXuat.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(main, 
                    "Bạn có chắc chắn muốn đăng xuất?", 
                    "Xác nhận đăng xuất", 
                    JOptionPane.YES_NO_OPTION);
            
            if (choice == JOptionPane.YES_OPTION) {
                // 1. Xóa thông tin session (nếu cần)
                Session.email = null;
                Session.role = null;
                
                // 2. Đóng cửa sổ Main hiện tại
                main.dispose();
                
                // 3. Mở lại màn hình Đăng Nhập
                new DangNhap().setVisible(true);
            }
        });
        sidebar.add(Box.createVerticalGlue()); // Đẩy nút đăng xuất xuống dưới cùng (nếu muốn)
        sidebar.add(btnDangXuat);
        sidebar.add(Box.createVerticalStrut(20));
    }
    
    public double updateTotal(){
    double total = 0;

        for (Component c : payListPanel.getComponents()) {
            if (c instanceof JPanel panel) {
                for (Component child : panel.getComponents()) {
                    if (child instanceof JPanel info) {
                        for (Component lb : info.getComponents()) {
                            if (lb instanceof JLabel label && label.getForeground().equals(Color.RED)) {
                                String text = label.getText().replace(" VND", "").replace(",", "");
                                total += Double.parseDouble(text);
                            }
                        }
                    }
                }
            }
        }
        lbTotal.setText(String.format("Tổng tiền: %,.0f VND", total));
        return total;
    }

    
    
    private void toggleSlide(){
        if (timer != null && timer.isRunning()) return ;
        timer = new Timer(5, e->{
            if(currentTarget == SlideTarget.SIDEBAR){
                if(isSidebarOpen) sidebarWidth -= 5;
                else sidebarWidth += 5;
                
                if(sidebarWidth <= 0){
                    sidebarWidth = 0;
                    isSidebarOpen = false;
                    timer.stop();
                }
                if(sidebarWidth>= SIDEBAR_MAX){
                    sidebarWidth = SIDEBAR_MAX;
                    isSidebarOpen = true;
                    timer.stop();
                }
                sidebartruot.setPreferredSize(new Dimension(sidebarWidth, getHeight()));
                sidebartruot.revalidate();
        }
        if(currentTarget == SlideTarget.PAY){
            if(isPayOpen) payWidth -= 5;
            else payWidth += 5;

            if(payWidth <= 0){
                payWidth = 0;
                isPayOpen = false;
                timer.stop();
            }
            if(payWidth >= PAY_MAX){
                payWidth = PAY_MAX;
                isPayOpen = true;
                timer.stop();
            }
            paytruot.setPreferredSize(new Dimension(payWidth, getHeight()));
            paytruot.revalidate();
        }
        content.revalidate(); 
        content.repaint();
        });
        timer.start();
    }
    public void loadProductsFromDB(){
        ArrayList<SanPham> products = new SanPhamDAO().selectAll();
        renderProductList(products);
    }
    public void clearCart(){
        cartMap.clear();
        payListPanel.removeAll();
        lbTotal.setText("Tổng tiền: 0 VND");
        payListPanel.revalidate();
        payListPanel.repaint();
    }
    public void renderProductList(ArrayList<SanPham> products){
        productPanel.removeAll();
        if(products.isEmpty()){
            JLabel lbEmpty = new JLabel("Không tìm thấy sản phẩm nào!");
            productPanel.add(lbEmpty);
        }else{
            for (SanPham p : products){
                addProductUI(p);
            }
        }
        productPanel.revalidate();
        productPanel.repaint();
    }
    void addProductUI(SanPham p){
        JPanel productItem = new JPanel();
        productItem.setLayout(new BorderLayout());
        productItem.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 5));
        //productItem.setPreferredSize(new Dimension(180, 230));
        //productItem.setMaximumSize(new Dimension(180, 230));
        productItem.setBackground(Color.WHITE);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.decode("#FF9966"));

        String tenfileanh = p.getHinhAnh();
        ImageIcon icon = null;
        if(tenfileanh != null && !tenfileanh.isEmpty()){
            try{
                icon = new ImageIcon(tenfileanh);
                if(icon.getImageLoadStatus() != MediaTracker.COMPLETE){
                    System.out.println("   --> LỖI: Không tìm thấy file " + tenfileanh + ". Đang dùng ảnh mặc định.");
                    icon = new ImageIcon("default.png");
                }
            }catch (Exception e){
                icon = new ImageIcon("default.png");
            }
        }else{
            icon = new ImageIcon("default.png");
        }
       
        Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH); 
        JLabel lbImage = new JLabel(new ImageIcon(img));
        lbImage.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lbname = new JLabel("<html><center>" + p.getTenSP() + "</center></html>");
        lbname.setFont(new Font("Arial", Font.BOLD, 14));
        lbname.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbname.setHorizontalAlignment(SwingConstants.CENTER); // SETUP cho tên nằm giữa 
        lbname.setVerticalAlignment(SwingConstants.CENTER);
        // Chỉ set chiều cao cố định cho tên, chiều rộng để tự do
        lbname.setPreferredSize(new Dimension(100, 40)); 

        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(lbImage);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(lbname);

        // Panel đáy chứa Giá, Kho, Nút
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.setBackground(Color.WHITE);

        JLabel lbsl = new JLabel("Kho: " + p.getSoLuong());
        lbsl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lbprice = new JLabel(String.format("%,.0f VNĐ", p.getGiaBan()));
        lbprice.setForeground(Color.red);
        lbprice.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton addgio = new JButton("+");
        addgio.setAlignmentX(Component.CENTER_ALIGNMENT);
        addgio.setBackground(new Color(255, 153, 102));
        addgio.setForeground(Color.WHITE);
        addgio.setFocusPainted(false);
        
        // Fix lỗi nút bấm: Khi ô quá nhỏ, nút bấm cần margin để không dính lề
        addgio.setMargin(new Insets(2, 10, 2, 10)); 

        addgio.addActionListener(e -> {
            addToCart(p);
        });

        southPanel.add(lbsl);
        southPanel.add(lbprice);
        southPanel.add(Box.createVerticalStrut(5));
        southPanel.add(addgio);
        southPanel.add(Box.createVerticalStrut(10));

        // Add vào productItem
        productItem.add(centerPanel, BorderLayout.CENTER);
        productItem.add(southPanel, BorderLayout.SOUTH);

        productPanel.add(productItem);

    }
    public void addToCart(SanPham p) {
        if(!isPayOpen){
            currentTarget = SlideTarget.PAY;
            toggleSlide();
        }
        String maSP = p.getMaSP();

        // ===== ĐÃ CÓ TRONG GIỎ =====
        if (cartMap.containsKey(maSP)) {
            CartItem item = cartMap.get(maSP);
            item.quantity++;
            item.txtSL.setText(String.valueOf(item.quantity));
            item.lbPrice.setText(String.format("%,.0f VND",
            item.quantity * p.getGiaBan()));
            updateTotal();
            return;
        }

    // ===== PANEL =====
    JPanel payPanel = new JPanel(new BorderLayout(5, 5));
    payPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    payPanel.setBackground(Color.WHITE);
    payPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

    JLabel lbName = new JLabel(p.getTenSP(), SwingConstants.CENTER);
    lbName.setFont(new Font("Time New Roman", Font.BOLD, 15));

    JLabel lbPrice = new JLabel(
            String.format("%,.0f VND", p.getGiaBan()),
            SwingConstants.CENTER
    );
    lbPrice.setForeground(Color.RED);

    // Thông tin sp trong giỏ 
    JPanel infoPanel = new JPanel();
    infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
    infoPanel.setBackground(Color.WHITE);
    infoPanel.add(lbName);
    infoPanel.add(lbPrice);

    JTextField txtSL = new JTextField("1");
    txtSL.setHorizontalAlignment(JTextField.CENTER);
    txtSL.setPreferredSize(new Dimension(40, 28));

    JButton btPlus = new JButton("+");
    btPlus.setBackground(Color.decode("#FF9966"));
    btPlus.setFocusPainted(false);

    JButton btMinus = new JButton("-");
    btMinus.setBackground(Color.decode("#FF9966"));
    btMinus.setFocusPainted(false);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
    buttonPanel.setBackground(Color.WHITE);
    buttonPanel.add(btPlus);
    buttonPanel.add(txtSL);
    buttonPanel.add(btMinus);

    payPanel.add(infoPanel, BorderLayout.CENTER);
    payPanel.add(buttonPanel, BorderLayout.SOUTH);

    CartItem item = new CartItem(p, payPanel, lbPrice, txtSL);
    cartMap.put(maSP, item);

    // ===== CẬP NHẬT GIÁ (KHÔNG SET TEXT Ở DOCUMENT) =====
    Runnable updatePriceOnly = () -> {
        try {
            int sl = Integer.parseInt(txtSL.getText());
            if (sl < 1) return;

            item.quantity = sl;
            lbPrice.setText(String.format("%,.0f VND",
                    sl * p.getGiaBan()));
            updateTotal();
        } catch (NumberFormatException ignored) {}
    };

    // ===== NÚT + =====
    btPlus.addActionListener(e -> {
        item.quantity++;
        txtSL.setText(String.valueOf(item.quantity));
        updatePriceOnly.run();
    });

    // ===== NÚT - =====
    btMinus.addActionListener(e -> {
        if (item.quantity > 1) {
            item.quantity--;
            txtSL.setText(String.valueOf(item.quantity));
            updatePriceOnly.run();
        } else {
            payListPanel.remove(payPanel);
            cartMap.remove(maSP);
            updateTotal();
            payListPanel.revalidate();
            payListPanel.repaint();
        }
    });

    // ===== NHẬP TRỰC TIẾP (CHỈ ĐỌC) =====
    txtSL.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            updatePriceOnly.run();
        }
        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            updatePriceOnly.run();
        }
        public void changedUpdate(javax.swing.event.DocumentEvent e) {}
    });

    payListPanel.add(payPanel);
    payListPanel.revalidate();
    payListPanel.repaint();
    updateTotal();
}
public void showHistoryDialog() {
    JDialog d = new JDialog(main, "Lịch Sử Bán Hàng", true);
    d.setSize(1000, 500);
    d.setLocationRelativeTo(main);
    
    // Tạo bảng
    String[] columns = {"Mã HĐ", "Thời Gian", "Nhân Viên","SDT", "Khách Hàng","Sản phẩm đã mua", "Tổng Tiền", "PTTT"};
    javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(columns, 0);
    JTable table = new JTable(model);
    table.setRowHeight(25);
    table.setFont(new Font("Arial", Font.PLAIN, 14));

    table.getColumnModel().getColumn(3).setPreferredWidth(150);
    table.getColumnModel().getColumn(5).setPreferredWidth(300);
    
    String filterID =null;
    nhanvien currentUser = main.getNhanVien();
    if(currentUser != null){
        if(!"admin".equalsIgnoreCase(currentUser.getRole())){
            filterID = String.valueOf(currentUser.getID_employee());
        }
    }
    // Lấy dữ liệu từ SQL lên
    ArrayList<HoaDon> list = HoaDon.getAllHoaDon(filterID);
    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    
    for (HoaDon hd : list) {
        model.addRow(new Object[]{
            hd.getMaHD(),
            sdf.format(hd.getNgayTao()),
            hd.getMaNV() == 0 ? "N/A" : hd.getMaNV(),
            hd.getSdt(),
            hd.getTenKH(),
            hd.getDsSanPham(),
            String.format("%,.0f VNĐ", hd.getTongTien()),
            hd.getPhuongThuc()
        });
    }
    
    JScrollPane sc = new JScrollPane(table);
    d.add(sc);
    d.setVisible(true);
}

 
}
class CartItem {
    SanPham product;
    int quantity;
    JPanel payPanel;
    JLabel lbPrice;
    JTextField txtSL;

    CartItem(SanPham product, JPanel payPanel, JLabel lbPrice, JTextField txtSL) {
        this.product = product;
        this.quantity = 1;
        this.payPanel = payPanel;
        this.lbPrice = lbPrice;
        this.txtSL = txtSL;
    }
}

