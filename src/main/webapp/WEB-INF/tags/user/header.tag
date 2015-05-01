<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Start Header Section -->
        <div class="hidden-header"></div>
        <header class="clearfix">

            <!-- Start Top Bar -->
            <div class="top-bar">
                <div class="container">
                    <div class="row">
                        <div class="col-md-7">
                            <!-- Start Contact Info -->
                            <ul class="contact-details">
                                <li><a href="https://goo.gl/maps/fTo45" target="_blank"><i class="fa fa-map-marker"></i> Cherni Vrah 100, Sofia, Bulgaria</a>
                                </li>
                                <li><a href="mailto:conference@jprime.io"><i class="fa fa-envelope-o"></i> conference@jprime.io</a>
                                </li>
                                <li><a href="#"><i class="fa fa-phone"></i> +359 887 749 325</a>
                                </li>
                            </ul>
                            <!-- End Contact Info -->
                        </div><!-- .col-md-6 -->
                        <div class="col-md-5">
                            <!-- Start Social Links -->
                            <ul class="social-list">
                                <li>
                                    <a class="twitter itl-tooltip" data-placement="bottom" title="Twitter" href="https://twitter.com/jPrimeConf"><i class="fa fa-twitter"></i></a>
                                </li>
                                <li>
                                    <a class="facebook itl-tooltip" data-placement="bottom" title="Facebook" href="https://www.facebook.com/jprimeConf"><i class="fa fa-facebook"></i></a>
                                </li>
                                <li>
                                    <a class="google itl-tooltip" data-placement="bottom" title="Google" href="https://plus.google.com/u/2/108212814919981345652/posts"><i class="fa fa-google"></i></a>
                                </li>
                                <li>
                                    <a class="linkedin itl-tooltip" data-placement="bottom" title="Linkedin" href="https://www.linkedin.com/groups/jPrime-8290432"><i class="fa fa-linkedin"></i></a>
                                </li>
                            </ul>
                            <!-- End Social Links -->
                        </div><!-- .col-md-6 -->
                    </div><!-- .row -->
                </div><!-- .container -->
            </div><!-- .top-bar -->
            <!-- End Top Bar -->


            <!-- Start  Logo & Naviagtion  -->
            <div class="navbar navbar-default navbar-top">
                <div class="container">
                    <div class="navbar-header">
                        <!-- Stat Toggle Nav Link For Mobiles -->
                        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                            <i class="fa fa-bars"></i>
                        </button>
                        <!-- End Toggle Nav Link For Mobiles -->
                        <a class="navbar-brand" href="/">
                            <img alt="" src="images/jprime-small.png">
                        </a>
                    </div>
                    <div class="navbar-collapse collapse">
                        <%--
                        <!-- Stat Search -->
                        <div class="search-side">
                            <a href="#" class="show-search"><i class="fa fa-search"></i></a>
                            <div class="search-form">
                                <form autocomplete="off" role="search" method="get" class="searchform" action="#">
                                    <input type="text" value="" name="s" id="s" placeholder="Search the site...">
                                </form>
                            </div>
                        </div>
                        <!-- End Search -->
                         --%>
                        <!-- Start Navigation List -->
                        <ul class="nav navbar-nav navbar-right">
                            <li>
                                <a <c:if test='${"/index.jsp" eq pageContext.request.requestURI}'> class="active"</c:if> href="/">Home </a>
                                <%--<ul class="dropdown">
                                    <li><a class="active" href="/">Home Main Version</a>
                                    </li>
                                    <li><a href="index-01.html">Home Version 1</a>
                                    </li>
                                    <li><a href="index-02.html">Home Version 2</a>
                                    </li>
                                    <li><a href="index-03.html">Home Version 3</a>
                                    </li>
                                    <li><a href="index-04.html">Home Version 4</a>
                                    </li>
                                    <li><a href="index-05.html">Home Version 5</a>
                                    </li>
                                    <li><a href="index-06.html">Home Version 6</a>
                                    </li>
                                    <li><a href="index-07.html">Home Version 7</a>
                                    </li>
                                </ul> --%>
                            </li>
                            <li>
                                <a <c:if test='${"/tickets-epay-register.jsp" eq pageContext.request.requestURI}'> class="active"</c:if> href="/tickets/epay">Register</a>
                            </li>
                            <li>
                                <a <c:if test='${"/team.jsp" eq pageContext.request.requestURI}'> class="active"</c:if> href="/team">The Team</a>
                            </li>
                            <li>
                                <a <c:if test='${"/venue.jsp" eq pageContext.request.requestURI}'> class="active"</c:if> href="/venue">Venue</a>
                            </li>
                            <c:forEach var="tag" items="${tags}">
                                <li>
                                    <a href="/nav/${tag.name}"><c:out value="${tag.name}"/></a>
                                </li>
                            </c:forEach>
                            <%--
                            <li>
                                <a href="#">Shortcodes</a>
                                <ul class="dropdown">
                                    <li><a href="tabs.html">Tabs</a>
                                    </li>
                                    <li><a href="buttons.html">Buttons</a>
                                    </li>
                                    <li><a href="action-box.html">Action Box</a>
                                    </li>
                                    <li><a href="testimonials.html">Testimonials</a>
                                    </li>
                                    <li><a href="latest-posts.html">Latest Posts</a>
                                    </li>
                                    <li><a href="latest-projects.html">Latest Projects</a>
                                    </li>
                                    <li><a href="pricing.html">Pricing Tables</a>
                                    </li>
                                    <li><a href="animated-graphs.html">Animated Graphs</a>
                                    </li>
                                    <li><a href="accordion-toggles.html">Accordion & Toggles</a>
                                    </li>
                                </ul>
                            </li>
                            <li>
                                <a href="portfolio-3.html">Portfolio</a>
                                <ul class="dropdown">
                                    <li><a href="portfolio-2.html">2 Columns</a>
                                    </li>
                                    <li><a href="portfolio-3.html">3 Columns</a>
                                    </li>
                                    <li><a href="portfolio-4.html">4 Columns</a>
                                    </li>
                                    <li><a href="single-project.html">Single Project</a>
                                    </li>
                                </ul>
                            </li>
                            <li>
                                <a href="nav">Blog</a>
                                <ul class="dropdown">
                                    <li><a href="nav">Blog - right Sidebar</a>
                                    </li>
                                    <li><a href="blog-left-sidebar.html">Blog - Left Sidebar</a>
                                    </li>
                                    <li><a href="single-post.html">Blog Single Post</a>
                                    </li>
                                </ul>
                            </li> 
                            <li><a href="contact.html">Contact</a>
                            </li>
                            --%>
                        </ul>
                        <!-- End Navigation List -->
                    </div>
                </div>
            </div>
            <!-- End Header Logo & Naviagtion -->

        </header>
        <!-- End Header Section -->