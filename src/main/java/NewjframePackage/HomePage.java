package NewjframePackage;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

public class HomePage extends javax.swing.JFrame {

    Color mouseEnterColor = new Color(0, 0, 0);  ///color specified for hover effect
    Color mouseExitColor = new Color(51, 51, 51);

    public HomePage() {
        initComponents();
        showPieChart();
        setBookDetailsToTable();
        setStudentDetailsToTable();
        setDataToCards();

    }
    DefaultTableModel model;

    public void setBookDetailsToTable() {

        try {
            Connection con = NewjframePackage.DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from book_details");  //executeQuery method return resultset hence stored in result set

            //fething data using DefaultTable model
            while (rs.next()) {
                String bookId = rs.getString("book_id"); //passed string values are same as name of coulmns in database
                String bookName = rs.getString("book_name");
                String author = rs.getString("author");
                int quantity = rs.getInt("quantity");
                Object[] obj = {bookId, bookName, author, quantity};
                model = (DefaultTableModel) tbl_bookDetails.getModel();
                model.addRow(obj); // object array is passed to add each new row in book table
            }

        } catch (SQLException e) {
            e.getMessage();
        }

    }

    public void setStudentDetailsToTable() {

        try {
            Connection con = NewjframePackage.DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from student_details");  //executeQuery method return resultset hence stored in result set

            //fething data using DefaultTable model
            while (rs.next()) {
                String studentId = rs.getString("student_id"); //passed string values are same as name of coulmns in database
                String studentName = rs.getString("name");
                String course = rs.getString("course");
                String branch = rs.getString("branch");
                Object[] obj = {studentId, studentName, course, branch};
                model = (DefaultTableModel) tbl_studentDetails.getModel();
                model.addRow(obj); // object array is passed to add each new row in book table
            }
        } catch (SQLException e) {
            e.getMessage();
        }

    }

