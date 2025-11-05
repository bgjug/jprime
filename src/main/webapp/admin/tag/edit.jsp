<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin"%>
	
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<jsp:directive.include file="../../theme-colors.jsp" />
<admin:pageJavaScriptAndCss/>

<title>Tag edit</title>
</head>
<body>
<div class="admin-container">
	<admin:menu/>
	<form:form modelAttribute="tag" method="post"
		action="/admin/tag/add">
		<fieldset>
			<legend>Add/Edit Tags</legend>
			<p>
				<form:errors />
			</p>
			<dl>
				<dt>
					<label for="name">Name</label>
				</dt>
				<dd>
					<form:input path="name" />
				</dd>
			</dl>
			
			<sec:csrfInput />
			<form:hidden path="id" />
			<button type="submit">Save</button>
		</fieldset>

	</form:form>
</div>
</body>
</html>