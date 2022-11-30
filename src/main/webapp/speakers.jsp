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
    <title>jPrime | Blog</title>

    <!-- Define Charset -->
    <meta charset="utf-8">

    <!-- Responsive Metatag -->
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

    <jsp:directive.include file="theme-colors.jsp" />

    <!-- Page Description and Author -->
    <meta name="description" content="jPrime 2023">
    <meta name="author" content="jPrime">

    <user:pageJavaScriptAndCss/>
</head>
<body>

<user:header/>

<!-- Page Banner Start -->
<div id="page-banner-area" class="page-banner">
    <div class="page-banner-title">
        <div class="text-center">
            <h2>Speakers</h2>
        </div>
    </div>
</div>
<!-- Page Banner End -->


<!-- Team Section Start -->
<section id="team" class="section-padding">
    <div class="container">
        <div class="row">

            <c:forEach var="speaker" items="${speakers.content}">
                <div class="col-sm-6 col-md-6 col-lg-3">
                    <!-- Team Item Starts -->
                    <div class="team-item text-center">
                        <div class="team-img">
                            <img class="img-fluid" src="/image/speaker/${speaker.id}" alt="">
                            <div class="team-overlay">
                                <div class="overlay-social-icon text-center">
                                    <ul class="social-icons">
                                        <li><a href="http://twitter.com/${speaker.twitter}"><i class="fa fa-twitter" aria-hidden="true"></i></a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <div class="info-text">
                            <h3><a href="/speaker/${speaker.id}"><c:out value="${speaker.firstName}"/>&nbsp;<c:out value="${speaker.lastName}"/></a></h3>
                            <p><c:out value="${speaker.headline}"/></p>
                        </div>
                    </div>
                    <!-- Team Item Ends -->
                </div>
            </c:forEach>

        </div>
<%--        <div class="row justify-content-center mt-4">--%>
<%--            <div class="col-xs-12">--%>
<%--                <nav aria-label="Page navigation justify-content-md-center">--%>
<%--                    <ul class="pagination">--%>
<%--                        <li class="page-item"><a class="page-link" href="#">Previous</a></li>--%>
<%--                        <li class="page-item"><a class="page-link" href="#">1</a></li>--%>
<%--                        <li class="page-item"><a class="page-link" href="#">2</a></li>--%>
<%--                        <li class="page-item"><a class="page-link" href="#">3</a></li>--%>
<%--                        <li class="page-item"><a class="page-link" href="#">Next</a></li>--%>
<%--                    </ul>--%>
<%--                </nav>--%>
<%--            </div>--%>
<%--        </div>--%>
    </div>
</section>
<!-- Team Section End -->



<user:footer/>


</body>
</html>
