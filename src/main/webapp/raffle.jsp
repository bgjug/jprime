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
    <title>Raffle</title>

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
            <h2>Raffle</h2>
        </div>
    </div>
</div>
<!-- Page Banner End -->


<section id="about" class="section-padding">
    <div class="container">
        <div class="row">
            <div class="col-md-7 col-md-offset-5">
                <p>
                <a href="javascript:void(0)" onclick="shuffle(this)" class="slider btn btn-primary">Start</a>
                </p> <br/>
                <h1 id="shuffle-result">Name (Company)</h1>
            </div>
        </div>
    </div>
</section>


<user:footer/>

<script type="text/javascript">

var shuffling = false;
var visitors = ${visitors};

function shuffle(el){
    shuffling = !shuffling;
    if(shuffling) {
        $(el).text("Stop");
        setTimeout(getRandomVisitor, 100);
    } else {
        $(el).text("Start");
    }
}

function getRandomVisitor(){
    var visitor = visitors[Math.floor(Math.random()*visitors.length)];
    $("#shuffle-result").text(" " + visitor.name + " (" + visitor.company + ")" )
    if(shuffling) {
        setTimeout(getRandomVisitor, 100);
    }
}
</script>

</body>
</html>
