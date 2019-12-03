<%-- 
    Document   : login
    Created on : 14.11.2019, 09:20:58
    Author     : Ben
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<html>
    <head>
        <title>Online-Shop | Login</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <h1>Login</h1>
        
        <p id="errorMessage">${requestScope.error}</p>
        
        <form action="./loginServlet" method="post">
            <div class="form-element">
                <label>Username:</label>
                <input type="text" name="username" required=""/>
            </div>
            
            <div class="form-element">
                <label>Password:</label>
                <input type="password" name="password" required=""/>
            </div>
           
            <button type="submit">Login</button>
        </form>
    </body>
</html>
