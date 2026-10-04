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
  <title>My submission</title>
  
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
            <h2>My submission</h2>
        </div>
    </div>
</div>
<!-- Page Banner End -->


<section class="section-padding">
    <div class="container">
        <c:choose>
        <c:when test="${editable}">
        <form:form modelAttribute="form" method="post">
            <sec:csrfInput/>
            <p><label>Title <form:input path="title"/></label> <form:errors path="title"/></p>
            <p><label>Description <form:textarea path="description" rows="10"/></label> <form:errors path="description"/></p>
            <p><label>Level <form:select path="level" items="${levels}"/></label></p>
            <p><label>Type <form:select path="type" items="${sessionTypes}"/></label></p>
            <button type="submit" class="btn btn-common">Save</button>
            <a href="<c:url value='/my'/>">Cancel</a>
        </form:form>
        </c:when>
        <c:otherwise>
            <p>Status: <c:out value="${submission.status}"/></p>
            <p>Title: <c:out value="${submission.title}"/></p>
            <p>Description: <c:out value="${submission.description}"/></p>
            <p>Level: <c:out value="${submission.level}"/></p>
            <p>Type: <c:out value="${submission.type}"/></p>
            <a href="<c:url value='/my'/>">Back</a>
        </c:otherwise>
        </c:choose>
    </div>
</section>

<user:footer/>

</body>
</html>
