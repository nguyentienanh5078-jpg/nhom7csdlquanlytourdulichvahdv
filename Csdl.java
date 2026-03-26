package csdl;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
class HotelPanel extends JPanel {
    private Connection conn;
    private DefaultTableModel model;
    private JTable table;
    private JTextField txtHID, txtHName, txtCIDate, txtCODate, txtHAddress;

    public HotelPanel(Connection conn) {
        this.conn = conn;
        setLayout(new BorderLayout());

        // Form nhập liệu
        JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));
        form.add(new JLabel("HID:"));
        txtHID = new JTextField(); form.add(txtHID);
        form.add(new JLabel("Name:"));
        txtHName = new JTextField(); form.add(txtHName);
        form.add(new JLabel("CheckIn Date (yyyy-MM-dd):"));
        txtCIDate = new JTextField(); form.add(txtCIDate);
        form.add(new JLabel("CheckOut Date (yyyy-MM-dd):"));
        txtCODate = new JTextField(); form.add(txtCODate);
        form.add(new JLabel("Address:"));
        txtHAddress = new JTextField(); form.add(txtHAddress);

        // Nút CRUD
        JPanel buttons = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");
        buttons.add(btnAdd); buttons.add(btnUpdate);
        buttons.add(btnDelete); buttons.add(btnClear);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        // Bảng dữ liệu
        String[] cols = {"HID", "Name", "CIDate", "CODate", "Address"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData();

        // Sự kiện nút
        btnAdd.addActionListener(e -> addData());
        btnUpdate.addActionListener(e -> updateData());
        btnDelete.addActionListener(e -> deleteData());
        btnClear.addActionListener(e -> clearForm());

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtHID.setText(model.getValueAt(row, 0).toString());
                txtHName.setText(model.getValueAt(row, 1).toString());
                txtCIDate.setText(model.getValueAt(row, 2).toString());
                txtCODate.setText(model.getValueAt(row, 3).toString());
                txtHAddress.setText(model.getValueAt(row, 4).toString());
            }
        });
    }

    private void loadData() {
        model.setRowCount(0);
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM HOTEL")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("HID"),
                    rs.getString("HName"),
                    rs.getString("CIDate"),
                    rs.getString("CODate"),
                    rs.getString("HAddress")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void addData() {
        try {
            String sql = "INSERT INTO HOTEL VALUES(?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtHID.getText());
            ps.setString(2, txtHName.getText());
            ps.setString(3, txtCIDate.getText());
            ps.setString(4, txtCODate.getText());
            ps.setString(5, txtHAddress.getText());
            ps.executeUpdate();
            loadData();
            clearForm();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void updateData() {
        try {
            String sql = "UPDATE HOTEL SET HName=?, CIDate=?, CODate=?, HAddress=? WHERE HID=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtHName.getText());
            ps.setString(2, txtCIDate.getText());
            ps.setString(3, txtCODate.getText());
            ps.setString(4, txtHAddress.getText());
            ps.setString(5, txtHID.getText());
            ps.executeUpdate();
            loadData();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void deleteData() {
        try {
            String sql = "DELETE FROM HOTEL WHERE HID=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtHID.getText());
            ps.executeUpdate();
            loadData();
            clearForm();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void clearForm() {
        txtHID.setText(""); txtHName.setText("");
        txtCIDate.setText(""); txtCODate.setText(""); txtHAddress.setText("");
    }
}
class TourPanel extends JPanel {
    private Connection conn;
    private DefaultTableModel model;
    private JTable table;
    private JTextField txtTID, txtTName, txtPrice, txtSDate, txtEDate, txtHID;

    public TourPanel(Connection conn) {
        this.conn = conn;
        setLayout(new BorderLayout());

        // Form nhập liệu
        JPanel form = new JPanel(new GridLayout(6, 2, 5, 5));
        form.add(new JLabel("TID:")); txtTID = new JTextField(); form.add(txtTID);
        form.add(new JLabel("Name:")); txtTName = new JTextField(); form.add(txtTName);
        form.add(new JLabel("Price:")); txtPrice = new JTextField(); form.add(txtPrice);
        form.add(new JLabel("Start Date (yyyy-MM-dd):")); txtSDate = new JTextField(); form.add(txtSDate);
        form.add(new JLabel("End Date (yyyy-MM-dd):")); txtEDate = new JTextField(); form.add(txtEDate);
        form.add(new JLabel("Hotel ID:")); txtHID = new JTextField(); form.add(txtHID);

        // Nút CRUD
        JPanel buttons = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");
        buttons.add(btnAdd); buttons.add(btnUpdate);
        buttons.add(btnDelete); buttons.add(btnClear);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        // Bảng dữ liệu
        String[] cols = {"TID", "Name", "Price", "SDate", "EDate", "HID"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Load dữ liệu ban đầu
        loadData();

        // Sự kiện nút
        btnAdd.addActionListener(e -> addData());
        btnUpdate.addActionListener(e -> updateData());
        btnDelete.addActionListener(e -> deleteData());
        btnClear.addActionListener(e -> clearForm());

        // Click bảng -> load form
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtTID.setText(model.getValueAt(row, 0).toString());
                txtTName.setText(model.getValueAt(row, 1).toString());
                txtPrice.setText(model.getValueAt(row, 2).toString());
                txtSDate.setText(model.getValueAt(row, 3).toString());
                txtEDate.setText(model.getValueAt(row, 4).toString());
                txtHID.setText(model.getValueAt(row, 5).toString());
            }
        });
    }

    // Load dữ liệu từ DB
    private void loadData() {
        model.setRowCount(0);
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM TOUR")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("TID"),
                    rs.getString("TName"),
                    rs.getString("Price"),
                    rs.getString("SDate"),
                    rs.getString("EDate"),
                    rs.getString("HID")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Thêm dữ liệu
    private void addData() {
        try {
            String sql = "INSERT INTO TOUR VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtTID.getText());
            ps.setString(2, txtTName.getText());
            ps.setBigDecimal(3, new java.math.BigDecimal(txtPrice.getText()));
            ps.setDate(4, java.sql.Date.valueOf(txtSDate.getText()));
            ps.setDate(5, java.sql.Date.valueOf(txtEDate.getText()));
            ps.setString(6, txtHID.getText());
            ps.executeUpdate();
            loadData();
            clearForm();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Cập nhật dữ liệu
    private void updateData() {
        try {
            String sql = "UPDATE TOUR SET TName=?, Price=?, SDate=?, EDate=?, HID=? WHERE TID=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtTName.getText());
            ps.setBigDecimal(2, new java.math.BigDecimal(txtPrice.getText()));
            ps.setDate(3, java.sql.Date.valueOf(txtSDate.getText()));
            ps.setDate(4, java.sql.Date.valueOf(txtEDate.getText()));
            ps.setString(5, txtHID.getText());
            ps.setString(6, txtTID.getText());
            ps.executeUpdate();
            loadData();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Xóa dữ liệu
    private void deleteData() {
        try {
            String sql = "DELETE FROM TOUR WHERE TID=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtTID.getText());
            ps.executeUpdate();
            loadData();
            clearForm();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Xóa trắng form
    private void clearForm() {
        txtTID.setText(""); txtTName.setText("");
        txtPrice.setText(""); txtSDate.setText("");
        txtEDate.setText(""); txtHID.setText("");
    }
}

class CustomerPanel extends JPanel {
    private Connection conn;
    private DefaultTableModel model;
    private JTable table;
    private JTextField txtCID, txtName, txtAddress, txtPhone;

    public CustomerPanel(Connection conn) {
        this.conn = conn;
        setLayout(new BorderLayout());

        // Form nhập liệu
        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.add(new JLabel("CID:"));
        txtCID = new JTextField(); form.add(txtCID);
        form.add(new JLabel("Name:"));
        txtName = new JTextField(); form.add(txtName);
        form.add(new JLabel("Address:"));
        txtAddress = new JTextField(); form.add(txtAddress);
        form.add(new JLabel("Phone:"));
        txtPhone = new JTextField(); form.add(txtPhone);

        // Nút CRUD
        JPanel buttons = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");
        buttons.add(btnAdd); buttons.add(btnUpdate);
        buttons.add(btnDelete); buttons.add(btnClear);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);

        add(top, BorderLayout.NORTH);

        // Bảng dữ liệu
        String[] cols = {"CID", "Name", "Address", "Phone"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Load dữ liệu
        loadData();

        // Sự kiện nút
        btnAdd.addActionListener(e -> addData());
        btnUpdate.addActionListener(e -> updateData());
        btnDelete.addActionListener(e -> deleteData());
        btnClear.addActionListener(e -> clearForm());

        // Click bảng -> load form
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtCID.setText(model.getValueAt(row, 0).toString());
                txtName.setText(model.getValueAt(row, 1).toString());
                txtAddress.setText(model.getValueAt(row, 2).toString());
                txtPhone.setText(model.getValueAt(row, 3).toString());
            }
        });
    }

    private void loadData() {
        model.setRowCount(0);
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM CUSTOMER")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("CID"),
                    rs.getString("CName"),
                    rs.getString("CAddress"),
                    rs.getString("CPN")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void addData() {
        try {
            String sql = "INSERT INTO CUSTOMER VALUES(?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtCID.getText());
            ps.setString(2, txtName.getText());
            ps.setString(3, txtAddress.getText());
            ps.setString(4, txtPhone.getText());
            ps.executeUpdate();
            loadData();
            clearForm();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void updateData() {
        try {
            String sql = "UPDATE CUSTOMER SET CName=?, CAddress=?, CPN=? WHERE CID=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtName.getText());
            ps.setString(2, txtAddress.getText());
            ps.setString(3, txtPhone.getText());
            ps.setString(4, txtCID.getText());
            ps.executeUpdate();
            loadData();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void deleteData() {
        try {
            String sql = "DELETE FROM CUSTOMER WHERE CID=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtCID.getText());
            ps.executeUpdate();
            loadData();
            clearForm();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void clearForm() {
        txtCID.setText(""); txtName.setText("");
        txtAddress.setText(""); txtPhone.setText("");
    }
}
class AgencyPanel extends JPanel {
    private Connection conn;
    private DefaultTableModel model;
    private JTable table;
    private JTextField txtAID, txtAPN, txtAAddress;

    public AgencyPanel(Connection conn) {
        this.conn = conn;
        setLayout(new BorderLayout());

        // Form nhập liệu
        JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
        form.add(new JLabel("AID:")); txtAID = new JTextField(); form.add(txtAID);
        form.add(new JLabel("Phone:")); txtAPN = new JTextField(); form.add(txtAPN);
        form.add(new JLabel("Address:")); txtAAddress = new JTextField(); form.add(txtAAddress);

        // Nút CRUD
        JPanel buttons = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");
        buttons.add(btnAdd); buttons.add(btnUpdate);
        buttons.add(btnDelete); buttons.add(btnClear);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        // Bảng dữ liệu
        String[] cols = {"AID", "Phone", "Address"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Load dữ liệu ban đầu
        loadData();

        // Sự kiện nút
        btnAdd.addActionListener(e -> addData());
        btnUpdate.addActionListener(e -> updateData());
        btnDelete.addActionListener(e -> deleteData());
        btnClear.addActionListener(e -> clearForm());

        // Click bảng -> load form
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtAID.setText(model.getValueAt(row, 0).toString());
                txtAPN.setText(model.getValueAt(row, 1).toString());
                txtAAddress.setText(model.getValueAt(row, 2).toString());
            }
        });
    }

    // Load dữ liệu từ DB
    private void loadData() {
        model.setRowCount(0);
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM AGENCY")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("AID"),
                    rs.getString("APN"),
                    rs.getString("AAddress")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Thêm dữ liệu
    private void addData() {
        try {
            String sql = "INSERT INTO AGENCY VALUES(?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtAID.getText());
            ps.setString(2, txtAPN.getText());
            ps.setString(3, txtAAddress.getText());
            ps.executeUpdate();
            loadData();
            clearForm();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Cập nhật dữ liệu
    private void updateData() {
        try {
            String sql = "UPDATE AGENCY SET APN=?, AAddress=? WHERE AID=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtAPN.getText());
            ps.setString(2, txtAAddress.getText());
            ps.setString(3, txtAID.getText());
            ps.executeUpdate();
            loadData();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Xóa dữ liệu
    private void deleteData() {
        try {
            String sql = "DELETE FROM AGENCY WHERE AID=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtAID.getText());
            ps.executeUpdate();
            loadData();
            clearForm();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Xóa trắng form
    private void clearForm() {
        txtAID.setText(""); txtAPN.setText(""); txtAAddress.setText("");
    }
}
class ReceiptPanel extends JPanel {
    private Connection conn;
    private DefaultTableModel model;
    private JTable table;
    private JTextField txtRID, txtRDate, txtSumR, txtCID, txtAID;

    public ReceiptPanel(Connection conn) {
        this.conn = conn;
        setLayout(new BorderLayout());

        // Form nhập liệu
        JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));
        form.add(new JLabel("RID:")); txtRID = new JTextField(); form.add(txtRID);
        form.add(new JLabel("Date (yyyy-MM-dd):")); txtRDate = new JTextField(); form.add(txtRDate);
        form.add(new JLabel("Sum:")); txtSumR = new JTextField(); form.add(txtSumR);
        form.add(new JLabel("Customer ID:")); txtCID = new JTextField(); form.add(txtCID);
        form.add(new JLabel("Agency ID:")); txtAID = new JTextField(); form.add(txtAID);

        // Nút CRUD
        JPanel buttons = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");
        buttons.add(btnAdd); buttons.add(btnUpdate);
        buttons.add(btnDelete); buttons.add(btnClear);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        // Bảng dữ liệu
        String[] cols = {"RID", "Date", "Sum", "CustomerID", "AgencyID"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Load dữ liệu ban đầu
        loadData();

        // Sự kiện nút
        btnAdd.addActionListener(e -> addData());
        btnUpdate.addActionListener(e -> updateData());
        btnDelete.addActionListener(e -> deleteData());
        btnClear.addActionListener(e -> clearForm());

        // Click bảng -> load form
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtRID.setText(model.getValueAt(row, 0).toString());
                txtRDate.setText(model.getValueAt(row, 1).toString());
                txtSumR.setText(model.getValueAt(row, 2).toString());
                txtCID.setText(model.getValueAt(row, 3).toString());
                txtAID.setText(model.getValueAt(row, 4).toString());
            }
        });
    }

    // Load dữ liệu từ DB
    private void loadData() {
        model.setRowCount(0);
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM RECEIPT")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("RID"),
                    rs.getString("RDate"),
                    rs.getString("SumR"),
                    rs.getString("CID"),
                    rs.getString("AID")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Thêm dữ liệu
    private void addData() {
        try {
            String sql = "INSERT INTO RECEIPT VALUES(?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtRID.getText());
            ps.setDate(2, java.sql.Date.valueOf(txtRDate.getText()));
            ps.setBigDecimal(3, new java.math.BigDecimal(txtSumR.getText()));
            ps.setString(4, txtCID.getText());
            ps.setString(5, txtAID.getText());
            ps.executeUpdate();
            loadData();
            clearForm();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Cập nhật dữ liệu
    private void updateData() {
        try {
            String sql = "UPDATE RECEIPT SET RDate=?, SumR=?, CID=?, AID=? WHERE RID=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDate(1, java.sql.Date.valueOf(txtRDate.getText()));
            ps.setBigDecimal(2, new java.math.BigDecimal(txtSumR.getText()));
            ps.setString(3, txtCID.getText());
            ps.setString(4, txtAID.getText());
            ps.setString(5, txtRID.getText());
            ps.executeUpdate();
            loadData();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Xóa dữ liệu
    private void deleteData() {
        try {
            String sql = "DELETE FROM RECEIPT WHERE RID=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtRID.getText());
            ps.executeUpdate();
            loadData();
            clearForm();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Xóa trắng form
    private void clearForm() {
        txtRID.setText(""); txtRDate.setText("");
        txtSumR.setText(""); txtCID.setText(""); txtAID.setText("");
    }
}

class TourGuidePanel extends JPanel {
    private Connection conn;
    private DefaultTableModel model;
    private JTable table;
    private JTextField txtTGID, txtTGName, txtTGPN, txtAID;

    public TourGuidePanel(Connection conn) {
        this.conn = conn;
        setLayout(new BorderLayout());

        // Form nhập liệu
        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.add(new JLabel("TGID:")); txtTGID = new JTextField(); form.add(txtTGID);
        form.add(new JLabel("Name:")); txtTGName = new JTextField(); form.add(txtTGName);
        form.add(new JLabel("Phone:")); txtTGPN = new JTextField(); form.add(txtTGPN);
        form.add(new JLabel("Agency ID:")); txtAID = new JTextField(); form.add(txtAID);

        // Nút CRUD
        JPanel buttons = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");
        buttons.add(btnAdd); buttons.add(btnUpdate);
        buttons.add(btnDelete); buttons.add(btnClear);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        // Bảng dữ liệu
        String[] cols = {"TGID", "Name", "Phone", "AgencyID"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Load dữ liệu ban đầu
        loadData();

        // Sự kiện nút
        btnAdd.addActionListener(e -> addData());
        btnUpdate.addActionListener(e -> updateData());
        btnDelete.addActionListener(e -> deleteData());
        btnClear.addActionListener(e -> clearForm());

        // Click bảng -> load form
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtTGID.setText(model.getValueAt(row, 0).toString());
                txtTGName.setText(model.getValueAt(row, 1).toString());
                txtTGPN.setText(model.getValueAt(row, 2).toString());
                txtAID.setText(model.getValueAt(row, 3).toString());
            }
        });
    }

    // Load dữ liệu từ DB
    private void loadData() {
        model.setRowCount(0);
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM TOURGUIDE")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("TGID"),
                    rs.getString("TGName"),
                    rs.getString("TGPN"),
                    rs.getString("AID")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Thêm dữ liệu
    private void addData() {
        try {
            String sql = "INSERT INTO TOURGUIDE VALUES(?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtTGID.getText());
            ps.setString(2, txtTGName.getText());
            ps.setString(3, txtTGPN.getText());
            ps.setString(4, txtAID.getText());
            ps.executeUpdate();
            loadData();
            clearForm();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Cập nhật dữ liệu
    private void updateData() {
        try {
            String sql = "UPDATE TOURGUIDE SET TGName=?, TGPN=?, AID=? WHERE TGID=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtTGName.getText());
            ps.setString(2, txtTGPN.getText());
            ps.setString(3, txtAID.getText());
            ps.setString(4, txtTGID.getText());
            ps.executeUpdate();
            loadData();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Xóa dữ liệu
    private void deleteData() {
        try {
            String sql = "DELETE FROM TOURGUIDE WHERE TGID=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtTGID.getText());
            ps.executeUpdate();
            loadData();
            clearForm();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Xóa trắng form
    private void clearForm() {
        txtTGID.setText(""); txtTGName.setText("");
        txtTGPN.setText(""); txtAID.setText("");
    }
}
class SalaryPanel extends JPanel {
    private Connection conn;
    private DefaultTableModel model;
    private JTable table;
    private JTextField txtSIDTG, txtTGS, txtTGID;

    public SalaryPanel(Connection conn) {
        this.conn = conn;
        setLayout(new BorderLayout());

        // Form nhập liệu
        JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
        form.add(new JLabel("Salary ID (SIDTG):")); txtSIDTG = new JTextField(); form.add(txtSIDTG);
        form.add(new JLabel("Salary (TGS):")); txtTGS = new JTextField(); form.add(txtTGS);
        form.add(new JLabel("TourGuide ID (TGID):")); txtTGID = new JTextField(); form.add(txtTGID);

        // Nút CRUD
        JPanel buttons = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");
        buttons.add(btnAdd); buttons.add(btnUpdate);
        buttons.add(btnDelete); buttons.add(btnClear);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        // Bảng dữ liệu
        String[] cols = {"SIDTG", "Salary", "TGID"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Load dữ liệu ban đầu
        loadData();

        // Sự kiện nút
        btnAdd.addActionListener(e -> addData());
        btnUpdate.addActionListener(e -> updateData());
        btnDelete.addActionListener(e -> deleteData());
        btnClear.addActionListener(e -> clearForm());

        // Click bảng -> load form
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtSIDTG.setText(model.getValueAt(row, 0).toString());
                txtTGS.setText(model.getValueAt(row, 1).toString());
                txtTGID.setText(model.getValueAt(row, 2).toString());
            }
        });
    }

    // Load dữ liệu từ DB
    private void loadData() {
        model.setRowCount(0);
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM SALARY")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("SIDTG"),
                    rs.getString("TGS"),
                    rs.getString("TGID")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Thêm dữ liệu
    private void addData() {
        try {
            String sql = "INSERT INTO SALARY VALUES(?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtSIDTG.getText());
            ps.setBigDecimal(2, new java.math.BigDecimal(txtTGS.getText()));
            ps.setString(3, txtTGID.getText());
            ps.executeUpdate();
            loadData();
            clearForm();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Cập nhật dữ liệu
    private void updateData() {
        try {
            String sql = "UPDATE SALARY SET TGS=?, TGID=? WHERE SIDTG=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setBigDecimal(1, new java.math.BigDecimal(txtTGS.getText()));
            ps.setString(2, txtTGID.getText());
            ps.setString(3, txtSIDTG.getText());
            ps.executeUpdate();
            loadData();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Xóa dữ liệu
    private void deleteData() {
        try {
            String sql = "DELETE FROM SALARY WHERE SIDTG=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtSIDTG.getText());
            ps.executeUpdate();
            loadData();
            clearForm();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Xóa trắng form
    private void clearForm() {
        txtSIDTG.setText(""); txtTGS.setText(""); txtTGID.setText("");
    }
}
class LocationPanel extends JPanel {
    private Connection conn;
    private DefaultTableModel model;
    private JTable table;
    private JTextField txtLID, txtLName, txtProvince;

    public LocationPanel(Connection conn) {
        this.conn = conn;
        setLayout(new BorderLayout());

        // Form nhập liệu
        JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
        form.add(new JLabel("Location ID (LID):")); txtLID = new JTextField(); form.add(txtLID);
        form.add(new JLabel("Location Name (LName):")); txtLName = new JTextField(); form.add(txtLName);
        form.add(new JLabel("Province:")); txtProvince = new JTextField(); form.add(txtProvince);

        // Nút CRUD
        JPanel buttons = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");
        buttons.add(btnAdd); buttons.add(btnUpdate);
        buttons.add(btnDelete); buttons.add(btnClear);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        // Bảng dữ liệu
        String[] cols = {"LID", "Name", "Province"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Load dữ liệu ban đầu
        loadData();

        // Sự kiện nút
        btnAdd.addActionListener(e -> addData());
        btnUpdate.addActionListener(e -> updateData());
        btnDelete.addActionListener(e -> deleteData());
        btnClear.addActionListener(e -> clearForm());

        // Click bảng -> load form
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtLID.setText(model.getValueAt(row, 0).toString());
                txtLName.setText(model.getValueAt(row, 1).toString());
                txtProvince.setText(model.getValueAt(row, 2).toString());
            }
        });
    }

    // Load dữ liệu từ DB
    private void loadData() {
        model.setRowCount(0);
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM TLOCATION")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("LID"),
                    rs.getString("LName"),
                    rs.getString("Province")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Thêm dữ liệu
    private void addData() {
        try {
            String sql = "INSERT INTO TLOCATION VALUES(?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtLID.getText());
            ps.setString(2, txtLName.getText());
            ps.setString(3, txtProvince.getText());
            ps.executeUpdate();
            loadData();
            clearForm();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Cập nhật dữ liệu
    private void updateData() {
        try {
            String sql = "UPDATE TLOCATION SET LName=?, Province=? WHERE LID=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtLName.getText());
            ps.setString(2, txtProvince.getText());
            ps.setString(3, txtLID.getText());
            ps.executeUpdate();
            loadData();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Xóa dữ liệu
    private void deleteData() {
        try {
            String sql = "DELETE FROM TLOCATION WHERE LID=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtLID.getText());
            ps.executeUpdate();
            loadData();
            clearForm();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Xóa trắng form
    private void clearForm() {
        txtLID.setText(""); txtLName.setText(""); txtProvince.setText("");
    }
}
class VehiclePanel extends JPanel {
    private Connection conn;
    private DefaultTableModel model;
    private JTable table;
    private JTextField txtLicensePlate, txtVType;

    public VehiclePanel(Connection conn) {
        this.conn = conn;
        setLayout(new BorderLayout());

        // Form nhập liệu
        JPanel form = new JPanel(new GridLayout(2, 2, 5, 5));
        form.add(new JLabel("License Plate:")); txtLicensePlate = new JTextField(); form.add(txtLicensePlate);
        form.add(new JLabel("Vehicle Type:")); txtVType = new JTextField(); form.add(txtVType);

        // Nút CRUD
        JPanel buttons = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");
        buttons.add(btnAdd); buttons.add(btnUpdate);
        buttons.add(btnDelete); buttons.add(btnClear);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        // Bảng dữ liệu
        String[] cols = {"LicensePlate", "VehicleType"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Load dữ liệu ban đầu
        loadData();

        // Sự kiện nút
        btnAdd.addActionListener(e -> addData());
        btnUpdate.addActionListener(e -> updateData());
        btnDelete.addActionListener(e -> deleteData());
        btnClear.addActionListener(e -> clearForm());

        // Click bảng -> load form
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtLicensePlate.setText(model.getValueAt(row, 0).toString());
                txtVType.setText(model.getValueAt(row, 1).toString());
            }
        });
    }

    // Load dữ liệu từ DB
    private void loadData() {
        model.setRowCount(0);
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM VEHICLE")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("LicensePlate"),
                    rs.getString("VType")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Thêm dữ liệu
    private void addData() {
        try {
            String sql = "INSERT INTO VEHICLE VALUES(?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtLicensePlate.getText());
            ps.setString(2, txtVType.getText());
            ps.executeUpdate();
            loadData();
            clearForm();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Cập nhật dữ liệu
    private void updateData() {
        try {
            String sql = "UPDATE VEHICLE SET VType=? WHERE LicensePlate=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtVType.getText());
            ps.setString(2, txtLicensePlate.getText());
            ps.executeUpdate();
            loadData();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Xóa dữ liệu
    private void deleteData() {
        try {
            String sql = "DELETE FROM VEHICLE WHERE LicensePlate=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtLicensePlate.getText());
            ps.executeUpdate();
            loadData();
            clearForm();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Xóa trắng form
    private void clearForm() {
        txtLicensePlate.setText(""); txtVType.setText("");
    }
}
class BookingPanel extends JPanel {
    private Connection conn;
    private DefaultTableModel model;
    private JTable table;
    private JTextField txtTID, txtCID;

    public BookingPanel(Connection conn) {
        this.conn = conn;
        setLayout(new BorderLayout());

        // Form nhập liệu
        JPanel form = new JPanel(new GridLayout(2, 2, 5, 5));
        form.add(new JLabel("Tour ID (TID):")); txtTID = new JTextField(); form.add(txtTID);
        form.add(new JLabel("Customer ID (CID):")); txtCID = new JTextField(); form.add(txtCID);

        // Nút CRUD
        JPanel buttons = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Add");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");
        buttons.add(btnAdd); buttons.add(btnDelete); buttons.add(btnClear);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        // Bảng dữ liệu
        String[] cols = {"TID", "CID"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Load dữ liệu ban đầu
        loadData();

        // Sự kiện nút
        btnAdd.addActionListener(e -> addData());
        btnDelete.addActionListener(e -> deleteData());
        btnClear.addActionListener(e -> clearForm());

        // Click bảng -> load form
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtTID.setText(model.getValueAt(row, 0).toString());
                txtCID.setText(model.getValueAt(row, 1).toString());
            }
        });
    }

    // Load dữ liệu từ DB
    private void loadData() {
        model.setRowCount(0);
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM BOOKING")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("TID"),
                    rs.getString("CID")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Thêm dữ liệu
    private void addData() {
        try {
            String sql = "INSERT INTO BOOKING VALUES(?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtTID.getText());
            ps.setString(2, txtCID.getText());
            ps.executeUpdate();
            loadData();
            clearForm();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Xóa dữ liệu
    private void deleteData() {
        try {
            String sql = "DELETE FROM BOOKING WHERE TID=? AND CID=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtTID.getText());
            ps.setString(2, txtCID.getText());
            ps.executeUpdate();
            loadData();
            clearForm();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Xóa trắng form
    private void clearForm() {
        txtTID.setText(""); txtCID.setText("");
    }
}
class GobyPanel extends JPanel {
    private Connection conn;
    private DefaultTableModel model;
    private JTable table;
    private JTextField txtTID, txtLicensePlate;

    public GobyPanel(Connection conn) {
        this.conn = conn;
        setLayout(new BorderLayout());

        // Form nhập liệu
        JPanel form = new JPanel(new GridLayout(2, 2, 5, 5));
        form.add(new JLabel("Tour ID (TID):")); txtTID = new JTextField(); form.add(txtTID);
        form.add(new JLabel("License Plate:")); txtLicensePlate = new JTextField(); form.add(txtLicensePlate);

        // Nút CRUD
        JPanel buttons = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Add");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");
        buttons.add(btnAdd); buttons.add(btnDelete); buttons.add(btnClear);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        // Bảng dữ liệu
        String[] cols = {"TID", "LicensePlate"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Load dữ liệu ban đầu
        loadData();

        // Sự kiện nút
        btnAdd.addActionListener(e -> addData());
        btnDelete.addActionListener(e -> deleteData());
        btnClear.addActionListener(e -> clearForm());

        // Click bảng -> load form
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtTID.setText(model.getValueAt(row, 0).toString());
                txtLicensePlate.setText(model.getValueAt(row, 1).toString());
            }
        });
    }

    // Load dữ liệu từ DB
    private void loadData() {
        model.setRowCount(0);
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM GOBY")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("TID"),
                    rs.getString("LicensePlate")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Thêm dữ liệu
    private void addData() {
        try {
            String sql = "INSERT INTO GOBY VALUES(?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtTID.getText());
            ps.setString(2, txtLicensePlate.getText());
            ps.executeUpdate();
            loadData();
            clearForm();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Xóa dữ liệu
    private void deleteData() {
        try {
            String sql = "DELETE FROM GOBY WHERE TID=? AND LicensePlate=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtTID.getText());
            ps.setString(2, txtLicensePlate.getText());
            ps.executeUpdate();
            loadData();
            clearForm();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Xóa trắng form
    private void clearForm() {
        txtTID.setText(""); txtLicensePlate.setText("");
    }
}
class AssignPanel extends JPanel {
    private Connection conn;
    private DefaultTableModel model;
    private JTable table;
    private JTextField txtTID, txtTGID;

    public AssignPanel(Connection conn) {
        this.conn = conn;
        setLayout(new BorderLayout());

        // Form nhập liệu
        JPanel form = new JPanel(new GridLayout(2, 2, 5, 5));
        form.add(new JLabel("Tour ID (TID):")); txtTID = new JTextField(); form.add(txtTID);
        form.add(new JLabel("TourGuide ID (TGID):")); txtTGID = new JTextField(); form.add(txtTGID);

        // Nút CRUD
        JPanel buttons = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Add");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");
        buttons.add(btnAdd); buttons.add(btnDelete); buttons.add(btnClear);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        // Bảng dữ liệu
        String[] cols = {"TID", "TGID"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Load dữ liệu ban đầu
        loadData();

        // Sự kiện nút
        btnAdd.addActionListener(e -> addData());
        btnDelete.addActionListener(e -> deleteData());
        btnClear.addActionListener(e -> clearForm());

        // Click bảng -> load form
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtTID.setText(model.getValueAt(row, 0).toString());
                txtTGID.setText(model.getValueAt(row, 1).toString());
            }
        });
    }

    // Load dữ liệu từ DB
    private void loadData() {
        model.setRowCount(0);
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM ASSIGN")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("TID"),
                    rs.getString("TGID")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Thêm dữ liệu
    private void addData() {
        try {
            String sql = "INSERT INTO ASSIGN VALUES(?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtTID.getText());
            ps.setString(2, txtTGID.getText());
            ps.executeUpdate();
            loadData();
            clearForm();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Xóa dữ liệu
    private void deleteData() {
        try {
            String sql = "DELETE FROM ASSIGN WHERE TID=? AND TGID=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtTID.getText());
            ps.setString(2, txtTGID.getText());
            ps.executeUpdate();
            loadData();
            clearForm();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Xóa trắng form
    private void clearForm() {
        txtTID.setText(""); txtTGID.setText("");
    }
}
class ComePanel extends JPanel {
    private Connection conn;
    private DefaultTableModel model;
    private JTable table;
    private JTextField txtTID, txtLID;

    public ComePanel(Connection conn) {
        this.conn = conn;
        setLayout(new BorderLayout());

        // Form nhập liệu
        JPanel form = new JPanel(new GridLayout(2, 2, 5, 5));
        form.add(new JLabel("Tour ID (TID):")); txtTID = new JTextField(); form.add(txtTID);
        form.add(new JLabel("Location ID (LID):")); txtLID = new JTextField(); form.add(txtLID);

        // Nút CRUD
        JPanel buttons = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Add");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");
        buttons.add(btnAdd); buttons.add(btnDelete); buttons.add(btnClear);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        // Bảng dữ liệu
        String[] cols = {"TID", "LID"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Load dữ liệu ban đầu
        loadData();

        // Sự kiện nút
        btnAdd.addActionListener(e -> addData());
        btnDelete.addActionListener(e -> deleteData());
        btnClear.addActionListener(e -> clearForm());

        // Click bảng -> load form
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtTID.setText(model.getValueAt(row, 0).toString());
                txtLID.setText(model.getValueAt(row, 1).toString());
            }
        });
    }

    // Load dữ liệu từ DB
    private void loadData() {
        model.setRowCount(0);
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM COME")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("TID"),
                    rs.getString("LID")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Thêm dữ liệu
    private void addData() {
        try {
            String sql = "INSERT INTO COME VALUES(?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtTID.getText());
            ps.setString(2, txtLID.getText());
            ps.executeUpdate();
            loadData();
            clearForm();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Xóa dữ liệu
    private void deleteData() {
        try {
            String sql = "DELETE FROM COME WHERE TID=? AND LID=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtTID.getText());
            ps.setString(2, txtLID.getText());
            ps.executeUpdate();
            loadData();
            clearForm();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // Xóa trắng form
    private void clearForm() {
        txtTID.setText(""); txtLID.setText("");
    }
}
class UniversalSearchPanel extends JPanel {
    private Connection conn;
    private JComboBox<String> cbTable;
    private JTextField txtKey;
    private JTextArea txtResult;

    public UniversalSearchPanel(Connection conn) {
        this.conn = conn;
        setLayout(new BorderLayout());

        // Form nhập liệu
        JPanel form = new JPanel(new FlowLayout());
        cbTable = new JComboBox<>(new String[]{
            "CUSTOMER", "AGENCY", "TOURGUIDE", "SALARY",
            "HOTEL", "TOUR", "RECEIPT", "VEHICLE", "TLOCATION",
            "BOOKING", "ASSIGN", "COME", "GOBY"
        });
        txtKey = new JTextField(15);
        JButton btnSearch = new JButton("Search");

        form.add(new JLabel("Chọn bảng:"));
        form.add(cbTable);
        form.add(new JLabel("Nhập khóa chính:"));
        form.add(txtKey);
        form.add(btnSearch);

        add(form, BorderLayout.NORTH);

        // Khu vực hiển thị kết quả
        txtResult = new JTextArea(20, 80);
        txtResult.setEditable(false);
        add(new JScrollPane(txtResult), BorderLayout.CENTER);

        // Sự kiện nút Search
        btnSearch.addActionListener(e -> searchData());
    }

    private void searchData() {
        String table = cbTable.getSelectedItem().toString();
        String key = txtKey.getText().trim();
        if (key.isEmpty()) {
            txtResult.setText("Vui lòng nhập khóa chính!");
            return;
        }

        try {
            String sql = "";
            switch (table) {
                case "CUSTOMER":
                    sql = "SELECT * FROM CUSTOMER WHERE CID=?";
                    break;
                case "AGENCY":
                    sql = "SELECT * FROM AGENCY WHERE AID=?";
                    break;
                case "TOURGUIDE":
                    sql = "SELECT tg.TGID, tg.TGName, tg.TGPN, a.AID, a.APN, a.AAddress " +
                          "FROM TOURGUIDE tg JOIN AGENCY a ON tg.AID=a.AID WHERE tg.TGID=?";
                    break;
                case "SALARY":
                    sql = "SELECT s.SIDTG, s.TGS, tg.TGID, tg.TGName, tg.TGPN" +
                          "FROM SALARY s JOIN TOURGUIDE tg ON s.TGID=tg.TGID WHERE s.SIDTG=?";
                    break;
                case "HOTEL":
                    sql = "SELECT * FROM HOTEL WHERE HID=?";
                    break;
                case "TOUR":
                    sql = "SELECT t.TID, t.TName, t.Price, t.SDate, t.EDate, h.HID, h.HName, h.CIDate, h.CODate, h.HAddress " +
                          "FROM TOUR t JOIN HOTEL h ON t.HID=h.HID WHERE t.TID=?";
                    break;
                case "RECEIPT":
                    sql = "SELECT r.RID, r.RDate, r.SumR, c.CID, c.CName, c.CAddress, c.CPN, a.AID, a.APN, a.AAddress " +
                          "FROM RECEIPT r JOIN CUSTOMER c ON r.CID=c.CID JOIN AGENCY a ON r.AID=a.AID WHERE r.RID=?";
                    break;
                case "VEHICLE":
                    sql = "SELECT * FROM VEHICLE WHERE LicensePlate=?";
                    break;
                case "TLOCATION":
                    sql = "SELECT * FROM TLOCATION WHERE LID=?";
                    break;
                case "BOOKING":
                    sql = "SELECT * FROM BOOKING WHERE TID=? OR CID=?";
                    break;
                case "ASSIGN":
                    sql = "SELECT * FROM ASSIGN WHERE TID=? OR TGID=?";
                    break;
                case "COME":
                    sql = "SELECT * FROM COME WHERE TID=? OR LID=?";
                    break;
                case "GOBY":
                    sql = "SELECT * FROM GOBY WHERE TID=? OR LicensePlate=?";
                    break;
            }

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, key);
            // Với các bảng có 2 khóa chính, set thêm tham số thứ 2
            if (table.equals("BOOKING") || table.equals("ASSIGN") || table.equals("COME") || table.equals("GOBY")) {
                ps.setString(2, key);
            }

            ResultSet rs = ps.executeQuery();
            StringBuilder sb = new StringBuilder();
            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= colCount; i++) {
                    sb.append(meta.getColumnName(i)).append(": ").append(rs.getString(i)).append("\n");
                }
                sb.append("---------------\n");
            }

            if (sb.length() == 0) {
                txtResult.setText("Không tìm thấy dữ liệu với khóa chính = " + key);
            } else {
                txtResult.setText(sb.toString());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            txtResult.setText("Lỗi khi tìm kiếm: " + e.getMessage());
        }
    }
}

public class Csdl extends JFrame {
    private Connection conn;
    private JTabbedPane tabbedPane;

    public Csdl() {
        setTitle("Tour Management System");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Kết nối SQL Server
        try {
            conn = DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=BAITAPLON;encrypt=true;trustServerCertificate=true",
                "sa", "manh2006ok"
            );
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Kết nối DB thất bại: " + e.getMessage());
            return;
        }

        tabbedPane = new JTabbedPane();

        // Thêm các module
        tabbedPane.addTab("CUSTOMER", new CustomerPanel(conn));
        tabbedPane.addTab("HOTEL", new HotelPanel(conn));
        tabbedPane.addTab("TOUR", new TourPanel(conn));
        tabbedPane.addTab("AGENCY", new AgencyPanel(conn));
        tabbedPane.addTab("TOURGUIDE", new TourGuidePanel(conn));
        tabbedPane.addTab("SALARY", new SalaryPanel(conn));
        tabbedPane.addTab("RECEIPT", new ReceiptPanel(conn));
        tabbedPane.addTab("VEHICLE", new VehiclePanel(conn));
        tabbedPane.addTab("TLOCATION", new LocationPanel(conn));
        tabbedPane.addTab("BOOKING", new BookingPanel(conn));
        tabbedPane.addTab("GOBY", new GobyPanel(conn));
        tabbedPane.addTab("ASSIGN", new AssignPanel(conn));
        tabbedPane.addTab("COME", new ComePanel(conn));
        tabbedPane.addTab("SEARCH", new UniversalSearchPanel(conn));
        add(tabbedPane);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Csdl().setVisible(true));
    }
}