<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<!DOCTYPE html>
<html>

<head>
    <title>消费查询</title>
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
    <script src="/admin-lib/js/jquery.bootstrap-pureClearButton.js"></script>



</head>
<style type="text/css">

</style>

<body>
<div class="main">

    <div class="right col-xs-12">

        <!--消费查询-->
        <h3>消费查询</h3>

        <form class="bs-example bs-example-form" role="form" id="formVal" action="/admin/xfSearch.do?choseType=3" method="post" onsubmit="return xfSearch()">
            <input type="hidden" value="${page.pageNo}" id="pageNo" name="pageNo"/>
            <input type="hidden" value="${accountID}" id="accountId" name="account"/>
            <div class="right-form">
                <div class="input-group input-myGroup"><%--vipName mendian vipPhone czTime moneyMin moneyMax--%>
                    <span class="input-group-addon input-group-lg">会员名称</span>
                    <input type="text" class="form-control" name="vipName" id="name" value="${vipName}" placeholder="会员名称">
                </div>
                <div class="input-group input-myGroup">
                    <span class="input-group-addon input-group-lg select-span">充值门店:</span>
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
                <div class="input-group input-myGroup">
                    <span class="input-group-addon input-group-lg select-span">消费类型:</span>
                    <select name="payType" class="selectpicker" data-size="5" id="xfType">
                        <option value="">请选择</option>
                        <option value="1" <c:if test="${payType == 1 }">selected</c:if>>微支付</option>
                        <option value="2" <c:if test="${payType == 2 }">selected</c:if>>余额支付</option>

                    </select>
                </div>
                <div class="input-group input-myGroup date form_date" data-date="" data-date-format="" data-link-field="dtp_input2"
                     data-link-format="yyyy-mm-dd">

                    <span class="input-group-addon input-group-lg">消费日期:</span>
                    <input class="form-control" size="16" type="text" name="xfTime" value="${xfTime}" id="time" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>

                </div>
                <div class="input-group input-myGroup">
                    <span class="input-group-addon input-group-lg">会员手机号</span>
                    <input type="text" class="form-control" name="vipPhone" placeholder="手机号" value="${vipPhone}" id="vipPhone">
                </div>

                <div class="input-group input-myGroup form-group" style="width: 176px;margin-right: 0">
                    <span class="input-group-addon input-group-lg">消费金额</span>
                    <input type="text" class="form-control money-min input-md" name="xfMoneyMin" value="${xfMoneyMin}" id="xfMoneyNum_1" data-pure-clear-button>
                </div>
                <div class="input-group input-myGroup form-group" style="width: 128px;margin-right: 0">
                    <span class="input-group-addon input-group-lg" style="border-radius: 0;">至</span>
                    <input type="text" class="form-control money-max input-md" name="xfMoneyMax" value="${xfMoneyMax}" id="xfMoneyNum_2" data-pure-clear-button>
                </div>

                <div class="right-button">
                    <button type="submit" class="btn btn-default" onclick="updatePage()">查询</button>
                    <button type="button" class="btn btn-default">导出</button>
                </div>
            </div>
        </form>


        <table class="table table-bordered table-hover dataTable">
            <thead>
            <tr class="info">
                <th>会员姓名</th>
                <th>手机号</th>
                <th>消费门店</th>
                <th>消费总计</th>
                <th>实收金额</th>
                <th>消费时间</th>
                <%-- <th>操作</th>--%>

            </tr>
            </thead>
            <tbody id="tableData">
            
            <c:if test="${page.list != null || fn:length(page.list) > 0}">
                <c:forEach items="${page.list}" var="xfInfo">
                    <tr>
                        <td>${xfInfo.vipNameMapKey}</td>
                        <td>${xfInfo.phoneMapKey}</td>
                        <td>${xfInfo.mendianMapKey}</td>
                        <td><sapn>￥</sapn>${xfInfo.xfMoneyMapKey}</td>
                        <td><sapn>￥</sapn>${xfInfo.xfMoneyMapKey}</td>
                        <td>${xfInfo.xfTimeMapKey}</td>
                    </tr>
                </c:forEach>
            </c:if>
            <c:if test="${page.list == null || fn:length(page.list) == 0}">
                <td colspan="6" style="text-align: center;font-size: 20px;;background-color: navajowhite;">无数据</td>
            </c:if>
            </tbody>
        </table>
        <div class="paging page_c">

        <span class="page">

            <input type="hidden" value="${page.totalCount}" id="totalCount" name="totalCount"/>

            <input type="hidden" value="${page.totalPage}" id="totalPage" name="totalPage"/>
            共<var id="pagePiece" class="orange">${page.totalCount }</var>条<var
                id="pageTotal">${page.pageNo }/${page.totalPage }</var>
           <%-- <a href="javascript:void(0);" id="previous" class="hidden" title="上一页">上一页</a>
            <a href="javascript:void(0);" id="next" class="hidden" title="下一页">下一页</a>--%>
            <select id="selectPage">
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
    function updatePage(){
        $("#pageNo").val(1);
    }
    $("#selectPage").change(function () {
        var s= $(this).val();
        $("#pageNo").val($(this).val());
        $("#formVal").submit();

    })

    function xfSearch() {
        var nameVal = $("#name").val().trim();
        var mendianVal = $("#mendian").val();
        var timeVal = $("#time").val();
        var vipPhoneVal = $("#vipPhone").val().trim();
        var xfMoneyVal_1 = $("#xfMoneyNum_1").val().trim();
        var xfMoneyVal_2 = $("#xfMoneyNum_2").val().trim();
        var xfTypeVal = $("#xfType").val();

        var fuHao_1 = xfMoneyVal_1.substring(0,1);
        var fuHao_2 = xfMoneyVal_2.substring(0,1);

        if (xfMoneyVal_1 != "") {

            if (!xfMoneyVal_1.match(/^([1-9]\d*(\.\d+)?|0)$/)) {
                alert("请填写数字");
                $("#xfMoneyNum_1").val("")
                return false;
            }
        }
        if (xfMoneyVal_2 != "") {

            if (!xfMoneyVal_2.match(/^([1-9]\d*(\.\d+)?|0)$/)) {
                alert("请填写数字");
                $("#xfMoneyNum_2").val("")
                return false;
            }
        }

        if(fuHao_1 == "-" || fuHao_2 == "-"){
            alert("请输入正整数");
            return false;
        }

        if (vipPhoneVal != "") {
            if (!vipPhoneVal.match(/^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/)) {
                alert("请填写正确的手机号");
                $("#vipPhone").val("");
                return false;
            }
        }

        if(xfMoneyVal_1 != "" && xfMoneyVal_2 == ""){
            alert("请输入最大金额");
            return false;
        }
        if(xfMoneyVal_1 == "" && xfMoneyVal_2 != ""){
            alert("请输入最小金额");
            return false;
        }

        if (parseInt(xfMoneyVal_1) > parseInt(xfMoneyVal_2)) {
            alert("起始金额大于结束金额");
            return false;
        }

    }



</script>
</body>

</html>