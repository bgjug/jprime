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
	<legend>Visitors</legend>
		<table class="admin-table">
			<tr>
				<td><i>Name</i></td>
				<td><i>Email</i></td>
				<td><i>Payed</i></td>
				<td><i>Registrant</i></td>
				<td><i>Invoice number</i></td>
				<td><i>Operations</i></td>
			</tr>
			<c:forEach var="visitor" items="${visitors}">
				<tr>
					<td>${visitor.name}</td>
					<td>${visitor.email}</td>
					<td>${visitor.status}</td>
					<td>${visitor.registrant.name}</td>
					<td>${not empty visitor.registrant and not empty visitor.registrant.realInvoiceNumber ?visitor.registrant.realInvoiceNumber:''}</td>
					<td>

						<span><a href="/admin/visitor/edit/${visitor.id}">Edit</a></span> &nbsp;&nbsp;&nbsp;

						<span><a href="/admin/visitor/remove/${visitor.id}"> Remove </a></span>
					</td>
				</tr>
			</c:forEach>
		</table>
		&nbsp;
		<div>
			<a href="/admin/visitor/add">Add</a>
		</div>
	</fieldset>
</body>
</html>