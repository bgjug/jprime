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
    <link rel="stylesheet" href="/js/tinyeditor/style.css"/>
    <script type="text/javascript" src="/js/tinyeditor/tinyeditor.js"></script>
    <link rel="stylesheet" type="text/css" media="all" href="/css/niceforms-default.css"/>
    <link rel="stylesheet" type="text/css" media="all" href="/css/admin.css"/>

</head>
<body>
<admin:menu/>
<form:form modelAttribute="fileModel" method="post" enctype = "multipart/form-data"
           action="/admin/visitor/upload">
    <fieldset>
        <legend>Upload visitors from CSV File</legend>
        <p>
            <form:errors/>
        </p>
        <dl>
            <dt>
                <label for="visitorType">Visitor type</label>
            </dt>
            <dd>
                <form:select path="visitorType" items="${visitorTypes}"/>
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="visitorStatus">Visitor status</label>
            </dt>
            <dd>
                <form:select path="visitorStatus" items="${visitorStatuses}"/><br/><span style="color: red">* For JPrime visitors</span>
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="csvFile">CSV File with visitors</label>
            </dt>
            <dd>
                <input name="csvFile" type="file" />
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="emptyVisitorsBeforeUpload">Remove all existing visitors</label>
            </dt>
            <dd>
                <form:checkbox path="emptyVisitorsBeforeUpload"/><br/><span style="color: red">* For JProfessionals only</span>
            </dd>
        </dl>
        <sec:csrfInput/>
        <button type="submit">Upload</button>
    </fieldset>

</form:form>


</body>
</html>
