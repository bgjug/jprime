<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <jsp:directive.include file="../../theme-colors.jsp"/>


    <script type="text/javascript" src="/js/niceforms.js"></script>
    <link rel="stylesheet" type="text/css" media="all" href="/css/niceforms-default.css"/>
    <link rel="stylesheet" type="text/css" media="all" href="/css/admin.css"/>
    <title>Branches</title>
</head>
<body>
<admin:menu/>
<fieldset>
    <legend>Branches</legend>
    <table class="admin-table">
        <tr>
            <td><i>Year</i></td>
            <td><i>Start date</i></td>
            <td><i>Duration</i></td>
            <td><i>CFP Open</i></td>
            <td><i>CFP Close</i></td>
            <td><i>Sold Out packages</i></td>
            <td><i>Tickets sold out?</i></td>
            <td><i>Agenda published</i></td>
            <td><i>Operations</i></td>
        </tr>
        <c:forEach var="branch" items="${branches}">
            <tr>
                <td><c:if test="${branch.currentBranch}"><span title="Current branch"
                                                               style="color: red">*</span></c:if>${branch.year}
                </td>
                <td><fmt:formatDate value="${branch.startDateTime}" pattern="dd.MM.yyyy"/></td>
                <td>${branch.durationString}</td>
                <td><fmt:formatDate value="${branch.cfpOpenDateTime}" pattern="dd.MM.yyyy"/></td>
                <td><fmt:formatDate value="${branch.cfpCloseDateTime}" pattern="dd.MM.yyyy HH:mm"/></td>
                <td>${branch.soldOutPackages()}</td>
                <td><input disabled type="checkbox" <c:if test="${branch.soldOut}">checked</c:if>></td>
                <td><input disabled type="checkbox" <c:if test="${branch.agendaPublished}">checked</c:if>>
                </td>
                <td>
                    <a href="/admin/branch/edit/${branch.year}">Edit</a>&nbsp;|
                    <c:if test="${branch.currentBranch}">Mark as current</c:if>
                    <c:if test="${!branch.currentBranch}"><a href="/admin/branch/current/${branch.year}">Mark
                        as current</a></c:if>&nbsp;|&nbsp;<a
                        href="/admin/branch/remove/${branch.year}">Remove</a>
                </td>
            </tr>
        </c:forEach>
    </table>
    &nbsp;
    <div>
        <c:if test="${number > 0}">
            <span><a href="/admin/branch/view?page=${number - 1}">previous</a></span>
        </c:if>
        <c:if test="${number < (totalPages - 1)}">
            <span><a href="/admin/branch/view?page=${number + 1}">next</a></span>
        </c:if>
    </div>
    <div>
        <a style="float:right;" href="/admin/branch/add">Add</a>
    </div>
</fieldset>
</body>
</html>