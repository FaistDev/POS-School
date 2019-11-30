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
public class Card {
    private Customer customer;
    private Article article;
    private int amount;

    public Card(Customer customer, Article article, int amount) {
        this.customer = customer;
        this.article = article;
        this.amount = amount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Article getArticle() {
        return article;
    }

    public int getAmount() {
        return amount;
    }
    
    
}
