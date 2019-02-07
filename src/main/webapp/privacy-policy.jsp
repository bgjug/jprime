<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
           uri="http://www.springframework.org/security/tags"%>
+<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user"%>

<!doctype html>
<!--[if IE 8 ]><html class="ie ie8" lang="en"> <![endif]-->
<!--[if (gte IE 9)|!(IE)]><html lang="en" class="no-js"> <![endif]-->
<html lang="en">
<head>

    <!-- Basic -->
    <title>Call for Papers</title>

    <!-- Define Charset -->
    <meta charset="utf-8">

    <!-- Responsive Metatag -->
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

    <jsp:directive.include file="theme-colors.jsp" />

    <!-- Page Description and Author -->
    <meta name="description" content="jPrime 2019">
    <meta name="author" content="jPrime">
    <user:pageJavaScriptAndCss/>

</head>
<body>

<!-- Container -->
<div id="container">

    <user:header/>

    <!-- Start Content -->
    <div id="content">
        <div class="container">
            <div class="row blog-post-page">
                <div class="col-md-9 blog-box">

                    <!-- Start Single Post Area -->
                    <div class="blog-post gallery-post">

                        <!-- Start Single Post Content -->
                        <div class="post-content">
                            <div class="fr-wrapper" dir="auto"><div class="fr-element fr-view" dir="auto" contenteditable="true" aria-disabled="false" spellcheck="true"><h1 dir="ltr" style="text-align: left;">Privacy policy</h1><p style="text-align: left;"><br></p><p dir="ltr" style="text-align: left;">This privacy policy covers the handling of personal data of people, registered for participation (speakers and attendees) and participate in the annual conference&nbsp;jPrime, as well as of the visitors of the site&nbsp;<a href="https://jprime.io">jprime.io</a>.</p><p style="text-align: left;"><br></p><p dir="ltr" style="text-align: left;">The entity responsible for handling personal data under the terms of this statement is as follows:&nbsp;“jPrime Events” Ltd., VAT number 203915037.&nbsp;</p><p style="text-align: left;"><br></p><p dir="ltr" style="text-align: left;">In case that you need more information about handling of your personal data, you may contact us on the contacts mentioned below</p><p style="text-align: left;"><br></p><p dir="ltr" style="text-align: left;">Personal data administrator: “jPrime Events Ltd”</p><p dir="ltr" style="text-align: left;">Address:&nbsp;&nbsp;Sofia, Postal Code 1574, r.d. Hristo Smirnenski, bl. 20, entr. B, fl. 6, apt. 20</p><p dir="ltr" style="text-align: left;">Email:&nbsp;conference@jprime.io</p><p dir="ltr" style="text-align: left;">Person to contact:&nbsp;Ivan St. Ivanov</p><h2 dir="ltr" style="text-align: left;">Purposes for processing personal data</h2><p><br></p><p dir="ltr" style="text-align: left;">Personal data is processed in order to serve the following purposes:</p><p style="text-align: left;"><br></p><ul><li dir="ltr"><p dir="ltr" style="text-align: left;">Registration of conference attendees</p></li><li dir="ltr"><p dir="ltr" style="text-align: left;">Selection of speakers and providing information about the made choice</p></li><li dir="ltr"><p dir="ltr" style="text-align: left;">Organizing of payment of the attendance fee and issuing of invoice for that</p></li><li dir="ltr"><p dir="ltr" style="text-align: left;">Organization and conducting the conference</p></li><li dir="ltr"><p dir="ltr" style="text-align: left;">Coverage of the event via publishing of picture on the website and in social media</p></li><li dir="ltr"><p dir="ltr" style="text-align: left;">Sending information about upcoming events organized by the Bulgarian Java User Groups</p></li></ul><p style="text-align: left;"><br></p><p dir="ltr" style="text-align: left;">We&nbsp;do not process&nbsp;personal data, included in the scope of the special categories of personal data, referet to also as “sensitive”: personal data disclosing racial or ethnic origin, political views, religious or philosophical beliefs.</p><p style="text-align: left;"><br></p><p dir="ltr" style="text-align: left;">Processing of pictures of registered attendees and speakers as well as their publishing on the conference website is not performed with special technical means and thus is not considered as biometric data processing.</p><h2 dir="ltr" style="text-align: left;">Duration of storage of personal data</h2><p><br></p><p dir="ltr" style="text-align: left;">Personal data will be deleted one year after the conference is held unless the law requires storing of personal data or the registrant requires explicitly the deletion of his/her personal data.</p><p style="text-align: left;"><br></p><p dir="ltr" style="text-align: left;">Personal data is stored accordingly in a proper database.</p><h2 dir="ltr" style="text-align: left;">Rights on personal data</h2><p dir="ltr" style="text-align: left;">Should you wish to receive more detailed information about the processing of your personal data or raise concerns you can send an email to&nbsp;<a href="mailto:conference@jprime.io">conference@jprime.io</a>. In particular queries can be raised about:</p><ul><li dir="ltr"><p dir="ltr" style="text-align: left;">reason for storing personal data;</p></li><li dir="ltr"><p dir="ltr" style="text-align: left;">what attributes of that data are stored and processed (such as names, e-mail etc.);</p></li><li dir="ltr"><p dir="ltr" style="text-align: left;">how is personal data provided for the purposes of the jPrime conference.</p></li></ul><p style="text-align: left;"><br></p><p dir="ltr" style="text-align: left;">In addition it is possible to request correction on the personal data being processed.</p><p style="text-align: left;"><br></p><p style="text-align: left;"><br></p><h2 class="text-light" style="text-align: left;"><br></h2></div></div>
                        </div>
                        <!-- End Single Post Content -->

                    </div>
                    <!-- End Single Post Area -->
                </div>



                <user:sidebar/>

            </div>

        </div>
    </div>
    <!-- End content -->


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