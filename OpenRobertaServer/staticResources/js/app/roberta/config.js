/**
 * Created by t-zhhong on 2017/7/18.
 */

define([ 'require', 'exports', 'log', 'util', 'comm', 'blocks-msg', 'jquery', 'bootstrap-table' ], function(require, exports, LOG, UTIL, COMM, Blockly, $) {
    var isiPad = false;
    var userDevice = 'PC';

    function init() {
        if(navigator.userAgent.indexOf('iPad') > -1){
            isiPad = true;
            userDevice = 'iPad';
        }
        return true;
    }
    exports.init = init;

    function getIsiPad(){
        //return isiPad;
        return false;
    }
    exports.getIsiPad = getIsiPad;

    function getUserDevice(){
        return userDevice;
    }
    exports.getUserDevice = getUserDevice;

});
