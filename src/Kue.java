import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Representasi objek Kue dengan atribut kodeKue, namaKue, dan hargaKue.
// Menyediakan metode untuk mengakses informasi dan representasi string.
public class Kue {
    protected String kodeKue;
    protected String namaKue;
    protected double hargaKue;

    // Konstruktor untuk inisialisasi objek Kue.
    public Kue(String kodeKue, String namaKue, double hargaKue) {
        this.kodeKue = kodeKue;
        this.namaKue = namaKue;
        this.hargaKue = hargaKue;
    }

    // Metode getter untuk mendapatkan kodeKue.
    public String getKodeKue() {
        return kodeKue;
    }

    // Metode getter untuk mendapatkan namaKue.
    public String getNamaKue() {
        return namaKue;
    }

    // Metode getter untuk mendapatkan hargaKue.
    public double getHargaKue() {
        return hargaKue;
    }
    // Metode untuk mendapatkan objek Kue berdasarkan kodeKue dari database.
    public static Kue getKueByKode(String kodeKue) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sicake_bakery", "root", "")) {
            String query = "SELECT * FROM kue WHERE kode_kue = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, kodeKue);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String namaKue = resultSet.getString("nama_kue");
                        double hargaKue = resultSet.getDouble("harga_kue");

                        return new Kue(kodeKue, namaKue, hargaKue);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Return null if kue not found
    }
    // Metode untuk membaca semua data kue dari database dan menyimpannya dalam List.
    public static List<Kue> bacaSemuaKue() {
        List<Kue> daftarKue = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sicake_bakery", "root", "")) {
            String query = "SELECT * FROM kue";
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery(query)) {
                    while (resultSet.next()) {
                        Kue kue = new Kue(
                                resultSet.getString("kode_kue"),
                                resultSet.getString("nama_kue"),
                                resultSet.getDouble("harga_kue")
                        );
                        daftarKue.add(kue);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return daftarKue;
    }
    // Metode untuk menambahkan data kue ke database.
    public static void tambahKue(Kue kue) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sicake_bakery", "root", "")) {
            String query = "INSERT INTO kue (kode_kue, nama_kue, harga_kue) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, kue.getKodeKue());
                statement.setString(2, kue.getNamaKue());
                statement.setDouble(3, kue.getHargaKue());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Metode untuk mengupdate data kue di database.
    public static void updateKue(Kue kue) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sicake_bakery", "root", "")) {
            String query = "UPDATE kue SET nama_kue = ?, harga_kue = ? WHERE kode_kue = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, kue.getNamaKue());
                statement.setDouble(2, kue.getHargaKue());
                statement.setString(3, kue.getKodeKue());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Metode untuk menghapus data kue dari database berdasarkan kodeKue.
    public static void hapusKue(String kodeKue) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sicake_bakery", "root", "")) {
            String query = "DELETE FROM kue WHERE kode_kue = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, kodeKue);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
