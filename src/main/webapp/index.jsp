<%@ page trimDirectiveWhitespaces="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>

<!doctype html>
<html lang="en">

<head>

    <link rel="manifest" href="/manifest.json">
    <script>
        if ('serviceWorker' in navigator) {
            window.addEventListener('load', function () {
                navigator.serviceWorker.register('/sw.js', {scope: '/'});
            })
        }
    </script>
    <!-- Basic -->
    <title>jPrime ${conference_dates} | Home</title>

    <!-- Define Charset -->
    <meta charset="utf-8">

    <!-- Responsive Metatag -->
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <%--    <jsp:directive.include file="theme-colors.jsp" />--%>

    <!-- Page Description and Author -->

    <user:pageJavaScriptAndCss/>

</head>

<body>

<user:header/>

<!-- Main Carousel Section Start -->
<div id="main-slide" class="carousel slide" data-bs-ride="carousel" data-bs-interval="5000">
    <div class="carousel-indicators">
        <button type="button" data-bs-target="#main-slide" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1"></button>
        <button type="button" data-bs-target="#main-slide" data-bs-slide-to="1" aria-label="Slide 2"></button>
        <button type="button" data-bs-target="#main-slide" data-bs-slide-to="2" aria-label="Slide 3"></button>
    </div>
    <div class="carousel-inner">
        <div class="carousel-item active">
            <img class="d-block w-100" src="images/slider/index2026_3.jpg" alt="First slide">
            <div class="carousel-caption d-md-block">
                <c:if test="${jprime_year != null}">
                    <h1 class="wow fadeInDown heading" data-wow-delay=".4s">jPrime ${jprime_year}</h1>
                    <p class="fadeInUp wow carousel-jprime" data-wow-delay=".6s">The conference will be held
                        <br/>on ${conference_dates} in Sofia Tech Park</p>
                    <span class="wow fadeInUp carousel-jprime" data-wow-delay=".6s">${conference_dates}<br/>in Sofia Tech Park</span>
                </c:if>
<%--                <a href="#" class="fadeInLeft wow btn btn-common" data-wow-delay=".6s">Get Ticket</a>--%>
                <!-- <a href="#" class="fadeInRight wow btn btn-border" data-wow-delay=".6s">Contact</a> -->
            </div>
        </div>
<%--        <div class="carousel-item">--%>
<%--            <img class="d-block w-100" src="images/slider/index2.jpg" alt="First slide">--%>
<%--            <div class="carousel-caption d-md-block">--%>
<%--                 <c:if test="${!cfp_closed}">--%>
<%--                 <h1 class="wow bounceIn heading" data-wow-delay=".7s">CFP IS STILL OPEN!</h1>--%>
<%--                 </c:if>--%>
<%--            </div>--%>
<%--        </div>--%>
        <div class="carousel-item">
            <img class="d-block w-100" src="images/slider/index1.jpg" alt="Second slide">
            <div class="carousel-caption d-md-block">
                <c:if test="${!cfp_closed}">
                <h1 class="wow bounceIn heading" data-wow-delay=".7s">Submit your proposal before ${cfp_close_date}!</h1>
                </c:if>
            </div>
        </div>
        <div class="carousel-item">
            <img class="d-block w-100" src="images/slider/index3.jpg" alt="Third slide">
            <div class="carousel-caption d-md-block">
                <c:if test="${!ticket_sales_open}">
                    <h1 class="wow fadeInUp heading" data-wow-delay=".6s">Book Your Seat Now!</h1>
                    <a href="#tickets" class="fadeInUp wow btn btn-common" data-wow-delay=".8s">BUY a ticket</a>
                </c:if>
            </div>
        </div>
    </div>
    <button class="carousel-control-prev" type="button" data-bs-target="#main-slide" data-bs-slide="prev">
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Previous</span>
    </button>
    <button class="carousel-control-next" type="button" data-bs-target="#main-slide" data-bs-slide="next">
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Next</span>
    </button>
</div>
<!-- Main Carousel Section End -->

<!-- Coundown Section Start -->
<%-- Hiding the countdown timer for now
<section class="countdown-timer section-padding">
    <div class="container">
        <div class="row text-center">
            <div class="col-md-12 col-sm-12 col-xs-12">
                <div class="heading-count">
                    <h2>Where Java Developers Meet</h2>
                    <h4 class="location wow bounce" data-wow-delay="0.2s"><span><i class="icon-location-pin"></i> Sofia Tech Park, Sofia, Bulgaria</span></h4>
                </div>
            </div>
            <div class="col-md-12 col-sm-12 col-xs-12">
                <div class="row time-countdown justify-content-center">
                    <div id="clock" class="time-count"></div>
                </div>
                <a href="tickets/" class="btn btn-common">Book Your Ticket</a>
            </div>
        </div>
    </div>
</section>
--%>
<!-- Coundown Section End -->

<!-- About Section Start -->
<section id="about" class="section-padding">
    <div class="container">
        <div class="row">
            <div class="col-md-6 col-lg-6 col-xs-12">
                <h2 class="intro-title">About</h2>
                <h3 class="title-sub">The Conference</h3>
                <p class="intro-desc">jPrime is a conference with talks on Java, various languages on the JVM,
                    mobile, web and best practices.
                </p>
                <p>It's run by the Bulgarian Java User Group and backed by the biggest companies in the
                    country. </p>
                <p>jPrime features a combination of great international speakers along with the best
                    presenters from Bulgaria and the Balkans.
                    It is divided in two tracks and provides great opportunities for learning, hacking,
                    networking and fun.</p>
                <div class="mt-3 mb-3">
                    <a href="venue" class="btn btn-common">Read More</a>
                </div>
            </div>
            <div class="col-md-6 col-lg-6 col-xs-12">
                <img class="img-fluid" src="assets/img/about/about.jpg" alt="">
            </div>
        </div>
    </div>
