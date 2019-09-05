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
    <title>jPrime | Team</title>

    <!-- Define Charset -->
    <meta charset="utf-8">

    <!-- Responsive Metatag -->
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

    <jsp:directive.include file="theme-colors.jsp" />

    <!-- Page Description and Author -->
    <meta name="description" content="JPrime Conference">
    <meta name="author" content="JPrime">
	
    <user:pageJavaScriptAndCss/>

</head>
<body>

<user:header/>

<!-- Page Banner Start -->
<div id="page-banner-area" class="page-banner">
    <div class="page-banner-title">
        <div class="text-center">
            <h2>The Team</h2>
        </div>
    </div>
</div>
<!-- Page Banner End -->
<div class="container">

    <div class="row">
        <div class="col-md-4 col-sm-6 col-xs-12">
            <div class="team-item text-center">
                <div class="team-img">
                    <img class="img-fluid" src="/images/team/ivan.jpg" alt="">
                    <div class="team-overlay">
                        <div class="overlay-social-icon text-center">
                            <ul class="social-icons">
                                <li><a href="https://twitter.com/ivan_stefanov"><i class="fa fa-twitter" aria-hidden="true"></i></a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="info-text">
                    <h3>Ivan&nbsp;St.&nbsp;Ivanov</h3>
                    <p></p>
                </div>
                <div style="text-align: left; padding: 10px 10px 10px 10px">
                    Ivan St. Ivanov is co-founder and senior programmer at VIDA Software, doing various consulting gigs and teaching a couple of Java and Microservices related courses. He is active JUG member, driving the Adopt-a-JSR programme in Bulgaria. In his free time he likes contributing to open source software. As such he is proud contributor to the MicroProfile.

                    Ivan is doing his PhD in the area of cloud multi-tenancy in the University of National and World Economy. He is also teaching Java and Java EE in the Sofia University.<br /><a href="http://nosoftskills.com/">nosoftskills.com</a>
                </div>
            </div>
        </div>
        <div class="col-md-4 col-sm-6 col-xs-12">
            <div class="team-item text-center">
                <div class="team-img">
                    <img class="img-fluid" src="/images/team/mitia.jpg" alt="">
                    <div class="team-overlay">
                        <div class="overlay-social-icon text-center">
                            <ul class="social-icons">
                                <li><a href="https://twitter.com/bercut2000"><i class="fa fa-twitter" aria-hidden="true"></i></a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="info-text">
                    <h3>Dmitry&nbsp;Aleksandrov</h3>
                    <p></p>
                </div>
                <div style="text-align: left; padding: 10px 10px 10px 10px">Dmitry is a passionate Java developer. Through his 10+ years career he has gained a huge experience with different web technologies. His areas of interest include the wide range of Java-related technologies, enterprise solutions, cloud computing technologies, Eclipse plug-ins, as well as non-Java like NodeJs and NoSql.
                    He’s big enthusiast of distributed multinational, multi location software development. At his free time he’s trying to contribute to OpenJDK and supports his own opensource project. His latest passion is Oracles Javascript runtime on JVM – Nashorn.<br /><a href="http://dmitryalexandrov.net">dmitryalexandrov.net</a></div>
            </div>
        </div>
        <div class="col-md-4 col-sm-6 col-xs-12">
            <div class="team-item text-center">
                <div class="team-img">
                    <img class="img-fluid" src="/images/team/martin.jpg" alt="">
                    <div class="team-overlay">
                        <div class="overlay-social-icon text-center">
                            <ul class="social-icons">
                                <li><a href="https://twitter.com/mihailstoynov"><i class="fa fa-twitter" aria-hidden="true"></i></a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="info-text">
                    <h3>Martin&nbsp;Toshev</h3>
                    <p></p>
                </div>
                <div style="text-align: left; padding: 10px 10px 10px 10px">Martin is a Java enthusiast. He is a graduate of Computer Science from the University of Sofia. He is also a certified Java professional (SCJP6) and a certified IBM cloud computing solution advisor. His areas of interest include the wide range of Java-related technologies (such as Servlets, JSP, JAXB, JAXP, JMS, JMX, JAX-RS, JAX-WS, Hibernate, Spring Framework, Liferay Portal and Eclipse RCP), cloud computing technologies, cloud-based software architectures, enterprise application integration, relational and NoSQL databases. You can reach him for any Java and FOSS-related topics (especially Eclipse and the OpenJDK).<br /><a href="http://martin-toshev.com/">martin-toshev.com/</a></div>
            </div>
        </div>
        <div class="col-md-4 col-sm-6 col-xs-12">
            <div class="team-item text-center">
                <div class="team-img">
                    <img class="img-fluid" src="/images/team/mihail.jpg" alt="">
                    <div class="team-overlay">
                        <div class="overlay-social-icon text-center">
                            <ul class="social-icons">
                                <li><a href="https://twitter.com/martin_fmi"><i class="fa fa-twitter" aria-hidden="true"></i></a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="info-text">
                    <h3>Mihail&nbsp;Stoynov</h3>
                    <p></p>
                </div>
                <div style="text-align: left; padding: 10px 10px 10px 10px">Mihail is a security and software consultant, trainer and author. His resume includes projects in companies like Saudi Aramco, Boeing, HP, Siemens, USAF, several foreign banks and government entities. Mihail is the co-author of 6 books on software, and has 10 years of training experience in local and foreign companies and most of the local universities.<br /><a href="https://mihail.stoynov.com">mihail.stoynov.com</a></div>
            </div>
        </div>
        <div class="col-md-4 col-sm-6 col-xs-12">
            <div class="team-item text-center">
                <div class="team-img">
                    <img class="img-fluid" src="/images/team/nayden.jpg" alt="">
                    <div class="team-overlay">
                        <div class="overlay-social-icon text-center">
                            <ul class="social-icons">
                                <li><a href="https://twitter.com/gochev"><i class="fa fa-twitter" aria-hidden="true"></i></a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="info-text">
                    <h3>Nayden Gochev</h3>
                    <p>The Java.Beer guy</p>
                </div>
                <div style="text-align: left; padding: 10px 10px 10px 10px">Nayden Gochev is Solution Architect at Nemesis Software with with more than 14 years of experience. Developer, Trainer and consultant in many international companies and Bulgarian companies. He took part in many conferences, seminars, technical trainings in multiple academic and non academic structures and companies. One of the organizers of jPrime and jProfessionals conferences and the monthly Bulgarian Java User Group seminars. Huge Java fan with love for all *.java and big community supporter. His personal blog can be found at <a href="http://gochev.blogspot.com">gochev.blogspot.com</a></div>
            </div>
        </div>
        <div class="col-md-4 col-sm-6 col-xs-12">
            <div class="team-item text-center">
                <div class="team-img">
                    <img class="img-fluid" src="/images/team/doychin.png" alt="">
                    <div class="team-overlay">
                        <div class="overlay-social-icon text-center">
                            <ul class="social-icons">
                                <li><a href="https://twitter.com/dbondzhev"><i class="fa fa-twitter" aria-hidden="true"></i></a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="info-text">
                    <h3>Doychin&nbsp;Bondzhev</h3>
                    <p>The Guy from Plovdiv</p>
                </div>
                <div style="text-align: left; padding: 10px 10px 10px 10px">Doychin Bondzhev is Java developer with experience in many technologies. His work includes software for different businesses like telecommunications, warehouse management, point of sale, service management, billing , service provisioning, customer support, banking and many more. In his free time he contributes to some Open Source projects. He is a big fan of JavaEE and Microprofile.</div>
            </div>
        </div>
    </div>
</div>

<user:footer/>

</body>
</html>
