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
                <p> We aim to offer a balanced mix of sessions across all levels, so selecting the correct level is important. Here’s how we define them:</p>
                <p> <b> Beginner </b> Designed for junior developers or those coming from other programming languages and technologies. These sessions serve as introductions, providing foundational knowledge and context to help participants get started. </p>
                <p> <b> Intermediate </b> Targeted at mid‑level developers with Java experience who may be new to the specific framework, library, or technology being discussed. These sessions build on existing skills and bridge gaps in understanding. </p>
                <p> <b> Advanced </b> Intended for developers already familiar with the framework, library, or technology. These sessions dive into hidden complexities, advanced techniques, internal insights, and expert tips that go beyond the basics. </p>
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
            <p> <b>Conference Talk </b> – A live session where speakers share insights through presentations, demos, or live coding, tailored for beginner, intermediate, or advanced audiences. </p>
            <p> <b>Workshop </b>– A hands‑on session with guided steps where participants follow along on their laptops. </p>
            <p> <b>Deep Dive </b>– A longer, advanced session with detailed explanations, discussions, and time for audience questions. </p>

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
        <div id="coSpeaker" style="display: none;">
            <dl>
                <dt>Co-Speaker</dt>
            </dl>
            <user:speaker role="coSpeaker"/>
        </div>
        <c:if test="${admin}">
            <dl>
                <dt>
                    <label for="status">status</label>
                </dt>
                <dd>
                    <form:input path="status" readonly="true"/>
                </dd>
            </dl>
            <dl>
                <dt>
                    <label for="featured">featured</label>
                </dt>
                <dd>
                    <form:checkbox path="featured"/>
                </dd>
            </dl>
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
        <form:hidden path="speaker.id" />
        <c:if test="${!admin}">
            <p>
                <c:url var="captchaUrl" value="/captcha-image"/>
                <img src="${captchaUrl}"/>
                <form:input path="captcha"/>
                <form:errors path="captcha"/>
            </p>
        </c:if>
        <p>
        	<button type="submit">Submit</button>
        </p>
    </fieldset>

</form:form>
