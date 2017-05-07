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

    <!-- Page Description and Author -->
    <meta name="description" content="JPrime Conference">
    <meta name="author" content="JPrime">
	<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>
	
    <user:pageJavaScriptAndCss/>

</head>
<body>

<!-- Container -->
<div id="container">

    <user:header/>

    <!-- Start Content -->
    <div id="content">
        <div class="container">

            <div class="big-title text-center" data-animation="fadeInDown" data-animation-delay="01">
                <h1>The <strong>Team</strong></h1>
            </div>
            <div class="row">
                    <div class="col-md-3 col-sm-6 col-xs-12" style="width: 230px">
                        <div class="team-member modern">
                            <!-- Memebr Photo, Name & Position -->
                            <div class="member-photo">
                                <img alt="" src="/images/team/ivan.jpg"/>

                                <div class="member-name">Ivan&nbsp;St.&nbsp;Ivanov
                                </div>
                            </div>
                            <div class="member-socail" style="text-align: left">
                                <a class="twitter" href="http://twitter.com/ivan_stefanov"><i
                                        class="fa fa-twitter"></i></a>
                            </div>
                            Ivan St. Ivanov is co-founder and senior programmer at VIDA Software, doing various consulting gigs and teaching a couple of Java and Microservices related courses. He is active JUG member, driving the Adopt-a-JSR programme in Bulgaria. In his free time he likes contributing to open source software. As such he is proud contributor to the MicroProfile.

                            Ivan is doing his PhD in the area of cloud multi-tenancy in the University of National and World Economy. He is also teaching Java and Java EE in the Sofia University.<br /><a href="http://nosoftskills.com/">nosoftskills.com</a>
                        </div>
                    </div>
                <div class="col-md-3 col-sm-6 col-xs-12" style="width: 230px">
                    <div class="team-member modern">
                        <!-- Memebr Photo, Name & Position -->
                        <div class="member-photo">
                            <img alt="" src="/images/team/mitia.jpg"/>

                            <div class="member-name">Dmitry&nbsp;Aleksandrov
                            </div>
                        </div>
                        <div class="member-socail" style="text-align: left">
                            <a class="twitter" href="http://twitter.com/bercut2000"><i
                                    class="fa fa-twitter"></i></a>
                        </div>
                        Dmitry is a passionate Java developer. Through his 10+ years career he has gained a huge experience with different web technologies. His areas of interest include the wide range of Java-related technologies, enterprise solutions, cloud computing technologies, Eclipse plug-ins, as well as non-Java like NodeJs and NoSql.
                        He’s big enthusiast of distributed multinational, multi location software development. At his free time he’s trying to contribute to OpenJDK and supports his own opensource project. His latest passion is Oracles Javascript runtime on JVM – Nashorn.<br /><a href="http://dmitryalexandrov.net">dmitryalexandrov.net</a>
                    </div>
                </div>
                <div class="col-md-3 col-sm-6 col-xs-12" style="width: 230px">
                    <div class="team-member modern">
                        <!-- Memebr Photo, Name & Position -->
                        <div class="member-photo">
                            <img alt="" src="/images/team/mihail.jpg"/>

                            <div class="member-name">Mihail&nbsp;Stoynov
                            </div>
                        </div>
                        <div class="member-socail" style="text-align: left">
                            <a class="twitter" href="https://twitter.com/mihailstoynov"><i
                                    class="fa fa-twitter"></i></a>
                        </div>
                        Mihail is a security and software consultant, trainer and author. His resume includes projects in companies like Saudi Aramco, Boeing, HP, Siemens, USAF, several foreign banks and government entities. Mihail is the co-author of 6 books on software, and has 10 years of training experience in local and foreign companies and most of the local universities.<br /><a href="https://mihail.stoynov.com">mihail.stoynov.com</a>
                    </div>
                </div>
                <div class="col-md-3 col-sm-6 col-xs-12" style="width: 230px">
                    <div class="team-member modern">
                        <!-- Memebr Photo, Name & Position -->
                        <div class="member-photo">
                            <img alt="" src="/images/team/martin.jpg"/>

                            <div class="member-name">Martin&nbsp;Toshev
                            </div>
                        </div>
                        <div class="member-socail" style="text-align: left">
                            <a class="twitter" href="https://twitter.com/mihailstoynov"><i
                                    class="fa fa-twitter"></i></a>
                        </div>
                        Martin is a Java enthusiast. He is a graduate of Computer Science from the University of Sofia. He is also a certified Java professional (SCJP6) and a certified IBM cloud computing solution advisor. His areas of interest include the wide range of Java-related technologies (such as Servlets, JSP, JAXB, JAXP, JMS, JMX, JAX-RS, JAX-WS, Hibernate, Spring Framework, Liferay Portal and Eclipse RCP), cloud computing technologies, cloud-based software architectures, enterprise application integration, relational and NoSQL databases. You can reach him for any Java and FOSS-related topics (especially Eclipse and the OpenJDK).<br /><a href="http://martin-toshev.com/">martin-toshev.com/</a>
                    </div>
                </div>
                <div class="col-md-3 col-sm-6 col-xs-12" style="width: 230px">
                    <div class="team-member modern">
                        <!-- Memebr Photo, Name & Position -->
                        <div class="member-photo">
                            <img alt="" src="/images/team/nayden.jpg"/>

                            <div class="member-name">Nayden&nbsp;Gochev
                            </div>
                        </div>
                        <div class="member-socail" style="text-align: left">
                            <a class="twitter" href="https://twitter.com/gochev"><i
                                    class="fa fa-twitter"></i></a>
                        </div>
                        Nayden Gochev is Solution Architect at Nemesis Software with with more than 12 years of experience. Developer, Trainer and consultant in many international companies and Bulgarian companies. He took part in many conferences, seminars, technical trainings in multiple academic and non academic structures and companies. One of the organizers of jPrime and jProfessionals conferences and the monthly Bulgarian Java User Group seminars. Huge Java fan with love for all *.java and big community supporter. His personal blog can be found at <a href="http://gochev.org">gochev.org</a>
                    </div>
                </div>
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