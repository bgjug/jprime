<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
           uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user"%>

<!doctype html>
<!--[if IE 8 ]><html class="ie ie8" lang="en"> <![endif]-->
<!--[if (gte IE 9)|!(IE)]><html lang="en" class="no-js"> <![endif]-->
<html lang="en">
<head>

    <!-- Basic -->
    <title>Buy conference tickets</title>

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
                            <!-- 
                            <h2>Buy conference tickets</h2>
                            <p>
                            <p>The conference fee is <strong>140</strong>.00 BGN (VAT included).</p>
                            <p>For registration contact us at <a href="mailto:conference@jprime.io">conference@jprime.io</a>.</p>
                            <p>* There is a ~25% discount for students. Student ticket price <strong>100</strong>.00 BGN (VAT included).</p>
                            <p>* There is a free pass for a JUG lead (one per Java User Group).</p>

 							-->
                             <h2>Registration is closed</h2>
                             <p>
                             	<p>The registration is currently closed, see you in 2019 :)</p>
                             	<p>Note that the recordings of the previous conference are available at <a href="https://www.youtube.com/playlist?list=PLcqA4DRMgIYvGPZfrK0EcMxEQCrtSl9A3">https://www.youtube.com/playlist?list=PLcqA4DRMgIYvGPZfrK0EcMxEQCrtSl9A3</a> .</p>
                             	<p>The tickets registration for 2019 will be opened at the beginning of 2019.</p>
                             </p>
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

</body>
</html>