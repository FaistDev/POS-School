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
    </head>
    <style>
        table {
            border: solid 2px black;
            border-collapse: collapse;
            width: 90%;
            margin: 0 auto;
            margin-top: 25px;
        }
        th,td {
            border: solid 2px black;
        }
        .orderItem:hover {
            background-color: black;
            color: white;
        }
        #orderDetails {
            width: 90%;
            margin: 0 auto;
            margin-top: 25px;
            display: none;
        }
        #orderDetails table {
            width: 100%;
        }
        .selectedOrder {
            background: grey;
            color: white;
        }
    </style>
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
<script>
    function getOrderDetails(orderid, element) {
        document.getElementById("orderDetails").style.display="block";
        
        const elements = document.getElementsByClassName("orderItem");
        
        for(var i=0;i<elements.length;i++){
            elements[i].classList.remove("selectedOrder");
        }
        element.classList.add("selectedOrder");
        
        //For security purposes, check on server side if order is customers one
        fetch('./getOrderDetailsServlet',
                {
                    method: 'post',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    body: orderid
                }).then(function (response) {
            response.json().then(function (data) {
const obj = JSON.parse(JSON.stringify(data));
                    console.log(JSON.stringify(data));
                   var output="";
                   
                    for(var i=0;i<obj.length;i++){
                        var price = Number(parseFloat(obj[i].article.price).toFixed(2)).toLocaleString('de');
                        var total = Number(parseFloat((obj[i].article.price)*(obj[i].amount)).toFixed(2)).toLocaleString('de');
                        output+= "<tr><td>"+obj[i].article.id+"</td><td>"+obj[i].article.name+"</td><td>"+price+" €</td><td>"+obj[i].amount+"</td><td>"+total+" €</td></tr>";
                    }
                    document.getElementById("orderDetailsItems").innerHTML=output;
            });
        });
    }
</script>
