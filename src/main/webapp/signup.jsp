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
                            <%--<h2>Buy conference tickets</h2>
                            <p>
                            <p>The conference fee is <strong>50</strong>.00 EUR (VAT included).</p>
                            <p>For registration contact us at <a href="mailto:conference@jprime.io">conference@jprime.io</a>.</p>
                             --%>
                             <h2>Signup</h2>
                             <p>
                             	<form:form modelAttribute="user" action="/signup" method="post" enctype="multipart/form-data">
	                    
                                <div class="form-wrapper">
                                    <label for="firstName">First Name</label> <br/>
                                    <input type="text" name="firstName" id="name"><br/>
		                        	<form:errors path="firstName"/>
                                </div>
                                <div class="form-wrapper">
                                    <label for="lastName">Last Name</label><br/>
                                    <input type="text" name="lastName" id="username"><br/>
		                        	 <form:errors path="lastName"/>
                                </div>
                                <div class="form-wrapper">
                                    <label for="email">Email *</label><br/>
                                    <input type="email" name="email" id="email"><br/>
		                        	<form:errors path="email"/>
                                </div>
                                <div class="form-wrapper">
                                    <label for="password">Password *</label><br/>
                                    <input type="password" name="password" id="password"><br/>
		                        	<form:errors path="password"/>
                                </div>
                                <div class="form-wrapper">
                                    <label for="password">Confirm password *</label><br/>
                                    <input type="password" name="cpassword" id="cpassword"><br/>
		                        	<form:errors path="cpassword"/>
                                </div>
                                <br/>
                                <input type="submit" value="Join" class="btn">
                            </form:form>
                             </p>
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