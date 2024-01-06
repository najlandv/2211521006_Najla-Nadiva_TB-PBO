import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

// Kelas VirtualDemo sebagai program utama dengan percabangan, perulangan, dan pemrosesan input.
public class VirtualDemo {
    // Scanner digunakan untuk menerima input dari pengguna
    private static final Scanner scanner = new Scanner(System.in);
    private static boolean continueProgram = true; // Variabel untuk menentukan apakah program harus terus berjalan

    public static void main(String[] args) {
        while (continueProgram) {
            try {
                // Tampilan awal program, termasuk informasi tanggal dan waktu
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println("                Data            ");
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println("Tanggal: " + getCurrentDate());
                System.out.println("Waktu  : " + getCurrentTime());
                System.out.println("-------------------------------------");
                System.out.println("   SILAHKAN LOG IN TERLEBIH DAHULU");
                System.out.println("-------------------------------------");
                System.out.print("Username: ");
                String username = scanner.nextLine();
                System.out.print("Password: ");
                String password = scanner.nextLine();
                String captcha = generateCaptcha();
                System.out.println(".....................................");
                System.out.println("Captcha: " + captcha);
                System.out.println("-------------------------------------");
                System.out.print("Masukkan Captcha (case insensitive): ");
                String userCaptcha = scanner.nextLine();

                // Validasi login dan captcha
                if (isValidLogin(username, password) && isValidCaptcha(captcha, userCaptcha)) {
                    System.out.println("Log in berhasil!");

                    int menuChoice;
                    do {
                        // Tampilan menu utama
                        System.out.println("\n");
                        System.out.println("***************************************");
                        System.out.println("    SELAMAT DATANG DI SI CAKE BAKERY");
                        System.out.println("        TEMPAT PESAN KUE SPESIAL");                    
                        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"); 
                        System.out.println("               Menu Utama             ");
                        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                        System.out.println("1. Lihat Semua Kue");
                        System.out.println("2. Tambah Data Kue");
                        System.out.println("3. Hapus Data Kue");
                        System.out.println("4. Edit Data Kue");
                        System.out.println("5. Input Data Pembelian");
                        System.out.println("6. Keluar");
                        System.out.println("***************************************");
                        System.out.println("PESANAN AKAN SIAP DI HARI BERIKUTNYA (H-1)");
                        System.out.println("Pesan sekarang untuk menikmati kelezatan!");
                        System.out.println("\n");
                        System.out.print("Pilih menu (1-6): ");
                        
                        menuChoice = scanner.nextInt();
                        scanner.nextLine(); // Consumes the newline character
                        // Switch case untuk memilih operasi menu
                        switch (menuChoice) {
                            case 1:
                                lihatSemuaKue();
                                break;
                            case 2:
                                tambahKue();
                                break;
                            case 3:
                                hapusKue();
                                break;
                            case 4:
                                updateKue();
                                break;
                            case 5:
                                Pelanggan pelanggan = inputPelanggan();
                                inputPembelian(pelanggan);
                                break;
                            case 6:
                                System.out.println("Keluar dari Menu Utama.");
                                break;
                            default:
                                System.out.println("Pilihan tidak valid. Silakan pilih lagi.");
                        }
                    } while (menuChoice != 6);

                    // Setelah keluar dari loop menu, tanyakan apakah ingin melanjutkan
                    System.out.print("Apakah Anda ingin melanjutkan? (yes/no): ");
                    String continueChoice = scanner.nextLine();

                     // Validasi pilihan melanjutkan atau tidak
                    while (!continueChoice.equalsIgnoreCase("yes") && !continueChoice.equalsIgnoreCase("no")) {
                        System.out.println("Pilihan tidak valid. Harap masukkan 'yes' atau 'no'.");
                        System.out.print("Apakah Anda ingin melanjutkan? (yes/no): ");
                        continueChoice = scanner.nextLine();
                    }
                    // Jika pengguna tidak ingin melanjutkan, keluar dari program
                    if (continueChoice.equalsIgnoreCase("no")) {
                        System.out.println("Program selesai. Terima kasih!");
                        System.exit(0);
                    }
        

                } else {
                    System.out.println("Log in gagal. Pastikan username, password, dan captcha benar.");
                }

            } catch (InputMismatchException e) {
                // Tangani exception jika terjadi kesalahan input
                System.out.println("Error: Pastikan input sesuai dengan format yang benar.");
                System.out.println("Silahkan ulangi pengisian sesuai dengan ketentuan.");
                scanner.nextLine();
            } catch (NumberFormatException e) {
                // Tangani exception jika terjadi kesalahan konversi angka
                System.out.println("Error: Harga Kue dan Jumlah Beli harus berupa angka.");
                System.out.println("Silahkan ulangi pengisian sesuai dengan ketentuan.");
                scanner.nextLine();
            } catch (JumlahBeliException e) {
                // Tangani exception khusus jumlah beli
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine();
            } catch (SQLException e) {
                // Tangani exception SQL
                System.out.println("Error: Terjadi kesalahan SQL - " + e.getMessage());
                scanner.nextLine();
            } catch (Exception e) {
                // Tangani exception umum
                System.out.println("Terjadi kesalahan: " + e.getMessage());
                scanner.nextLine();
            }
        }

        scanner.close(); // Tutup Scanner setelah selesai menggunakan
    }

