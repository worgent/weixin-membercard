if (!String.prototype.format) {
    String.prototype.format = function () {
        var args = arguments;
        return this.replace(/{(\d+)}/g, function (match, number) {
            return typeof args[number] != 'undefined'
                ? args[number]
                : match
                ;
        });
    };
}
var aSelect = function(options) {

    var bindEls = new Array();
    var items = {};

    var settings = {
        data: {},
        file: null,
        root: '0',
        ajax: null,
        timeout: 30,
        method: 'post',
        field_name: null,
        auto: false,
        default_text: "请选择"
    };

    if (options) {
        jQuery.extend(settings, options);
    }

    items = settings.data;

    function _bind(element, value) {
        var $e = $(element).data("selected");
        $e && (value = $e);
        var key = bindEls.length ? bindEls[bindEls.length - 1].key + ',' + bindEls[bindEls.length - 1].value : settings.root;

        bindEls.push({
            element: element,
            key: key,
            value: value
        });

        var item_count = 0;
        for (var i in items) {
            item_count++;
        }

        for (var el_id in bindEls) {
            if (bindEls[el_id].element == element) {
                var self_id = parseInt(el_id);
            }
        }

        for (var el_id in bindEls) {
            if (el_id < self_id) {
                bindEls[el_id].element.change(function() {
                    _fill(element);
                })
            }
        }

        if (self_id > 0) {
            bindEls[self_id - 1].element.change(function() {
                _fill(element, bindEls[self_id].key);
            });
        }

        element.change(function() {
            var self_key = bindEls[self_id - 1] ? bindEls[self_id].key + ',' + $(this).val() : '0,' + $(this).val();
            if (typeof bindEls[self_id + 1] != 'undefined') {
                bindEls[self_id + 1].key = self_key;
            }

            if (settings.field_name) {
                $(settings.field_name).val($(this).val());
            }

            if (settings.auto) {
                if (typeof bindEls[self_id + 1] == 'undefined') {
                    _find(self_key, function(key, json) {
                        if (json) {
                            var el = $('<select></select>');
                            element.after(el);
                            _bind(el, '');
                            _fill(bindEls[self_id + 1].element, key, json);
                        }
                    });
                }
            }
        })
        _fill(element, key, value);

    }

    function _fill(element, key, value) {

        element.empty();
        element.append('<option value="">{0}</option>'.format(settings.default_text));

        var json = _find(key, function() {
            _fill(element, key, value);
        });

        if (!json) {
            if (settings.auto) element.hide();
            return false;
        }
        element.show();
        var index = 1;
        var selected_index = 0;
        for (var opt_value in json) {
            var opt_title = json[opt_value];
            var selected = '';
            if (opt_value == value) {
                selected_index = index;
                selected = 'selected="selected"';
            }
            var option = $('<option value="' + opt_value + '" ' + selected + '>' + opt_title + '</option>');
            element.append(option);
            index++;
        }

        if (element[0]) {
            //IE6
            setTimeout(function() {
                element[0].options[selected_index].selected = true;
            }, 0);
            // ÈÃFFÑ¡ÖÐÄ¬ÈÏÏî
            element[0].selectedIndex = 0;
            element.attr('selectedIndex', selected_index);
        }
        element.width(element.width());
    }

    function _find(key, callback) {
        if (typeof key == 'undefined') { // ÈôÎ´¶¨Òåkey
            return null;
        } else if (key[key.length - 1] == ',') { // ÈôkeyÒÔ','½áÎ²£¬¿Ï¶¨ÊÇÈ¡²»µ½Öµ
            return null
        } else if (typeof(items[key]) == "undefined") {

            // ¼ÆËãitemsÔªËØ¸öÊý
            var item_count = 0;
            for (var i in items) {
                item_count++;
                break;
            }

            if (settings.ajax) {
                $.getJSON(settings.ajax, {
                    key: key
                }, function(json) {
                    items[key] = json;
                    callback(key, json);
                })
            } else if (settings.file && item_count == 0) {
                $.getJSON(settings.file, function(json) {
                    items = json;
                    callback(key, json);
                })
            }
        }

        return items[key];
    }

    function _getEl(element) {
        if (typeof element == 'string') {
            return $(element);
        } else {
            return element;
        }
    }

    return {
        bind: function(element, value) {
            if (typeof element != 'object') element = _getEl(element);
            value = value ? value : '';

            _bind(element, value);

        }
    }

}