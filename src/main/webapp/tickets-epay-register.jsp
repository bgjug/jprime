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

    <!--validation-->
    <script src="js/jquery.validate.min.js"></script>


    <script type="text/javascript">

        $(document).ready(function () {

            $('#visitorsForm').validate({ // initialize the plugin
                rules: {
                    'visitors[0].name': {
                        required: true
                    },
                    'visitors[0].email': {
                        required: true,
                        email: true
                    },
                    'visitors[0].company': {
                            required: true
                    },
                    'name': {
                        required: true
                    },
                    'address': {
                        required: true
                    },
                    'eik': {
                        required: true
                    },
                    'mol': {
                        required: true
                    },
                    'email': {
                        required: true,
                        email: true
                    }
                }
            })
        });


        var appendVisitor = function() {
            var visitorsFieldset = $("#visitorsFieldset");
            var index = visitorsFieldset.find("dl").size();
            var clone = visitorsFieldset.find("dl:last").clone();
            clone.find("dd label").remove();
            clone.find("dd input").each(function(i) {
                if (i == 0) {
                    $(this).attr("id", "visitors" + index + ".name");
                    $(this).attr("name", "visitors[" + index + "].name");
                }
                else {
                    $(this).attr("id", "visitors" + index + ".email");
                    $(this).attr("name", "visitors[" + index + "].email");
                }
            });
            clone.find("dt label").each(function(i) {
                if (i == 0) {
                    $(this).attr("for", "visitors" + index + ".name");
                }
                else {
                    $(this).attr("for", "visitors" + index + ".email");
                }
            });
            visitorsFieldset.append(clone);
            $("#visitors" + index + "\\.name").rules("add",{required: true,minlength: 5});
            $("#visitors" + index + "\\.email").rules("add",{required: true,email: true});
            $("#visitors" + index + "\\.company").rules("add",{required: true});
        };

        var issueInvoiceHandler = function() {
            if (this.checked) {
                $("#invoiceFieldset").show();
            } else {
                $("#invoiceFieldset").hide();
            }
        };

        $(function() {
            $("#newVisitor").click(appendVisitor);
            $("#isCompany").click(issueInvoiceHandler);
        });
    </script>
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
        <form:form commandName="registrant" method="post"
                   action="/tickets/epay" id="visitorsForm">
            <p>
                <form:errors/>
            </p>
            <fieldset id="visitorsFieldset">
                <legend>Visitors</legend>
                <dl>
                    <dt><label for="visitors[0].name">Visitor</label></dt>
                    <dd><form:input path="visitors[0].name"/></dd>
                    <dt><label for="visitors[0].email">Visitor email</label></dt>
                    <dd><form:input path="visitors[0].email"/></dd>
                    <dt><label for="visitors[0].company">Visitor company</label></dt>
                    <dd><form:input path="visitors[0].company"/></dd>
                </dl>
            </fieldset>
            <a id="newVisitor">Add new</a>
            <br><br>
            <form:checkbox path="company" label=" Issue invoice" id="isCompany" checked="true"/>
            <br><br>
            <fieldset id="invoiceFieldset">
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
                    <dt><label for="vatNumber">VAT registration number</label></dt>
                    <dd><form:input path="vatNumber" /></dd>
                </dl>
                <dl>
                    <dt><label for="eik">Unified Identity Code (EIK)</label></dt>
                    <dd><form:input path="eik" /></dd>
                </dl>
                <dl>
                    <dt><label for="mol">Accountable person (MOL)</label></dt>
                    <dd><form:input path="mol" /></dd>
                </dl>
                <dl>
                    <dt><label for="email">e-mail</label></dt>
                    <dd><form:input path="email" /></dd>
                </dl>
            </fieldset>
            <sec:csrfInput/>
            <form:hidden path="id"/>
            <button type="submit">Save</button>

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