    public void setDataToCards() {
        Statement st = null;
        ResultSet rs = null;

        // Get the current date
        long l = System.currentTimeMillis();
        Date todayDate = new Date(l);
        java.sql.Date sqlTodayDate = new java.sql.Date(todayDate.getTime());

        try {
            Connection con = NewjframePackage.DBConnection.getConnection();
            st = con.createStatement();

            // Getting the number of books
            rs = st.executeQuery("SELECT COUNT(*) FROM book_details");
            if (rs.next()) {
                lbl_noOfBooks.setText(Integer.toString(rs.getInt(1))); // Retrieves the value from the first column which eventually contains total number of books
            }

            // Getting the number of students
            rs = st.executeQuery("SELECT COUNT(*) FROM student_details");
            if (rs.next()) {
                lbl_noOfStudents.setText(Integer.toString(rs.getInt(1)));
            }

            // Getting the number of issued books
            rs = st.executeQuery("SELECT COUNT(*) FROM issue_book_details where status = 'pending'");
            if (rs.next()) {
                lbl_noOfIssuedBooks.setText(Integer.toString(rs.getInt(1)));
            }

            // Getting the number of defaulters
            String defaulterQuery = "SELECT COUNT(*) FROM issue_book_details WHERE due_date < ? AND status = ?";
            PreparedStatement pstmt = con.prepareStatement(defaulterQuery);
            pstmt.setDate(1, sqlTodayDate);
            pstmt.setString(2, "pending");
            rs = pstmt.executeQuery();
            if (rs.next()) {
                lbl_noOfDefaulters.setText(Integer.toString(rs.getInt(1)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the ResultSet and Statement
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public final void showPieChart() {

        //create dataset
        DefaultPieDataset barDataset = new DefaultPieDataset();
        try {
            Connection con = NewjframePackage.DBConnection.getConnection();
            String sql = "select book_name , count(*) as issue_count from issue_book_details where status = 'pending' group by book_name";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                barDataset.setValue(rs.getString("book_name"), Double.valueOf(rs.getDouble("issue_count")));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving data: " + e.getMessage());
        }

        //create chart
        JFreeChart piechart = ChartFactory.createPieChart("*** Issued Books Details ***", barDataset, true, true, false); 
                                                                                    // true : legend(colored boxes with name what shows which color represents which book
                                                                                    // true : tooltips(name shown on hover of piechart slice)
                                                                                    // false : if true, it Generate URLs for the chart

        PiePlot piePlot = (PiePlot) piechart.getPlot();

        //changing pie chart blocks colors
        piePlot.setSectionPaint("IPhone 5s", new Color(255, 255, 102));
        piePlot.setSectionPaint("SamSung Grand", new Color(102, 255, 102));
        piePlot.setSectionPaint("MotoG", new Color(255, 102, 153));
        piePlot.setSectionPaint("Nokia Lumia", new Color(0, 204, 204));

        piePlot.setBackgroundPaint(Color.white);

        //create chartPanel to display chart(graph)
        ChartPanel barChartPanel = new ChartPanel(piechart);
        panelPieChart.removeAll();
        panelPieChart.add(barChartPanel, BorderLayout.CENTER);
        panelPieChart.validate();
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
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        lbl_noOfBooks = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        lbl_noOfStudents = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        lbl_noOfIssuedBooks = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        lbl_noOfDefaulters = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_studentDetails = new rojeru_san.complementos.RSTableMetro();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_bookDetails = new rojeru_san.complementos.RSTableMetro();
        panelPieChart = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(102, 51, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI Light", 0, 12)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_menu_48px_1.png"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 13, 50, 30));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.setFont(new java.awt.Font("Segoe UI", 0, 8)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 56, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(67, 0, 4, 56));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/male_user_50px.png"))); // NOI18N
        jLabel2.setText("Welcome, Admin");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1150, 0, 220, 50));

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 28)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Library Management System");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 0, 360, 50));

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
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(1370, 0, 40, 30));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1410, 56));

        jPanel3.setBackground(new java.awt.Color(51, 51, 51));
        jPanel3.setForeground(new java.awt.Color(255, 255, 255));
        jPanel3.setFont(new java.awt.Font("Segoe UI", 0, 8)); // NOI18N
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(255, 51, 51));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Home_26px_2.png"))); // NOI18N
        jLabel4.setText("Home Page");
        jPanel4.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, 160, 30));

        jPanel3.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 280, 50));

        jLabel5.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Features ");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 80, 30));

        jPanel6.setBackground(new java.awt.Color(51, 51, 51));
        jPanel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel6MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel6MouseExited(evt);
            }
        });
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(153, 153, 153));
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Library_26px_1.png"))); // NOI18N
        jLabel7.setText("  View Requests");
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel7MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel7MouseExited(evt);
            }
        });
        jPanel6.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 170, 30));

        jPanel3.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 280, 280, 50));

        jPanel7.setBackground(new java.awt.Color(51, 51, 51));
        jPanel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel7MouseEntered(evt);
            }
        });
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(153, 153, 153));
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Book_26px.png"))); // NOI18N
        jLabel8.setText("  Manage Books");
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel8MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel8MouseExited(evt);
            }
        });
        jPanel7.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 170, 30));

        jPanel3.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 280, 50));

        jPanel8.setBackground(new java.awt.Color(51, 51, 51));
        jPanel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel8MouseClicked(evt);
            }
        });
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(153, 153, 153));
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Read_Online_26px.png"))); // NOI18N
        jLabel9.setText("  Manage Students");
        jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel9MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel9MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel9MouseExited(evt);
            }
        });
        jPanel8.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 190, 30));

        jPanel3.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 230, 280, 50));

        jPanel9.setBackground(new java.awt.Color(51, 51, 51));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(153, 153, 153));
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Sell_26px.png"))); // NOI18N
        jLabel11.setText("  Isuue Book");
        jLabel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel11MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel11MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel11MouseExited(evt);
            }
        });
        jPanel9.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 170, 30));

        jPanel3.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 330, 280, 50));

        jPanel10.setBackground(new java.awt.Color(51, 51, 51));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(153, 153, 153));
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Return_Purchase_26px.png"))); // NOI18N
        jLabel12.setText("  Retrun Book");
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel12MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel12MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel12MouseExited(evt);
            }
        });
        jPanel10.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 170, 30));

        jPanel3.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 380, 280, 50));

        jPanel11.setBackground(new java.awt.Color(51, 51, 51));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel13.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(153, 153, 153));
        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_View_Details_26px.png"))); // NOI18N
        jLabel13.setText("  View Records");
        jLabel13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel13MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel13MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel13MouseExited(evt);
            }
        });
        jPanel11.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 170, 30));

        jPanel3.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 430, 280, 50));

        jPanel12.setBackground(new java.awt.Color(51, 51, 51));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel14.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(153, 153, 153));
        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Books_26px.png"))); // NOI18N
        jLabel14.setText("  View Issued Books");
        jLabel14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel14MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel14MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel14MouseExited(evt);
            }
        });
        jPanel12.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 200, 30));

        jPanel3.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 480, 280, 50));

        jPanel13.setBackground(new java.awt.Color(51, 51, 51));
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(153, 153, 153));
        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Conference_26px.png"))); // NOI18N
        jLabel15.setText("  Defaulter List");
        jLabel15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel15MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel15MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel15MouseExited(evt);
            }
        });
        jPanel13.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 170, 30));

        jPanel3.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 530, 280, 50));

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
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel16MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel16MouseExited(evt);
            }
        });
        jPanel14.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, 140, 30));

        jPanel3.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 590, 280, 50));

        jPanel19.setBackground(new java.awt.Color(51, 51, 51));
        jPanel19.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel17.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(153, 153, 153));
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Library_26px_1.png"))); // NOI18N
        jLabel17.setText("LMS Dashboard");
        jPanel19.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, 170, 30));

        jPanel3.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 280, 50));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 55, 280, 670));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel15.setBackground(new java.awt.Color(204, 204, 204));
        jPanel15.setBorder(javax.swing.BorderFactory.createMatteBorder(15, 0, 0, 0, new java.awt.Color(255, 51, 51)));
        jPanel15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel15MouseClicked(evt);
            }
        });

        lbl_noOfBooks.setBackground(new java.awt.Color(255, 255, 255));
        lbl_noOfBooks.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        lbl_noOfBooks.setForeground(new java.awt.Color(51, 51, 51));
        lbl_noOfBooks.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Book_Shelf_50px.png"))); // NOI18N
        lbl_noOfBooks.setText("10");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lbl_noOfBooks)
                .addContainerGap(44, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lbl_noOfBooks)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 50, 180, 110));

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Segoe UI Variable", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 51, 51));
        jLabel6.setText("Student Details");
        jPanel5.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 180, 140, 20));

        jLabel18.setBackground(new java.awt.Color(255, 255, 255));
        jLabel18.setFont(new java.awt.Font("Segoe UI Variable", 1, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(51, 51, 51));
        jLabel18.setText("No. of Students");
        jPanel5.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 20, 140, 20));

        jPanel16.setBackground(new java.awt.Color(204, 204, 204));
        jPanel16.setBorder(javax.swing.BorderFactory.createMatteBorder(15, 0, 0, 0, new java.awt.Color(102, 0, 255)));
        jPanel16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel16MouseClicked(evt);
            }
        });

        lbl_noOfStudents.setBackground(new java.awt.Color(255, 255, 255));
        lbl_noOfStudents.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        lbl_noOfStudents.setForeground(new java.awt.Color(51, 51, 51));
        lbl_noOfStudents.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_People_50px.png"))); // NOI18N
        lbl_noOfStudents.setText("10");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lbl_noOfStudents)
                .addContainerGap(44, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lbl_noOfStudents)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 50, 180, 110));

        jLabel20.setBackground(new java.awt.Color(255, 255, 255));
        jLabel20.setFont(new java.awt.Font("Segoe UI Variable", 1, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(51, 51, 51));
        jLabel20.setText("Issued Books");
        jPanel5.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 20, 120, 20));

        jPanel17.setBackground(new java.awt.Color(204, 204, 204));
        jPanel17.setBorder(javax.swing.BorderFactory.createMatteBorder(15, 0, 0, 0, new java.awt.Color(255, 51, 51)));
        jPanel17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel17MouseClicked(evt);
            }
        });

        lbl_noOfIssuedBooks.setBackground(new java.awt.Color(255, 255, 255));
        lbl_noOfIssuedBooks.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        lbl_noOfIssuedBooks.setForeground(new java.awt.Color(51, 51, 51));
        lbl_noOfIssuedBooks.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Sell_50px.png"))); // NOI18N
        lbl_noOfIssuedBooks.setText("10");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lbl_noOfIssuedBooks)
                .addContainerGap(44, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lbl_noOfIssuedBooks)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 50, 180, 110));

        jLabel22.setBackground(new java.awt.Color(255, 255, 255));
        jLabel22.setFont(new java.awt.Font("Segoe UI Variable", 1, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(51, 51, 51));
        jLabel22.setText("Defaulters List");
        jPanel5.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 20, 130, 20));

        jPanel18.setBackground(new java.awt.Color(204, 204, 204));
        jPanel18.setBorder(javax.swing.BorderFactory.createMatteBorder(15, 0, 0, 0, new java.awt.Color(102, 0, 255)));
        jPanel18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel18MouseClicked(evt);
            }
        });

        lbl_noOfDefaulters.setBackground(new java.awt.Color(255, 255, 255));
        lbl_noOfDefaulters.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        lbl_noOfDefaulters.setForeground(new java.awt.Color(51, 51, 51));
        lbl_noOfDefaulters.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_List_of_Thumbnails_50px.png"))); // NOI18N
        lbl_noOfDefaulters.setText("10");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lbl_noOfDefaulters)
                .addContainerGap(44, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lbl_noOfDefaulters)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 50, 180, 110));

        tbl_studentDetails.setBackground(new java.awt.Color(255, 255, 204));
        tbl_studentDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Student id", "Name", "Course", "Branch"
            }
        ));
        tbl_studentDetails.setColorBackgoundHead(new java.awt.Color(102, 0, 255));
        tbl_studentDetails.setColorFilasBackgound1(new java.awt.Color(255, 255, 204));
        tbl_studentDetails.setColorFilasBackgound2(new java.awt.Color(255, 255, 204));
        tbl_studentDetails.setColorSelBackgound(new java.awt.Color(255, 0, 51));
        tbl_studentDetails.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        tbl_studentDetails.setFuenteFilas(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        tbl_studentDetails.setFuenteFilasSelect(new java.awt.Font("Yu Gothic UI", 1, 20)); // NOI18N
        tbl_studentDetails.setFuenteHead(new java.awt.Font("Yu Gothic UI Semibold", 1, 20)); // NOI18N
        tbl_studentDetails.setRowHeight(30);
        jScrollPane1.setViewportView(tbl_studentDetails);

        jPanel5.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 590, 190));

        jLabel24.setBackground(new java.awt.Color(255, 255, 255));
        jLabel24.setFont(new java.awt.Font("Segoe UI Variable", 1, 18)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(51, 51, 51));
        jLabel24.setText("No. of Books");
        jPanel5.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 120, 20));

        jLabel25.setBackground(new java.awt.Color(255, 255, 255));
        jLabel25.setFont(new java.awt.Font("Segoe UI Variable", 1, 18)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(51, 51, 51));
        jLabel25.setText("Book Details");
        jPanel5.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 420, 140, 20));

        tbl_bookDetails.setBackground(new java.awt.Color(255, 255, 204));
        tbl_bookDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Book id", "Name", "Author", "Quantity"
            }
        ));
        tbl_bookDetails.setColorBackgoundHead(new java.awt.Color(102, 0, 255));
        tbl_bookDetails.setColorFilasBackgound1(new java.awt.Color(255, 255, 204));
        tbl_bookDetails.setColorFilasBackgound2(new java.awt.Color(255, 255, 204));
        tbl_bookDetails.setColorSelBackgound(new java.awt.Color(255, 0, 51));
        tbl_bookDetails.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        tbl_bookDetails.setFuenteFilas(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        tbl_bookDetails.setFuenteFilasSelect(new java.awt.Font("Yu Gothic UI", 1, 20)); // NOI18N
        tbl_bookDetails.setFuenteHead(new java.awt.Font("Yu Gothic UI Semibold", 1, 20)); // NOI18N
        tbl_bookDetails.setRowHeight(30);
        jScrollPane2.setViewportView(tbl_bookDetails);

        jPanel5.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 450, 590, 200));

        panelPieChart.setBackground(new java.awt.Color(204, 204, 204));
        panelPieChart.setLayout(new java.awt.BorderLayout());
        jPanel5.add(panelPieChart, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 220, 470, 420));

        getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 55, 1130, 670));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jLabel10MouseClicked

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        ManageBooks books = new ManageBooks();
        books.setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel8MouseClicked

    private void jPanel7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel7MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel7MouseEntered

    private void jLabel8MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseEntered
        jPanel7.setBackground(mouseEnterColor);
    }//GEN-LAST:event_jLabel8MouseEntered

    private void jLabel8MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseExited
        jPanel7.setBackground(mouseExitColor);
    }//GEN-LAST:event_jLabel8MouseExited

    private void jLabel9MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseEntered
        jPanel8.setBackground(mouseEnterColor);
    }//GEN-LAST:event_jLabel9MouseEntered

    private void jLabel9MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseExited
        jPanel8.setBackground(mouseExitColor);
    }//GEN-LAST:event_jLabel9MouseExited

    private void jLabel11MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseEntered
        jPanel9.setBackground(mouseEnterColor);
    }//GEN-LAST:event_jLabel11MouseEntered

    private void jLabel11MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseExited
        jPanel9.setBackground(mouseExitColor);
    }//GEN-LAST:event_jLabel11MouseExited

    private void jLabel12MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseEntered
        jPanel10.setBackground(mouseEnterColor);
    }//GEN-LAST:event_jLabel12MouseEntered

    private void jLabel12MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseExited
        jPanel10.setBackground(mouseExitColor);
    }//GEN-LAST:event_jLabel12MouseExited

    private void jLabel13MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel13MouseEntered
        jPanel11.setBackground(mouseEnterColor);
    }//GEN-LAST:event_jLabel13MouseEntered

    private void jLabel13MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel13MouseExited
        jPanel11.setBackground(mouseExitColor);
    }//GEN-LAST:event_jLabel13MouseExited

    private void jLabel14MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseEntered
        jPanel12.setBackground(mouseEnterColor);
    }//GEN-LAST:event_jLabel14MouseEntered

    private void jLabel14MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseExited
        jPanel12.setBackground(mouseExitColor);
    }//GEN-LAST:event_jLabel14MouseExited

    private void jLabel15MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MouseEntered
        jPanel13.setBackground(mouseEnterColor);
    }//GEN-LAST:event_jLabel15MouseEntered

    private void jLabel15MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MouseExited
        jPanel13.setBackground(mouseExitColor);
    }//GEN-LAST:event_jLabel15MouseExited

    private void jLabel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseClicked
        ManageStudents student = new ManageStudents();
        student.setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel9MouseClicked

    private void jLabel11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseClicked
        IssueBook book = new IssueBook();
        book.setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel11MouseClicked

    private void jLabel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseClicked
        ReturnBook book = new ReturnBook();
        book.setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel12MouseClicked

    private void jLabel13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel13MouseClicked
        ViewAllRecords records = new ViewAllRecords();
        records.setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel13MouseClicked

    private void jLabel14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseClicked
        IssuedBookDetails details = new IssuedBookDetails();
        details.setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel14MouseClicked

    private void jLabel15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MouseClicked
        DefaulterList defaulter = new DefaulterList();
        defaulter.setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel15MouseClicked

    private void jLabel16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel16MouseClicked
        // Create and show a message dialog
        JOptionPane optionPane = new JOptionPane("Logging Out.....", JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog(this, "Message");

        // Create a Timer to close the dialog after 1.2 seconds
        Timer timer = new Timer(1200, new ActionListener() {
            @Override
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

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        RequestedBooks req = new RequestedBooks();
        req.setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel7MouseClicked

    private void jPanel6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel6MouseEntered

    }//GEN-LAST:event_jPanel6MouseEntered

    private void jPanel6MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel6MouseExited

    }//GEN-LAST:event_jPanel6MouseExited

    private void jPanel15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel15MouseClicked
        ManageBooks bk = new ManageBooks();
        bk.setVisible(true);
        dispose();
    }//GEN-LAST:event_jPanel15MouseClicked

    private void jPanel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel8MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel8MouseClicked

    private void jPanel16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel16MouseClicked
        ManageStudents student = new ManageStudents();
        student.setVisible(true);
        dispose();
    }//GEN-LAST:event_jPanel16MouseClicked

    private void jPanel17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel17MouseClicked
        IssuedBookDetails details = new IssuedBookDetails();
        details.setVisible(true);
        dispose();
    }//GEN-LAST:event_jPanel17MouseClicked

    private void jPanel18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel18MouseClicked
        DefaulterList defaulter = new DefaulterList();
        defaulter.setVisible(true);
        dispose();
    }//GEN-LAST:event_jPanel18MouseClicked

    private void jLabel7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseEntered
        jPanel6.setBackground(mouseEnterColor);
    }//GEN-LAST:event_jLabel7MouseEntered

    private void jLabel7MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseExited
        jPanel6.setBackground(mouseExitColor);
    }//GEN-LAST:event_jLabel7MouseExited

    private void jLabel16MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel16MouseEntered
        Color LogoutEnter = new Color(51, 51, 255);

        jPanel14.setBackground(LogoutEnter);
    }//GEN-LAST:event_jLabel16MouseEntered

    private void jLabel16MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel16MouseExited
        Color LogoutExit = new Color(102, 51, 255);
        jPanel14.setBackground(LogoutExit);
    }//GEN-LAST:event_jLabel16MouseExited

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
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomePage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
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
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbl_noOfBooks;
    private javax.swing.JLabel lbl_noOfDefaulters;
    private javax.swing.JLabel lbl_noOfIssuedBooks;
    private javax.swing.JLabel lbl_noOfStudents;
    private javax.swing.JPanel panelPieChart;
    private rojeru_san.complementos.RSTableMetro tbl_bookDetails;
    private rojeru_san.complementos.RSTableMetro tbl_studentDetails;
    // End of variables declaration//GEN-END:variables
}