</section>
<!-- About Section End -->

<!-- Coorganizers Section Start -->
<div id="coorganizers" class="section-padding">
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <h1 class="section-sub text-center">The conference is organized by</h1>
            </div>
        </div>
        <div class="row mb-30">
            <div class="col-md-2">
                <div class="sponsors-logo">
                    <a href="https://www.ibm.com"><img class="img-fluid" src="images/ibm.png" alt=""></a>
                </div>
            </div>
            <div class="col-md-2">
                <div class="sponsors-logo">
                    <a href="https://www.dxc.com"><img class="img-fluid" src="images/dxc.png"
                                                             alt=""></a>
                </div>
            </div>
            <div class="col-md-2">
                <div class="sponsors-logo">
                    <a href="https://www.sap.com/bulgaria"><img class="img-fluid" src="images/sap.png" alt=""></a>
                </div>
            </div>
            <div class="col-md-2">
                <div class="sponsors-logo">
                    <a href="https://www.experian.bg"><img class="img-fluid" src="images/experian.png" alt=""></a>
                </div>
            </div>
            <div class="col-md-2">
                <div class="sponsors-logo">
                    <a href="https://www.paysafe.com/"><img class="img-fluid" src="images/paysafe.png" alt=""></a>
                </div>
            </div>
            <div class="col-md-2">
                <div class="sponsors-logo">
                    <a href="https://www.ocadogroup.com/technology/development-centres/bulgaria"><img
                            class="img-fluid" src="images/ocado.png" alt=""></a>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-2" style="float:none;margin: 0 auto">
                <div class="sponsors-logo">
                    <a href="https://java-bg.org/"><img class="img-fluid" src="images/bg-jug.png"
                                                       style="width:85px;" alt=""></a>
                </div>
            </div>
        </div>
    </div>
</section>
<!-- Coorganizers Section End -->

<!-- Team Section Start -->
<section id="team" class="section-padding text-center">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <div class="section-title-header text-center">
                    <h1 class="section-title">Our Featured Speakers</h1>
                    <p>Confirmed speakers coming to jPrime !</p>
                </div>
            </div>
        </div>
        <div class="row">
            <c:forEach var="speaker" items="${featuredSpeakers}">
                <div class="col-sm-6 col-md-6 col-lg-3">
                    <!-- Team Item Starts -->
                    <div class="team-item text-center">
                        <div class="team-img">
                            <img class="img-fluid" src="/image/speaker/${speaker.id}" height="365" width="280"
                                 alt="">
                            <div class="team-overlay">
                                <div class="overlay-social-icon text-center">
                                    <ul class="social-icons">
                                        <c:if test="${speaker.twitter != null and speaker.twitter.length() > 0}">
                                            <li><a href="https://x.com/${speaker.twitter}"><i
                                                    class="fa-brands fa-x" aria-hidden="true"></i></a>
                                            </li>
                                        </c:if>
                                        <c:if test="${speaker.bsky != null and speaker.bsky.length() > 0}">
                                            <li><a href="${speaker.bsky}"><i class="fa-brands fa-bluesky"
                                                                             aria-hidden="true"></i></a></li>
                                        </c:if>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <div class="info-text">
                            <h3><a href="/speaker/${speaker.id}"><c:out
                                    value="${speaker.firstName}"/>&nbsp;<c:out
                                    value="${speaker.lastName}"/></a></h3>
                            <p><c:out value="${speaker.headline}"/></p>
                        </div>
                    </div>
                    <!-- Team Item Ends -->
                </div>
            </c:forEach>
        </div>
        <a href="/speakers" class="btn btn-common mt-30">All Speakers</a>
    </div>
</section>
<!-- Team Section End -->


<!-- Services Section Start -->
<section class="section-padding">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <div class="section-title-header text-center">
                    <h1 class="section-title">Why Attend</h1>
                    <p>The community driven Java conference in Bulgaria ! <br> Come and become part of the
                        community.</p>
                </div>
            </div>
        </div>
        <div class="row">
            <!-- Services item -->
            <div class="col-md-6 col-lg-4 col-xs-12">
                <div class="services-item s">
                    <div class="icon">
                        <i class="icon-eyeglass"></i>
                    </div>
                    <div class="services-content">
                        <h3><a href="/speakers">Meet Speakers</a></h3>
                        <p>One of the main goals to go to a conferences is not only to learn something new
                            from the sessions, but also to speak with the speakers. At jPrime conference there
                            is <b>no speaker room</b>, the speakers are outside with any and all of you, so
                            you can speak with them freely.</p>
                    </div>
                </div>
            </div>
            <!-- Services item -->
            <div class="col-md-6 col-lg-4 col-xs-12">
                <div class="services-item s">
                    <div class="icon">
                        <i class="icon-settings"></i>
                    </div>
                    <div class="services-content">
                        <h3><a href="/agenda">Tech Sessions</a></h3>
                        <p>We are trying to have the best and the best sessions possible. All sessions are
                            selected via CFP process and <b>voting</b>, there are <b>no sponsor sessions</b>,
                            there are 0 marketing and HR talks. This is not a bla bla conference, it's
                            practical and awesome conference!</p>
                    </div>
                </div>
            </div>
            <!-- Services item -->
            <div class="col-md-6 col-lg-4 col-xs-12">
                <div class="services-item s">
                    <div class="icon">
                        <i class="icon-globe"></i>
                    </div>
                    <div class="services-content">
                        <h3><a href="/agenda">Workshops</a></h3>
                        <p>Since 2017 we have also workshops. To participate a workshop just <b>bring your
                            laptop</b> with you, there is no extra cost, there is no pre-workshop signing,
                            just visit the workshop and participate in the training.</p>
                    </div>
                </div>
            </div>
            <!-- Services item -->
            <div class="col-md-6 col-lg-4 col-xs-12">
                <div class="services-item s">
                    <div class="icon">
                        <i class="icon-present"></i>
                    </div>
                    <div class="services-content">
                        <h3><a href="#tickets">Free Swags</a></h3>
                        <p>We always have a bag of goodies, some of which are quite good and this are not only
                            sponsor goodies but also <b>jPrime goodies</b> like shirts, notebooks, flash
                            drives, stickers, etc.</p>
                    </div>
                </div>
            </div>
            <!-- Services item -->
            <div class="col-md-6 col-lg-4 col-xs-12">
                <div class="services-item">
                    <div class="icon">
                        <i class="icon-bubbles"></i>
                    </div>
                    <div class="services-content">
                        <h3><a href="#">Networking</a></h3>
                        <p>Main part of the conference is the networking, speaking with each other, and share
                            experience and knowledge and ... well just have fun.</p>
                    </div>
                </div>
            </div>
            <!-- Services item -->
            <div class="col-md-6 col-lg-4 col-xs-12">
                <div class="services-item">
                    <div class="icon">
                        <i class="icon-graph"></i>
                    </div>
                    <div class="services-content">
                        <h3><a href="#">Tech Insights</a></h3>
                        <p>Get additional insights from the community, the speakers and or the sponsors during
                            our lunch breaks, coffee break .. or beer breaks ;)</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<!-- Services Section End -->

