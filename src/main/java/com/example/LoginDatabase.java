package com.example;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import com.example.security.PasswordChecker;
import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;

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
        return addUser(username, password);
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


}


