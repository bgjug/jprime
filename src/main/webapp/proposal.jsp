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
  <title>Submit a proposal</title>
  
  <!-- Define Charset -->
  <meta charset="utf-8">
  
  <!-- Responsive Metatag -->
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  
  <!-- Page Description and Author -->
    <meta name="description" content="jPrime 2018">
    <meta name="author" content="jPrime">

    <user:pageJavaScriptAndCss/>
    <script type="text/javascript">
        $(document).ready(function () {
            var coSpeakerButton = $("#toggleCoSpeaker");
            var buttonCaption = coSpeakerButton.text();
            buttonCaption == "Add co speaker" ? $("#coSpeaker").hide() : $("#coSpeaker").show();

            coSpeakerButton.click(function () {
                var coSpeakerDiv = $("#coSpeaker");
                coSpeakerDiv.toggle();
                var coSpeakerButton = $("#toggleCoSpeaker");
                var buttonCaption = coSpeakerButton.text();

                if (buttonCaption == "Remove co speaker") {
                    coSpeakerButton.text("Add co speaker");
                    $("input[id^='coSpeaker']").val("");
                } else {
                    coSpeakerButton.text("Remove co speaker");
                }
            });
        });
    </script>
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
        <h2>Submit your proposal</h2>
        <br>Call for paper closes on <strong>1st of March!</strong>
        <p>
        <user:cfp action="/cfp"/>
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