package NewjframePackage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author sudha
 */
public class BookRequest extends javax.swing.JFrame {

    //Attributes 
    String bookName, author, sName, course, branch, requestTo;
    int bookId;
    DefaultTableModel model; //using this we will set the values in the book table after fetching actual value form database

    public BookRequest() {
        initComponents();
        setBookDetailsToTable();
    }

    //to set book details into the table
    public final void setBookDetailsToTable() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "SUD@2001");
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from book_details");  //executeQuery method return resultset hence stored in result set

            //fething data using DefaultTable model
            while (rs.next()) {
                String bookId = rs.getString("book_id"); //passed string values are same as name of coulmns in database
                String bookName = rs.getString("book_name");
                String author = rs.getString("author");
                int quantity = rs.getInt("quantity");
                Object[] obj = {bookId, bookName, author, quantity};  //order of elements (that are objects individually) is as same as table from left to right
                model = (DefaultTableModel) tbl_bookDetails.getModel();
                model.addRow(obj); // object array is passed to add each new row in book table
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.getMessage();
        }

    }

    // To check book entered by user is valid or not 
    public boolean checkBookExists() {
        int id = Integer.parseInt(txt_bookId.getText()); //get id entered by user 
        String name = txt_bookName.getText(); //get book name entered by user 
        String author = txt_authorName.getText();
        try {
            Connection con = NewjframePackage.DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select book_id , book_name , author from book_details");  //executeQuery method returns collection of rows hence stored in result set
            while (rs.next()) {
                int bookIdFromDB = rs.getInt("book_id");
                String bookNameFromDB = rs.getString("book_name"); //get book name at current row 
                String authorFromDB = rs.getString("author");
                if (name.equals(bookNameFromDB) && id == bookIdFromDB && author.equals(authorFromDB)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        JOptionPane.showMessageDialog(this, "Enter Valid Book Credentials!");
        return false;
    }

    // to insert request to  request book issue table which will shown to admin
    public boolean requestBookIssue() {

        boolean isRequested = false;

        if (txt_bookId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "please Enter Book id");
            return false;
        }
        if (txt_bookName.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Please Enter Book name");
            return false;
        }

        if (txt_authorName.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Please Enter Author name");
            return false;
        }
        if (txt_studentName.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Please Enter Student name");
            return false;
        }

        if (combo_CourseName.getSelectedItem().toString().equals(" ")) {
            JOptionPane.showMessageDialog(this, "Please Choose a valid course");
            return false;
        }
        if (combo_branch.getSelectedItem().toString().equals(" ")) {
            JOptionPane.showMessageDialog(this, "Please Choose a valid branch");
            return false;
        }

        if (checkBookExists()) {

            bookId = Integer.parseInt(txt_bookId.getText()); //returns a string so convert it to int as book_id is of type int
            bookName = txt_bookName.getText();
            author = txt_authorName.getText();
            sName = capitalizeFirstLetterOnly(txt_studentName.getText());
            course = combo_CourseName.getSelectedItem().toString();
            branch = combo_branch.getSelectedItem().toString();

            try {
                Connection con = NewjframePackage.DBConnection.getConnection();
                PreparedStatement pst = con.prepareStatement("insert into book_issue_requests values (?,?,?,?,?,?,?);");
                pst.setInt(1, bookId);
                pst.setString(2, bookName);
                pst.setString(3, author);
                pst.setString(4, sName);
                pst.setString(5, course);
                pst.setString(6, branch);
                pst.setString(7, "Issue");

                int rowCount = pst.executeUpdate();  //executeUpdate method returns rowcount in integer hence stored in variable , and is a non-select query so executeUpdate used
                // with select query, ececuteQuery is used and returns result set object
                if (rowCount > 0) {
                    isRequested = true;
                } else {
                    isRequested = false;
                }

            } catch (SQLException e) {
                e.getMessage();
            }

        }
        return isRequested;
    }

    public static String capitalizeFirstLetterOnly(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    // to request book return
    public boolean requestBookReturn() {

        boolean isRequested = false;
        if (txt_bookId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "please Enter Book id");
            return false;
        }
        if (txt_bookName.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Please Enter Book name");
            return false;
        }

        if (txt_authorName.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Please Enter Author name");
            return false;
        }
        if (txt_studentName.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Please Enter Student name");
            return false;
        }

        if (combo_CourseName.getSelectedItem().toString().equals(" ")) {
            JOptionPane.showMessageDialog(this, "Please Choose a valid course");
            return false;
        }
        if (combo_branch.getSelectedItem().toString().equals(" ")) {
            JOptionPane.showMessageDialog(this, "Please Choose a valid branch");
            return false;
        }

        if (checkBookExists()) {
            bookId = Integer.parseInt(txt_bookId.getText()); //returns a string so convert it to int as book_id is of type int
            bookName = txt_bookName.getText();
            author = txt_authorName.getText();
            sName = capitalizeFirstLetterOnly(txt_studentName.getText());
            course = combo_CourseName.getSelectedItem().toString();
            branch = combo_branch.getSelectedItem().toString();

            try {
                Connection con = NewjframePackage.DBConnection.getConnection();
                PreparedStatement pst = con.prepareStatement("insert into book_issue_requests values (?,?,?,?,?,?,?);");
                pst.setInt(1, bookId);
                pst.setString(2, bookName);
                pst.setString(3, author);
                pst.setString(4, sName);
                pst.setString(5, course);
                pst.setString(6, branch);
                pst.setString(7, "Return");

                int rowCount = pst.executeUpdate();  //executeUpdate method returns rowcount in integer hence stored in variable , and is a non-select query so executeUpdate used
                // with select query, ececuteQuery is used and returns result set object
                if (rowCount > 0) {
                    isRequested = true;
                } else {
                    isRequested = false;
                }

            } catch (SQLException e) {
                e.getMessage();
            }
        }

        return isRequested;
    }

    //when user wants to return, check whether the book is ever issued or not , if issued then only it can be returned 
    public boolean isAbleToReturn() {
        boolean isAble = false;
        int id = Integer.parseInt(txt_bookId.getText());
        String bookName = txt_bookName.getText();
        String sName = capitalizeFirstLetterOnly(txt_studentName.getText());
        try {
            Connection con = NewjframePackage.DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select book_id, book_name, student_name, status from issue_book_details");  //executeQuery method returns collection of rows hence stored in result set
            while (rs.next()) {
                int idFromDB = rs.getInt("book_id");
                String bookNameFromDB = rs.getString("book_name");
                String sNameFromDB = rs.getString("student_name"); //get book name at current row ;
                String status = rs.getString("status");
                if (id == idFromDB && bookName.equals(bookNameFromDB) && sName.equals(sNameFromDB) && status.equals("pending")) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }

        return isAble;
    }

    // check the student returning book is defaulter or not
    public boolean checkDefaulter() {
        long l = System.currentTimeMillis();
        Date todayDate = new Date(l);

        String stuName = capitalizeFirstLetterOnly(txt_studentName.getText());

        boolean isDefaulter = false;
        try {
            Connection con = NewjframePackage.DBConnection.getConnection();
            String sql = "select * from issue_book_details where due_date < ? and status =  ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setDate(1, todayDate);
            pst.setString(2, "pending");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String stuNameFromDb = rs.getString("student_name");
                if (stuName.equals(stuNameFromDb)) {
                    isDefaulter = true;
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return isDefaulter;
    }

    //Method to clear table 
    public void clearTable() {  // on click of add button for adding book, table should  be cleared first to remove existing data , 
        //and all data is fetched again from database with added book, updated data will be fetched by showBookDetailsToTable() method
        model = (DefaultTableModel) tbl_bookDetails.getModel();
        model.setRowCount(0); //makes rowcount 0 i.e delete the existing data 
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_bookDetails = new rojeru_san.complementos.RSTableMetro();
        jLabel4 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        txt_bookId = new app.bolivia.swing.JCTextField();
        jLabel33 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        txt_studentName = new app.bolivia.swing.JCTextField();
        combo_CourseName = new javax.swing.JComboBox<>();
        jLabel41 = new javax.swing.JLabel();
        txt_bookName = new app.bolivia.swing.JCTextField();
        jLabel35 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        txt_authorName = new app.bolivia.swing.JCTextField();
        jLabel43 = new javax.swing.JLabel();
        combo_branch = new javax.swing.JComboBox<>();
        rSMaterialButtonCircle2 = new necesario.RSMaterialButtonCircle();
        rSMaterialButtonCircle3 = new necesario.RSMaterialButtonCircle();
        jPanel14 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 204));
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(102, 0, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel5.setPreferredSize(new java.awt.Dimension(47, 40));

        jLabel3.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("  X");
        jLabel3.setAlignmentX(4.0F);
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1205, 0, 44, 50));

        tbl_bookDetails.setBackground(new java.awt.Color(255, 255, 204));
        tbl_bookDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Book id", "Name", "Author", "Quantity"
            }
        ));
        tbl_bookDetails.setColorBackgoundHead(new java.awt.Color(102, 0, 255));
        tbl_bookDetails.setColorBordeFilas(new java.awt.Color(51, 51, 51));
        tbl_bookDetails.setColorFilasBackgound1(new java.awt.Color(255, 255, 153));
        tbl_bookDetails.setColorFilasBackgound2(new java.awt.Color(255, 255, 153));
        tbl_bookDetails.setColorSelBackgound(new java.awt.Color(255, 0, 0));
        tbl_bookDetails.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        tbl_bookDetails.setFuenteFilas(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        tbl_bookDetails.setFuenteFilasSelect(new java.awt.Font("Yu Gothic UI", 1, 20)); // NOI18N
        tbl_bookDetails.setFuenteHead(new java.awt.Font("Yu Gothic UI Semibold", 1, 20)); // NOI18N
        tbl_bookDetails.setRowHeight(30);
        tbl_bookDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_bookDetailsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbl_bookDetails);

        jPanel3.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 140, 1010, 230));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 30)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 51, 51));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Books_52px_1.png"))); // NOI18N
        jLabel4.setText(" Book Issue OR Return Request");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 30, 490, -1));

        jPanel6.setBackground(new java.awt.Color(255, 51, 51));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1250, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 1250, 8));

        jPanel1.setBackground(new java.awt.Color(255, 0, 0));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_bookId.setBackground(new java.awt.Color(255, 0, 0));
        txt_bookId.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_bookId.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        txt_bookId.setFont(new java.awt.Font("Yu Gothic Medium", 0, 15)); // NOI18N
        txt_bookId.setPlaceholder("Enter Book id...");
        txt_bookId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_bookIdFocusLost(evt);
            }
        });
        txt_bookId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_bookIdActionPerformed(evt);
            }
        });
        jPanel1.add(txt_bookId, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 60, 240, 40));

        jLabel33.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("Book id");
        jPanel1.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 30, 60, 20));

        jLabel39.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setText("Student Name :");
        jPanel1.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 140, 120, 20));

        txt_studentName.setBackground(new java.awt.Color(255, 0, 0));
        txt_studentName.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_studentName.setForeground(new java.awt.Color(0, 0, 0));
        txt_studentName.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        txt_studentName.setFont(new java.awt.Font("Yu Gothic Medium", 0, 15)); // NOI18N
        txt_studentName.setPlaceholder("Enter Student Name...");
        txt_studentName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_studentNameFocusLost(evt);
            }
        });
        txt_studentName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_studentNameActionPerformed(evt);
            }
        });
        jPanel1.add(txt_studentName, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 170, 240, 40));

        combo_CourseName.setFont(new java.awt.Font("Verdana", 0, 17)); // NOI18N
        combo_CourseName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "B.Sc", "B.Tech", "MCA", "Ph.D.", "B.A", " " }));
        jPanel1.add(combo_CourseName, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 180, 260, 40));

        jLabel41.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(255, 255, 255));
        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel41.setText("Select Course :");
        jPanel1.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 140, 110, 20));

        txt_bookName.setBackground(new java.awt.Color(255, 0, 0));
        txt_bookName.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_bookName.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        txt_bookName.setFont(new java.awt.Font("Yu Gothic Medium", 0, 14)); // NOI18N
        txt_bookName.setPlaceholder("Enter Book Name...");
        txt_bookName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_bookNameFocusLost(evt);
            }
        });
        txt_bookName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_bookNameActionPerformed(evt);
            }
        });
        jPanel1.add(txt_bookName, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 60, 240, 40));

        jLabel35.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("Book Name");
        jPanel1.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 30, 90, 20));

        jLabel37.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("Author Name");
        jPanel1.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 40, 100, 20));

        txt_authorName.setBackground(new java.awt.Color(255, 0, 0));
        txt_authorName.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_authorName.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        txt_authorName.setFont(new java.awt.Font("Yu Gothic Medium", 0, 15)); // NOI18N
        txt_authorName.setPlaceholder("Enter Author Name...");
        txt_authorName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_authorNameFocusLost(evt);
            }
        });
        txt_authorName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_authorNameActionPerformed(evt);
            }
        });
        jPanel1.add(txt_authorName, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 70, 250, 40));

        jLabel43.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(255, 255, 255));
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel43.setText("Select Branch :");
        jPanel1.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 140, 110, 20));

        combo_branch.setFont(new java.awt.Font("Verdana", 0, 17)); // NOI18N
        combo_branch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mathematics (B.Sc)", "Physics (B.sc)", "Computer Science (B.Tech)", "Electronics (B.Tech)", "Civil(B.Tech)", "Neuroscience (Ph.D.)", "Psychology (Ph.D.)", "General / Regular", " ", " ", " ", " " }));
        combo_branch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo_branchActionPerformed(evt);
            }
        });
        jPanel1.add(combo_branch, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 180, 260, 40));

        rSMaterialButtonCircle2.setBackground(new java.awt.Color(102, 0, 255));
        rSMaterialButtonCircle2.setText("REQUEST ISSUE");
        rSMaterialButtonCircle2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rSMaterialButtonCircle2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonCircle2ActionPerformed(evt);
            }
        });
        jPanel1.add(rSMaterialButtonCircle2, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 240, 260, 60));

        rSMaterialButtonCircle3.setBackground(new java.awt.Color(102, 0, 255));
        rSMaterialButtonCircle3.setText("REQUEST RETURN");
        rSMaterialButtonCircle3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rSMaterialButtonCircle3.setMaximumSize(new java.awt.Dimension(158, 32));
        rSMaterialButtonCircle3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonCircle3ActionPerformed(evt);
            }
        });
        jPanel1.add(rSMaterialButtonCircle3, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 240, 260, 60));

        jPanel3.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 390, 1250, 330));

        jPanel14.setBackground(new java.awt.Color(102, 51, 255));
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel16.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 24)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Exit_26px_2.png"))); // NOI18N
        jLabel16.setText("  Logout");
        jLabel16.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel16MouseClicked(evt);
            }
        });
        jPanel14.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 130, 40));

        jPanel3.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 170, 50));

        jPanel15.setBackground(new java.awt.Color(102, 51, 255));
        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/male_user_50px.png"))); // NOI18N
        jLabel2.setText("Welcome, Student");
        jPanel15.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 210, -1));

        jPanel3.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 0, 230, 50));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1250, 720));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txt_bookIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_bookIdFocusLost

    }//GEN-LAST:event_txt_bookIdFocusLost

    private void txt_bookIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_bookIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_bookIdActionPerformed

    private void txt_bookNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_bookNameFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_bookNameFocusLost

    private void txt_bookNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_bookNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_bookNameActionPerformed

    private void txt_authorNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_authorNameFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_authorNameFocusLost

    private void txt_authorNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_authorNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_authorNameActionPerformed

    private void rSMaterialButtonCircle2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle2ActionPerformed

        if (requestBookIssue()) {
            JOptionPane.showMessageDialog(this, "Book is Requested for issue");
        } else {
            JOptionPane.showMessageDialog(this, "Book Issue Request failed");
        }
    }//GEN-LAST:event_rSMaterialButtonCircle2ActionPerformed

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jLabel3MouseClicked

    private void tbl_bookDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_bookDetailsMouseClicked
        int rowNo = tbl_bookDetails.getSelectedRow();
        TableModel model = tbl_bookDetails.getModel();

        txt_bookId.setText(model.getValueAt(rowNo, 0).toString());  //getValueAt takes two parameters, 1st is from which row and 2nd is column index
        txt_bookName.setText(model.getValueAt(rowNo, 1).toString());
        txt_authorName.setText(model.getValueAt(rowNo, 2).toString());
    }//GEN-LAST:event_tbl_bookDetailsMouseClicked

    private void txt_studentNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_studentNameFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_studentNameFocusLost

    private void txt_studentNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_studentNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_studentNameActionPerformed

    private void combo_branchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_branchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_combo_branchActionPerformed

    private void jLabel16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel16MouseClicked
        // Create and show a message dialog
        JOptionPane optionPane = new JOptionPane("Logging Out.....", JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog(this, "Message");

        // Create a Timer to close the dialog after 3 seconds
        Timer timer = new Timer(1500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                dialog.dispose();
            }
        });
        timer.setRepeats(false); // Only execute once

        // Start the timer and show the dialog
        timer.start();
        dialog.setVisible(true);

        NewSignUp signup = new NewSignUp();
        signup.setVisible(true);
        dispose();
        JOptionPane.showMessageDialog(this, "Logged Out Sucessfuly");
    }//GEN-LAST:event_jLabel16MouseClicked

    private void rSMaterialButtonCircle3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle3ActionPerformed

        if (isAbleToReturn()) {
            if (requestBookReturn()) {
                if (checkDefaulter()) {
                    JOptionPane.showMessageDialog(this, "You have been fined with ₹100 for late Return");
                }
                JOptionPane.showMessageDialog(this, "Book is Requested for Return");
            } else {
                JOptionPane.showMessageDialog(this, "Book Return Request failed");
            }
        } else {
            JOptionPane.showMessageDialog(this, "This book was never issued by this student name");
        }

    }//GEN-LAST:event_rSMaterialButtonCircle3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BookRequest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BookRequest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BookRequest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BookRequest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BookRequest().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> combo_CourseName;
    private javax.swing.JComboBox<String> combo_branch;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private necesario.RSMaterialButtonCircle rSMaterialButtonCircle2;
    private necesario.RSMaterialButtonCircle rSMaterialButtonCircle3;
    private rojeru_san.complementos.RSTableMetro tbl_bookDetails;
    private app.bolivia.swing.JCTextField txt_authorName;
    private app.bolivia.swing.JCTextField txt_bookId;
    private app.bolivia.swing.JCTextField txt_bookName;
    private app.bolivia.swing.JCTextField txt_studentName;
    // End of variables declaration//GEN-END:variables
}
