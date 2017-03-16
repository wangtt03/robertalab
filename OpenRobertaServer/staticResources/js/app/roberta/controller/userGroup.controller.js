define([ 'exports', 'log', 'message', 'util', 'userGroup.model', 'guiState.controller', 'jquery', 'blocks', 'blocks-msg' ], function(exports, LOG, MSG, UTIL, USERGROUP,
        GUISTATE_C, $, Blockly) {

    var $divForms;
    var $formSingleModal;
    var $modalAnimateTime = 300;
    var $msgAnimateTime = 150;
    var $msgShowTime = 2000;

    /**
     * Create new user group
     */
    function createUserGroupToServer() {
        $formRegister.validate();
        if ($formRegister.valid()) {
            USERGROUP.createUserToServer($("#addUser").val(), $('#addGroup').val(), function(
                    result) {
                if (result.rc === "ok") {
                	$('#addUser').val($('#addUser').val());
                    $('#addGroup').val($('#addGroup').val());
                }
                MSG.displayInformation(result, "", result.message);
            });
        }
    }

    
    /**
     * Get user group from server
     */
    function getUserFromServer() {
        USERGROUP.getUserGroupFromServer(GUISTATE_C.getUserGroup(), function(result) {
            if (result.rc === "ok") {
                $("#registerUserGroup").val(result.userGroup);
            }
        });
    }

    
    /**
     * Delete user group on server
     */
    function deleteUserGroupOnServer() {
        $formSingleModal.validate();
        if ($formSingleModal.valid()) {
            USER_GROUP.deleteUserGroupOnServer(GUISTATE_C.getUserGroup(), $('#singleModalInput').val(), function(result) {
                if (result.rc === "ok") {
                    //logout();
                }
                MSG.displayInformation(result, "MESSAGE_USER_GROUP_DELETED", result.message, GUISTATE_C.getUserGroup());
            });
        }
    }

  

    //Animate between forms in login modal
    function modalAnimate($oldForm, $newForm) {
        var $oldH = $oldForm.height();
        var $newH = $newForm.height();
        $divForms.css("height", $oldH);
        $oldForm.fadeToggle($modalAnimateTime, function() {
            $divForms.animate({
                height : $newH
            }, $modalAnimateTime, function() {
                $newForm.fadeToggle($modalAnimateTime);
            });
        });
    }

    function msgFade($msgId, $msgText) {
        $msgId.fadeOut($msgAnimateTime, function() {
            $(this).text($msgText).fadeIn($msgAnimateTime);
        });
    }

    //header change of the modal login
    function hederChange($oldHeder, $newHeder) {
        $oldHeder.addClass('hidden');
        $newHeder.removeClass('hidden');
    }

    /**
     * Resets the validation of every form in login modal
     * 
     */
    function resetForm() {
        $formLogin.validate().resetForm();
        $formLost.validate().resetForm();
        $formRegister.validate().resetForm();
    }

    /**
     * Clear input fields in login modal
     */
    function clearInputs() {
        $divForms.find('input').val('');
    }

    function initRegisterForm() {
        $formRegister.unbind('submit');
        $formRegister.onWrap('submit', function(e) {
            e.preventDefault();
            createUserToServer();
        });
        $("#registerUserGroup").text(Blockly.Msg["POPUP_REGISTER_USER"]);
        $("#register_user_group_btn").show()
        $formRegister.show();
        $('#div-login-forms').css('height', 'auto');
    }

    function initLoginModal() {
       
        $formLost.onWrap('submit', function(e) {
            e.preventDefault();
            userPasswordRecovery();
        });
        $formLogin.onWrap('submit', function(e) {
            e.preventDefault();
            login();
        });
        $formRegister.onWrap('submit', function(e) {
            e.preventDefault();
            createUserToServer();
        });

        $('#add_usergroup_btn').onWrap('click', function() {
            hederChange($h3Register, $h3Login)
            modalAnimate($formRegister, $formLogin);
            UTIL.setFocusOnElement($('#add_usergroup'));
        });
        
        validateRegisterUser();
    }

   
    /**
     * Initialize the login modal
     */
    function init() {
        var ready = $.Deferred();
        $.when(USER.clear(function(result) {
            UTIL.response(result);
        })).then(function() {
            $divForms = $('#div-login-forms');
            $formLogin = $('#login-form');
            $formLost = $('#lost-form');
            $formRegister = $('#register-form');
            $formUserPasswordChange = $('#change-user-password-form');
            $formSingleModal = $('#single-modal-form');

            $h3Login = $('#loginLabel');

            $('#iconDisplayLogin').onWrap('click', function() {
                showUserInfo();
            }, 'icon user click');

            initLoginModal();
            initUserPasswordChangeModal();
            LOG.info('init user forms');
            ready.resolve();
        });
        return ready.promise();
    }
    exports.init = init;

   
    function showDeleteUserGroupModal() {
        UTIL.showSingleModal(function() {
            $('#single-modal h3').text(Blockly.Msg["MENU_DELETE_USER_GROUP"]);
            $('#single-modal span').removeClass('typcn-pencil');
            $('#single-modal span').addClass('typcn-lock-closed');
        }, deleteUserOnServer, function() {
            $('#single-modal span').addClass('typcn-pencil');
            $('#single-modal span').removeClass('typcn-lock-closed');
        }, {
            rules : {
                singleModalInput : {
                    required : true
                }
            },
            errorClass : "form-invalid",
            errorPlacement : function(label, element) {
                label.insertAfter(element);
            },
            messages : {
                singleModalInput : {
                    required : jQuery.validator.format(Blockly.Msg["VALIDATION_FIELD_REQUIRED"])
                }
            }
        });
    }
    exports.showDeleteUserGroupModal = showDeleteUserGroupModal;

   
    function initValidationMessages() {
        validateRegisterUserGroup();
    }
    exports.initValidationMessages = initValidationMessages;
});
