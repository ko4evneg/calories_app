<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <style>
        table, th, td {
            border: 1px solid;
            border-collapse: collapse;
        }
    </style>
    <title>Meals</title>
</head>
<body>
<h3><a href="../../index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table>
    <tr>
        <th>Description</th>
        <th>Calories</th>
        <th>Date</th>
    </tr>

    <jsp:useBean id="meals" scope="request" type="java.util.List"/>
    <c:forEach var="meal" items="${meals}">
        <c:set var="color" value="color: black;"/>
        <c:if test="${meal.excess}">
            <c:set var="color" value="color: red;"/>
        </c:if>
        <tr>
            <td><span style="${color}">${meal.description}</span></td>
            <td><span style="${color}">${meal.calories}</span></td>
            <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="date"/>
            <td><span style="${color}"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${date}"/></span></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>