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
            <fieldset>
                <dl>
                    <dt><label for="title">Title</label></dt>
                    <dd><form:input path="title"/> <form:errors path="title"/></dd>
                </dl>
                <dl>
                    <dt><label for="description">Abstract</label></dt>
                    <dd><form:textarea path="description" style="width:80%" rows="5"/> <form:errors path="description"/></dd>
                </dl>
                <dl>
                    <dt><label for="level">Level</label></dt>
                    <dd><form:select path="level" items="${levels}"/> <form:errors path="level"/></dd>
                </dl>
                <dl>
                    <dt><label for="type">Type</label></dt>
                    <dd><form:select path="type" items="${sessionTypes}"/> <form:errors path="type"/></dd>
                </dl>
                <p>
                    <button type="submit" class="btn btn-common">Save</button>
                    <a href="<c:url value='/my'/>">Cancel</a>
                </p>
            </fieldset>
        </form:form>
        </c:when>
        <c:otherwise>
            <dl>
                <dt>Status</dt>
                <dd><c:out value="${submission.status}"/></dd>
            </dl>
            <dl>
                <dt>Title</dt>
                <dd><c:out value="${submission.title}"/></dd>
            </dl>
            <dl>
                <dt>Abstract</dt>
                <dd><c:out value="${submission.description}"/></dd>
            </dl>
            <dl>
                <dt>Level</dt>
                <dd><c:out value="${submission.level}"/></dd>
            </dl>
            <dl>
                <dt>Type</dt>
                <dd><c:out value="${submission.type}"/></dd>
            </dl>
            <p><a href="<c:url value='/my'/>">Back</a></p>
        </c:otherwise>
        </c:choose>
    </div>
</section>

<user:footer/>

</body>
</html>
