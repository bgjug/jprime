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
    <legend>Sessions</legend>
    &nbsp;
    <div>
        <a href="/admin/session/add">Add</a>
    </div>
    <table class="admin-table">
        <tr>
            <td><i>Title</i></td>
            <td><i>Abstract</i></td>
            <td><i>Speaker</i></td>
            <td><i>Co-speaker</i></td>
            <td><i>Start time</i></td>
            <td><i>End time</i></td>
            <td><i>Hall</i></td>
            <td><i>Actions</i></td>
        </tr>
        <c:forEach var="session" items="${sessions}">
            <tr>
                <td>${session.title}</td>
                <td>${session.submission.description}</td>
                <td>${session.submission.speaker.firstName} ${session.submission.speaker.lastName}</td>
                <td>${session.submission.coSpeaker.firstName} ${session.submission.coSpeaker.lastName}</td>
                <td>${session.startTime}</td>
                <td>${session.endTime}</td>
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