<!-- Counter Area Start-->
<section class="counter-section section-padding" data-stellar-background-ratio="0.5">
    <div class="container">
        <div class="row">
            <!-- Counter Item -->
            <div class="col-md-3 col-sm-6 work-counter-widget text-center">
                <div class="counter">
                    <div class="icon"><i class="icon-microphone"></i></div>
                    <p>Speakers</p>
                    <div class="counterUp">33</div>
                </div>
            </div>
            <!-- Counter Item -->
            <div class="col-md-3 col-sm-6 work-counter-widget text-center">
                <div class="counter">
                    <div class="icon"><i class="icon-bulb"></i></div>
                    <p>Participants</p>
                    <div class="counterUp">1200</div>
                </div>
            </div>
            <!-- Counter Item -->
            <div class="col-md-3 col-sm-6 work-counter-widget text-center">
                <div class="counter">
                    <div class="icon"><i class="icon-briefcase"></i></div>
                    <p>Sponsors</p>
                    <div class="counterUp">28</div>
                </div>
            </div>
            <!-- Counter Item -->
            <div class="col-md-3 col-sm-6 work-counter-widget text-center">
                <div class="counter">
                    <div class="icon"><i class="icon-cup"></i></div>
                    <p>Sessions</p>
                    <div class="counterUp">33</div>
                </div>
            </div>
        </div>
    </div>
</section>
<!-- Counter Area End-->

<!-- Ticket Pricing Area Start -->
<section id="pricing" class="section-padding">
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <h1 class="section-sub text-center">Book Your Ticket</h1>
            </div>
        </div>
        <div class="row" id="tickets">
            <div class="col-lg-4 col-md-4 col-xs-12 mb-3" style="<c:out value='${early_sold_out}'/>">
                <div class="price-block-wrapper">
                    <div class="prici-left">
                        <span class="price"><span>€</span>${early_bird_ticket_price_eur}</span>
                        <span class="price"><span>BGN</span>${early_bird_ticket_price}</span><br/>
                        <h5>
                            Until ${cfp_close_date}! </h5>
                    </div>
                    <div class="pricing-list">
                        <h4>Early Bird Ticket</h4>
                        <ul>
                            <li><i class="icon-check"></i><span class="text">2 days access!</span></li>
                            <li><i class="icon-close"></i><span class="text">28+ awesome talks!</span></li>
                            <li><i class="icon-close"></i><span class="text">Hands-on labs!</span></li>
                            <li><i class="icon-close"></i><span class="text">Lunch Box</span></li>
                            <li><i class="icon-close"></i><span class="text">A bag full of goodies</span></li>
                            <li><i class="icon-close"></i><span class="text">Free T-Shirt!</span></li>
                            <li><i class="icon-close"></i><span class="text">Raffle!</span></li>
                            <li><i class="icon-close"></i><span class="text">Beers!</span></li>
                            <li><i class="icon-close"></i><span class="text">Coffee and soft drinks.</span>
                            </li>
                        </ul>
                        <c:if test="${early_sold_out.length() == 0}">
                            <a href="tickets" class="btn btn-common">Buy Ticket</a>
                        </c:if>
                        <c:if test="${early_sold_out.length() > 0}">
                            <a href="tickets" class="btn btn-common">Not Available</a>
                        </c:if>
                    </div>
                </div>
            </div>
            <div class="col-lg-4 col-md-4 col-xs-12 mb-3" style="<c:out value='${regular_sold_out}'/>">
                <div class="price-block-wrapper">
                    <div class="prici-left">
                        <span class="price"><span>€</span>${regular_ticket_price_eur}</span>
                        <span class="price"><span>BGN</span>${regular_ticket_price}</span><br/>
                        <h5>After ${cfp_close_date} </h5>
                    </div>
                    <div class="pricing-list">
                        <h4>Regular Ticket</h4>
                        <ul>
                            <li><i class="icon-check"></i><span class="text">2 days access!</span></li>
                            <li><i class="icon-close"></i><span class="text">28+ awesome talks!</span></li>
                            <li><i class="icon-close"></i><span class="text">Hands-on labs!</span></li>
                            <li><i class="icon-close"></i><span class="text">Lunch Box</span></li>
                            <li><i class="icon-close"></i><span class="text">A bag full of goodies</span></li>
                            <li><i class="icon-close"></i><span class="text">Free T-Shirt!</span></li>
                            <li><i class="icon-close"></i><span class="text">Raffle!</span></li>
                            <li><i class="icon-close"></i><span class="text">Beers!</span></li>
                            <li><i class="icon-close"></i><span class="text">Coffee and soft drinks.</span>
                            </li>
                        </ul>
                        <c:if test="${regular_sold_out.length() == 0}">
                            <a href="tickets" class="btn btn-common">Buy Ticket</a>
                        </c:if>
                        <c:if test="${regular_sold_out.length() > 0}">
                            <a href="tickets" class="btn btn-common">Not Available</a>
                        </c:if>
                    </div>
                </div>
            </div>
            <div class="col-lg-4 col-md-4 col-xs-12 mb-3" style="<c:out value='${students_sold_out}'/>">
                <div class="price-block-wrapper">
                    <div class="prici-left">
                        <span class="price"><span>€</span>${student_ticket_price_eur}</span>
                        <span class="price"><span>BGN</span>${student_ticket_price}</span><br/>
                        <h5>limited to students only</h5>
                    </div>
                    <div class="pricing-list">
                        <h4>Students ticket</h4>
                        <ul>
                            <li><i class="icon-check"></i><span class="text">2 days access!</span></li>
                            <li><i class="icon-close"></i><span class="text">28+ awesome talks!</span></li>
                            <li><i class="icon-close"></i><span class="text">Hands-on labs!</span></li>
                            <li><i class="icon-close"></i><span class="text">Lunch Box</span></li>
                            <li><i class="icon-close"></i><span class="text">A bag full of goodies</span></li>
                            <li><i class="icon-close"></i><span class="text">Free T-Shirt!</span></li>
                            <li><i class="icon-close"></i><span class="text">Raffle!</span></li>
                            <li><i class="icon-close"></i><span class="text">Beers!</span></li>
                            <li><i class="icon-close"></i><span class="text">Coffee and soft drinks.</span>
                            </li>
                        </ul>
                        <c:if test="${students_sold_out.length() == 0}">
                            <a href="tickets" class="btn btn-common">Buy Ticket</a>
                        </c:if>
                        <c:if test="${students_sold_out.length() > 0}">
                            <a href="tickets" class="btn btn-common">Not Available</a>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<!-- Ticket Pricing Area End -->

