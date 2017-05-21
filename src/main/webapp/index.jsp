<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf8"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>

<head>
    <base href="<%=basePath%>">
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>微利：微聚利量</title>
    <script type="text/javascript" src="js/jquery-1.8.1.min.js"></script>
    <script type="text/javascript" src="js/aSelect.js"></script>
    <script type="text/javascript" src="js/aLocation.js"></script>
    <script type="text/javascript" src="js/date.js"></script>

</head>
<body>
<script>

    $().ready(function(){
        new dater({
            selectYear:document.getElementById("selectYear"),
            selectMonth:document.getElementById("selectMonth"),
            selectDate:document.getElementById("selectDate"),
            minDat: new Date("1900/1/1"),
            maxDat: new Date(),
            curDat: new Date("1990/01/01")
        }).init();
    });

    $(function () {
        var sel = aSelect({data: aLocation});
        sel.bind('#selectProvince', '110000');
        sel.bind('#selectCity', '0');
        sel.bind('#selectArea', '0');
    })
</script>
<h2>Hello World!</h2>

<div class="box select_box">
    <div>
        <select name="addr_prov" class="select" id="selectProvince"></select>
    </div>
    <div>
        <select name="addr_city" class="select" id="selectCity"></select>
    </div>
    <div>
        <select name="addr_area" class="select" id="selectArea"></select>
    </div>
</div>

<dt>生 日：</dt>
<dd>
    <div class="box select_box">
        <div>
            <select name="birth_year" readonly="readonly"                                            class="select" id="selectYear" value="1990"><!--auth Eric_wu--></select>
        </div>
        <div>
            <select name="birth_month" readonly="readonly"                                            class="select" id="selectMonth" value="01"><!--auth Eric_wu--></select>
        </div>
        <div>
            <select name="birth_date" readonly="readonly"                                            class="select" id="selectDate" value="01"><!--auth Eric_wu--></select>
        </div>
    </div>
</dd>

</body>
</html>