<%-- 
    Document   : error
    Created on : 02.12.2019, 12:42:33
    Author     : Ben
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Error</title>
    </head>
    <body>
        <h1>An error occured</h1>
        <p>${requestScope.message}</p>
    </body>
</html>
