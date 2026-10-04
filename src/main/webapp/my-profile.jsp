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
  <title>My profile</title>
  
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
            <h2>My profile</h2>
        </div>
    </div>
</div>
<!-- Page Banner End -->


<section class="section-padding">
    <div class="container">
        <form:form modelAttribute="profile" method="post" enctype="multipart/form-data">
            <sec:csrfInput/>
            <p>Email: <c:out value="${email}"/></p>
            <p><label>First name <form:input path="firstName"/></label> <form:errors path="firstName"/></p>
            <p><label>Last name <form:input path="lastName"/></label> <form:errors path="lastName"/></p>
            <p><label>Headline <form:input path="headline"/></label></p>
            <p><label>Bio <form:textarea path="bio" rows="6"/></label> <form:errors path="bio"/></p>
            <p><label>Twitter <form:input path="twitter"/></label></p>
            <p><label>Bluesky <form:input path="bsky"/></label></p>
            <p><label>Photo <input type="file" name="picture" accept="image/*"/></label> <form:errors/></p>
            <button type="submit" class="btn btn-common">Save</button>
            <a href="<c:url value='/my'/>">Cancel</a>
        </form:form>
    </div>
</section>

<user:footer/>

</body>
</html>
