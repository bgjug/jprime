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
                        <span> <a href="/admin/article/view/${article.id}">Preview</a></span> &nbsp;&nbsp;&nbsp;
                        <span><a href="/admin/article/edit/${article.id}">Edit</a></span> &nbsp;&nbsp;&nbsp;
						<span><a href="/admin/article/remove/${article.id}"> Remove </a></span>
					</td>
				</tr>
			</c:forEach>
		</table>

	</fieldset>
</body>
</html>