<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin"%>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <jsp:directive.include file="../../theme-colors.jsp" />
<admin:pageJavaScriptAndCss/>

    <title>Registrants</title>
</head>
<body>
<div class="admin-container">
<admin:menu/>
<fieldset>
    <legend>Venuew halls</legend>
    &nbsp;
    <div>
        <a href="/admin/hall/add">Add</a>
    </div>
    <table class="admin-table">
        <tr>
            <td><i>Name</i></td>
            <td><i>Description</i></td>
            <td><i>Actions</i></td>
        </tr>
        <c:forEach var="hall" items="${halls}">
            <tr>
                <td>${hall.name}</td>
                <td width="100%">${hall.description}</td>
                <td>
                    <a href="/admin/hall/edit/${hall.id}">Edit</a>&nbsp;|&nbsp;<a href="/admin/hall/remove/${hall.id}">Remove</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</fieldset>
</div>
</body>
</html>
