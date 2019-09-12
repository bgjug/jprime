<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

<!-- Header Area wrapper Starts -->
<header id="header-wrap">
    <!-- Navbar Start -->
    <nav class="navbar navbar-expand-lg fixed-top scrolling-navbar">
        <div class="container">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#main-navbar" aria-controls="main-navbar" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                    <span class="icon-menu"></span>
                    <span class="icon-menu"></span>
                    <span class="icon-menu"></span>
                </button>
                <a href="/" class="navbar-brand"><img src="images/jprime-small.png" alt=""></a>
            </div>
            <div class="collapse navbar-collapse" id="main-navbar">
                <ul class="navbar-nav mr-auto w-100 justify-content-end">
<%--                    <li class="nav-item dropdown active">--%>
<%--                        <a class="nav-link dropdown-toggle" href="#" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">--%>
<%--                            Home <i class="fa fa-angle-down"></i>--%>
<%--                        </a>--%>
<%--                        <div class="dropdown-menu">--%>
<%--                            <a class="dropdown-item active" href="index.html">Home 1</a>--%>
<%--                            <a class="dropdown-item" href="index-2.html">Home 2</a>--%>
<%--                            <a class="dropdown-item" href="index-3.html">Home 3</a>--%>
<%--                        </div>--%>
<%--                    </li>--%>
                    <li class="nav-item <c:if test='${"/index.jsp" eq pageContext.request.requestURI}'> active</c:if>">
                        <a class="nav-link" href="/">
                            Home
                        </a>
                    </li>
                    <li class="nav-item <c:if test='${"/proposal.jsp" eq pageContext.request.requestURI}'> active</c:if>">
                        <a class="nav-link" href="/cfp">
                            Call for papers
                        </a>
                    </li>
                    <li class="nav-item <c:if test='${"/tickets-epay-register.jsp" eq pageContext.request.requestURI}'> active</c:if>">
                        <a class="nav-link" href="/tickets">
                            Tickets
                        </a>
                    </li>
                    <li class="nav-item <c:if test='${"/team.jsp" eq pageContext.request.requestURI}'> active</c:if>">
                        <a class="nav-link" href="/team">
                            Team
                        </a>
                    </li>
                    <li class="nav-item <c:if test='${"/venue.jsp" eq pageContext.request.requestURI}'> active</c:if>">
                        <a class="nav-link" href="/venue">
                            Useful info
                        </a>
                    </li>
                    <li class="nav-item <c:if test='${"/speakers.jsp" eq pageContext.request.requestURI}'> active</c:if>">
                        <a class="nav-link" href="/speakers">
                            Speakers
                        </a>
                    </li>
                    <li class="nav-item <c:if test='${"/agenda.jsp" eq pageContext.request.requestURI}'> active</c:if>">
                        <a class="nav-link" href="/agenda">
                            Agenda
                        </a>
                    </li>
                    <c:forEach var="tag" items="${tags}" varStatus="i">
                        <c:choose>
                            <%-- this part has invalid XML by DESIGN I need to add li and div only ones --%>
                            <c:when test="${fn:startsWith(tag.name, '20')}">
                                <c:if test="${i.count eq 1}">
                                    <li class="nav-item dropdown">
                                        <a class="nav-link dropdown-toggle" href="#" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                            Previous years<i class="fa fa-angle-down"></i>
                                        </a>
                                        <div class="dropdown-menu">
                                </c:if>
                                        <a class="dropdown-item" href="/nav/${tag.name}"><c:out value="${tag.name}"/></a>
                                <%-- I dont know when we should close them, it should be after the LAST year tag, but.. :D:D so fck it - ignore --%>
<%--                                        </div>--%>
<%--                                    </li>--%>
                            </c:when>
                            <%-- end of this part --%>
                            <c:otherwise>
                                <li class="nav-item">
                                    <a class="nav-link" href="/nav/${tag.name}">
                                        <c:out value="${tag.name}"/>
                                    </a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </ul>
            </div>
        </div>

        <!-- Mobile Menu Start -->
        <ul class="mobile-menu">
<%--            <li>--%>
<%--                <a class="active" href="#">--%>
<%--                    Home--%>
<%--                </a>--%>
<%--                <ul class="dropdown">--%>
<%--                    <li>--%>
<%--                        <a class="active" href="index.html">Home 1</a>--%>
<%--                    </li>--%>
<%--                    <li>--%>
<%--                        <a href="index-2.html">Home 2</a>--%>
<%--                    </li>--%>
<%--                    <li>--%>
<%--                        <a href="index-3.html">Home 3</a>--%>
<%--                    </li>--%>
<%--                </ul>--%>
<%--            </li>--%>
            <li>
                <a <c:if test='${"/index.jsp" eq pageContext.request.requestURI}'> class="active"</c:if> href="/">Home </a>
            </li>
            <li>
                <a <c:if test='${"/proposal.jsp" eq pageContext.request.requestURI}'> class="active"</c:if> href="/cfp">Call for papers</a>
            </li>
            <li>
                <a <c:if test='${"/tickets-epay-register.jsp" eq pageContext.request.requestURI}'> class="active"</c:if> href="/tickets">Tickets</a>
            </li>
            <li>
                <a <c:if test='${"/team.jsp" eq pageContext.request.requestURI}'> class="active"</c:if> href="/team">The Team</a>
            </li>

            <li>
                <a <c:if test='${"/venue.jsp" eq pageContext.request.requestURI}'> class="active"</c:if> href="/venue">Useful info</a>
            </li>

            <li>
                <a <c:if test='${"/speakers.jsp" eq pageContext.request.requestURI}'> class="active"</c:if> href="/speakers">Speakers</a>
            </li>

            <li>
                <a <c:if test='${"/agenda.jsp" eq pageContext.request.requestURI}'> class="active"</c:if> href="/agenda">Agenda</a>
            </li>

            <c:forEach var="tag" items="${tags}">
                <li>
                    <a href="/nav/${tag.name}"><c:out value="${tag.name}"/></a>
                </li>
            </c:forEach>
        </ul>
        <!-- Mobile Menu End -->
    </nav>
    <!-- Navbar End -->
</header>

