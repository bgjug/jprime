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
<title>Sponsors</title>
</head>
<body>
<div class="admin-container">
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
			<c:forEach var="sponsor" items="${sponsors}">
				<tr>
					<td width="100%">${sponsor.companyName}</td>
					<td>${sponsor.sponsorPackage}</td>
					<td><img src="/image/sponsor/${sponsor.id}"/></td>
                    <td>${sponsor.active}</td>
					<td>
						<a href="/admin/sponsor/edit/${sponsor.id}">Edit</a>&nbsp;|&nbsp;<a href="/admin/sponsor/remove/${sponsor.id}">Remove</a>
					</td>
				</tr>
			</c:forEach>
		</table>
		&nbsp;
		<div class="pagination">
			<c:if test="${number > 0}">
				<a href="/admin/sponsor/view?page=${number - 1}">previous</a>
			</c:if>
			<c:if test="${number < (totalPages - 1)}">
				<a href="/admin/sponsor/view?page=${number + 1}">next</a>
			</c:if>
		</div>
		<div>
			<a style="float:right;" href="/admin/sponsor/add">Add</a>
		</div>
	</fieldset>
</div>
</body>
</html>