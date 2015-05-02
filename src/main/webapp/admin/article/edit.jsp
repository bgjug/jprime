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

<%--
COMMENTING BECAUSE IT SIMPLY DOESNT WORK.
GUYS PLEASE STOP USE PRODUCTS THAT DONT WORK.

WHy it doesnt work :

example .. paste the following HTML snippet in CODE VIEW :

<div class="col-md-3 col-sm-6 col-xs-12 animated fadeIn delay-03" data-animation="fadeIn" data-animation-delay="03">
    <div class="team-member modern">
        <!-- Memebr Photo, Name & Position -->
        <div class="member-photo">
            <img alt="" src="/image/speaker/3">

            <div class="member-name">Heinz&nbsp;Kabutz<span>The Java Specialist</span>
            </div>
        </div>
        <div class="member-socail" style="text-align: left">
            <a class="twitter" href="http://twitter.com/heinzkabutz"><i class="fa fa-twitter"></i></a>
        </div>
    </div>
</div>

THIS SNIPPET IS TAKEN FROM THE HOMEPAGE... SAVE ... EDIT again ... check the HTML is changed.. 
PLEASE DONT INCLUDE CRAPS in the project

    <script type="text/javascript" src="/js/tinymce/tinymce.min.js"></script>
    <script type="text/javascript">
        tinymce.init({
            selector: "#text",
            plugins: [
                "advlist autolink lists link image charmap print preview anchor",
                "searchreplace visualblocks code fullscreen",
                "insertdatetime media table contextmenu paste"
            ],
            toolbar: "insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image"
        });
    </script>
     --%>
</head>
<body>
<admin:menu/>
<form:form commandName="article" method="post"
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
                <form:input path="title"/>
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="description">Description</label>
            </dt>
            <dd>
                <form:textarea path="description" cols="70" rows="5"/>
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="text">Text</label>
            </dt>
            <dd>
                <form:textarea path="text" cols="70" rows="20" id="text"/>
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


</body>
</html>