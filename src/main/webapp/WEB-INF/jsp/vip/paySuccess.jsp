<%--
  Created by IntelliJ IDEA.
  User: worgen
  Date: 2015/9/10
  Time: 18:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"  isELIgnored="false"%>
<html>
<head>
    <title></title>
</head>
<body onselectstart="return true;" ondragstart="return false;">
<div class="container success">

  <header>
    <div class="desc tbox">
      <div>
        <span class="img">&nbsp;</span>
      </div>
      <div>
        <label>
          付款成功！<br/>
          付款金额<r>
          ${fee}</r>元
        </label>
      </div>
    </div>
  </header>
  <div class="body">
    <div class="group_btn2">
      <h6>余额：<label>${balance}元</label></h6>
      <ul>
        <li>
          <a href="/vip/vipHomePage.do?business_id=${businessID}&member_id=${memberID}" class="btn">确&nbsp;&nbsp;定</a>
        </li>
      </ul>
    </div>
  </div>

</div></body>
</html>
