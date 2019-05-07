<%--
  Created by IntelliJ IDEA.
  User: teo
  Date: 02.04.19
  Time: 20:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script>
      if ('serviceWorker' in navigator) {
        window.addEventListener('load', function () {
          navigator.serviceWorker.register('/sw.js', { scope: '/'});
        })
      }
    </script>
</head>
<body>
<div id="pwa-container"></div>
<script src="/js/pwa.js"></script>
</body>
</html>
