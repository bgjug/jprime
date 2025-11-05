<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
<fieldset>
    <legend>Registrants</legend>
    &nbsp;
    <form action="view">
         <table>
            <tr>
                <td><label for="branch"><strong>Year:</strong></label></td>
                <td><select name="branch" id="branch">
                    <option value=""
                            <c:if test="${selected_branch == null or selected_branch.length() == 0}">selected</c:if>>
                        All
                    </option>
                    <c:forEach var="branch" items="${branches}">
                        <option value="${branch.label}"
                                <c:if test="${selected_branch != null && selected_branch.equals(branch.label)}">selected</c:if>>${branch.year}</option>
                    </c:forEach>
                </select></td>
                <td><input type="submit" value="Search"></td>
            </tr>
        </table>
    </form>
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
                    <a href="/admin/registrant/edit/${registrant.id}">Edit</a>&nbsp;|&nbsp;<a href="/admin/registrant/remove/${registrant.id}">Remove</a><c:if test="${registrant.paymentType ne 'EPAY_ACCOUNT'}">&nbsp;|&nbsp;<a href="/admin/invoice/${registrant.id}">Invoice</a></c:if>
                    <span><a href="/admin/registrant/send-tickets/${registrant.id}">SEND TICKETS</a></span>
                </td>
            </tr>
        </c:forEach>
    </table>
    <div>
        <c:if test="${number > 0}">
            <c:if test="${selected_branch != null}">
                <span><a href="view?page=${number - 1}&branch=${selected_branch}">previous</a></span>
            </c:if>
            <c:if test="${selected_branch == null}">
                <span><a href="view?page=${number - 1}">previous</a></span>
            </c:if>
        </c:if>
        <c:if test="${number < (totalPages - 1)}">
            <c:if test="${selected_branch != null}">
                <span><span><a href="view?page=${number + 1}&branch=${selected_branch}">next</a></span></span>
            </c:if>
            <c:if test="${selected_branch == null}">
                <span><a href="view?page=${number + 1}">next</a></span>
            </c:if>
        </c:if>
    </div>
</fieldset>
</div>
</body>
</html>
