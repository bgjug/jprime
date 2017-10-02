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
    <title>Signup</title>

    <!-- Define Charset -->
    <meta charset="utf-8">

    <!-- Responsive Metatag -->
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

    <!-- Page Description and Author -->
	<meta name="description" content="jPrime 2018">
	<meta name="author" content="jPrime">
    <base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>
    
    <user:pageJavaScriptAndCss/>

</head>
<body>

<!-- Container -->
<div id="container">

    <user:header/>

    <!-- Start Content -->
    <div id="content">
        <div class="container">
            <div class="row blog-post-page">
                <div class="col-md-9 blog-box">

                    <!-- Start Single Post Area -->
                    <div class="blog-post gallery-post">

                        <!-- Start Single Post Content -->
                        <div class="post-content">
                            
                            <h3>Login</h3>

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
										<td colspan="2"><a href="<c:out value='/resetPassword'/>" style="margin-left: 100px; position: absolute;">Forgot your password?</a></td>
									</tr>
									<tr>
										<td>
										<br/>
										<input name="submit" type="submit"
											value="submit" />
										</td>
										<td>
											<a href="<c:out value='/signup'/>" style="margin-left: 80px; position: absolute;"> Register </a>
										</td>
									</tr>
								</table>
								
								<input type="hidden" name="${_csrf.parameterName}"
									value="${_csrf.token}" />
					
							</form>
                            
                        </div>
                        <!-- End Single Post Content -->

                    </div>
                    <!-- End Single Post Area -->

                </div>



                <user:sidebar/>

            </div>

        </div>
    </div>
    <!-- End content -->


    <jsp:directive.include file="footer.jsp" />
</div>
<!-- End Container -->

<!-- Go To Top Link -->
<a href="#" class="back-to-top"><i class="fa fa-angle-up"></i></a>

<div id="loader">
    <div class="spinner">
        <div class="dot1"></div>
        <div class="dot2"></div>
    </div>
</div>



</body>
</html>