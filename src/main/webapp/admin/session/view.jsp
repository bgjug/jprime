<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin"%>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <jsp:directive.include file="../../theme-colors.jsp" />

    <script type="text/javascript" src="/js/niceforms.js"></script>
    <link rel="stylesheet" type="text/css" media="all" href="/css/niceforms-default.css" />
    <link rel="stylesheet" type="text/css" media="all" href="/css/admin.css" />
    <title>Registrants</title>
    <style>
        fieldset {
            width: 50%;
        }
    </style>
</head>
<body>
<admin:menu/>
<fieldset>
    <legend>Sessions</legend>
    &nbsp;
    <div>
        <a href="/admin/session/add">Add</a>
    </div>
    <table class="admin-table">
        <tr>
            <td><i>Title</i></td>
<%--            <td><i>Abstract</i></td>--%>
<%--            <td><i>Speaker</i></td>--%>
            <td><i>Co-speaker</i></td>
            <td><i>Start time</i></td>
            <td><i>End time</i></td>
            <td><i>Hall</i></td>
            <td><i>Actions</i></td>
        </tr>
        <c:forEach var="session" items="${sessions}">
            <tr>
                <td>
                    <c:if test="${session.submission != null}">
                        <a href="/admin/submission/edit/${session.submission.id}?sourcePage=/admin/session/view">${session.title}</a><p>
                        <a href="/admin/speaker/edit/${session.submission.speaker.id}?sourcePage=/admin/session/view">${session.submission.speaker.name}</a>
                    </p>
                    </c:if>
                    <c:if test="${session.submission == null}">
                        ${session.title}
                    </c:if>
                </td>
                <td>
                    <c:if test="${session.submission != null and session.submission.coSpeaker != null}">
                        <a href="/admin/speaker/edit/${session.submission.coSpeaker.id}?sourcePage=/admin/session/view">${session.submission.coSpeaker.name}</a>
                    </c:if>
                    <c:if test="${session.submission == null or session.submission.coSpeaker == null}">
                        &nbsp;
                    </c:if>
                </td>
                <td><joda:format value="${session.startTime}" pattern="dd.MM.yyyy HH:mm"/></td>
                <td><joda:format value="${session.endTime}" pattern="dd.MM.yyyy HH:mm"/></td>
                <td>${session.hall.name}</td>
                <td>
                    <span><a href="/admin/session/edit/${session.id}">Edit</a></span> &nbsp;&nbsp;&nbsp;
                    <span><a href="/admin/session/remove/${session.id}">Remove</a></span>
                </td>
            </tr>
        </c:forEach>
    </table>
</fieldset>
</body>
</html>
