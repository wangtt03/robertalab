define([ 'exports', 'log', 'message', 'util', 'group.model', 'guiState.controller', 'jquery', 'blocks', 'blocks-msg' ], function(exports, LOG, MSG, UTIL, GROUP,
        GUISTATE_C, $, Blockly) {

    var $divForms;
    var $formLogin;
    var $formLost;
    var $formRegister;
    //var $formGroupPasswordChange;
    var $formSingleModal;
    var $modalAnimateTime = 300;
    var $msgAnimateTime = 150;
    var $msgShowTime = 2000;

    /**
     * Create new group
     */
    function createUserToServer() {
        $formRegister.validate();
        if ($formRegister.valid()) {
            GROUPS.createUserToServer($("#registerGroupName").val(), function(result) {
                if (result.rc === "ok") {
                    $("#registerGroupName").val();
                    //$('#loginPassword').val($('#registerPass').val());
                }
                MSG.displayInformation(result, "", result.message);
            });
        }
    }

    /**
     * Update group
     */
    function updateGroupToServer() {
        $formRegister.validate();
        if ($formRegister.valid()) {
        	GROUPS.updateGroupToServer(GUISTATE_C.getGroupName(), $('#registerGroupName').val(), function(result) {
                if (result.rc === "ok") {
                	GROUPS.getUserFromServer(GUISTATE_C.getUserAccountName(), function(result) {
                        if (result.rc === "ok") {
                            GUISTATE_C.setLogin(result);
                        }
                    });
                }
                MSG.displayInformation(result, "", result.message);
            });
        }
    }

    /**
     * Update Group Password
     
    function updateUserPasswordOnServer() {
        restPasswordLink = $("#resetPassLink").val()
        $formUserPasswordChange.validate();
        if ($formUserPasswordChange.valid()) {
            if (restPasswordLink) {
                USER.resetPasswordToServer(restPasswordLink, $("#passNew").val(), function(result) {
                    if (result.rc === "ok") {
                        $("#change-user-password").modal('hide');
                        $("#resetPassLink").val(undefined);
                        // not to close the startup popup, if it is open
                        MSG.displayMessage(result.message, "TOAST", "");
                    } else {
                        MSG.displayInformation(result, "", result.message);
                    }
                });
            } else {
                USER.updateUserPasswordToServer(GUISTATE_C.getUserAccountName(), $('#passOld').val(), $("#passNew").val(), function(result) {
                    if (result.rc === "ok") {
                        $("#change-user-password").modal('hide');
                    }
                    MSG.displayInformation(result, "", result.message);
                });
            }

        }
    }*/

    /**
     * Get group from server
     */
    function getUserFromServer() {
    	GROUPS.getUserFromServer(GUISTATE_C.getGroupName(), function(result) {
            if (result.rc === "ok") {
                $("#registerGroupName").val(result.groupName);
            }
        });
    }

   

    /**
     * Update user password
     
    function userPasswordRecovery() {
        $formLost.validate();
        if ($formLost.valid()) {
            USER.userPasswordRecovery($('#lost_email').val(), GUISTATE_C.getLanguage(), function(result) {
                MSG.displayInformation(result, result.message, result.message);
            });
        }
    }*/

    /**
     * Delete group on server
     */
    function deleteGroupOnServer() {
        $formSingleModal.validate();
        if ($formSingleModal.valid()) {
        	GROUPS.deleteGroupOnServer(GUISTATE_C.getGroupName(), $('#singleModalInput').val(), function(result) {
                if (result.rc === "ok") {
                    logout();
                }
                MSG.displayInformation(result, "MESSAGE_GROUP_DELETED", result.message, GUISTATE_C.getGroupName());
            });
        }
    }


    function validateRegisterGroup() {
        $formRegister.removeData('validator');
        $.validator.addMethod("loginRegex", function(value, element) {
            return this.optional(element) || /^[a-z0-9\-\s]+$/i.test(value);
        }, "Group name must contain only letters, numbers, or dashes.");
        $formRegister.validate({
            rules : {
                registerAccountName : {
                    required : true,
                    maxlength : 15,
                    loginRegex : true
                },
                /*registerPass : {
                    required : true,
                    minlength : 6
                },
                registerPassConfirm : {
                    required : true,
                    equalTo : "#registerPass"
                },*/

            },
            errorClass : "form-invalid",
            errorPlacement : function(label, element) {
                label.insertAfter(element);
            },
            messages : {
                registerAccountName : {
                    required : Blockly.Msg["VALIDATION_FIELD_REQUIRED"],
                    maxlength : Blockly.Msg["ORA_GROUP_CREATE_ERROR_CONTAINS_SPECIAL_CHARACTERS"],
                    loginRegex : Blockly.Msg["ORA_GROUP_CREATE_ERROR_CONTAINS_SPECIAL_CHARACTERS"]
                }
                /*registerPass : {
                    required : Blockly.Msg["VALIDATION_FIELD_REQUIRED"],
                    minlength : Blockly.Msg["VALIDATION_PASSWORD_MIN_LENGTH"]
                },
                registerPassConfirm : {
                    required : Blockly.Msg["VALIDATION_FIELD_REQUIRED"],
                    equalTo : Blockly.Msg["SECOND_PASSWORD_EQUAL"]
                }*/

            }
        });
    }

    /*function validateUserPasswordChange() {
        $formUserPasswordChange.removeData('validator');
        $formUserPasswordChange.validate({
            rules : {
                passOld : "required",
                passNew : {
                    required : true,
                    minlength : 6
                },
                passNewRepeat : {
                    required : true,
                    equalTo : "#passNew"
                },
            },
            errorClass : "form-invalid",
            errorPlacement : function(label, element) {
                label.insertAfter(element);
            },
            messages : {
                passOld : {
                    required : Blockly.Msg["VALIDATION_FIELD_REQUIRED"]
                },
                passNew : {
                    required : Blockly.Msg["VALIDATION_FIELD_REQUIRED"],
                    minlength : Blockly.Msg["VALIDATION_PASSWORD_MIN_LENGTH"]
                },
                passNewRepeat : {
                    required : Blockly.Msg["VALIDATION_FIELD_REQUIRED"],
                    equalTo : Blockly.Msg["VALIDATION_SECOND_PASSWORD_EQUAL"]
                }
            }
        });
    }

    function validateLostPassword() {
        $formLost.removeData('validator');
        $formLost.validate({
            rules : {
                lost_email : {
                    required : true,
                    email : true
                }
            },
            errorClass : "form-invalid",
            errorPlacement : function(label, element) {
                label.insertAfter(element);
            },
            messages : {
                lost_email : {
                    required : Blockly.Msg["VALIDATION_FIELD_REQUIRED"],
                    email : Blockly.Msg["VALID_EMAIL_ADDRESS"]
                }
            }
        });
    }*/

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

    function initGroupCreationForm() {
        $formRegister.unbind('submit');
        $formRegister.onWrap('submit', function(e) {
            e.preventDefault();
            createUserToServer();
        });
        $("#registerGroup").text(Blockly.Msg["POPUP_REGISTER_GROUP"]);
        $("#registerGroupName").prop("disabled", false);
        //$("#fgRegisterPass").show()
        //$("#fgRegisterPassConfirm").show()
        //$("#showChangeUserPassword").addClass('hidden');
        $("#register_login_btn").show()
        $formLogin.hide()
        $formRegister.show();
        $('#div-login-forms').css('height', 'auto');
    }

    function initLoginModal() {
        $('#login-user').onWrap('hidden.bs.modal', function() {
            initRegisterForm();
            resetForm();
            clearInputs();
        });

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

        // Login form change between sub-form
        $('#group_register_btn').onWrap('click', function() {
            hederChange($h3Login, $h3Register)
            modalAnimate($formLogin, $formRegister)
            UTIL.setFocusOnElement($('#registerGroupName'));
        });
       
        validateRegisterGroup();
        //validateLostPassword();
    }


   
    function showGroupDataForm() {
        getGroupFromServer();
        $formRegister.unbind('submit');
        $formRegister.onWrap('submit', function(e) {
            e.preventDefault();
            updateUserToServer();
        });
        $("#registerGroup").text("OK");
        $("#registerGroupName").prop("disabled", true);
        $("#groupInfoLabel").removeClass('hidden');
        $("#loginLabel").addClass('hidden');
        $("#registerInfoLabel").addClass('hidden');
        $("#register_group_btn").hide();
        $('#div-login-forms').css('height', 'auto');
        $("#login-group").modal('show');
    }
    exports.showGroupDataForm = showGroupDataForm;

    function showDeleteGroupModal() {
        UTIL.showSingleModal(function() {
            $('#singleModalInput').attr('type', 'password');
            $('#single-modal h3').text(Blockly.Msg["MENU_DELETE_GROUP"]);
            //$('#single-modal label').text(Blockly.Msg["POPUP_PASSWORD"]);
            $('#single-modal span').removeClass('typcn-pencil');
            $('#single-modal span').addClass('typcn-lock-closed');
        }, deleteGroupOnServer, function() {
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
    exports.showDeleteGroupModal = showDeleteGroupModal;

    /**
     * Show group info
     */
    function showGroupInfo() {
        $("#loggedIn").text(GUISTATE_C.getGroupName());
        if (GUISTATE_C.isUserLoggedIn()) {
            $("#popup_groupname").text(Blockly.Msg["POPUP_GROUPNAME"] + ": ");
        } else {
            $("#popup_groupname").text(Blockly.Msg["POPUP_GROUPNAME_LOGOFF"]);
        }
        $("#show-state-info").modal("show");
    }
    exports.showGroupInfo = showGroupInfo;

    /*function showResetPassword(target) {
        USER.checkTargetRecovery(target, function(result) {
            if (result.rc !== 'ok') {
                $('#passOld').val(target);
                $('#resetPassLink').val(target);
                $('#grOldPassword').hide();
                $('#change-user-password').modal('show');
            } else {
                result.rc = 'error'
                MSG.displayInformation(result, "", result.message);
            }
        });
    }
    exports.showResetPassword = showResetPassword;*/

    function initValidationMessages() {
        validateRegisterUser();
    }
    exports.initValidationMessages = initValidationMessages;
});
