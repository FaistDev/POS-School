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
import java.sql.Statement;
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

    /**
     * Check if a user with a password exists, and return the userID; if no user exists, return 0
     * @param username username
     * @param password password
     * @return customerID customerID
     * @throws SQLException SQLException
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     */
    public int checkLoginCredentials(String username, String password) throws SQLException, UnsupportedEncodingException, NoSuchAlgorithmException {
        PreparedStatement ps = connection.prepareStatement("SELECT ID FROM customer WHERE username=? AND password=?");

        String passwordHash = getMD5hash(password);

        ps.setString(1, username);
        ps.setString(2, passwordHash);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
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
    
    /**
     * Convert a string to a MD5-Hash 
     * @param value value
     * @return MD5-Hash MD5-Hash
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     */
    private String getMD5hash(String value) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        StringBuilder builder = new StringBuilder();

        byte[] bytesOfMessage = value.getBytes("UTF-8");

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(bytesOfMessage);

        for (byte b : thedigest) {
            builder.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }

        return builder.toString();
    }

    /**
     * Returns an ArrayList with orders for a specifc customer 
     * @param customerID customerID
     * @return orders orders
     * @throws SQLException SQLException
     */
    public ArrayList<Order> getOrders(int customerID) throws SQLException {
        ArrayList<Order> orders = new ArrayList<>();

        PreparedStatement ps = connection.prepareStatement("SELECT id,orderdate,(SELECT SUM(article.price * position.amount) FROM position JOIN article ON article.ID=position.articleid WHERE position.orderid=ordering.id) total FROM ordering WHERE customerid=?");

        ps.setInt(1, customerID);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            orders.add(new Order(rs.getInt("id"), customerID, rs.getDate("orderdate").toLocalDate(), rs.getDouble("total")));
        }

        return orders;
    }

    /**
     * Returns all articles 
     * @return articles articles
     * @throws SQLException SQLException
     */
    public ArrayList<Article> getArticles() throws SQLException {
        ArrayList<Article> articles = new ArrayList<>();

        PreparedStatement ps = connection.prepareStatement("SELECT id, name, price FROM article");

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            articles.add(new Article(rs.getInt("id"), rs.getString("name"), rs.getDouble("price")));
        }

        return articles;
    }

    /**
     * Returns customers card 
     * @param customerid customerid
     * @return cardpositions cardpositions
     * @throws SQLException SQLException
     */
    public ArrayList<Card> getCard(int customerid) throws SQLException {
        ArrayList<Card> cards = new ArrayList<>();

        PreparedStatement ps = connection.prepareStatement("SELECT ct.id cid, ct.firstname,ct.lastname,ct.password,ct.username, a.id aid, a.name, a.price, c.amount FROM card c JOIN customer ct ON c.customerid = ct.id JOIN article a ON c.articleid = a.id WHERE c.customerid=?");

        ps.setInt(1, customerid);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            cards.add(new Card(new Customer(rs.getInt("cid"), rs.getString("firstname"), rs.getString("lastname"), rs.getString("password"), rs.getString("username")), new Article(rs.getInt("aid"), rs.getString("name"), rs.getDouble("price")), rs.getInt("amount")));
        }

        return cards;
    }

    /**
     * Adds something to customers card, if it already is in card, the amount will be increased
     * @param customerid customerid
     * @param articleid articleid
     * @param amount amount
     * @throws SQLException SQLException 
     */
    public void addToCard(int customerid, int articleid, int amount) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO public.card(customerid, articleid, amount) VALUES(?, ?, ?) ON CONFLICT (customerid,articleid) DO UPDATE SET amount=card.amount+1");

        ps.setInt(1, customerid);
        ps.setInt(2, articleid);
        ps.setInt(3, amount);

        ps.executeUpdate();
    }

    /**
     * Deletes something from card, if the amount in card is smaller than the amount that will be deleted, the cardposition will be deleted
     * @param customerid customerid
     * @param articleid articleid
     * @param amount amount
     * @throws SQLException SQLException
     * @throws Exception Exception
     */
    public void deleteFromCard(int customerid, int articleid, int amount) throws SQLException, Exception {
        PreparedStatement ps = connection.prepareStatement("SELECT amount FROM public.card WHERE customerid=? AND articleid=?;");

        ps.setInt(1, customerid);
        ps.setInt(2, articleid);

        ResultSet rs = ps.executeQuery();
        PreparedStatement updateOrDelete = null;
        while (rs.next()) {
            if (rs.getInt("amount") > amount) {
                updateOrDelete = connection.prepareStatement("UPDATE public.card SET amount=card.amount-? WHERE customerid=? AND articleid=?;");
                updateOrDelete.setInt(1, amount);
                updateOrDelete.setInt(2, customerid);
                updateOrDelete.setInt(3, articleid);
            } else {
                updateOrDelete = connection.prepareStatement("DELETE FROM public.card WHERE customerid=? AND articleid=?;");

                updateOrDelete.setInt(1, customerid);
                updateOrDelete.setInt(2, articleid);
            }
        }

        if (updateOrDelete == null) {
            throw new Exception("No card entry found");
        }

        updateOrDelete.executeUpdate();

    }

    /**
     * Insert a new order for a specifc customer and clear customers card 
     * @param customerid customerid
     * @throws SQLException SQLException
     * @throws Exception Exception
     */
    public void createNewOrder(int customerid) throws SQLException, Exception {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO public.ordering(customerid, orderdate) VALUES (?,CURRENT_DATE);", Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, customerid);

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        int last_inserted_id=0;
        if (rs.next()) {
            last_inserted_id = rs.getInt(1);
        }
        
        if(last_inserted_id==0){
            throw new Exception("Order creation failed");
        }
        
        PreparedStatement ps2 = connection.prepareStatement("INSERT INTO public.\"position\"(orderid, articleid, amount) SELECT ?,articleid,amount FROM card WHERE customerid=?;");
        ps2.setInt(1, last_inserted_id);
        ps2.setInt(2, customerid);
        ps2.executeUpdate();
        
        PreparedStatement ps3 = connection.prepareStatement("DELETE FROM public.card WHERE customerid=?;");

        ps3.setInt(1, customerid);
        ps3.executeUpdate();
    }
    
    /**
     * Returns order positions for an specifc order; check if order is owned by customer
     * @param orderid orderid
     * @param customerid customerid
     * @return order-positions order-positions
     * @throws SQLException SQLException
     */
    public ArrayList<Position> getOrderPositions(int orderid, int customerid) throws SQLException {
        ArrayList<Position> positions = new ArrayList<>();

        PreparedStatement ps = connection.prepareStatement("SELECT ordering.orderdate,(SELECT SUM(position.amount * article.price) FROM public.ordering JOIN position ON ordering.id=position.orderid JOIN article ON article.id=position.articleid WHERE ordering.id=?) total,article.id,article.name,article.price, amount, article.price * amount singletotal FROM public.\"position\" JOIN article ON position.articleid=article.id JOIN ordering ON \"position\".orderid=ordering.id WHERE ordering.id=? AND ordering.customerid=?;");

        ps.setInt(1, orderid);
        ps.setInt(2, orderid);
        ps.setInt(3, customerid);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            positions.add(new Position(new Order(orderid, customerid, rs.getDate("orderdate").toLocalDate(), rs.getDouble("total")),new Article(rs.getInt("id"), rs.getString("name"), rs.getDouble("price")),rs.getInt("amount"),rs.getDouble("singletotal")));
        }

        return positions;
    }
}
