define([ 'require', 'exports', 'log', 'util', 'message', 'comm', 'userGroup.model', 'guiState.controller', 'blocks-msg', 'jquery', 'bootstrap-table' ], function(require, exports, LOG,
        UTIL, MSG, COMM, USERGROUP, GUISTATE_C, Blockly, $) {

    function init() {

        initEvents();
        LOG.info('init user group delete');
    }
    exports.init = init;

    function initView() {

    }

    function initEvents() {
        /**
         * Delete the user groups that were selected in a user group list
         */
        $('#doDeleteUserGroup').onWrap('click', function() {
            var user = $("#confirmDeleteUserGroup").data('userGroup');
            for (var i = 0; i < groupUsers.length; i++) {
                var userName = user[0];
                var groupName = GUISTATE_C.getGroupName();
                USERGROUP.deleteUserFromTheGroup(userName, function(userName, groupName, result) {
                UTIL.response(result);
                if (result.rc === 'ok') {
                    MSG.displayInformation(result, "MESSAGE_USERGROUP_DELETED", result.message, userGroupName);
                    $('.bootstrap-table').find('button[name="refresh"]').trigger('click');
                    LOG.info('delete group "' + groupName + "'");
                }
            });
                
            }
            $('.modal').modal('hide');
        }), 'doDeleteUserGroup clicked';
    }
});
