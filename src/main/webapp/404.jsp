<%@ page trimDirectiveWhitespaces="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>

<!doctype html>
<!--[if IE 8 ]><html class="ie ie8" lang="en"> <![endif]-->
<!--[if (gte IE 9)|!(IE)]><html lang="en" class="no-js"> <![endif]-->
<html lang="en">

<head>

    <!-- Basic -->
    <title>jPrime | Home</title>

    <!-- Define Charset -->
    <meta charset="utf-8">

    <!-- Responsive Metatag -->
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

    <jsp:directive.include file="theme-colors.jsp" />

    <!-- Page Description and Author -->
    <meta name="description" content="jPrime ${jprime_year}">
    <meta name="author" content="jPrime">

    <user:pageJavaScriptAndCss/>
</head>

<body>

<user:header/>

<!-- Page Banner Start -->
<div id="page-banner-area" class="page-banner">
    <div class="page-banner-title">
        <div class="text-center">
            <h2>404</h2>
        </div>
    </div>
</div>
<!-- Page Banner End -->

<!-- error section -->
<section class="matrl-error-section section-padding">
    <div class="container">
        <div class="row">
            <div class="col-md-12 text-center">
                <h1 class="wow animated fadeInRight" data-wow-delay=".2s">404</h1>
                <h2 class="wow animated fadeInRight" data-wow-delay=".4s">Page not found! :(</h2>
                <a href="javascript:void(0)" class="wow animated fadeInUp btn btn-common mt-5" data-wow-delay=".6s"><i class="material-icons mdi mdi-home"></i> Back to home<div class="ripple-container"></div></a>
            </div>
        </div>
    </div>
</section>
<!-- error section end -->

<user:footer/>


</body>

</html>
