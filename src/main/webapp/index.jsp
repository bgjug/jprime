<%@ page trimDirectiveWhitespaces="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
                    <img class="img-responsive" src="images/slider/artwork.jpg" alt="slider">

                    <div class="slider-content">
                        <div class="col-md-12 text-center">
                            <h2 class="animated2 white" style="text-shadow: 3px 3px black;">
                                <span><strong><!--Java Prime Time in Sofia--></strong></span>
                            </h2>

                            <h3 class="animated3 white" style="text-shadow: 2px 2px black;">
                                <span><!--29 and 30 May 2018--></span>
                            </h3>
                            <%--
                            <p class="animated4"><a href="#about" class="slider btn btn-primary">read more</a>
                            --%>
                        </div>
                    </div>
                </div>
                <!--/ Carousel item end -->
                <div class="item">
                    <img class="img-responsive" src="images/slider/index1.jpg" alt="slider">

                    <div class="slider-content">
                        <div class="col-md-12 text-center">
                            <h2 class="animated4 white" style="text-shadow: 3px 3px black;">
                                <span><strong>jPrime</strong> wants YOU </span>
                            </h2>

                            <h3 class="animated5 white" style="text-shadow: 2px 2px black;">
                                <span>2 full days</span>
                            </h3>

                            <p class="animated4"><a href="tickets/epay" class="slider btn btn-primary">Buy ticket</a>
                            </p>

                        </div>
                    </div>
                </div>
                <!--/ Carousel item end -->
                <div class="item">
                    <img class="img-responsive" src="images/slider/index3.jpg" alt="slider">

                    <div class="slider-content">
                        <div class="col-md-12 text-center">
                            <h2 class="animated7 white" style="text-shadow: 3px 3px black;">
<!--                                 <span>Become a <strong>Sponsor</strong></span> -->
									<span>Student <strong>tickets !</strong></span>
                            </h2>

                            <h3 class="animated8 white" style="text-shadow: 2px 2px black;">
                                <span>Now available</span>
                            </h3>

                            <div class="">
                                <a class="animated4 slider btn btn-primary btn-min-block" href="#sponsors">Get Ticket</a>
                                <a class="animated4 slider btn btn-default btn-min-block"
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
                    <h1><strong>The conference is organized by</strong></h1>
                </div>
                <!-- End Big Heading -->

                <!--Start Clients Carousel-->
                <div class="our-clients">
                    <div class="clients-carousel custom-carousel touch-carousel navigation-3" data-appeared-items="6"
                         data-navigation="true">

                        <!-- Client 1 -->
                        <div class="client-item item">
                            <a href="http://java-bg.org/"><img src="images/bg-jug.png" alt="Bulgarian JUG" style="width:85px;"/></a>
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
                            <a href="https://www.paysafe.com/"><img src="images/paysafe.png" alt="Paysafe"/></a>
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


	<%--
	<!-- Start Live Stream Section -->
    <div class="section full-width-portfolio" style="border-top:0; border-bottom:0; background:#fff;">

        <!-- Start Big Heading -->
        <div class="big-title text-center" data-animation="fadeInDown" data-animation-delay="01">
            <h1>jPrime<strong> livestream</strong></h1>
        </div>
        <!-- End Big Heading -->

        <p class="text-center">Track 1 (Beta)</p>
        <p class="text-center">FIX: since the stream is plain http, it won't work in chrome, <a target="_blank" href="http://i.cdn.bg/live/0zI7XuE2Dr">open in new tab</a></p>

        <div class="container" style="text-align: center">
        	 <iframe title="" width="720" height="405" src="http://i.cdn.bg/live/0zI7XuE2Dr" frameborder="0" mozallowfullscreen webkitallowfullscreen allowfullscreen></iframe>
        </div>

        <p class="text-center">Track 2 (Alpha)</p>
        <p class="text-center">FIX: since the stream is plain http, it won't work in chrome, <a target="_blank" href="http://i.cdn.bg/live/wC7nBgrPX1">open in new tab</a></p>

        <div class="container" style="text-align: center">
        	 <iframe title="" width="720" height="405" src="http://i.cdn.bg/live/wC7nBgrPX1" frameborder="0" mozallowfullscreen webkitallowfullscreen allowfullscreen></iframe>
        </div>

    </div>
    <!-- End Live Stream Section -->
     --%>

    <!-- Start Portfolio Section -->
    <div class="section full-width-portfolio" style="border-top:0; border-bottom:0; background:#fff;"  id="about">

        <!-- Start Big Heading -->
        <div class="big-title text-center" data-animation="fadeInDown" data-animation-delay="01">
            <h1>About <strong>jPrime</strong></h1>
        </div>
        <!-- End Big Heading -->

        <p class="text-center">jPrime is a conference with talks on Java, various languages on the JVM, mobile, web and best practices.
            <br/> Its forth edition will be held on <strong>29th and 30th May 2018</strong> in Sofia Tech Park.
            It's run by the Bulgarian Java User Group and backed by the biggest companies in the city.
            <br/><br/>jPrime features a combination of great international speakers along with the best presenters from
            Bulgaria and the Balkans. <br/>It is divided in two tracks and provides great opportunities for learning,
            hacking, networking and fun.</p>
