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
    <title>Buy conference tickets</title>

    <!-- Define Charset -->
    <meta charset="utf-8">

    <!-- Responsive Metatag -->
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

    <jsp:directive.include file="theme-colors.jsp" />

    <!-- Page Description and Author -->
    <meta name="description" content="JPrime Conference">
    <meta name="author" content="JPrime">

    <user:pageJavaScriptAndCss/>

</head>
<body>



<user:header/>

<!-- Page Banner Start -->
<div id="page-banner-area" class="page-banner">
    <div class="page-banner-title">
        <div class="text-center">
            <h2>Registration is closed</h2>
            <%--            <h2>Buy conference tickets</h2>--%>
        </div>
    </div>
</div>
<!-- Page Banner End -->

<section id="about" class="section-padding">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <p>jPrime ${jprime_year} is SOLD OUT !</p>

                <p>The tickets registration for ${jprime_next_year} will be opened in November ${jprime_year}.</p>
                <p>If you want to join ${jprime_year} - don't cry :) there will be streaming probably and there will be recording
                    of everything from ${jprime_year} also at some point :) you can try to buy a ticket on the black market, but I
                    would just watch online;)</p>
                <p>Note that the recordings of the previous conference are available at <a
                        href="https://www.youtube.com/playlist?list=PLcqA4DRMgIYvGPZfrK0EcMxEQCrtSl9A3">https://www.youtube.com/playlist?list=PLcqA4DRMgIYvGPZfrK0EcMxEQCrtSl9A3</a>
                    .</p>
            </div>
        </div>
    </div>
</section>

<user:footer/>

</body>
</html>

