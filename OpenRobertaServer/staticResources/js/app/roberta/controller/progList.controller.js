define([ 'require', 'exports', 'log', 'util', 'comm', 'progList.model', 'program.model', 'program.controller', 'blocks-msg', 'jquery', 'bootstrap-table' ], function(require, exports, LOG, UTIL, COMM, PROGLIST, PROGRAM, PROGRAM_C, Blockly, $) {

    /**
     * Initialize table of programs
     */
    function init() {

        initProgList();
        initProgListEvents();
        LOG.info('init program list view');
    }
    exports.init = init;

    function initProgList() {

        $('#programNameTable').bootstrapTable({
            height : UTIL.calcDataTableHeight(),
            pageList : '[ 10, 25, All ]',
            toolbar : '#progListToolbar',
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
                title : "<span lkey='Blockly.Msg.DATATABLE_PROGRAM_NAME'>" + (Blockly.Msg.DATATABLE_PROGRAM_NAME || "Name des Programms") + "</span>",
                sortable : true,
            }, {
                title : "<span lkey='Blockly.Msg.DATATABLE_CREATED_BY'>" + (Blockly.Msg.DATATABLE_CREATED_BY || "Erzeugt von") + "</span>",
                sortable : true,
            }, {
                title : "<span class='typcn typcn-flow-merge'></span>",
                sortable : true,
                sorter : sortRelations,
                formatter : formatRelations,
                align : 'left',
                valign : 'middle',
            }, {
                title : "<span lkey='Blockly.Msg.DATATABLE_CREATED_ON'>" + (Blockly.Msg.DATATABLE_CREATED_ON || "Erzeugt am") + "</span>",
                sortable : true,
                formatter : UTIL.formatDate
            }, {
                title : "<span lkey='Blockly.Msg.DATATABLE_ACTUALIZATION'>" + (Blockly.Msg.DATATABLE_ACTUALIZATION || "Letzte Aktualisierung") + "</span>",
                sortable : true,
                formatter : UTIL.formatDate
            }, {
                checkbox : true,
                valign : 'middle',
            }, {
                events : eventsDeleteShareLoad,
                title : titleActions,
                align : 'left',
                valign : 'top',
                formatter : formatDeleteShareLoad,
             // TODO activate gallery for release 2.3.0
             // width : '110px',
                width : '89px',
            }, ]
        });
        $('#programNameTable').bootstrapTable('togglePagination');
    }

    function initProgListEvents() {

        $(window).resize(function() {
            $('#programNameTable').bootstrapTable('resetView', {
                height : UTIL.calcDataTableHeight()
            });
        });
        $('#tabProgList').on('show.bs.tab', function(e) {
            guiStateController.setView('tabProgList');
            if ($('#tabProgList').data('type') === 'userProgram') {
                PROGLIST.loadProgList(update);
            } else {
                PROGLIST.loadExampleList(update);
            }
        });

        $('.bootstrap-table').find('button[name="refresh"]').onWrap('click', function() {
            if ($('#tabProgList').data('type') === 'userProgram') {
                PROGLIST.loadProgList(update);
            } else {
                PROGLIST.loadExampleList(update);
            }
            return false;
        }, "refresh program list clicked");

        $('#programNameTable').onWrap('dbl-click-row.bs.table', function($element, row) {
            PROGRAM_C.loadFromListing(row);
        }, "Load program from listing double clicked");

        $('#programNameTable').onWrap('check-all.bs.table', function($element, rows) {
            $('.deleteSomeProg').removeClass('disabled');
            $('#shareSome').removeClass('disabled');
            $('.delete').addClass('disabled');
            $('.share').addClass('disabled');
            $('.load').addClass('disabled');
        }, 'check all programs');

        $('#programNameTable').onWrap('check.bs.table', function($element, row) {
            $('.deleteSomeProg').removeClass('disabled');
            $('#shareSome').removeClass('disabled');
            $('.delete').addClass('disabled');
            $('.share').addClass('disabled');
            $('.load').addClass('disabled');
        }, 'check one program');

        $('#programNameTable').onWrap('uncheck-all.bs.table', function($element, rows) {
            $('.deleteSomeProg').addClass('disabled');
            $('#shareSome').addClass('disabled');
            $('.delete').removeClass('disabled');
            $('.share').removeClass('disabled');
            $('.load').removeClass('disabled');
        }, 'uncheck all programs');

        $('#programNameTable').onWrap('uncheck.bs.table', function($element, row) {
            var selectedRows = $('#programNameTable').bootstrapTable('getSelections');
            if (selectedRows.length <= 0 || selectedRows == null) {
                $('.deleteSomeProg').addClass('disabled');
                $('#shareSome').addClass('disabled');
                $('.delete').removeClass('disabled');
                $('.share').removeClass('disabled');
                $('.load').removeClass('disabled');
            }
        }, 'uncheck one program');

        $('#backProgList').onWrap('click', function() {
            $('#tabProgram').trigger('click');
            return false;
        }, "back to program view")

        $(document).onWrap('click', '.deleteSomeProg', function() {
            var programs = $('#programNameTable').bootstrapTable('getSelections', {});
            var names = '';
            for (var i = 0; i < programs.length; i++) {
                names += programs[i][0];
                names += '<br>';
            }
            $('#confirmDeleteProgramName').html(names);
            $('#confirmDeleteProgram').one('hide.bs.modal', function(event) {
                PROGLIST.loadProgList(update);
            });
            $("#confirmDeleteProgram").data('programs', programs);
            $("#confirmDeleteProgram").modal("show");
            return false;
        }, "delete programs");

        $('#programNameTable').on('shown.bs.collapse hidden.bs.collapse', function(e) {
            $('#programNameTable').bootstrapTable('resetWidth');
        });

        function update(result) {
            UTIL.response(result);
            if (result.rc === 'ok') {
                $('#programNameTable').bootstrapTable("load", result.programNames);
                $('#programNameTable').bootstrapTable("showColumn", '2');
                $('#programNameTable').bootstrapTable("showColumn", '3');
                if ($('#tabProgList').data('type') === 'userProgram') {
                    $('.deleteSomeProg').show();
                } else {
                    $('.deleteSomeProg').hide();
                }
            }

            $('#deleteSomeProg').attr('data-original-title', Blockly.Msg.PROGLIST_DELETE_ALL_TOOLTIP || "Click here to delete all selected programs.");
            $('#programNameTable').find('.delete').attr('data-original-title', Blockly.Msg.PROGLIST_DELETE_TOOLTIP || 'Click here to delete your program.');
            $('#programNameTable').find('.share').attr('data-original-title', Blockly.Msg.PROGLIST_SHARE_TOOLTIP || "Click here to share your program with a friend.");
            $('#programNameTable').find('.load').attr('data-original-title', Blockly.Msg.PROGLIST_LOAD_TOOLTIP || 'Click here to load your program in the programming environment.');
            $('#programNameTable').find('[rel="tooltip"]').tooltip();
        }
    }

    var eventsDeleteShareLoad = {
        'click .delete' : function(e, value, row, index) {
            var selectedRows = [ row ];
            var names = '';
            for (var i = 0; i < selectedRows.length; i++) {
                names += selectedRows[i][0];
                names += '<br>';
            }
            $('#confirmDeleteProgramName').html(names);
            $("#confirmDeleteProgram").data('programs', selectedRows);
            $('#confirmDeleteProgram').one('hidden.bs.modal', function(event) {
            });
            $("#confirmDeleteProgram").modal("show");
            return false;
        },
        'click .share' : function(e, value, row, index) {
            if (!row[2].sharedFrom) {
                $('#show-relations').trigger('updateAndShow', [ row ]);
            }
            return false;
        },
     // TODO activate gallery for release 2.3.0
//        'click .gallery' : function(e, value, row, index) {
//            if (!row[2].sharedFrom) {
//                $('#share-with-gallery').trigger('updateAndShow', [ row ]);
//            }
//            return false;
//        },
        'click .load' : function(e, value, row, index) {
            PROGRAM_C.loadFromListing(row);
        }
    };

    var formatRelations = function(value, row, index) {
        if ($.isEmptyObject(value)) {
            return '<span class="typcn typcn-minus"></span>';
        }
        if (value.sharedFrom === 'READ') {
            return '<span class="typcn typcn-eye"></span>';
        }
        if (value.sharedFrom === 'WRITE') {
            return '<span class="typcn typcn-pencil"></span>';
        }
        if (value.sharedWith && Object.keys(value.sharedWith).length == 1) {
            var result = '';
            $.each(value.sharedWith, function(i, obj) {
                $.each(obj, function(user, right) {
                    result += '<span>';
                    if (user === 'Roberta') {
                        // should not happen
                    } else if (user === 'Gallery') {
                        result += '<span class="typcn typcn-puzzle-outline"></span>';
                    } else if (right === 'READ') {
                        result += '<span class="typcn typcn-eye"></span>';
                    } else {
                        result += '<span rel="tooltip" data-original-title="WRITE" class="typcn typcn-pencil"></span>';
                    }
                    result += '&nbsp;';
                    result += user;
                    result += '</span>';
                });
            });
            return result;
        }
        if (value.sharedWith && Object.keys(value.sharedWith).length > 1) {
            var result = [];
            $.each(value.sharedWith, function(i, obj) {
                $.each(obj, function(user, right) {
                    if (user === 'Roberta') {
                        return true;
                    } else if (user === 'Gallery') {
                        result.unshift('<span class="typcn typcn-puzzle-outline"></span>&nbsp;' + user);
                    } else if (right === 'READ') {
                        result.push('<span class="typcn typcn-eye"></span>&nbsp;' + user);
                    } else {
                        result.push('<span class="typcn typcn-pencil"></span>&nbsp;' + user);
                    }
                });
            });
            var resultString = '<div style="white-space:nowrap;"><span style="float:left;">';
            resultString += result[0];
            resultString += '</span><a class="collapsed showRelations" href="#" style="float:right;"' + 'href="#" data-toggle="collapse" data-target=".relation' + index + '"></a></div>';
            for (var i = 1; i < result.length; i++) {
                resultString += '<div style="clear:both;" class="collapse relation' + index + '">';
                resultString += result[i];
                resultString += '</div>';
            }
            return resultString;
        }
    }

    var formatDeleteShareLoad = function(value, row, index) {
        // TODO check here and on the server, if this user is allowed to share programs
        var result = '';
        if ($('#tabProgList').data('type') === 'userProgram') {
            result += '<a href="#" class="delete" rel="tooltip" lkey="Blockly.Msg.PROGLIST_DELETE_TOOLTIP" data-original-title="" title=""><span class="typcn typcn-delete"></span></a>';
            if (row[2].sharedFrom) {
                result += '<a href="#" class="share disabled" rel="tooltip" lkey="Blockly.Msg.PROGLIST_SHARE_TOOLTIP" data-original-title="" title=""><span class="typcn typcn-flow-merge"></span></a>';
                // TODO activate gallery for release 2.3.0
                // result += '<a href="#" class="gallery disabled" rel="tooltip" lkey="Blockly.Msg.PROGLIST_SHARE_WITH_GALLERY_TOOLTIP" data-original-title="" title=""><span class="typcn typcn-puzzle-outline"></span></a>';
            } else {
                result += '<a href="#" class="share" rel="tooltip" lkey="Blockly.Msg.PROGLIST_SHARE_TOOLTIP" data-original-title="" title=""><span class="typcn typcn-flow-merge"></span></a>';
             // TODO activate gallery for release 2.3.0
                // result += '<a href="#" class="gallery" rel="tooltip" lkey="Blockly.Msg.PROGLIST_SHARE_WITH_GALLERY_TOOLTIP" data-original-title="" title=""><span class="typcn typcn-puzzle-outline"></span></a>';
            }
        }
        result += '<a href="#" class="load" rel="tooltip" lkey="Blockly.Msg.PROGLIST_LOAD_TOOLTIP" data-original-title="" title=""><span class="typcn typcn-document"></span></a>';
        return result;
    }

    var sortRelations = function(a, b) {
        if ($.isEmptyObject(a) && $.isEmptyObject(b)) {
            return 0;
        }
        if (a.sharedFrom && b.sharedFrom) {
            if (a.sharedFrom === 'WRITE' && b.sharedFrom === 'WRITE')
                return 0;
            if (a.sharedFrom === 'WRITE')
                return 1;
            else
                return -1;
        }
        if (a.sharedWith && b.sharedWith) {
            var value = {};
            $.each(a.sharedWith, function(i, obj) {
                $.each(obj, function(user, right) {
                    value.a = right;
                    return false;
                });
                return false;
            });
            $.each(b.sharedWith, function(i, obj) {
                $.each(obj, function(user, right) {
                    value.b = right;
                    return false;
                });
                return false;
            });
            if (value.a === value.b)
                return 0;
            if (value.a === 'WRITE')
                return 1;
            else
                return -1;
        }
        if ($.isEmptyObject(a)) {
            return -1;
        }
        if ($.isEmptyObject(b)) {
            return 1;
        }
        if (a.sharedWith) {
            return 1;
        }
        return -1;
    }
    var titleActions = '<a href="#" id="deleteSomeProg" class="deleteSomeProg disabled" rel="tooltip" lkey="Blockly.Msg.PROGLIST_DELETE_ALL_TOOLTIP" data-original-title="" data-container="body" title="">' + '<span class="typcn typcn-delete"></span></a>';
});
