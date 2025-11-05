<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <jsp:directive.include file="../../theme-colors.jsp" />
<admin:pageJavaScriptAndCss/>

    <title>Visitor edit</title>

</head>
<body>
<div class="admin-container">
<admin:menu/>
<form:form modelAttribute="visitor" method="post"
           action="/admin/visitor/add">
    <fieldset>
        <legend>Add/Edit visitor</legend>
        <p>
            <form:errors/>
        </p>
        <dl>
            <dt>
                <label for="id">ID</label>
            </dt>
            <dd>
                ${visitor.id}
            </dd>
        </dl>
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
                <label for="email">Company</label>
            </dt>
            <dd>
                <form:input path="company"/>
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="status">Status</label>
            </dt>
            <dd>
                <form:select path="status" items="${statuses}"/>

            </dd>
        </dl>
        <dl>
            <dt>
                <label for="present">is present ?</label>
            </dt>
            <dd>
                <form:checkbox path="present"/>
            </dd>
        </dl>
        <sec:csrfInput/>
        <form:hidden path="id"/>
        <form:hidden path="registrant.id"/>
        <button type="submit">Save</button>
    </fieldset>

</form:form>


</div>
</body>
</html>