<!-- Sponsors Section Start -->
<div id="sponsors" class="section-padding">
    <div class="container">
        <c:if test="${platinumSponsors.size() > 0}">
            <div class="row">
                <div class="col-md-12">
                    <h1 class="section-sub text-center">Platinum Sponsors</h1>
                </div>
            </div>
            <div class="row mb-30">
                <c:forEach var="sponsor" items="${platinumSponsors}">
                    <div class="col-md-3 col-sm-3 col-xs-12">
                        <div class="sponsors-logo">
                            <a href="${sponsor.companyWebsite}"><img class=img-fluid"
                                                                     src="/image/sponsor/${sponsor.id}"
                                                                     alt="${sponsor.companyName}"/></a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:if>

        <c:if test="${goldSponsors.size() > 0}">
            <div class="row">
                <div class="col-md-12">
                    <h1 class="section-sub text-center">Gold Sponsors</h1>
                </div>
                <c:forEach var="sponsor" items="${goldSponsors}">
                    <div class="col-md-2 col-sm-2 col-xs-12">
                        <div class="sponsors-logo">
                            <a href="${sponsor.companyWebsite}"><img class="img-fluid"
                                                                     src="/image/sponsor/${sponsor.id}"
                                                                     alt="${sponsor.companyName}"/></a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:if>

        <c:if test="${goldLiteSponsors.size() > 0}">
            <div class="row">
                <div class="col-md-12">
                    <h1 class="section-sub text-center">Gold Lite Sponsors</h1>
                </div>
                <c:forEach var="sponsor" items="${goldLiteSponsors}">
                    <div class="col-md-2 col-sm-2 col-xs-12">
                        <div class="sponsors-logo">
                            <a href="${sponsor.companyWebsite}"><img class="img-fluid"
                                                                     src="/image/sponsor/${sponsor.id}"
                                                                     alt="${sponsor.companyName}"/></a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:if>

        <c:if test="${goldOpenSponsors.size() > 0}">
            <div class="row">
                <div class="col-md-12">
                    <h1 class="section-sub text-center">Gold Open Sponsors</h1>
                </div>
                <c:forEach var="sponsor" items="${goldOpenSponsors}">
                    <div class="col-md-2 col-sm-2 col-xs-12">
                        <div class="sponsors-logo">
                            <a href="${sponsor.companyWebsite}"><img class="img-fluid"
                                                                     src="/image/sponsor/${sponsor.id}"
                                                                     alt="${sponsor.companyName}"/></a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:if>

        <c:if test="${silverSponsors.size() > 0}">
            <div class="row">
                <div class="col-md-12">
                    <h1 class="section-sub text-center">Silver Sponsors</h1>
                </div>
                <c:forEach var="sponsor" items="${silverSponsors}">
                    <div class="col-md-2 col-sm-2 col-xs-12">
                        <div class="sponsors-logo">
                            <a href="${sponsor.companyWebsite}"><img class="img-fluid"
                                                                     src="/image/sponsor/${sponsor.id}"
                                                                     alt="${sponsor.companyName}"/></a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:if>

    </div>
</section>
<!-- Sponsors Section End -->

