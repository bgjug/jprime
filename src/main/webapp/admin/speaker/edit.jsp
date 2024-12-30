<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:directive.include file="../../theme-colors.jsp" />

<title>Insert title here</title>
<script type="text/javascript" src="/js/niceforms.js"></script>
<link rel="stylesheet" type="text/css" media="all"
	href="/css/niceforms-default.css" />
<link rel="stylesheet" type="text/css" media="all" href="/css/admin.css" />
</head>
<body>
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
            <dl>
                <dt>
                    <label for="featured">featured</label>
                </dt>
                <dd>
                    <form:checkbox path="featured"/>
                </dd>
            </dl>
			<dl>
				<dt>
					<label for="accepted">accepted</label>
				</dt>
				<dd>
					<form:checkbox path="accepted"/>
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
</body>
</html>