<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
           uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/user"%>

<!doctype html>
<!--[if IE 8 ]><html class="ie ie8" lang="en"> <![endif]-->
<!--[if (gte IE 9)|!(IE)]><html lang="en" class="no-js"> <![endif]-->
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <jsp:directive.include file="../../theme-colors.jsp" />

    <title>Edit submission</title>
    <script type="text/javascript" src="/js/niceforms.js"></script>
    <script type="text/javascript" src="/js/jquery.min.js"></script>
    <link rel="stylesheet" type="text/css" media="all"
          href="/css/niceforms-default.css" />
    <link rel="stylesheet" type="text/css" media="all" href="/css/admin.css" />
</head>
<body>
<admin:menu />
<user:cfp action="/admin/submission/edit"  admin="true"/>


<script type="text/javascript">
    $(document).ready(function () {
        var coSpeakerButton = $("#toggleCoSpeaker");
        var buttonCaption = coSpeakerButton.text();
        buttonCaption == "Add co speaker" ? $("#coSpeaker").hide() : $("#coSpeaker").show();

        coSpeakerButton.click(function () {
            var coSpeakerDiv = $("#coSpeaker");
            coSpeakerDiv.toggle();
            var coSpeakerButton = $("#toggleCoSpeaker");
            var buttonCaption = coSpeakerButton.text();

            if (buttonCaption == "Remove co speaker") {
                coSpeakerButton.text("Add co speaker");
                $("input[id^='coSpeaker']").val("");
            } else {
                coSpeakerButton.text("Remove co speaker");
            }
        });
    });
</script>

</body>
</html>
