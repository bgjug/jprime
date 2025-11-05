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

    <title>Send email</title>

</head>
<body>
<div class="admin-container">
<admin:menu/>
<form method="post"
           action="/admin/visitor/send">
    <fieldset>
        <legend>Send email to all visitors</legend>
        <p>
            <form:errors/>
        </p>
        <dl>
            <dt>
                <label for="subject">Subject</label>
            </dt>
            <dd>
                <input type="text" name="subject"/>
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="content">Content</label>
            </dt>
            <dd>
                <textarea name="content" cols="70" rows="20" id="text" style="width:100%">
                </textarea>
            </dd>
        </dl>
        <sec:csrfInput/>
        <button type="submit">SEND(and wait)</button>
    </fieldset>

</form>


</div>
</body>
</html>