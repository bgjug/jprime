<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin"%>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <jsp:directive.include file="../../theme-colors.jsp" />
<admin:pageJavaScriptAndCss/>
    <meta http-equiv="refresh" content="30">

    <title>Registrants</title>
</head>
<body>
<div class="admin-container">
<script type="text/javascript">
    remaining = 30;
    updateFunction = function(){
        remaining--;
        document.getElementById("legend").innerHTML = "Background Jobs - will refresh in " + remaining + " seconds";
        setTimeout(updateFunction, 1000);
    }
    setTimeout(updateFunction, 1000);
</script>

<admin:menu/>
    <fieldset>
        <legend id="legend">Background Jobs - will refresh in 30 seconds</legend>
        <div>
            <table class="admin-table">
                <thead>
                <tr>
                   <td colspan="3">Background jobs</td>
                </tr>
                </thead>
                <tr>
                    <td><i>Description</i></td>
                    <td><i>Last status message</i></td>
                    <td><i>Completed</i></td>
                </tr>
            <c:forEach var="job" items="${jobs}">
                <tr>
                    <td>${job.description}</td>
                    <td>${job.status}</td>
                    <td>${job.completed}</td>
    <%--                <td>--%>
    <%--                    <span><a href="/admin/registrant/edit/${registrant.id}">Edit</a></span> &nbsp;&nbsp;&nbsp;--%>
    <%--                    <span><a href="/admin/registrant/remove/${registrant.id}">Remove</a></span>--%>
    <%--                    <c:if test="${registrant.paymentType ne 'EPAY_ACCOUNT'}">--%>
    <%--                        <span><a href="/admin/invoice/${registrant.id}">Invoice</a></span>--%>
    <%--                    </c:if>--%>
    <%--                    <span><a href="/admin/registrant/send-tickets/${registrant.id}">SEND TICKETS</a></span>--%>
    <%--                </td>--%>
                </tr>
            </c:forEach>
            </table>
        </div>
</fieldset>
</div>
</body>
</html>
