$(function () {
    var gYearVal="2015";
    var gMonthVal="01";
    var gMax;
    var gStatisticsType;
    var gSum=0;
    var gTip;
    var gDay;
    var gSumY;
    var gJieguoSum;
    var gCompany;
    var gSelMendian;
    var gAccountId;
    $("#statisticsSearchButton").click(function(){
        //alert("dd");
        gYearVal = $("#selYear").val();
        gMonthVal = $("#selMonth").val();
        gStatisticsType = $("#statistics-type").val();
        gSelMendian = $("#selMendian").val();
        gAccountId = $("#accountId").val();
        stratics();
    })
    /*$("#selYear").change(function(){
        gYearVal = $("#selYear").val();
        gStatisticsType = $("#statistics-type").val();
        gSelMendian = $("#selMendian").val();
        gAccountId = $("#accountId").val();
        stratics();
    })
    $("#selMendian").change(function(){
        gYearVal = $("#selYear").val();
        gStatisticsType = $("#statistics-type").val();
        gSelMendian = $("#selMendian").val();
        gAccountId = $("#accountId").val();
        stratics();
    })
    $("#selMonth").change(function(){
        gMonthVal = $("#selMonth").val();
        gStatisticsType = $("#statistics-type").val();
        gSelMendian = $("#selMendian").val();
        gAccountId = $("#accountId").val();

        stratics();
    });*/

    //显示提示标签（鼠标经过显示具体数据）
    function show_tooltip(x, y, contents) {
        $('<div id="tooltip">' + contents + '</div>').css( {
            position: 'absolute',
            display: 'none',
            top: y + 5,
            left: x + 5,
            border: '1px solid #fdd',
            padding: '2px',
            'background-color': '#fee',
            opacity: 0.80
        }).appendTo("body").fadeIn(200);
    }
    function stratics(){
        //动态显示月份的天数
        if(gMonthVal == "02"){
            gMax = 28;
        }
        if(gMonthVal == "01" || gMonthVal == "03" || gMonthVal == "05" || gMonthVal == "07" || gMonthVal == "08" || gMonthVal == "10" || gMonthVal == "12"){
            gMax = 31;
        }
        if(gMonthVal == "04" || gMonthVal == "06" || gMonthVal == "09" || gMonthVal == "11"){
            gMax = 30;
        }

        $.ajax({
            url : "/admin/statics.do",
            type : "post",
            data:{
                yearVal:gYearVal,
                monthVal:gMonthVal,
                statisticsType:gStatisticsType,
                mendian:gSelMendian,
                account:gAccountId
            },
            success : function(data) {
                if(data.length>0){
                    $(".widget-content").css("display","block");
                    var dayArr = [['01',0],['02',0],['03',0],['04',0],['05',0],['06',0],['07',0],['08',0],['09',0],['10',0],
                        ['11',0],['12',0],['13',0],['14',0],['15',0],['16',0],['17',0],['18',0],['19',0],['20',0],['21',0],['22',0],
                        ['23',0],['24',0],['25',0],['26',0],['27',0],['28',0],['29',0],['30',0],['31',0]];

                    $.each(data, function(key1, val) {
                        gSumY= val.sumY;
                        //var slval = parseFloat(gSumY);
                        if(gStatisticsType == "1" || gStatisticsType == "2") {
                            gJieguoSum = parseFloat(gSumY / 100).toFixed(2);
                            gCompany="元"
                        }else{
                            gJieguoSum =  val.sumY;;
                            gCompany="个"
                        }
                        var finishTime= val.finishTime;/*2015-02-02*/
                        gDay = finishTime.substring(8,10);
                        var arr = new Array();
                        var spliceIndex = gDay-1;
                        arr[0] = gDay;
                        arr[1] = gJieguoSum;
                        dayArr.splice(spliceIndex,1,arr); //字符串替换
                        gSum+=gSumY;
                    });

                    if(gStatisticsType == "1"){
                        $("#tig-span").html(
                            "本月实际充值："+gSum
                        );
                        gTip="充值金额:"
                    }
                    if(gStatisticsType == "2"){
                        $("#tig-span").html(
                            "本月消费金额："+gSum
                        );
                        gTip="消费金额:"
                    }
                    if(gStatisticsType == "3") {
                        $("#tig-span").html(
                            "本月新增会员数：" + gSum
                        );
                        gTip="会员数:"
                    }
                    var plot = $.plot($("#line-chart"),
                        [ { data: dayArr, label: gTip } ], {
                            series: {
                                lines: { show: true },
                                points: { show: true }
                            },

                            grid: { hoverable: true, clickable: true, borderColor:'#000',borderWidth:1},
                            //yaxis: { min: 0, max: 10000  },
                           // labelHeight: 100,
                            xaxis: { ticks: [[1, gMonthVal+"-1"], [2, gMonthVal+"-2"],[3, gMonthVal+"-3"],[4, gMonthVal+"-4"],
                                [5, gMonthVal+"-5"],[6, gMonthVal+"-6"],[7, gMonthVal+"-7"],[8, gMonthVal+"-8"],
                                [9, gMonthVal+"-9"],[10, gMonthVal+"-10"],[11, gMonthVal+"-11"],[12, gMonthVal+"-12"],
                                [13, gMonthVal+"-13"],[14, gMonthVal+"-14"],[15, gMonthVal+"-15"],[16, gMonthVal+"-16"],
                                [17, gMonthVal+"-17"],[18, gMonthVal+"-18"],[19, gMonthVal+"-19"],[20, gMonthVal+"-20"],
                                [21, gMonthVal+"-21"],[22, gMonthVal+"-22"],[23, gMonthVal+"-23"],[24, gMonthVal+"-24"],
                                [25, gMonthVal+"-25"],[26, gMonthVal+"-26"],[27, gMonthVal+"-27"],[28, gMonthVal+"-28"],
                                [29, gMonthVal+"-29"],[30, gMonthVal+"-30"],[31, gMonthVal+"-31"]],min: 1, max: gMax },

                            colors: ["#F90", "#3C4049", "#666", "#BBB"]
                        });

                    //鼠标经过显示具体数据
                    var previousPoint = null;
                    $("#line-chart").bind("plothover", function (event, pos, item) {
                        if (item) {
                            if (previousPoint != item.dataIndex) {
                                previousPoint = item.dataIndex;
                                $("#tooltip").remove();
                                var x = item.datapoint[0], y = item.datapoint[1];
                                show_tooltip(item.pageX, item.pageY,
                                    item.series.label + "[" + gMonthVal+"-"+x + "] : " + y + gCompany);
                            }
                        }
                        else {
                            $("#tooltip").remove();
                            previousPoint = null;
                        }
                    });
                    gSum=0;
                }else{
                    alert("此时间段无数据");
                    $(".widget-content").css("display","none");
                    gSum=0;
                    if(gStatisticsType == "1"){
                        $("#tig-span").html(
                            "本月实际充值："+gSum
                        );
                    }
                    if(gStatisticsType == "2"){
                        $("#tig-span").html(
                            "本月消费金额："+gSum
                        );
                    }
                    if(gStatisticsType == "3") {
                        $("#tig-span").html(
                            "本月新增会员数：" + gSum
                        );
                    }
                    gSum=0;
                }
            },
            error: function(e){
                alert("ajax error");
            }
        });

    }
});

