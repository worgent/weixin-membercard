<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%
   /* out.print(request.getAttribute("pages"));*/
%>
<!DOCTYPE html>
<html>

<head>
    <title>商户管理</title>
    <link href="/admin-lib/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/admin-lib/css/bootstrap-select.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/admin-lib/css/style.css">

    <link rel="stylesheet" href="/admin-lib/css/adminia.css" rel="stylesheet">

    <script src="/admin-lib/js/jquery.min.js"></script>
    <script src="/admin-lib/bootstrap-3.3.5-dist/js/bootstrap.min.js"></script>
    <script src="/admin-lib/js/bootstrap-select.js"></script>
    <script src="/admin-lib/js/bootstrap-datetimepicker.js"></script>
    <script src="/admin-lib/js/bootstrap-datetimepicker.zh-CN.js"></script>
    <script src="/admin-lib/js/default.js"></script>

    <script src="/admin-lib/js/jquery.flot.js"></script>

    <script src="/admin-lib/js/line.js"></script>


</head>
<style type="text/css">

</style>

<body>
<div class="main">

    <div class="right col-xs-12" style="border-left: 1px #ddd solid;">

        <h3>商户管理</h3>
            <div class="add-business-div" style="border-bottom: 1px #ddd solid;padding: 26px 0 26px 0;border-top: 1px #ddd solid;">
                <button type="button" data-toggle="modal" data-target="#toAddBusinessTip" class="btn btn-success">添加商户</button>
            </div>
        <h3>商户信息列表</h3>

        <form action="/admin/gzhManageSerach.do" id="formVal">
            <input type="hidden" value="${accountId}" id="accountId" name="account"/>
            <input type="hidden" value="1" id="choseType" name="choseType"/>
            <input type="hidden" value="${page.pageNo}" id="pageNo" name="pageNo"/>
        </form>
        <div class="gzh-info-div" style="display: none" id="gzh-info-div-id">

        </div>
        <div class="business-info-div" id="business-info-div-id">
            <table class="table table-bordered table-hover dataTable business-info-table">
                <thead>
                <tr class="info">
                    <th>商户名称</th>
                    <th>电话</th>
                    <th>地址</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody id="tableData" class="business-info-table-tbody">
                <c:forEach items="${page.list}" var="businessInfo">
                    <tr>
                        <td>${businessInfo.businessName}</td>
                        <td>${businessInfo.businessTel}</td>
                        <td>${businessInfo.businessAssr}</td>
                        <td><%--/call_back/toAuthorize.do?account="+accountId--%>
                            <c:if test="${businessInfo.businessType == 0}">
                                <button type="button" class="btn btn-success my-button" data-toggle="modal"
                                        data-target="#toAuthorizeTip"
                                        onclick="getAuthorizeUrl('${businessInfo.businessID}')">授权绑定</button>
                                <button type="button" class="btn btn-success my-button"
                                        onclick="deleteBusiness('${businessInfo.businessID}')">删除商户</button>
                            </c:if>
                            <c:if test="${businessInfo.businessType == 1}">
                                <button type="button" class="btn btn-info my-button" onclick="gzhInfo('${businessInfo.businessID}')">
                                    公众号详情</button>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <%--去授权弹窗  start--%>
        <div class="modal fade tip-modal shouquan-tip-modal" id="toAuthorizeTip" tabindex="-1" role="dialog">
            <div class="modal-header">
                <button class="close" type="button" data-dismiss="modal">×</button>
                <h3 id="toAuthorizeLabel">提示</h3>
            </div>
            <div class="modal-body" style="font-size: 16px;">你即将前往微信的页面进行公众号授权登录，请确认页面网址为http://mp.weixin.qq.com</div>
            <div class="modal-footer">
                <a href="#" class="btn btn-primary" id="authorize-aHref-id" onclick="toAuthorize()" target="_blank">确定</a>
            </div>
        </div>
        <%--去授权弹窗  end--%>

        <%--添加商户弹窗 start--%>
        <div class="modal fade tip-modal add-business-tip-modal" id="toAddBusinessTip" tabindex="-1" role="dialog">
            <div class="modal-header"><button class="close" type="button" data-dismiss="modal">×</button>
                <h3 id="toAddBusinessLabel">添加商户</h3>
            </div>
            <div class="modal-body" style="font-size: 16px;">

                <div class="input-group add-business-input-group">
                    <span class="input-group-addon input-group-lg">商户名称</span>
                    <input type="text" name="vipPhone" class="form-control" id="businessName" placeholder="商户名称">
                </div>
                <div class="input-group add-business-input-group"  id="businessTelDivId">
                    <span class="input-group-addon input-group-lg">联系电话</span>
                    <input type="text" name="vipPhone" class="form-control" id="businessTel" placeholder="联系电话">
                </div>
                <div class="input-group add-business-input-group">
                    <span class="input-group-addon input-group-lg">所在城市</span>
                    <input type="text" name="vipPhone" class="form-control" id="businessCity" placeholder="所在城市">
                </div>
                <div class="input-group add-business-input-group">
                    <span class="input-group-addon input-group-lg">详细地址</span>
                    <input type="text" name="vipPhone" class="form-control" id="businessAddr" placeholder="商户地址">
                </div>
            </div>
            <div class="modal-footer">
                <a href="#" class="btn btn-primary" id="add-business-aHref-id" onclick="addBusiness()">确定</a>
            </div>
        </div>
        <%--添加商户弹窗 end--%>
        <div class="paging page_c" id="page_c">
            <span class="page">
                <input type="hidden" value="${page.totalCount}" id="totalCount" name="totalCount"/>
                <input type="hidden" value="${page.pageNo}" id="currentPageNo" name="currentPageNo"/>
                <input type="hidden" value="${page.totalPage}" id="totalPage" name="totalPage"/>
                <span class="gong">共</span>
                    <var id="pagePiece" class="orange">${page.totalCount }</var>
                <span class="tiao">条</span>
                    <var id="pageTotal">${page.pageNo }/${page.totalPage }</var>

                <select class="pageSelect" id="selectPage">
                    <c:forEach begin="1" end="${page.totalPage }" var="curPage">
                        <option value="${curPage }"
                                <c:if test="${curPage == page.pageNo }">selected</c:if> >${curPage }</option>
                    </c:forEach>
                </select>
            </span>
        </div>
    </div>