<!-- Official Supporters Partners Section Start -->
<c:if test="${fn:length(officialSupporterPartnersChunks) gt 0}">
    <div id="sponsors" class="section-padding">
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <h1 class="section-sub text-center">Official Supporters</h1>
                </div>
            </div>
            <div class="row mb-30">
                <c:forEach var="partners" items="${officialSupporterPartnersChunks}">
                    <c:forEach var="partner" items="${partners}">
                        <div class="col-md-3 col-sm-3 col-xs-12">
                            <div class="sponsors-logo">
                                <a href="${partner.companyWebsite}"><img class=img-fluid"
                                                                         src="/image/partner/${partner.id}"
                                                                         alt="${partner.companyName}"/></a>
                            </div>
                        </div>
                    </c:forEach>
                </c:forEach>
            </div>
        </div>
    </section>
</c:if>
<!-- Official Supporters Partners Section End -->

<!-- Media Partners Section Start -->
<c:if test="${fn:length(mediaPartnersChunks) gt 0}">
    <div id="sponsors" class="section-padding">
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <h1 class="section-sub text-center">Community and Media Partners</h1>
                </div>
            </div>
            <div class="row mb-30">
                <c:forEach var="partners" items="${mediaPartnersChunks}">
                    <c:forEach var="partner" items="${partners}">
                        <div class="col-md-3 col-sm-3 col-xs-12">
                            <div class="sponsors-logo">
                                <a href="${partner.companyWebsite}"><img class=img-fluid"
                                                                         src="/image/partner/${partner.id}"
                                                                         alt="${partner.companyName}"/></a>
                            </div>
                        </div>
                    </c:forEach>
                </c:forEach>
            </div>
        </div>
    </section>
</c:if>
<!-- Media Partners Section End -->

<!-- Event Partners Section Start -->
<c:if test="${fn:length(eventPartnerChunks) gt 0}">
    <div id="sponsors" class="section-padding">
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <h1 class="section-sub text-center">Event Partners</h1>
                </div>
            </div>
            <div class="row mb-30">
                <c:forEach var="partners" items="${eventPartnerChunks}">
                    <c:forEach var="partner" items="${partners}">
                        <div class="col-md-3 col-sm-3 col-xs-12">
                            <div class="sponsors-logo">
                                <a href="${partner.companyWebsite}"><img class=img-fluid"
                                                                         src="/image/partner/${partner.id}"
                                                                         alt="${partner.companyName}"/></a>
                            </div>
                        </div>
                    </c:forEach>
                </c:forEach>
            </div>
        </div>
    </section>
</c:if>
<!-- Event Partners Section End -->

<%--
<section id="gallery-section" class="section-padding">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <h1 class="section-title">Last jPrime in Action</h1>
            </div>
        </div>
        <div class="row">
            <div class="col-12" style="height:550px">
                <iframe style="position: relative; top: 0; left: 0; width: 100%; height: 100%;" src="https://flickrembed.com/cms_embed.php?source=flickr&layout=responsive&input=132936671@N08&sort=0&by=user&theme=default_notextpanel&scale=fill&speed=3000&limit=100&skin=default&autoplay=true" scrolling="no" frameborder="0" allowFullScreen="true" webkitallowfullscreen="true" mozallowfullscreen="true"><p><a  href="https://www.codeguesser.co.uk/scottsmenswear.com">Codeguesser</a></p><small>Powered by <a href="https://flickrembed.com">flickr embed</a>.</small></iframe><script type="text/javascript">function showpics(){var a=$("#box").val();$.getJSON("http://api.flickr.com/services/feeds/photos_public.gne?tags="+a+"&tagmode=any&format=json&jsoncallback=?",function(a){$("#images").hide().html(a).fadeIn("fast"),$.each(a.items,function(a,e){$("<img/>").attr("src",e.media.m).appendTo("#images")})})}</script>
            </div>
        </div>
    </div>
</section>
<!-- remove flickr since it creates 15 mbs :D:D maybe will move it to another page but SHOULD NOT BE ON INDEX -->
--%>

<%-- <div id="sponsors" class="section-padding">--%>
<%--     <div class="container" style="text-align: center">--%>
<%--        <div class="row">--%>
<%--            <div class="col-12">--%>
<%--                <h1 class="section-title">jPrime ${jprime_year} recordings</h1>--%>
<%--            </div>--%>
<%--        </div>--%>
        <%--            <div class="row">--%>
        <%--                <div class="col-12">--%>
        <%--                    <iframe width="560" height="315" src="https://www.youtube.com/embed/DxOVSxpFI8E" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>--%>
        <%--                </div>--%>
        <%--            </div>--%>
