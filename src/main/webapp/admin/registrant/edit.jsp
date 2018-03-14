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
    <title>Insert title here</title>
    <script type="text/javascript" src="/js/niceforms.js"></script>
    <link rel="stylesheet" href="/js/tinyeditor/style.css"/>
    <script type="text/javascript" src="/js/tinyeditor/tinyeditor.js"></script>
    <link rel="stylesheet" type="text/css" media="all" href="/css/niceforms-default.css"/>
    <link rel="stylesheet" type="text/css" media="all" href="/css/admin.css"/>

</head>
<body>
<admin:menu/>
<form:form modelAttribute="registrant" method="post"
           action="/admin/registrant/add">
<fieldset>
    <legend>Add/Edit Registrant</legend>
    <p>
        <form:errors/>
    </p>
    <dl>
        <dt>
            <label for="name">Name</label>
        </dt>
        <dd>
            <form:input path="name"/>
        </dd>
    </dl>
    <dl>
        <dt>
            <label for="email">email</label>
        </dt>
        <dd>
            <form:input path="email"/>
        </dd>
    </dl>
    <dl>
        <dt>
            <label for="company">Company</label>
        </dt>
        <dd>
            <form:checkbox path="company" id="isCompany" value="${registrant.company}"/>
        </dd>
    </dl>
    <dl>
        <dt>
            <label for="address">Address</label>
        </dt>
        <dd>
            <form:input path="address"/>
        </dd>
    </dl>
    <dl>
        <dt>
            <label for="vatNumber">BULSTAT (VAT number)</label>
        </dt>
        <dd>
            <form:input path="vatNumber"/>
        </dd>
    </dl>
    <dl>
        <dt>
            <label for="eik">EIK</label>
        </dt>
        <dd>
            <form:input path="eik"/>
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
            epay Invoice
        </dt>
        <dd>
            <c:out value="${registrant.epayInvoiceNumber}"/>
        </dd>
    </dl>
    <dl>
        <dt>
            Real Invoice
        </dt>
        <dd>
            <c:out value="${registrant.realInvoiceNumber}"/>
        </dd>
    </dl>
    <dl>
        <dt>
            Proforma
        </dt>
        <dd>
            <c:out value="${registrant.proformaInvoiceNumber}"/>
        </dd>
    </dl>
    <dl>
        <dt>
            <label for="paymentType">Payment type</label>
        </dt>
        <dd>
            <form:select path="paymentType" items="${paymentTypes}"/>

        </dd>
    </dl>
	<dl>
		<dt>
			<label for="branch">branch</label>
		</dt>
		<dd>
			<form:select path="branch" items="${branches}" />
		</dd>
	</dl>
	<sec:csrfInput />
	<form:hidden path="id"/>
    <form:hidden path="epayInvoiceNumber"/>
    <form:hidden path="realInvoiceNumber"/>
    <form:hidden path="proformaInvoiceNumber"/>
    <button type="submit">Save</button>
</fieldset>
</form:form>
<c:if test="${not empty registrant.id}">
<fieldset>
    <legend>Visitors</legend>
    <div>
        <a href="/admin/registrant/${registrant.id}/addVisitor">Add new</a>
    </div>
    <table class="admin-table">
        <tr>
            <td><i>Name</i></td>
            <td><i>Email</i></td>
            <td><i>Company</i></td>
            <td><i>Actions</i></td>
        </tr>
    <c:forEach var="visitor" items="${registrant.visitors}">
        <tr>
            <td>${visitor.name}</td>
            <td>${visitor.email}</td>
            <td>${visitor.company}</td>
            <td>
                <span><a href="/admin/visitor/edit/${visitor.id}">Edit</a></span> &nbsp;&nbsp;&nbsp;
                <span><a href="/admin/visitor/remove/${visitor.id}"> Remove </a></span>
            </td>
        </tr>
    </c:forEach>
    </table>
</fieldset>
</c:if>
</body>
</html>
