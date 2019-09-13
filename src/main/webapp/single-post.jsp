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
    <title>jPrime | Post</title>

    <!-- Define Charset -->
    <meta charset="utf-8">

    <!-- Responsive Metatag -->
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

    <jsp:directive.include file="theme-colors.jsp" />

    <!-- Page Description and Author -->
    <meta name="description" content="jPrime 2019">
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
                <h2>No article available</h2>
            </c:if>
        </div>
    </div>
</div>
<!-- Page Banner End -->

<br/>

<!-- Start Blog Posts -->
<div class="container">
    <div class="row">
        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">

            <!-- Start Post -->
            <div class="post-content-block">
                <!-- Post Content -->
                <div class="post-content">
                    <h3 class="post-title">${article.title}</h3>
                    <div class="post-meta">
                        <span class="post-author"><a href="#"><i class="icon-user"></i> By jPrime</a></span>
        <%--                <span class="post-cat"><i class="icon-folder"></i>News</span>--%>
        <%--                <span class="post-comment"><a href="#"><i class="icon-bubble"></i> (5) Comments</a></span>--%>
                    </div>
                    <div class="entry-content">
                        <p>${article.text}</p>
                    </div>
                    <div class="tags-area clearfix">
                        <div class="post-tags pull-left">
                            <c:forEach var="tag" items="${article.tags}">
                                <a href="/nav/${tag.name}">${tag.name}</a>
                            </c:forEach>
                        </div>
                    </div>
                </div>
                <!-- Post Content -->
                <br/><br/>
                <a href="" onclick="window.history.back()">Back</a>
            </div>
            <!-- End Post -->
        </div>
        <!-- End Blog Posts -->
    </div>
</div>

<user:footer/>

</body>
</html>
