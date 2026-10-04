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
  <title>My talks</title>
  
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
            <h2>My talks</h2>
        </div>
    </div>
</div>
<!-- Page Banner End -->


<section class="section-padding">
    <div class="container">
        <h3><c:out value="${speaker.firstName} ${speaker.lastName}"/></h3>
        <p><c:out value="${speaker.headline}"/></p>
        <p><a href="<c:url value='/my/profile'/>">Edit profile</a> |
            <a href="<c:url value='/cfp'/>">Submit a talk</a></p>
        <table class="table">
            <thead><tr><th>Title</th><th>Type</th><th>Level</th><th>Co-speaker</th><th>Status</th><th></th></tr></thead>
            <tbody>
            <c:forEach items="${submissions}" var="s">
                <tr>
                    <td><c:out value="${s.title}"/></td>
                    <td><c:out value="${s.type}"/></td>
                    <td><c:out value="${s.level}"/></td>
                    <td><c:out value="${s.coSpeaker.name}"/></td>
                    <td><c:out value="${s.status}"/></td>
                    <td><a href="<c:url value='/my/submissions/${s.id}'/>">${editable[s.id] ? 'Edit' : 'View'}</a></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</section>

<user:footer/>

</body>
</html>
