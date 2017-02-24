define([ 'require', 'exports', 'log', 'util', 'comm', 'userGroupList.model', 'userGroup.model', 'userGroup.controller', 'blocks-msg', 'jquery', 'bootstrap-table' ], function(
        require, exports, LOG, UTIL, COMM, GROUPLIST, GROUP, GROUP_C, Blockly, $) {

    /**
     * Initialize table of user groups
     */
    function init() {

        initUserGroupList();
        initUserGroupListEvents();
        LOG.info('init user group list view');
    }
    exports.init = init;

    function initUserGroupList() {

        $('#userGroupNameTable').bootstrapTable({
            height : UTIL.calcDataTableHeight(),
            pageList : '[ 10, 25, All ]',
            toolbar : '#groupListToolbar',
            showRefresh : 'true',
            showPaginationSwitch : 'true',
            pagination : 'true',
            buttonsAlign : 'right',
            resizable : 'true',
            iconsPrefix : 'typcn',
            icons : {
                paginationSwitchDown : 'typcn-document-text',
                paginationSwitchUp : 'typcn-book',
                refresh : 'typcn-refresh',
            },
            columns : [ {
                title : "<span lkey='Blockly.Msg.DATATABLE_USER_GROUP_NAME'>Name of the user</span>",
                sortable : true,
                field : '0',
            }, 
            {
                field : '7',
                events : eventsDeleteLoad,
                title : '<a href="#" class="deleteSomeUserGroup disabled" title="Delete selected user groups">' + '<span class="typcn typcn-delete"></span></a>',
                align : 'left',
                valign : 'top',
                formatter : formatDeleteShareLoad,
                width : '89px',
            }, ]
        });
        $('#userGroupNameTable').bootstrapTable('togglePagination');
    }

    function initUserGroupListEvents() {

        $(window).resize(function() {
            $('#userGroupNameTable').bootstrapTable('resetView', {
                height : UTIL.calcDataTableHeight()
            });
        });
        $('#tabUserGroupList').on('show.bs.tab', function(e) {
            guiStateController.setView('tabUserGroupList');
            USERGROUPLIST.loadUserGroupList(update);
        });

        $('.bootstrap-table').find('button[name="refresh"]').onWrap('click', function() {
        	USERGROUPLIST.loadUserGroupList(update);
            return false;
        }, "refresh user group list clicked");

        $('#userGroupNameTable').onWrap('dbl-click-row.bs.table', function($element, row) {
            USERGROUP_C.loadFromListing(row);
        }, "Load group from listing double clicked");

        $('#userGroupNameTable').onWrap('check-all.bs.table', function($element, rows) {
            $('.deleteSomeUserGroup').removeClass('disabled');
            $('.delete').addClass('disabled');
            $('.load').addClass('disabled');
        }, 'check all user groups');

        $('#userGroupNameTable').onWrap('check.bs.table', function($element, row) {
            $('.deleteSomeUserGroup').removeClass('disabled');
            $('.delete').addClass('disabled');
            $('.load').addClass('disabled');
        }, 'check one user group');

        $('#userGroupNameTable').onWrap('uncheck-all.bs.table', function($element, rows) {
            $('.deleteSomeUserGroup').addClass('disabled');
            $('.delete').removeClass('disabled');
            $('.load').removeClass('disabled');
        }, 'uncheck all user groups');

        $('#userGroupNameTable').onWrap('uncheck.bs.table', function($element, row) {
            var selectedRows = $('#userGroupNameTable').bootstrapTable('getSelections');
            if (selectedRows.length <= 0 || selectedRows == null) {
                $('.deleteSomeUserGroup').addClass('disabled');
                $('.delete').removeClass('disabled');
                $('.load').removeClass('disabled');
            }
        }, 'uncheck one user group');

        $('#backUserGroupList').onWrap('click', function() {
            $('#tabGroup').trigger('click');
            return false;
        }, "back to group view")

        $(document).onWrap('click', '.deleteSomeUserGroup', function() {
            var userGroup = $('#userGroupNameTable').bootstrapTable('getSelections', {});
            var names = '';
            for (var i = 0; i < group.length; i++) {
                names += group[i][0];
                names += '<br>';
            }
            $('#confirmDeleteUserGroupName').html(names);
            $('#confirmDeleteUserGroup').one('hide.bs.modal', function(event) {
                USERGROUPLIST.loadGroupList(update);
            });
            $("#confirmDeleteUserGroup").data('group', group);
            $("#confirmDeleteUserGroup").modal("show");
            return false;
        }, "delete user groups");

        $('#groupUserNameTable').on('shown.bs.collapse hidden.bs.collapse', function(e) {
            $('#groupUserNameTable').bootstrapTable('resetWidth');
        });

        function update(result) {
            UTIL.response(result);
            if (result.rc === 'ok') {
                //$('#programNameTable').bootstrapTable({});
                $('#groupUserNameTable').bootstrapTable("load", result.programNames);
                $('#groupUserNameTable').bootstrapTable("showColumn", '2');
                $('#groupUserNameTable').bootstrapTable("showColumn", '3');
                if ($('#tabUserGroupList').data('type') === 'group') {
                    $('.deleteSomeUserGroup').show();
                } else {
                    $('.deleteSomeUserGroup').hide();
                }
            }
        }
    }

    var eventsDeleteLoad = {
        'click .delete' : function(e, value, row, index) {
            var selectedRows = [ row ];
            var names = '';
            for (var i = 0; i < selectedRows.length; i++) {
                names += selectedRows[i][0];
                names += '<br>';
            }
            $('#confirmDeleteUserGroupName').html(names);
            $("#confirmDeleteUserGroup").data('userGroup', selectedRows);
            $('#confirmDeleteUserGroup').one('hidden.bs.modal', function(event) {
            });
            $("#confirmDeleteUserGroup").modal("show");
            return false;
        },
        'click .load' : function(e, value, row, index) {
            USERGROUP_C.loadFromListing(row);
        }
    };

    
    var formatDeleteLoad = function(value, row, index) {
        var result = '';
        if ($('#tabUserGroupList').data('type') === 'userGroup') {
            result += '<a href="#" class="delete" title="Delete user group"><span class="typcn typcn-delete"></span></a>';
        }
        result += '<a href="#" class="load "  title="Load user group"><span class="typcn typcn-document"></span></a>';
        return result;
    }

});
