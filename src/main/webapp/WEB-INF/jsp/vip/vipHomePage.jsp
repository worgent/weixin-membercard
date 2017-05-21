<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>

<!DOCTYPE html>
<html lang="zh">

<head>
    <meta http-equiv="Content-Type" Content="text/html;charset=utf-8">
    <meta name="viewport"
          content="width=640, user-scalable=no, initial-scale=0.5, maximum-scale=0.5, minimum-scale=0.5">
    <meta content="telephone=no" name="format-detection"/>
    <meta content="email=no" name="format-detection"/>
    <title>个人信息</title>
    <link rel="stylesheet" href="/lib/css/footer.css">
    <link rel="stylesheet" href="/lib/css/home.css">
    <script type="text/javascript" src="/lib/js/jquery-1.8.1.min.js"></script>
    <script type="text/javascript" src="/lib/js/jweixin-1.0.0.js"></script>

</head>

<body id="home">

<div class="main">
    <img class="pic1" src="/lib/img/banner1.jpg" alt="">

    <div class="btn-group">
        <span class="btn-left" onclick="showRechargeWin()"><a>充值</a></span>
        <span class="btn-right" onclick="showPayWin()">付款</span>
    </div>
    <div class="menu" id="menu">
        <ul class="mt10">
            <li class="bsx">
                <a href="/vip/vipCoupon.do?business_id=${businessID}&member_id=${memberID}"><img src="/lib/img/t1.png" alt="">
                    <span>会员优惠</span><img src="/lib/img/my_dy.png" alt=""></a>
            </li>
            <li class="bsx" id="idIntegralExchage">
                <a href=""><img src="/lib/img/t2.png" alt=""><span>积分兑换</span><img src="/lib/img/my_dy.png" alt=""></a>
            </li>
            <li class="bsx">
                <a href="/vip/improveVipInfo.do?business_id=${businessID}&member_id=${memberID}"><img src="/lib/img/t3.png" alt="">
                    <span>完善会员卡资料</span><img src="/lib/img/my_dy.png" alt=""></a>
            </li>
        </ul>
        <ul class="mt20">
            <li class="bsx">
                <a href="tel:${vipBusiness.tel}"><img src="/lib/img/t5.png" alt=""><span>${vipBusiness.tel}</span><img src="/lib/img/my_dy.png" alt=""></a>
            </li>
            <li class="bsx">
                <a href=""><img src="/lib/img/t6.png" alt=""><span>${vipBusiness.addr}</span><img src="/lib/img/my_dy.png" alt=""></a>
            </li>
            <li class="bsx">
                <a href="/vip/storeInfo.do?business_id=${businessID}&member_id=${memberID}"><img src="/lib/img/t7.png" alt="">
                    <span>适用门店</span><img src="/lib/img/my_dy.png" alt=""></a>
            </li>
        </ul>
    </div>

    <div class="cover" id="cover">
        <!--遮罩层-->
    </div>
    <form action="javascript:void(0)" id="idPayForm">
        <div class="div-show pay-popup" id="pay-popup">
            <!--支付弹出层-->
            <!--<label for="">地址:</label>-->
            <select name="store">

                <option value="0">${vipBusiness.name}</option>

            </select>
            <select name="discount">
                <option value="0">选择所需优惠:</option>
            </select>
            <select name="pay_type">
                <option value="1">微支付</option>
                <option value="2">余额支付</option>

            </select>

            <div class="money">
                <span>消费金额:</span>
                <input type="text" name="money" placeholder="请输入实际消费金额">
            </div>
            <div class="money">
                <span>消费密码:</span>
                <input type="text" name="pay_pass" placeholder="请输入消费密码">
            </div>
            <footer>
                <div class="bottom">
                    <input type="button" value="取消" onclick="cloesDivShow()">
                    <input type="submit" value="确定" onclick="vipPay()">
                </div>
            </footer>
        </div>
    </form>
    <form action="javascript:void(0)" id="idRechargeForm">
    <div class="div-show pay-popup recharge-popup" id="recharge-popup">
        <div class="money recharge">
            <span>充值金额:</span>
            <input type="text"  name="money" placeholder="请输入金额">
        </div>
        <select name="paytype">
            <option value="1">微支付</option>

        </select>
        <footer>
            <div class="bottom bottom-2">
                <input type="button" value="取消" onclick="cloesDivShow()">
                <input type="submit" value="确定" onclick="vipRecharge()">
            </div>
        </footer>
    </div>
    </form>
    <div class="h"></div>
</div>
<script>
    $(function () {
        $('#idIntegralExchage').click(function () {
            alert("功能开发中，敬请期待");
        })
    })
</script>
<script>
    function showPayWin() {
        /*$('body').css("overflow", "hidden");*/
        $("#cover").show();
        $("#pay-popup").show();

    }
    function showRechargeWin() {
        $("#cover").show();
        $("#recharge-popup").show();
    }
    function cloesDivShow() {
        $(".div-show").hide();
        $("#cover").hide();
    }
</script>
<script>
    function vipPay() {
        //check
        //检查金额
        var form = document.getElementById("idPayForm");
        var obj = {
            store: form.store.value,
            discount: form.discount.value,
            paytype: form.pay_type.value,
            money: form.money.value,
            pay_pass: form.pay_pass.value
        }
        if (obj.money == "" || parseFloat(obj.money) <= 0) {
            alert("请输入正确的金额");
            return false;
        }
        $.ajax({
                    url: "/pay/testcharge/getPayHtml.do?business_id=${businessID}&member_id=${memberID}",
                    type: "post",
                    data: obj,
                    dataType: "json",
                    success: function (data) {
                        if (data.errno == 0) {
                            if (data.pay_url == undefined) {
                                location.href = data.pay_result_url;
                            } else {
                                location.href = data.pay_url;
                            }
                        } else {
                            alert(data.error);
                            return false;
                        }
                    }
                }
        )
    }

    function vipRecharge() {
        //check
        //检查金额
        var form = document.getElementById("idRechargeForm");
        var obj = {
            paytype: form.paytype.value,
            money: form.money.value
        }
        if (obj.money == "" || parseFloat(obj.money) <= 0) {
            alert("请输入正确的金额");
            return false;
        }
        $.ajax({
                    url: "/pay/testcharge/getRechargeHtml.do?business_id=${businessID}&member_id=${memberID}",
                    type: "post",
                    data: obj,
                    dataType: "json",
                    success: function (data) {
                        if (data.errno == 0) {

                            location.href = data.pay_url;

                        } else {
                            alert(data.error);
                            return false;
                        }
                    }
                }
        )
    }
</script>
<%@ include file="common/footer.jsp" %>
    <%@ include file="common/weixinShare.jsp"%>


</body>

</html>