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
<form method="post">
    <c:if test="${meal != null}">
        <jsp:useBean id="meal" scope="request" type="com.github.ko4evneg.caloriesApp.model.MealTo"/>
    </c:if>
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
                <label for="datetime">Date: </label>
            </td>
            <td>
                <input id="datetime" type="datetime-local" name="datetime" value="${meal.dateTime}"/>
            </td>
        </tr>
    </table>
    <br>
    <input type="hidden" name="id" value="${meal.id}"/>
    <input type="submit" name="action" value="Save">
</form>
</body>
</html>