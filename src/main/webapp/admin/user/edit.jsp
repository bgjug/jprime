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
    <jsp:directive.include file="../../theme-colors.jsp" />

    <title>Insert title here</title>
    <script type="text/javascript" src="/js/niceforms.js"></script>
        <link rel="stylesheet" type="text/css" media="all" href="/css/niceforms-default.css"/>
    <link rel="stylesheet" type="text/css" media="all" href="/css/admin.css"/>

</head>
<body>
<admin:menu/>
<form:form modelAttribute="user" method="post"
           action="/admin/user/add">
<fieldset>
    <legend>Add/Edit User</legend>
    <p>
        <form:errors/>
    </p>
    <dl>
        <dt>
            <label for="firstName">First Name</label>
        </dt>
        <dd>
            <form:input path="firstName"/>
        </dd>
    </dl>
    <dl>
        <dt>
            <label for="lastName">Last Name</label>
        </dt>
        <dd>
            <form:input path="lastName"/>
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
	<sec:csrfInput />
	<form:hidden path="id"/>
    <button type="submit">Save</button>
</fieldset>
</form:form>
</body>
</html>
