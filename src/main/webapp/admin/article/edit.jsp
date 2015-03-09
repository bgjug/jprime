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
	<form:form commandName="article" method="post"
		action="/admin/article/add">
		<fieldset>
			<legend>Add/Edit Article</legend>
			<p>
				<form:errors />
			</p>
			<dl>
				<dt>
					<label for="title">Title</label>
				</dt>
				<dd>
					<form:input path="title" />
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
					<label for="text">Text</label>
				</dt>
				<dd>
					<form:textarea path="text" cols="70" rows="20" />
				</dd>
			</dl>
			<dl>
				<dt>
					<label for="tag">Tag</label>
				</dt>
				<dd>
					<form:select path="tags" multiple="true" items="${tags}" itemLabel="name" itemValue="id"/>
				</dd>
			</dl>
			<sec:csrfInput />
			<form:hidden path="id" />
			<button type="submit">Save</button>
		</fieldset>

	</form:form>
</body>
</html>