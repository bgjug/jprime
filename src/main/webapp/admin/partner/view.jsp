<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:directive.include file="../../theme-colors.jsp" />

<script type="text/javascript" src="/js/niceforms.js"></script>
<link rel="stylesheet" type="text/css" media="all" href="/css/niceforms-default.css" />
<link rel="stylesheet" type="text/css" media="all" href="/css/admin.css" />
<title>Partners</title>
</head>
<body>
	<admin:menu/>
	<fieldset>
	<legend>Partners</legend>
		<table class="admin-table">
			<tr>
				<td><i>Name</i></td>
				<td><i>LOGO</i></td>
				<td><i>Active</i></td>
				<td><i>Package</i></td>
				<td><i>Operations</i></td>
			</tr>
			<c:forEach var="partner" items="${partners}">
				<tr>
					<td width="100%">${partner.companyName}</td>
					<td><img src="/image/partner/${partner.id}"/></td>
					<td>${partner.active}</td>
					<td>${partner.partnerPackage}</td>
					<td>
						<a href="/admin/partner/edit/${partner.id}">Edit</a></span>&nbsp;|&nbsp;<a href="/admin/partner/remove/${partner.id}">Remove</a>
					</td>
				</tr>
			</c:forEach>
		</table>
		&nbsp;
		<div>
			<c:if test="${number > 0}">
				<span><a href="/admin/partner/view?page=${number - 1}">previous</a></span>
			</c:if>
			<c:if test="${number < (totalPages - 1)}">
				<span><a href="/admin/partner/view?page=${number + 1}">next</a></span>
			</c:if>
		</div>
		<div>
			<a style="float:right;" href="/admin/partner/add">Add</a>
		</div>
	</fieldset>
</body>
</html>