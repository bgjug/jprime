<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <jsp:directive.include file="../../theme-colors.jsp"/>

    <title>Insert title here</title>
    <script type="text/javascript" src="/js/niceforms.js"></script>
    <link rel="stylesheet" type="text/css" media="all" href="/css/niceforms-default.css"/>
    <link rel="stylesheet" type="text/css" media="all" href="/css/admin.css"/>
    <script type="text/javascript" src="/js/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="/js/datetime/jquery.datetimepicker.min.js"></script>
    <link rel="stylesheet" type="text/css" media="all" href="/css/datetime/jquery.datetimepicker.css"/>
</head>
<body>
<admin:menu/>
<form:form modelAttribute="branch" method="post"
           action="/admin/branch/add">
    <fieldset>
        <legend>Add/Edit Branch</legend>
        <p>
            <form:errors/>
        </p>
        <dl>
            <dt>
                <label for="year">Year</label>
            </dt>
            <dd>
                <form:input id="year" path="year"/>
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="startDate">Start date</label>
            </dt>
            <dd>
                <form:input id="startDate" path="startDate"/>
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="duration">Duration</label>
            </dt>
            <dd>
                <form:select path="duration" items="${durations}"/>
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="cfpOpenDate">CFP Open Date</label>
            </dt>
            <dd>
                <form:input id="cfpOpenDate" path="cfpOpenDate"/>
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="cfpCloseDate">CFP Close Date</label>
            </dt>
            <dd>
                <form:input id="cfpCloseDate" path="cfpCloseDate"/>
            </dd>
        </dl>
        <dl></dl>
        <h3>Sold Out Sponsor packages</h3>
        <dl>
            <dt>
                <label for="coOrgSoldOut">Co-Org Sold Out</label>
            </dt>
            <dd>
                <form:checkbox path="coOrgSoldOut" id="coOrgSoldOut" value="${branch.coOrgSoldOut}"/>
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="platinumSoldOut">Platinum Sold Out</label>
            </dt>
            <dd>
                <form:checkbox path="platinumSoldOut" id="platinumSoldOut" value="${branch.platinumSoldOut}"/>
            </dd>
        </dl>

        <dl>
            <dt>
                <label for="goldSoldOut">Gold Sold Out</label>
            </dt>
            <dd>
                <form:checkbox path="goldSoldOut" id="goldSoldOut" value="${branch.goldSoldOut}"/>
            </dd>
        </dl>

        <dl>
            <dt>
                <label for="goldLiteSoldOut">Gold Lite Sold Out</label>
            </dt>
            <dd>
                <form:checkbox path="goldLiteSoldOut" id="goldLiteSoldOut" value="${branch.goldLiteSoldOut}"/>
            </dd>
        </dl>

        <dl>
            <dt>
                <label for="goldOpenSoldOut">Gold Open Sold Out</label>
            </dt>
            <dd>
                <form:checkbox path="goldOpenSoldOut" id="goldOpenSoldOut" value="${branch.goldOpenSoldOut}"/>
            </dd>
        </dl>

        <dl>
            <dt>
                <label for="silverSoldOut">Silver Sold Out</label>
            </dt>
            <dd>
                <form:checkbox path="silverSoldOut" id="silverSoldOut" value="${branch.silverSoldOut}"/>
            </dd>
        </dl>
        <dl></dl>
        <h3>Agenda</h3>
        <dl>
            <dt>
                <label for="agendaPublished">Agenda Published</label>
            </dt>
            <dd>
                <form:checkbox path="agendaPublished" id="agendaPublished" value="${branch.agendaPublished}"/>
            </dd>
        </dl>
        <dl></dl>
        <h3>Tickets</h3>
        <dl>
            <dt>
                <label for="soldOut">Tickets Sold Out</label>
            </dt>
            <dd>
                <form:checkbox path="soldOut" id="soldOut" value="${branch.soldOut}"/>
            </dd>
        </dl>
        <dl></dl>
        <h3>Ticket Prices</h3>
        <dl>
            <dt>
                <label for="regularPrice">Regular Price</label>
            </dt>
            <dd>
                <form:input path="ticketPrices.regularPrice" id="regularPrice"/>
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="earlyBirdPrice">Early Bird Price</label>
            </dt>
            <dd>
                <form:input path="ticketPrices.earlyBirdPrice" id="earlyBirdPrice"/>
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="studentPrice">Student Price</label>
            </dt>
            <dd>
                <form:input path="ticketPrices.studentPrice" id="studentPrice"/>
            </dd>
        </dl>
        <sec:csrfInput/>
        <button type="submit">Save</button>
    </fieldset>

</form:form>

<script type="text/javascript">
    $(function () {
        var dateConfig = {
            timepicker: false,
            format: 'd.m.Y'
        };

        $('#startDate').datetimepicker(dateConfig);
        $('#cfpOpenDate').datetimepicker(dateConfig);
        $('#cfpCloseDate').datetimepicker(dateConfig);
    });

    $(document).ready(function () {
        const yearInput = $('#year');
        if (yearInput.val() && parseInt(yearInput.val()) !== 0) {
            yearInput.prop('disabled', true);
        }

        $('form').on('submit', function () {
            yearInput.prop('disabled', false);
        });
    });

</script>
</body>
</html>