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
            <h2>Thank you for your submission!</h2>
        </div>
    </div>
</div>
<!-- Page Banner End -->


<section id="about" class="section-padding">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <p>
                <p>Stay tuned for contact from our side. The CFP closes 15th of February.</p>
                <p>Usually after that there is voting in place for about 2 weeks. You should be contacted in the beginning of March ! <br/>
                    For any questions please mail us to <a href="mailto:conference@jprime.io"><i class="fa fa-envelope-o"></i> conference@jprime.io</a></p>

                <p>
                    And lastly - right now you cannot edit a submission that you have already submitted. This functionality is not yet developed however if you need to do so.. you can contact us, or open a PR
                    <a href="https://github.com/bgjug/jprime/issues/120">https://github.com/bgjug/jprime/issues/120</a> ;)
                </p>
            </div>
        </div>
    </div>
</section>


<user:footer/>

</body>
</html>