    private static void hapusKue() {
        System.out.println("\n");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");        
        System.out.println("           HAPUS DATA KUE");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        System.out.print("Input Kode Kue yang akan dihapus: ");
        String kodeKue = scanner.nextLine();

        // Panggil metode untuk menghapus kue
        hapusKueDariDatabase(kodeKue);

        System.out.println("Data kue berhasil dihapus.");
        System.out.println("=====================================");

        // Setelah keluar dari hapusKue, tanyakan apakah ingin melanjutkan
        System.out.print("Apakah Anda ingin melanjutkan? (yes/no): ");
        String continueChoice = scanner.nextLine();

        while (!continueChoice.equalsIgnoreCase("yes") && !continueChoice.equalsIgnoreCase("no")) {
            System.out.println("Pilihan tidak valid. Harap masukkan 'yes' atau 'no'.");
            System.out.print("Apakah Anda ingin melanjutkan? (yes/no): ");
            continueChoice = scanner.nextLine();
        }

        if (continueChoice.equalsIgnoreCase("no")) {
            System.out.println("Program selesai. Terima kasih!");
            System.exit(0);
        }
    }

    private static void updateKue() {
        System.out.println("\n");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");       
        System.out.println("           EDIT DATA KUE");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        System.out.print("Input Kode Kue yang akan diupdate: ");
        String kodeKue = scanner.nextLine();

        // Retrieve existing kue data
        Kue existingKue = Kue.getKueByKode(kodeKue);

        if (existingKue != null) {
            System.out.println("Data Kue Saat Ini:");
            System.out.println(existingKue);

            System.out.print("Nama Kue         : ");
            String namaKue = scanner.nextLine();

            double hargaKue;
            try {
                System.out.print("Ubah Harga Kue   : ");
                hargaKue = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Harga Kue harus berupa angka.");
                return;
            }

            // Create a new Kue object with updated data
            Kue updatedKue = new Kue(kodeKue, namaKue, hargaKue);

            // Update kue in the database
            Kue.updateKue(updatedKue);

            System.out.println("Data kue berhasil diupdate.");
        } else {
            System.out.println("Error: Kue dengan kode " + kodeKue + " tidak ditemukan.");
        }

        // Setelah keluar dari updateKue, tanyakan apakah ingin melanjutkan
        System.out.print("Apakah Anda ingin melanjutkan? (yes/no): ");
        String continueChoice = scanner.nextLine();

        while (!continueChoice.equalsIgnoreCase("yes") && !continueChoice.equalsIgnoreCase("no")) {
            System.out.println("Pilihan tidak valid. Harap masukkan 'yes' atau 'no'.");
            System.out.print("Apakah Anda ingin melanjutkan? (yes/no): ");
            continueChoice = scanner.nextLine();
        }

        if (continueChoice.equalsIgnoreCase("no")) {
            System.out.println("Program selesai. Terima kasih!");
            System.exit(0);
        }
 
        
    }
        private static Pelanggan inputPelanggan() {
        System.out.println("\n");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");  
        System.out.println("         INPUT DATA PELANGGAN");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");  
        System.out.print("Input Nama Pelanggan   : ");
        String namaPelanggan = scanner.nextLine();
    
        System.out.print("Input No. HP Pelanggan : ");
        String noHpPelanggan = scanner.nextLine();
    
        System.out.print("Input Alamat Pelanggan : ");
        String alamatPelanggan = scanner.nextLine();
    
        Pelanggan pelanggan = new Pelanggan(namaPelanggan, noHpPelanggan, alamatPelanggan);
    
        try {
            pelanggan.simpanKeDatabase();
            System.out.println("\n");
            System.out.println("Data pelanggan berhasil disimpan.");
        } catch (SQLException e) {
            System.out.println("Gagal menyimpan data pelanggan: " + e.getMessage());
        }
        return pelanggan;
    }
    
