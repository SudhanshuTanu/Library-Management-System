package NewjframePackage;

import java.awt.HeadlessException;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.JOptionPane;

public class IssueBook extends javax.swing.JFrame {

    public IssueBook() {
        initComponents();
    }

    public static String capitalizeFirstLetterOnly(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    // to fetch the book details from the database and display it to book details panel
    public void getBookDetails() {
        int bookId = Integer.parseInt(txt_bookId.getText());
        try {
            Connection con = NewjframePackage.DBConnection.getConnection();
            String sql = "select * from book_details where book_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, bookId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                lbl_bookId.setText(rs.getString("book_id"));
                lbl_bookName.setText(rs.getString("book_name"));
                lbl_author.setText(rs.getString("author"));
                lbl_quantity.setText(rs.getString("quantity"));
                lbl_bookError.setText("");
            } else {
                lbl_bookError.setText("Book id does not exists!");
                lbl_bookId.setText("");
                lbl_bookName.setText("");
                lbl_author.setText("");
                lbl_quantity.setText("");
            }

        } catch (SQLException e) {
            e.getMessage();
        }
    }

    // to fetch the student details from the database and display it to student details panel
    public void getStudentDetails() {
        int studentId = Integer.parseInt(txt_studentId.getText());
        try {
            Connection con = NewjframePackage.DBConnection.getConnection();
            String sql = "select * from student_details where student_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, studentId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                lbl_studentId.setText(rs.getString("student_id"));
                lbl_studentName.setText(rs.getString("name"));
                lbl_course.setText(rs.getString("course"));
                lbl_branch.setText(rs.getString("branch"));
                lbl_studentError.setText("");
            } else {
                lbl_studentError.setText("Student id does not exists!");
                lbl_studentId.setText("");
                lbl_studentName.setText("");
                lbl_course.setText("");
                lbl_branch.setText("");
            }

        } catch (SQLException e) {
            e.getMessage();
        }
    }

    // insert issue book details to database
    public boolean issueBook() {
        boolean isIssued = false;
        int bookId = Integer.parseInt(txt_bookId.getText());
        int studentId = Integer.parseInt(txt_studentId.getText());
        String bookName = lbl_bookName.getText();
        String studentName = capitalizeFirstLetterOnly(lbl_studentName.getText());

        Date uIssueDate = date_issueDate.getDatoFecha();
        Date uDueDate = date_dueDate.getDatoFecha();

        Long l1 = uIssueDate.getTime();
        long l2 = uDueDate.getTime();

        java.sql.Date sIssueDate = new java.sql.Date(l1);
        java.sql.Date sDueDate = new java.sql.Date(l2);

        try {
            Connection con = NewjframePackage.DBConnection.getConnection();
            String sql = "insert into issue_book_details(book_id,book_name,student_id,student_name,issue_date,due_date,status) values (?,?,?,?,?,?,?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, bookId);
            pst.setString(2, bookName);
            pst.setInt(3, studentId);
            pst.setString(4, studentName);
            pst.setDate(5, sIssueDate);
            pst.setDate(6, sDueDate);
            pst.setString(7, "pending");

            int rowCount = pst.executeUpdate();  //executeUpdate method returns rowcount in integer hence stored in variable , and is a non-select query so executeUpdate used
            // with select query, ececuteQuery is used and returns result set object
            if (rowCount > 0) {
                isIssued = true;
            } else {
                isIssued = false;
            }

        } catch (SQLException e) {
            e.getMessage();
        }
        return isIssued;
    }

    // updating book count when the book is issued
    public void updateBookCount() {
        int bookId = Integer.parseInt(txt_bookId.getText());
        try {
            Connection con = NewjframePackage.DBConnection.getConnection();
            String sql = "update book_details set quantity = quantity - 1 where book_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, bookId);
            int rowCount = pst.executeUpdate();  //executeUpdate method returns rowcount in integer hence stored in variable , and is a non-select query so executeUpdate used
            // with select query, ececuteQuery is used and returns result set object
            if (rowCount > 0) {
                JOptionPane.showMessageDialog(this, "Book Count Updated");
                int initialCount = Integer.parseInt(lbl_quantity.getText());
                lbl_quantity.setText(Integer.toString(initialCount - 1));
            } else {
                JOptionPane.showMessageDialog(this, "Can't Update Book Count");
            }
        } catch (HeadlessException | NumberFormatException | SQLException e) {
            e.getMessage();
        }
    }

    //checking whether book already allocated to the student or not
    public boolean isAlreadyIssued() {
        boolean isAlreadyIssued = false;
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
                isAlreadyIssued = true;
            } else {
                isAlreadyIssued = false;
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return isAlreadyIssued;
    }

    // after completing return request by admin , request should be reomved from book_requests
    public void removeIssueReqFromTable() {
        int bookId = Integer.parseInt(txt_bookId.getText());
        String sName = capitalizeFirstLetterOnly(lbl_studentName.getText());

        try {
            Connection con = NewjframePackage.DBConnection.getConnection();
            String sql = "delete from book_issue_requests where book_id = ? and student_name = ? and requests_to = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, bookId);
            pst.setString(2, sName);
            pst.setString(3, "Issue");
            int rowCount = pst.executeUpdate();  //executeUpdate method returns rowcount in integer hence stored in variable , and is a non-select query so executeUpdate used
            // with select query, ececuteQuery is used and returns result set object
            if (rowCount > 0) {
                JOptionPane.showMessageDialog(this, "Student Request Removed from table Sucessfully");
            } else {
                JOptionPane.showMessageDialog(this, "Student Request Removal failure");
            }
        } catch (HeadlessException | SQLException e) {
            e.getMessage();
        }
    }

    //cheking book admin is issuing is ever requested or not
    public boolean validRequest() {
        boolean isRequested = false;
        int bookId = Integer.parseInt(txt_bookId.getText());
        try {
            Connection con = NewjframePackage.DBConnection.getConnection();
            String sql = "select book_id , requests_to from book_issue_requests where book_id = ? and requests_to = 'Issue'";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, bookId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                isRequested = true;

            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return isRequested;

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
        jPanel2 = new javax.swing.JPanel();
        lbl_branch = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        lbl_studentId = new javax.swing.JLabel();
        lbl_studentName = new javax.swing.JLabel();
        lbl_course = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        lbl_studentError = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lbl_quantity = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        lbl_bookError = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        lbl_bookId = new javax.swing.JLabel();
        lbl_bookName = new javax.swing.JLabel();
        lbl_author = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        txt_bookId = new app.bolivia.swing.JCTextField();
        jLabel36 = new javax.swing.JLabel();
        txt_studentId = new app.bolivia.swing.JCTextField();
        date_issueDate = new rojeru_san.componentes.RSDateChooser();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        date_dueDate = new rojeru_san.componentes.RSDateChooser();
        rSMaterialButtonCircle2 = new necesario.RSMaterialButtonCircle();
        jPanel12 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(102, 0, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_branch.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        lbl_branch.setForeground(new java.awt.Color(255, 255, 255));
        lbl_branch.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel2.add(lbl_branch, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 500, 190, 20));

        jLabel35.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("Student Name  :");
        jPanel2.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 320, -1, 20));

        jLabel37.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("Course Name  :");
        jPanel2.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 410, 120, 20));

        jLabel38.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText("Branch  :");
        jPanel2.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 500, 70, 20));

        jLabel34.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setText("Student id  :");
        jPanel2.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 240, 100, 20));

        lbl_studentId.setBackground(new java.awt.Color(0, 0, 0));
        lbl_studentId.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        lbl_studentId.setForeground(new java.awt.Color(255, 255, 255));
        lbl_studentId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel2.add(lbl_studentId, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 240, 190, 20));

        lbl_studentName.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        lbl_studentName.setForeground(new java.awt.Color(255, 255, 255));
        lbl_studentName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel2.add(lbl_studentName, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 320, 190, 20));

        lbl_course.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        lbl_course.setForeground(new java.awt.Color(255, 255, 255));
        lbl_course.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel2.add(lbl_course, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 410, 190, 20));

        jLabel2.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 25)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Student_Registration_100px_2.png"))); // NOI18N
        jLabel2.setText(" STUDENT DETAILS");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 60, 330, 100));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 370, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 370, 5));

        lbl_studentError.setFont(new java.awt.Font("Yu Gothic UI", 1, 24)); // NOI18N
        lbl_studentError.setForeground(new java.awt.Color(255, 255, 102));
        lbl_studentError.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel2.add(lbl_studentError, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 600, 330, 50));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 370, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 580, -1, -1));

        jPanel3.setBackground(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 720, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 0, -1, -1));

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 720, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

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

        jPanel2.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 230, 40));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 0, 430, 720));

        jPanel5.setBackground(new java.awt.Color(204, 51, 0));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setBackground(new java.awt.Color(102, 0, 255));

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
                .addContainerGap(8, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 4, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 40));

        jLabel4.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 25)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Literature_100px_1.png"))); // NOI18N
        jLabel4.setText(" BOOK DETAILS");
        jPanel5.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 60, 280, 100));

        lbl_quantity.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        lbl_quantity.setForeground(new java.awt.Color(255, 255, 255));
        lbl_quantity.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel5.add(lbl_quantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 500, 190, 20));

        jLabel42.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(255, 255, 255));
        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel42.setText("Book Name  :");
        jPanel5.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 320, -1, 20));

        jLabel43.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(255, 255, 255));
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel43.setText("Author Name  :");
        jPanel5.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 410, 120, 20));

        lbl_bookError.setFont(new java.awt.Font("Yu Gothic UI", 1, 24)); // NOI18N
        lbl_bookError.setForeground(new java.awt.Color(255, 255, 102));
        lbl_bookError.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel5.add(lbl_bookError, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 600, 350, 50));

        jLabel45.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(255, 255, 255));
        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel45.setText("Book id  :");
        jPanel5.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 240, 70, 20));

        lbl_bookId.setBackground(new java.awt.Color(0, 0, 0));
        lbl_bookId.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        lbl_bookId.setForeground(new java.awt.Color(255, 255, 255));
        lbl_bookId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel5.add(lbl_bookId, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 240, 190, 20));

        lbl_bookName.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        lbl_bookName.setForeground(new java.awt.Color(255, 255, 255));
        lbl_bookName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel5.add(lbl_bookName, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 320, 190, 20));

        lbl_author.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        lbl_author.setForeground(new java.awt.Color(255, 255, 255));
        lbl_author.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel5.add(lbl_author, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 410, 190, 20));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 370, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanel5.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 370, 5));

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
        jLabel46.setText("Quantity  :");
        jPanel5.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 500, 80, 20));

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 370, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanel5.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 580, 370, 5));

        jPanel15.setBackground(new java.awt.Color(204, 0, 0));

        jLabel7.setBackground(new java.awt.Color(153, 0, 0));
        jLabel7.setFont(new java.awt.Font("Verdana", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Rewind_48px.png"))); // NOI18N
        jLabel7.setText("View Requests");
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 4, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 0, -1, -1));

        jPanel16.setBackground(new java.awt.Color(204, 0, 0));

        jLabel8.setBackground(new java.awt.Color(153, 0, 0));
        jLabel8.setFont(new java.awt.Font("Verdana", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Rewind_48px.png"))); // NOI18N
        jLabel8.setText("View Requests");
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 4, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 0, -1, -1));

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 430, 720));

        jLabel1.setBackground(new java.awt.Color(204, 0, 0));
        jLabel1.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 25)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 0, 51));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Books_52px_1.png"))); // NOI18N
        jLabel1.setText(" ISSUE BOOK");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 80, 210, 70));

        jPanel4.setBackground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 390, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(884, 180, 390, 5));

        jLabel10.setBackground(new java.awt.Color(153, 153, 153));
        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("X");
        jLabel10.setAlignmentY(0.2F);
        jLabel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(1300, 0, 40, 30));

        jPanel9.setBackground(new java.awt.Color(102, 0, 255));
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

        jPanel1.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(1256, 0, 44, 40));

        jLabel33.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(204, 0, 0));
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("Book id :");
        jPanel1.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 240, 70, 20));

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
        jPanel1.add(txt_bookId, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 230, 290, -1));

        jLabel36.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(204, 0, 0));
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText("Issue Date :");
        jPanel1.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 410, 90, 20));

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
        jPanel1.add(txt_studentId, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 310, 290, -1));

        date_issueDate.setColorBackground(new java.awt.Color(204, 0, 0));
        date_issueDate.setColorForeground(new java.awt.Color(255, 51, 51));
        date_issueDate.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        date_issueDate.setPlaceholder("Select Issuing date...");
        jPanel1.add(date_issueDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 400, 290, -1));

        jLabel39.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(204, 0, 0));
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setText("Student id :");
        jPanel1.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 320, 90, 20));

        jLabel40.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(204, 0, 0));
        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel40.setText("Due Date :");
        jPanel1.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 500, 90, 20));

        date_dueDate.setColorBackground(new java.awt.Color(204, 0, 0));
        date_dueDate.setColorForeground(new java.awt.Color(255, 51, 51));
        date_dueDate.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        date_dueDate.setPlaceholder("Select Due date...");
        jPanel1.add(date_dueDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 490, 290, -1));

        rSMaterialButtonCircle2.setBackground(new java.awt.Color(204, 51, 0));
        rSMaterialButtonCircle2.setText("ISSUE BOOK");
        rSMaterialButtonCircle2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rSMaterialButtonCircle2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonCircle2ActionPerformed(evt);
            }
        });
        jPanel1.add(rSMaterialButtonCircle2, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 600, 330, 60));

        jPanel12.setBackground(new java.awt.Color(204, 0, 0));

        jLabel6.setBackground(new java.awt.Color(153, 0, 0));
        jLabel6.setFont(new java.awt.Font("Verdana", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Rewind_48px.png"))); // NOI18N
        jLabel6.setText("View Requests");
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 4, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 0, -1, -1));

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 720, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(425, 0, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1300, 720));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        HomePage home = new HomePage();
        home.setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jLabel10MouseClicked

    private void jLabel11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jLabel11MouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jLabel5MouseClicked

    private void txt_bookIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_bookIdFocusLost
        if (!txt_bookId.getText().isEmpty()) {
            getBookDetails();
        }

    }//GEN-LAST:event_txt_bookIdFocusLost

    private void txt_bookIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_bookIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_bookIdActionPerformed

    private void txt_studentIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_studentIdFocusLost
        if (!txt_studentId.getText().isEmpty()) {
            getStudentDetails();
        }
    }//GEN-LAST:event_txt_studentIdFocusLost

    private void txt_studentIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_studentIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_studentIdActionPerformed

    private void rSMaterialButtonCircle2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle2ActionPerformed
        if (lbl_quantity.getText().equals("0")) {
            JOptionPane.showMessageDialog(this, "Book is not Available to issue");
        } else {
            if (isAlreadyIssued() == false) {
                if (validRequest()) {
                    if (issueBook()) {
                        JOptionPane.showMessageDialog(this, "Book issued Sucessfully");
                        removeIssueReqFromTable();
                        updateBookCount();
                    } else {
                        JOptionPane.showMessageDialog(this, "Can't issue the Book");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Student never requested this book to be issued");
                    JOptionPane.showMessageDialog(this, "Book issue failure");

                }

            } else {
                JOptionPane.showMessageDialog(this, "This Student had already issued this book");
            }
        }


    }//GEN-LAST:event_rSMaterialButtonCircle2ActionPerformed

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        RequestedBooks rq = new RequestedBooks();
        rq.setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel6MouseClicked

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel7MouseClicked

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel8MouseClicked

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
            java.util.logging.Logger.getLogger(IssueBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IssueBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IssueBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IssueBook.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new IssueBook().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rojeru_san.componentes.RSDateChooser date_dueDate;
    private rojeru_san.componentes.RSDateChooser date_issueDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel lbl_author;
    private javax.swing.JLabel lbl_bookError;
    private javax.swing.JLabel lbl_bookId;
    private javax.swing.JLabel lbl_bookName;
    private javax.swing.JLabel lbl_branch;
    private javax.swing.JLabel lbl_course;
    private javax.swing.JLabel lbl_quantity;
    private javax.swing.JLabel lbl_studentError;
    private javax.swing.JLabel lbl_studentId;
    private javax.swing.JLabel lbl_studentName;
    private necesario.RSMaterialButtonCircle rSMaterialButtonCircle2;
    private app.bolivia.swing.JCTextField txt_bookId;
    private app.bolivia.swing.JCTextField txt_studentId;
    // End of variables declaration//GEN-END:variables
}
