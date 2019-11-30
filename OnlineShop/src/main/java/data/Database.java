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
import java.time.LocalDate;
import java.util.ArrayList;

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

    public static synchronized Database getInstance() throws SQLException {
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

    public int checkLoginCredentials(String username, String password) throws SQLException, UnsupportedEncodingException, NoSuchAlgorithmException {
        PreparedStatement ps = connection.prepareStatement("SELECT ID FROM customer WHERE username=? AND password=?");

        String passwordHash = getMD5hash(password);

        ps.setString(1, username);
        ps.setString(2, passwordHash);

        ResultSet rs = ps.executeQuery();
        
        while(rs.next()){
            return rs.getInt("id");
        }

        return 0;
    }

    /*private int getResultSetSize(ResultSet rs) throws SQLException {
        int size = 0;
        if (rs != null) {
            rs.last();
            size = rs.getRow();
        }
        return size;
    }*/

    private String getMD5hash(String value) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        StringBuilder builder = new StringBuilder();
        
        byte[] bytesOfMessage = value.getBytes("UTF-8");
        
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(bytesOfMessage);
        
        for(byte b: thedigest) {
            builder.append(Integer.toString( ( b & 0xff ) + 0x100, 16).substring( 1 ));
        }
        
        return builder.toString();
    }
    
    public ArrayList<Order> getOrders(int customerID) throws SQLException{
        ArrayList<Order> orders = new ArrayList<>();
        
        PreparedStatement ps = connection.prepareStatement("SELECT id,orderdate,(SELECT SUM(article.price * position.amount) FROM position JOIN article ON article.ID=position.articleid WHERE position.orderid=ordering.id) total FROM ordering WHERE customerid=?");
        
        ps.setInt(1, customerID);

        ResultSet rs = ps.executeQuery();
        
        while(rs.next()){
            orders.add(new Order(rs.getInt("id"), customerID, rs.getDate("orderdate").toLocalDate(),rs.getDouble("total")));
        }
        
        return orders;
    }
    
    public ArrayList<Article> getArticles() throws SQLException{
        ArrayList<Article> articles = new ArrayList<>();
        
        PreparedStatement ps = connection.prepareStatement("SELECT id, name, price FROM article");
        
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()){
            articles.add(new Article(rs.getInt("id"), rs.getString("name"), rs.getDouble("price")));
        }
        
        return articles;
    }
    
    public ArrayList<Card> getCard(int customerid) throws SQLException{
        ArrayList<Card> cards = new ArrayList<>();
        
        PreparedStatement ps = connection.prepareStatement("SELECT ct.id cid, ct.firstname,ct.lastname,ct.password,ct.username, a.id aid, a.name, a.price, c.amount FROM card c JOIN customer ct ON c.customerid = ct.id JOIN article a ON c.articleid = a.id WHERE c.customerid=?");
        
        ps.setInt(1, customerid);
        
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()){
            cards.add(new Card(new Customer(rs.getInt("cid"), rs.getString("firstname"), rs.getString("lastname"), rs.getString("password"), rs.getString("username")), new Article(rs.getInt("aid"), rs.getString("name"), rs.getDouble("price")),rs.getInt("amount")));
        }
        
        return cards;
    }
    
    public void addToCard(int customerid, int articleid, int amount) throws SQLException{
        PreparedStatement ps = connection.prepareStatement("INSERT INTO public.card(customerid, articleid, amount) VALUES(?, ?, ?)");
        
        ps.setInt(1, customerid);
        ps.setInt(2, articleid);
        ps.setInt(3, amount);
        
        ps.executeUpdate();
    }
}
