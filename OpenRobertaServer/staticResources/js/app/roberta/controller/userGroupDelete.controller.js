define([ 'require', 'exports', 'log', 'util', 'message', 'comm', 'userGroup.model', 'blocks-msg', 'jquery', 'bootstrap-table' ], function(require, exports, LOG,
        UTIL, MSG, COMM, USERGROUP, Blockly, $) {

    function init() {
//        initView();
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
            var groupUsers = $("#confirmDeleteUserGroup").data('userGroup');
            for (var i = 0; i < groupUsers.length; i++) {
                var user = groupUsers[i];
                var userName = user[0];
                var groupName = user[1];
                USERGROUP.deleteUserGroupFromListing(userGroupName, function(result, userGroupName) {
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
