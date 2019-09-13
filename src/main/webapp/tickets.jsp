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
                <p>
                    <p>The registration is now over, see you in 2020 :)</p>
                    <p>Note that the recordings of the previous conference are available at <a href="https://www.youtube.com/playlist?list=PLcqA4DRMgIYvGPZfrK0EcMxEQCrtSl9A3">https://www.youtube.com/playlist?list=PLcqA4DRMgIYvGPZfrK0EcMxEQCrtSl9A3</a> .</p>
                    <p>The tickets registration for 2020 will be opened at the beginning of 2020.</p>
<%--                    <p>If you want to join 2019 - dont cry :) there will be streaming probably and there will be recording of everything from 2019 also you can try to buy a ticket on the black market ;)</p>--%>
                <%--                <p>The conference fee is <strong>140</strong>.00 BGN (VAT included).</p>--%>
                <%--                <p>For registration contact us at <a href="mailto:conference@jprime.io">conference@jprime.io</a>.</p>--%>
                <%--                <p>* There is a ~25% discount for students. Student ticket price <strong>100</strong>.00 BGN (VAT included).</p>--%>
                <%--                <p>* There is a free pass for a JUG lead (one per Java User Group).</p>--%>
                </p>
            </div>
        </div>
    </div>
</section>

<user:footer/>

</body>
</html>
