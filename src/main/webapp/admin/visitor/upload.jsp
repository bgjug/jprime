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

    <title>Import visitors</title>

</head>
<body>
<div class="admin-container">
<admin:menu/>
<form:form modelAttribute="fileModel" method="post" enctype = "multipart/form-data"
           action="/admin/visitor/upload">
    <fieldset>
        <legend>Upload visitors from CSV File</legend>
        <p>
            <form:errors />
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


</div>
</body>
</html>
