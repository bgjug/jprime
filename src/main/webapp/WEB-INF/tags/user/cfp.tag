<%@ tag language="java" pageEncoding="UTF-8"%>

<%@ attribute name="action" %>
<%@ attribute name="admin" required="false" type="java.lang.Boolean"%>


<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
           uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user"%>


<form:form modelAttribute="submission" method="post"
           action="${action}" enctype="multipart/form-data">
    <fieldset>
        <p>
        </p>
        <dl>
            <dt>Session info</dt>
        </dl>
        <dl>
            <dt>
                <label for="title">Title</label>
            </dt>
            <dd>
                <form:input path="title" />
                <form:errors path="title"/>
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="description">Abstract</label>
            </dt>
            <dd>
                <form:textarea path="description"  style="width:80%" rows="5"/>
                <form:errors path="description"/>
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="level">Session level</label>
            </dt>
            <dd>
                <form:select path="level" items="${levels}"/>
                <form:errors path="level"/>
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="type">Session type</label>
            </dt>
            <dd>
                <form:select path="type" items="${sessionTypes}"/>
                <form:errors path="type"/>
            </dd>
        </dl>
        <dl>
            <dt>Speaker</dt>
        </dl>
        <user:speaker role="speaker"/>
        <button id="toggleCoSpeaker" type="button">${coSpeaker_caption}</button>
        <dl></dl>
        <div id="coSpeaker">
            <dl>
                <dt>Co-Speaker</dt>
            </dl>
            <user:speaker role="coSpeaker"/>
        </div>
        <c:if test="${admin}">
        	<dl>
                <dt>
                    <label for="branch">branch</label>
                </dt>
                <dd>
                    <form:select path="branch" items="${branches}"/>
                </dd>
            </dl>
        </c:if>
        <sec:csrfInput />
        <form:hidden path="id" />
        <form:hidden path="createdDate"/>
        <form:hidden path="createdBy"/>
        <form:hidden path="status" />
        <form:hidden path="speaker.id" />
        <p>
	        <c:url var="captchaUrl" value="/captcha-image"/>
	        <img src="${captchaUrl}"/>
	        <form:input path="captcha"/>
	        <form:errors path="captcha"/>
        </p>
        <p>
        	<button type="submit">Submit</button>
        </p>
    </fieldset>

</form:form>