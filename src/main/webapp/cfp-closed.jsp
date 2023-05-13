<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec"
           uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>

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
            <h2>Call for Papers is closed</h2>
        </div>
    </div>
</div>
<!-- Page Banner End -->

<section id="about" class="section-padding">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <p>
                    <c:if test="${agenda}">You can check the agenda for this year <a
                            href="/agenda">here</a></c:if>
                    <c:if test="${not agenda}">Stay tuned for the agenda. It will be published soon.</c:if>
                </p>
                <p>You can also <a href="/tickets">register</a> as attendee.</p>
            </div>
        </div>
    </div>
</section>

<user:footer/>

</body>
</html>
