<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin"%>
	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="/js/niceforms.js"></script>
<link rel="stylesheet" type="text/css" media="all" href="/css/niceforms-default.css" />
<link rel="stylesheet" type="text/css" media="all" href="/css/admin.css" />
</head>
<body>
	<admin:menu/>
	<form:form modelAttribute="partner" method="post"
		action="/admin/partner/add" enctype="multipart/form-data">
		<fieldset>
			<legend>Add/Edit Partner</legend>
			<p>
				<form:errors />
			</p>
			<dl>
				<dt>
					<label for="companyName">companyName</label>
				</dt>
				<dd>
					<form:input path="companyName" />
				</dd>
			</dl>
			<dl>
				<dt>
					<label for="description">Description</label>
				</dt>
				<dd>
					<form:textarea path="description" cols="70" rows="5" />
				</dd>
			</dl>
			<dl>
				<dt>
					<label for="companyWebsite">companyWebsite</label>
				</dt>
				<dd>
					<form:input path="companyWebsite" />
				</dd>
			</dl>
			<c:if test="${not empty multipartException}">
				<p style="color:darkred">${multipartException}</p>
			</c:if>
			<dl>
				<dt>
					<label for="file">logo ( MUST BE 180x64 !!!)</label>
				</dt>
				<dd>
					<input name="file" type="file" />
				</dd>
			</dl>
			<dl>
				<dt>
					<label for="active">active</label>
				</dt>
				<dd>
					<form:checkbox path="active"/>
				</dd>
			</dl>
			<dl>
				<dt>
					<label for="package">package ( type MEDIA, OTHER)</label>
				</dt>
				<dd>
					<form:input path="partnerPackage" />
				</dd>
			</dl>
			<sec:csrfInput />
			<form:hidden path="id" />
			<form:hidden path="createdDate" />
			<form:hidden path="createdBy" />
			<button type="submit">Save</button>
		</fieldset>

	</form:form>
</body>
</html>