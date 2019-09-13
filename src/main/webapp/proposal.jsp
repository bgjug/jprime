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
            <h2>Submit your proposal</h2>
        </div>
    </div>
</div>
<!-- Page Banner End -->


<section id="about" class="section-padding">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <p>
                <p>Call for paper closes on <strong>15th of February!</strong></p>
                <user:cfp action="/cfp"/>
            </div>
        </div>
    </div>
</section>


<user:footer/>

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

</body>
</html>
