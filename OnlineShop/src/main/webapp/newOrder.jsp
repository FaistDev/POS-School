<%-- 
    Document   : newOrder
    Created on : 27.11.2019, 10:02:06
    Author     : Ben
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="ordering.js"></script>
        <link href="styles.css" rel="stylesheet" type="text/css"/>
        <title>New order</title>
    </head>
    <body>
        <h1>New order</h1>
       
        <div class="container">
            <div>
                <h3>Available articles</h3>
                <table class="newOrderTable">
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Price</th>
                        <th></th>

                        <c:forEach var="article" items="${requestScope.articles}">
                        <tr>
                            <td>${article.id}</td>
                            <td>${article.name}</td>
                            <td>${String.format("%.2f",article.price)} €</td>
                            <td class="actionButton"><button onclick="addToCard(${article.id});">+</button></td>
                        </tr>
                    </c:forEach>
                    </tr>
                </table>
            </div>
            <div>
                <h3>Selected articles</h3>
                <table class="newOrderTable">
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Price</th>
                        <th>Amount</th>
                        <th>Total</th>
                        <th></th>
                     </tr>
                    <tbody id="selectedArticlesContent">
                        
                    </tbody>
                    
                </table>
            </div>
            <form method="get" action="saveOrderServlet">
                <button>Save order</button>
            </form>
        </div>
    </body>
</html>
