<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
           uri="http://www.springframework.org/security/tags"%>
+<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user"%>

<!doctype html>
<html lang="en">
<head>

    <!-- Basic -->
    <title>Call for Papers</title>

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
            <h2>Register your presence for the raffle!</h2>
        </div>
    </div>
</div>
<!-- Page Banner End -->


<section id="about" class="section-padding">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <p>
                    Scan the QR image:
                </p>
                <p>
                    <img alt="qr" title="qr" src="images/qr.png"/>
                </p>
                <p> or open <a href="https://jprime.io/qr/tuk">https://jprime.io/qr/tuk</a>
                </p>
            </div>
        </div>
    </div>
</section>


<user:footer/>

</body>
</html>
