package NewjframePackage;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ReturnBook extends javax.swing.JFrame {

    public ReturnBook() {
        initComponents();
    }

    //To fetch the issue book details from db and display it to panel 
    public void getIssueBookDetails() {
        int bookId = Integer.parseInt(txt_bookId.getText());
        int studentId = Integer.parseInt(txt_studentId.getText());
        try {
            Connection con = NewjframePackage.DBConnection.getConnection();
            String sql = "select * from issue_book_details where book_id = ? and student_id = ? and status = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, bookId);
            pst.setInt(2, studentId);
            pst.setString(3, "pending");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                lbl_bookId.setText(rs.getString("book_Id"));
                lbl_bookName.setText(rs.getString("book_name"));
                lbl_studentName.setText(rs.getString("student_name"));
                lbl_issueDate.setText(rs.getString("issue_date"));
                lbl_dueDate.setText(rs.getString("due_date"));

                lbl_bookError.setText("");

            } else {
                lbl_bookError.setText("No Record found!");
                lbl_bookId.setText("");
                lbl_bookName.setText("");
                lbl_studentName.setText("");
                lbl_issueDate.setText("");
                lbl_dueDate.setText("");
            }
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    //Return the book
    public boolean returnBook() {
        boolean isReturned = false;
        int bookId = Integer.parseInt(txt_bookId.getText());
        int studentId = Integer.parseInt(txt_studentId.getText());
        try {
            Connection con = NewjframePackage.DBConnection.getConnection();
            String sql = "update issue_book_details set status = ? where student_id = ? and book_id = ? and status = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, "returned");
            pst.setInt(2, studentId);
            pst.setInt(3, bookId);
            pst.setString(4, "pending");
            int rowCount = pst.executeUpdate();  //executeUpdate method returns rowcount in integer hence stored in variable , and is a non-select query so executeUpdate used
            // with select query, ececuteQuery is used and returns result set object
            if (rowCount > 0) {
                isReturned = true;
            } else {
                isReturned = false;
            }

        } catch (HeadlessException | SQLException e) {
            e.getMessage();
        }
        return isReturned;
    }

    // updating book count when the book is issued
    public void updateBookCount() {
        int bookId = Integer.parseInt(txt_bookId.getText());
        try {
            Connection con = NewjframePackage.DBConnection.getConnection();
            String sql = "update book_details set quantity = quantity + 1 where book_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, bookId);
            int rowCount = pst.executeUpdate();  //executeUpdate method returns rowcount in integer hence stored in variable , and is a non-select query so executeUpdate used
            // with select query, ececuteQuery is used and returns result set object
            if (rowCount > 0) {
                JOptionPane.showMessageDialog(this, "Book Count Updated");
                int initialCount = Integer.parseInt(lbl_dueDate.getText());
                lbl_dueDate.setText(Integer.toString(initialCount - 1));
            } else {
                JOptionPane.showMessageDialog(this, "Can't Update Book Count");
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    // after completing return request by admin , request should be reomved from book_requests
    public void removeReturnReqFromTable() {
        int bookId = Integer.parseInt(txt_bookId.getText());
        String sName = lbl_studentName.getText();

        try {
            Connection con = NewjframePackage.DBConnection.getConnection();
            String sql = "delete from book_issue_requests where book_id = ? and student_name = ? and requests_to = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, bookId);
            pst.setString(2, sName);
            pst.setString(3, "Return");
            int rowCount = pst.executeUpdate();  //executeUpdate method returns rowcount in integer hence stored in variable , and is a non-select query so executeUpdate used
            // with select query, ececuteQuery is used and returns result set object
            if (rowCount > 0) {
                JOptionPane.showMessageDialog(this, "Student Request Removed from table Sucessfully");
            } else {
                JOptionPane.showMessageDialog(this, "Student Request Removal failure");
            }
        } catch (Exception e) {
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

        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        lbl_dueDate = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        lbl_bookError = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        lbl_bookId = new javax.swing.JLabel();
        lbl_bookName = new javax.swing.JLabel();
        lbl_studentName = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        lbl_issueDate = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        txt_bookId = new app.bolivia.swing.JCTextField();
        txt_studentId = new app.bolivia.swing.JCTextField();
        jLabel39 = new javax.swing.JLabel();
        rSMaterialButtonCircle2 = new necesario.RSMaterialButtonCircle();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        rSMaterialButtonCircle3 = new necesario.RSMaterialButtonCircle();
        jLabel48 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(204, 51, 0));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 25)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Literature_100px_1.png"))); // NOI18N
        jLabel4.setText(" BOOK DETAILS");
        jPanel5.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 280, 100));

        lbl_dueDate.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        lbl_dueDate.setForeground(new java.awt.Color(255, 255, 255));
        lbl_dueDate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel5.add(lbl_dueDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 490, 190, 20));

        jLabel42.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(255, 255, 255));
        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel42.setText("Book Name  :");
        jPanel5.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 280, -1, 20));

        jLabel43.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(255, 255, 255));
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel43.setText("Student Name  :");
        jPanel5.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 350, 120, 20));

        lbl_bookError.setFont(new java.awt.Font("Yu Gothic UI", 1, 24)); // NOI18N
        lbl_bookError.setForeground(new java.awt.Color(255, 255, 102));
        lbl_bookError.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel5.add(lbl_bookError, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 560, 290, 50));

        jLabel45.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(255, 255, 255));
        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel45.setText("Book id  :");
        jPanel5.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 80, 20));

        lbl_bookId.setBackground(new java.awt.Color(0, 0, 0));
        lbl_bookId.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        lbl_bookId.setForeground(new java.awt.Color(255, 255, 255));
        lbl_bookId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel5.add(lbl_bookId, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 210, 190, 20));

        lbl_bookName.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        lbl_bookName.setForeground(new java.awt.Color(255, 255, 255));
        lbl_bookName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel5.add(lbl_bookName, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 280, 180, 20));

        lbl_studentName.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        lbl_studentName.setForeground(new java.awt.Color(255, 255, 255));
        lbl_studentName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel5.add(lbl_studentName, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 350, 170, 20));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanel5.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, 300, 5));

        jLabel11.setBackground(new java.awt.Color(153, 153, 153));
        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("X");
        jLabel11.setAlignmentY(0.2F);
        jLabel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel11MouseClicked(evt);
            }
        });
        jPanel5.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 0, 130, 30));

        jLabel46.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(255, 255, 255));
        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel46.setText("Due Date  :");
        jPanel5.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 490, 90, 20));

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanel5.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 550, 300, 5));

        jLabel47.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(255, 255, 255));
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel47.setText("Issue Date  :");
        jPanel5.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 420, 90, 20));

        lbl_issueDate.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        lbl_issueDate.setForeground(new java.awt.Color(255, 255, 255));
        lbl_issueDate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel5.add(lbl_issueDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 420, 190, 20));

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 0, 350, 640));

        jLabel1.setBackground(new java.awt.Color(204, 0, 0));
        jLabel1.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 25)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 0, 51));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Books_52px_1.png"))); // NOI18N
        jLabel1.setText(" RETURN BOOK");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1110, 80, 240, 70));

        jPanel4.setBackground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 170, 300, 5));

        jPanel9.setBackground(new java.awt.Color(0, 0, 204));
        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel9.setPreferredSize(new java.awt.Dimension(47, 40));

        jLabel5.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText(" X");
        jLabel5.setAlignmentX(3.8F);
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(1380, 0, 44, 40));

        jLabel33.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(204, 0, 0));
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("Book id :");
        jPanel1.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 260, 70, 20));

        txt_bookId.setBackground(new java.awt.Color(255, 255, 204));
        txt_bookId.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(204, 0, 0)));
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
        jPanel1.add(txt_bookId, new org.netbeans.lib.awtextra.AbsoluteConstraints(1190, 260, 160, 20));

        txt_studentId.setBackground(new java.awt.Color(255, 255, 204));
        txt_studentId.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(204, 0, 0)));
        txt_studentId.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        txt_studentId.setFont(new java.awt.Font("Yu Gothic Medium", 0, 15)); // NOI18N
        txt_studentId.setPlaceholder("Enter Student id...");
        txt_studentId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_studentIdFocusLost(evt);
            }
        });
        txt_studentId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_studentIdActionPerformed(evt);
            }
        });
        jPanel1.add(txt_studentId, new org.netbeans.lib.awtextra.AbsoluteConstraints(1190, 340, 160, 20));

        jLabel39.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(204, 0, 0));
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setText("Student id :");
        jPanel1.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 340, 90, 20));

        rSMaterialButtonCircle2.setBackground(new java.awt.Color(51, 0, 204));
        rSMaterialButtonCircle2.setText("FIND DETAILS");
        rSMaterialButtonCircle2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rSMaterialButtonCircle2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonCircle2ActionPerformed(evt);
            }
        });
        jPanel1.add(rSMaterialButtonCircle2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 430, 280, 60));

        jPanel6.setBackground(new java.awt.Color(0, 0, 204));

        jLabel3.setFont(new java.awt.Font("Verdana", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Rewind_48px.png"))); // NOI18N
        jLabel3.setText("Home");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 4, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        rSMaterialButtonCircle3.setBackground(new java.awt.Color(204, 51, 0));
        rSMaterialButtonCircle3.setText("RETURN BOOK");
        rSMaterialButtonCircle3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rSMaterialButtonCircle3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonCircle3ActionPerformed(evt);
            }
        });
        jPanel1.add(rSMaterialButtonCircle3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 520, 280, 60));

        jLabel48.setBackground(new java.awt.Color(255, 255, 255));
        jLabel48.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(255, 255, 255));
        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel48.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/WHAT-IS-THE-PURPOSE-OF-A-LIBRARY-MANAGEMENT-SYSTEM-min.png"))); // NOI18N
        jPanel1.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 640, 480));

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 7, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 640, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 0, 7, -1));

        jPanel3.setBackground(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 7, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 640, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(673, 0, 7, -1));

        jPanel8.setBackground(new java.awt.Color(204, 0, 0));

        jLabel6.setFont(new java.awt.Font("Verdana", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Rewind_48px.png"))); // NOI18N
        jLabel6.setText("View Requests");
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 4, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 210, 40));

        jPanel17.setBackground(new java.awt.Color(204, 0, 0));

        jLabel9.setFont(new java.awt.Font("Verdana", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Rewind_48px.png"))); // NOI18N
        jLabel9.setText("Manage Students");
        jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel9MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 4, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(1110, 0, 230, 40));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1420, 640));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        HomePage home = new HomePage();
        home.setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabel11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jLabel11MouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jLabel5MouseClicked

    private void txt_bookIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_bookIdFocusLost

    }//GEN-LAST:event_txt_bookIdFocusLost

    private void txt_bookIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_bookIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_bookIdActionPerformed

    private void txt_studentIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_studentIdFocusLost

    }//GEN-LAST:event_txt_studentIdFocusLost

    private void txt_studentIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_studentIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_studentIdActionPerformed

    private void rSMaterialButtonCircle2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle2ActionPerformed
        getIssueBookDetails();

    }//GEN-LAST:event_rSMaterialButtonCircle2ActionPerformed

    private void rSMaterialButtonCircle3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle3ActionPerformed
        if (returnBook()) {
            JOptionPane.showMessageDialog(this, "Book Returned Sucessfully");
            removeReturnReqFromTable();
            updateBookCount();
        } else {
            JOptionPane.showMessageDialog(this, "Book Return Failure");
        }

    }//GEN-LAST:event_rSMaterialButtonCircle3ActionPerformed

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        RequestedBooks rq = new RequestedBooks();
        rq.setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel6MouseClicked

    private void jLabel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseClicked
        ManageStudents st = new ManageStudents();
        st.setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel9MouseClicked

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
            java.util.logging.Logger.getLogger(ReturnBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReturnBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReturnBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReturnBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReturnBook().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel lbl_bookError;
    private javax.swing.JLabel lbl_bookId;
    private javax.swing.JLabel lbl_bookName;
    private javax.swing.JLabel lbl_dueDate;
    private javax.swing.JLabel lbl_issueDate;
    private javax.swing.JLabel lbl_studentName;
    private necesario.RSMaterialButtonCircle rSMaterialButtonCircle2;
    private necesario.RSMaterialButtonCircle rSMaterialButtonCircle3;
    private app.bolivia.swing.JCTextField txt_bookId;
    private app.bolivia.swing.JCTextField txt_studentId;
    // End of variables declaration//GEN-END:variables
}
