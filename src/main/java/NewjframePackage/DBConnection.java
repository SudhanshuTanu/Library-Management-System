package NewjframePackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection con;

    public static Connection getConnection() {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "SUD@2001");
            System.out.println("connected");

        } catch (ClassNotFoundException e) {
            System.out.println("driver error" + e.getMessage());
        } catch (SQLException e) {
            System.out.println(" error" + e.getMessage());
        }

        return con;
    }
}
