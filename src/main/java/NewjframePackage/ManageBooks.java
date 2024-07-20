package NewjframePackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class ManageBooks extends javax.swing.JFrame {

    //Attributes 
    String bookName, author;
    int bookId, quantity;
    DefaultTableModel model; //using this we will set the values in the book table after fetching actual value form database

    public ManageBooks() {
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

    // To check user trying to add book already exits
    public boolean checkBookExists() {
        int id = Integer.parseInt(txt_bookId.getText()); //get id entered by user 
        try {
            Connection con = NewjframePackage.DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select book_id from book_details");  //executeQuery method returns collection of rows hence stored in result set
            while (rs.next()) {
                int bookIdFromDB = rs.getInt("book_id"); //get book id at current row 
                if (id == bookIdFromDB) {
                    JOptionPane.showMessageDialog(this, "Book Already exists with this id!");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return true;
    }

    //to add book to book_details table
    public boolean addBook() {
        boolean isAdded = false;
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
        if (txt_quantity.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter quantity");
            return false;
        }
        if (!txt_quantity.getText().matches("\\d+") || !(Integer.parseInt(txt_quantity.getText()) > 0)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid integer quantity");
            return false;
        }
        if (checkBookExists()) {
            bookId = Integer.parseInt(txt_bookId.getText()); //returns a string so convert it to int as book_id is of type int
            bookName = txt_bookName.getText();
            author = txt_authorName.getText();
            quantity = Integer.parseInt(txt_quantity.getText());
            try {
                Connection con = NewjframePackage.DBConnection.getConnection();
                PreparedStatement pst = con.prepareStatement("insert into book_details values (?,?,?,?);");
                pst.setInt(1, bookId);
                pst.setString(2, bookName);
                pst.setString(3, author);
                pst.setInt(4, quantity);

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
    public boolean checkBookId() {
        int id = Integer.parseInt(txt_bookId.getText()); //get id entered by user 
        try {
            Connection con = NewjframePackage.DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select book_id from book_details");  //executeQuery method returns collection of rows hence stored in result set
            while (rs.next()) {
                int studentIdFromDB = rs.getInt("book_id"); //get student id at current row 
                if (id == studentIdFromDB) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        JOptionPane.showMessageDialog(this, "Book id does not exists!");
        return false;
    }

    //to update book details
    public boolean updateBook() {
        boolean isUpdated = false;
        if (txt_bookId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter Book id");
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
        if (txt_quantity.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter quantity");
            return false;
        }
        if (checkBookId()) {
            bookId = Integer.parseInt(txt_bookId.getText()); //returns a string so convert it to int as book_id is of type int
            bookName = txt_bookName.getText();
            author = txt_authorName.getText();
            quantity = Integer.parseInt(txt_quantity.getText());
            try {
                Connection con = NewjframePackage.DBConnection.getConnection();
                String sql = "update book_details set book_name = ? , author = ? , quantity = ? where book_id = ?";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, bookName);
                pst.setString(2, author);
                pst.setInt(3, quantity);
                pst.setInt(4, bookId);
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
    public boolean deleteBook() {
        boolean isDeleted = false;
        if (txt_bookId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter Book id");
            return false;
        }
        if (checkBookId()) {
            bookId = Integer.parseInt(txt_bookId.getText());
            try {
                Connection con = NewjframePackage.DBConnection.getConnection();
                String sql = "delete from book_details where book_id = ?";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setInt(1, bookId);
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

        jPanel1 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        txt_bookId = new app.bolivia.swing.JCTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        txt_bookName = new app.bolivia.swing.JCTextField();
        jLabel35 = new javax.swing.JLabel();
        txt_authorName = new app.bolivia.swing.JCTextField();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        txt_quantity = new app.bolivia.swing.JCTextField();
        rSMaterialButtonCircle1 = new necesario.RSMaterialButtonCircle();
        rSMaterialButtonCircle2 = new necesario.RSMaterialButtonCircle();
        rSMaterialButtonCircle3 = new necesario.RSMaterialButtonCircle();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_bookDetails = new rojeru_san.complementos.RSTableMetro();
        jLabel4 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 204));
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(102, 51, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel33.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("Book id");
        jPanel1.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 120, 60, 20));

        txt_bookId.setBackground(new java.awt.Color(102, 51, 255));
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
        jPanel1.add(txt_bookId, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 148, 290, -1));

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

        txt_bookName.setBackground(new java.awt.Color(102, 51, 255));
        txt_bookName.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_bookName.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        txt_bookName.setFont(new java.awt.Font("Yu Gothic Medium", 0, 15)); // NOI18N
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
        jPanel1.add(txt_bookName, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 260, 290, 30));

        jLabel35.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("Book Name");
        jPanel1.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 230, -1, 20));

        txt_authorName.setBackground(new java.awt.Color(102, 51, 255));
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
        jPanel1.add(txt_authorName, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 370, 290, 30));

        jLabel36.setFont(new java.awt.Font("Copperplate Gothic Bold", 0, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Collaborator_Male_26px.png"))); // NOI18N
        jLabel36.setMaximumSize(new java.awt.Dimension(30, 30));
        jPanel1.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 360, 40, 50));

        jLabel37.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("Author Name");
        jPanel1.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 340, 100, 20));

        jLabel38.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText("Quantity");
        jPanel1.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 450, 70, 20));

        jLabel39.setFont(new java.awt.Font("Copperplate Gothic Bold", 0, 14)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Unit_26px.png"))); // NOI18N
        jLabel39.setMaximumSize(new java.awt.Dimension(30, 30));
        jPanel1.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 460, 30, 60));

        txt_quantity.setBackground(new java.awt.Color(102, 51, 255));
        txt_quantity.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_quantity.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        txt_quantity.setFont(new java.awt.Font("Yu Gothic Medium", 0, 15)); // NOI18N
        txt_quantity.setPlaceholder("Enter Quantity...");
        txt_quantity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_quantityFocusLost(evt);
            }
        });
        txt_quantity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_quantityActionPerformed(evt);
            }
        });
        jPanel1.add(txt_quantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 480, 290, 30));

        rSMaterialButtonCircle1.setBackground(new java.awt.Color(255, 51, 51));
        rSMaterialButtonCircle1.setText("Delete");
        rSMaterialButtonCircle1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rSMaterialButtonCircle1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonCircle1ActionPerformed(evt);
            }
        });
        jPanel1.add(rSMaterialButtonCircle1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 580, 130, 70));

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
        jPanel1.add(rSMaterialButtonCircle3, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 580, 130, 70));

        jPanel2.setBackground(new java.awt.Color(255, 51, 51));

        jLabel2.setFont(new java.awt.Font("Verdana", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Rewind_48px.png"))); // NOI18N
        jLabel2.setText("Home");
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
                .addComponent(jLabel2)
                .addContainerGap(8, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 4, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 40));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 440, 720));

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

        jPanel3.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(807, 0, 44, 40));

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

        jPanel3.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 820, 230));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 30)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 51, 51));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/NewjframePackage/icons8_Books_52px_1.png"))); // NOI18N
        jLabel4.setText(" Manage Books ");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 40, 270, -1));

        jPanel6.setBackground(new java.awt.Color(255, 51, 51));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 850, 8));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 0, 860, 720));

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

    private void txt_quantityFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_quantityFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_quantityFocusLost

    private void txt_quantityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_quantityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_quantityActionPerformed

    private void rSMaterialButtonCircle1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle1ActionPerformed
        if (deleteBook() == true) {
            JOptionPane.showMessageDialog(this, "Book Deleted Sucessfully");
            clearTable();
            setBookDetailsToTable();
        } else {
            JOptionPane.showMessageDialog(this, "Book Deletion Failed");
        }
    }//GEN-LAST:event_rSMaterialButtonCircle1ActionPerformed

    private void rSMaterialButtonCircle2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle2ActionPerformed
        if (addBook() == true) {
            JOptionPane.showMessageDialog(this, "Book Added Sucessfully");
            clearTable();
            setBookDetailsToTable();
        } else {
            JOptionPane.showMessageDialog(this, "Book Addition Failed");
        }
    }//GEN-LAST:event_rSMaterialButtonCircle2ActionPerformed

    private void rSMaterialButtonCircle3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle3ActionPerformed
        if (updateBook() == true) {
            JOptionPane.showMessageDialog(this, "Book Updated Sucessfully");
            clearTable();
            setBookDetailsToTable();
        } else {
            JOptionPane.showMessageDialog(this, "Book Updation Failed");
        }
    }//GEN-LAST:event_rSMaterialButtonCircle3ActionPerformed

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
          System.exit(0); 
    }//GEN-LAST:event_jLabel3MouseClicked

    private void tbl_bookDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_bookDetailsMouseClicked
        int rowNo = tbl_bookDetails.getSelectedRow();
        TableModel model = tbl_bookDetails.getModel();

        txt_bookId.setText(model.getValueAt(rowNo, 0).toString());  //getValueAt takes two parameters, 1st is from which row and 2nd is column index
        txt_bookName.setText(model.getValueAt(rowNo, 1).toString());
        txt_authorName.setText(model.getValueAt(rowNo, 2).toString());
        txt_quantity.setText(model.getValueAt(rowNo, 3).toString());
    }//GEN-LAST:event_tbl_bookDetailsMouseClicked

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        HomePage home = new HomePage();
        home.setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel2MouseClicked

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
            java.util.logging.Logger.getLogger(ManageBooks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManageBooks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManageBooks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManageBooks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManageBooks().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private necesario.RSMaterialButtonCircle rSMaterialButtonCircle1;
    private necesario.RSMaterialButtonCircle rSMaterialButtonCircle2;
    private necesario.RSMaterialButtonCircle rSMaterialButtonCircle3;
    private rojeru_san.complementos.RSTableMetro tbl_bookDetails;
    private app.bolivia.swing.JCTextField txt_authorName;
    private app.bolivia.swing.JCTextField txt_bookId;
    private app.bolivia.swing.JCTextField txt_bookName;
    private app.bolivia.swing.JCTextField txt_quantity;
    // End of variables declaration//GEN-END:variables
}
