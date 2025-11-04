<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
           uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user"%>

<!doctype html>
<html lang="en">
<head>

    <!-- Basic -->
    <title>Signup</title>

	<meta charset="utf-8">

	<!-- Responsive Metatag -->
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

	<%--    <jsp:directive.include file="theme-colors.jsp" />--%>

	<!-- Page Description and Author -->

	<user:pageJavaScriptAndCss/>

</head>
<body>

<user:header/>

<!-- Page Banner Start -->
<div id="page-banner-area" class="page-banner">
	<div class="page-banner-title">
		<div class="text-center">
			<h2>Login</h2>
		</div>
	</div>
</div>
<!-- Page Banner End -->


<section id="about" class="section-padding">
	<div class="container">
		<div class="row">
			<div class="col-12">
				<p>
					<c:if test="${not empty error}">
				<div class="error">${error}</div>
				</c:if>
				<c:if test="${not empty msg}">
					<div class="msg">${msg}</div>
				</c:if>
				<sec:authorize access="hasRole('USER')">
					welcome USER
				</sec:authorize>
				<sec:authorize access="hasRole('ADMIN')">
					welcome ADMIN
				</sec:authorize>
				<form name='loginForm'
					  action="<c:url value='/login' />" method='POST'>


					<table>
						<tr>
							<td>User:</td>
							<td><input type='text' name='username' value=''></td>
						</tr>
						<tr>
							<td>Password:</td>
							<td><input type='password' name='password' /></td>
						</tr>
						<tr>
							<td>
								<br/>
							</td>
							<td>
								<a href="<c:out value='/resetPassword'/>" style="margin-left: 30px; position: absolute;">Forgot your password?</a>
							</td>
						</tr>
						<tr>
							<td>
								<input name="submit" type="submit" value="submit" />
							</td>
							<td>
								<a href="<c:out value='/signup'/>" style="margin-left: 30px; position: absolute;"> Register </a>
							</td>
						</tr>
					</table>

				</form>
			</div>
		</div>
	</div>
</section>


<user:footer/>


</body>
</html>
