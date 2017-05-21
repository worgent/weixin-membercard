var dater = (function() {
    _dater = function(b) {
        this.curDat = new Date();
        this.maxDat = new Date("2015/12/31");
        this.minDat = new Date("1980/01/01");
        this.selectYear = null;
        this.selectMonth = null;
        this.selectDate = null;
        for (var c in b) {
            this[c] = b[c]
        }
        this.minDat && (this.curDat = new Date(Math.max(this.minDat, this.curDat)));
        this.maxDat && (this.curDat = new Date(Math.min(this.maxDat, this.curDat)));
        this.curDate = this.formate(this.curDat);
        this.maxDate = this.formate(this.maxDat);
        this.minDate = this.formate(this.minDat)
    };
    _dater.prototype = {
        init: function(a) {
            var b = this;
            b.selectYear.onchange = b.selectMonth.onchange = b.selectDate.onchange = function() {
                b.onchange.call(b, this, true)
            };
            b.onchange(b.selectYear, false);
            return b
        },
        onchange: function(a, c) {
            var b = this;
            switch (a) {
            case b.selectYear:
                if (c) {
                    b.curDate[0] = parseInt(a.value, 10) || b.curDate[0];
                    b.curDate[1] = 1;
                    b.curDate[2] = 1;
                    b.selectMonth.value = 1
                }
                b.selectYear && (function() {
                    b.selectYear.options.length = 0;
                    for (var d = b.minDate[0]; d <= b.maxDate[0]; d++) {
                        b.selectYear.options.add(new Option(d + "年", d))
                    }
                    b.selectYear.value = b.curDate[0]
                })();
                b.onchange(b.selectMonth, c);
                break;
            case b.selectMonth:
                if (c) {
                    b.curDate[1] = parseInt(a.value, 10) || b.curDate[1];
                    b.curDate[2] = 1;
                    b.selectDate.value = 1
                }
                b.selectMonth && (function() {
                    b.selectMonth.options.length = 0;
                    var d = Math.max((function() {
                        var g = (b.curDate[0] == b.minDate[0]) ? b.minDate[1] : 1;
                        return g
                    })(), 1);
                    var f = Math.min((function() {
                        var g = b.curDate[0] == b.maxDate[0] ? b.maxDate[1] : 12;
                        return g
                    })(), 12);
                    b.curDate[1] = [d, b.curDate[1], f].sort(function(h, g) {
                        return h > g ? 1 : 0
                    })[1];
                    for (var e = d; e <= f; e++) {
                        b.selectMonth.options.add(new Option(e + "月", e))
                    }
                    b.selectMonth.value = b.curDate[1]
                })();
                b.onchange(b.selectDate, c);
                break;
            case b.selectDate:
                if (c) {
                    b.curDate[2] = parseInt(a.value, 10) || b.curDate[2]
                }
                b.selectDate && (function() {
                    b.selectDate.options.length = 0;
                    var e = Math.max((function() {
                        var g = ((b.curDate[0] == b.minDate[0]) && (b.curDate[1] == b.minDate[1])) ? b.minDate[2] : 1;
                        return g
                    })(), 1);
                    var d = Math.min((function() {
                        var g = (b.curDate[1] in {
                            1 : true,
                            3 : true,
                            5 : true,
                            7 : true,
                            8 : true,
                            10 : true,
                            12 : true
                        }) ? 31 : 30;
                        return g
                    })(), (function() {
                        if (2 == b.curDate[1]) {
                            var g = (b.curDate[0] % 4 == 0 && b.curDate[0] % 100 != 0) || (b.curDate[0] % 100 == 0 && b.curDate[0] % 400 == 0) ? 28 : 29;
                            return g
                        } else {
                            return 31
                        }
                    })(), (function() {
                        var g = ((b.curDate[0] == b.maxDate[0]) && (b.curDate[1] == b.maxDate[1])) ? b.maxDate[2] : 31;
                        return g
                    })());
                    b.curDate[2] = [e, b.curDate[2], d].sort(function(h, g) {
                        return h > g ? 1 : 0
                    })[1];
                    for (var f = e; f <= d; f++) {
                        b.selectDate.options.add(new Option(f + "日", f))
                    }
                    b.selectDate.value = b.curDate[2]
                })();
            default:
                console.log(b.curDate);
                break
            }
            return b
        },
        formate: function(a) {
            var b = this;
            if (a instanceof Array) {
                return a
            }
            var c = new Array();
            c.push(parseInt(a.getFullYear(), 10));
            c.push(parseInt(a.getMonth() + 1, 10));
            c.push(parseInt(a.getDate(), 10));
            return c
        }
    };
    return _dater
})();