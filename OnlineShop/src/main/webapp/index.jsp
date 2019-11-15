<% 
if(request.getSession().getAttribute("customerID")!=null){
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/orders.jsp");
        rd.forward(request, response);
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <title>Online Shop</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <h1>Welcome to Silkroad</h1>
        <p>Please login <a href="login.jsp">here</a></p>
    </body>
</html>
