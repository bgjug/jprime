<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/js/niceforms.js"></script>
<link rel="stylesheet" type="text/css" media="all" href="/css/niceforms-default.css" />
<link rel="stylesheet" type="text/css" media="all" href="/css/admin.css" />
<title>Insert title here</title>
</head>
<body>
	<admin:menu/>
	<fieldset>
	<legend>Submissions</legend>
		<table class="admin-table">
			<tr>
				<td><i>Title</i></td>
				<td><i>Abstract</i></td>
                <td><i>Level</i></td>
				<td><i>Speaker</i></td>
				<td><i>Co-Speaker</i></td>
				<td><i>Branch<i></td>
                <td><i>Status</i></td>
				<td><i>Operations</i></td>
			</tr>
			<c:forEach var="submission" items="${submissions.content}">
				<tr>
					<td>${submission.title}</td>
					<td>${submission.description}</td>
                    <td>${submission.level}</td>
					<td>${submission.speaker.firstName} ${submission.speaker.lastName}</td>
					<td>${submission.coSpeaker.firstName} ${submission.coSpeaker.lastName}</td>
					<td>${submission.branch}</td>
                    <td>${submission.status}</td>
					<td>
						<span style="float:left;"><a href="/admin/submission/accept/${submission.id}">Accept</a></span> <br>
						<span style="float:left;"><a href="/admin/submission/reject/${submission.id}">Reject</a></span> <br>
                        <span style="float:left;"><a href="/admin/submission/edit/${submission.id}">Edit</a></span>
					</td>
				</tr>
			</c:forEach>
		</table>
		&nbsp;
		<div>
			<a href="/admin/submission/add">Add</a>
		</div>
	</fieldset>
</body>
</html>