<%--
  Created by IntelliJ IDEA.
  User: worgen
  Date: 2015/9/9
  Time: 15:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"  isELIgnored="false"%>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta http-equiv="Content-Type" Content="text/xml;charset=utf-8">
  <meta name="viewport" content="width=640, user-scalable=no, initial-scale=0.5, maximum-scale=0.5, minimum-scale=0.5">
  <meta content="telephone=no" name="format-detection" />
  <meta content="email=no" name="format-detection" />
  <title>台球会员卡</title>
  <!---->
  <link rel="stylesheet" href="/css/jquery.mobile.min.css">
  <link rel="stylesheet" href="/css/member-data.css">
  <script type="text/javascript" src="/js/jquery-1.8.1.min.js"></script>
  <script type="text/javascript" src="/js/aSelect.js"></script>
  <script type="text/javascript" src="/js/aLocation.js"></script>
  <script type="text/javascript" src="/js/checked.js"></script>
</head>

<body>

<div class="top">
  <img src="/images/top.png" alt="">

</div>
<form method="post" action="" id="idSaveForm">

  <div class="mainDIV">
    <div class="box name">
      <label>姓名:</label>
      <input type="text" name="name" id="name" value="${vipMember.name}" placeholder="请输入姓名">
    </div>
    <div class="box phone">
      <label>手机:</label>
      <input type="text" name="tel" id="tel"  value="${vipMember.tel}" placeholder="请输入手机号">
    </div>
    <div class="box gender">
      <label for="sex">性别:</label>
      <select name="sex" id="sex">
        <option value="0">请选择性别</option>
        <option value="1">男</option>
        <option value="2">女</option>
      </select>
    </div>
    <div class="box birthday">
      <label>生日:</label>
      <input type="date" name="birthday" id="birthday"  value="${vipMember.birthday}">
    </div>
    <label>地区:</label>
    <div class="box select_box">

      <div class="province">
        <select name="addrProvince" class="select" id="addrProvince" value="${vipMember.addrProvince}"></select>
      </div>
      <div class="city">
        <select name="addrCity" class="select" id="addrCity" value="${vipMember.addrCity}"></select>
      </div>
      <div class="area">
        <select name="addrArea" class="select" id="addrArea" value="${vipMember.addrArea}"></select>
      </div>
    </div>
    <div class="box address">
      <label>详细地址:</label>
      <input type="text" name="addrDetail" id="addrDetail" value="${vipMember.addrDetail}" placeholder="请输入详细地址">
    </div>

    <input type="button" name="" id="" onclick="save()" value="提交">

  </div>
</form>
<script>
  $(function () {
    var sel = aSelect({
      data: aLocation
    });
//    sel.bind('#addrProvince', '110000');
//    sel.bind('#addrCity', '0');
//    sel.bind('#addrArea', '0');

    sel.bind('#addrProvince', '${vipMember.addrProvince}');
    sel.bind('#addrCity', '${vipMember.addrCity}');
    sel.bind('#addrArea', '${vipMember.addrArea}');
    $('#sex').val(${vipMember.sex});
    <%--$('#addrProvince').val('${vipMember.addrProvince}');--%>
    <%--$('#addrCity').val(${vipMember.addrCity});--%>
    <%--$('#addrArea').val(${vipMember.addrArea});--%>

  })


</script>
<script>

  function saveCheck(){
    //姓名
    var nameVal = $("#name").val();
    if( nameVal == "" ){
      alert("请输入姓名");
      return false;
    }
    //手机
    var telVal = $("#tel").val();
    var isTel = isMobil(telVal);
    if(isTel==false){
      alert("请输入正确的手机号");
      return false;
    }
    //性别
    var sexVal = $("#sex").val();
    if( sexVal == 0 ){
      alert("请选择性别");
      return false;
    }
    //生日
    var bdayVal = $("#bday").val();
    if( bdayVal == "" ){
      alert("请选择生日");
      return false;
    }

    return true;
  }
  //注册完成方法
  function save(){
    if( saveCheck() == false ) return;

    jQuery.ajax({
      url: "/vip/ajaxImproveVipInfo.do?business_id=${businessID}&member_id=${memberID}",
      type: "post",
      dataType: "json",
      cache: false,
      async: false,
      data:$('#idSaveForm').serialize(),
      success: function(ret){
        if(ret != 0){
          alert("出错了，请确认");
        }else{
          alert("保存成功");
          location.href = "/vip/vipHomePage.do?business_id=${businessID}&member_id=${memberID}";
        }
      },
      error:function(){
        alert("error");
      }
    })
  }
</script>
</body>

</html>