<%--        <div class="row">--%>
<%--            <div class="col-12">--%>
<%--                <h1 class="section-title">jPrime 2024 Live Streams</h1>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--        <div class="row">--%>
<%--            <div class="col-12">--%>
<%--                <iframe width="560" height="315"--%>
<%--                        src="https://www.youtube.com/embed/TBgWyM2l0Mg?si=dpZCo43-u6vbSNn8&amp;start=1790"--%>
<%--                        title="YouTube video player" frameborder="0"--%>
<%--                        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"--%>
<%--                        referrerpolicy="strict-origin-when-cross-origin" allowfullscreen></iframe>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--        <div class="row">--%>
<%--            <div class="col-12">--%>
<%--                <iframe width="560" height="315"--%>
<%--                        src="https://www.youtube.com/embed/aApQjogPsZo?si=LMcUrIl8jxA6XFJT"--%>
<%--                        title="YouTube video player" frameborder="0"--%>
<%--                        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"--%>
<%--                        referrerpolicy="strict-origin-when-cross-origin" allowfullscreen></iframe>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--        <div class="row">--%>
<%--            <div class="col-12">--%>
<%--                <iframe width="560" height="315"--%>
<%--                        src="https://www.youtube.com/embed/7QFQkmZ0VNI?si=N07yuKKsddAIXUyM"--%>
<%--                        title="YouTube video player" frameborder="0"--%>
<%--                        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"--%>
<%--                        referrerpolicy="strict-origin-when-cross-origin" allowfullscreen></iframe>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--        <div class="row">--%>
<%--            <div class="col-12">--%>
<%--                <h1 class="section-title">jPrime 2024 Day 2 Live Streams</h1>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--        <div class="row">--%>
<%--            <div class="col-12">--%>
<%--                <iframe width="560" height="315"--%>
<%--                        src="https://www.youtube.com/embed/vuzTgaeU5TI?si=pyHlBYjXaidDBYJ0&amp;start=4639"--%>
<%--                        title="YouTube video player" frameborder="0"--%>
<%--                        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"--%>
<%--                        referrerpolicy="strict-origin-when-cross-origin" allowfullscreen></iframe>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--        <div class="row">--%>
<%--            <div class="col-12">--%>
<%--                <iframe width="560" height="315"--%>
<%--                        src="https://www.youtube.com/embed/5Cvqava5J54?si=xQlMEBci9S12RVej&amp;start=2961"--%>
<%--                        title="YouTube video player" frameborder="0"--%>
<%--                        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"--%>
<%--                        referrerpolicy="strict-origin-when-cross-origin" allowfullscreen></iframe>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</section>--%>

<%--    <!-- Gallary Section Start -->--%>
<%--    <section id="gallery-section" class="section-padding">--%>
<%--        <div class="container">--%>
<%--            <div class="row">--%>
<%--                <div class="col-12">--%>
<%--                    <div class="section-title-header text-center">--%>
<%--                        <h1 class="section-title">From Last Event</h1>--%>
<%--                        <p>Lorem ipsum dolor sit amet, consectetur adipiscing <br> elit, sed do eiusmod tempor</p>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--            </div>--%>
<%--            <div class="row">--%>
<%--                <div class="col-md-6 col-sm-6 col-lg-4">--%>
<%--                    <div class="gallery-box">--%>
<%--                        <div class="img-thumb">--%>
<%--                            <img class="img-fluid" src="assets/img/gallery/img-1.jpg" alt="">--%>
<%--                        </div>--%>
<%--                        <div class="overlay-box text-center">--%>
<%--                            <a class="lightbox" href="assets/img/gallery/img-1.jpg">--%>
<%--                                <i class="icon-eye"></i>--%>
<%--                            </a>--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--                <div class="ccol-md-6 col-sm-6 col-lg-4">--%>
<%--                    <div class="gallery-box">--%>
<%--                        <div class="img-thumb">--%>
<%--                            <img class="img-fluid" src="assets/img/gallery/img-2.jpg" alt="">--%>
<%--                        </div>--%>
<%--                        <div class="overlay-box text-center">--%>
<%--                            <a class="lightbox" href="assets/img/gallery/img-2.jpg">--%>
<%--                                <i class="icon-eye"></i>--%>
<%--                            </a>--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--                <div class="ccol-md-6 col-sm-6 col-lg-4">--%>
<%--                    <div class="gallery-box">--%>
<%--                        <div class="img-thumb">--%>
<%--                            <img class="img-fluid" src="assets/img/gallery/img-3.jpg" alt="">--%>
<%--                        </div>--%>
<%--                        <div class="overlay-box text-center">--%>
<%--                            <a class="lightbox" href="assets/img/gallery/img-3.jpg">--%>
<%--                                <i class="icon-eye"></i>--%>
<%--                            </a>--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--                <div class="ccol-md-6 col-sm-6 col-lg-4">--%>
<%--                    <div class="gallery-box">--%>
<%--                        <div class="img-thumb">--%>
<%--                            <img class="img-fluid" src="assets/img/gallery/img-4.jpg" alt="">--%>
<%--                        </div>--%>
<%--                        <div class="overlay-box text-center">--%>
<%--                            <a class="lightbox" href="assets/img/gallery/img-4.jpg">--%>
<%--                                <i class="icon-eye"></i>--%>
<%--                            </a>--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--                <div class="ccol-md-6 col-sm-6 col-lg-4">--%>
<%--                    <div class="gallery-box">--%>
<%--                        <div class="img-thumb">--%>
<%--                            <img class="img-fluid" src="assets/img/gallery/img-5.jpg" alt="">--%>
<%--                        </div>--%>
<%--                        <div class="overlay-box text-center">--%>
<%--                            <a class="lightbox" href="assets/img/gallery/img-5.jpg">--%>
<%--                                <i class="icon-eye"></i>--%>
<%--                            </a>--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--                <div class="ccol-md-6 col-sm-6 col-lg-4">--%>
<%--                    <div class="gallery-box">--%>
<%--                        <div class="img-thumb">--%>
<%--                            <img class="img-fluid" src="assets/img/gallery/img-6.jpg" alt="">--%>
<%--                        </div>--%>
<%--                        <div class="overlay-box text-center">--%>
<%--                            <a class="lightbox" href="assets/img/gallery/img-6.jpg">--%>
<%--                                <i class="icon-eye"></i>--%>
<%--                            </a>--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </section>--%>
<%--    <!-- Gallary Section End -->--%>

