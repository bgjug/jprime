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
    <title>Margo | Blog</title>

    <!-- Define Charset -->
    <meta charset="utf-8">

    <!-- Responsive Metatag -->
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

    <!-- Page Description and Author -->
    <meta name="description" content="Margo - Responsive HTML5 Template">
    <meta name="author" content="iThemesLab">

    <user:pageJavaScriptAndCss/>

    <script type="text/javascript">

        $(document).ready(function() {
            $('.truncArt').truncate({length:200})
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
                <div class="col-md-9 blog-box">

                    <c:forEach var="article" items="${articles.content}">
                        <!-- Start Post -->
                        <div class="blog-post image-post">
                            <!-- Post Thumb -->
                            <div class="post-head">
                                <a class="lightbox" title="This is an image title" href="images/blog-01.jpg">
                                    <div class="thumb-overlay"><i class="fa fa-arrows-alt"></i></div>
                                    <img alt="" src="images/blog-01.jpg">
                                </a>
                            </div>
                            <!-- Post Content -->
                            <div class="post-content">
                                <div class="post-type"><i class="fa fa-picture-o"></i></div>
                                <h2><a href="#">${article.title}</a></h2>
                                <ul class="post-meta">
                                    <li>By <a href="#">${article.author.firstName} ${article.author.lastName}</a></li>
                                    <li>${article.createdDate}</li>
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


                <!--Sidebar-->
                <div class="col-md-3 sidebar right-sidebar">

                    <!-- Search Widget -->
                    <div class="widget widget-search">
                        <form action="#">
                            <input type="search" placeholder="Enter Keywords..."/>
                            <button class="search-btn" type="submit"><i class="fa fa-search"></i></button>
                        </form>
                    </div>

                    <!-- Categories Widget -->
                    <div class="widget widget-categories">
                        <h4>Categories <span class="head-line"></span></h4>
                        <ul>
                            <li>
                                <a href="#">Brandign</a>
                            </li>
                            <li>
                                <a href="#">Design</a>
                            </li>
                            <li>
                                <a href="#">Development</a>
                            </li>
                            <li>
                                <a href="#">Graphic</a>
                            </li>
                        </ul>
                    </div>

                    <!-- Popular Posts widget -->
                    <div class="widget widget-popular-posts">
                        <h4>Popular Post <span class="head-line"></span></h4>
                        <ul>
                            <li>
                                <div class="widget-thumb">
                                    <a href="#"><img src="images/blog-mini-01.jpg" alt=""/></a>
                                </div>
                                <div class="widget-content">
                                    <h5><a href="#">How To Download The Google Fonts Catalog</a></h5>
                                    <span>Jul 29 2013</span>
                                </div>
                                <div class="clearfix"></div>
                            </li>
                            <li>
                                <div class="widget-thumb">
                                    <a href="#"><img src="images/blog-mini-02.jpg" alt=""/></a>
                                </div>
                                <div class="widget-content">
                                    <h5><a href="#">How To Download The Google Fonts Catalog</a></h5>
                                    <span>Jul 29 2013</span>
                                </div>
                                <div class="clearfix"></div>
                            </li>
                            <li>
                                <div class="widget-thumb">
                                    <a href="#"><img src="images/blog-mini-03.jpg" alt=""/></a>
                                </div>
                                <div class="widget-content">
                                    <h5><a href="#">How To Download The Google Fonts Catalog</a></h5>
                                    <span>Jul 29 2013</span>
                                </div>
                                <div class="clearfix"></div>
                            </li>
                        </ul>
                    </div>

                    <!-- Video Widget -->
                    <div class="widget">
                        <h4>Video <span class="head-line"></span></h4>

                        <div>
                            <iframe src="http://player.vimeo.com/video/63322694?byline=0&amp;portrait=0&amp;badge=0"
                                    width="800" height="450"></iframe>
                        </div>
                    </div>

                    <!-- Tags Widget -->
                    <div class="widget widget-tags">
                        <h4>Tags <span class="head-line"></span></h4>

                        <div class="tagcloud">
                            <a href="#">Portfolio</a>
                            <a href="#">Theme</a>
                            <a href="#">Mobile</a>
                            <a href="#">Design</a>
                            <a href="#">Wordpress</a>
                            <a href="#">Jquery</a>
                            <a href="#">CSS</a>
                            <a href="#">Modern</a>
                            <a href="#">Theme</a>
                            <a href="#">Icons</a>
                            <a href="#">Google</a>
                        </div>
                    </div>

                </div>
                <!--End sidebar-->


            </div>
        </div>
    </div>
    <!-- End Content -->

    <user:footer/>

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