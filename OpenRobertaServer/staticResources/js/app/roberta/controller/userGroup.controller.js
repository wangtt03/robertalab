define([ 'exports', 'log', 'message', 'util', 'userGroup.model', 'guiState.controller', 'jquery', 'blocks', 'blocks-msg' ], function(exports, LOG, MSG, UTIL, USERGROUP,
        GUISTATE_C, $, Blockly) {

    var $divForms;
    var $formSingleModal;
    var $modalAnimateTime = 300;
    var $msgAnimateTime = 150;
    var $msgShowTime = 2000;
    
    function init() {
        initEvents();
        initGroupForms();
    }
    exports.init = init;

    /**
     * Add a user to group
     */
    function addUserToGroup() {
    	$('.modal').modal('hide'); // close all opened popups
        var userName = $('#singleModalInput').val().trim();
        var groupName = GUISTATE_C.getGroupName();
        LOG.info('add user ' + userName + 'to a group' + groupName);
        USERGROUP.addUser(userName, groupName, function(result) {
        UTIL.response(result);
        if (result.rc === 'ok') {
            MSG.displayInformation(result, "ADDED_USER", result.message, userName);
        }
        });
    }
    exports.addUserToGroup = addUserToGroup;
       
    function showSaveAsModal() {
        $.validator.addMethod("regex", function(value, element, regexp) {
            value = value.trim();
            return value.match(regexp);
        }, "No special Characters allowed here.");

        UTIL.addUserToGroup(function() {
            $('#singleModalInput').attr('type', 'text');
            $('#single-modal h3').text("Add a user");
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
