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

    <title>users</title>
</head>
<body>
<div class="admin-container">
<admin:menu/>
<fieldset>
    <legend>users</legend>
    &nbsp;
    <div>
        <a href="/admin/user/add">Add</a>
    </div>
    <table class="admin-table">
        <tr>
            <td><i>Email</i></td>
            <td><i>First Name</i></td>
            <td><i>Last Name</i></td>
            <td><i>Actions</i></td>
        </tr>
        <c:forEach var="user" items="${users.content}">
            <tr>
                <td>${user.email}</td>
                <td>${user.firstName}</td>
                <td>${user.lastName}</td>
                <td>
                    <span><a href="/admin/user/remove/${user.id}">Remove</a></span>
                </td>
            </tr>
        </c:forEach>
    </table>
</fieldset>
</div>
</body>
</html>
