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

                    <c:forEach var="article" items="${articles.content}">
                        <!-- Start Post -->
                        <div class="blog-post image-post"  style="clear:both;margin-bottom: 100px;">
                            <!-- Post Thumb
                            <div class="post-head">
                                <a class="lightbox" title="This is an image title" href="images/blog-01.jpg">
                                    <div class="thumb-overlay"><i class="fa fa-arrows-alt"></i></div>
                                    <img alt="" src="images/blog-01.jpg">
                                </a>
                            </div>
                            <!-- Post Content -->
                            <div class="post-content">
                            	<br/><br/>
                                <h2><a href="/nav/article/${article.id}">${article.title} (<joda:format pattern="yyyy" value="${article.createdDate}" />)</a></h2>
                                <ul class="post-meta">
                                   <%-- By <a href="/nav/article/${article.id}">${article.author.firstName} ${article.author.lastName}</a>  &nbsp;&nbsp;
                                    <joda:format value="${article.createdDate}" pattern="dd-MM-yyyy"/>--%>
                                </ul>
                                <div class="truncArt">${article.text}</div>
                                <a class="main-button" href="/nav/article/${article.id}">Read More <i class="fa fa-angle-right"></i></a>
                            </div>
                        </div>
                        
                        <!-- End Post -->
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