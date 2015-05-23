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


</body>
</html>