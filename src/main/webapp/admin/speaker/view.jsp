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
	<legend>Speakers</legend>
		<table class="admin-table">
			<tr>
				<td><i>Name</i></td>
                <td><i>Bio</i></td>
				<td><i>Email</i></td>
				<td><i>Phone</i></td>
				<td><i>Twitter</i></td>
                <td><i>Featured?</i></td>
				<td><i>Accepted?</i></td>
				<td><i>Branch?</i></td>
				<td><i>Picture</i></td>
				<td><i>Video</i></td>
				<td><i>Operations</i></td>
			</tr>
			<c:forEach var="speaker" items="${speakers.content}">
				<tr>
					<td>${speaker.firstName} ${speaker.lastName} </td>
                    <td>${speaker.bio}</td>
                    <td>${speaker.email}</td>
					<td>${speaker.phone}</td>
					<td>${speaker.twitter}</td>
                    <td>${speaker.featured}</td>
					<td>${speaker.accepted}</td>
					<td>${speaker.branch}</td>
					<td><img src="/image/speaker/${speaker.id}" style="max-width: 280px"/></td>
					<td>${speaker.videos}</td>
					<td>
						<span style="float:left;"><a href="/admin/speaker/edit/${speaker.id}">Edit</a></span> &nbsp; 
						<span style="float:right;"><a href="/admin/speaker/remove/${speaker.id}"> Remove </a></span> 
					</td>
				</tr>
			</c:forEach>
		</table>
		&nbsp;
		<div>
			<c:if test="${speakers.number > 0}">
				<span><a href="/admin/speaker/view?page=${speakers.number - 1}">previous</a></span>
			</c:if>
			<c:if test="${speakers.number < (speakers.totalPages - 1)}">
				<span><a href="/admin/speaker/view?page=${speakers.number + 1}">next</a></span>
			</c:if>
		</div>
		<div>
			<a style="float:right;" href="/admin/speaker/add">Add</a>
		</div>
	</fieldset>
</body>
</html>