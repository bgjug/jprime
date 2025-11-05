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

    <title>Edit hall</title>

</head>
<body>
<div class="admin-container">
<admin:menu/>
<form:form modelAttribute="hall" method="post"
           action="/admin/hall/add">
<fieldset>
    <legend>Add/Edit Venue Hall</legend>
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
            <label for="description">Description</label>
        </dt>
        <dd>
            <form:textarea path="description"/>
        </dd>
    </dl>
    <sec:csrfInput />
    <form:hidden path="id" />
    <form:hidden path="createdDate" />
    <form:hidden path="createdBy" />
    <button type="submit">Save</button>
</fieldset>
</form:form>
</div>
</body>
</html>
