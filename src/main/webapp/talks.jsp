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
                            <h2>No article available</h2>
                        </div>
                    </c:if>

                    <a href="" onclick="window.history.back()">Back</a><br/><br/>
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
                            <p>
                            
                            The agenda is not yet available !
                            <%--
                            The agenda is not yet complete &amp; final, some changes may occur !
							
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
							        <a href="javascript:return false;" onclick="select('link-1','agenda-1')" id="link-1" style="padding-top: 28px; padding-bottom: 28px;">Thursday, 26th May, 2016 </a>
							
							    </li>
							    <li>
							        <a href="javascript:return false;"  onclick="select('link-2','agenda-2')" id="link-2" style="padding-top: 28px; padding-bottom: 28px;">Friday, 27th May, 2016</a>
							    </li>
							</ul>
							
							</br/>
							
							<table class="tcenter" id="agenda-1">
							<thead>
							<tr>
							<th></th>
							<th>Track 1 (Beta)</th>
							<th>Track 2 (Alpha)</th>
							</tr>
							</thead>
							<tbody>
							<tr>
							<td>8:00 <br> 9:00</td>
							<td colspan="2">Registration & Coffee</td>
							</tr>
							<tr>
							<td>9:00 <br> 9:30</td>
							<td colspan="2">Opening<br> </td>
							</tr>
							<tr>
							<td>9:30 <br> 10:20</td>
							<td colspan="2"><a href="/nav/article/64">Time to Code: the Art is Distraction Free Programming</a> <br> Kees Jan Koster </td>
							</tr>
							<tr>
							<td>10:20 <br> 10:30</td>
							<td colspan="2">Break</td>
							</tr>
							<tr>
							<td>10:30 <br> 11:20</td>
							<td><a href="/nav/article/52">Kotlin  - Ready for production</a> <br>Hadi Hariri</td>
							<td><a href="/nav/article/53">Using Docker to deliver Java Enterprise Applications, <br/>one year later... </a> <br> Petyo Dimitrov</td>
							</tr>
							<tr>
							<td>11:20 <br> 11:30</td>
							<td colspan="2">Break</td>
							</tr>
							<tr>
							<td>11:30 <br> 12:20</td>
							<td><a href="/nav/article/54">JSR377: What's up and what's next
							</a> <br> Andres Almirai</td>
							<td><a href="/nav/article/55">SnoopEE - The Lean and Simple Discovery Service <br/> for Java EE</a> <br>
							Ivar Grimstad</td>
							
							</tr>
							<tr>
							<td>12:20 <br> 13:30</td>
							<td colspan="2">Lunch break</td>
							</tr>
							<tr>
							<td>13:30 <br> 14:20</td>
							<td><a href="/nav/article/56">What's not new in modular Java?</a><br>
							Milen Dyankov</td>
							<td><a href="/nav/article/57">Databases - the choice is yours
							</a> <br> Philipp Krenn</td>
							</tr>
							<tr>
							<td>14:20 <br> 14:30</td>
							<td colspan="2">Break</td>
							</tr>
							<tr>
							<td>14:30 <br> 15:20</td>
							<td><a href="/nav/article/58">Home Automation Reloaded</a><br>Kai Kreuzer</td>
							<td><a href="/nav/article/59">DIY Java &amp; Kubernetes</a> <br>
							Panche Chavkovski</td>
							</tr>
							<tr>
							<td>15:20 <br> 16:00</td>
							<td colspan="2">Coffee Break</td>
							</tr>
							<tr>
							<td>16:00 <br> 16:50</td>
							<td><a href="/nav/article/60">Going Reactive with RxJava
							</a><br>Hrvoje Crnjak</td>
							<td><a href="/nav/article/61">Migrating 25K lines of Ant scripting to Gradle</a> <br>Hanno Embregts</td>
							</tr>
							<tr>
							<td>16:50 <br> 17:00</td>
							<td colspan="2">Break</td>
							</tr>
							<tr>
							<td>17:00 <br> 17:50</td>
							<td><a href="/nav/article/62">DI-Frameworks - the hidden pearls</a> <br>Sven Ruppert</td>
							<td><a href="/nav/article/63">Reactive Java Robotics and IoT</a> <br>Trayan Iliev</td>
							</tr>
							<tr>
							<td>17:50 <br> 18:00 </td>
							<td colspan="2">Break &amp; Beer</td>
							</tr>
							<tr>
							<td>18:00 <br> 19:00 </td>
							<td colspan="2">Raffle &amp; Beer</td>
							</tr>
							<tr>
							<td>19:00 <br> 21:00 </td>
							<td colspan="2">Only Beer :)</td>
							</tr>
							</tbody>
							</table>
							</div>
							
							
							<table class="tcenter" id="agenda-2" style="display:none;">
							<thead>
							<tr>
							<th></th>
							<th>Track 1 (Beta)</th>
							<th>Track 2 (Alpha)</th>
							</tr>
							</thead>
							<tbody>
							<tr>
							<td>8:00 <br> 9:00</td>
							<td colspan="2">Coffee</td>
							</tr>
							<tr>
							<td>9:00 <br> 9:50</td>
							<td><a href="/nav/article/65">JDK 8: Lessons Learnt With Lambdas and Streams</a> <br>Simon Ritter</td>
							<td><a href="/nav/article/66">Apache Brooklyn - run your application in the cloud,<br/> any cloud !</a> <br> Svetoslav Neykov</td>
							</tr>
							<tr>
							<td>9:50 <br> 10:00</td>
							<td colspan="2">Break</td>
							</tr>
							<tr>
							<td>10:00 <br> 10:50</td>
							<td><a href="/nav/article/67">Go for Java Developers
							</a> <br>Stoyan Rachev</td>
							<td><a href="/nav/article/68">RxJava in Microservices World</a> <br>
							Piotr Kafel</td>
							
							</tr>
							<tr>
							<td>10:50 <br> 11:00</td>
							<td colspan="2">Break</td>
							</tr>
							<tr>
							<td>11:00 <br> 11:50</td>
							<td><a href="/nav/article/69">Sane Sharding with Akka Cluster</a><br>
							Michal Plachta</td>
							<td><a href="/nav/article/70">An incremental approach to Formal Methods <br/> in enterprise Java applications
							</a> <br>Teodor Parvanov</td>
							</tr>
							<tr>
								<td>11:50 <br> 12:00</td>
								<td colspan="2">Thank you &amp; closing</td>
							</tr>
							</tbody>
							</table>
							</div> --%>

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