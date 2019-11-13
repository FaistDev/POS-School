/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Ben
 */
public class Database {

    private static Database instance;
    private final Connection connection;

    private final String dbName = "OnlineShop";
    private final String dbUsername = "postgres";
    private final String dbPassword = "postgres";

    public static Database getInstance() throws SQLException {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    private Database() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + dbName, dbUsername, dbPassword);
    }

    public boolean checkLoginCredentials(String username, String password) throws SQLException, UnsupportedEncodingException, NoSuchAlgorithmException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM cusomer WHERE username=? AND password=?");

        byte[] passwordHash = getMD5hash(password);

        ps.setString(0, username);
        ps.setBytes(1, passwordHash);

        ResultSet rs = ps.executeQuery();

        return getResultSetSize(rs) > 0;
    }

    private int getResultSetSize(ResultSet rs) throws SQLException {
        int size = 0;
        if (rs != null) {
            rs.last();
            size = rs.getRow();
        }
        return size;
    }

    private byte[] getMD5hash(String value) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] bytesOfMessage = value.getBytes("UTF-8");

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(bytesOfMessage);
        return thedigest;
    }
}
