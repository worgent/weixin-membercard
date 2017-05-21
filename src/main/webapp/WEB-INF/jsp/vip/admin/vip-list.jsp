<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>

<!DOCTYPE html>
<html>

<head>
    <title>会员列表</title>
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

    <div class="right col-xs-12">


        <!--会员管理-->
        <h3>会员列表</h3>

        <div class="top-tag">
            <span>会员总数:【${page.totalCount }】&nbsp;&nbsp;今日新增:【${todayCount}】</span>
        </div>
        <form class="bs-example bs-example-form" role="form" id="formVal" action="/admin/searchVipInfo.do" onsubmit="return searchVipInfo()">
            <input type="hidden" value="${page.pageNo}" id="pageNo" name="pageNo"/>
            <input type="hidden" value="${accountID}" id="accountId" name="account"/>
            <div class="right-search-form col-xs-12">
                <!--<input type="text" placeholder="请输入关键词">-->
                <input type="text" id="searchNameId" class="form-control" name="searchName" value="${searchName}" placeholder="请输入关键词">

                <div class="viplist-select-group">
                    <select name="mendian" class="selectpicker" data-size="10" id="mendian">
                        <option value="0" selected>选择门店</option>
                        <c:if test="${vipBusinessList != null || fn:length(vipBusinessList) > 0}">
                            <c:forEach items="${vipBusinessList}" var="businessMendian">
                                <option value="${businessMendian.id}" <c:if test="${businessID == businessMendian.id}">selected="" </c:if>>${businessMendian.name}</option>
                            </c:forEach>
                        </c:if>
                        <c:if test="${vipBusinessList == null || fn:length(vipBusinessList) == 0}">
                            <option value="0">您暂无门店信息</option>
                        </c:if>
                    </select>
                </div>
                <div class="viplist-select-group">
                    <select name="searchType" class="selectpicker" data-size="5" id="searchTypeId">
                        <option value="0" <c:if test="${searchType == 0}">selected="" </c:if>>选择查询类型</option>
                        <option value="2" <c:if test="${searchType == 2}">selected="" </c:if>>用户名</option>
                        <option value="1" <c:if test="${searchType == 1}">selected="" </c:if>>会员卡号</option>
                        <option value="3" <c:if test="${searchType == 3}">selected="" </c:if>>手机号</option>

                    </select>
                </div>
                <button type="submit" class="btn btn-default">查询</button>
            </div>
        </form>

        <div class="vip-button-group col-xs-12">
            <button type="button" class="btn btn-default">导出会员</button>
            <button type="button" class="btn btn-default"><a href="/admin/toVipList.do" style="color: black;text-decoration: none;">刷新</a></button>

        </div>

        <table class="table table-bordered table-hover dataTable">
            <thead>
            <tr class="info">
                <th>会员卡号</th>
                <th>姓名</th>
                <th>手机号码</th>
                <th>领卡时间</th>
                <th>余额</th>
                <th>状态</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${page.list}" var="vipInfo">

                <tr>
                    <td>${vipInfo.vipCardNumMapKey}</td>
                    <td>${vipInfo.vipNameMapKey}</td>
                    <td>${vipInfo.phoneMapKey}</td>
                    <td>${vipInfo.createTimeMapKey}</td>
                    <td><span>￥</span>${vipInfo.feeMapKey}</td>
                    <td><c:if test="${vipInfo.statusMapKey == 1}">有效</c:if>
                    <c:if test="${vipInfo.statusMapKey == 0}">无效</c:if></td>
                    <td style="text-align: center; width: 9%;">
                        <button type="button" class="btn btn-success" onclick="showUpdateWin(${vipInfo.vipCardNumMapKey},'${vipInfo.vipNameMapKey}',${vipInfo.phoneMapKey},'${vipInfo.createTimeMapKey}',${vipInfo.feeMapKey},${vipInfo.statusMapKey})">修改</button>
                        <%--<button type="button" class="btn btn-danger" onclick="deleteVipInfo(${vipInfo.vipCardNumMapKey})" id="deleteBtn">删除</button>--%>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <form action="/admin/updateVipInfo.do" method="post">
            <input type="hidden" id="hiddenSearchNameWin" name="searchName"/>
            <input type="hidden" id="hiddenSearchTypeWin" name="searchType"/>
            <input type="hidden" value="${accountID}" id="u-accountId" name="account"/>
            <div class="updateWin" id="updateWin">
                <div class="updateWin-title">
                    <span>会员信息修改</span>
                </div>
                <div class="updateWin-concent">
                    <div class="input-group updateWin-input-group">
                        <span class="input-group-addon input-group-lg">会员卡号</span>
                        <input type="text" name="vipNumWin" class="form-control" id="vipNum" placeholder="会员名称" readonly>
                    </div>
                    <div class="input-group updateWin-input-group">
                        <span class="input-group-addon input-group-lg">会员名称</span>
                        <input type="text" name="vipNameWin" class="form-control" id="vipName" placeholder="会员名称">
                    </div>
                    <div class="input-group updateWin-input-group">
                        <span class="input-group-addon input-group-lg">手机号</span>
                        <input type="text" name="vipTelWin" class="form-control" id="vipTel" placeholder="会员名称" readonly>
                    </div>
                    <div class="input-group updateWin-input-group">
                        <span class="input-group-addon input-group-lg">领卡时间</span>
                        <input class="form-control" size="16" type="text" name="getCardTimeWin" id="getCardTime" readonly>
                    </div>

                    <div class="input-group updateWin-input-group">
                        <span class="input-group-addon input-group-lg">余额</span>
                        <input type="text" name="vipFeeWin" class="form-control" id="vipFee" placeholder="会员名称" readonly>
                    </div>

                    <div class="input-group updateWin-input-group">
                        <span class="input-group-addon input-group-lg select-span">状态</span>
                        <select name="vipStatusWin" id="vipStatus">
                            <option value="1">有效</option>
                            <option value="0">无效</option>
                        </select>
                        </div>
                    <div class="updateWin-input-group updateWin-button">
                        <button type="submit" class="btn btn-default updateWin-my-btn">确定</button>
                        <button type="button" class="btn btn-default updateWin-my-btn" onclick="closeWin()">取消</button>
                    </div>
                </div>
            </div>
        </form>
        <div class="paging page_c">
            <span class="page">

                <input type="hidden" value="${page.totalCount}" id="totalCount" name="totalCount"/>
                <input type="hidden" value="${page.pageNo}" id="currentPageNo" name="currentPageNo"/>
                <input type="hidden" value="${page.totalPage}" id="totalPage" name="totalPage"/>
                <span class="gong">共</span>
                    <var id="pagePiece" class="orange">${page.totalCount }</var>
                <span class="tiao">条</span>
                    <var id="pageTotal">${page.pageNo }/${page.totalPage }</var>
               <%-- <a href="javascript:void(0);" id="previous" class="hidden" title="上一页">上一页</a>
                <a href="javascript:void(0);" id="next" class="hidden" title="下一页">下一页</a>--%>
                <select class="pageSelect" id="selectPage">
                    <c:forEach begin="1" end="${page.totalPage }" var="curPage">
                        <option value="${curPage }" <c:if test="${curPage == page.pageNo }">selected</c:if> >${curPage }</option>
                    </c:forEach>
                </select>
            </span>
        </div>

    </div>


