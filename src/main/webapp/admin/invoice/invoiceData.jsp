<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin"%>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <jsp:directive.include file="../../theme-colors.jsp" />
<admin:pageJavaScriptAndCss/>

    <title>Registrants</title>
</head>
<body>
<div class="admin-container">
<admin:menu/>
<form:form modelAttribute="invoiceData" method="post"
           action="/admin/invoice/send">
    <fieldset>
        <legend>Invoice</legend>
        <p>
            <form:errors/>
        </p>
        <dl>
            <dt>
                <label for="client">Client</label>
            </dt>
            <dd>
                <form:input path="client"/>
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="singlePriceWithVAT">Single price (EUR) (VAT included)</label>
            </dt>
            <dd>
                <form:input path="singlePriceWithVAT"/>
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="clientAddress">Address</label>
            </dt>
            <dd>
                <form:input path="clientAddress"/>
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="clientEIK">EIK</label>
            </dt>
            <dd>
                <form:input path="clientEIK"/>
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="clientVAT">VAT Number</label>
            </dt>
            <dd>
                <form:input path="clientVAT"/>
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="mol">MOL</label>
            </dt>
            <dd>
                <form:input path="mol"/>
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="description">description</label>
            </dt>
            <dd>
                <form:input path="description"/>
            </dd>
        </dl>
        <dl>
            <dt>
                Payment type
            </dt>
            <dd>
                <c:out value="${invoiceData.paymentType}"/>
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="invoiceDate">Date</label>
            </dt>
            <dd>
                <form:input path="invoiceDate"/>
            </dd>
        </dl>
        <sec:csrfInput/>
        <form:hidden path="paymentType"/>
        <form:hidden path="invoiceNumber"/>
        <form:hidden path="registrantId"/>
        <button type="submit">Send</button>
    </fieldset>
</form:form>
</div>
</body>
</html>
