<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
    <script type="text/javascript" src="/js/niceforms.js"></script>
    <script type="text/javascript" src="/js/jquery-2.1.1.min.js"></script>
    <link rel="stylesheet" type="text/css" media="all" href="/css/niceforms-default.css"/>
    <link rel="stylesheet" type="text/css" media="all" href="/css/admin.css"/>

    <user:pageJavaScriptAndCss/>
</head>
<body  style="background: #cccccc!important;">
<admin:menu/>

<div style="display: inline-block;width:auto">
<form:form modelAttribute="article" method="post"
           action="/admin/article/add">
    <fieldset>
        <legend>Add/Edit Article</legend>
        <p>
            <form:errors/>
        </p>
        <dl>
            <dt>
                <label for="title">Title</label>
            </dt>
            <dd>
                <form:input path="title" id="title"/>
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="description">Description</label>
            </dt>
            <dd>
                <form:textarea path="description" cols="70" rows="5" cssStyle="width:100%"/>
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="text">Text</label>
            </dt>
            <dd>
                <form:textarea path="text"  cols="70" rows="20" id="text" cssStyle="width:100%"/>
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="tags">Tag</label>
            </dt>
            <dd>
                <form:select path="tags" multiple="true" items="${tags}" itemLabel="name" itemValue="id"/>
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="published">Published</label>
            </dt>
            <dd>
                <form:checkbox path="published" value="${published}" />
            </dd>
        </dl>
        <sec:csrfInput/>
        <form:hidden path="id"/>
        <form:hidden path="createdDate"/>
        <form:hidden path="createdBy"/>
        <button type="submit">Save</button>
    </fieldset>

</form:form>
</div>
<div id="preview" style="display: inline-block; height:100%; vertical-align: top;border: 1px;background: white;padding:20px;max-width: 45%">

    <div class="blog-post gallery-post">
        <div class="post-content">
            <h2><span id="previewTitle"></span> </h2>

            <p id="previewText"></p>

            <div class="post-bottom clearfix">
            </div>
        </div>

    </div>

</div>

<script type="text/javascript">
    $(document).ready(function(){
        //init
        $("#previewTitle").html($( "#title").val());
        $("#previewText").html($( "#text").val());

        $( "#text" ).on("change paste keyup", function() {
            $("#previewText").html($( "#text").val());
        });
        $( "#title" ).on("change paste keyup", function() {
            $("#previewTitle").html($( "#title").val());
        });
    });
</script>


</body>
</html>