<%@ page trimDirectiveWhitespaces="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>

<!doctype html>
<!--[if IE 8 ]><html class="ie ie8" lang="en"> <![endif]-->
<!--[if (gte IE 9)|!(IE)]><html lang="en" class="no-js"> <![endif]-->
<html lang="en">
<head>
	
    <!-- Basic -->
    <title>jPrime | Venue</title>

    <!-- Define Charset -->
    <meta charset="utf-8">

    <!-- Responsive Metatag -->
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

    <!-- Page Description and Author -->
    <meta name="description" content="JPrime Conference">
    <meta name="author" content="JPrime">
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

            <p class="text-center"> <br/> jPrime is held on <strong>27th May 2015</strong> in Sofia Event Center. </p>

            <center><iframe src="https://www.google.com/maps/embed?pb=!1m0!3m2!1sru!2sbg!4v1429266821187!6m8!1m7!1sipo8Sl0qHi8AAAQfDR2bcQ!2m2!1d42.657808!2d23.315222!3f307.9207157147706!4f-10.178049410940872!5f0.7820865974627469" width="100%" height="450" frameborder="0" style="border:0"></iframe></center>
            <center><iframe src="https://www.google.com/maps/embed?pb=!1m14!1m8!1m3!1d11736.674105662074!2d23.314898!3d42.657784!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x0%3A0x968e1b14857aa12d!2z0J_QkNCg0JDQlNCQ0JnQoSDQptCV0J3QotCq0KA!5e0!3m2!1sbg!2sbg!4v1429266435259&style=saturation:-75|gamma:1.07|hue:0xffe500" width="100%" height="500" frameborder="0" style="border:0"></iframe></center>

        </div>
    </div>
    <!-- End Content -->

    <user:footer/>

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