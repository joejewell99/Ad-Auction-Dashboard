package com.example;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import com.example.security.PasswordChecker;
import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;


public class LoginDatabase {
    // url connection
    private static final String DB_URL = "jdbc:sqlite:login.db";
    //create a db table
    private static final String CREATE_TABLE_SQL =
            "CREATE TABLE IF NOT EXISTS users ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "username TEXT UNIQUE, "
                    + "password TEXT, "
                    + "role TEXT DEFAULT 'user',"
                    + "mfa_secret TEXT"
                    + ")";


    public LoginDatabase() {
        createDatabase();
    }

    private void createDatabase() {
        try {
            // Explicitly load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            System.out.println("Database will be created at: " + new java.io.File("login.db").getAbsolutePath());

            if (conn != null) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(CREATE_TABLE_SQL);

                    String adminPassword = BCrypt.hashpw("adminPass", BCrypt.gensalt());
                    String insertAdmin = "INSERT OR IGNORE INTO users(username, password, role) "
                            + "VALUES('admin', '" + adminPassword + "', 'admin')";
                    stmt.executeUpdate(insertAdmin);

                    String userPassword = BCrypt.hashpw("pass", BCrypt.gensalt());
                    String insertUser = "INSERT OR IGNORE INTO users(username, password, role) "
                            + "VALUES('user', '" + userPassword + "', 'user')";
                    stmt.executeUpdate(insertUser);


                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // authenticate method
    public User authenticateUser(String username, String password) {
        String query = "SELECT password, role FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()){
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    String role = rs.getString("role");
                    if (BCrypt.checkpw(password, storedHash)) {
                        return new User(username, role);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean addUser(String username, String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String insertSQL = "INSERT INTO users (username, password) VALUES (?,?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            pstmt.executeUpdate();
            return true;
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean userExists(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)){
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if(rs.next()){
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean registerUser(String username, String password) {
        if(!PasswordChecker.validate(password)) {
            System.out.println("Weak password" + PasswordChecker.requirements());
            return false;
        }
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }
        if (userExists(username)) {
            System.out.println("User " + username + " already exists");
            return false;
        }
        boolean added = addUser(username, password);
        if (!added) {
            return false;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            TimeBasedOneTimePasswordGenerator totp = new TimeBasedOneTimePasswordGenerator();

            KeyGenerator keyGen = KeyGenerator.getInstance(totp.getAlgorithm());
            keyGen.init(128);
            SecretKey secretKey = keyGen.generateKey();

            String encodedSecret = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            String updateSecretSql =
                    "UPDATE users SET mfa_secret = ? WHERE username = ?";
            try(PreparedStatement ps = conn.prepareStatement(updateSecretSql)) {
                ps.setString(1, encodedSecret);
                ps.setString(2, username);
                ps.execute();
            }
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
    public boolean updateUserPassword(String username, String newPassword) {
        if (!PasswordChecker.validate(newPassword)) {
            System.out.println("Weak password: " + PasswordChecker.requirements());
            return false;
        }
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        String updateSQL = "UPDATE users SET password = ? WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, hashedPassword);
            pstmt.setString(2, username);
            int affected = pstmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(String username) {
        String deleteSQL = "DELETE FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setString(1, username);
            int affected = pstmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<String[]> listUsers() {
        ArrayList<String[]> users = new ArrayList<>();
        String query = "SELECT id, username FROM users";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String id = Integer.toString(rs.getInt("id"));
                String username = rs.getString("username");
                users.add(new String[]{id, username});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean verifyOtp(String username, String code) {
        String sql = "SELECT mfa_secret FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next() || rs.getString("mfa_secret") == null) {
                    return false;
                }

                // 1) Get the Base64-encoded secret
                String b64 = rs.getString("mfa_secret");

                // 2) Sanitize the user’s input: strip spaces, dashes, etc.
                String entered = code.trim().replaceAll("\\D+", "");
                if (entered.length() != 6) {
                    // bad format
                    return false;
                }

                // 3) Decode to raw key
                byte[] rawKey = Base64.getDecoder().decode(b64);
                SecretKeySpec key = new SecretKeySpec(
                        rawKey,
                        TimeBasedOneTimePasswordGenerator.TOTP_ALGORITHM_HMAC_SHA1
                );

                // 4) TOTP generator & time‐step settings
                TimeBasedOneTimePasswordGenerator totp =
                        new TimeBasedOneTimePasswordGenerator();
                long stepSeconds = totp.getTimeStep().getSeconds();
                long now = Instant.now().getEpochSecond();

                // 5) Check previous, current, and next window
                for (int i = -1; i <= 1; i++) {
                    Instant instant = Instant.ofEpochSecond(now + (i * stepSeconds));
                    int expected = totp.generateOneTimePassword(key, instant);
                    String expectedStr = String.format("%06d", expected);
                    if (expectedStr.equals(entered)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public String getMfaSecretForUser(String username){
        String sql = "SELECT mfa_secret FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("mfa_secret");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}




