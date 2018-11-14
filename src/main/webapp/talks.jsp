<%@ page trimDirectiveWhitespaces="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>

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

    <!-- Page Description and Author -->
    <meta name="description" content="jPrime 2019">
    <meta name="author" content="jPrime">

    <user:pageJavaScriptAndCss/>
</head>

<body>

<!-- Container -->
<div id="container">


    <user:header/>


    <!-- Start Content -->
    <div id="content">
        <div class="container">
            <div class="row blog-post-page">
                <div class="col-md-11 blog-box">

                    <c:if test="${empty talk}">
                        <div class="post-content">
                            <h2>Agenda</h2>
                        </div>
                    </c:if>

                    <!-- Start Single Post Area -->
                    <div class="blog-post gallery-post">
                        <!-- Start Single Post Content -->
                        <div class="post-content">
                            <h2>${talk.submission.title}</h2>
                            <ul class="post-meta">
                                <%--<li>By <a href="#">${article.author.firstName} ${article.author.lastName}</a>  &nbsp;&nbsp;
                                <joda:format value="${article.createdDate}" pattern="dd-MM-yyyy"/>
                                </li>--%>
                            </ul>
                            <c:if test="${not agenda}"><p>The agenda is not yet available </p></c:if>
                            <c:if test="${agenda}"><p>The agenda for jPrime <joda:format pattern="YYYY" value="${firstDayDate}" /> is now available!</p>
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

                                <ul class="nav navbar-nav" style="float: none;">
                                    <li>
                                        <a href="javascript:return false;" onclick="select('link-1','agenda-1')" id="link-1" style="padding-top: 28px; padding-bottom: 28px;"><joda:format locale="en" pattern="EEEE (dd/MM/YYYY)" value="${firstDayDate}" /></a>

                                    </li>
                                    <li>
                                        <a href="javascript:return false;"  onclick="select('link-2','agenda-2')" id="link-2" style="padding-top: 28px; padding-bottom: 28px;"><joda:format locale="en" pattern="EEEE (dd/MM/YYYY)" value="${secondDayDate}"/> </a>
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
                                        <joda:format pattern="D" value="${alpha[0].startTime}" />
                                    </c:set>

                                    <c:set var="workshopsCounter" value="0"/>
                                    <c:forEach var="talk" items="${alpha}" varStatus="i">
                                        <c:set var="talkDay">
                                            <joda:format pattern="D" value="${talk.startTime}" />
                                        </c:set>
                                        <%-- e.g. first day talk --%>
                                        <c:if test="${talkDay eq firstDay}">
                                            <tr>
                                                <td>
                                                    <joda:format pattern="HH:mm" value="${talk.startTime}" />
                                                    <br/>
                                                    <joda:format pattern="HH:mm" value="${talk.endTime}" />
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
                                                        <c:out value="${talk.submission.speaker.firstName}"/>&nbsp;<c:out value="${talk.submission.speaker.lastName}"/>
                                                    </c:if>
                                                    <c:if test="${not empty talk.submission and not empty talk.submission.coSpeaker}">
                                                        <br/>&& <br/>
                                                        <c:out value="${talk.submission.coSpeaker.firstName}"/>&nbsp;<c:out value="${talk.submission.coSpeaker.lastName}"/>
                                                    </c:if>
                                                </td>
                                                <c:if test="${beta[i.count-1].startTime eq talk.startTime}">
                                                    <c:set var="talk" value="${beta[i.count-1]}"/>
                                                    <td>
                                                        <a href="/agenda/${talk.id}">${talk.submission.title}</a>
                                                        <br/>
                                                        <c:if test="${not empty talk.submission and not empty talk.submission.speaker}">
                                                            <c:out value="${talk.submission.speaker.firstName}"/>&nbsp;<c:out value="${talk.submission.speaker.lastName}"/>
                                                        </c:if>
                                                        <c:if test="${not empty talk.submission and not empty talk.submission.coSpeaker}">
                                                            <br/>&& <br/>
                                                            <c:out value="${talk.submission.coSpeaker.firstName}"/>&nbsp;<c:out value="${talk.submission.coSpeaker.lastName}"/>
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
                                                            <c:out value="${talk.submission.speaker.firstName}"/>&nbsp;<c:out value="${talk.submission.speaker.lastName}"/>
                                                        </c:if>
                                                        <c:if test="${not empty talk.submission and not empty talk.submission.coSpeaker}">
                                                            <br/>&& <br/>
                                                            <c:out value="${talk.submission.coSpeaker.firstName}"/>&nbsp;<c:out value="${talk.submission.coSpeaker.lastName}"/>
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
                                        <joda:format pattern="D" value="${talk.startTime}" />
                                    </c:set>
                                    <%-- e.g. second day talk --%>
                                    <c:if test="${talkDay eq (firstDay+1)}">
                                        <tr>
                                            <td>
                                                <joda:format pattern="HH:mm" value="${talk.startTime}" />
                                                <br/>
                                                <joda:format pattern="HH:mm" value="${talk.endTime}" />
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
                                                    <c:out value="${talk.submission.speaker.firstName}"/>&nbsp;<c:out value="${talk.submission.speaker.lastName}"/>
                                                </c:if>
                                                <c:if test="${not empty talk.submission and not empty talk.submission.coSpeaker}">
                                                    <br/>&& <br/>
                                                    <c:out value="${talk.submission.coSpeaker.firstName}"/>&nbsp;<c:out value="${talk.submission.coSpeaker.lastName}"/>
                                                </c:if>
                                            </td>
                                            <c:if test="${beta[i.count-1].startTime eq talk.startTime}">
                                                <c:set var="talk" value="${beta[i.count-1]}"/>
                                                <td>
                                                    <a href="/agenda/${talk.id}">${talk.submission.title}</a>
                                                    <br/>
                                                    <c:if test="${not empty talk.submission and not empty talk.submission.speaker}">
                                                        <c:out value="${talk.submission.speaker.firstName}"/>&nbsp;<c:out value="${talk.submission.speaker.lastName}"/>
                                                    </c:if>
                                                    <c:if test="${not empty talk.submission and not empty talk.submission.coSpeaker}">
                                                        <br/>&& <br/>
                                                        <c:out value="${talk.submission.coSpeaker.firstName}"/>&nbsp;<c:out value="${talk.submission.coSpeaker.lastName}"/>
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
                                                        <c:out value="${talk.submission.speaker.firstName}"/>&nbsp;<c:out value="${talk.submission.speaker.lastName}"/>
                                                    </c:if>
                                                    <c:if test="${not empty talk.submission and not empty talk.submission.coSpeaker}">
                                                        <br/>&& <br/>
                                                        <c:out value="${talk.submission.coSpeaker.firstName}"/>&nbsp;<c:out value="${talk.submission.coSpeaker.lastName}"/>
                                                    </c:if>
                                                </td>
                                            </c:if>
                                        </tr>
                                    </c:if>
                                </c:forEach>
                                </tbody>
                                </table>
                            </c:if>
                                </div>

                        </div>
                        <!-- End Single Post Content -->

                    </div>
                    <!-- End Single Post Area -->
                    <br/><br/>
                    <a href="" onclick="window.history.back()">Back</a>
                    <!-- currently not needed!!
                    <!-- Start Comment Area
                    <div id="comments">
                        <h2 class="comments-title">(4) Comments</h2>
                        <ol class="comments-list">
                            <li>
                                <div class="comment-box clearfix">
                                    <div class="avatar"><img alt="" src="images/avatar.png"/></div>
                                    <div class="comment-content">
                                        <div class="comment-meta">
                                            <span class="comment-by">John Doe</span>
                                            <span class="comment-date">February 15, 2013 at 11:39 pm</span>
                                            <span class="reply-link"><a href="#">Reply</a></span>
                                        </div>
                                        <p>At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis
                                            praesentium voluptatum deleniti atque corrupti quos dolores et quas
                                            molestias excepturi sint occaecati cupiditate non provident, similique sunt
                                            in culpa qui officia desrut mollitia animi, id est laborum et dolorum fuga.
                                            Et harum quidem rerum facilis est et expedita distinctio.</p>
                                    </div>
                                </div>

                                <ul>
                                    <li>
                                        <div class="comment-box clearfix">
                                            <div class="avatar"><img alt="" src="images/avatar.png"/></div>
                                            <div class="comment-content">
                                                <div class="comment-meta">
                                                    <span class="comment-by">John Doe</span>
                                                    <span class="comment-date">February 15, 2013 at 11:39 pm</span>
                                                    <span class="reply-link"><a href="#">Reply</a></span>
                                                </div>
                                                <p>At vero eos et accusamus et iusto odio dignissimos ducimus qui
                                                    blanditiis praesentium voluptatum deleniti atque corrupti quos
                                                    dolores et quas molestias excepturi sint occaecati cupiditate non
                                                    provident.</p>
                                            </div>
                                        </div>

                                        <ul>
                                            <li>
                                                <div class="comment-box clearfix">
                                                    <div class="avatar"><img alt="" src="images/avatar.png"/></div>
                                                    <div class="comment-content">
                                                        <div class="comment-meta">
                                                            <span class="comment-by">John Doe</span>
                                                            <span class="comment-date">February 15, 2013 at 11:39 pm</span>
                                                            <span class="reply-link"><a href="#">Reply</a></span>
                                                        </div>
                                                        <p>At vero eos et accusamus et iusto odio dignissimos ducimus
                                                            qui blanditiis praesentium voluptatum deleniti atque
                                                            corrupti quos dolores et quas molestias excepturi.</p>
                                                    </div>
                                                </div>
                                            </li>
                                        </ul>

                                    </li>
                                </ul>
                            </li>

                            <li>
                                <div class="comment-box clearfix">
                                    <div class="avatar"><img alt="" src="images/avatar.png"/></div>
                                    <div class="comment-content">
                                        <div class="comment-meta">
                                            <span class="comment-by">John Doe</span>
                                            <span class="comment-date">February 15, 2013 at 11:39 pm</span>
                                            <span class="reply-link"><a href="#">Reply</a></span>
                                        </div>
                                        <p>At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis
                                            praesentium voluptatum deleniti atque corrupti quos dolores et quas
                                            molestias excepturi sint occaecati cupiditate non provident, similique sunt
                                            in culpa qui officia desrut mollitia animi, id est laborum et dolorum fuga.
                                            Et harum quidem rerum facilis est et expedita distinctio.</p>
                                    </div>
                                </div>
                            </li>

                        </ol>

                        <!-- Start Respond Form
                        <div id="respond">
                            <h2 class="respond-title">Leave a reply</h2>

                            <form action="#">
                                <div class="row">
                                    <div class="col-md-4">
                                        <label for="author">Name<span class="required">*</span></label>
                                        <input id="author" name="author" type="text" value="" size="30"
                                               aria-required="true">
                                    </div>
                                    <div class="col-md-4">
                                        <label for="email">Email<span class="required">*</span></label>
                                        <input id="email" name="author" type="text" value="" size="30"
                                               aria-required="true">
                                    </div>
                                    <div class="col-md-4">
                                        <label for="url">Website<span class="required">*</span></label>
                                        <input id="url" name="url" type="text" value="" size="30" aria-required="true">
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-12">
                                        <label for="comment">Add Your Comment</label>
                                        <textarea id="comment" name="comment" cols="45" rows="8"
                                                  aria-required="true"></textarea>
                                        <input name="submit" type="submit" id="submit" value="Submit Comment">
                                    </div>
                                </div>
                            </form>
                        </div>
                        <!-- End Respond Form

                    </div>
                    <!-- End Comment Area -->

                </div>


                <user:sidebar/>

            </div>

        </div>
    </div>
    <!-- End content -->


    <!-- Start Footer -->
    <<jsp:directive.include file="footer.jsp" />
    <!-- End Footer -->

</div>
<!-- End Container -->

<!-- Go To Top Link -->
<a href="#" class="back-to-top"><i class="fa fa-angle-up"></i></a>

<div id="loader">
    <div class="spinner">
        <div class="dot1"></div>
        <div class="dot2"></div>
    </div>
</div>


</body>
</html>