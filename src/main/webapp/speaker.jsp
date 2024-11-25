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
    <title>jPrime | Post</title>

    <!-- Define Charset -->
    <meta charset="utf-8">

    <!-- Responsive Metatag -->
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

    <jsp:directive.include file="theme-colors.jsp"/>

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
            <c:if test="${empty article}">
                <h2><c:out value="${speaker.firstName}"/>&nbsp;<c:out value="${speaker.lastName}"/></h2>
            </c:if>
        </div>
    </div>
</div>
<!-- Page Banner End -->

<br/>

<!-- Start Blog Posts -->
<div class="container">
    <div class="row">
        <div class="col-sm-6 col-md-6 col-lg-3">
            <div class="team-item text-center" style="margin: 0px">
                <div class="team-img">
                    <img class="img-fluid" src="/image/speaker/${speaker.id}" height="365" width="280"/>
                    <div class="team-overlay">
                        <div class="overlay-social-icon text-center">
                            <ul class="social-icons">
                                <c:if test="${speaker.twitter != null and speaker.twitter.length() > 0}">
                                    <li><a href="https://x.com/${speaker.twitter}"><i
                                            class="fa-brands fa-x" aria-hidden="true"></i></a></li>
                                </c:if>
                                <c:if test="${speaker.bsky != null and speaker.bsky.length() > 0}">
                                    <li><a href="${speaker.bsky}"><i class="fa-brands fa-bluesky"
                                                                     aria-hidden="true"></i></a></li>
                                </c:if>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="info-text">
                    <h3><c:out value="${speaker.firstName}"/>&nbsp;<c:out value="${speaker.lastName}"/></h3>
                    <p><c:out value="${speaker.headline}"/></p>
                </div>
            </div>
        </div>
        <div class="col-sm-6 col-md-6 col-lg-9">

            <!-- Start Post -->
            <div class="post-content-block">
                <!-- Post Content -->
                <div class="post-content">
                    <div class="entry-content">
                        <p>
                            <c:out value="${speaker.bio}"/>
                        </p>
                    </div>
                </div>
                <!-- Post Content -->
            </div>
            <!-- End Post -->
        </div>
        <!-- End Blog Posts -->
    </div>
</div>

<br/>

<user:footer/>

</body>
</html>
