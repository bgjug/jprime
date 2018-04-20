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

    <!-- Page Description and Author -->
    <meta name="description" content="JPrime Conference">
    <meta name="author" content="JPrime">
	<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>
	
    <user:pageJavaScriptAndCss/>

    <script type="text/javascript">

        $(document).ready(function() {
            $('.truncArt').truncate({length:1500})
        });
    </script>
</head>
<body>

<!-- Container -->
<div id="container">

    <user:header/>

    <!-- Start Content -->
    <div id="content">
        <div class="container">
            <div class="row blog-page">

                <!-- Start Blog Posts -->
                <div class="col-md-11 blog-box">

                    <c:forEach var="speaker" items="${speakers.content}">
                        <div class="blog-post image-post"  style="clear:both;margin-bottom: 100px;">
                            <div class="col-md-3 col-sm-6 col-xs-12 animated">
                                <div class="team-member modern">
                                    <!-- Memebr Photo, Name & Position -->
                                    <div class="member-photo">
                                        <img alt="" src="/image/speaker/${speaker.id}"  height="365" width="280">
                                        <div class="member-name">
                                            <c:out value="${speaker.firstName}"/>&nbsp;<c:out value="${speaker.lastName}"/><span><c:out value="${speaker.headline}"/></span>
                                        </div>
                                    </div>
                                    <div class="member-socail" style="text-align: left">
                                        <a class="twitter" href="http://twitter.com/${speaker.twitter}"><i class="fa fa-twitter"></i></a>
                                    </div>
                                </div>
                            </div>

                            <p>
                                <div class="truncArt"><c:out value="${speaker.bio}"/></div>
                            </p>
                            <p>
                                <a class="main-button" href="/speaker/${speaker.id}">Read More <i class="fa fa-angle-right"></i></a>
                            </p>
                        </div>
                    </c:forEach>
                    <!-- Start Pagination // not needed now
                  <div id="pagination">
                      <span class="all-pages">Page 1 of 3</span>
                      <span class="current page-num">1</span>
                      <a class="page-num" href="#">2</a>
                      <a class="page-num" href="#">3</a>
                      <a class="next-page" href="#">Next</a>
                  </div>
                  <!-- End Pagination -->
                </div>
                <!-- End Blog Posts -->

                <user:sidebar/>


            </div>
        </div>
    </div>
    <!-- End Content -->

   <jsp:directive.include file="footer.jsp" />

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