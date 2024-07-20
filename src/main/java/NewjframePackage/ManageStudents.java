package NewjframePackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class ManageStudents extends javax.swing.JFrame {

    //Attributes 
    String studentName, course, branch;
    int studentId;
    DefaultTableModel model; //using this we will set the values in the book table after fetching actual value form database

    public ManageStudents() {
        initComponents();
        setStudentDetailsToTable();
    }

    public static String capitalizeFirstLetterOnly(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    //to set book details into the table
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
                Object[] obj = {studentId, studentName, course, branch}; //order of elements (that are objects individually) is as same as table from left to right
                model = (DefaultTableModel) tbl_studentDetails.getModel();
                model.addRow(obj); // object array is passed to add each new row in book table
            }
        } catch (SQLException e) {
            e.getMessage();
        }

    }
    // To check user trying to add student with id which already exits

    public boolean checkStudentExists() {
        int id = Integer.parseInt(txt_studentId.getText()); //get id entered by user 
        try {
            Connection con = NewjframePackage.DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select student_id from student_details");  //executeQuery method returns collection of rows hence stored in result set
            while (rs.next()) {
                int studentIdFromDB = rs.getInt("student_id"); //get book id at current row 
                if (id == studentIdFromDB) {
                    JOptionPane.showMessageDialog(this, "Student Already exists with this id!");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return true;
    }

    //to add student to student_details table
    public boolean addStudent() {
        boolean isAdded = false;
        if (txt_studentId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "please Enter Student id");
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
        if (checkStudentExists()) {
            studentId = Integer.parseInt(txt_studentId.getText()); //returns a string so convert it to int as book_id is of type int
            studentName = capitalizeFirstLetterOnly(txt_studentName.getText());
            course = combo_CourseName.getSelectedItem().toString(); //return a object hence converted to string
            branch = combo_branch.getSelectedItem().toString();

            try {
                Connection con = NewjframePackage.DBConnection.getConnection();
                PreparedStatement pst = con.prepareStatement("insert into student_details values (?,?,?,?);");
                pst.setInt(1, studentId);
                pst.setString(2, studentName);
                pst.setString(3, course);
                pst.setString(4, branch);

                int rowCount = pst.executeUpdate();  //executeUpdate method returns rowcount in integer hence stored in variable , and is a non-select query so executeUpdate used
                // with select query, ececuteQuery is used and returns result set object
                if (rowCount > 0) {
                    isAdded = true;
                } else {
                    isAdded = false;
                }

            } catch (SQLException e) {
                e.getMessage();
            }
        }

        return isAdded;
    }

    // on updation of credentials , to check that user is trying to update a valid id that exists in db or he is entering any random student id which dont exist is db
    public boolean checkStudentId() {
        int id = Integer.parseInt(txt_studentId.getText()); //get id entered by user 
        try {
            Connection con = NewjframePackage.DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select student_id from student_details");  //executeQuery method returns collection of rows hence stored in result set
            while (rs.next()) {
                int studentIdFromDB = rs.getInt("student_id"); //get student id at current row 
                if (id == studentIdFromDB) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        JOptionPane.showMessageDialog(this, "Student id does not exists!");
        return false;
    }

    //to update student details
    public boolean updateStudent() {
        boolean isUpdated = false;
        if (txt_studentId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "please Enter Student id");
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
        if (checkStudentId()) {
            studentId = Integer.parseInt(txt_studentId.getText()); //returns a string so convert it to int as student_id is of type int
            studentName = capitalizeFirstLetterOnly(txt_studentName.getText());
            course = combo_CourseName.getSelectedItem().toString(); //return a object hence converted to string
            branch = combo_branch.getSelectedItem().toString();
            try {
                Connection con = NewjframePackage.DBConnection.getConnection();
                String sql = "update student_details set name = ? , course = ? , branch = ? where student_id = ?";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, studentName);
                pst.setString(2, course);
                pst.setString(3, branch);
                pst.setInt(4, studentId);
                int rowCount = pst.executeUpdate();
                if (rowCount > 0) {
                    isUpdated = true;
                } else {
                    isUpdated = false;
                }
            } catch (SQLException e) {
                e.getMessage();
            }
        }
        return isUpdated;
    }

    //Method to delete a book detail
    public boolean deleteStudent() {
        boolean isDeleted = false;
        if (txt_studentId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "please Enter Student id");
            return false;
        }
        if (checkStudentId()) {
            studentId = Integer.parseInt(txt_studentId.getText());
            try {
                Connection con = NewjframePackage.DBConnection.getConnection();
                String sql = "delete from student_details where student_id = ?";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setInt(1, studentId);
                int rowCount = pst.executeUpdate();
                if (rowCount > 0) {
                    isDeleted = true;
                } else {
                    isDeleted = false;
                }
            } catch (SQLException e) {
                e.getMessage();
            }
        }

        return isDeleted;
    }

    //Method to clear table 
    public void clearTable() {  // on click of add button for adding book, table should  be cleared first to remove existing data , 
        //and all data is fetched again from database with added book, updated data will be fetched by showBookDetailsToTable() method
        model = (DefaultTableModel) tbl_studentDetails.getModel();
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        txt_studentId = new app.bolivia.swing.JCTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        txt_studentName = new app.bolivia.swing.JCTextField();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        rSMaterialButtonCircle1 = new necesario.RSMaterialButtonCircle();
        rSMaterialButtonCircle2 = new necesario.RSMaterialButtonCircle();
        rSMaterialButtonCircle3 = new necesario.RSMaterialButtonCircle();
        combo_branch = new javax.swing.JComboBox<>();
        combo_CourseName = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_studentDetails = new rojeru_san.complementos.RSTableMetro();
        jLabel4 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        rSMaterialButtonCircle4 = new necesario.RSMaterialButtonCircle();
        rSMaterialButtonCircle5 = new necesario.RSMaterialButtonCircle();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 204));
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(102, 51, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 51, 51));

        jLabel2.setFont(new java.awt.Font("Verdana", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Rewind_48px.png"))); // NOI18N
        jLabel2.setText("     Home");
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 4, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 210, 40));

        jLabel33.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("Student id");
        jPanel1.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 120, 80, 20));

        txt_studentId.setBackground(new java.awt.Color(102, 51, 255));
        txt_studentId.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_studentId.setAlignmentY(0.0F);
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
        jPanel1.add(txt_studentId, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 150, 290, 30));

        jLabel32.setFont(new java.awt.Font("Copperplate Gothic Bold", 0, 14)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Contact_26px.png"))); // NOI18N
        jLabel32.setMaximumSize(new java.awt.Dimension(30, 30));
        jPanel1.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 140, 30, 50));

        jLabel34.setFont(new java.awt.Font("Copperplate Gothic Bold", 0, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Moleskine_26px.png"))); // NOI18N
        jLabel34.setMaximumSize(new java.awt.Dimension(30, 30));
        jPanel1.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, 40, 60));

        txt_studentName.setBackground(new java.awt.Color(102, 51, 255));
        txt_studentName.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
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
        jPanel1.add(txt_studentName, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 260, 290, 30));

        jLabel35.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("Student Name");
        jPanel1.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 230, -1, 20));

        jLabel36.setFont(new java.awt.Font("Copperplate Gothic Bold", 0, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Collaborator_Male_26px.png"))); // NOI18N
        jLabel36.setMaximumSize(new java.awt.Dimension(30, 30));
        jPanel1.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 360, 40, 50));

        jLabel37.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("Select Course");
        jPanel1.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 340, 100, 20));

        jLabel38.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText("Select Branch");
        jPanel1.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 460, 100, 20));

        jLabel39.setFont(new java.awt.Font("Copperplate Gothic Bold", 0, 14)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Unit_26px.png"))); // NOI18N
        jLabel39.setMaximumSize(new java.awt.Dimension(30, 30));
        jPanel1.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 470, 40, 70));

        rSMaterialButtonCircle1.setBackground(new java.awt.Color(255, 51, 51));
        rSMaterialButtonCircle1.setText("Delete");
        rSMaterialButtonCircle1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rSMaterialButtonCircle1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonCircle1ActionPerformed(evt);
            }
        });
        jPanel1.add(rSMaterialButtonCircle1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 580, 140, 70));

        rSMaterialButtonCircle2.setBackground(new java.awt.Color(255, 51, 51));
        rSMaterialButtonCircle2.setText("ADD");
        rSMaterialButtonCircle2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rSMaterialButtonCircle2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonCircle2ActionPerformed(evt);
            }
        });
        jPanel1.add(rSMaterialButtonCircle2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 580, 140, 70));

        rSMaterialButtonCircle3.setBackground(new java.awt.Color(255, 51, 51));
        rSMaterialButtonCircle3.setText("Update");
        rSMaterialButtonCircle3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rSMaterialButtonCircle3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonCircle3ActionPerformed(evt);
            }
        });
        jPanel1.add(rSMaterialButtonCircle3, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 580, 140, 70));

        combo_branch.setFont(new java.awt.Font("Verdana", 0, 17)); // NOI18N
        combo_branch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "General / Regular", "Mathematics (B.Sc)", "Physics (B.sc)", "Political Science(B.A)", "Computer Science (B.Tech)", "Electronics (B.Tech)", "Civil(B.Tech)", "Neuroscience (Ph.D.)", "Psychology (Ph.D.)", "Computer Science (Ph.D)", " ", " ", " ", " " }));
        jPanel1.add(combo_branch, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 490, 298, 40));

        combo_CourseName.setFont(new java.awt.Font("Verdana", 0, 17)); // NOI18N
        combo_CourseName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "B.Sc", "B.Tech", "MCA", "Ph.D.", "B.A", " " }));
        jPanel1.add(combo_CourseName, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 370, 298, 40));

        jPanel4.setBackground(new java.awt.Color(255, 0, 0));

        jLabel5.setFont(new java.awt.Font("Verdana", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Rewind_48px.png"))); // NOI18N
        jLabel5.setText("View Requests");
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 4, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 210, 40));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 450, 720));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(102, 0, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel5.setPreferredSize(new java.awt.Dimension(47, 40));

        jLabel3.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText(" X");
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(806, 0, 44, 40));

        tbl_studentDetails.setBackground(new java.awt.Color(255, 255, 204));
        tbl_studentDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Student id", "Name", "Course", "Branch"
            }
        ));
        tbl_studentDetails.setColorBackgoundHead(new java.awt.Color(102, 0, 255));
        tbl_studentDetails.setColorBordeFilas(new java.awt.Color(51, 51, 51));
        tbl_studentDetails.setColorFilasBackgound1(new java.awt.Color(255, 255, 153));
        tbl_studentDetails.setColorFilasBackgound2(new java.awt.Color(255, 255, 153));
        tbl_studentDetails.setColorSelBackgound(new java.awt.Color(255, 0, 0));
        tbl_studentDetails.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        tbl_studentDetails.setFuenteFilas(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        tbl_studentDetails.setFuenteFilasSelect(new java.awt.Font("Yu Gothic UI", 1, 20)); // NOI18N
        tbl_studentDetails.setFuenteHead(new java.awt.Font("Yu Gothic UI Semibold", 1, 20)); // NOI18N
        tbl_studentDetails.setRowHeight(30);
        tbl_studentDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_studentDetailsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbl_studentDetails);

        jPanel3.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, 790, 230));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 30)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 51, 51));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Student_Male_100px.png"))); // NOI18N
        jLabel4.setText(" Manage Students ");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 10, 360, -1));

        jPanel6.setBackground(new java.awt.Color(255, 51, 51));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 860, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 470, 860, 8));

        jLabel1.setFont(new java.awt.Font("Yu Gothic UI", 1, 25)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Add New Students to Database before Issuing the book .");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 500, 640, 60));

        jPanel7.setBackground(new java.awt.Color(255, 51, 51));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 850, 8));

        rSMaterialButtonCircle4.setBackground(new java.awt.Color(102, 0, 255));
        rSMaterialButtonCircle4.setText("ISSUE BOOK");
        rSMaterialButtonCircle4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rSMaterialButtonCircle4.setFont(new java.awt.Font("Yu Gothic UI", 1, 30)); // NOI18N
        rSMaterialButtonCircle4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonCircle4ActionPerformed(evt);
            }
        });
        jPanel3.add(rSMaterialButtonCircle4, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 590, 300, 70));

        rSMaterialButtonCircle5.setBackground(new java.awt.Color(102, 0, 255));
        rSMaterialButtonCircle5.setText("RETUEN BOOK");
        rSMaterialButtonCircle5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rSMaterialButtonCircle5.setFont(new java.awt.Font("Yu Gothic UI", 1, 30)); // NOI18N
        rSMaterialButtonCircle5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonCircle5ActionPerformed(evt);
            }
        });
        jPanel3.add(rSMaterialButtonCircle5, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 590, 300, 70));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 0, 850, 720));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txt_studentIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_studentIdFocusLost

    }//GEN-LAST:event_txt_studentIdFocusLost

    private void txt_studentIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_studentIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_studentIdActionPerformed

    private void txt_studentNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_studentNameFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_studentNameFocusLost

    private void txt_studentNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_studentNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_studentNameActionPerformed

    private void rSMaterialButtonCircle1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle1ActionPerformed
        if (deleteStudent() == true) {
            JOptionPane.showMessageDialog(this, "Student deleted Sucessfully");
            clearTable();
            setStudentDetailsToTable();
        } else {
            JOptionPane.showMessageDialog(this, "Student deletion Failed");
        }
    }//GEN-LAST:event_rSMaterialButtonCircle1ActionPerformed

    private void rSMaterialButtonCircle2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle2ActionPerformed
        if (addStudent() == true) {
            JOptionPane.showMessageDialog(this, "Student Added Sucessfully");
            clearTable();
            setStudentDetailsToTable();
        } else {
            JOptionPane.showMessageDialog(this, "Student Addition Failed");
        }
    }//GEN-LAST:event_rSMaterialButtonCircle2ActionPerformed

    private void rSMaterialButtonCircle3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle3ActionPerformed
        if (updateStudent() == true) {
            JOptionPane.showMessageDialog(this, "Student Updated Sucessfully");
            clearTable();
            setStudentDetailsToTable();
        } else {
            JOptionPane.showMessageDialog(this, "Student Updation Failed");
        }
    }//GEN-LAST:event_rSMaterialButtonCircle3ActionPerformed

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        HomePage home = new HomePage();
        home.setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jLabel3MouseClicked

    private void tbl_studentDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_studentDetailsMouseClicked
        int rowNo = tbl_studentDetails.getSelectedRow();
        TableModel model = tbl_studentDetails.getModel();

        txt_studentId.setText(model.getValueAt(rowNo, 0).toString());  //getValueAt takes two parameters, 1st is from which row and 2nd is column index
        txt_studentName.setText(model.getValueAt(rowNo, 1).toString());
        combo_CourseName.setSelectedItem(model.getValueAt(rowNo, 2).toString());
        combo_branch.setSelectedItem(model.getValueAt(rowNo, 3).toString());


    }//GEN-LAST:event_tbl_studentDetailsMouseClicked

    private void rSMaterialButtonCircle4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle4ActionPerformed
        IssueBook st = new IssueBook();
        st.setVisible(true);
        dispose();
    }//GEN-LAST:event_rSMaterialButtonCircle4ActionPerformed

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        RequestedBooks rq = new RequestedBooks();
        rq.setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel5MouseClicked

    private void rSMaterialButtonCircle5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle5ActionPerformed
        ReturnBook bk = new ReturnBook();
        bk.setVisible(true);
        dispose();
    }//GEN-LAST:event_rSMaterialButtonCircle5ActionPerformed

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
            java.util.logging.Logger.getLogger(ManageStudents.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManageStudents.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManageStudents.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManageStudents.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManageStudents().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> combo_CourseName;
    private javax.swing.JComboBox<String> combo_branch;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane2;
    private necesario.RSMaterialButtonCircle rSMaterialButtonCircle1;
    private necesario.RSMaterialButtonCircle rSMaterialButtonCircle2;
    private necesario.RSMaterialButtonCircle rSMaterialButtonCircle3;
    private necesario.RSMaterialButtonCircle rSMaterialButtonCircle4;
    private necesario.RSMaterialButtonCircle rSMaterialButtonCircle5;
    private rojeru_san.complementos.RSTableMetro tbl_studentDetails;
    private app.bolivia.swing.JCTextField txt_studentId;
    private app.bolivia.swing.JCTextField txt_studentName;
    // End of variables declaration//GEN-END:variables
}
