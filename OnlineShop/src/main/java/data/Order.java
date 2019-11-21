/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.time.LocalDate;

/**
 *
 * @author Ben
 */
public class Order {
    private int orderid;
    private int customerid;
    private LocalDate orderdate;
    private double totalPrice;

    public Order(int orderID, int customerid, LocalDate orderdate, double totalPrice) {
        this.orderid = orderID;
        this.customerid = customerid;
        this.orderdate = orderdate;
        this.totalPrice = totalPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }
    
    public int getCustomerid() {
        return customerid;
    }

    public void setCustomerid(int customerid) {
        this.customerid = customerid;
    }

    public LocalDate getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(LocalDate orderdate) {
        this.orderdate = orderdate;
    }
    
    
}
