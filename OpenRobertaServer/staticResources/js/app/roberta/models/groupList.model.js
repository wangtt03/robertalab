/**
 * Rest calls to the server related to group operations (save, delete ...)
 * 
 * @module rest/group
 */
define([ 'exports', 'comm' ], function(exports, COMM) {

    /**
     * Refresh group list
     */
    function loadProgList(successFn) {
        COMM.json("/usergroups", {
            "cmd" : "getMemberGroups"
        }, successFn, "load group list");
    }
    exports.loadGroupList = loadGroupList;

});