<%--    <!-- Blog Section Start -->--%>
<%--    <section id="blog" class="section-padding">--%>
<%--        <div class="container">--%>
<%--            <div class="row">--%>
<%--                <div class="col-12">--%>
<%--                    <div class="section-title-header text-center">--%>
<%--                        <h2 class="section-title">From The Blog</h2>--%>
<%--                        <p>Lorem ipsum dolor sit amet, consectetur adipiscing <br> elit, sed do eiusmod tempor</p>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--                <div class="col-lg-4 col-md-6 col-xs-12">--%>
<%--                    <div class="blog-item text-center">--%>
<%--                        <div class="blog-image">--%>
<%--                            <a href="#">--%>
<%--                                <img class="img-fluid" src="assets/img/blog/img1.jpg" alt="">--%>
<%--                            </a>--%>
<%--                            <div class="date"><span class="day">12</span>June</div>--%>
<%--                        </div>--%>
<%--                        <div class="descr">--%>
<%--                            <p class="subtitle">By Korneila</p>--%>
<%--                            <h3 class="title">--%>
<%--                                <a href="single-blog.html">--%>
<%--                                    15 Best Outfit Ideas To Wear in Events--%>
<%--                                </a>--%>
<%--                            </h3>--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--                <div class="col-lg-4 col-md-6 col-xs-12">--%>
<%--                    <div class="blog-item text-center">--%>
<%--                        <div class="blog-image">--%>
<%--                            <a href="#">--%>
<%--                                <img class="img-fluid" src="assets/img/blog/img2.jpg" alt="">--%>
<%--                            </a>--%>
<%--                            <div class="date"><span class="day">12</span>June</div>--%>
<%--                        </div>--%>
<%--                        <div class="descr">--%>
<%--                            <p class="subtitle">By Stuart</p>--%>
<%--                            <h3 class="title">--%>
<%--                                <a href="single-blog.html">--%>
<%--                                    10 Tips for Successful Business Networking--%>
<%--                                </a>--%>
<%--                            </h3>--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--                <div class="col-lg-4 col-md-6 col-xs-12">--%>
<%--                    <div class="blog-item text-center">--%>
<%--                        <div class="blog-image">--%>
<%--                            <a href="#">--%>
<%--                                <img class="img-fluid" src="assets/img/blog/img3.jpg" alt="">--%>
<%--                            </a>--%>
<%--                            <div class="date"><span class="day">12</span>June</div>--%>
<%--                        </div>--%>
<%--                        <div class="descr">--%>
<%--                            <p class="subtitle">By John</p>--%>
<%--                            <h3 class="title">--%>
<%--                                <a href="single-blog.html">--%>
<%--                                    The 9 Design Trends You Need to Know--%>
<%--                                </a>--%>
<%--                            </h3>--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </section>--%>
<%--    <!-- Blog Section End -->--%>

<!-- Subscribe Area Start -->
<div id="subscribe" class="section-padding">
    <div class="container">
        <div class="row justify-content-md-center">
            <div class="col-md-10 col-lg-7">
                <div class="subscribe-inner wow fadeInUp" data-wow-delay="0.2s">
                    <h2 class="subscribe-title">Get Realtime Event Updates</h2>
                    <p>You can signup for our newsletter to recieve updates related to jPrime.</p>
                    <form class="text-center form-inline"
                          action="https://jprime.us13.list-manage.com/subscribe/post?u=968bc6e2a2348118b259c0df2&id=c9a8e30e18"
                          method="POST" id="mc-embedded-subscribe-form" name="mc-embedded-subscribe-form"
                          class="validate" target="_blank" novalidate style="display: flex;">
                        <input class="mb-20 form-control" style="text-transform: none" name="EMAIL"
                               placeholder="Your Email Here" required type="email">
                        <div class="icon-s"><i class="icon-envelope"></i></div>
                        <button type="submit" class="btn btn-common" data-style="zoom-in"
                                data-spinner-size="30" name="submit" id="submit">
                            <span class="ladda-label">Submit</span>
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- Subscribe Area End -->

