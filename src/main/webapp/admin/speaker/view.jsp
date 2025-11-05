<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ page import="site.model.SubmissionStatus" %>

<%
    pageContext.setAttribute("submissionStatus", SubmissionStatus.class);
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <jsp:directive.include file="../../theme-colors.jsp"/>
<admin:pageJavaScriptAndCss/>

    <title>Speakers</title>
</head>
<body>
<div class="admin-container">
<admin:menu/>
<fieldset>
    <legend>Speakers</legend>
    <form action="view">
        <table>
            <tr>
                <td><label for="branch"><strong>Year:</strong></label></td>
                <td><select name="branch" id="branch">
                    <option value=""
                            <c:if test="${selected_branch == null or selected_branch.length() == 0}">selected</c:if>>
                        All
                    </option>
                    <c:forEach var="branch" items="${branches}">
                        <option value="${branch.label}"
                                <c:if test="${selected_branch != null && selected_branch.equals(branch.label)}">selected</c:if>>${branch.year}</option>
                    </c:forEach>
                </select></td>
                <td><input type="submit" value="Search"></td>
            </tr>
        </table>
    </form>
    <table class="new-admin-table">
        <caption><strong>Speakers</strong></caption>
        <tr>
            <th><em>Picture</em></th>
            <th><em>Name</em></th>
            <th><em>Email</em></th>
            <th><em>Phone</em></th>
            <th><em>Twitter/X</em></th>
            <th><em>Bluesky</em></th>
            <th><em>Type</em></th>
            <c:if test="${selected_branch != null}">
                <th><em>Submissions for ${selected_year}</em></th>
                <th><em>Featured for ${selected_year}?</em></th>
                <th><em>Status for ${selected_year}?</em></th>
            </c:if>
            <c:if test="${selected_branch == null}">
                <th><em>Submissions</em></th>
                <th><em>Featured?</em></th>
                <th><em>Status?</em></th>
            </c:if>
            <th><em>Last submitted for?</em></th>
            <th><em>Operations</em></th>
        </tr>
        <c:forEach var="speaker" items="${speakers}">
            <tr>
                <td rowspan="2"><img alt="Speaker picture" src="/image/speaker/${speaker.id}"
                                     style="max-width: 280px"/></td>
                <td>${speaker.firstName} ${speaker.lastName} </td>
                <td>${speaker.email}</td>
                <td>${speaker.phone}</td>
                <td>${speaker.twitter}</td>
                <td>${speaker.bsky}</td>
                <td>${speaker.speakerType}</td>
                <td>${speaker.numberOfSubmissions}</td>
                <td><input disabled type="checkbox" <c:if test="${speaker.featured}">checked</c:if>></td>
                <td>${speaker.status}</td>
                <td>${speaker.branch}</td>
                <td rowspan="2">
                    <a href="edit/${speaker.id}">Edit</a>&nbsp;|&nbsp;<a
                        href="remove/${speaker.id}">Remove</a>
                </td>
            </tr>
            <tr class="title-column">
                <td colspan="10">
                    <table class="new-admin-table" style="height: unset">
                        <tr>
                            <td><em><strong>Bio :</strong></em>${speaker.bio}</td>
                        </tr>
                        <tr>
                            <td>
                                Talks :
                                <table class="new-admin-table" style="height: unset; box-shadow: unset">
                                    <tr>
                                        <th width="100%">Title</th>
                                        <th>Branch</th>
                                        <th>Featured</th>
                                        <th>Status</th>
                                        <th>View in</th>
                                    </tr>
                                    <c:forEach var="submission" items="${selected_branch != null ? speaker.branchSubmissions(selected_branch): speaker.allSubmissions}">
                                        <tr>
                                            <td>${submission.title}</td>
                                            <td>${submission.branch.year}</td>
                                            <td><input disabled type="checkbox" <c:if test="${submission.featured}">checked</c:if>></td>
                                            <td>${submission.status}</td>
                                            <td><a href="/admin/submission/view/id/${submission.id}">Submissions</a> </td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </c:forEach>
    </table>
    &nbsp;
    <div>
        <c:if test="${number > 0}">
            <c:if test="${selected_branch != null}">
                <span><a href="view?page=${number - 1}&branch=${selected_branch}">previous</a></span>
            </c:if>
            <c:if test="${selected_branch == null}">
                <span><a href="view?page=${number - 1}">previous</a></span>
            </c:if>
        </c:if>
        <c:if test="${number < (totalPages - 1)}">
            <c:if test="${selected_branch != null}">
                <span><span><a href="view?page=${number + 1}&branch=${selected_branch}">next</a></span></span>
            </c:if>
            <c:if test="${selected_branch == null}">
                <span><a href="view?page=${number + 1}">next</a></span>
            </c:if>
        </c:if>
    </div>
    <div>
        <a style="float:right;" href="add">Add</a>
    </div>
</fieldset>
</div>
</body>
</html>