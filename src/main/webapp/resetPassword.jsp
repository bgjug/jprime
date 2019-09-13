<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
           uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user"%>

<!doctype html>
<!--[if IE 8 ]><html class="ie ie8" lang="en"> <![endif]-->
<!--[if (gte IE 9)|!(IE)]><html lang="en" class="no-js"> <![endif]-->
<html lang="en">
<head>

    <!-- Basic -->
    <title>Reset Password</title>

    <!-- Define Charset -->
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
			<h2>Reset Password</h2>
		</div>
	</div>
</div>
<!-- Page Banner End -->


<section id="about" class="section-padding">
	<div class="container">
		<div class="row">
			<div class="col-12">
				<c:choose>
					<c:when test="${not empty sent_to_email}">
						<h2>Check your email</h2>
						<br/>
						<p> We have sent an email to <strong>${sent_to_email}</strong>. Click the link in the email to reset your password.</p>
						<br />
						<p>If you don't see the email, check other places it might be, like your junk, spam, social, or other folders. </p>
						<br />
					</c:when>
					<c:otherwise>
						<h2>Reset your password</h2>
						<p>
						<form:form action="/resetPassword" method="post" enctype="multipart/form-data">
							<div class="form-wrapper">
								<label for="email">Email</label>
								<br/>
								<input type="text" name="email" id="email"/>
								<br/>
								<form:errors path="email"/>
							</div>
							<p><strong>${error_msg}<strong><p>
							<br/>
							<input type="submit" value="reset" class="btn">
						</form:form>
						</p>
						<br />
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
</section>


<user:footer/>

</body>
</html>
