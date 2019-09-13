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
  <title>Conference tickets</title>

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
      <h2>Thank you for registering for jPrime!</h2>
    </div>
  </div>
</div>
<!-- Page Banner End -->

<section id="about" class="section-padding">
  <div class="container">
    <div class="row">
      <div class="col-12">
        <p>
          <c:if test="${result}">
            <h2>Thank you for registering for jPrime!</h2>
            <br/><br/><br/>
          </c:if>
          <c:if test="${not result}">
            <h2>There was a problem for purchasing the tickets</h2>
            <br/>
            <a href="/tickets">Try again?</a>
            <br/><br/><br/>
          </c:if>
        <p>In case of questions, contact us at <a href="mailto:conference@jprime.io">conference@jprime.io</a>.</p>
        </p>
      </div>
    </div>
  </div>
</section>

<user:footer/>

</body>
</html>
