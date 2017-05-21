<%--
  Created by IntelliJ IDEA.
  User: worgen
  Date: 2015/9/9
  Time: 16:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<html>
<head>
  <meta http-equiv="Content-Type" Content="text/html;charset=utf-8">
  <meta name="viewport" content="width=640, user-scalable=no, initial-scale=0.5, maximum-scale=0.5, minimum-scale=0.5">
  <meta content="telephone=no" name="format-detection" />
  <meta content="email=no" name="format-detection" />
  <title>台球会员卡</title>
  <link rel="stylesheet" type="text/css" href="/css/lixue.css"/>
  <script type="text/javascript" src="/js/jquery-1.8.1.min.js"></script>
  <script type="text/javascript" src="/js/dialog_min.js"></script>
  <script type="text/javascript" src="/js/main.js"></script>

</head>
<body>
<div class="main">
  <img class="pic1" src="/images/one-banner.png"/>
  <div class="btn-group">
    <a href="javascript:charge()">
      <span class="btn-left">充值</span>
    </a>
    <a href="javascript:pay()">
      <span class="btn-right">付款</span>
    </a>
  </div>
  <ul>
    <%--<a href="/vipPrivilege.do?business_id=${businessID}&member_id=${memberID}">--%>
      <li class="a4" id="idVipPrivilege"><span>会员优惠</span></li>
    <%--</a>--%>
    <%--<a href="/integralExchage.do?business_id=${businessID}&member_id=${memberID}">--%>
      <li class="a5" id="idIntegralExchage">积分兑换</li>
    <%--</a>--%>
    <a href="/vip/improveVipInfo.do?business_id=${businessID}&member_id=${memberID}">
      <li class="a6">完善会员卡资料</li>
    </a>
  </ul>
  <ul>
    <a href="tel:${vipBusiness.tel}">
      <li class="a1">${vipBusiness.tel}</li>
    </a>
    <a href="/vip/integralExchage.do?business_id=${businessID}&member_id=${memberID}">
      <li class="a2">${vipBusiness.addr}</li>
    </a>
    <a href="/vip/storeInfo.do?business_id=${businessID}&member_id=${memberID}">
      <li class="a3">试用门店</li>
    </a>
  </ul>
</div>
<script>
  $(function(){
    $('#idVipPrivilege').click(function(){
      alert("功能开发中，敬请期待");
    })
    $('#idIntegralExchage').click(function(){
      alert("功能开发中，敬请期待");
    })
  })

</script>
<script type="text/javascript">
  function SelectCode(obj){
    var val = $(obj).val();
    if(val != 2){
      $("#vcode").hide();
    }else{
      $("#vcode").show();
    }

    if(val == 1){
      $("#vcode").hide();
      $("#password").show();
    }else{
      $("#password").hide();
    }
  }


