<%-- 
    Document   : orders
    Created on : 15.11.2019, 12:27:13
    Author     : Ben
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Online-Shop | Orders</title>
        <link href="styles.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <h1>Your orders</h1>
        <form action="newOrderServlet">
            <button>New order</button>
        </form>
        <table>
            <tr>
                <th>OrderID</th>
                <th>Date</th>
                <th>Total Price</th>
            </tr>
            <c:forEach var="order" items="${requestScope.orders}">
                <tr onclick="getOrderDetails(${order.orderid}, this);" class="orderItem">
                    <td>${order.orderid}</td>
                    <td>${order.orderdate}</td>
                    <td>${String.format("%.2f",order.totalPrice)} €</td>
                </tr>
            </c:forEach>
        </table>

        <div id="orderDetails">
            <h3>Order Details</h3>
            <table>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Price</th>
                    <th>Amount</th>
                    <th>Total</th>
                </tr>
                <tbody id="orderDetailsItems">

                </tbody>
            </table>
        </div>

    </body>
</html>
<script src="ordering.js" type="text/javascript"></script>
