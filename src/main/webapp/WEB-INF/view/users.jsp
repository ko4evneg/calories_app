<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <style>
        table, th, td {
            border: 1px solid;
            border-collapse: collapse;
        }
    </style>
    <title>Users</title>
</head>
<body>
<h3><a href="../../index.html">Home</a></h3>
<hr>
<h2>Users</h2>
<table>
    <tr>
        <th>Name</th>
        <th>Email</th>
        <th>Password</th>
        <th>Calories per day</th>
        <th>Enabled</th>
    </tr>
    <jsp:useBean id="users" scope="request" type="java.util.List"/>
    <c:forEach var="user" items="${users}">
        <tr>
            <td>${user.name}</td>
            <td>${user.email}</td>
            <td>${user.password}</td>
            <td>${user.caloriesPerDay}</td>
            <td>${user.enabled}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>