</script>
<script>
  // 	$().ready(function(){
  //
  // });

  function charge(){
    var d = new iDialog();
    var payment = '';
    payment +='<option value="1">微支付</option>';
    d.open({
      classList: "valid_phone charge",
      title:"",
      close:"",
      content:'<ul class="list_ul_card">'+
      '<form id="form1" action="javascript:;" method="post">'+
      '<li data-card>'+
      '<table>'+
      '<tr class="input wrapInput">'+
      '<td><label class="pre" style="color:#333333;">充值金额：</label></td>'+
      '<td style="width:100%;"><input type="number" name="money" placeholder="请输入充值金额" maxlength="15" class="input" /></td>'+
      '</tr><tr><td colspan="2" style="height:15px;"></td></tr>'+
      '<tr class="input wrapInput">'+
      '<td><label class="pre pl_10" style="color:#333333;">充值方式:</label></td>'+
      '<td style="width:100%;" colspan="2">'+
      '<select name="paytype" class="select">'+
      payment+
      '</select>'+
      '</td>'+
      '</tr>'+
      '</table>'+
      '</li>'+
      '</form>'+
      '</ul>',
      btns:[
        {id:"", name:"确定", onclick:"fn.call();", fn: function(self){
          var form1 = document.getElementById("form1");
          var obj = {
            money: form1.money.value,
            paytype: form1.paytype.value
          }
          if(parseFloat(obj.money) <=0 ){
            alert("请输入正确的充值金额", 1500);
            return false;
          }
          loading(true);
          $.ajax({
            url: "/pay/testcharge/getRechargeHtml.do?business_id=${businessID}&member_id=${memberID}",
            type:"post",
            data:obj,
            dataType:'json',
            success: function(data){
              if (data.errno == 0) {
                setTimeout("location.href='"+data.pay_url+"'",1500);
              } else {
                alert(data.error, 1500);
                loading(false);
              }

            }
          });
        }},
        {id:"", name:"取消", onclick:"fn.call();", fn: function(self){
          self.die();
        }}
      ]
    });
  }
  function pay(){
    var d = new iDialog();
    var payment = '';
    payment +='<option value="1">微支付</option>';
    d.open({
      classList: "valid_phone pay",
      title:"",
      close:"",
      content:'<ul class="list_ul_card">'+
      '<form id="form2" action="javascript:;" method="post">'+
      '<li data-card>'+
      '<table>'+
      '<tr class="input wrapInput">'+
      '<td></td>'+
      '<td style="width:100%;" colspan="2">'+
      '<select name="store" class="select">'+
      '<option value="54086" selected>${vipBusiness.name}</option>'+
      '</select>'+
      '</td>'+
      '</tr>'+
      '<tr class="input wrapInput">'+
      '<td></td>'+
      '<td style="width:100%;" colspan="2">'+
      '<select name="discount" class="select">'+
      '<option value="0">请选择所需优惠：</option>'+
      '</select>'+
      '</td>'+
      '</tr>'+
      '<tr class="input wrapInput">'+
      '<td></td>'+
      '<td style="width:100%;" colspan="2">'+
      '<select name="paytype"  id="paytype" class="select" onChange="SelectCode(this);">'+
      payment+
      '<option value="2">余额支付 </option>'+
      '</select>'+
      '</td>'+
      '</tr>'+
      '<tr class="input wrapInput">'+
      '<td>'+
      '<label class="pre ml_5" style="color:#333333;"> 消费金额：</label> '+
      '</td>'+
      '<td style="width:100%;">'+
      '<input name="money" type="number" placeholder="请输入实际消费金额" maxlength="15" class="input" />'+
      '</td>'+
      '</tr>'+
      '<tr class="input wrapInput" id="vcode">'+
      '<td style="width:100%;"><input type="password" placeholder="会员卡密码：暂未设置，无需填写" maxlength="30" class="input" name="pay_pass"></td>'+
      '<td>&nbsp;</td>'+
      '</tr>'+
      '<tr class="input wrapInput" id="password" style="display:none">'+
      '<td style="width:70%;"><input type="password" placeholder="商家确认密码：" maxlength="30" class="input" name="password"></td>'+
      '<td>&nbsp;</td>'+
      '</tr>'+

      '</table>'+
      '</li>'+
      '</form>'+
      '</ul>',
      btns:[
        {id:"", name:"确定", onclick:"fn.call();", fn: function(self){
          var form2 = document.getElementById("form2");
          var obj = {
            store: form2.store.value,
            discount: form2.discount.value,
            paytype: form2.paytype.value,
            money: form2.money.value,
            //validCode: form2.vcode.value,
            password: form2.password.value,
            pay_pass: form2.pay_pass.value
          }
          var code = $("input[name='paytype'] :selected").attr("code");
          if(obj.paytype == 0){
            alert("请选择支付方式", 1500);
            return false;
          }else if(obj.paytype == 3){
            if(obj.password == ''){
              alert("商家确认密码不能为空", 1500);
              return false;
            }
          }
          if(obj.money == "" || parseFloat(obj.money) <= 0){
            alert("请输入正确的消费金额",1500);
            return false;
          }
          alert(parseFloat(obj.money));
          loading(true);
          $.ajax({
            url: "/pay/testcharge/getPayHtml.do?business_id=${businessID}&member_id=${memberID}",
            type:"post",
            data:obj,
            dataType:'json',
            success: function(data){
              loading(false);
              if(data.errno == 0) {
                if (data.pay_url == undefined) {
                  alert(data.error, 1500);
                  //专挑到支付成功页面
                  setTimeout("location.href='"+data.pay_result_url+"'",1500);
                } else {
                  //alert(data.pay_url);
                  setTimeout("location.href='"+data.pay_url+"'",1500);
                }

              }else{
                alert(data.error, 1500);
                return false;
              }

            }
          });
        }},
        {id:"", name:"取消", onclick:"fn.call();", fn: function(selfs){
          selfs.die();
        }}
      ]
    });

    $("#paytype option:first").prop("selected", 'selected');
    SelectCode($("#paytype"));
  }

  function OrderWeipay(urls){
    var url = '/payment/weixinpay/start/470983/default.aspx?showwxpaytitle=1';
    $.ajax({
      url:url,
      cache:false,
      dataType:'html',
      data:{urls:urls},
      type:"POST",
      success:function(msgObj){
        $('#orderpay').html(msgObj);
        return false;
      }
    });

  }

  $(document).ready(function(){
    $("#codeing").on(function(){
      alert("请输入正确的充值金额",1500);


    });


  });
</script>
</body>
</html>