<!-- Sponsors Pricing Area Start -->
<section id="pricing" class="section-padding">
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <h1 class="section-sub text-center">Become a sponsor!</h1>
            </div>
        </div>
        <div class="row" id="sponsor">
            <div class="col-lg-4 col-md-4 col-xs-12 mb-4"
                 style="<c:out value="${sold_out_sponsor_packages.get('PLATINUM')}"/>">
                <div class="price-block-wrapper">
                    <div class="prici-left">
                        <span class="price"><span>€</span>4250</span>
                        <span class="price"><span>BGN</span>8312.28</span>
                        <h5></h5>
                    </div>
                    <div class="pricing-list">
                        <h4>Platinum</h4>
                        <ul>
                            <li><i class="icon-check"></i><span class="text">9 free passes!</span></li>
                            <li><i class="icon-close"></i><span class="text">15% discount on additional passes!</span>
                            </li>
                            <li><i class="icon-close"></i><span
                                    class="text">Booth in the conference hall!</span></li>
                            <li><i class="icon-close"></i><span class="text">Presence on the stage for the raffle</span>
                            </li>
                            <li><i class="icon-close"></i><span
                                    class="text">Logo on the presentation screens</span></li>
                            <li><i class="icon-close"></i><span class="text">1 invite for the "thank you" dinner</span>
                            </li>
                        </ul>
                        <c:if test="${sold_out_sponsor_packages.get('PLATINUM').length() == 0}">
                            <a href="mailto:conference@jprime.io?subject=platinum sponsorship"
                               class="btn btn-common">Sign Up Now</a>
                        </c:if>
                        <c:if test="${sold_out_sponsor_packages.get('PLATINUM').length() > 0}">
                            <a href="javascript: return 0;" class="btn btn-common">Sold out</a>
                        </c:if>
                    </div>
                </div>
            </div>
            <div class="col-lg-4 col-md-4 col-xs-12 mb-4"
                 style="<c:out value="${sold_out_sponsor_packages.get('GOLD')}"/>">
                <div class="price-block-wrapper">
                    <div class="prici-left">
                        <span class="price"><span>€</span>3250</span>
                        <span class="price"><span>BGN</span>6356.45</span>
                        <h5></h5>
                    </div>
                    <div class="pricing-list">
                        <h4>Gold</h4>
                        <ul>
                            <li><i class="icon-check"></i><span class="text">6 free passes!</span></li>
                            <li><i class="icon-close"></i><span class="text">10% discount on additional passes!</span>
                            </li>
                            <li><i class="icon-close"></i><span
                                    class="text">Booth in the conference hall!</span></li>
                            <li><i class="icon-close"></i><span
                                    class="text">Logo on the presentation screens</span></li>
                        </ul>
                        <c:if test="${sold_out_sponsor_packages.get('GOLD').length() == 0}">
                            <a href="mailto:conference@jprime.io?subject=gold sponsorship"
                               class="btn btn-common">Sign Up Now</a>
                        </c:if>
                        <c:if test="${sold_out_sponsor_packages.get('GOLD').length() > 0}">
                            <a href="javascript: return 0;" class="btn btn-common">Sold out</a>
                        </c:if>
                    </div>
                </div>
            </div>
            <div class="col-lg-4 col-md-4 col-xs-12 mb-4"
                 style="<c:out value="${sold_out_sponsor_packages.get('GOLD_LITE')}"/>">
                <div class="price-block-wrapper">
                    <div class="prici-left">
                        <span class="price"><span>€</span>2650</span>
                        <span class="price"><span>BGN</span>5182.95</span>
                        <h5></h5>
                    </div>
                    <div class="pricing-list">
                        <h4>Gold Light</h4>
                        <ul>
                            <li><i class="icon-check"></i><span class="text">6 free passes!</span></li>
                            <li><i class="icon-close"></i><span class="text">10% discount on additional passes!</span>
                            </li>
                            <li><i class="icon-close"></i><span class="text">Booth in the corner area!</span>
                            </li>
                            <li><i class="icon-close"></i><span
                                    class="text">Logo on the presentation screens</span></li>
                        </ul>
                        <c:if test="${sold_out_sponsor_packages.get('GOLD_LITE').length() == 0}">
                            <a href="mailto:conference@jprime.io?subject=gold_lite_sponsorship"
                               class="btn btn-common">Sign Up Now</a>
                        </c:if>
                        <c:if test="${sold_out_sponsor_packages.get('GOLD_LITE').length() > 0}">
                            <a href="javascript: return 0;" class="btn btn-common">Sold out</a>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
        <div class="row" id="sponsor1">
            <div class="col-lg-4 col-md-4 col-xs-12 mb-4"
                 style="<c:out value="${sold_out_sponsor_packages.get('GOLD_OPEN')}"/>">
                <div class="price-block-wrapper">
                    <div class="prici-left">
                        <span class="price"><span>€</span>2250</span>
                        <span class="price"><span>BGN</span>4400.62</span>
                        <h5></h5>
                    </div>
                    <div class="pricing-list">
                        <h4>Gold Open</h4>
                        <ul>
                            <li><i class="icon-check"></i><span class="text">6 free passes!</span></li>
                            <li><i class="icon-close"></i><span class="text">10% discount on additional passes!</span>
                            </li>
                            <li><i class="icon-close"></i><span class="text">Booth under the tent in the area in front of the venue!</span>
                            </li>
                            <li><i class="icon-close"></i><span
                                    class="text">Logo on the presentation screens</span></li>
                        </ul>
                        <c:if test="${sold_out_sponsor_packages.get('GOLD_OPEN').length() == 0}">
                            <a href="mailto:conference@jprime.io?subject=gold_open_sponsorship"
                               class="btn btn-common">Sign Up Now</a>
                        </c:if>
                        <c:if test="${sold_out_sponsor_packages.get('GOLD_OPEN').length() > 0}">
                            <a href="javascript: return 0;" class="btn btn-common">Sold out</a>
                        </c:if>
                    </div>
                </div>
            </div>
            <div class="col-lg-4 col-md-4 col-xs-12 mb-4"
                 style="<c:out value="${sold_out_sponsor_packages.get('SILVER')}"/>">
                <div class="price-block-wrapper">
                    <div class="prici-left">
                        <span class="price"><span>€</span>1000</span>
                        <span class="price"><span>BGN</span>1955.83</span>
                        <h5></h5>
                    </div>
                    <div class="pricing-list">
                        <h4>Silver</h4>
                        <ul>
                            <li><i class="icon-check"></i><span class="text">3 free passes!</span></li>
                            <li><i class="icon-close"></i><span class="text">10% discount on additional passes!</span>
                            </li>
                            <li><i class="icon-close"></i><span
                                    class="text">Logo on the presentation screens</span></li>
                        </ul>
                        <c:if test="${sold_out_sponsor_packages.get('SILVER').length() == 0}">
                            <a href="mailto:conference@jprime.io?subject=silver_sponsorship"
                               class="btn btn-common">Sign Up Now</a>
                        </c:if>
                        <c:if test="${sold_out_sponsor_packages.get('SILVER').length() > 0}">
                            <a href="javascript: return 0;" class="btn btn-common">Sold out</a>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<!-- Sponsors Pricing Area End -->

<user:footer/>

<script>
// Initialize Bootstrap 5 carousel with auto-sliding
document.addEventListener('DOMContentLoaded', function() {
    var carouselElement = document.querySelector('#main-slide');
    if (carouselElement && typeof bootstrap !== 'undefined') {
        var carousel = new bootstrap.Carousel(carouselElement, {
            interval: 5000,
            ride: 'carousel',
            wrap: true,
            keyboard: true,
            pause: 'hover'
        });
    }
});
</script>

</body>

</html>
