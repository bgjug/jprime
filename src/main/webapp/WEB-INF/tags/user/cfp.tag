<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
           uri="http://www.springframework.org/security/tags"%>
<%@ attribute name="action" %>

<form:form commandName="submission" method="post"
           action="${action}" enctype="multipart/form-data">
    <fieldset>
        <legend>Add Submission</legend>
        <p>
            <form:errors />
        </p>
        <dl>
            <dt>
                <label for="title">Title</label>
            </dt>
            <dd>
                <form:input path="title" />
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="description">Abstract</label>
            </dt>
            <dd>
                <form:textarea path="description"  cols="70" rows="5"/>
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="level">Session level</label>
            </dt>
            <dd>
                <form:select path="level" items="${levels}"/>
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="speaker.firstName">First Name</label>
            </dt>
            <dd>
                <form:input path="speaker.firstName" />
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="speaker.lastName">Last Name</label>
            </dt>
            <dd>
                <form:input path="speaker.lastName" />
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="speaker.email">Email</label>
            </dt>
            <dd>
                <form:input path="speaker.email" />
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="speaker.twitter">Twitter</label>
            </dt>
            <dd>
                <form:input path="speaker.twitter" />
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="speaker.bio">Bio</label>
            </dt>
            <dd>
                <form:textarea path="speaker.bio" cols="70" rows="5" />
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="speaker.headline">headline</label>
            </dt>
            <dd>
                <form:textarea path="speaker.headline" cols="70" rows="5" />
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="speaker.featured">featured</label>
            </dt>
            <dd>
                <form:checkbox path="speaker.featured"/>
            </dd>
        </dl>
        <dl>
            <dt>
                <label for="file">Picture ( MUST be 280x326)</label>
            </dt>
            <dd>
                <input name="file" type="file" />
            </dd>
        </dl>
        <sec:csrfInput />
        <form:hidden path="id" />
        <form:hidden path="status" />
        <form:hidden path="speaker.id" />
        <button type="submit">Save</button>
    </fieldset>

</form:form>