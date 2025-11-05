<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <jsp:directive.include file="../../theme-colors.jsp"/>
<admin:pageJavaScriptAndCss/>


    <script type="text/javascript" src="/js/jquery.min.js"></script>
    <title>Submissions</title>
</head>
<body>
<div class="admin-container">
<admin:menu/>
<script>
    function confirmAction(event, message) {
        // Show confirmation dialog
        const userConfirmed = confirm(message);

        // If the user cancels, stop the execution
        if (!userConfirmed) {
            event.preventDefault(); // Cancel navigation
            return false;
        }

        // Allow navigation if the user clicks "OK/Yes"
        return true;
    }
</script>
<fieldset style="width: 90%; margin: 0 auto;">
    <legend>Submissions</legend>
    <c:if test="${isCurrentYearOnly}">
        <div>
            <a href="/admin/submission/view/all">See all submissions</a>
        </div>
    </c:if>
    <div>
        <a href="/admin/submission/exportCSV">Export to CSV</a>
    </div>
    <c:if test="${submissionsByStatus != null}">
        <table style="width: 100%" class="new-admin-table">
            <tr><c:forEach var="submByStatus" items="${submissionsByStatus}">
                <th><a href="/admin/submission/view/status/${submByStatus.status}">${submByStatus.status}</a> : ${submByStatus.count}</th>
            </c:forEach></tr>
        </table>
    </c:if>
    <table class="new-admin-table">
        <caption><strong>Submissions</strong></caption>
        <tr>
            <th><em>Level</em></th>
            <th><em>Type</em></th>
            <th><em>Speaker</em></th>
            <th><em>Co-Speaker</em></th>
            <th><em>Branch</em></th>
            <th><em>Status</em></th>
            <th style="padding: 0px 2em 0px 2em;"><em>Operations</em></th>
        </tr>
        <c:forEach var="submission" items="${submissions}">
            <tr>
                <td colspan="6" class="title-column"><em><strong>Title</strong></em>: ${submission.title}</td>
                <td rowspan="3" class="title-column">
                    <a  onclick="<c:if test="${submission.status == 'ACCEPTED'}">return false;</c:if><c:if test="${submission.status != 'ACCEPTED'}">return confirmAction(event, 'Are you sure you want to accept this submission?')</c:if>"
                       href="/admin/submission/accept/${submission.id}">Accept</a>&nbsp;|&nbsp;
                    <a onclick="<c:if test="${submission.status == 'CONFIRMED'}">return false;</c:if><c:if test="${submission.status != 'CONFIRMED'}">return confirmAction(event, 'Are you sure you want to confirm this submission?')</c:if>"
                       href="/admin/submission/confirm/${submission.id}">Confirm</a>
                    <br/>
                    <a onclick="<c:if test="${submission.status == 'REJECTED'}">return false;</c:if><c:if test="${submission.status != 'REJECTED'}">return confirmAction(event, 'Are you sure you want to reject this submission?')</c:if>"
                       href="/admin/submission/reject/${submission.id}">Reject</a> |
                    <a onclick="<c:if test="${submission.status == 'CANCELED'}">return false;</c:if><c:if test="${submission.status != 'CANCELED'}">return confirmAction(event, 'Are you sure you want to cancel this submission?')</c:if>"
                       href="/admin/submission/cancel/${submission.id}">Cancel</a>
                    <br/>
                    <a href="/admin/submission/edit/${submission.id}">Edit</a>&nbsp;|&nbsp;
                    <a onclick="return confirmAction(event, 'Are you sure you want to resend the notification message for this submission?')"
                       href="/admin/submission/notify/${submission.id}">Notify</a><br/>
                    <a onclick="return confirmAction(event, 'Are you sure you want to delete this submission?')"
                       href="/admin/submission/delete/${submission.id}" class="confirmation">Delete</a>
                </td>
            </tr>
            <tr>
                <td colspan="6"><em><strong>Abstract</strong></em>: ${submission.description}</td>
            </tr>
            <tr>
                <td>${submission.level}</td>
                <td>${submission.type != null ? submission.type.toString() : "Conference session"}</td>
                <td>${submission.speaker.firstName} ${submission.speaker.lastName}</td>
                <td>${submission.coSpeaker.firstName} ${submission.coSpeaker.lastName}</td>
                <td>${submission.branch}</td>
                <td>${submission.status}</td>
            </tr>
        </c:forEach>
    </table>
    &nbsp;
    <div>
        <c:if test="${number > 0}">
            <span><a href="/admin/submission/view${path}?page=${number - 1}">previous</a></span>
        </c:if>
        <c:if test="${number < (totalPages - 1)}">
            <span><a href="/admin/submission/view${path}?page=${number + 1}">next</a></span>
        </c:if>
    </div>
    <div>
        <a style="float:right;" href="/admin/submission/add">Add</a>
    </div>

    <c:if test="${msg != null}">
        <script type="text/javascript">
            alert("${msg}")
        </script>
    </c:if>
</fieldset>

<script type="text/javascript">
    $('.confirmation').on('click', function () {
        return confirm('Please confirm that you want to delete this submission?');
    });
</script>
</div>
</body>
</html>