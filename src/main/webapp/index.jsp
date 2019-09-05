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
    <title>jPrime | Home</title>

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
    <div id="main-slide" class="carousel slide" data-ride="carousel">
        <ol class="carousel-indicators">
            <li data-target="#main-slide" data-slide-to="0" class="active"></li>
            <li data-target="#main-slide" data-slide-to="1"></li>
            <li data-target="#main-slide" data-slide-to="2"></li>
        </ol>
        <div class="carousel-inner">
            <div class="carousel-item active">
                <img class="d-block w-100" src="assets/img/slider/slide1.jpg" alt="First slide">
                <div class="carousel-caption d-md-block">
                    <h1 class="wow fadeInDown heading" data-wow-delay=".4s">jPrime 2020</h1>
                    <p class="fadeInUp wow" data-wow-delay=".6s">The conference will be held on 28 and 29th of May, 2020 in Sofia Tech Park</p>
                    <a href="#" class="fadeInLeft wow btn btn-common" data-wow-delay=".6s">Get Ticket</a>
                    <!-- <a href="#" class="fadeInRight wow btn btn-border" data-wow-delay=".6s">Contact</a> -->
                </div>
            </div>
            <div class="carousel-item">
                <img class="d-block w-100" src="assets/img/slider/slide2.jpg" alt="Second slide">
                <div class="carousel-caption d-md-block">
                    <h1 class="wow bounceIn heading" data-wow-delay=".7s">36 Amazing Speakers</h1>
                    <p class="fadeInUp wow" data-wow-delay=".9s">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Provident eum ullam <br> cupiditate nam rerum numquam blanditiis doloribus aspernatur.</p>
                    <a href="#" class="fadeInUp wow btn btn-border" data-wow-delay=".8s">Learn More</a>
                </div>
            </div>
            <div class="carousel-item">
                <img class="d-block w-100" src="assets/img/slider/slide3.jpg" alt="Third slide">
                <div class="carousel-caption d-md-block">
                    <h1 class="wow fadeInUp heading" data-wow-delay=".6s">Book Your Seat Now!</h1>
                    <p class="fadeInUp wow" data-wow-delay=".8s">The last 2 years the seets were sold out.</p>
                    <a href="tickets/" class="fadeInUp wow btn btn-common" data-wow-delay=".8s">BUY a ticket</a>
                </div>
            </div>
        </div>
        <a class="carousel-control-prev" href="#main-slide" role="button" data-slide="prev">
            <span class="carousel-control" aria-hidden="true"><i class="icon-arrow-left"></i></span>
            <span class="sr-only">Previous</span>
        </a>
        <a class="carousel-control-next" href="#main-slide" role="button" data-slide="next">
            <span class="carousel-control" aria-hidden="true"><i class="icon-arrow-right"></i></span>
            <span class="sr-only">Next</span>
        </a>
    </div>
    <!-- Main Carousel Section End -->

    <!-- Coundown Section Start -->
    <section class="countdown-timer section-padding">
        <div class="container">
            <div class="row text-center">
                <div class="col-md-12 col-sm-12 col-xs-12">
                    <div class="heading-count">
                        <h2>Where Java Developers Meets</h2>
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
    <!-- Coundown Section End -->

    <!-- About Section Start -->
    <section id="about" class="section-padding">
        <div class="container">
            <div class="row">
                <div class="col-md-6 col-lg-6 col-xs-12">
                    <h2 class="intro-title">About</h2>
                    <h3 class="title-sub">The Conference</h3>
                    <p class="intro-desc">jPrime is a conference with talks on Java, various languages on the JVM, mobile, web and best practices.
                        Its sixth edition will be held on 28th and 29th May 2019 in Sofia Tech Park.
                    </p>
                    <p>It's run by the Bulgarian Java User Group and backed by the biggest companies in the city. </p>
                    <p>jPrime features a combination of great international speakers along with the best presenters from Bulgaria and the Balkans.
                        It is divided in two tracks and provides great opportunities for learning, hacking, networking and fun.</p>
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
    <section id="sponsors" class="section-padding">
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <h1 class="section-sub text-center">The conference is organized by</h1>
                </div>
            </div>
            <div class="row mb-30">
                <div class="col-md-3 col-sm-3 col-xs-12">
                    <div class="spnsors-logo">
                        <a href="#"><img class="img-fluid" src="assets/img/sponsors/gold-1.png" alt=""></a>
                    </div>
                </div>
                <div class="col-md-3 col-sm-3 col-xs-12">
                    <div class="spnsors-logo">
                        <a href="#"><img class="img-fluid" src="assets/img/sponsors/gold-2.png" alt=""></a>
                    </div>
                </div>
                <div class="col-md-3 col-sm-3 col-xs-12">
                    <div class="spnsors-logo">
                        <a href="#"><img class="img-fluid" src="assets/img/sponsors/gold-3.png" alt=""></a>
                    </div>
                </div>
                <div class="col-md-3 col-sm-3 col-xs-12">
                    <div class="spnsors-logo">
                        <a href="#"><img class="img-fluid" src="assets/img/sponsors/gold-4.png" alt=""></a>
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
                        <h1 class="section-title">Meet Speakers</h1>
                        <p>Lorem ipsum dolor sit amet, consectetur adipiscing <br> elit, sed do eiusmod tempor</p>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-6 col-md-6 col-lg-3">
                    <!-- Team Item Starts -->
                    <div class="team-item text-center">
                        <div class="team-img">
                            <img class="img-fluid" src="assets/img/team/team-01.jpg" alt="">
                            <div class="team-overlay">
                                <div class="overlay-social-icon text-center">
                                    <ul class="social-icons">
                                        <li><a href="#"><i class="fa fa-facebook" aria-hidden="true"></i></a></li>
                                        <li><a href="#"><i class="fa fa-twitter" aria-hidden="true"></i></a></li>
                                        <li><a href="#"><i class="fa fa-instagram" aria-hidden="true"></i></a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <div class="info-text">
                            <h3><a href="#">Emilly Williams</a></h3>
                            <p>Product Designer, Tesla</p>
                        </div>
                    </div>
                    <!-- Team Item Ends -->
                </div>
                <div class="col-sm-6 col-md-6 col-lg-3">
                    <!-- Team Item Starts -->
                    <div class="team-item text-center">
                        <div class="team-img">
                            <img class="img-fluid" src="assets/img/team/team-02.jpg" alt="">
                            <div class="team-overlay">
                                <div class="overlay-social-icon text-center">
                                    <ul class="social-icons">
                                        <li><a href="#"><i class="fa fa-facebook" aria-hidden="true"></i></a></li>
                                        <li><a href="#"><i class="fa fa-twitter" aria-hidden="true"></i></a></li>
                                        <li><a href="#"><i class="fa fa-instagram" aria-hidden="true"></i></a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <div class="info-text">
                            <h3><a href="#">Patric Green</a></h3>
                            <p>Front-end Developer, Dropbox</p>
                        </div>
                    </div>
                    <!-- Team Item Ends -->
                </div>

                <div class="col-sm-6 col-md-6 col-lg-3">
                    <!-- Team Item Starts -->
                    <div class="team-item text-center">
                        <div class="team-img">
                            <img class="img-fluid" src="assets/img/team/team-03.jpg" alt="">
                            <div class="team-overlay">
                                <div class="overlay-social-icon text-center">
                                    <ul class="social-icons">
                                        <li><a href="#"><i class="fa fa-facebook" aria-hidden="true"></i></a></li>
                                        <li><a href="#"><i class="fa fa-twitter" aria-hidden="true"></i></a></li>
                                        <li><a href="#"><i class="fa fa-instagram" aria-hidden="true"></i></a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <div class="info-text">
                            <h3><a href="#">Paul Kowalsy</a></h3>
                            <p>Lead Designer, TNW</p>
                        </div>
                    </div>
                    <!-- Team Item Ends -->
                </div>

                <div class="col-sm-6 col-md-6 col-lg-3">
                    <!-- Team Item Starts -->
                    <div class="team-item text-center">
                        <div class="team-img">
                            <img class="img-fluid" src="assets/img/team/team-04.jpg" alt="">
                            <div class="team-overlay">
                                <div class="overlay-social-icon text-center">
                                    <ul class="social-icons">
                                        <li><a href="#"><i class="fa fa-facebook" aria-hidden="true"></i></a></li>
                                        <li><a href="#"><i class="fa fa-twitter" aria-hidden="true"></i></a></li>
                                        <li><a href="#"><i class="fa fa-instagram" aria-hidden="true"></i></a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <div class="info-text">
                            <h3><a href="#">Jhon Doe</a></h3>
                            <p>Back-end Developer, ASUS</p>
                        </div>
                    </div>
                    <!-- Team Item Ends -->
                </div>
            </div>
            <div class="row">
                <div class="col-sm-6 col-md-6 col-lg-3">
                    <!-- Team Item Starts -->
                    <div class="team-item text-center">
                        <div class="team-img">
                            <img class="img-fluid" src="assets/img/team/team-05.jpg" alt="">
                            <div class="team-overlay">
                                <div class="overlay-social-icon text-center">
                                    <ul class="social-icons">
                                        <li><a href="#"><i class="fa fa-facebook" aria-hidden="true"></i></a></li>
                                        <li><a href="#"><i class="fa fa-twitter" aria-hidden="true"></i></a></li>
                                        <li><a href="#"><i class="fa fa-instagram" aria-hidden="true"></i></a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <div class="info-text">
                            <h3><a href="#">Daryl Dixon</a></h3>
                            <p>Full-stack Developer, Google</p>
                        </div>
                    </div>
                    <!-- Team Item Ends -->
                </div>
                <div class="col-sm-6 col-md-6 col-lg-3">
                    <!-- Team Item Starts -->
                    <div class="team-item text-center">
                        <div class="team-img">
                            <img class="img-fluid" src="assets/img/team/team-06.jpg" alt="">
                            <div class="team-overlay">
                                <div class="overlay-social-icon text-center">
                                    <ul class="social-icons">
                                        <li><a href="#"><i class="fa fa-facebook" aria-hidden="true"></i></a></li>
                                        <li><a href="#"><i class="fa fa-twitter" aria-hidden="true"></i></a></li>
                                        <li><a href="#"><i class="fa fa-instagram" aria-hidden="true"></i></a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <div class="info-text">
                            <h3><a href="#">Chris Adams</a></h3>
                            <p>UI Designer, Apple</p>
                        </div>
                    </div>
                    <!-- Team Item Ends -->
                </div>

                <div class="col-sm-6 col-md-6 col-lg-3">
                    <!-- Team Item Starts -->
                    <div class="team-item text-center">
                        <div class="team-img">
                            <img class="img-fluid" src="assets/img/team/team-07.jpg" alt="">
                            <div class="team-overlay">
                                <div class="overlay-social-icon text-center">
                                    <ul class="social-icons">
                                        <li><a href="#"><i class="fa fa-facebook" aria-hidden="true"></i></a></li>
                                        <li><a href="#"><i class="fa fa-twitter" aria-hidden="true"></i></a></li>
                                        <li><a href="#"><i class="fa fa-instagram" aria-hidden="true"></i></a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <div class="info-text">
                            <h3><a href="#">Lisa Amaira</a></h3>
                            <p>Product Manager, Uber</p>
                        </div>
                    </div>
                    <!-- Team Item Ends -->
                </div>

                <div class="col-sm-6 col-md-6 col-lg-3">
                    <!-- Team Item Starts -->
                    <div class="team-item text-center">
                        <div class="team-img">
                            <img class="img-fluid" src="assets/img/team/team-08.jpg" alt="">
                            <div class="team-overlay">
                                <div class="overlay-social-icon text-center">
                                    <ul class="social-icons">
                                        <li><a href="#"><i class="fa fa-facebook" aria-hidden="true"></i></a></li>
                                        <li><a href="#"><i class="fa fa-twitter" aria-hidden="true"></i></a></li>
                                        <li><a href="#"><i class="fa fa-instagram" aria-hidden="true"></i></a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <div class="info-text">
                            <h3><a href="#">Rick Grimes</a></h3>
                            <p>QA, Samsung</p>
                        </div>
                    </div>
                    <!-- Team Item Ends -->
                </div>
            </div>
            <a href="speakers.html" class="btn btn-common mt-30">All Speakers</a>
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
                        <p>Lorem ipsum dolor sit amet, consectetur adipiscing <br> elit, sed do eiusmod tempor</p>
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
                            <h3><a href="#">Meet Experts</a></h3>
                            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>
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
                            <h3><a href="#">Tech Sessions</a></h3>
                            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>
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
                            <h3><a href="#">Global Event</a></h3>
                            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>
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
                            <h3><a href="#">Free Swags</a></h3>
                            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>
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
                            <h3><a href="#">Networking Session</a></h3>
                            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>
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
                            <h3><a href="#">Design Insights</a></h3>
                            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</p>
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
                        <p>Spekers</p>
                        <div class="counterUp">36</div>
                    </div>
                </div>
                <!-- Counter Item -->
                <div class="col-md-3 col-sm-6 work-counter-widget text-center">
                    <div class="counter">
                        <div class="icon"><i class="icon-bulb"></i></div>
                        <p>Participants</p>
                        <div class="counterUp">950</div>
                    </div>
                </div>
                <!-- Counter Item -->
                <div class="col-md-3 col-sm-6 work-counter-widget text-center">
                    <div class="counter">
                        <div class="icon"><i class="icon-briefcase"></i></div>
                        <p>Sponsors</p>
                        <div class="counterUp">17</div>
                    </div>
                </div>
                <!-- Counter Item -->
                <div class="col-md-3 col-sm-6 work-counter-widget text-center">
                    <div class="counter">
                        <div class="icon"><i class="icon-cup"></i></div>
                        <p>Sessions</p>
                        <div class="counterUp">52</div>
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
            <div class="row">
                <div class="col-lg-6 col-md-6 col-xs-12 mb-3">
                    <div class="price-block-wrapper">
                        <div class="prici-left">
                            <span class="price"><span>$</span>35</span>
                            <h5>One Day Ticket</h5>
                        </div>
                        <div class="pricing-list">
                            <h4>Bronze pass</h4>
                            <ul>
                                <li><i class="icon-check"></i><span class="text">Event Access</span></li>
                                <li><i class="icon-close"></i><span class="text">Free Lunch & Coffees</span></li>
                                <li><i class="icon-close"></i><span class="text">Free Swags</span></li>
                                <li><i class="icon-close"></i><span class="text">Free WiFi Token</span></li>
                            </ul>
                            <a href="#" class="btn btn-common">Buy Ticket</a>
                        </div>
                    </div>
                </div>
                <div class="col-lg-6 col-md-6 col-xs-12 mb-3">
                    <div class="price-block-wrapper">
                        <div class="prici-left">
                            <span class="price"><span>$</span>55</span>
                            <h5>One Day Ticket</h5>
                        </div>
                        <div class="pricing-list">
                            <h4>Silver Pass</h4>
                            <ul>
                                <li><i class="icon-check"></i><span class="text">Event Access</span></li>
                                <li><i class="icon-check"></i><span class="text">Free Lunch & Coffees</span></li>
                                <li><i class="icon-close"></i><span class="text">Free Swags</span></li>
                                <li><i class="icon-close"></i><span class="text">Free WiFi Token</span></li>
                            </ul>
                            <a href="#" class="btn btn-common">Buy Ticket</a>
                        </div>
                    </div>
                </div>
                <div class="col-lg-6 col-md-6 col-xs-12 mb-3">
                    <div class="price-block-wrapper">
                        <div class="prici-left">
                            <span class="price"><span>$</span>95</span>
                            <h5>One Day Ticket</h5>
                        </div>
                        <div class="pricing-list">
                            <h4>Golden Pass</h4>
                            <ul>
                                <li><i class="icon-check"></i><span class="text">Event Access</span></li>
                                <li><i class="icon-check"></i><span class="text">Free Lunch & Coffees</span></li>
                                <li><i class="icon-check"></i><span class="text">Free Swags</span></li>
                                <li><i class="icon-close"></i><span class="text">Free WiFi Token</span></li>
                            </ul>
                            <a href="#" class="btn btn-common">Buy Ticket</a>
                        </div>
                    </div>
                </div>
                <div class="col-lg-6 col-md-6 col-xs-12 mb-3">
                    <div class="price-block-wrapper">
                        <div class="prici-left">
                            <span class="price"><span>$</span>115</span>
                            <h5>One Day Ticket</h5>
                        </div>
                        <div class="pricing-list">
                            <h4>Platinum Pass</h4>
                            <ul>
                                <li><i class="icon-check"></i><span class="text">Event Access</span></li>
                                <li><i class="icon-check"></i><span class="text">Free Lunch & Coffees</span></li>
                                <li><i class="icon-check"></i><span class="text">Free Swags</span></li>
                                <li><i class="icon-check"></i><span class="text">Free WiFi Token</span></li>
                            </ul>
                            <a href="#" class="btn btn-common">Buy Ticket</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!-- Ticket Pricing Area End -->

    <!-- Sponsors Section Start -->
    <section id="sponsors" class="section-padding">
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <h1 class="section-sub text-center">Gold Sponsors</h1>
                </div>
            </div>
            <div class="row mb-30">
                <div class="col-md-3 col-sm-3 col-xs-12">
                    <div class="spnsors-logo">
                        <a href="#"><img class="img-fluid" src="assets/img/sponsors/gold-1.png" alt=""></a>
                    </div>
                </div>
                <div class="col-md-3 col-sm-3 col-xs-12">
                    <div class="spnsors-logo">
                        <a href="#"><img class="img-fluid" src="assets/img/sponsors/gold-2.png" alt=""></a>
                    </div>
                </div>
                <div class="col-md-3 col-sm-3 col-xs-12">
                    <div class="spnsors-logo">
                        <a href="#"><img class="img-fluid" src="assets/img/sponsors/gold-3.png" alt=""></a>
                    </div>
                </div>
                <div class="col-md-3 col-sm-3 col-xs-12">
                    <div class="spnsors-logo">
                        <a href="#"><img class="img-fluid" src="assets/img/sponsors/gold-4.png" alt=""></a>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12">
                    <h1 class="section-sub text-center">Silver Sponsors</h1>
                </div>
                <div class="col-md-2 col-sm-2 col-xs-12">
                    <div class="spnsors-logo">
                        <a href="#"><img class="img-fluid" src="assets/img/sponsors/logo-01.jpg" alt=""></a>
                    </div>
                </div>
                <div class="col-md-2 col-sm-2 col-xs-12">
                    <div class="spnsors-logo">
                        <a href="#"><img class="img-fluid" src="assets/img/sponsors/logo-02.jpg" alt=""></a>
                    </div>
                </div>
                <div class="col-md-2 col-sm-2 col-xs-12">
                    <div class="spnsors-logo">
                        <a href="#"><img class="img-fluid" src="assets/img/sponsors/logo-03.jpg" alt=""></a>
                    </div>
                </div>
                <div class="col-md-2 col-sm-2 col-xs-12">
                    <div class="spnsors-logo">
                        <a href="#"><img class="img-fluid" src="assets/img/sponsors/logo-04.jpg" alt=""></a>
                    </div>
                </div>
                <div class="col-md-2 col-sm-2 col-xs-12">
                    <div class="spnsors-logo">
                        <a href="#"><img class="img-fluid" src="assets/img/sponsors/logo-05.jpg" alt=""></a>
                    </div>
                </div>
                <div class="col-md-2 col-sm-2 col-xs-12">
                    <div class="spnsors-logo">
                        <a href="#"><img class="img-fluid" src="assets/img/sponsors/logo-06.jpg" alt=""></a>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!-- Sponsors Section End -->

    <!-- Gallary Section Start -->
    <section id="gallery-section" class="section-padding">
        <div class="container">
            <div class="row">
                <div class="col-12">
                    <div class="section-title-header text-center">
                        <h1 class="section-title">From Last Event</h1>
                        <p>Lorem ipsum dolor sit amet, consectetur adipiscing <br> elit, sed do eiusmod tempor</p>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6 col-sm-6 col-lg-4">
                    <div class="gallery-box">
                        <div class="img-thumb">
                            <img class="img-fluid" src="assets/img/gallery/img-1.jpg" alt="">
                        </div>
                        <div class="overlay-box text-center">
                            <a class="lightbox" href="assets/img/gallery/img-1.jpg">
                                <i class="icon-eye"></i>
                            </a>
                        </div>
                    </div>
                </div>
                <div class="ccol-md-6 col-sm-6 col-lg-4">
                    <div class="gallery-box">
                        <div class="img-thumb">
                            <img class="img-fluid" src="assets/img/gallery/img-2.jpg" alt="">
                        </div>
                        <div class="overlay-box text-center">
                            <a class="lightbox" href="assets/img/gallery/img-2.jpg">
                                <i class="icon-eye"></i>
                            </a>
                        </div>
                    </div>
                </div>
                <div class="ccol-md-6 col-sm-6 col-lg-4">
                    <div class="gallery-box">
                        <div class="img-thumb">
                            <img class="img-fluid" src="assets/img/gallery/img-3.jpg" alt="">
                        </div>
                        <div class="overlay-box text-center">
                            <a class="lightbox" href="assets/img/gallery/img-3.jpg">
                                <i class="icon-eye"></i>
                            </a>
                        </div>
                    </div>
                </div>
                <div class="ccol-md-6 col-sm-6 col-lg-4">
                    <div class="gallery-box">
                        <div class="img-thumb">
                            <img class="img-fluid" src="assets/img/gallery/img-4.jpg" alt="">
                        </div>
                        <div class="overlay-box text-center">
                            <a class="lightbox" href="assets/img/gallery/img-4.jpg">
                                <i class="icon-eye"></i>
                            </a>
                        </div>
                    </div>
                </div>
                <div class="ccol-md-6 col-sm-6 col-lg-4">
                    <div class="gallery-box">
                        <div class="img-thumb">
                            <img class="img-fluid" src="assets/img/gallery/img-5.jpg" alt="">
                        </div>
                        <div class="overlay-box text-center">
                            <a class="lightbox" href="assets/img/gallery/img-5.jpg">
                                <i class="icon-eye"></i>
                            </a>
                        </div>
                    </div>
                </div>
                <div class="ccol-md-6 col-sm-6 col-lg-4">
                    <div class="gallery-box">
                        <div class="img-thumb">
                            <img class="img-fluid" src="assets/img/gallery/img-6.jpg" alt="">
                        </div>
                        <div class="overlay-box text-center">
                            <a class="lightbox" href="assets/img/gallery/img-6.jpg">
                                <i class="icon-eye"></i>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!-- Gallary Section End -->

    <!-- Blog Section Start -->
    <section id="blog" class="section-padding">
        <div class="container">
            <div class="row">
                <div class="col-12">
                    <div class="section-title-header text-center">
                        <h2 class="section-title">From The Blog</h2>
                        <p>Lorem ipsum dolor sit amet, consectetur adipiscing <br> elit, sed do eiusmod tempor</p>
                    </div>
                </div>
                <div class="col-lg-4 col-md-6 col-xs-12">
                    <div class="blog-item text-center">
                        <div class="blog-image">
                            <a href="#">
                                <img class="img-fluid" src="assets/img/blog/img1.jpg" alt="">
                            </a>
                            <div class="date"><span class="day">12</span>June</div>
                        </div>
                        <div class="descr">
                            <p class="subtitle">By Korneila</p>
                            <h3 class="title">
                                <a href="single-blog.html">
                                    15 Best Outfit Ideas To Wear in Events
                                </a>
                            </h3>
                        </div>
                    </div>
                </div>
                <div class="col-lg-4 col-md-6 col-xs-12">
                    <div class="blog-item text-center">
                        <div class="blog-image">
                            <a href="#">
                                <img class="img-fluid" src="assets/img/blog/img2.jpg" alt="">
                            </a>
                            <div class="date"><span class="day">12</span>June</div>
                        </div>
                        <div class="descr">
                            <p class="subtitle">By Stuart</p>
                            <h3 class="title">
                                <a href="single-blog.html">
                                    10 Tips for Successful Business Networking
                                </a>
                            </h3>
                        </div>
                    </div>
                </div>
                <div class="col-lg-4 col-md-6 col-xs-12">
                    <div class="blog-item text-center">
                        <div class="blog-image">
                            <a href="#">
                                <img class="img-fluid" src="assets/img/blog/img3.jpg" alt="">
                            </a>
                            <div class="date"><span class="day">12</span>June</div>
                        </div>
                        <div class="descr">
                            <p class="subtitle">By John</p>
                            <h3 class="title">
                                <a href="single-blog.html">
                                    The 9 Design Trends You Need to Know
                                </a>
                            </h3>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!-- Blog Section End -->

    <!-- Subscribe Area Start -->
    <div id="subscribe" class="section-padding">
        <div class="container">
            <div class="row justify-content-md-center">
                <div class="col-md-10 col-lg-7">
                    <div class="subscribe-inner wow fadeInUp" data-wow-delay="0.2s">
                        <h2 class="subscribe-title">Get Realtime Event Updates</h2>
                        <p>You can signup for our newsletter to recieve updates related to jPrime.</p>
                        <form class="text-center form-inline" action="https://jprime.us13.list-manage.com/subscribe/post?u=968bc6e2a2348118b259c0df2&id=c9a8e30e18" method="POST" id="mc-embedded-subscribe-form" name="mc-embedded-subscribe-form" class="validate" target="_blank" novalidate>
                            <input class="mb-20 form-control" style="text-transform: none" name="EMAIL" placeholder="Your Email Here" required type="email">
                            <div class="icon-s"><i class="icon-envelope"></i></div>
                            <button type="submit" class="btn btn-common" data-style="zoom-in" data-spinner-size="30" name="submit" id="submit">
                                <span class="ladda-label">Submit</span>
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- Subscribe Area End -->

    <user:footer/>


</body>

</html>
