<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<jsp:directive.include file="../../theme-colors.jsp" />
<admin:pageJavaScriptAndCss/>

<title>Speaker edit</title>
</head>
<body>
<div class="admin-container">
	<admin:menu />
		<form:form modelAttribute="speaker" method="post"
			   action="/admin/speaker/add?sourcePage=${sourcePage}" enctype="multipart/form-data">
		<fieldset>
			<legend>Add/Edit Speaker</legend>
			<p>
				<form:errors />
			</p>
			<dl>
				<dt>
					<label for="firstName">First Name</label>
				</dt>
				<dd>
					<form:input path="firstName" />
				</dd>
			</dl>
			<dl>
				<dt>
					<label for="lastName">Last Name</label>
				</dt>
				<dd>
					<form:input path="lastName" />
				</dd>
			</dl>
				<dl>
				<dt>
					<label for="email">Email</label>
				</dt>
				<dd>
					<form:input path="email" />
				</dd>
			</dl>
			<dl>
				<dt>
					<label for="phone">Phone</label>
				</dt>
				<dd>
					<form:input path="phone" />
				</dd>
			</dl>
			<dl>
				<dt>
					<label for="twitter">X</label>
				</dt>
				<dd>
					<form:input path="twitter" />
				</dd>
			</dl>
			<dl>
				<dt>
					<label for="bsky">Bluesky Profile</label>
				</dt>
				<dd>
					<form:input path="bsky" placeholder="A link to Bluesky profile" pattern="^https:\/\/[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\/profile\/\b[-a-zA-Z0-9()@:%_\+.~#?&=]*$" size="30" maxlength="100"/>
				</dd>
			</dl>
			<dl>
				<dt>
					<label for="bio">Bio</label>
				</dt>
				<dd>
					<form:textarea path="bio" cols="70" rows="5" />
				</dd>
			</dl>
			<dl>
				<dt>
					<label for="headline">headline</label>
				</dt>
				<dd>
					<form:textarea path="headline" cols="70" rows="5" />
				</dd>
			</dl>
			<c:if test="${not empty multipartException}">
				<p style="color:darkred">${multipartException}</p>
			</c:if>
			<dl>
				<dt>
					<label for="file">Picture ( MUST be 280x326)</label>
				</dt>
				<dd>
					<table>
						<tr>
							<td>
								<input name="file" type="file"/>
							</td>
							<td>
								<label for="resize">Resize Image</label>
								<input id="resize" type="checkbox" checked name="resizeImage"/>
							</td>
						</tr>
					</table>
				</dd>
			</dl>
			<sec:csrfInput />
			<form:hidden path="id" />
			<form:hidden path="createdDate" />
			<form:hidden path="createdBy" />
			<button type="submit">Save</button>
		</fieldset>

	</form:form>
</div>
</body>
</html>