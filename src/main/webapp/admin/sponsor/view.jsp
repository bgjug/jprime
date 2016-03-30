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
	<legend>Sponsors</legend>
		<table class="admin-table">
			<tr>
				<td><i>Name</i></td>
				<td><i>Package</i></td>
				<td><i>LOGO</i></td>
                <td><i>Active</i></td>
				<td><i>Operations</i></td>
			</tr>
			<c:forEach var="sponsor" items="${sponsors.content}">
				<tr>
					<td>${sponsor.companyName}</td>
					<td>${sponsor.sponsorPackage}</td>
					<td><img src="/image/sponsor/${sponsor.id}"/></td>
                    <td>${sponsor.active}</td>
					<td>
						<span style="float:left;"><a href="/admin/sponsor/edit/${sponsor.id}">Edit</a></span> &nbsp; 
						<span style="float:right;"><a href="/admin/sponsor/remove/${sponsor.id}"> Remove </a></span> 
					</td>
				</tr>
			</c:forEach>
		</table>
		&nbsp;
		<div>
			<c:if test="${sponsors.number > 0}">
				<span><a href="/admin/sponsor/view?page=${sponsors.number - 1}">previous</a></span>
			</c:if>
			<c:if test="${sponsors.number < (sponsors.totalPages - 1)}">
				<span><a href="/admin/sponsor/view?page=${sponsors.number + 1}">next</a></span>
			</c:if>
		</div>
		<div>
			<a style="float:right;" href="/admin/sponsor/add">Add</a>
		</div>
	</fieldset>
</body>
</html>