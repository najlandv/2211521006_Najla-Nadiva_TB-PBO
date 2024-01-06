import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Representasi objek User yang menyimpan password.
public class User {
    private String password;

    // Konstruktor untuk inisialisasi objek User dengan password.
    public User(String password) {
        this.password = password;
    }

    // Metode untuk mendapatkan objek User berdasarkan username dari database.
    public static User getUserByUsername(String username) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sicake_bakery", "root", "")) {
            String query = "SELECT * FROM akun WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String password = resultSet.getString("password");
                        return new User(password);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    // Metode untuk memvalidasi login dengan membandingkan password yang dimasukkan.
    public boolean isValidLogin(String enteredPassword) {
        return password.equals(enteredPassword);
    }
}
