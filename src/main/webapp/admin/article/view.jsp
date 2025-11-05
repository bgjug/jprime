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
<title>Articles</title>
</head>
<body>
<div class="admin-container">
	<admin:menu/>
	<fieldset>
	<legend>Articles</legend>

		<div>
			<a href="/admin/article/add">Add</a>
		</div>
		&nbsp;
		<table class="admin-table">
			<tr>
				<td><i>Title</i></td>
				<td><i>Description</i></td>
				<td><i>Operations</i></td>
			</tr>
			<c:forEach var="article" items="${articles}">
				<tr>
					<td>${article.title}</td>
					<td>${article.description }</td>
					<td>
                        <a href="/admin/article/view/${article.id}">Preview</a>&nbsp;|&nbsp;<a href="/admin/article/edit/${article.id}">Edit</a>&nbsp;|&nbsp;<a href="/admin/article/remove/${article.id}">Remove</a>
					</td>
				</tr>
			</c:forEach>
		</table>

	</fieldset>
</div>
</body>
</html>