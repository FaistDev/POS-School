/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

/**
 *
 * @author Ben
 */
public class Position {
    private Order order;
    private Article article;
    private int amount;
    private double total;

    public Position(Order order, Article article, int amount, double total) {
        this.order = order;
        this.article = article;
        this.amount = amount;
        this.total=total;
    }

    public Order getOrder() {
        return order;
    }

    public Article getArticle() {
        return article;
    }

    public int getAmount() {
        return amount;
    }

    public double getTotal() {
        return total;
    }

    
    
    
}
