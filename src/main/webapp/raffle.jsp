<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
           uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user"%>

<!doctype html>
<!--[if IE 8 ]><html class="ie ie8" lang="en"> <![endif]-->
<!--[if (gte IE 9)|!(IE)]><html lang="en" class="no-js"> <![endif]-->
<html lang="en">
<head>

	<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>
    <!-- Basic -->
    <title>Raffle</title>

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
                <div class="col-md-9 blog-box">

                    <!-- Start Single Post Area -->
                    <div class="blog-post gallery-post">

                        <!-- Start Single Post Content -->
                        <h2>Raffle</h2>
                        <div class="post-content col-md-7 col-md-offset-5">
                            
                            
                            <p> <br/> 
                            <a href="javascript:void(0)" onclick="shuffle(this)" class="slider btn btn-primary">Start</a>
                            <h1 id="shuffle-result">Name (Company)</h1>
                        </div>
                        <!-- End Single Post Content -->

                    </div>
                    <!-- End Single Post Area -->

                </div>



                <user:sidebar/>

            </div>

        </div>
    </div>
    <!-- End content -->


    <jsp:directive.include file="footer.jsp" />
    <script type="text/javascript">
    
    var shuffling = false;
    var visitors = ${visitors};
    
    function shuffle(el){
    	shuffling = !shuffling;
    	if(shuffling) {
    		$(el).text("Stop");
    		setTimeout(getRandomVisitor, 100);
    	} else {
    		$(el).text("Start");
    	}
    }
    	
    function getRandomVisitor(){
    	var visitor = visitors[Math.floor(Math.random()*visitors.length)];
    	$("#shuffle-result").text(" " + visitor.name + " (" + visitor.company + ")" )
    	if(shuffling) {
    		setTimeout(getRandomVisitor, 100);
    	}
    }
    </script>
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