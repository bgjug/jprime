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
  <title>Register your participation in the raffle</title>
  
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
            <h2>Register your participation in the raffle</h2>
        </div>
    </div>
</div>
<!-- Page Banner End -->


<section id="about" class="section-padding">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <p>
                <p>Write your name and company(optional) exactly as <strong>in the badge !</strong></p>
                <form:form modelAttribute="visitor" method="post"
                           action="/qr/tuk" enctype="multipart/form-data">
                    <fieldset>
                        <p>
                        </p>
                        <dl>
                            <dt style="color:red">${message}</dt>
                        </dl>
                        <dl>
                            <dt>
                                <label for="name">Name</label>
                            </dt>
                            <dd>
                                <form:input path="name" />
                                <form:errors path="name"/>
                            </dd>
                        </dl>
                        <dl>
                            <dt>
                                <label for="company">Company</label>
                            </dt>
                            <dd>
                                <form:input path="company" />
                                <form:errors path="company"/>
                            </dd>
                        </dl>

                        <p>
                            <button type="submit">Submit</button>
                        </p>
                    </fieldset>

                </form:form>
            </div>
        </div>
    </div>
</section>


<user:footer/>



</body>
</html>
