$(function () {
    $('.form_date').datetimepicker({
        language: 'zh-CN',
        weekStart: 1,
        todayBtn: 1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        minView: 2,
        forceParse: 0
    });


    var gFrameSrc;

    $('#nav-id a').click(function () {

        var aVal = $(this).text();
        /* alert(aVal);*/
        var accountId = $("#account-id").val()
        if (aVal == "商户管理") {

            /*gFrameSrc = "/call_back/toAuthorize.do?account="+accountId+"&"+"choseType=1";*/
            gFrameSrc = "/admin/gzhManageSerach.do?account="+accountId+"&"+"choseType=1";
            $('#iframeId').attr("src", gFrameSrc);
        }
        if (aVal == "充值查询") {
            gFrameSrc = "/admin/toCzSearch.do?account="+accountId;
            $('#iframeId').attr("src", gFrameSrc);
        }
        if (aVal == "消费查询") {
            gFrameSrc = "/admin/toXfSearch.do?account="+accountId;
            $('#iframeId').attr("src", gFrameSrc);
        }
        if (aVal == "用户充值统计") {
            gFrameSrc = "/admin/toUserCzStatistics.do?account="+accountId;
            $('#iframeId').attr("src", gFrameSrc);
        }
        if (aVal == "用户消费统计") {
            gFrameSrc = "/admin/toUserXfStatistics.do?account="+accountId;
            $('#iframeId').attr("src", gFrameSrc);
        }
        if (aVal == "会员数据统计") {
            gFrameSrc = "/admin/toVipDataStatistics.do?account="+accountId;
            $('#iframeId').attr("src", gFrameSrc);
        }
        if (aVal == "会员列表") {
            gFrameSrc = "/admin/toVipList.do?account="+accountId;
            $('#iframeId').attr("src", gFrameSrc);
        }


    });

    $("#tel").blur(function(){
        //验证手机哈o
        if ($.trim($("#tel").val()) != "") {
            $("#telMesSpan").css("display", "none");
            var reg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
            if (!reg.test($.trim($('#tel').val()))) {

                $("#telDiv").after(
                    "<span style=\"color:red;font-size: 10px\" id='telMesSpan'><b>*</b>手机号码格式不对！</span>"
                )
                $("#registerButton").attr("disabled", true);
                return false;
            } else {
                $("#telMesSpan").css("display", "none");
                $("#registerButton").attr("disabled", false);

            }
        }
    });
    $("#email").blur(function(){
        //验证邮箱
        var emailName = $("#email").val();
        $("#emailMesSpan").css("display", "none");
        var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
        if (!reg.test(emailName)) {

            $("#emailDiv").after(
                "<span style=\"color:red;font-size: 10px\" id='emailMesSpan'><b>*</b>邮箱码格式不对！</span>"
            )
            $("#registerButton").attr("disabled", true);
            return false;
        } else {
            $("#emailMesSpan").css("display", "none");
            $("#registerButton").attr("disabled", false);

        }

        //验证身份证号


    });

    $("#nav-id li").click(function () {
        var liVal = $(this).text();
        /*        $(this).addClass("active");*/
        $('.active').removeClass('active');
        $(this).addClass('active');
       /* $(this).css("background-color","blue");*/
    });
});

function iframeShow() {
    $('#iframe-div').css("display", "block");
    $('#cz-search').css("display", "none");

}