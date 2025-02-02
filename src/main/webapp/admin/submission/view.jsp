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
<script type="text/javascript" src="/js/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" media="all" href="/css/niceforms-default.css" />
<link rel="stylesheet" type="text/css" media="all" href="/css/admin.css" />
<title>Submissions</title>
</head>
<body>
	<admin:menu/>
	<fieldset>
	<legend>Submissions</legend>
	    <c:if test="${isCurrentYearOnly}">
		    <div>
	            <a href="/admin/submission/view/all">See all submissions</a>
	        </div>
        </c:if>
        <div>
        	<a href="/admin/submission/exportCSV">Export to CSV</a>
        </div>
		<table class="new-admin-table">
			<caption><strong>Submissions</strong></caption>
			<tr>
                <th><em>Level</em></th>
				<th><em>Type</em></th>
				<th><em>Speaker</em></th>
				<th><em>Co-Speaker</em></th>
				<th><em>Branch</em></th>
                <th><em>Status</em></th>
				<th><em>Operations</em></th>
			</tr>
			<c:forEach var="submission" items="${submissions}">
				<tr>
					<td colspan="6" class="title-column"><em><strong>Title</strong></em>: ${submission.title}</td>
					<td rowspan="3" class="title-column">
						<a href="/admin/submission/accept/${submission.id}">Accept</a>&nbsp;|&nbsp;<a href="/admin/submission/reject/${submission.id}">Reject</a><br/>
						<a href="/admin/submission/edit/${submission.id}">Edit</a>&nbsp;|&nbsp;<a href="/admin/submission/notify/${submission.id}">Notify</a><br/>
						<a href="/admin/submission/delete/${submission.id}" class="confirmation">Delete</a>
					</td>
				</tr>
				<tr>
					<td colspan="6"><em><strong>Abstract</strong></em>: ${submission.description}</td>
				</tr>
				<tr>
					<td>${submission.level}</td>
					<td>${submission.type != null ? submission.type.toString() : "Conference session"}</td>
					<td>${submission.speaker.firstName} ${submission.speaker.lastName}</td>
					<td>${submission.coSpeaker.firstName} ${submission.coSpeaker.lastName}</td>
					<td>${submission.branch}</td>
					<td>${submission.status}</td>
				</tr>
			</c:forEach>
		</table>
		&nbsp;
		<div>
			<c:if test="${number > 0}">
				<span><a href="/admin/submission/view${path}?page=${number - 1}">previous</a></span>
			</c:if>
			<c:if test="${number < (totalPages - 1)}">
				<span><a href="/admin/submission/view${path}?page=${number + 1}">next</a></span>
			</c:if>
		</div>
		<div>
			<a style="float:right;" href="/admin/submission/add">Add</a>
		</div>

		<c:if test="${msg != null}">
			<script type="text/javascript">
				alert("${msg}")
			</script>
		</c:if>
	</fieldset>

	<script type="text/javascript">
		$('.confirmation').on('click', function () {
			return confirm('Please confirm that you want to delete this submission?');
		});
	</script>
</body>
</html>