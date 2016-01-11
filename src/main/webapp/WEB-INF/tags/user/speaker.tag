<%@ tag language="java" pageEncoding="UTF-8"%>

<%@ attribute name="role" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<dl>
    <dt>
        <label for="${role}.firstName">First Name</label>
    </dt>
    <dd>
        <form:input path="${role}.firstName" />
    </dd>
</dl>
<dl>
    <dt>
        <label for="${role}.lastName">Last Name</label>
    </dt>
    <dd>
        <form:input path="${role}.lastName" />
    </dd>
</dl>
<dl>
    <dt>
        <label for="${role}.email">Email</label>
    </dt>
    <dd>
        <form:input path="${role}.email" />
    </dd>
</dl>
<dl>
    <dt>
        <label for="${role}.twitter">Twitter</label>
    </dt>
    <dd>
        <form:input path="${role}.twitter" />
    </dd>
</dl>
<dl>
    <dt>
        <label for="${role}.bio">Bio</label>
    </dt>
    <dd>
        <form:textarea path="${role}.bio" style="width:80%" rows="5" />
    </dd>
</dl>
<dl>
    <dt>
        <label for="${role}Image">Image</label>
    </dt>
    <dd>
        <input name="${role}Image" type="file" />
    </dd>
</dl>
