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

    <base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>
    <!-- Basic -->
    <title>jPrime | Post</title>

    <!-- Define Charset -->
    <meta charset="utf-8">

    <!-- Responsive Metatag -->
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

    <!-- Page Description and Author -->
    <meta name="description" content="jPrime 2018">
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
                            <h2>No talk information available</h2>
                        </div>
                    </c:if>

                    <a href="" onclick="window.history.back()">Back</a><br/><br/>
                    <!-- Start Single Post Area -->
                    <div class="blog-post gallery-post">
                        <!-- Start Single Post Content -->
                        <div class="post-content">
                            <h2>${talk.submission.title} (<joda:format pattern="HH:mm" value="${talk.startTime}" /> - <joda:format pattern="HH:mm" value="${talk.endTime}" /> on <joda:format locale="en" pattern="EEEE" value="${talk.endTime}" />)</h2>
                            <ul class="post-meta">
                                <%--<li>By <a href="#">${article.author.firstName} ${article.author.lastName}</a>  &nbsp;&nbsp;
                                <joda:format value="${article.createdDate}" pattern="dd-MM-yyyy"/>
                                </li>--%>
                            </ul>
                            <p>
                            
                            <div class="col-md-3 col-sm-6 col-xs-12 animated" >
	                        <div class="team-member modern">
	                            <!-- Memebr Photo, Name & Position -->
	                            <div class="member-photo">
	                                <img alt="" src="/image/speaker/${talk.submission.speaker.id}">
	
	                                <div class="member-name">${talk.submission.speaker.firstName}&nbsp;${talk.submission.speaker.lastName}<span>${talk.submission.speaker.headline}</span>
	                                </div>
	                            </div>
	                            <div class="member-socail" style="text-align: left">
	                                <a class="twitter" href="https://twitter.com/${talk.submission.speaker.twitter}/"><i class="fa fa-twitter"></i></a>
	                            </div>
	                        </div>
                            <c:if test="${not empty talk.submission.coSpeaker}">
                                <div class="team-member modern">
                                    <!-- Memebr Photo, Name & Position -->
                                    <div class="member-photo">
                                        <img alt="" src="/image/speaker/${talk.submission.coSpeaker.id}">

                                        <div class="member-name">${talk.submission.speaker.firstName}&nbsp;${talk.submission.coSpeaker.lastName}<span>${talk.submission.coSpeaker.headline}</span>
                                        </div>
                                    </div>
                                    <div class="member-socail" style="text-align: left">
                                        <a class="twitter" href="https://twitter.com/${talk.submission.coSpeaker.twitter}/"><i class="fa fa-twitter"></i></a>
                                    </div>
                                </div>
                            </c:if>
	                    </div>
	
						<p>
						${talk.submission.description}
						</p>
						
						<p><b>Level:</b><br/>
						${talk.submission.level}
						</p>

                        <c:choose>
                            <c:when test="${not empty talk.submission.coSpeaker}">
                                <p>
                                    <b>Speakers:</b><br/>
                                    <p>
                                        ${talk.submission.speaker.bio}
                                    </p>
                                    <p>
                                            ${talk.submission.coSpeaker.bio}
                                    </p>
                                </p>
                            </c:when>
                            <c:otherwise>
                                <p>
                                    <b>Bio:</b><br/>${talk.submission.speaker.bio}
                                </p>
                            </c:otherwise>
                        </c:choose>

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
    <jsp:directive.include file="footer.jsp" />
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