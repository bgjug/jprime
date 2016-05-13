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

    <!-- Page Description and Author -->

    <user:pageJavaScriptAndCss/>
</head>

<body>

<!-- Full Body Container -->
<div id="container">

    <user:header/>

    <!-- Start Client/Partner Section -->
    <div class="section">
        <div class="container">
            <div class="row">

                <!-- Start Big Heading -->
                <div class="big-title text-center">
                    <h3> ${msg}</h3>
                </div>
                <!-- End Big Heading -->

            </div>
            <!-- .row -->
        </div>
        <!-- .container -->
    </div>

    <jsp:directive.include file="footer.jsp" />

</div>
<!-- End Full Body Container -->

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
