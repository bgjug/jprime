<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script type="text/javascript" src="/js/niceforms.js"></script>
    <link rel="stylesheet" type="text/css" media="all" href="/css/niceforms-default.css" />
    <link rel="stylesheet" type="text/css" media="all" href="/css/admin.css" />
    <title>Registrants</title>
</head>
<body>
<admin:menu/>
<fieldset>
    <legend>Registrants</legend>
    &nbsp;
    <div>
        <a href="/admin/registrant/add">Add</a>
    </div>
    <table class="admin-table">
        <tr>
            <td><i>Name</i></td>
            <td><i>Email</i></td>
            <td><i>No of Visitors</i></td>
            <td><i>epay Invoice number</i></td>
            <td><i>Real Invoice number</i></td>
            <td><i>Proforma Invoice number</i></td>
            <td><i>Payment type</i></td>
            <td><i>Branch</i></td>
            <td><i>Student?</i></td>
            <td><i>Actions</i></td>
        </tr>
        <c:forEach var="registrant" items="${registrants}">
            <tr>
                <td>${registrant.name}</td>
                <td>${registrant.email}</td>
                <td>${fn:length(registrant.visitors)}</td>
                <td>${registrant.epayInvoiceNumber}</td>
                <td>${registrant.realInvoiceNumber}</td>
                <td>${registrant.proformaInvoiceNumber}</td>
                <td>${registrant.paymentType}</td>
                <td>${registrant.branch}</td>
                <td>${registrant.student}</td>
                <td>
                    <span><a href="/admin/registrant/edit/${registrant.id}">Edit</a></span> &nbsp;&nbsp;&nbsp;
                    <span><a href="/admin/registrant/remove/${registrant.id}">Remove</a></span>
                    <c:if test="${registrant.paymentType ne 'EPAY_ACCOUNT'}">
                        <span><a href="/admin/invoice/${registrant.id}">Invoice</a></span>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>
</fieldset>
</body>
</html>
