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
    <meta name="description" content="jPrime 2019">
    <meta name="author" content="jPrime">

    <user:pageJavaScriptAndCss/>

    <script type="text/javascript">

        $(document).ready(function() {
            $('.truncArt').truncate({length:1500})
        });
    </script>
</head>
<body>

<user:header/>

<!-- Page Banner Start -->
<div id="page-banner-area" class="page-banner">
    <div class="page-banner-title">
        <div class="text-center">
            <c:if test="${empty article}">
                <h2></h2>
            </c:if>
        </div>
    </div>
</div>
<!-- Page Banner End -->

<br/>

<!-- Start Blog Posts -->
<div class="container">
    <c:forEach var="article" items="${articles.content}">
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
                            <div class="truncArt">${article.text}</div>
                            <a class="main-button" href="/nav/article/${article.id}">Read More <i class="fa fa-angle-right"></i></a>
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
                </div>
                <!-- End Post -->
            </div>
            <!-- End Blog Posts -->
        </div>
    </c:forEach>
</div>

<user:footer/>

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


</body>
</html>
