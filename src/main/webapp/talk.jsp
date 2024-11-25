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
            <c:choose>
                <c:when test="${empty talk}">
                    <h2>No talk information available</h2>
                </c:when>
                <c:otherwise>
                    <h2>${talk.submission.title} (<fmt:formatDate pattern="HH:mm"
                                                                  value="${talk.startDateTime}"/> -
                        <fmt:formatDate pattern="HH:mm" value="${talk.endDateTime}"/> on <fmt:formatDate
                                pattern="EEEE" value="${talk.endDateTime}"/>)</h2>
                </c:otherwise>
            </c:choose>
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
                    <img class="img-fluid" src="/image/speaker/${talk.submission.speaker.id}" height="365"
                         width="280"/>
                    <div class="team-overlay">
                        <div class="overlay-social-icon text-center">
                            <ul class="social-icons">
                                <c:if test="${talk.submission.speaker.twitter != null and talk.submission.speaker.twitter.length() > 0}">
                                    <li><a href="https://x.com/${talk.submission.speaker.twitter}"><i
                                            class="fa-brands fa-x" aria-hidden="true"></i></a></li>
                                </c:if>
                                <c:if test="${talk.submission.speaker.bsky != null and talk.submission.speaker.bsky.length() > 0}">
                                    <li><a href="${talk.submission.speaker.bsky}"><i
                                            class="fa-brands fa-bluesky" aria-hidden="true"></i></a></li>
                                </c:if>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="info-text">
                    <h3><c:out value="${talk.submission.speaker.firstName}"/>&nbsp;<c:out
                            value="${talk.submission.speaker.lastName}"/></h3>
                    <p><c:out value="${talk.submission.speaker.headline}"/></p>
                </div>
            </div>
        </div>
        <c:if test="${not empty talk.submission.coSpeaker}">
            <div class="col-sm-6 col-md-6 col-lg-3">
                <div class="team-item text-center" style="margin: 0px">
                    <div class="team-img">
                        <img class="img-fluid" src="/image/speaker/${talk.submission.coSpeaker.id}"
                             height="365" width="280"/>
                        <div class="team-overlay">
                            <div class="overlay-social-icon text-center">
                                <ul class="social-icons">
                                    <c:if test="${talk.submission.coSpeaker.twitter != null and talk.submission.coSpeaker.twitter.length() > 0}">
                                        <li><a href="https://x.com/${talk.submission.coSpeaker.twitter}"><i
                                                class="fa-brands fa-x" aria-hidden="true"></i></a></li>
                                    </c:if>
                                    <c:if test="${talk.submission.coSpeaker.bsky != null and talk.submission.coSpeaker.bsky.length() > 0}">
                                        <li><a href="${talk.submission.coSpeaker.bsky}"><i
                                                class="fa-brands fa-bluesky" aria-hidden="true"></i></a></li>
                                    </c:if>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="info-text">
                        <h3><c:out value="${talk.submission.coSpeaker.firstName}"/>&nbsp;<c:out
                                value="${talk.submission.coSpeaker.lastName}"/></h3>
                        <p><c:out value="${talk.submission.coSpeaker.headline}"/></p>
                    </div>
                </div>
            </div>
        </c:if>

        <div class="col-sm-6 col-md-6 ${not empty talk.submission.coSpeaker ? 'col-lg-push-9': 'col-lg-9'}">

            <!-- Start Post -->
            <div class="post-content-block">
                <!-- Post Content -->
                <div class="post-content">
                    <div class="entry-content">
                        <p>
                            ${talk.submission.description}
                        </p>
                        <p><b>Talk Level:</b><br/>
                            ${talk.submission.level}
                        </p>
                        <c:choose>
                            <c:when test="${not empty talk.submission.coSpeaker}">
                                <p>
                                    <b>Speakers:</b><br/>
                                <p>
                                    <b><c:out value="${talk.submission.speaker.firstName}"/>&nbsp;<c:out
                                            value="${talk.submission.speaker.lastName}"/>: </b>
                                        ${talk.submission.speaker.bio}
                                </p>
                                <p>
                                    <b><c:out value="${talk.submission.coSpeaker.firstName}"/>&nbsp;<c:out
                                            value="${talk.submission.coSpeaker.lastName}"/>: </b>
                                        ${talk.submission.coSpeaker.bio}
                                </p>
                                </p>
                            </c:when>
                            <c:otherwise>
                                <p>
                                    <b>Bio:</b><br/>${talk.submission.speaker.bio}
                                </p>
                            </c:otherwise>
                        </c:choose>
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
