<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin"%>


<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<jsp:directive.include file="../../theme-colors.jsp" />
<admin:pageJavaScriptAndCss/>
<title>Tags</title>
</head>
<body>
<div class="admin-container">
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
		<div class="pagination">
			<c:if test="${number > 0}">
				<a href="/admin/tag/view?page=${number - 1}">previous</a>
			</c:if>
			<c:if test="${number < (totalPages - 1)}">
				<a href="/admin/tag/view?page=${number + 1}">next</a>
			</c:if>
		</div>
		<div>
			<a style="float:right;" href="/admin/tag/add">Add</a>
		</div>
	</fieldset>
</div>
</body>
</html>