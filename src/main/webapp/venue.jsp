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
    <title>jPrime | Venue</title>

    <meta charset="utf-8">

    <!-- Responsive Metatag -->
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <%--    <jsp:directive.include file="theme-colors.jsp" />--%>

    <!-- Page Description and Author -->

    <user:pageJavaScriptAndCss/>

</head>
<body>

<user:header/>

<!-- Page Banner Start -->
<div id="page-banner-area" class="page-banner">
    <div class="page-banner-title">
        <div class="text-center">
            <h2>The Venue !</h2>
        </div>
    </div>
</div>
<!-- Page Banner End -->

<section id="about" class="section-padding">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <p>
                <p class="text-center"><br/> jPrime will be held on <strong>30-31st May 2023</strong> in
                    "John Atanasoff" Innovation forum in Sofia Tech Park which is one of the leading and most preferable locations for running events connected with hi tech, entrepreneurship, science, ecology, education, innovations, digitalisation and health care.
                </p>
                <center>
                    <img alt="techpark" src="/images/venue.jpg" width="100%"/><br/><br/>
                </center>
                <center>
                    <iframe src="https://www.google.com/maps/embed?pb=!1m14!1m8!1m3!1d11734.949903249553!2d23.3733712!3d42.6669183!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x0%3A0xcbc66ddbb54d08b3!2z0KHQvtGE0LjRjyDRgtC10YUg0L_QsNGA0Lo!5e0!3m2!1sru!2sbg!4v1494940078038" width="100%" height="450" frameborder="0" style="border:0" allowfullscreen></iframe>
                </center>
                <a name="info"></a>
                <p class="text-center"><br/> <strong>Useful</strong>&nbsp;information</p>
                <div id="content">
                    <div class="container" style="vertical-align: top">
                        <div class="row" style="display: inline-block;max-width:33%;padding: 20px;vertical-align: top">
                            <div class="team-member modern">

                                <img alt="" src="/images/bulgaria.png"/><br/><br/>

                                <div class="member-name"><b>Bulgaria</b><br/><br/>

                                    <b>County entrance</b>: EU citizens do not require visa. Free entrance for Schengen visa holders. For other countries check <a href="https://visaguide.world/europe/bulgaria-visa/">here</a><br/>
                                    <b>Local curency</b>: Bulgarian Lev. Fixed rate 1,95583 Lev/Euro<br/>
                                    <br/>
                                    More info: <a href="https://en.wikipedia.org/wiki/Bulgaria">in Wikipedia</a>
                                </div>
                            </div>
                        </div>

                        <div class="row" style="display: inline-block;max-width:33%;padding: 20px;vertical-align: top">
                            <div class="team-member modern">

                                <img alt="" src="/images/apt.png"/><br/><br/>

                                <div class="member-name"><b>Transport</b><br/><br/>
                                    <b>Conference hall address</b>: Tsarigradsko Shosse 115B, Sofia, Bulgaria<br/>
                                    Sofia Tech Park is located 6 km away from the Sofia International Airport.<br/>
                                    It usually takes up to 10 min to reach the venue with a car.<br/>
                                    <br/>
                                    Average taxi price <b>0,40/0,45</b> Eurocent per km.
                                </div>
                            </div>
                        </div>

                        <div class="row" style="display: inline-block;max-width:33%;padding: 20px;vertical-align: top">
                            <div class="team-member modern">

                                <img alt="" src="/images/hotel.png"/><br/><br/>

                                <div class="member-name"><b>Accomodation</b><br/><br/>

                                    For the conference guests we recommend <a href="http://www.hotel-europe-bg.com/en/">Best Western Hotel Europe</a> which is centrally located. <br/>
                                    <br/>
                                    <br/>
                                    For more information please contact <a href="mailto:ivan@jprime.io">Ivan</a>.
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<user:footer/>
<script src="assets/js/map.js"></script>
<script type="text/javascript" src="//maps.googleapis.com/maps/api/js?key=AIzaSyCsa2Mi2HqyEcEnM1urFSIGEpvualYjwwM"></script>

</body>
</html>
