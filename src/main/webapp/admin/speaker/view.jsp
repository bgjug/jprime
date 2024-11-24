<%@ page contentType="text/html; charset=UTF-8"
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
<title>Insert title here</title>
</head>
<body>
	<admin:menu/>
	<fieldset>
	<legend>Speakers</legend>
		<form action="view">
			<table>
				<tr>
					<td><label for="year"><strong>Year:</strong></label></td>
					<td><select name="year" id="year">
						<option value="" <c:if test="${selected_branch == null or selected_branch.length() == 0}">selected</c:if>>All</option>
						<c:forEach var="branch" items="${branches}">
							<option value="${branch.label}" <c:if test="${selected_branch != null && selected_branch.equals(branch.label)}">selected</c:if>>${branch.label}</option>
						</c:forEach>
					</select></td>
					<td><input type="submit" value="Search"></td>
				</tr>
			</table>
		</form>
		<table class="new-admin-table">
			<caption><strong>Speakers</strong></caption>
			<tr>
				<th><em>Picture</em></th>
				<th><em>Name</em></th>
				<th><em>Email</em></th>
				<th><em>Phone</em></th>
				<th><em>Twitter</em></th>
				<th><em>Bluesky</em></th>
				<th><em>Featured?</em></th>
				<th><em>Accepted?</em></th>
				<th><em>Branch?</em></th>
				<th><em>Operations</em></th>
			</tr>
			<c:forEach var="speaker" items="${speakers}">
				<tr>
					<td rowspan="2"><img alt="Speaker picture" src="/image/speaker/${speaker.id}" style="max-width: 280px"/></td>
					<td>${speaker.firstName} ${speaker.lastName} </td>
                    <td>${speaker.email}</td>
					<td>${speaker.phone}</td>
					<td>${speaker.twitter}</td>
					<td>${speaker.bsky}</td>
					<td><input disabled type="checkbox" <c:if test="${speaker.featured}">checked</c:if>></td>
					<td><input disabled type="checkbox" <c:if test="${speaker.accepted}">checked</c:if>></td>
					<td>${speaker.branch}</td>
					<td rowspan="2">
						<a href="edit/${speaker.id}">Edit</a>&nbsp;|&nbsp;<a href="remove/${speaker.id}">Remove</a>
					</td>
				</tr>
				<tr class="title-column">
					<td colspan="7"><em><strong>Bio :</strong></em>${speaker.bio}</td>
				</tr>
			</c:forEach>
		</table>
		&nbsp;
		<div>
			<c:if test="${number > 0}">
				<span><a href="view?page=${number - 1}">previous</a></span>
			</c:if>
			<c:if test="${number < (totalPages - 1)}">
				<span><a href="view?page=${number + 1}">next</a></span>
			</c:if>
		</div>
		<div>
			<a style="float:right;" href="add">Add</a>
		</div>
	</fieldset>
</body>
</html>