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
    <title>jPrime | Home</title>

    <!-- Define Charset -->
    <meta charset="utf-8">

    <!-- Responsive Metatag -->
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

    <!-- Page Description and Author -->

    <user:pageJavaScriptAndCss/>
</head>

<body>

<!-- Full Body Container -->
<div id="container">

    <user:header/>

    <!-- Start Home Page Slider -->
    <section id="home">
        <!-- Carousel -->
        <div id="main-slide" class="carousel slide" data-ride="carousel">

            <!-- Indicators -->
            <ol class="carousel-indicators">
                <li data-target="#main-slide" data-slide-to="0" class="active"></li>
                <li data-target="#main-slide" data-slide-to="1"></li>
                <li data-target="#main-slide" data-slide-to="2"></li>
            </ol>
            <!--/ Indicators end-->

            <!-- Carousel inner -->
            <div class="carousel-inner">
                <div class="item active">
                    <img class="img-responsive" src="images/slider/bg1.jpg" alt="slider">

                    <div class="slider-content">
                        <div class="col-md-12 text-center">
                            <h2 class="animated2 white" style="text-shadow: 3px 3px black;">
                                <span><strong>jPrime</strong></span>
                            </h2>

                            <h3 class="animated3 white" style="text-shadow: 2px 2px black;">
                                <span>Java prime time in Sofia</span>
                            </h3>

                            <p class="animated4"><a href="/cfp" class="slider btn btn-primary">Submit a talk</a>
                        </div>
                    </div>
                </div>
                <!--/ Carousel item end -->
                <div class="item">
                    <img class="img-responsive" src="images/slider/bg2.jpg" alt="slider">

                    <div class="slider-content">
                        <div class="col-md-12 text-center">
                            <h2 class="animated4 white" style="text-shadow: 3px 3px black;">
                                <span><strong>jPrime</strong> wants YOU </span>
                            </h2>

                            <h3 class="animated5 white" style="text-shadow: 2px 2px black;">
                                <span>Register now</span>
                            </h3>

                            <p class="animated6"><a href="/tickets" class="slider btn btn-primary">Register</a>
                            </p>
                        </div>
                    </div>
                </div>
                <!--/ Carousel item end -->
                <div class="item">
                    <img class="img-responsive" src="images/slider/bg3.jpg" alt="slider">

                    <div class="slider-content">
                        <div class="col-md-12 text-center">
                            <h2 class="animated7 white" style="text-shadow: 3px 3px black;">
                                <span>Become a <strong>Sponsor</strong></span>
                            </h2>

                            <h3 class="animated8 white" style="text-shadow: 2px 2px black;">
                                <span>WHAT ARE YOU WAITING FOR</span>
                            </h3>

                            <div class="">
                                <a class="animated4 slider btn btn-primary btn-min-block" href="#sponsors">Become
                                    Sponsor</a><a class="animated4 slider btn btn-default btn-min-block"
                                                  href="#sponsors">Learn More</a>
                            </div>
                        </div>
                    </div>
                </div>
                <!--/ Carousel item end -->
            </div>
            <!-- Carousel inner end-->

            <!-- Controls -->
            <a class="left carousel-control" href="#main-slide" data-slide="prev">
                <span><i class="fa fa-angle-left"></i></span>
            </a>
            <a class="right carousel-control" href="#main-slide" data-slide="next">
                <span><i class="fa fa-angle-right"></i></span>
            </a>
        </div>
        <!-- /carousel -->
    </section>
    <!-- End Home Page Slider -->

    <!-- Start Client/Partner Section -->
    <div class="section">
        <div class="container">
            <div class="row">

                <!-- Start Big Heading -->
                <div class="big-title text-center">
                    <h1><strong>This conference is brought to you by</strong></h1>
                </div>
                <!-- End Big Heading -->

                <!--Start Clients Carousel-->
                <div class="our-clients">
                    <div class="clients-carousel custom-carousel touch-carousel navigation-3" data-appeared-items="6"
                         data-navigation="true">

                        <!-- Client 1 -->
                        <div class="client-item item">
                            <a href="http://java-bg.org/"><img src="images/bg-jug.png" alt="Bulgarian JUG"/></a>
                        </div>

                        <!-- Client 2 -->
                        <div class="client-item item">
                            <a href="http://www.vmware.com"><img src="images/vmware.png" alt="VMware"/></a>
                        </div>

                        <!-- Client 3 -->
                        <div class="client-item item">
                            <a href="http://www.softwareag.com"><img src="images/softwareag.png" alt="Software AG"/></a>
                        </div>

                        <!-- Client 4 -->
                        <div class="client-item item">
                            <a href="http://www.sap.com/bulgaria"><img src="images/sap.png" alt="SAP"/></a>
                        </div>

                        <!-- Client 5 -->
                        <div class="client-item item">
                            <a href="http://www.experian.bg"><img src="images/experian.png" alt="Experian"/></a>
                        </div>

                        <!-- Client 6 -->
                        <div class="client-item item">
                            <a href="http://www.epam.com"><img src="images/epam.png" alt="EPAM"/></a>
                        </div>

                    </div>
                </div>
                <!-- End Clients Carousel -->
            </div>
            <!-- .row -->
        </div>
        <!-- .container -->
    </div>


    <%--
    <!-- Start Purchase Section -->
    <div class="section purchase">
        <div class="container">

            <!-- Start Video Section Content -->
            <div class="section-video-content text-center">

                <!-- Start Animations Text -->
                <h1 class="fittext wite-text uppercase tlt">
                  <span class="texts">
                    <span>Modern</span>
                    <span>Clean</span>
                    <span>Awesome</span>
                    <span>Cool</span>
                    <span>Great</span>
                  </span>
                    Margo Template is Ready for <br/>Business, Agency <strong>or</strong> Creative Portfolios
                </h1>
                <!-- End Animations Text -->


                <!-- Start Buttons -->
                <a href="#" class="btn-system btn-large border-btn btn-wite"><i class="fa fa-tasks"></i> Check Out Features</a>
                <a href="#" class="btn-system btn-large btn-wite"><i class="fa fa-download"></i> Purchase This Now</a>

            </div>
            <!-- End Section Content -->

        </div><!-- .container -->
    </div>
    <!-- End Purchase Section -->
     --%>


    <!-- Start Portfolio Section -->
    <div class="section full-width-portfolio" style="border-top:0; border-bottom:0; background:#fff;">

        <!-- Start Big Heading -->
        <div class="big-title text-center" data-animation="fadeInDown" data-animation-delay="01">
            <h1>About <strong>jPrime</strong></h1>
        </div>
        <!-- End Big Heading -->

        <p class="text-center">jPrime is a one day conference with talks on Java, various languages on the JVM, mobile,
            web and best practices. <br/> It is held on <strong>27th May 2015</strong> in Sofia Event Center. It's run
            by the Bulgarian Java User Group and backed by the biggest companies in the city.
            <br/><br/>jPrime features a combination of great international speakers along with the best presenters from
            Bulgaria and the Balkans. <br/>It is divided in two tracks and provides great opportunities for learning,
            hacking, networking and fun.</p>

        <center><iframe src="https://www.google.com/maps/embed?pb=!1m0!3m2!1sru!2sbg!4v1429266821187!6m8!1m7!1sipo8Sl0qHi8AAAQfDR2bcQ!2m2!1d42.657808!2d23.315222!3f307.9207157147706!4f-10.178049410940872!5f0.7820865974627469" width="80%" height="450" frameborder="0" style="border:0"></iframe></center>
    	<center><iframe src="https://www.google.com/maps/embed?pb=!1m14!1m8!1m3!1d11736.674105662074!2d23.314898!3d42.657784!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x0%3A0x968e1b14857aa12d!2z0J_QkNCg0JDQlNCQ0JnQoSDQptCV0J3QotCq0KA!5e0!3m2!1sbg!2sbg!4v1429266435259&style=saturation:-75|gamma:1.07|hue:0xffe500" width="80%" height="500" frameborder="0" style="border:0"></iframe></center>
    </div>
    <!-- End Portfolio Section -->
    
    <!-- Start Team Member Section -->
    <div class="section" style="background:#fff;">
        <div class="container">

            <!-- Start Big Heading -->
            <div class="big-title text-center" data-animation="fadeInDown" data-animation-delay="01">
                <h1>Some of our <strong>Speakers</strong></h1>
            </div>
            <!-- End Big Heading -->

            <!-- Start Team Members -->

            <div class="row">
                <c:forEach var="speaker" items="${featuredSpeakers}">
                    <div class="col-md-3 col-sm-6 col-xs-12" data-animation="fadeIn" data-animation-delay="03" >
                    	<c:url var="speakerUrl" value="/nav/article">
                    		<c:param name="title" value="${speaker.firstName} ${speaker.lastName}"/>
                    	</c:url>
                        <a href="${speakerUrl}"><div class="team-member modern">
                            <!-- Memebr Photo, Name & Position -->
                            <div class="member-photo">
                                <img alt="" src="/image/speaker/${speaker.id}"/>

                                <div class="member-name"><c:out value="${speaker.firstName}"/>&nbsp;<c:out
                                        value="${speaker.lastName}"/><span><c:out
                                        value="${speaker.headline}"/></span>
                                </div>
                            </div>
                            <div class="member-socail" style="text-align: left">
                                <a class="twitter" href="http://twitter.com/${speaker.twitter}"><i
                                        class="fa fa-twitter"></i></a>
                            </div>
                        </div>
                        </a>
                    </div>
                </c:forEach>
            </div>
        </div>
        <!-- .container -->
    </div>
    <!-- End Team Member Section -->


    <!-- Start Pricing Table Section -->
    <div class=" section pricing-section" id="sponsors">
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <!-- Start Big Heading -->
                    <div class="big-title text-center">
                        <h1>Become a <strong>Sponsor!</strong></h1>
                    </div>
                    <!-- End Big Heading -->

                    <!-- Text -->
                    <p class="text-center">We have 3 different packages for sponsors.</p>
                </div>
            </div>

            <div class="row pricing-tables">

                <div class="col-md-3 col-sm-3 col-xs-12" style="float: none; display: inline-block">
                    <div class="pricing-table">
                        <div class="plan-name">
                            <h3>Platinum</h3>
                        </div>
                        <div class="plan-price">
                            <div class="price-value">1500<span>.00</span> EUR</div>
                        </div>
                        <div class="plan-list">
                            <ul>
                                <li><strong>9</strong> free passes</li>
                                <li><strong>20%</strong> discount of passes</li>
                                <li><strong>Booth</strong> at the conference hall</li>
                                <li><strong>Free</strong> raffle sponsorship</li>
                                <li><strong>2</strong> banners in the conference rooms</li>
                                <li><strong>1</strong> invite for the speaker dinner</li>
                            </ul>
                        </div>
                        <div class="plan-signup">
                            <a href="mailto:conference@jprime.io?subject=platinum sponsorship"
                               class="btn-system btn-small">Sign Up Now</a>
                        </div>
                    </div>
                </div>

                <div class="col-md-3 col-sm-3 col-xs-12" style="float: none; display: inline-block">
                    <div class="pricing-table">
                        <div class="plan-name">
                            <h3>Gold</h3>
                        </div>
                        <div class="plan-price">
                            <div class="price-value">1000<span>.00</span> EUR</div>
                        </div>
                        <div class="plan-list">
                            <ul>
                                <li><strong>6</strong> free passes</li>
                                <li><strong>10%</strong> discount of passes</li>
                                <li><strong>Booth</strong> at the conference hall</li>
                                <li><strong>1</strong> banner in the conference rooms</li>
                            </ul>
                        </div>
                        <div class="plan-signup">
                            <a href="mailto:conference@jprime.io?subject=gold sponsorship"
                               class="btn-system btn-small">Sign
                                Up Now</a>
                        </div>
                    </div>
                </div>


                <div class="col-md-3 col-sm-3 col-xs-12" style="float: none; display: inline-block">
                    <div class="pricing-table">
                        <div class="plan-name">
                            <h3>Silver</h3>
                        </div>
                        <div class="plan-price">
                            <div class="price-value">500<span>.00</span> EUR</div>
                        </div>
                        <div class="plan-list">
                            <ul>
                                <li><strong>3</strong> free passes</li>
                                <li><strong>10%</strong> discount of passes</li>
                                <li><strong>1</strong> banner in the conference rooms</li>
                            </ul>
                        </div>
                        <div class="plan-signup">
                            <a href="mailto:conference@jprime.io?subject=silver sponsorship"
                               class="btn-system btn-small">Sign Up Now</a>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
    <!-- End Pricing Table Section -->

    <%--

            <!-- Start Client/Partner Section -->
            <div class="partner">
            <div class="container">
                <div class="row">

            <!--Start Clients Carousel-->
            <div class="big-title text-center">
                <h1><strong>Platinum</strong> Sponsors</h1>
            </div>

            <div class="our-clients">
                <div class="clients-carousel custom-carousel touch-carousel navigation-3" data-appeared-items="5" data-navigation="true">

                    <c:forEach var="sponsor" items="${platinumSponsors}">
                        <div class="client-item item">
                            <a href="${sponsor.companyWebsite}"><img src="/image/sponsor/${sponsor.id}" alt="${sponsor.companyName}" /></a>
                        </div>
                    </c:forEach>

                </div>
            </div>

            <div class="big-title text-center">
                <h1><strong>Gold</strong> Sponsors</h1>
            </div>

            <div class="our-clients">
                <div class="clients-carousel custom-carousel touch-carousel navigation-3" data-appeared-items="5" data-navigation="true">

                    <c:forEach var="sponsor" items="${goldSponsors}">
                        <div class="client-item item">
                            <a href="${sponsor.companyWebsite}"><img src="/image/sponsor/${sponsor.id}" alt="${sponsor.companyName}" /></a>
                        </div>
                    </c:forEach>

                </div>
            </div>
            <div class="big-title text-center">
                <h1><strong>Silver</strong> Sponsors</h1>
            </div>

            <div class="our-clients">
                <div class="clients-carousel custom-carousel touch-carousel navigation-3" data-appeared-items="5" data-navigation="true">

                    <c:forEach var="sponsor" items="${silverSponsors}">
                        <div class="client-item item">
                            <a href="${sponsor.companyWebsite}"><img src="/image/sponsor/${sponsor.id}" alt="${sponsor.companyName}"/></a>
                        </div>
                    </c:forEach>

                </div>
            </div>
            <!-- End Clients Carousel -->
                </div><!-- .row -->
            </div><!-- .container -->
            </div>
            <!-- End Client/Partner Section -->
    --%>

    <user:footer/>

</div>
<!-- End Full Body Container -->

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