</div>

<script type="text/javascript">
    $("#selectPage").change(function () {
        var s= $(this).val();
        $("#pageNo").val($(this).val());
        $("#formVal").submit();
        $("#currentPageNo").val("1");

    })
    function closeWin(){
        $("#updateWin").css("display","none");
    }
    function showUpdateWin(vipCardNum,vipName,phone,createTime,fee,status){

        /*if(vipCardNum !=  || vipName || phone || createTime || fee || status){*/
        $("#vipNum").val(vipCardNum);
        $("#vipName").val(vipName);
        $("#vipTel").val(phone);
        $("#getCardTime").val(createTime);
        $("#vipFee").val(fee);
        $("#vipStatus").val(status);
       /* }*/
        var searchNameVal = $("#searchNameId").val();
        var searchTypeVal = $("#searchTypeId").val();
        $("#hiddenSearchNameWin").val(searchNameVal);
        $("#hiddenSearchTypeWin").val(searchTypeVal);
        $("#updateWin").css("display","block");
    }

    function deleteVipInfo(vipId){
        var confirmflag = window.confirm("你确定执行删除吗？");
        if(!confirmflag){
            return false;
        }
                $.ajax({
                    type : "post",
                    url : "/admin/deleteVip.do",
                    data:{vipId:vipId},
                    success : function(data) {
                        if(data == "success") {
                            $(the).parents("li").remove();
                            parent.initDraft();
                        }
                    }
                });
    }

    function searchVipInfo(){
        var searchNameVal = $("#searchNameId").val().trim();
        var s = $("#searchTypeId").val();
        if(searchNameVal != "" && s==0){
            alert("请选择查询类型");
            return false;
        }
        if(searchNameVal == "" && s !="0"){
            $("#searchTypeId").val("0");
        }
    }

</script>
</body>

</html>