<!--
        <div class="container">

            <!-- Start Big Heading --/>
            <div class="big-title text-center" data-animation="fadeInDown" data-animation-delay="01">
                <h1>We are done for this year! <strong>See you in 2018!</strong></h1>
            </div>
        </div>
-->
    </div>
    <!-- End Portfolio Section -->

    <!-- Start Team Member Section -->
    <a name="speakers"></a>
    <div class="section" style="background:#fff;">
        <div class="container">

            <!-- Start Big Heading -->
            <div class="big-title text-center" data-animation="fadeInDown" data-animation-delay="01">
                <h1>Our <strong>Speakers:</strong></h1>
            </div>
            <!-- End Big Heading -->

            <!-- Start Team Members -->

            <div class="row">
                <c:forEach var="speaker" items="${acceptedSpeakers}">
                    <div class="col-md-3 col-sm-6 col-xs-12">
                        <a href="/speaker/${speaker.id}"><div class="team-member modern">
                            <!-- Memebr Photo, Name & Position -->
                            <div class="member-photo">
                                <img alt="" src="/image/speaker/${speaker.id}" height="365" width="280"/>

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

    <!-- Start Pricing Table for Sponsors Section -->
    <div class=" section pricing-section" id="sponsors">
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <!-- Start Big Heading -->
                    <div class="big-title text-center">
                        <h1>Become a <strong>Sponsor!</strong></h1>
                    </div>
                    <!-- End Big Heading -->
                </div>
            </div>

            <div class="row pricing-tables">

                <div class="col-md-3 col-sm-3 col-xs-12" style="float: none; display: inline-block">
                    <div class="pricing-table">
                        <div class="plan-name">
                            <h3>Platinum</h3>
                        </div>
                        <div class="plan-price">
                            <div class="price-value">5000<span>.00</span> BGN</div>
                        </div>
                        <div class="plan-list">
                            <ul>
                                <li><strong>9</strong> free passes</li>
                                <li><strong>15%</strong> discount of passes</li>
                                <li><strong>Booth</strong> in the conference hall</li>
                                <li><strong>Presence</strong> on the stage for the raffle</li>
                                <li><strong>2</strong> banners in the conference rooms</li>
                                <li><strong>1</strong> invite for the "thank you" dinner</li>
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
                            <div class="price-value">4000<span>.00</span> BGN</div>
                        </div>
                        <div class="plan-list">
                            <ul>
                                <li><strong>6</strong> free passes</li>
                                <li><strong>10%</strong> discount of passes</li>
                                <li><strong>Booth</strong> in the conference hall</li>
                                <li><strong>2</strong> banners in the conference rooms</li>
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
                            <div class="price-value">1500<span>.00</span> BGN</div>
                        </div>
                        <div class="plan-list">
                            <ul>
                                <li><strong>3</strong> free passes</li>
                                <li><strong>10%</strong> discount of passes</li>
                                <li><strong>2</strong> banners in the conference rooms</li>
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
    <!-- End Pricing Table for Sponsors Section -->

    <!-- Start Pricing Table for Sponsors Section -->
    <div class=" section pricing-section" id="sponsors">
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <!-- Start Big Heading -->
                    <div class="big-title text-center">
                        <h1>Buy <strong>Ticket!</strong></h1>
                    </div>
                    <!-- End Big Heading -->
                </div>
            </div>

            <div class="row pricing-tables">

                <div class="col-md-3 col-sm-3 col-xs-12" style="float: none; display: inline-block; opacity: 0.5; margin-bottom: -200px;">
                    <div class="pricing-table">
                        <div class="plan-name">
                            <h3>Early bird ticket</h3>
                            <span>(Until 15th of Match)</span>
                        </div>
                        <div class="plan-price">
                            <div class="price-value">140<span>.00</span> BGN</div>
                        </div>
                        <div class="plan-list">
                            <ul>
                                <li><strong>2</strong> days conference</li>
                                <li><strong>28+</strong> awesome talks!</li>
                                <li><strong>Hands-on</strong> labs!</li>
                                <li><strong>Lunch</strong> box</li>
                                <li><strong>A bag</strong> full of goodies</li>
                                <li><strong>Free T-shirt!</strong></li>
                                <li><strong>Raffle !</strong></li>
                                <li><strong>Beers !</strong></li>
                                <li><strong>Coffee</strong> and soft drinks</li>
                            </ul>
                        </div>
                        <div class="plan-signup">
                            <a href="#"
                               class="btn-system btn-small">Over</a>

                        </div>
                    </div>
                </div>

                <div class="col-md-3 col-sm-3 col-xs-12" style="float: none; display: inline-block">
                    <div class="pricing-table">
                        <div class="plan-name">
                            <h3>Regular ticket</h3>
                            <span>(After 15th of Match)</span>
                        </div>
                        <div class="plan-price">
                            <div class="price-value">200<span>.00</span> BGN</div>
                        </div>
                        <div class="plan-list">
                            <ul>
                                <li><strong>2</strong> days conference</li>
                                <li><strong>28+</strong> awesome talks!</li>
                                <li><strong>Hands-on</strong> labs!</li>
                                <li><strong>Lunch</strong> box</li>
                                <li><strong>A bag</strong> full of goodies</li>
                                <li><strong>Free T-shirt!</strong></li>
                                <li><strong>Raffle !</strong></li>
                                <li><strong>Beers !</strong></li>
                                <li><strong>Coffee</strong> and soft drinks</li>
                            </ul>
                        </div>
                        <div class="plan-signup">
                            <a href="tickets/epay"
                               class="btn-system btn-small">Get It Now</a>

                        </div>
                    </div>
                </div>

                <div class="col-md-3 col-sm-3 col-xs-12" style="float: none; display: inline-block">
                    <div class="pricing-table">
                        <div class="plan-name">
                            <h3>Student ticket</h3>
                            <span>(Available at all times)</span>
                        </div>
                        <div class="plan-price">
                            <div class="price-value">100<span>.00</span> BGN</div>
                        </div>
                        <div class="plan-list">
                            <ul>
                                <li><strong>2</strong> days conference</li>
                                <li><strong>28+</strong> awesome talks!</li>
                                <li><strong>Hands-on</strong> labs!</li>
                                <li><strong>Lunch</strong> box</li>
                                <li><strong>A bag</strong> full of goodies</li>
                                <li><strong>Free T-shirt!</strong></li>
                                <li><strong>Raffle !</strong></li>
                                <li><strong>Beers !</strong></li>
                                <li><strong>Coffee</strong> and soft drinks</li>
                            </ul>
                        </div>
                        <div class="plan-signup">
                            <a href="tickets/epay"
                               class="btn-system btn-small">Get It Now</a>

                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
    <!-- End Pricing Table for Sponsors Section -->


    <!-- Start Sponsor Section -->

    <div class="partner">
        <div class="container">
            <div class="row">

                <!--Start Clients Carousel-->
                <div class="big-title text-center">
                    <h1><strong>Platinum</strong> Sponsors</h1>
                </div>

                <div class="our-clients">

                    <c:forEach var="sponsor" items="${platinumSponsors}">
                        <div class="client-item item">
                            <a href="${sponsor.companyWebsite}"><img src="/image/sponsor/${sponsor.id}"
                                                                     alt="${sponsor.companyName}"/></a>
                        </div>
                    </c:forEach>

                </div>

                <div class="big-title text-center">
                    <h1><strong>Gold</strong> Sponsors</h1>
                </div>

                <div class="our-clients text-center" style="text-align: center">

                    <c:forEach var="sponsor" items="${goldSponsors}">
                        <div class="client-item item" style="float: none; display: inline-block">
                            <a href="${sponsor.companyWebsite}"><img src="/image/sponsor/${sponsor.id}" alt="${sponsor.companyName}"/></a>
                        </div>
                    </c:forEach>

                </div>

                <div class="big-title text-center">
                    <h1><strong>Silver</strong> Sponsors</h1>
                </div>

                <div class="our-clients">

                    <c:forEach var="sponsor" items="${silverSponsors}">
                        <div class="client-item item">
                            <a href="${sponsor.companyWebsite}"><img src="/image/sponsor/${sponsor.id}"
                                                                     alt="${sponsor.companyName}"/></a>
                        </div>
                    </c:forEach>

                </div>
                <!-- End Clients Carousel -->
            </div><!-- .row -->
        </div><!-- .container -->
    </div>
    <!-- End Sponsor Section -->


    <!-- Start Media Partners Section -->
    <c:if test="${fn:length(partnerChunks) gt 0}">
	    <div class="partner">
	        <div class="container">
	            <div class="row">

	                <div class="big-title text-center" >
	                    <h1><strong>Community and Media</strong> Partners</h1>
	                </div>

	                <div class="our-clients">
	                     <c:forEach var="partners" items="${partnerChunks}">

                                <div class="clients-carousel custom-carousel touch-carousel navigation-${fn:length(partners) gt 3? '3' : fn:length(partners)}" data-appeared-items="${fn:length(partners)}"
                                             data-navigation="true">

                                            <c:forEach var="partner" items="${partners}">
                                                <div class="client-item item">
                                                    <a href="${partner.companyWebsite}"><img src="/image/partner/${partner.id}"
                                                                                             alt="${partner.companyName}"/></a>
                                                </div>
                                            </c:forEach>
                                </div>
    			       </c:forEach>

	                </div>
	                <!-- End Clients Carousel -->
	            </div><!-- .row -->
	        </div><!-- .container -->
	    </div>
    </c:if>
    <!-- End Media Partners Section -->

    <!-- Start Event Partners Section -->
    <c:if test="${fn:length(eventPartnerChunks) gt 0}">
        <div class="event-partner">
            <div class="container">
                <div class="row">

                    <div class="big-title text-center" >
                        <h1><strong>Event</strong> Partners</h1>
                    </div>

                    <div class="our-clients">
                        <c:forEach var="eventPartners" items="${eventPartnerChunks}">

                            <div class="clients-carousel custom-carousel touch-carousel navigation-${fn:length(eventPartners) gt 3? '3' : fn:length(eventPartners)}" data-appeared-items="${fn:length(eventPartners)}"
                                 data-navigation="true">

                                <c:forEach var="eventPartner" items="${eventPartners}">
                                    <div class="client-item item">
                                        <a href="${eventPartner.companyWebsite}"><img src="/image/partner/${eventPartner.id}"
                                                                                 alt="${eventPartner.companyName}"/></a>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:forEach>

                    </div>
                    <!-- End Clients Carousel -->
                </div><!-- .row -->
            </div><!-- .container -->
        </div>
    </c:if>
    <!-- End Event Partners Section -->


    <jsp:directive.include file="footer.jsp" />

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
