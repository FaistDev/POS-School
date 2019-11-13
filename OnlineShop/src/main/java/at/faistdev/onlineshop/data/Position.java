/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.faistdev.onlineshop.data;

/**
 *
 * @author Ben
 */
public class Position {
    private int orderid;
    private int articleid;
    private int amount;

    public Position(int orderid, int articleid, int amount) {
        this.orderid = orderid;
        this.articleid = articleid;
        this.amount = amount;
    }
    
    

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public int getArticleid() {
        return articleid;
    }

    public void setArticleid(int articleid) {
        this.articleid = articleid;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    
}
