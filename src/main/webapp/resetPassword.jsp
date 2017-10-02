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