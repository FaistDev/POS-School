/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.faistdev.onlineshop.data;

import java.sql.Connection;
import java.sql.DriverManager;
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
    
    public static Database getInstance() throws SQLException{
        if(instance==null){
            instance = new Database();
        }
        return instance;
    }
    
    private Database() throws SQLException{
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        connection = DriverManager.getConnection("jdbc:postgresql://localhost/"+dbName, dbUsername, dbPassword);
    }
}
