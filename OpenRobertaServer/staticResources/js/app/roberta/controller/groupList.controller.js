define([ 'require', 'exports', 'log', 'util', 'comm', 'groupList.model', 'group.model', 'group.controller', 'blocks-msg', 'jquery', 'bootstrap-table' ], function(
        require, exports, LOG, UTIL, COMM, GROUPLIST, GROUP, GROUP_C, Blockly, $) {

    /**
     * Initialize table of groups
     */
    function init() {

        initGroupList();
        initGroupListEvents();
        LOG.info('init group list view');
    }
    exports.init = init;

    function initGroupList() {

        $('#groupNameTable').bootstrapTable({
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
                title : "<span lkey='Blockly.Msg.DATATABLE_GROUP_NAME'>Name of the group</span>",
                sortable : true,
                field : '0',
            }, {
                title : "<span lkey='Blockly.Msg.DATATABLE_CREATED_BY'>Created by</span>",
                sortable : true,
                field : '1',
            }, /*{
                title : "<span lkey='Blockly.Msg.DATATABLE_CREATED_ON'>Created on</span>",
                sortable : true,
                field : '3',
                formatter : UTIL.formatDate
            }, {
                title : "<span lkey='Blockly.Msg.DATATABLE_ACTUALIZATION'>Letzte Aktualisierung</span>",
                sortable : true,
                field : '4',
                formatter : UTIL.formatDate
            }, */{
                field : '5',
                checkbox : true,
                valign : 'middle',
            },
            {
                field : '7',
                events : eventsDeleteShareLoad,
                title : '<a href="#" class="deleteSomeGroup disabled" title="Delete selected groups">' + '<span class="typcn typcn-delete"></span></a>',
                align : 'left',
                valign : 'top',
                formatter : formatDeleteShareLoad,
                width : '89px',
            }, ]
        });
        $('#groupNameTable').bootstrapTable('togglePagination');
    }

    function initGroupListEvents() {

        $(window).resize(function() {
            $('#groupNameTable').bootstrapTable('resetView', {
                height : UTIL.calcDataTableHeight()
            });
        });
        $('#tabGroupList').on('show.bs.tab', function(e) {
            guiStateController.setView('tabGroupList');
            GROUPLIST.loadGroupList(update);
        });

        $('.bootstrap-table').find('button[name="refresh"]').onWrap('click', function() {
        	GROUPLIST.loadGroupList(update);
            return false;
        }, "refresh group list clicked");

        $('#groupNameTable').onWrap('dbl-click-row.bs.table', function($element, row) {
            GROUP_C.loadFromListing(row);
        }, "Load group from listing double clicked");

        $('#groupNameTable').onWrap('check-all.bs.table', function($element, rows) {
            $('.deleteSomeGroup').removeClass('disabled');
            $('.delete').addClass('disabled');
            $('.load').addClass('disabled');
        }, 'check all groups');

        $('#groupNameTable').onWrap('check.bs.table', function($element, row) {
            $('.deleteSomeGroup').removeClass('disabled');
            $('.delete').addClass('disabled');
            $('.load').addClass('disabled');
        }, 'check one group');

        $('#groupNameTable').onWrap('uncheck-all.bs.table', function($element, rows) {
            $('.deleteSomeGroup').addClass('disabled');
            $('.delete').removeClass('disabled');
            $('.load').removeClass('disabled');
        }, 'uncheck all groups');

        $('#groupNameTable').onWrap('uncheck.bs.table', function($element, row) {
            var selectedRows = $('#groupNameTable').bootstrapTable('getSelections');
            if (selectedRows.length <= 0 || selectedRows == null) {
                $('.deleteSomeGroup').addClass('disabled');
                $('.delete').removeClass('disabled');
                $('.load').removeClass('disabled');
            }
        }, 'uncheck one group');

        $('#backGroupList').onWrap('click', function() {
            $('#tabGroup').trigger('click');
            return false;
        }, "back to group view")

        $(document).onWrap('click', '.deleteSomeGroup', function() {
            var group = $('#groupNameTable').bootstrapTable('getSelections', {});
            var names = '';
            for (var i = 0; i < group.length; i++) {
                names += group[i][0];
                names += '<br>';
            }
            $('#confirmDeleteGroupName').html(names);
            $('#confirmDeleteGroup').one('hide.bs.modal', function(event) {
                GROUPLIST.loadGroupList(update);
            });
            $("#confirmDeleteGroup").data('group', group);
            $("#confirmDeleteGroup").modal("show");
            return false;
        }, "delete groups");

        $('#groupNameTable').on('shown.bs.collapse hidden.bs.collapse', function(e) {
            $('#groupNameTable').bootstrapTable('resetWidth');
        });

        function update(result) {
            UTIL.response(result);
            if (result.rc === 'ok') {
                $('#groupNameTable').bootstrapTable("load", result.groupNames);
                $('#groupNameTable').bootstrapTable("showColumn", '2');
                $('#groupNameTable').bootstrapTable("showColumn", '3');
                if ($('#tabGroupList').data('type') === 'group') {
                    $('.deleteSomeGroup').show();
                } else {
                    $('.deleteSomeGroup').hide();
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
            $('#confirmDeleteGroupName').html(names);
            $("#confirmDeleteGroup").data('group', selectedRows);
            $('#confirmDeleteGroup').one('hidden.bs.modal', function(event) {
            });
            $("#confirmDeleteGroup").modal("show");
            return false;
        },
        'click .load' : function(e, value, row, index) {
            GROUP_C.loadFromListing(row);
        }
    };

    
    var formatDeleteLoad = function(value, row, index) {
        var result = '';
        if ($('#tabGroupList').data('type') === 'group') {
            result += '<a href="#" class="delete" title="Delete group"><span class="typcn typcn-delete"></span></a>';
        }
        result += '<a href="#" class="load "  title="Load group"><span class="typcn typcn-document"></span></a>';
        return result;
    }

});
