<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>



<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">

    <title>微利-微聚利量-JS授权</title>
</head>

<body>
<div class="loginForm">
    <form method="post" action="index.jsp#" onsubmit="return Dcheck();">
        <div style="padding-top:50px;padding-left:25px;">

            <a href="${authorizeUrl}">
                <img src="/images/wxdl.png"/>
            </a>
        </div>
    </form>
</div>
</body>
</html>