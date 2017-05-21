<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"  isELIgnored="false"%>
<!DOCTYPE html>
<html>

<head>
    <title>用户充值统计</title>
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

            <!--用户充值统计-->
            <h3>用户充值统计</h3>
            <div class="serach-user-cz col-xs-12">
                <input type="hidden" value="${accountID}" id="accountId" name="account"/>
                <div class="user-cz-select-group">
                    <span>按月份查看</span>
                    <select name="selYearName" class="selectpicker" data-size="10" id="selYear">
                        <option value="2013">2013年</option>
                        <option value="2014">2014年</option>
                        <option value="2015" selected="selected">2015年</option>
                        <option value="2016">2016年</option>
                        <option value="2017">2017年</option>
                        <option value="2018">2018年</option>
                        <option value="2019">2019年</option>
                        <option value="2020">2020年</option>
                        <option value="2021">2021年</option>
                        <option value="2022">2022年</option>
                    </select>
                    <!--</div>
                <div class="user-cz-select-group">-->
                    <select name="selMonthName" class="selectpicker" data-size="12" id="selMonth">
                        <option value="01">1月</option>
                        <option value="02">2月</option>
                        <option value="03">3月</option>
                        <option value="04">4月</option>
                        <option value="05">5月</option>
                        <option value="06">6月</option>
                        <option value="07">7月</option>
                        <option value="08">8月</option>
                        <option value="09">9月</option>
                        <option value="10">10月</option>
                        <option value="11">11月</option>
                        <option value="12">12月</option>

                    </select>
                        <span>充值门店:</span>
                        <select name="mendian" class="selectpicker" data-size="10" id="selMendian">
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
                    <button type="button" class="btn btn-success" id="statisticsSearchButton">查询</button>
                    <%--<span id="tig-span">本月实际充值：0</span>--%>
                </div>

                <input type="hidden" value="1" id="statistics-type"/>
                <div id="placeholder" style="width:600px;height:300px;"></div>
            </div>

            <div class="span9">
                <div class="widget">

                    <div class="widget-header">
                        <h3>Line Chart</h3>
                    </div>
                    <div class="widget-content">
                        <div id="line-chart" class="chart-holder"></div>
                    </div>
                </div>
            </div>

        </div>

    </div>

    <script type="text/javascript">

    </script>
</body>

</html>