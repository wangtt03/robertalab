define([ 'exports', 'message', 'log', 'jquery', 'jquery-validate', 'bootstrap' ], function(exports, MSG, LOG, $) {
    /**
     * Set cookie
     * 
     * @param {key}
     *            Key of the cookie
     * @param {value}
     *            Value of the cookie
     */
    function setCookie(key, value) {
        var expires = new Date();
        expires.setTime(expires.getTime() + (30 * 24 * 60 * 60 * 1000));
        document.cookie = key + '=' + value + ';expires=' + expires.toUTCString();
    }
    exports.setCookie = setCookie;

    /**
     * Get cookie
     * 
     * @param {key}
     *            Key of the cookie to read
     */
    function getCookie(key) {
        var keyValue = document.cookie.match('(^|;) ?' + key + '=([^;]*)(;|$)');
        return keyValue ? keyValue[2] : null;
    }
    exports.getCookie = getCookie;

    function clone(obj) {
        var copy;

        // Handle the 3 simple types, and null or undefined
        if (null == obj || "object" != typeof obj)
            return obj;

        // Handle Date
        if (obj instanceof Date) {
            copy = new Date();
            copy.setTime(obj.getTime());
            return copy;
        }

        // Handle Array
        if (obj instanceof Array) {
            copy = [];
            for (var i = 0, len = obj.length; i < len; i++) {
                copy[i] = clone(obj[i]);
            }
            return copy;
        }

        // Handle Object
        if (obj instanceof Object) {
            copy = {};
            for ( var attr in obj) {
                if (obj.hasOwnProperty(attr))
                    copy[attr] = clone(obj[attr]);
            }
            return copy;
        }

        throw new Error("Unable to copy obj! Its type isn't supported.");
    }
    exports.clone = clone;

    function isEmpty(obj) {
        return Object.keys(obj).length === 0 && obj.constructor === Object;
    }

    exports.isEmpty = isEmpty;

    function getPropertyFromObject(obj, prop, arrayIndex) {
        //property not found
        if (typeof obj === 'undefined')
            return false;

        //index of next property split
        var _index = prop.indexOf('.')

        //property split found; recursive call
        if (_index > -1) {
            //get object at property (before split), pass on remainder
            return getPropertyFromObject(obj[prop.substring(0, _index)], prop.substr(_index + 1), arrayIndex);
        }

        //no split; get property
        if (arrayIndex != undefined) {
            return obj[prop][arrayIndex];
        }
        return obj[prop];
    }

    exports.getPropertyFromObject = getPropertyFromObject

    function setObjectProperty(obj, prop, value, arrayIndex) {
        //property not found
        if (typeof obj === 'undefined')
            return false;

        //index of next property split
        var _index = prop.indexOf('.')

        //property split found; recursive call
        if (_index > -1) {
            //get object at property (before split), pass on remainder
            return setObjectProperty(obj[prop.substring(0, _index)], prop.substr(_index + 1), value, arrayIndex);
        }

        //no split; get property
        if (arrayIndex != undefined) {
            return obj[prop][arrayIndex] = value;
        }
        obj[prop] = value;
    }

    exports.setObjectProperty = setObjectProperty

    /**
     * Format date
     * 
     * @param {date}
     *            date from server to be formatted
     */
    function formatDate(dateLong) {
        if (dateLong) {
            var date = new Date(dateLong);
            var datestring = ("0" + date.getDate()).slice(-2) + "." + ("0" + (date.getMonth() + 1)).slice(-2) + "." + date.getFullYear() + ", "
                    + ("0" + date.getHours()).slice(-2) + ":" + ("0" + date.getMinutes()).slice(-2);
            return datestring;
        } else {
            return "";
        }
    }
    exports.formatDate = formatDate;

    /**
     * Convert date into numeric value
     * 
     * @param {d}
     *            date in the form 'dd.mm.yyyy, hh:mm:ss'
     */
    function parseDate(d) {
        if (d) {
            var dayPart = d.split(', ')[0];
            var timePart = d.split(', ')[1];
            var day = dayPart.split('.')[0];
            var month = dayPart.split('.')[1] - 1;
            var year = dayPart.split('.')[2];
            var hour = timePart.split(':')[0];
            var minute = timePart.split(':')[1];
            var second = timePart.split(':')[2];
            var mseconds = timePart.split('.')[1];
            var date = new Date(year, month, day, hour, minute, second, mseconds);
            return date.getTime();
        }
        return 0;
    }
    exports.parseDate = parseDate;

    /**
     * Format result of server call for logging
     * 
     * @param {result}
     *            Result-object from server call
     */
    function formatResultLog(result) {
        var str = "{";
        var comma = false;
        for (key in result) {
            if (comma) {
                str += ',';
            } else {
                comma = true;
            }
            str += '"' + key + '":';
            if (result.hasOwnProperty(key)) {
                // The output of items is limited to the first 100 characters
                if (result[key].length > 100) {
                    str += '"' + JSON.stringify(result[key]).substring(1, 100) + ' ..."';
                } else {
                    str += JSON.stringify(result[key]);
                }
            }
        }
        str += '}';
        return str;
    }
    exports.formatResultLog = formatResultLog;

    /**
     * Extension of Jquery-datatables for sorting German date fields
     */
    function initDataTables() {
        $.extend($.fn.dataTableExt.oSort['date-de-asc'] = function(a, b) {
            a = parseDate(a);
            b = parseDate(b);
            return ((a < b) ? -1 : ((a > b) ? 1 : 0));
        },

        $.fn.dataTableExt.oSort['date-de-desc'] = function(a, b) {
            a = parseDate(a);
            b = parseDate(b);
            return ((a < b) ? 1 : ((a > b) ? -1 : 0));
        });
    }
    exports.initDataTables = initDataTables;

    /**
     * Calculate height of data table
     */
    function calcDataTableHeight() {
        return Math.round($(window).height() - 100);
    }
    exports.calcDataTableHeight = calcDataTableHeight;

    function checkVisibility() {
        var stateKey, eventKey, keys = {
            hidden : "visibilitychange",
            webkitHidden : "webkitvisibilitychange",
            mozHidden : "mozvisibilitychange",
            msHidden : "msvisibilitychange"
        };
        for (stateKey in keys) {
            if (stateKey in document) {
                eventKey = keys[stateKey];
                break;
            }
        }
        return function(c) {
            if (c) {
                document.addEventListener(eventKey, c);
            }
            return !document[stateKey];
        };
    }
    exports.checkVisibility = checkVisibility;

    function setFocusOnElement($elem) {
        setTimeout(function() {
            if ($elem.is(":visible") == true) {
                $elem.focus();
            }
        }, 800);
    }
    exports.setFocusOnElement = setFocusOnElement;

    function showSingleModal(customize, onSubmit, onHidden, validator) {
        customize();
        $('#single-modal-form').onWrap('submit', function(e) {
            e.preventDefault();
            onSubmit();
        });
        $('#single-modal').onWrap('hidden.bs.modal', function() {
            $('#single-modal-form').unbind('submit');
            $('#singleModalInput').val('');
            $('#single-modal-form').validate().resetForm();
            onHidden();
        });
        $('#single-modal-form').removeData('validator');
        $('#single-modal-form').validate(validator);
        setFocusOnElement($("#singleModalInput"));
        $("#single-modal").modal('show');
    }
    exports.showSingleModal = showSingleModal;
    
    
    function showSingleListModal(customize, onSubmit, onHidden, validator) {
    	$('#single-modal-list-form').onWrap('submit', function(e) {
            e.preventDefault();
            onSubmit();
        });
    	$('#single-modal-list').onWrap('hidden.bs.modal', function() {
            $('#single-modal-list-form').unbind('submit');
            onHidden();
        });
        setFocusOnElement($("#singleModalListInput"));
        $("#single-modal-list").modal('show');
    }
    exports.showSingleListModal = showSingleListModal;

    /**
     * Helper to show the information on top of the share modal.
     * 
     */
    function showMsgOnTop(msg) {

        $('#show-message').find('button').removeAttr("data-dismiss");
        $('#show-message').find('button').one('click', function(e) {
            $('#show-message').modal("hide");
            $('#show-message').find('button').attr("data-dismiss", "modal");
        });
        MSG.displayInformation({
            rc : "not ok"
        }, "", msg);
    }
    exports.showMsgOnTop = showMsgOnTop;

    /**
     * Handle result of server call
     * 
     * @param {result}
     *            Result-object from server call
     */
    function response(result) {
        LOG.info('result from server: ' + formatResultLog(result));
        if (result.rc != 'ok') {
            MSG.displayMessage(result.message, "POPUP", "");
        }
    }
    exports.response = response;

    /**
     * Rounds a number to required decimal
     * 
     * @param value
     *            {Number} - to be rounded
     * @param decimals
     *            {Number} - number of decimals after rounding
     * @return {Number} rounded number
     * 
     */
    function round(value, decimals) {
        return Number(Math.round(value + 'e' + decimals) + 'e-' + decimals);
    }
    exports.round = round;

    /**
     * Get the sign of the number.
     * 
     * @param x
     *            {Number} -
     * @return {Number} - 1 if it is positive number o/w return -1
     */
    function sgn(x) {
        return (x > 0) - (x < 0);
    }
    exports.sgn = sgn;

    /**
     * Returns the basename (i.e. "hello" in "C:/folder/hello.txt")
     * 
     * @param path
     *            {String} - path
     */
    function getBasename(path) {
        var base = new String(path).substring(path.lastIndexOf('/') + 1);
        if (base.lastIndexOf(".") != -1) {
            base = base.substring(0, base.lastIndexOf("."));
        }
        return base;
    }
    exports.getBasename = getBasename;

    function download(filename, content) {
        var blob = new Blob([ content ]);
        if (window.navigator.msSaveOrOpenBlob) {
            window.navigator.msSaveOrOpenBlob(blob, filename);
        } else {
            var element = document.createElement('a');
            var myURL = window.URL || window.webkitURL;
            element.setAttribute('href', myURL.createObjectURL(blob));
            element.setAttribute('download', filename);
            element.style.display = 'none';
            document.body.appendChild(element);
            element.click();
            document.body.removeChild(element);
        }
    }
    exports.download = download;

    function getHashFrom(string) {
        var hash = 0;
        for (var i = 0; i < string.length; i++) {
            hash = ((hash << 5) - hash) + string.charCodeAt(i++);
        }
        return (hash < 0) ? ((hash * -1) + 0xFFFFFFFF) : hash;
    }
    exports.getHashFrom = getHashFrom;
    
    function countBlocks(xmlString) {
        var counter = 0;
        var pos = 0;

        while (true) {
            pos = xmlString.indexOf('<block', pos);
            if (pos != -1) {
                counter++;
                pos += 6;
            } else {
                break;
            }
        }
        return counter - 1;
    }
    exports.countBlocks = countBlocks;

    var __entityMap = {
        "&" : "&amp;",
        "<" : "&lt;",
        ">" : "&gt;",
        '"' : '&quot;',
        "'" : '&#39;',
        "/" : '&#x2F;'
    };

    String.prototype.escapeHTML = function() {
        return String(this).replace(/[&<>"'\/]/g, function(s) {
            return __entityMap[s];
        });
    }

    $.fn.draggable = function(opt) {

        opt = $.extend({
            handle : "",
            cursor : "move",
            draggableClass : "draggable",
            activeHandleClass : "active-handle"
        }, opt);

        var $selected = null;
        var $elements = (opt.handle === "") ? this : this.find(opt.handle);

        $elements.css('cursor', opt.cursor).on("mousedown touchstart", function(e) {
            var pageX = e.pageX || e.originalEvent.touches[0].pageX;
            var pageY = e.pageY || e.originalEvent.touches[0].pageY;
            if (opt.handle === "") {
                $selected = $(this);
                $selected.addClass(opt.draggableClass);
            } else {
                $selected = $(this).parent();
                $selected.addClass(opt.draggableClass).find(opt.handle).addClass(opt.activeHandleClass);
            }
            var drg_h = $selected.outerHeight(), drg_w = $selected.outerWidth(), pos_y = $selected.offset().top + drg_h - pageY, pos_x = $selected.offset().left
                    + drg_w - pageX;
            $(document).on("mousemove touchmove", function(e) {
                var pageX = e.pageX || e.originalEvent.touches[0].pageX;
                var pageY = e.pageY || e.originalEvent.touches[0].pageY;
                // special case movable slider between workspace and right divs
                if (opt.axis == 'x') {
                    var left = pageX + pos_x - drg_w - 6;
                    var left = Math.min(left, $('#main-section').width() - 80);
                    var left = Math.max(left, 42);
                    $selected.offset({
                        top : 0,
                        left : left
                    });
                    $('#blocklyDiv').width(left + 6);
                    $('.rightMenuButton.shifted').css({
                        'right' : $('#main-section').width() - left - 10
                    });
                    $(window).resize();
                } else {
                    $selected.offset({
                        top : pageY + pos_y - drg_h,
                        left : pageX + pos_x - drg_w
                    });
                }
                $selected.css({
                    right : 'auto',
                });
            }).on("mouseup touchend", function() {
                $(this).off("mousemove touchmove"); // Unbind events from document
                if ($selected !== null) {
                    $selected.removeClass(opt.draggableClass);
                    $selected = null;
                }
            });
        }).on("mouseup touchend", function() {
            if ($selected) {
                if (opt.handle === "") {
                    $selected.removeClass(opt.draggableClass);
                } else {
                    $selected.removeClass(opt.draggableClass).find(opt.handle).removeClass(opt.activeHandleClass);
                }
            }
            $selected = null;
        });
        return this;
    };
});
