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
    <script type="text/javascript" src="/js/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="/js/datetime/jquery.datetimepicker.min.js"></script>
    <link rel="stylesheet" type="text/css" media="all" href="/css/niceforms-default.css"/>
    <link rel="stylesheet" type="text/css" media="all" href="/css/admin.css"/>
    <link rel="stylesheet" type="text/css" media="all" href="/css/datetime/jquery.datetimepicker.css"/>
</head>
<body>
<admin:menu/>
<form:form commandName="session" method="post"
           action="/admin/session/add">
<fieldset>
    <legend>Add/Edit Session</legend>
    <p>
        <form:errors/>
    </p>
    <dl>
        <dt>
            <label for="submission">Submission</label>
        </dt>
        <dd>
            <form:select path="submission">
                <form:option value="-1" label="Break"/>
                <form:options items="${submissions}" itemValue="id" itemLabel="title"/>
            </form:select>

        </dd>
    </dl>
    <dl id="titleRow">
        <dt>
            <label for="title">Break title</label>
        </dt>
        <dd>
            <form:input path="title"/>
        </dd>
    </dl>
    <dl>
        <dt>
            <label for="startTime">Start time</label>
        </dt>
        <dd>
            <form:input path="startTime"/>
        </dd>
    </dl>
    <dl>
        <dt>
            <label for="endTime">End time</label>
        </dt>
        <dd>
            <form:input path="endTime"/>
        </dd>
    </dl>
    <dl id="hallRow">
        <dt>
            <label for="hall">Hall</label>
        </dt>
        <dd>
            <form:select path="hall">
                <form:options items="${halls}" itemValue="id" itemLabel="name"/>
            </form:select>
        </dd>
    </dl>
    <sec:csrfInput />
    <form:hidden path="id" />
    <form:hidden path="createdDate" />
    <form:hidden path="createdBy" />
    <button type="submit">Save</button>
</fieldset>
</form:form>
<script type="text/javascript">
$(function() {
	var calendarConfig = {
		 	format:'d.m.Y H:i',
		 	allowTimes:[
	           '08:00', '09:00', '09:30', '09:50', '10:00',
	           '10:20', '10:30', '10:50', '11:00', '11:20',
			   '11:30', '11:50', '12:00', '12:20',
	           '13:30', '14:20', '14:30', '15:20', '16:00',
	           '16:50', '17:00', '17:50', '18:00', '19:00',
	           '21:00'
            ]
	 };
	 
    $('#startTime').datetimepicker(calendarConfig);
	$('#endTime').datetimepicker(calendarConfig);


    var handleBreaks = function() {
        var selectedValue = $("#submission").val();
        var titleField = $('#titleRow');
        var hallField = $('#hallRow');
        if (selectedValue != "-1") {
            titleField.hide();
            $('#title').val("");
            hallField.show();
        } else {
            titleField.show();
            hallField.hide();
        }
    };

    handleBreaks();
    $('#submission').change(handleBreaks);
});
</script>

</body>
</html>
