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
            <h2>Signup</h2>
        </div>
    </div>
</div>
<!-- Page Banner End -->


<section id="about" class="section-padding">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <p>
                    <form:form modelAttribute="user" action="/signup" method="post" enctype="multipart/form-data">

                <div class="form-wrapper">
                    <label for="firstName">First Name</label> <br/>
                    <input type="text" name="firstName" id="name">
                    <form:errors path="firstName"/>
                </div>
                <div class="form-wrapper">
                    <label for="lastName">Last Name</label><br/>
                    <input type="text" name="lastName" id="username">
                    <form:errors path="lastName"/>
                </div>
                <div class="form-wrapper">
                    <label for="email">Email *</label><br/>
                    <input type="email" name="email" id="email">
                    <form:errors path="email"/>
                </div>
                <div class="form-wrapper">
                    <label for="password">Password *</label><br/>
                    <input type="password" name="password" id="password">
                    <form:errors path="password"/>
                </div>
                <div class="form-wrapper">
                    <label for="password">Confirm password *</label><br/>
                    <input type="password" name="cpassword" id="cpassword">
                    <form:errors path="cpassword"/>
                </div>
                <br/>
                <input type="submit" value="Register" class="btn">
                <a href="<c:out value='/login'/>" style="margin-left: 30px; position: absolute;">Back to Login?</a>
                </form:form>
                </p>
            </div>
        </div>
    </div>
</section>


<user:footer/>


</body>
</html>
