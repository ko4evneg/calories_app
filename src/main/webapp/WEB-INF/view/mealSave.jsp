<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meal edit</title>
</head>
<body>
<h3><a href="../../index.html">Home</a></h3>
<hr>
<h2>Meal save</h2>
<form method="post" action="meal/save">
    <jsp:useBean id="meal" scope="request" type="com.github.ko4evneg.caloriesApp.model.Meal"/>
    <table>
        <tr>
            <td>
                <label for="desc">Description:</label>
            </td>
            <td>
                <input id="desc" type="text" name="description" value="${meal.description}"/>
            </td>
        </tr>
        <tr>
            <td>
                <label for="cal">Calories:</label>
            </td>
            <td>
                <input id="cal" type="number" name="calories" value="${meal.calories}"/>
            </td>
        </tr>
        <tr>
            <td>
                <label for="date">Date, </label>
                <label for="time">Time:</label>
            </td>
            <td>
                <input id="date" type="date" name="date" value="${meal.dateTime.toLocalDate()}"/>
                <input id="time" type="time" name="time" value="${meal.dateTime.toLocalTime()}"/>
            </td>
        </tr>
    </table>
    <br>
    <input type="submit" value="Save">
</form>
</body>
</html>