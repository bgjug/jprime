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
<title>Tags</title>
</head>
<body>
	<admin:menu/>
	<fieldset>
	<legend>Tags</legend>
		<table class="admin-table">
			<tr>
				<td><i>Name</i></td>
				<td><i>Operations</i></td>
			</tr>
			<c:forEach var="tag" items="${tags}">
				<tr>
					<td width="85%">${tag.name}</td>
					<td width="15%">
						<a href="/admin/tag/edit/${tag.id}">Edit</a>&nbsp;|&nbsp;<a href="/admin/tag/remove/${tag.id}">Remove</a>
					</td>
				</tr>
			</c:forEach>
		</table>
		&nbsp;
		<div>
			<c:if test="${number > 0}">
				<span><a href="/admin/tag/view?page=${number - 1}">previous</a></span>
			</c:if>
			<c:if test="${number < (totalPages - 1)}">
				<span><a href="/admin/tag/view?page=${number + 1}">next</a></span>
			</c:if>
		</div>
		<div>
			<a style="float:right;" href="/admin/tag/add">Add</a>
		</div>
	</fieldset>
</body>
</html>