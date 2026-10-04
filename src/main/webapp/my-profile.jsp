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
            <fieldset>
                <dl>
                    <dt>Email</dt>
                    <dd><c:out value="${email}"/></dd>
                </dl>
                <dl>
                    <dt><label for="firstName">First name</label></dt>
                    <dd><form:input path="firstName"/> <form:errors path="firstName"/></dd>
                </dl>
                <dl>
                    <dt><label for="lastName">Last name</label></dt>
                    <dd><form:input path="lastName"/> <form:errors path="lastName"/></dd>
                </dl>
                <dl>
                    <dt><label for="headline">Headline</label></dt>
                    <dd><form:input path="headline"/> <form:errors path="headline"/></dd>
                </dl>
                <dl>
                    <dt><label for="bio">Bio</label></dt>
                    <dd><form:textarea path="bio" style="width:80%" rows="5"/> <form:errors path="bio"/></dd>
                </dl>
                <dl>
                    <dt><label for="twitter">Twitter</label></dt>
                    <dd><form:input path="twitter"/> <form:errors path="twitter"/></dd>
                </dl>
                <dl>
                    <dt><label for="bsky">Bluesky Profile</label></dt>
                    <dd><form:input path="bsky"/> <form:errors path="bsky"/></dd>
                </dl>
                <dl>
                    <dt><label for="picture">Photo</label></dt>
                    <dd><input id="picture" type="file" name="picture" accept="image/*"/> <form:errors/></dd>
                </dl>
                <p>
                    <button type="submit" class="btn btn-common">Save</button>
                    <a href="<c:url value='/my'/>">Cancel</a>
                </p>
            </fieldset>
        </form:form>
    </div>
</section>

<user:footer/>

</body>
</html>
