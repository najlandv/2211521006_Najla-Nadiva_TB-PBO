import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Kelas DatabaseConnector untuk mengelola koneksi ke database menggunakan JDBC.
public class DatabaseConnector {
    // URL, USER, dan PASSWORD untuk koneksi ke database MySQL.
    private static final String URL = "jdbc:mysql://localhost:3306/sicake_bakery";
    private static final String USER = "root";
    private static final String PASSWORD = ""; 

    // Metode untuk mendapatkan koneksi ke database.
    public static Connection getConnection() throws SQLException {
        // Mendapatkan koneksi menggunakan DriverManager.getConnection.
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
