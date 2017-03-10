define([ 'exports', 'log', 'message', 'util', 'group.model', 'guiState.controller', 'jquery', 'jquery-validate',  'blocks', 'blocks-msg' ], function(exports, LOG, MSG, UTIL, GROUP,
        GUISTATE_C, $, Blockly) {

    var $divForms;
    var $formSingleModal;

    function init() {
        initEvents();
        initGroupForms();
    }
    exports.init = init;
    
    /**
     * Create new group
     */
    
    function createGroupToServer() {
        $('.modal').modal('hide'); // close all opened popups
        var groupName;
        groupName = $('#singleModalInput').val().trim();
        LOG.info('create group ' + groupName);
        GROUP.createGroupToServer(groupName, function(result) {
        UTIL.response(result);
        if (result.rc === 'ok') {
            result.name = groupName;
            GUISTATE_C.setGroupName(result);
            MSG.displayInformation(result, "MESSAGE_EDIT_SAVE_GROUP_AS", result.message, GUISTATE_C.getGroupName());
        }
        });
    }
    
    exports.createGroupToServer = createGroupToServer;
    
    function initGroupForms() {
        $formSingleModal = $('#single-modal-form');
        $('#buttonCancelFirmwareUpdateAndRun').onWrap('click', function() {
            start();
        });
    }
    exports.initGroupForms = initGroupForms;

    function showSaveAsModal() {
        $.validator.addMethod("regex", function(value, element, regexp) {
            value = value.trim();
            return value.match(regexp);
        }, "No special Characters allowed here.");

        UTIL.showSingleModal(function() {
            $('#singleModalInput').attr('type', 'text');
            $('#single-modal h3').text("Register a group");
            $('#single-modal label').text("Name");
        }, createGroupToServer, function() {

        }, {
            rules : {
                singleModalInput : {
                    required : true,
                    regex : /^[a-zA-Z_öäüÖÄÜß$€][a-zA-Z0-9_öäüÖÄÜß$€]*$/
                }
            },
            errorClass : "form-invalid",
            errorPlacement : function(label, element) {
                label.insertAfter(element);
            },
            messages : {
                singleModalInput : {
                    //required : Blockly.Msg["VALIDATION_FIELD_REQUIRED"],
                    //regex : Blockly.Msg["MESSAGE_INVALID_NAME"]
                }
            }
        });
    }
    exports.showSaveAsModal = showSaveAsModal;
    
});