    private static void inputPembelian(Pelanggan pelanggan) throws SQLException, JumlahBeliException {
    System.out.println("\n");
    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    System.out.println("       INPUT DATA PEMBELIAN KUE");
    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

    System.out.print("No. Faktur              : ");
    String noFaktur = scanner.nextLine();

    System.out.print("Input Kode Kue          : ");
    String kodeKue = scanner.nextLine();

    // Isi informasi kue dari database
    String query = "SELECT nama_kue, harga_kue FROM kue WHERE kode_kue = ?";
    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sicake_bakery", "root", "");
         PreparedStatement statement = connection.prepareStatement(query)) {

        statement.setString(1, kodeKue);

        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                String namaKue = resultSet.getString("nama_kue");
                double hargaKue = resultSet.getDouble("harga_kue");

                double jumlahBeli;
                try {
                    System.out.print("Input Jumlah Beli       : ");
                    jumlahBeli = Double.parseDouble(scanner.nextLine());
                    if (jumlahBeli < 0) {
                        throw new JumlahBeliException("Jumlah Beli harus lebih besar dari 0.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error: Jumlah Beli harus berupa angka.");
                    return;
                } catch (JumlahBeliException e) {
                    System.out.println("Error: " + e.getMessage());
                    return;
                }

                System.out.print("Input Nama Kasir        : ");
                String kasir = scanner.nextLine();
                System.out.println("\n");

                // Membuat objek Pemesanan dengan data yang telah diinput
                Pemesanan pesanan = new Pemesanan(noFaktur, pelanggan, new Kue(kodeKue, namaKue, hargaKue), (int)jumlahBeli, kasir);

                // Menampilkan informasi pemesanan
                pesanan.tampilkanInformasi();

                // Save Order
                pesanan.simpanPemesanan();

                System.out.println("==============================================");
            } else {
                System.out.println("Error: Kode Kue tidak valid.");
            }
        }
    }
}
    // Metode untuk mendapatkan tanggal dengan format tertentu
    private static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd/MM/yyyy");
        return dateFormat.format(Calendar.getInstance().getTime());
    }
    // Metode untuk mendapatkan waktu dengan format tertentu
    private static String getCurrentTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a zzz");
        return timeFormat.format(Calendar.getInstance().getTime());
    }
    // Metode untuk menghasilkan captcha acak
    private static String generateCaptcha() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int length = 6;
        StringBuilder captcha = new StringBuilder();
        // Loop untuk membuat captcha acak
        for (int i = 0; i < length; i++) {
            captcha.append(characters.charAt((int) (Math.random() * characters.length())));
        }

        return captcha.toString();
    }
    // Metode untuk memeriksa kevalidan login
    private static boolean isValidLogin(String username, String password) {
        User user = User.getUserByUsername(username);
        return user != null && user.isValidLogin(password);
    }
    
    // Metode untuk memeriksa kevalidan captcha
    private static boolean isValidCaptcha(String generatedCaptcha, String userCaptcha) {
        return generatedCaptcha.equalsIgnoreCase(userCaptcha);
    }
    // Metode untuk menampilkan semua kue dari database
    private static void lihatSemuaKue() {
        List<Kue> daftarKue = Kue.bacaSemuaKue();
        System.out.println("\n");
        System.out.println("------------------------------------------");
        System.out.println("            DAFTAR SEMUA KUE    ");
        System.out.println("------------------------------------------");
        System.out.println(" Kode   -   Nama Kue           -   Harga   ");
        System.out.println("------------------------------------------");

        // Loop untuk menampilkan informasi setiap kue
        for (Kue kue : daftarKue) {
            System.out.printf(" %-6s - %-20s - %-10.2f \n", kue.getKodeKue(), kue.getNamaKue(), kue.getHargaKue());
        }
        
        System.out.println("------------------------------------------");
        
        
        
        // Setelah keluar dari Lihat Semua Daftar Kue, tanyakan apakah ingin melanjutkan
        System.out.print("Apakah Anda ingin melanjutkan? (yes/no): ");
        String continueChoice = scanner.nextLine();

        // Validasi pilihan melanjutkan atau tidak
        while (!continueChoice.equalsIgnoreCase("yes") && !continueChoice.equalsIgnoreCase("no")) {
            System.out.println("Pilihan tidak valid. Harap masukkan 'yes' atau 'no'.");
            System.out.print("Apakah Anda ingin melanjutkan? (yes/no): ");
            continueChoice = scanner.nextLine();
        }
            // Jika pengguna tidak ingin melanjutkan, keluar dari program
            if (continueChoice.equalsIgnoreCase("no")) {
                System.out.println("Program selesai. Terima kasih!");
                System.exit(0);
            }
    }
    // Metode untuk menambahkan kue ke database.
    public static void tambahKue() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sicake_bakery", "root", "")) {
            String query = "INSERT INTO kue (kode_kue, nama_kue, harga_kue) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                System.out.print("Input Kode Kue   : ");
                String kodeKue = scanner.nextLine();
    
                System.out.print("Input Nama Kue   : ");
                String namaKue = scanner.nextLine();
    
                double hargaKue;
                try {
                    System.out.print("Input Harga Kue  : ");
                    hargaKue = Double.parseDouble(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Error: Harga Kue harus berupa angka.");
                    return;
                }
    
                statement.setString(1, kodeKue);
                statement.setString(2, namaKue);
                statement.setDouble(3, hargaKue);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Setelah keluar dari tambahKue, tanyakan apakah ingin melanjutkan
        System.out.print("Apakah Anda ingin melanjutkan? (yes/no): ");
        String continueChoice = scanner.nextLine();

        while (!continueChoice.equalsIgnoreCase("yes") && !continueChoice.equalsIgnoreCase("no")) {
            System.out.println("Pilihan tidak valid. Harap masukkan 'yes' atau 'no'.");
            System.out.print("Apakah Anda ingin melanjutkan? (yes/no): ");
            continueChoice = scanner.nextLine();
        }

        if (continueChoice.equalsIgnoreCase("no")) {
            System.out.println("Program selesai. Terima kasih!");
            System.exit(0);
        }   
    }
        private static void hapusKueDariDatabase(String kodeKue) {
        // Panggil metode dari kelas Kue untuk menghapus kue dari database
        Kue.hapusKue(kodeKue);
}  
// Metode untuk menghapus kue dari database dengan implementasi JDBC dan CRUD.
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

    // Check if the user wants to continue
    System.out.print("Apakah Anda ingin melanjutkan? (yes/no): ");
    String continueChoice = scanner.nextLine();

    while (!continueChoice.equalsIgnoreCase("yes") && !continueChoice.equalsIgnoreCase("no")) {
        System.out.println("Pilihan tidak valid. Harap masukkan 'yes' atau 'no'.");
        System.out.print("Apakah Anda ingin melanjutkan? (yes/no): ");
        continueChoice = scanner.nextLine();
    }

        if (continueChoice.equalsIgnoreCase("no")) {
            System.out.println("Program selesai. Terima kasih!");
            System.exit(0);
        }

    }

}