</div>

<script type="text/javascript">

    var gAuthorizeUrl="#";
    var gGzhType;
    var gVerifyType;
    $("#selectPage").change(function () {
        var s= $(this).val();
        $("#pageNo").val($(this).val());
        $("#formVal").submit();
    })

    function getAuthorizeUrl(businessId){
        $.ajax({
            url:'/call_back/toAuthorize.do',
            data:{
                businessID:businessId
            },
            success:function(data){
                //alert(data);
                if(data.length!=0){
                    /*modal-backdrop  是bootstrap 蒙版弹窗生成的div class */
                    $("#toAuthorizeTip").removeClass("hide");
                    $(".modal-backdrop").css("display","block")
                    $("#toAddBusinessTip").css("display","none");
                     $("#toAuthorizeTip").css("display","block");
                    gAuthorizeUrl=data;
                }else{
                    alert("请求授权失败，请重新尝试；")
                }
            },
            error:function(e){
                $("#toAuthorizeTip").addClass("hide");
                $(".modal-backdrop").css("display","none")
                alert("['component_access_token'] not found");
                return false;
            }
        });
    }
    function toAuthorize(){
        $("#authorize-aHref-id").attr("href",gAuthorizeUrl);
        $("#toAuthorizeTip").addClass("hide");
        $(".modal-backdrop").css("display","none")
       // location.reload();
    }

    function gzhInfo(businessId){

        $.ajax({
            url:'/admin/getGzhInfo.do',
            data:{
                businessID:businessId
            },
            success:function(data){
                if(data.length!=0){
                //alert(data[0].gzhName);
                var gzhType = data[0].gzhType;
                var verifyType = data[0].verifyType;
                if(gzhType == 0){
                    gGzhType ="订阅号"
                }
                if(gzhType == 1){
                    gGzhType ="由历史老帐号升级后的订阅号"
                }
                if(gzhType == 2){
                    gGzhType ="服务号"
                }
                if(verifyType == -1){
                    gVerifyType ="未认证"
                }
                if(verifyType == 0){
                    gVerifyType ="微信认证"
                }
                if(verifyType == 1){
                    gVerifyType ="新浪微博认证"
                }
                if(verifyType == 2){
                    gVerifyType ="腾讯微博认证"
                }
                if(verifyType == 3){
                    gVerifyType ="已资质认证通过但还未通过名称认证"
                }
                if(verifyType == 4){
                    gVerifyType ="已资质认证通过、还未通过名称认证，但通过了新浪微博认证"
                }
                if(verifyType == 5){
                    gVerifyType ="已资质认证通过、还未通过名称认证，但通过了腾讯微博认证"
                }
                $("#gzh-info-div-id").html(
                    "<table class='table table-bordered table-hover dataTable table-list gzh-info-table'>"+
                        "<thead>"+
                            "<tr class='info'>"+
                                "<th>公众号昵称</th>"+
                                "<th>头像</th>"+
                                "<th>公众号类型</th>"+
                                "<th>认证类型</th>"+
                                "<th>微信号</th>"+
                                "<th>二维码</th>"+
                                "<th>权限集</th>"+
                                "<th>授权码</th>"+
                                "<th>授权方令牌</th>"+
                                "<th>刷新令牌</th>"+
                                "<th>有效期</th>"+
                            "</tr>"+
                        "</thead>"+
                        "<tbody id='gzh-info-tableData'>"+
                            "<tr>"+
                                "<td>"+data[0].gzhName+"</td>"+
                                "<td>"+data[0].headImg+"</td>"+
                                "<td>"+gGzhType+"</td>"+
                                "<td>"+gVerifyType+"</td>"+
                                "<td>"+data[0].gzhAlias+"</td>"+
                                "<td style='padding: 0;'>"+"<img src='"+data[0].qrcodeUrl+"' alt=''/>"+"</td>"+
                                "<td>"+
                                    "<div class='td-div'>"+data[0].funcInfo+"</div>"+
                                "</td>"+
                                "<td>"+
                                    " <div class='td-div'>"+data[0].authorizationCode+"</div>"+
                                "</td>"+
                                "<td>"+
                                    "<div class='td-div'>"+data[0].authorizerAccessToken+"</div>"+
                                "</td>"+
                                "<td>"+
                                    "<div class='td-div'>"+data[0].authorizerRefreshToken+"</div>"+
                                "</td>"+
                                "<td>"+data[0].tokenExpireTime+"</td>"+
                            "</tr>"+
                        "</tbody>"+
                    "</table>"+
                    "<button type='button' class='btn btn-warning' style='float: right;' onclick=\"javascript:$('#gzh-info-div-id').css('display','none');$('#business-info-div-id').css('display','block');$('#page_c').css('display','block');\">"+
                    "关闭"+"</button>"
                )
                    $("#gzh-info-div-id").css("display","block");
                    $("#business-info-div-id").css("display","none");
                    $("#page_c").css("display","none");
                }else{
                    alert("请重新尝试；")
                }
            },
            error:function(e){
                alert("ajax error:");
            }
        })

    }

    function addBusiness(){
        $("#toAddBusinessTip").css("display","block");
        $("#toAuthorizeTip").css("display","none");
        var accountIdVal = $("#accountId").val();
        var businessNameVal = $.trim($("#businessName").val());
        var businessTelVal = $.trim($("#businessTel").val());
        var businessCityVal = $.trim($("#businessCity").val());
        var businessAddrVal = $.trim($("#businessAddr").val());
        if(businessNameVal == "" || businessTelVal == "" || businessCityVal == "" || businessAddrVal == ""){
            alert("请填写全部内容");
            return false;
        }
        if (businessTelVal != "") {
            $("#telMesSpan").css("display", "none");
            var reg1 = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
            var reg2=/^([0-9]{3,4}-)?[0-9]{7,8}$/;
            if (reg1.test(businessTelVal) || reg2.test(businessTelVal)) {
                $("#telMesSpan").css("display", "none");
            } else {
                $("#businessTelDivId").after(
                        "<span style=\"color:red;font-size: 10px\" id='telMesSpan'><b>*</b>电话号码格式不正确！</span>"
                )
                return false;
            }
        }
        $.ajax({
            url:'/admin/addBusiness.do',
            data:{
                accountId:accountIdVal,
                businessName:businessNameVal,
                businessTel:businessTelVal,
                businessCity:businessCityVal,
                businessAddr:businessAddrVal
            },
            success:function(data){
                if(data!=0){
                    alert("添加成功；");
                    $("#toAddBusinessTip").addClass("hide");

                    location.reload();
                }else{
                    alert("商户名称已存在，请更正；");
                    return false;
                }
            },
            error:function(e){
                alert("ajax error");
            }
        })

    }


    function deleteBusiness(businessId){
        var r = window.confirm("确定删除么？");
        if(!r){
            return false;
        }
        $.ajax({
            url:'/admin/deleteBusiness.do',
            data:{
                businessID:businessId
            },
            success:function(data){
                if(data == 1){
                    alert("删除成功");
                    location.reload();
                }else{
                    alert("删除失败");
                }
            },
            error:function(e){
                alert("ajax error");
            }
        })
    }

</script>
</body>
</html>