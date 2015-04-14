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
  <title>Buy conference tickets</title>
  
  <!-- Define Charset -->
  <meta charset="utf-8">
  
  <!-- Responsive Metatag -->
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  
  <!-- Page Description and Author -->
  <meta name="description" content="Margo - Responsive HTML5 Template">
  <meta name="author" content="ZoOm Arts">
    <base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>

    <user:pageJavaScriptAndCss/>

</head>
<body>

	<!-- Container -->
	<div id="container">

        <user:header/>

<!-- Start Content -->
<div id="content">
 <div class="container">
  <div class="row blog-post-page">
   <div class="col-md-9 blog-box">

    <!-- Start Single Post Area -->
    <div class="blog-post gallery-post">
	
    <!-- Start Single Post Content -->
    <div class="post-content">
        <h2>Buy conference tickets</h2>
        <p>
        <p>The conference fee is <strong>100</strong>.00 BGN (VAT included).</p>

        Buy a ticket:

        <form>
            Kolko visitora batka?
            <input name="visitorsCount" value="${param.visitorsCount}"/>
            <input type="submit" value="tolkova"/>
        </form>

        <form:form commandName="registrant" method="post"
                   action="/tickets/register">
            <p>
                <form:errors/>
            </p>
            <fieldset>
                <legend>Visitors</legend>
            <c:choose>
                <c:when test="${empty param.visitorsCount}">
                    <dl>
                        <dt><label for="visitors.name">Visitor name</label></dt>
                        <dd><form:input path="visitors[0].name"/></dd>
                    <%--</dl>--%>
                    <%--<dl>--%>
                        <dt><label for="visitors.email">Visitor email</label></dt>
                        <dd><form:input path="visitors[0].email" /></dd>
                    </dl>
                </c:when>
                <c:otherwise>
                    <c:forEach varStatus="i" begin="0" end="${param.visitorsCount - 1}">
                        <dl>
                            <dt><label for="visitors.name">Visitor name</label></dt>
                            <dd><form:input path="visitors[${i.count -1}].name"/></dd>
                        </dl>
                        <dl>
                            <dt><label for="visitors.email">Visitor email</label></dt>
                            <dd><form:input path="visitors[${i.count -1}].email" /></dd>
                        </dl>
                    </c:forEach>
                </c:otherwise>
            </c:choose>


            </fieldset>

            <fieldset>
                <legend>Invoice information</legend>
                <dl>
                    <dt><label for="name">Name</label></dt>
                    <dd><form:input path="name" /></dd>
                </dl>
                <dl>
                    <dt><label for="address">Address</label></dt>
                    <dd><form:input path="address" /></dd>
                </dl>
                <dl>
                    <dt><label for="vatNumber">VAT</label></dt>
                    <dd><form:input path="vatNumber" /></dd>
                </dl>
                <dl>
                    <dt><label for="mol">MOL</label></dt>
                    <dd><form:input path="mol" /></dd>
                </dl>
                <dl>
                    <dt><label for="email">email</label></dt>
                    <dd><form:input path="email" /></dd>
                </dl>
                <sec:csrfInput/>
                <form:hidden path="id"/>
                <button type="submit">Save</button>
            </fieldset>

        </form:form>



        <%--<form>--%>
            <%--<input type="text" name="name" placeholder="Name of the registrant"><br>--%>
            <%--<input type="text" name="name" placeholder="Name of the registrant"><br>--%>
            <%--<input type="text" name="name" placeholder="Name of the registrant"><br>--%>
            <%--<input type="text" name="name" placeholder="Name of the registrant"><br>--%>
            <%--<input type="text" name="name" placeholder="Name of the registrant"><br>--%>
            <%--<br>--%>
            <%--Invoice information:<br>--%>
            <%--<input type="text" name="invoiceName" placeholder="name"><br>--%>
            <%--<input type="text" name="invoiceAddress" placeholder="address"><br>--%>
            <%--<input type="text" name="invoiceVATorEGN" placeholder="VATNO or EGN"><br>--%>
            <%--<input type="text" name="mol" placeholder="mol"><br>--%>
            <%--<input type="text" name="email" placeholder="email"><br>--%>
            <%--<input type="submit" value="Register"/>--%>
        <%--</form>--%>
        <%--<form action="https://demo.epay.bg/" method=post>--%>
            <%--<input type="hidden" name="PAGE" value="paylogin">--%>
            <%--<input type="hidden" name="ENCODED" value="${ENCODED}">--%>
            <%--<input type="hidden" name="CHECKSUM" value="${CHECKSUM}">--%>
            <%--<input type="hidden" name="URL_OK" value="http://yahoo.com">--%>
            <%--<input type="hidden" name="URL_CANCEL" value="http://cnn.com">--%>
            <%--<input type="submit" value="Buy a ticket from epay.bg"/>--%>
        <%--</form>--%>

        <p>In case of questions, contact us at <a href="mailto:conference@jprime.io">conference@jprime.io</a>.</p>
</div>
 <!-- End Single Post Content -->
 
</div>
<!-- End Single Post Area -->

</div>



<user:sidebar/>

</div>

</div>
</div>
<!-- End content -->


        <user:footer/>
</div>
<!-- End Container -->

<!-- Go To Top Link -->
<a href="#" class="back-to-top"><i class="fa fa-angle-up"></i></a>

<div id="loader">
 <div class="spinner">
  <div class="dot1"></div>
  <div class="dot2"></div>
</div>
</div>



</body>
</html>