<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
           uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!doctype html>
<!--[if IE 8 ]><html class="ie ie8" lang="en"> <![endif]-->
<!--[if (gte IE 9)|!(IE)]><html lang="en" class="no-js"> <![endif]-->
<html lang="en">
<head>

   <!-- Basic -->
   <title>Buy conference tickets</title>

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
            <h2>Buy conference tickets</h2>
        </div>
    </div>
</div>
<!-- Page Banner End -->

<section id="about" class="section-padding">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <p>
                    <h2>Buy conference tickets</h2>
                    <p>
                        <%-- <p style="text-decoration: line-through;"> --%>
                    <p>* The <strong>early bird ticket</strong> price for the conference is <strong>${early_bird_ticket_price}</strong> BGN (VAT included) until ${cfp_close_date}.</p>
                    <p>* The <strong>regular</strong> ticket price after ${cfp_close_date_no_year} will be <strong>${regular_ticket_price}</strong> BGN (VAT included).</p>
                    <p>* There is a discount of the regular ticket price for students. Student ticket price is <strong>${student_ticket_price}</strong> BGN (VAT included).</p>
                    <p>* There is a free pass for a JUG lead (one per Java User Group).</p>

                    Buy a ticket:
                    <form:form modelAttribute="registrant" method="post"
                               action="/tickets" id="visitorsForm">
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
                                <!-- see https://github.com/bgjug/jprime/issues/28 -->
                                <dt><label for="visitors[0].company">Visitor company</label></dt>
                                <dd><form:input path="visitors[0].company"/></dd>
                            </dl>
                        </fieldset>
                        <input type="button" id="newVisitor" value="Add new">&nbsp;&nbsp;
                        <input type="button" id="removeVisitor" value="Remove last">
                        <br><br>
                        <form:checkbox path="student" label="I am student" id="isStudent" onchange="document.getElementById('isCompany').disabled = this.checked; this.checked?document.getElementById('isCompany').checked= false:'';"/>
                        <br>
                        <form:checkbox path="company" label="Issue company VAT invoice" id="isCompany" onchange="document.getElementById('isStudent').disabled = this.checked; this.checked?document.getElementById('isStudent').checked= false:'';"/>
                        <br><br>
                        <fieldset id="studentFieldset" style="display:none">
                            <legend> Student tickets </legend>
                            <dl><dd><dd> If you are a Student and you can’t afford to pay full price ticket we still have something for YOU! <br/><br/>
                                Join in our community conference and be part of our event! <br/>
                                We do our best to support everybody who wants to learn!</dd></dl>
                        </fieldset>
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
                        <%--<dl>
                            <dt><label for="paymentType">Pay with</label></dt>
                            <dd><form:select path="paymentType" items="${paymentTypes}"/></dd>
                        </dl>--%>
                        <sec:csrfInput/>
                        <form:hidden path="id"/>
                        <p>
                            <c:url var="captchaUrl" value="/captcha-image"/>
                            <img src="${captchaUrl}"/>
                            <form:input path="captcha"/>
                            <form:errors path="captcha"/>
                        </p>
                        <p>
                            <input type="checkbox"  onchange="document.getElementById('submitRegistration').disabled = !this.checked;" />
                            &nbsp; I agree with the <a href="/privacy-policy" target="_blank">Privacy policy</a>
                        </p>
                        <p>
                            <button type="submit" id="submitRegistration" disabled>Proceed</button>
                        </p>

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
                </p>
            </div>
        </div>
    </div>
</section>

<user:footer/>

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

    var checkQty=function(){
        var size = $("#visitorsFieldset").find("dl").size();
        if(size>1) {
            $("#removeVisitor").show();
        }else{
            $("#removeVisitor").hide();
        }
    };

    var appendVisitor = function() {
        var visitorsFieldset = $("#visitorsFieldset");
        var index = visitorsFieldset.find("dl").size();
        var clone = visitorsFieldset.find("dl:last").clone();
        clone.find("dd label").remove();
        clone.find("dd input").each(function(i) {
            switch (i) {
                case 0:
                    $(this).attr("id", "visitors" + index + ".name");
                    $(this).attr("name", "visitors[" + index + "].name");
                    break;
                case 1:
                    $(this).attr("id", "visitors" + index + ".email");
                    $(this).attr("name", "visitors[" + index + "].email");
                    break;
                case 2:
                    $(this).attr("id", "visitors" + index + ".company");
                    $(this).attr("name", "visitors[" + index + "].company");
                    break;
            }
        });
        clone.find("dt label").each(function(i) {
            switch (i) {
                case 0:
                    $(this).attr("for", "visitors" + index + ".name");
                    break;
                case 1:
                    $(this).attr("for", "visitors" + index + ".email");
                    break;
                case 2:
                    $(this).attr("for", "visitors" + index + ".company");
                    break;
            }
        });
        visitorsFieldset.append(clone);
        $("#visitors" + index + "\\.name").rules("add",{required: true,minlength: 5});
        $("#visitors" + index + "\\.email").rules("add",{required: true,email: true});
        $("#visitors" + index + "\\.company").rules("add",{required: true});

        checkQty();
    };

    var removeVisitor = function() {
        var visitorsFieldset = $("#visitorsFieldset");
        var size = visitorsFieldset.find("dl").size()+1;
        if(size>1) {
            visitorsFieldset.find("dl:last").remove();
        }
        checkQty();
    };

    var issueStudentTicketHandler = function() {
        if (this.checked) {
            $("#studentFieldset").show();
            $("#invoiceFieldset").hide();
        } else {
            $("#studentFieldset").hide();
        }
    };

    var issueInvoiceHandler = function() {
        if (this.checked) {
            $("#invoiceFieldset").show();
            $("#studentFieldset").hide();
        } else {
            $("#invoiceFieldset").hide();
        }
    };

    $(function() {
        $("#newVisitor").click(appendVisitor);
        $("#removeVisitor").hide();
        $("#removeVisitor").click(removeVisitor);
        $("#isStudent").click(issueStudentTicketHandler);
        $("#isCompany").click(issueInvoiceHandler);
    });
</script>

</body>
</html>
