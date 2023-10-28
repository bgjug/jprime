<%@ page trimDirectiveWhitespaces="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>
<%@ page import="site.model.SessionType" %>

<!doctype html>
<!--[if IE 8 ]><html class="ie ie8" lang="en"> <![endif]-->
<!--[if (gte IE 9)|!(IE)]><html lang="en" class="no-js"> <![endif]-->
<html lang="en">
<head>

    <!-- Basic -->
    <title>jPrime | Post</title>

    <!-- Define Charset -->
    <meta charset="utf-8">

    <!-- Responsive Metatag -->
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

    <jsp:directive.include file="theme-colors.jsp" />

    <!-- Page Description and Author -->
    <meta name="description" content="JPrime Conference">
    <meta name="author" content="JPrime">

    <user:pageJavaScriptAndCss/>
</head>

<body>

<user:header/>

<!-- Page Banner Start -->
<div id="page-banner-area" class="page-banner">
    <div class="page-banner-title">
        <div class="text-center">
            <h2>Agenda</h2>
        </div>
    </div>
</div>
<!-- Page Banner End -->

<section id="about" class="section-padding">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <!-- Agenda Content Start -->
                <h2>${talk.submission.title}</h2>
                <c:if test="${not agenda}"><p>The agenda is not yet available </p></c:if>
                <c:if test="${agenda}"><p>The agenda for jPrime <fmt:formatDate pattern="YYYY" value="${firstDayDate}" /> is now available!</p>
                    <div class="entry-content">
                        <style>
                            table{
                                border-color: gray;
                                -webkit-font-smoothing: antialiased;
                            }
                            a{
                                font-weight: bolder;
                            }

                            media="all" tr {
                                            display: table-row;
                                            vertical-align: inherit;
                                            border-color: inherit;
                                        }
                            media="all"
                            table thead tr th, table tfoot tr th, table tfoot tr td, table tbody tr th, table tbody tr td, table tr td {
                                display: table-cell;
                                line-height: 1.125rem;
                            }
                            table tr.even, table tr.alt, table tr:nth-of-type(even){
                                background: #f9f9f9;
                            }
                            .tcenter td {
                                text-align: center;
                            }
                            .tcenter th {
                                text-align: center;
                            }
                        </style>

                        <script type="text/javascript">
                            $( document ).ready(function() {
                                $('#link-1').css("color","#f8ba01");
                                $('#link-1').css("border-bottom","2px solid");
                                $('#link-1').css("border-color","#f8ba01");

                                $('#link-2').css("color","black");
                                $('#link-2').css("border-bottom","0px solid");
                                $('#link-2').css("border-color","black");

                                $('#agenda-1').show();
                                $('#agenda-2').hide();
                            });
                            function select(element, agendaId){
                                if(element === 'link-1') {
                                    $('#link-1').css("color","#f8ba01");
                                    $('#link-1').css("border-bottom","2px solid");
                                    $('#link-1').css("border-color","#f8ba01");

                                    $('#link-2').css("color","black");
                                    $('#link-2').css("border-bottom","0px solid");
                                    $('#link-2').css("border-color","black");

                                    $('#agenda-1').show();
                                    $('#agenda-2').hide();
                                } else {
                                    $('#link-2').css("color","#f8ba01");
                                    $('#link-2').css("border-bottom","2px solid");
                                    $('#link-2').css("border-color","#f8ba01");

                                    $('#link-1').css("color","black");
                                    $('#link-1').css("border-bottom","0px solid");
                                    $('#link-1').css("border-color","black");
                                    $('#agenda-2').show();
                                    $('#agenda-1').hide();
                                }
                            }
                        </script>

                        <ul class="list-group list-group-horizontal" style="flex-direction:row">
                            <li class="list-group-item">
                                <a href="javascript:return false;" onclick="select('link-1','agenda-1')" id="link-1"><fmt:formatDate pattern="EEEE (dd/MM/YYYY)" value="${firstDayDate}" /></a>

                            </li>
                            <li class="list-group-item">
                                <a href="javascript:return false;"  onclick="select('link-2','agenda-2')" id="link-2"><fmt:formatDate pattern="EEEE (dd/MM/YYYY)" value="${secondDayDate}"/> </a>
                            </li>
                        </ul>

                        <br/>

                        <table class="tcenter" id="agenda-1" width="100%">
                            <thead>
                            <tr>
                                <th></th>
                                <th>Hall A</th>
                                <th>Hall B</th>
                                <th>Workshops</th>
                            </tr>
                            </thead>
                            <tbody>

                            <c:set var="firstDay">
                                <fmt:formatDate pattern="D" value="${alpha[0].startDateTime}" />
                            </c:set>

                            <c:set var="workshopsCounter" value="0"/>
                            <c:forEach var="talk" items="${alpha}" varStatus="i">
                                <c:set var="talkDay">
                                    <fmt:formatDate pattern="D" value="${talk.startDateTime}" />
                                </c:set>
                                <%-- e.g. first day talk --%>
                                <c:if test="${talkDay eq firstDay}">
                                    <tr>
                                        <td>
                                            <fmt:formatDate pattern="HH:mm" value="${talk.startDateTime}" />
                                            <br/>
                                            <fmt:formatDate pattern="HH:mm" value="${talk.endDateTime}" />
                                        </td>
                                        <td colspan=" ${beta[i.count-1].startTime eq talk.startTime ? (empty talk.submission ? 2 : 1 ) : 2}">
                                            <c:choose>
                                                <c:when test="${empty talk.submission}">
                                                    ${talk.title}
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="/agenda/${talk.id}">${talk.submission.title}</a>
                                                </c:otherwise>
                                            </c:choose>
                                            <br/>
                                            <c:if test="${not empty talk.submission and not empty talk.submission.speaker}">
                                                <c:out value="${talk.submission.speaker.name}"/>
                                            </c:if>
                                            <c:if test="${not empty talk.submission and not empty talk.submission.coSpeaker}">
                                                <br/>&& <br/>
                                                <c:out value="${talk.submission.coSpeaker.name}"/>
                                            </c:if>
                                        </td>
                                        <c:if test="${beta[i.count-1].startTime eq talk.startTime}">
                                            <c:set var="talk" value="${beta[i.count-1]}"/>
                                            <td>
                                                <a href="/agenda/${talk.id}">${talk.submission.title}</a>
                                                <br/>
                                                <c:if test="${not empty talk.submission and not empty talk.submission.speaker}">
                                                    <c:out value="${talk.submission.speaker.name}"/>
                                                </c:if>
                                                <c:if test="${not empty talk.submission and not empty talk.submission.coSpeaker}">
                                                    <br/>&& <br/>
                                                    <c:out value="${talk.submission.coSpeaker.name}"/>
                                                </c:if>
                                            </td>
                                        </c:if>
                                        <c:if test="${workshops[workshopsCounter].startTime eq talk.startTime}">
                                            <c:set var="talk" value="${workshops[workshopsCounter]}"/>
                                            <c:set var="workshopsCounter" value="${workshopsCounter + 1}"/>
                                            <td rowspan="${talk.endTime eq alpha[i.count].startTime ? 1 : 3}">
                                                <a href="/agenda/${talk.id}">${talk.submission.title}</a>
                                                <br/>
                                                <c:if test="${not empty talk.submission and not empty talk.submission.speaker}">
                                                    <c:out value="${talk.submission.speaker.name}"/>
                                                </c:if>
                                                <c:if test="${not empty talk.submission and not empty talk.submission.coSpeaker}">
                                                    <br/>&&<br/>
                                                    <c:out value="${talk.submission.coSpeaker.name}"/>
                                                </c:if>
                                                <c:if test="${talk.submission.type == SessionType.DEEP_DIVE}">
                                                    <br/><c:out value="(Deep Dive)"/>
                                                </c:if>
                                            </td>
                                        </c:if>
                                    </tr>
                                </c:if>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <table class="tcenter" id="agenda-2" style="display:none;" width="100%">
                        <thead>
                        <tr>
                            <th></th>
                            <th>Hall A</th>
                            <th>Hall B</th>
                            <th>Workshops</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="talk" items="${alpha}" varStatus="i">
                            <c:set var="talkDay">
                                <fmt:formatDate pattern="D" value="${talk.startDateTime}" />
                            </c:set>
                            <%-- e.g. second day talk --%>
                            <c:if test="${talkDay eq (firstDay+1)}">
                                <tr>
                                    <td>
                                        <fmt:formatDate pattern="HH:mm" value="${talk.startDateTime}" />
                                        <br/>
                                        <fmt:formatDate pattern="HH:mm" value="${talk.endDateTime}" />
                                    </td>
                                    <td colspan=" ${beta[i.count-1].startTime eq talk.startTime ? (empty talk.submission ? 2 : 1 ) : 2}">
                                        <c:choose>
                                            <c:when test="${empty talk.submission}">
                                                ${talk.title}
                                            </c:when>
                                            <c:otherwise>
                                                <a href="/agenda/${talk.id}">${talk.submission.title}</a>
                                            </c:otherwise>
                                        </c:choose>
                                        <br/>
                                        <c:if test="${not empty talk.submission and not empty talk.submission.speaker}">
                                            <c:out value="${talk.submission.speaker.name}"/>
                                        </c:if>
                                        <c:if test="${not empty talk.submission and not empty talk.submission.coSpeaker}">
                                            <br/>&& <br/>
                                            <c:out value="${talk.submission.coSpeaker.name}"/>
                                        </c:if>
                                    </td>
                                    <c:if test="${beta[i.count-1].startTime eq talk.startTime}">
                                        <c:set var="talk" value="${beta[i.count-1]}"/>
                                        <td>
                                            <a href="/agenda/${talk.id}">${talk.submission.title}</a>
                                            <br/>
                                            <c:if test="${not empty talk.submission and not empty talk.submission.speaker}">
                                                <c:out value="${talk.submission.speaker.name}"/>
                                            </c:if>
                                            <c:if test="${not empty talk.submission and not empty talk.submission.coSpeaker}">
                                                <br/>&& <br/>
                                                <c:out value="${talk.submission.coSpeaker.name}"/>
                                            </c:if>
                                        </td>
                                    </c:if>
                                    <c:if test="${workshops[workshopsCounter].startTime eq talk.startTime}">
                                        <c:set var="talk" value="${workshops[workshopsCounter]}"/>
                                        <c:set var="workshopsCounter" value="${workshopsCounter + 1}"/>
                                        <td rowspan="${talk.endTime eq alpha[i.count].startTime ? 1 : 3}">
                                            <a href="/agenda/${talk.id}">${talk.submission.title}</a>
                                            <br/>
                                            <c:if test="${not empty talk.submission and not empty talk.submission.speaker}">
                                                <c:out value="${talk.submission.speaker.name}"/>
                                            </c:if>
                                            <c:if test="${not empty talk.submission and not empty talk.submission.coSpeaker}">
                                                <br/>&& <br/>
                                                <c:out value="${talk.submission.coSpeaker.name}"/>
                                            </c:if>
                                            <c:if test="${talk.submission.type == SessionType.DEEP_DIVE}">
                                                <br/><c:out value="(Deep Dive)"/>
                                            </c:if>
                                        </td>
                                    </c:if>
                                </tr>
                            </c:if>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:if>
                <!-- Agenda Content End -->
            </div>
        </div>
    </div>
</section>


<user:footer/>
</body>
</html>
