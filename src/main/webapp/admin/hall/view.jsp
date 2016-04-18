<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script type="text/javascript" src="/js/niceforms.js"></script>
    <link rel="stylesheet" type="text/css" media="all" href="/css/niceforms-default.css" />
    <link rel="stylesheet" type="text/css" media="all" href="/css/admin.css" />
    <title>Registrants</title>
</head>
<body>
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
                <td>${hall.description}</td>
                <td>
                    <span><a href="/admin/hall/edit/${hall.id}">Edit</a></span> &nbsp;&nbsp;&nbsp;
                    <span><a href="/admin/hall/remove/${hall.id}">Remove</a></span>
                </td>
            </tr>
        </c:forEach>
    </table>
</fieldset>
</body>
</html>
