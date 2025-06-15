<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Login Page</title>
</head>
<body>


<h2>Login</h2>

<form method="post" action="login.jsp">
    <label for="username">Username:</label>
    <input type="text" name="username" required><br><br>

    <label for="password">Password:</label>
    <input type="password" name="password" required><br><br>

    <input type="submit" value="Login">
</form>

<%-- Optional error message display --%>
<%
    String error = request.getParameter("error");
    if ("1".equals(error)) {
%>
    <p style="color:red;">Invalid username or password.</p>
<%
    }
%>

</body>
</html>
